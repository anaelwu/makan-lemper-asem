package com.imb.tbs.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.iapps.libs.helpers.NotificationGenerator;
import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityLogin;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanNotif;
import com.imb.tbs.objects.BeanProfile;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

public class GcmListener extends GcmListenerService {
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification(data);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(final Bundle message) {
        Intent intent = new Intent(this, ActivityLogin.class);
        intent.putExtra(ActivityLogin.PUSH_NOTIF, message);

        new NotificationGenerator(this, intent) {
            @Override
            public String content() {
                return message.getString(Keys.PUSH_TITLE);
            }

            @Override
            public int color() {
                return R.color.tbs_base_color;
            }

            @Override
            public int iconLollipop() {
                return R.drawable.ic_launcher_white;
            }
        }.build();

        saveNotif(message);
    }

    private void saveNotif(Bundle message) {
        try {
            BeanProfile profile = null;
            if (Preference.getInstance(this).getBoolean(Preference.IS_LOGGED_IN))
                profile = Converter.toProfile(Preference.getInstance(this).getString(
                        Preference.USER_DETAILS));

            JSONArray jArr = null;
            String text = Preference.getInstance(getApplicationContext()).getString(Preference.PUSH_NOTIF);
            if (Helper.isEmpty(text))
                jArr = new JSONArray();
            else
                jArr = new JSONArray(text);

            String url;
            if (profile == null)
                url = message.getString(Keys.PUSH_URL);
            else
                url = message.getString(Keys.PUSH_URL) + "?" + Keys.USER_ID + "=" +
                        profile.getId();

            jArr.put(new BeanNotif().setTime(DateTime.now())
                                    .setCampId(message.getString(Keys.PUSH_ID))
                                    .setTitle(message.getString(Keys.PUSH_TITLE))
                                    .setUrl(url)
                                    .parseToText());

            Preference.getInstance(getApplicationContext()).setString(Preference.PUSH_NOTIF, jArr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}