package com.imb.tbs.helpers;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.iapps.libs.helpers.HTTPAsyncImb;
import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.views.LoadingCompound;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Random;

public abstract class HTTPTbs
        extends HTTPAsyncImb {
    public HTTPTbs(Fragment frag, boolean displayProgress) {
        super(frag, displayProgress);
    }

    public HTTPTbs(Fragment frag, LoadingCompound ld) {
        super(frag, ld);
    }

    @Override
    public HTTPAsyncTask execute() {
        setGetParams("token", generateToken());

        if (getFragment() != null) {
            Location location = ((BaseActivityTbs) getFragment().getActivity()).mLastLocation;
            if (location != null) {
                setGetParams(Keys.LATITUDE, Double.toString(location.getLatitude()));
                setGetParams(Keys.LONGITUDE, Double.toString(location.getLongitude()));
            }
        }
        Log.d("tes", this.getUrl().toString());
        return super.execute();
    }

    private String generateToken() {
        String[] arrName   = {"acel", "jhomponk", "budi", "cikun", "abui"};
        int      randIndex = new Random().nextInt(5);
        String token = arrName[randIndex] +
                DateTime.now().toDateTime(DateTimeZone.UTC).toString(Constants.DATE_YMD);
        token = Helper.encryptMd5(token);

        return token;
    }
}
