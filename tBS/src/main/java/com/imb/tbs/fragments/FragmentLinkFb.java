package com.imb.tbs.fragments;

import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iapps.libs.helpers.FacebookLogin;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProfile;

public class FragmentLinkFb
	extends BaseFragmentTbs {

	@InjectView(R.id.btnLink)
	private Button	btnFb;
	@InjectView(R.id.btnSkip)
	private Button	btnSkip;

	public static final int	TAG_FB	= 1, TAG_SKIP = 2;
	private FacebookLogin	fb;

	@Override
	public int setLayout() {
		return R.layout.fragment_link_fb;
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		btnFb.setTag(TAG_FB);
		btnSkip.setTag(TAG_SKIP);

		btnFb.setOnClickListener(this);
		btnSkip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch ((Integer) v.getTag()) {
		case TAG_FB:
			fb = new FacebookLogin(getString(R.string.fb_app_id), this) {

				@Override
				public void onFbLoginSuccess(com.facebook.Response response) {
					updateFb(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID)
							.toString());
				}
			};
			fb.execute();
			break;

		case TAG_SKIP:
			getLoginActivity().changeActivity(true);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fb.onActivityResult(requestCode, resultCode, data);
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	public void updateFb(String fbId) {
		BeanProfile bean = Converter.toProfile(getPref().getString(Preference.USER_DETAILS));
		if (bean == null) {
			Helper.showAlert(getActivity(), R.string.iapps__network_error);
			return;
		}

		new HTTPTbs(this, true) {

			@Override
			public String url() {
				return Api.UPDATE_FB;
			}

			@Override
			public void onSuccess(JSONObject j) {
				getLoginActivity().changeActivity(true);
			}
		}.setPostParams(Keys.USER_ID, bean.getId()).setPostParams(Keys.USER_FB_ID, fbId).execute();
	}

}
