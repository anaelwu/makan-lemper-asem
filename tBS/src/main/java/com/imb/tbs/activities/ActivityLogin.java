package com.imb.tbs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.fragments.FragmentPrelogin;
import com.imb.tbs.fragments.FragmentSplash;
import com.imb.tbs.helpers.BaseActivityTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;

import roboguice.inject.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ActivityLogin
        extends BaseActivityTbs {
    @InjectView(R.id.imgBg)
    private ImageViewLoader imgBg;
    @InjectView(R.id.toolbar)
    private Toolbar         toolbar;
    @InjectView(R.id.toolbarBg)
    public  View            toolbarBg;
    public static String  PUSH_NOTIF = "pushNotif";
    public static boolean isActive   = false;

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                              .setDefaultFontPath(getString(R.string.font_default))
                                              .setFontAttrId(R.attr.fontPath)
                                              .build());

        setContentView(R.layout.activity_login);

        setActionBar();
        imgBg.loadImage(R.drawable.background);

        setContainerId(R.id.fl);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Preference.IS_LOGGED_IN)) {
            setFragment(new FragmentPrelogin());
        } else {
            if (getIntent().getBundleExtra(PUSH_NOTIF) != null) {
                if (ActivityLogin.isActive || ActivityHome.isActive) {
                    Intent pushIntent = new Intent(this, ActivityPush.class);
                    pushIntent.putExtras(this.getIntent().getBundleExtra(ActivityLogin.PUSH_NOTIF));
                    startActivity(pushIntent);
                    this.finish();
                    return;
                }
            }

            setFragment(new FragmentSplash(getIntent().getBundleExtra(PUSH_NOTIF) != null));
        }

        Log.d(Constants.LOG, Helper.getHashKey(this));
    }

    public void changeActivity(boolean saveLogin) {
        if (saveLogin)
            Preference.getInstance(this).setBoolean(Preference.IS_LOGGED_IN, true);

        this.startActivity(new Intent(this, ActivityHome.class));
        this.finish();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setVisibility(View.VISIBLE);
        toolbar.getBackground().setAlpha(0);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    // @Override
    // protected void attachBaseContext(Context newBase) {
    // super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    // }
}
