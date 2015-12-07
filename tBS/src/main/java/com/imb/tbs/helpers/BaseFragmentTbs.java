package com.imb.tbs.helpers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ScrollView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.iapps.libs.generics.GenericFragment;
import com.imb.tbs.activities.ActivityHome;
import com.imb.tbs.activities.ActivityLogin;
import com.imb.tbs.activities.ActivityPush;
import com.imb.tbs.analytics.AnalyticsApplication;
import com.imb.tbs.objects.BeanProfile;

public abstract class BaseFragmentTbs
        extends GenericFragment implements OnScrollListener,
                                           OnScrollChangedListener, OnTouchListener {
    private ScrollView       sv;
    private ViewTreeObserver observer;
    private int mVisibleCount, mVisibleFirst, mPage = 1;
    private boolean doPagination;
    private Tracker mTracker;
    public String  title = "";
    public boolean doLog = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Log
        if (doLog) {
            String name = "";
            if ((getToolbar() == null || getToolbar().getTitle() == null ||
                    Helper.isEmpty(getToolbar().getTitle().toString()) || getToolbar().getTitle().length() == 0) &&
                    Helper.isEmpty(title)) {
                Class<?> enclosingClass = getClass().getEnclosingClass();
                if (enclosingClass != null) {
                    name = enclosingClass.getName();
                } else {
                    name = getClass().getName();
                }
            } else if (!Helper.isEmpty(title)) {
                name = title;
            } else {
                name = getToolbar().getTitle().toString();
            }

            name = "Android - " + name;
            log(name);

            AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName(name);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public BaseActivityTbs getHomeTbs() {
        return (BaseActivityTbs) getActivity();
        // return null;
    }

    public ActivityLogin getLoginActivity() {
        return (ActivityLogin) getActivity();
    }

    public ActivityHome getHomeActivity() {
        return (ActivityHome) getActivity();
    }

    public void setFragment(Fragment frag) {
        if (getHomeTbs() != null)
            getHomeTbs().setFragment(frag);
    }

    public void onBackPressed() {
        if (getHomeTbs() != null)
            getHomeTbs().onBackPressed();
    }

    public Preference getPref() {
        if (getHomeTbs() != null)
            return Preference.getInstance(getHomeTbs());

        return null;
    }

    public BeanProfile getProfile() {
        return getHomeActivity().getProfile();
    }

    @Override
    public boolean isDebugging() {
        return Constants.IS_DEBUGGING;
    }

    @Override
    public int setLayout() {
        return 0;
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {

    }

    // ================================================================================
    // Toolbar
    // ================================================================================
    public Toolbar getToolbar() {
        if (getActivity() instanceof ActivityHome) {
            if (getHomeActivity() != null)
                return getHomeActivity().getToolbar();
        } else if (getActivity() instanceof ActivityLogin) {
            return ((ActivityLogin) getActivity()).getToolbar();
        } else if (getActivity() instanceof ActivityPush) {
            return ((ActivityPush) getActivity()).getToolbar();
        }

        return null;
    }

    public void hideToolbar() {
        setToolbarOpacity(0);
        if (getActivity() instanceof ActivityHome)
            getHomeActivity().toolbarBg.setVisibility(View.GONE);
        else
            getLoginActivity().toolbarBg.setVisibility(View.GONE);
    }

    public void setToolbarOpacity(int opacity) {
        // Max opacity will be 255, as per android's code
        if (opacity > 255)
            opacity = 255;
        Toolbar toolbar = getToolbar();
        if (toolbar != null)
            toolbar.getBackground().setAlpha(opacity);
    }

    public void setToolbarColor(int color) {
        setToolbarOpacity(0);

        if (color > 0) {
            if (getActivity() instanceof ActivityHome) {
                getHomeActivity().toolbarBg.setBackgroundResource(color);
                getHomeActivity().toolbarBg.setVisibility(View.VISIBLE);
            } else {
                getLoginActivity().toolbarBg.setBackgroundResource(color);
                getLoginActivity().toolbarBg.setVisibility(View.VISIBLE);
            }
        }
    }

    public void resetToolbarColor() {
        setToolbarOpacity(0);
        if (getActivity() instanceof ActivityHome)
            getHomeActivity().toolbarBg.setVisibility(View.VISIBLE);
        else
            getLoginActivity().toolbarBg.setVisibility(View.VISIBLE);
    }

    // ================================================================================
    // Title
    // ================================================================================
    @Override
    public void setTitle(int resTitle) {
        getToolbar().setTitle(resTitle);
    }

    @Override
    public void setTitle(String title) {
        getToolbar().setTitle(title);
    }

    // ================================================================================
    // Logging
    // ================================================================================
    @Override
    public void log(String text) {
        if (Constants.IS_DEBUGGING)
            Log.d(Constants.LOG, text);
    }

    // ================================================================================
    // Scroll Listener ListView
    // ================================================================================
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && paginationCount() <= mVisibleCount + mVisibleFirst && pagination()) {
            paginationPage(mPage + 1);
            onPagination(paginationPage());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        this.mVisibleCount = visibleItemCount;
        this.mVisibleFirst = firstVisibleItem;

        if (transparentActionbar()) {
            View v = view.getChildAt(0);
            int scrollY = (v == null) ? 0 : -v.getTop() + view.getFirstVisiblePosition()
                    * v.getHeight();

            setToolbarOpacity(scrollY);
        }
    }

    /**
     * If this sets to true, don't forget to implement pagination count, paginationPage, paginationLimit, and
     * onPagination
     *
     * @return
     */
    public boolean pagination() {
        return this.doPagination;
    }

    public int paginationCount() {
        return 0;
    }

    public void paginationPage(int page) {
        this.mPage = page;

        if (paginationCount() < page * paginationLimit()) {
            this.doPagination = false;
        }
    }

    public int paginationPage() {
        return this.mPage;
    }

    public int paginationLimit() {
        return Constants.LIMIT;
    }

    public void onPagination(int page) {
    }

    public boolean transparentActionbar() {
        return false;
    }

    // ================================================================================
    // Scroll Listener ScrollView
    // ================================================================================
    public void setScrollListener(ScrollView sv) {
        // Don't use this anymore
        // this.sv = sv;
        // this.sv.setScrollBarStyle(0);
        //
        // sv.setOnTouchListener(this);
    }

    @Override
    public void onScrollChanged() {
        setToolbarOpacity(sv.getScrollY());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (observer == null) {
            observer = sv.getViewTreeObserver();
            observer.addOnScrollChangedListener(this);
        } else if (!observer.isAlive()) {
            observer.removeOnScrollChangedListener(this);
            observer = sv.getViewTreeObserver();
            observer.addOnScrollChangedListener(this);
        }

        return false;
    }
}
