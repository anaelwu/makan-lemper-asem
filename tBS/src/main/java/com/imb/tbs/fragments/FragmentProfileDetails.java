package com.imb.tbs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.facebook.Response;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.helpers.FacebookImage;
import com.iapps.libs.helpers.FacebookLogin;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentProfileDetails
	extends BaseFragmentTbs implements OnClickListener,
		PinPickerDialogHandler {

	@InjectView(R.id.edtName)
	private EditText		edtName;
	@InjectView(R.id.edtFamily)
	private EditText		edtFamily;
	@InjectView(R.id.edtEmail)
	private EditText		edtEmail;
	@InjectView(R.id.edtPhone)
	private EditText		edtPhone;
	@InjectView(R.id.edtDob)
	private EditText		edtDob;
	@InjectView(R.id.imgProfile)
	private ImageViewLoader	imgProfile;
	@InjectView(R.id.tvId)
	private TextView		tvId;
	@InjectView(R.id.sv)
	private ScrollView		sv;
	@InjectView(R.id.btnFb)
	private Button			btnFb;
	@InjectView(R.id.btnLogout)
	private Button			btnLogout;
	@InjectView(R.id.btnTnc)
	private Button			btnTnc;
	@InjectView(R.id.rbMale)
	private RadioButton		rbMale;
	@InjectView(R.id.rbFemale)
	private RadioButton		rbFemale;

	public static final int	TAG_PHONE	= 1, TAG_DOB = 2, TAG_FB = 3, TAG_LOGOUT = 4, TAG_TNC = 5,
										TAG_IMAGE = 6;

	private boolean			isPhotoUpdate, isFbUpdate;
	private String			fbId;
	private FacebookLogin	fb;

	@Override
	public int setLayout() {
		return R.layout.fragment_profile_details;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.profile_white);
		resetToolbarColor();

		if (getProfile().getCard() > 0)
			tvId.setText("Card # : " + Long.toString(getProfile().getCard()));
		else
			tvId.setVisibility(View.GONE);

		edtPhone.setFocusable(false);
		edtDob.setFocusable(false);

		edtPhone.setTag(TAG_PHONE);
		edtDob.setTag(TAG_DOB);
		btnFb.setTag(TAG_FB);
		btnLogout.setTag(TAG_LOGOUT);
		btnTnc.setTag(TAG_TNC);
		imgProfile.setTag(TAG_IMAGE);

		edtPhone.setOnClickListener(this);
		edtDob.setOnClickListener(this);
		btnFb.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnTnc.setOnClickListener(this);
		imgProfile.setOnClickListener(this);

		edtDob.setEnabled(false);
		edtName.setEnabled(false);
		edtFamily.setEnabled(false);

		setScrollListener(sv);

		loadProfile();
	}

	@Override
	public int setMenuLayout() {
		return R.menu.save;
	}

	public void loadProfile() {
		edtName.setText(getProfile().getName());
		edtFamily.setText(getProfile().getLastname());
		edtDob.setText(getProfile().getDob().toString(Constants.DATE_MDY));
		edtPhone.setText(getProfile().getPhone());
		edtEmail.setText(getProfile().getEmail());
		imgProfile.loadImage(getProfile().getImage(), R.drawable.ic_person, true);

		if (!Helper.isEmpty(getProfile().getFbId()))
			btnFb.setVisibility(View.GONE);

		if (Helper.isMale(getProfile().getGender())) {
			rbMale.setChecked(true);
			rbFemale.setEnabled(false);
		}
		else {
			rbFemale.setChecked(true);
			rbMale.setEnabled(false);
		}
	}

	public void fillFbDetails(Response response) {
		isFbUpdate = true;
		// Fill details
		fbId = Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID).toString();
		new FacebookImage(imgProfile, fbId) {

			@Override
			public void onSuccess(byte[] avatar) {
				isPhotoUpdate = true;
			}
		}.execute();

		btnFb.setVisibility(View.GONE);
	}

	// ================================================================================
	// Menu
	// ================================================================================
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			if (Helper.validateEditTexts(new EditText[] {
					edtName, edtFamily, edtEmail, edtPhone, edtDob
			}) && Helper.validateEmail(edtEmail))
				update();
			return true;
		}

		return false;
	}

	// ================================================================================
	// Event Listeners
	// ================================================================================
	@Override
	public void onClick(View v) {
		switch ((Integer) v.getTag()) {
		case TAG_PHONE:
			Helper.numberPicker(this, TAG_PHONE);
			break;

		case TAG_DOB:
			Helper.datePicker(edtDob);
			break;

		case TAG_FB:
			fb = new FacebookLogin(getString(R.string.fb_app_id), this) {

				@Override
				public void onFbLoginSuccess(Response response) {
					fillFbDetails(response);
				}
			};
			fb.execute();
			break;

		case TAG_LOGOUT:
			getHomeActivity().logout();
			break;

		case TAG_TNC:
			hideToolbar();
			setFragment(new FragmentTnc(true));
			break;

		case TAG_IMAGE:
			Helper.pickImage(this);
			break;
		}
	}

	@Override
	public void onDialogPinSet(int reference, String pin) {
		switch (reference) {
		case TAG_PHONE:
			edtPhone.setText(pin);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Helper.REQUEST_GET_IMAGE_CODE && resultCode == -1) {
			imgProfile.hideProgress();
			BaseUIHelper.processImage(getActivity(), imgProfile.getImage());
			isPhotoUpdate = true;
		}
		else if (requestCode != Helper.REQUEST_GET_IMAGE_CODE) {
			fb.onActivityResult(requestCode, resultCode, data);
		}

	}

	// ================================================================================
	// Webservice
	// ================================================================================
	public void update() {
		HTTPTbs update = new HTTPTbs(this, true) {

			@Override
			public void onSuccess(JSONObject j) {
				reloadProfile();
			}

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.UPDATE_USER;
			}
		};
		update.setPostParams(Keys.USER_ID, getProfile().getId());
		update.setPostParams(Keys.USER_EMAIL, edtEmail);
		update.setPostParams(Keys.USER_PHONE, Constants.PHONE_EXTENSION + edtPhone.getText().toString());
		if (isFbUpdate)
			update.setPostParams(Keys.USER_FB_ID, this.fbId);

		if (isPhotoUpdate)
			update.setPostParams(Keys.USER_PHOTO, imgProfile.getUploadFormat());

		update.execute();
	}

	private void reloadProfile() {
		new HTTPTbs(this, true) {

			@Override
			public void onSuccess(JSONObject j) {
				try {
					getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
					getHomeActivity().getUser();
					getHomeActivity().updateUser();
					Toast.makeText(getActivity(), R.string.update_success, Toast.LENGTH_SHORT)
							.show();
					onBackPressed();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String url() {
				return Api.LOGIN_WITH_CARD;
			}
		}.setPostParams(Keys.CARD, Long.toString(getProfile().getCard()))
				.setPostParams(Keys.DOB, getProfile().getDob().toString(Constants.DATE_JSON)).execute();
	}
}
