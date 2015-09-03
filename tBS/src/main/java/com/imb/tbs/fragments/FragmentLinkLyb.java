package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProfile;
import com.material.widget.FloatingEditText;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentLinkLyb
	extends BaseFragmentTbs implements PinPickerDialogHandler {

	@InjectView(R.id.edtCard)
	private FloatingEditText	edtCard;
	@InjectView(R.id.edtDob)
	private FloatingEditText	edtDob;
	@InjectView(R.id.btnSubmit)
	private Button				btnSubmit;
	@InjectView(R.id.btnNo)
	private Button				btnNo;
	@InjectView(R.id.btnContact)
	private Button				btnContact;
	@InjectView(R.id.btnHelp)
	private ImageButton			btnHelp;

	public static final int		TAG_CARD	= 1, TAG_DOB = 2, TAG_SUBMIT = 3, TAG_NO = 4,
											TAG_CONTACT = 5, TAG_HELP = 6;
	private Bundle				bundle;

	public FragmentLinkLyb(Bundle bundle) {
		this.bundle = bundle;
	}

	public FragmentLinkLyb() {
	}

	@Override
	public int setLayout() {
		return R.layout.fragment_link_lyb;
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		edtCard.setFocusable(false);
		edtDob.setFocusable(false);

		edtCard.setTag(TAG_CARD);
		edtDob.setTag(TAG_DOB);
		btnSubmit.setTag(TAG_SUBMIT);
		btnNo.setTag(TAG_NO);
		btnContact.setTag(TAG_CONTACT);
		btnHelp.setTag(TAG_HELP);

		edtCard.setOnClickListener(this);
		edtDob.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnNo.setOnClickListener(this);
		btnContact.setOnClickListener(this);
		btnHelp.setOnClickListener(this);

		// edtDob.setText(dob.toString(Constants.DATE_MDY));
		edtDob.setText(Helper.formatDateTime(bundle.getString(Keys.USER_DOB), Constants.DATE_JSON, Constants.DATE_MDY));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch ((Integer) v.getTag()) {
		case TAG_CARD:
			popupPicker(FragmentLinkLyb.this).setDisplayAsPassword(false)
					.setDecimalVisibility(View.GONE)
					.setPlusMinusVisibility(View.GONE).setAllowZero(true)
					.setReference((Integer) v.getTag()).show();
			break;

		case TAG_DOB:
			Helper.datePicker(edtDob);
			break;

		case TAG_SUBMIT:
			if (Helper.validateEditTexts(new EditText[] {
					edtCard, edtDob
			}))
				loginLyb();
			break;

		case TAG_NO:
			register();
			break;

		case TAG_CONTACT:
			setFragment(new FragmentContact());
			break;

		case TAG_HELP:
			Helper.popupHelp(getActivity());
			break;
		}
	}

	// ================================================================================
	// Listener
	// ================================================================================
	@Override
	public void onDialogPinSet(int reference, String pin) {
		switch (reference) {
		case TAG_CARD:
			edtCard.setText(pin);
			break;
		}
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	public void register() {
		HTTPTbs register = new HTTPTbs(this, true) {

			@Override
			public void onSuccess(JSONObject j) {
				try {
					// BeanProfile bean = Converter.toProfile(j.getString(Keys.RESULTS));

					getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
					getLoginActivity().changeActivity(true);
					// }
					// else {
					// Helper.showAlert(getActivity(), R.string.invalid_credentials);
					// }
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String url() {
				return Api.REGISTER_USER;
			}
		};
		for (String key : bundle.keySet()) {
			register.setPostParams(key, bundle.getString(key));
		}
		register.execute();
	}

	public void loginLyb() {
		HTTPTbs check = new HTTPTbs(this, true) {

			@Override
			public void onSuccess(JSONObject j) {
				try {
					getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));

					if (!Helper.isEmpty(bundle.getString(Keys.USER_FB_ID))) {
						updateFb(bundle.getString(Keys.USER_FB_ID));
					}
					else
						getLoginActivity().changeActivity(true);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String url() {
				return Api.LOGIN_WITH_CARD;
			}
		};
		check.setPostParams(Keys.CARD, edtCard);
		check.setPostParams(Keys.DOB,
				Helper.formatDateTime(edtDob.getText().toString(), Constants.DATE_MDY, Constants.DATE_JSON));
		check.execute();
	}

	public void updateFb(String fbId) {
		BeanProfile bean = Converter.toProfile(getPref().getString(Preference.USER_DETAILS));
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
