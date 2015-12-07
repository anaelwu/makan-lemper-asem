package com.imb.tbs.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;

import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentWebview
        extends BaseFragmentTbs {
    @InjectView(R.id.wv)
    private WebView         wv;
    @InjectView(R.id.ld)
    private LoadingCompound ld;
    private String          title = "", url;
    private int    resTitle;
    private String campId;

    public String getCampId() {
        return campId;
    }

    public FragmentWebview setCampId(String campId) {
        this.campId = campId;
        return this;
    }

    public FragmentWebview(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public FragmentWebview(int resTitle, String url) {
        this.resTitle = resTitle;
        this.url = url;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        if (resTitle > 0) {
            setTitle(resTitle);
        } else {
            setTitle(title);
        }

        loadContent();

        if (!Helper.isEmpty(campId)) {
            new HTTPTbs(this, false) {
                @Override
                public void onSuccess(JSONObject j) {
                    // do nothing
                }

                @Override
                public String url() {
                    return Api.OPEN_RATE;
                }
            }.setPostParams("app_id", Constants.APP_ID)
             .setPostParams("key", Constants.APP_KEY)
             .setPostParams("camp_id", this.campId)
             .setPostParams("device_id",
                            Preference.getInstance(getActivity()).getString(Preference.DEVICE_TOKEN)).execute();
        }
    }

    @Override
    public int setMenuLayout() {
        return R.menu.web;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    public void loadContent() {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setPluginState(PluginState.ON);
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebViewClient(new Callback());
        wv.loadUrl(url);
    }

    // ================================================================================
    // Menu
    // ================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                wv.reload();
                ld.showLoading();
                break;

            case R.id.menu_browser:
                Helper.intentWeb(getActivity(), url);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // ================================================================================
    // Callback Setting
    // ================================================================================
    private class Callback
            extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ld.showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ld.hide(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
}
