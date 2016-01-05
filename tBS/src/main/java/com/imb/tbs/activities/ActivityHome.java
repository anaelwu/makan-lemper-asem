package com.imb.tbs.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.iapps.libs.helpers.BaseHelper.ConfirmListener;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterDrawer;
import com.imb.tbs.fragments.FragmentCampaign;
import com.imb.tbs.fragments.FragmentConnect;
import com.imb.tbs.fragments.FragmentContact;
import com.imb.tbs.fragments.FragmentHome;
import com.imb.tbs.fragments.FragmentInbox;
import com.imb.tbs.fragments.FragmentNews;
import com.imb.tbs.fragments.FragmentProductCategory;
import com.imb.tbs.fragments.FragmentProductScan;
import com.imb.tbs.fragments.FragmentProfile;
import com.imb.tbs.fragments.FragmentRewards;
import com.imb.tbs.fragments.FragmentStore;
import com.imb.tbs.fragments.FragmentWishlist;
import com.imb.tbs.helpers.BaseActivityTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanDrawer;
import com.imb.tbs.objects.BeanProfile;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ActivityHome
        extends BaseActivityTbs implements OnItemClickListener {
    @InjectView(R.id.lvDrawer)
    private ListView        lv;
    @InjectView(R.id.toolbar)
    private Toolbar         toolbar;
    @InjectView(R.id.drawer_layout)
    private DrawerLayout    drawer;
    @InjectView(R.id.imgBg)
    private ImageViewLoader imgBg;
    @InjectView(R.id.toolbarBg)
    public  View            toolbarBg;
    private TextView        tvStatus, tvName;
    private int curIndex = 1;
    public  ActionBarDrawerToggle drawerToggle;
    private AdapterDrawer         adapter;
    private ArrayList<BeanDrawer> alDrawer = new ArrayList<BeanDrawer>();
    private BeanProfile     profile;
    private ImageViewLoader img;
    public static final int     TAG_CAMPAIGN = 1;
    public static final int     TAG_REWARDS  = 2;
    public static final int     TAG_PRODUCT  = 3;
    public static final int     TAG_WISHLIST = 4;
    public static final int     TAG_STORE    = 5;
    public static final int     TAG_SCAN     = 6;
    public static final int     TAG_CONTACT  = 7;
    public static final int     TAG_PROFILE  = 8;
    public static final int     TAG_CONNECT  = 9;
    public static final int     TAG_NEWS     = 10;
    public static final int     TAG_INBOX    = 11;
    public static final int     TAG_LOGOUT   = 99;
    public static final int     TAG_HOME     = 100;
    public static       boolean isActive     = false;

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
        setContentView(R.layout.activity_home);

        setActionBar();
        getUser();
        initDrawer();

        setContainerId(R.id.flFragment);
        setFragment(new FragmentHome(Preference.getInstance(this).getBoolean(Preference.IS_LOGGED_IN)));

        imgBg.loadImage(R.drawable.background);
    }

    // @Override
    // protected void attachBaseContext(Context newBase) {
    // super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    // }

    public void getUser() {
        if (Preference.getInstance(this).getBoolean(Preference.IS_LOGGED_IN))
            profile = Converter.toProfile(Preference.getInstance(this).getString(
                    Preference.USER_DETAILS));
    }

    // ================================================================================
    // ActionBar
    // ================================================================================
    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getBackground().setAlpha(255);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name,
                                                 R.string.app_name);
        drawer.setDrawerListener(drawerToggle);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setFragment(Fragment frag) {
        if (frag == null)
            return;

        super.setFragment(frag);
        toolbar.getBackground().setAlpha(0);
        drawer.closeDrawers();
    }

    // ================================================================================
    // Drawer functions
    // ================================================================================
    private void initDrawer() {
        alDrawer.add(new BeanDrawer(TAG_HOME, getString(R.string.home))
                             .setFragment(new FragmentHome()));
        alDrawer.add(new BeanDrawer(TAG_CAMPAIGN, getString(R.string.campaign))
                             .setFragment(new FragmentCampaign()));
        alDrawer.add(new BeanDrawer(TAG_REWARDS, getString(R.string.rewards))
                             .setFragment(new FragmentRewards()));
        alDrawer.add(new BeanDrawer(TAG_NEWS, getString(R.string.news))
                             .setFragment(new FragmentNews()));
        alDrawer.add(new BeanDrawer(TAG_PRODUCT, getString(R.string.product))
                             .setFragment(new FragmentProductCategory()));
        alDrawer.add(new BeanDrawer(TAG_WISHLIST, getString(R.string.wishlist))
                             .setFragment(new FragmentWishlist()));
        alDrawer.add(new BeanDrawer(TAG_SCAN, getString(R.string.scan_product))
                             .setFragment(new FragmentProductScan()));
        alDrawer.add(new BeanDrawer(TAG_STORE, getString(R.string.store_locators))
                             .setFragment(new FragmentStore()));
        alDrawer.add(new BeanDrawer(TAG_CONNECT, getString(R.string.connect))
                             .setFragment(new FragmentConnect()));
        alDrawer.add(new BeanDrawer(TAG_CONTACT, getString(R.string.contact_us))
                             .setFragment(new FragmentContact()));
        alDrawer.add(new BeanDrawer(TAG_INBOX, getString(R.string.inbox)).setFragment(new FragmentInbox()));
        // alDrawer.add(new BeanDrawer(TAG_LOGOUT, getString(R.string.logout)).setFragment(null));

        adapter = new AdapterDrawer(this, alDrawer);

        initHeader();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    public void resetIndex() {
        this.curIndex = -1;
    }

    public void clickDrawer(int id) {
        int index = getItemById(id);
        // Cater for the header
        // index += 1;

        if (getProfile() != null)
            curIndex = index + 1;
        else
            curIndex = index;

        setFragment(alDrawer.get(index).getFragment());
        // lv.performItemClick(
        // lv.getAdapter().getView(index, null, null),
        // index, lv.getAdapter().getItemId(index));
    }

    private int getItemById(int id) {
        for (int i = 0; i < alDrawer.size(); i++) {
            if (alDrawer.get(i).getId() == id)
                return i;
        }

        return 0;
    }

    private void initHeader() {
        if (getProfile() == null) {
            Preference.getInstance(this).setBoolean(Preference.IS_LOGGED_IN, false);
            return;
        }

        View v = getLayoutInflater().inflate(R.layout.cell_drawer_header, null);
        img = ((ImageViewLoader) v.findViewById(R.id.imgProfile));
        img.loadImage(profile.getImage(), true);
        img.setImageOverlay(0);
        tvName = ((TextView) v.findViewById(R.id.tvName));
        tvStatus = ((TextView) v.findViewById(R.id.tvStatus));
        updateUser();
        lv.addHeaderView(v);
    }

    public void updateUser() {
        getUser();
        tvName.setText(profile.getFullName());
        tvStatus.setText(getProfile().getStatus());
        img.loadImage(profile.getImage(), R.drawable.ic_person, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (curIndex == position) {
            drawer.closeDrawers();
            return;
        }

        curIndex = position;

        if (getProfile() != null) {
            if (position == 0) {
                clearFragment();
                setFragment(new FragmentProfile());
                drawer.closeDrawers();
                return;
            } else
                position -= 1;
        }

        if (alDrawer.get(position).getFragment() != null) {
            clearFragment();
            if (position != 0)
                // popBackstack();
                setFragment(alDrawer.get(position).getFragment());
        } else {
            switch (alDrawer.get(position).getId()) {
                case TAG_LOGOUT:
                    logout();
                    break;
            }
        }
    }

    @Override
    public void clearFragment() {
        // Clear all stacks, except for the 1st fragment (home)
        for (int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void logout() {
        clearUserData();
        changeActivity();
    }

    public void changeActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Preference.IS_LOGGED_IN, true);
        Intent mIntent = new Intent(this, ActivityLogin.class);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        this.finish();
    }

    public void clearUserData() {
        Preference.getInstance(this).setString(Preference.USER_DETAILS, "");
        Preference.getInstance(this).setBoolean(Preference.IS_LOGGED_IN, false);
    }

    public BeanProfile getProfile() {
        return this.profile;
    }

    // ================================================================================
    // Event Listeners
    // ================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        toolbar.getBackground().setAlpha(0);

        if (drawer.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            drawer.closeDrawers();
            return;
        }
        List<Fragment> list = getSupportFragmentManager().getFragments();
        Fragment       frag = list.get(list.size() - 1);
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1 || frag instanceof FragmentHome) {
            Helper.confirm(this, R.string.exit_message, new ConfirmListener() {
                @Override
                public void onYes() {
                    ActivityHome.this.finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
