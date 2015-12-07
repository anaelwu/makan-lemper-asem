package com.imb.tbs.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;

import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityLogin;
import com.imb.tbs.activities.ActivityPush;
import com.imb.tbs.adapters.AdapterSplash;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanSplash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

public class FragmentSplash
        extends BaseFragmentTbs {
    @InjectView(R.id.gv)
    private GridView      gv;
    private AdapterSplash adapter;
    private             ArrayList<BeanSplash> alSplash  = new ArrayList<BeanSplash>();
    public static final int                   TAG_LOGIN = 1, TAG_TNC = 2, TAG_CHANGE_ACTIVITY = 3;
    private boolean doPush;

    public FragmentSplash(boolean doPush) {
        this.doPush = doPush;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Splash Screen";
        initSplash();

        if (getPref().getBoolean(Preference.IS_LOGGED_IN)) {
            startTimer(TAG_CHANGE_ACTIVITY);
        } else {
            if (Helper.isEmpty(getPref().getString(Preference.SETTINGS))) {
                loadSettings();
            } else if (getPref().getBoolean(Preference.IS_ACCEPT_TNC)) {
                startTimer(TAG_LOGIN);
            } else {
                startTimer(TAG_TNC);
            }
        }
    }

    public void startTimer(final int tag) {
        new CountDownTimer(Constants.TIMER_SPLASH * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                switch (tag) {
                    case TAG_LOGIN:
                        clearFragment();
                        setFragment(new FragmentPrelogin());
                        break;

                    case TAG_TNC:
                        clearFragment();
                        setFragment(new FragmentTnc());
                        break;

                    case TAG_CHANGE_ACTIVITY:
                        getLoginActivity().changeActivity(true);
                        break;
                }

                if (doPush) {
                    openPushNotif();
                }
            }
        }.start();
    }

    public void openPushNotif(){
        Intent pushIntent = new Intent(getActivity(), ActivityPush.class);
        pushIntent.putExtras(getActivity().getIntent().getBundleExtra(ActivityLogin.PUSH_NOTIF));
        startActivity(pushIntent);
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    // ================================================================================
    // Splash
    // ================================================================================
    public void initSplash() {
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#5a99b0")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_1).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#b5e0f8")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#3e8095")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#b5e0f8")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#9ed8db")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#6fc6b5")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_2).setImg(true));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_3).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#b1cc94")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_4).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#63903f")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#989936")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_5).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#cccb31")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#ffcc67")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#cd6632")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#cb9832")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_6).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#ffcd33")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_7).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#961b1e")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_8).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#c75c28")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#c91b74")));
        alSplash.add(new BeanSplash().setContent(R.drawable.img_9).setImg(true));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#9c2059")));
        alSplash.add(new BeanSplash().setContent(Color.parseColor("#b02074")));

        adapter = new AdapterSplash(getActivity(), alSplash);
        gv.setAdapter(adapter);

        // Disable scroll
        gv.setVerticalScrollBarEnabled(false);
        gv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }

        });
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    public void loadSettings() {
        new HTTPTbs(this, false) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.SETTINGS, j.getString(Keys.RESULTS));
                    setFragment(new FragmentTnc());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String url() {
                return Api.SETTINGS;
            }
        }.execute();
    }

    public void loadHome() {
        new HTTPTbs(this, false) {
            @Override
            public String url() {
                return Api.GET_CAROUSEL;
            }

            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.CAROUSEL, j.getString(Keys.RESULTS));
                    getLoginActivity().changeActivity(true);

                    if (doPush) {
                        openPushNotif();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

}
