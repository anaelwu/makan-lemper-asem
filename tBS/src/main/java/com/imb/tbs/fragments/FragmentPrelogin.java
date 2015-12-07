package com.imb.tbs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.Response;
import com.iapps.libs.helpers.FacebookLogin;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentPrelogin
        extends BaseFragmentTbs {
    @InjectView(R.id.btnFb)
    private Button btnFb;
    @InjectView(R.id.btnLogin)
    private Button btnLogin;
    @InjectView(R.id.btnRegister)
    private Button btnRegister;
    public static final int TAG_FB = 1, TAG_LOGIN = 2, TAG_REGISTER = 3;
    private FacebookLogin fb;
    private Response      response;

    @Override
    public int setLayout() {
        return R.layout.fragment_prelogin;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Pre login";
        btnFb.setTag(TAG_FB);
        btnLogin.setTag(TAG_LOGIN);
        btnRegister.setTag(TAG_REGISTER);

        btnFb.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public int setMenuLayout() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // ================================================================================
    // Listener
    // ================================================================================
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch ((Integer) v.getTag()) {
            case TAG_FB:
                fb = new FacebookLogin(getString(R.string.fb_app_id), this) {
                    @Override
                    public void onFbLoginSuccess(Response response) {
                        setResponse(response);
                        login(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID).toString());
                    }
                };
                fb.execute();
                break;

            case TAG_LOGIN:
                setFragment(new FragmentLogin());
                break;

            case TAG_REGISTER:
                setFragment(new FragmentSignup());
                break;
        }
    }

    // @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fb.onActivityResult(requestCode, resultCode, data);
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    public void login(String fbId) {
        if(fbId == null) {
            Helper.showInternetError(getActivity());
            return;
        }

        new HTTPTbs(this, true) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
                    getLoginActivity().changeActivity(true);
                } catch (JSONException e) {
                    Helper.showAlert(getActivity(), R.string.iapps__network_error);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, JSONObject j) {
                setFragment(new FragmentSignup(response));
            }

            @Override
            public String url() {
                return Api.LOGIN_WITH_FB;
            }
        }.setPostParams(Keys.USER_FB_ID, fbId).execute();
    }
}
