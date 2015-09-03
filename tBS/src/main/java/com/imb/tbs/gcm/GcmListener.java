package com.imb.tbs.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.google.android.gms.gcm.GcmListenerService;
import com.iapps.libs.helpers.NotificationGenerator;
import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityLogin;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;

public class GcmListener extends GcmListenerService {
    private static final String TAG         = "GcmListener";
    public static final  String INTENT_KEY  = "intentkey";
    public static final  String INTENT_DATA = "intentdata";
    public static final  int    INTENT_WEB  = 1;

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
        if (!Helper.isEmpty(message.getString(Keys.PUSH_CONTENT)) &&
                Patterns.WEB_URL.matcher(message.getString(Keys.PUSH_CONTENT)).matches()) {
            intent.putExtra(INTENT_KEY, INTENT_WEB);
            intent.putExtra(INTENT_DATA, message);
        }

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
    }
}