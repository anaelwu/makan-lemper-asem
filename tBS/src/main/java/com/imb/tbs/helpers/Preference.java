package com.imb.tbs.helpers;

import android.content.Context;

import com.iapps.libs.helpers.BasePreference;

public class Preference
        extends BasePreference {
    private final       String PREF_NAME       = "tbsindonesia";
    public static final String USER_DETAILS    = "userdetails";
    public static final String SETTINGS        = "settings";
    public static final String DEVICE_TOKEN    = "devicetoken";
    public static final String IS_LOGGED_IN    = "isloggedin";
    public static final String IS_ACCEPT_TNC   = "isaccepttnc";
    public static final String CAROUSEL        = "carousel";
    public static final String PRODUCT_LAYERS  = "productlayers";
    public static final String PRODUCT_DETAILS = "productdetails";
    public static final String PRODUCT_ORDER   = "productorder";
    public static final String STC_LIMIT       = "stclimit";
    public static final String PUSH_NOTIF      = "pushnotif";
    private static Preference pref;

    public static Preference getInstance(Context context) {
        if (pref == null) {
            pref = new Preference(context);
        }

        return pref;
    }

    private Preference(Context context) {
        this.context = context;
    }

    @Override
    public String setPrefName() {
        return PREF_NAME;
    }

}
