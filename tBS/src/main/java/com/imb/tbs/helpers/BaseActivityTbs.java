package com.imb.tbs.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.location.LocationServices;
import com.iapps.libs.generics.GenericActivity;
import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.objects.Response;
import com.imb.tbs.R;
import com.imb.tbs.gcm.GcmRegistration;
import com.imb.tbs.objects.BeanProfile;

import java.io.IOException;

public class BaseActivityTbs
        extends GenericActivity implements
                                GoogleApiClient.ConnectionCallbacks,
                                GoogleApiClient.OnConnectionFailedListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // Location
    GoogleApiClient mGoogleApiClient;
    Location        mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerGcm();
        getToken();
        buildGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                                                                 new IntentFilter(
                                                                         GcmRegistration.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void registerGcm() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(GcmRegistration.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d("GCM Token : ", "Sent");
                } else {
                    Log.d("GCM Token : ", "Error");
                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to getToken this application with GCM.
            Intent intent = new Intent(this, GcmRegistration.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                                                      PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("Google play service", "this device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    private void getToken() {
        AsyncTask execute = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    InstanceID instanceID = InstanceID.getInstance(BaseActivityTbs.this);
                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                                                       GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Preference.getInstance(BaseActivityTbs.this).setString(Preference.DEVICE_TOKEN, token);

                    return token;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (o instanceof String && !Helper.isEmpty((String) o)) {
                    Log.d("Token : ", (String) o);
                    register((String) o);
                }
                Log.d("Token : ", "fail");
            }
        };
        execute.execute();
    }

    private void register(String token) {
        HTTPAsyncTask register = new HTTPAsyncTask() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(Response response) {
                if (response != null)
                    Log.d("Register Device id", response.getContent().toString());
            }
        };
        register.setUrl(Api.PUSH_NOTIF);
        BeanProfile profile = Converter.toProfile(Preference.getInstance(this).getString(Preference.USER_DETAILS));
        if (profile != null && profile.getId() > 0)
            register.setPostParams(Keys.PUSH_ACC, Integer.toString(profile.getId()));
        register.setPostParams(Keys.PUSH_TYPE, "android");
        register.setPostParams(Keys.PUSH_DEVICE, token);
        register.execute();
    }

    // ================================================================================
    // Location
    // ================================================================================
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
