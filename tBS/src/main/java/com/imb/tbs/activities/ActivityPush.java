package com.imb.tbs.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.imb.tbs.R;
import com.imb.tbs.fragments.FragmentWebview;
import com.imb.tbs.helpers.BaseActivityTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by marcelsantoso.
 * <p/>
 * 9/4/15
 */
public class ActivityPush extends BaseActivityTbs {
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        setActionBar();

        setContainerId(R.id.flPush);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            setFragment(
                    new FragmentWebview(extras.getString(Keys.PUSH_TITLE), extras.getString(Keys.PUSH_URL)).setCampId(
                            extras.getString(Keys.PUSH_ID)));
        else {
            Helper.showInternetError(this);
            onBackPressed();
        }

    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getBackground().setAlpha(255);

        toolbar.setVisibility(View.VISIBLE);
        toolbar.getBackground().setAlpha(0);
    }

    public Toolbar getToolbar() {
        return this.toolbar;
    }

    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        Fragment       frag = list.get(list.size() - 1);
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1 || frag instanceof FragmentWebview) {
            ActivityPush.this.finish();
        } else {
            super.onBackPressed();
        }
    }
}
