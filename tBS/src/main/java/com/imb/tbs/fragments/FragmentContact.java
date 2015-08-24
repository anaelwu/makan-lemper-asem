package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityLogin;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProfile;
import com.imb.tbs.objects.BeanSetting;
import com.material.widget.FloatingEditText;

public class FragmentContact
	extends BaseFragmentTbs implements PinPickerDialogHandler {

	@InjectView(R.id.btnPhone1)
	private Button				btnPhone1;
	@InjectView(R.id.btnPhone2)
	private Button				btnPhone2;
	@InjectView(R.id.btnEmail)
	private Button				btnEmail;
	@InjectView(R.id.btnSubmit)
	private Button				btnSubmit;
	@InjectView(R.id.edtName)
	private FloatingEditText	edtName;
	@InjectView(R.id.edtEmail)
	private FloatingEditText	edtEmail;
	@InjectView(R.id.edtMobile)
	private FloatingEditText	edtPhone;
	@InjectView(R.id.edtComment)
	private FloatingEditText	edtComment;
	@InjectView(R.id.tvTime)
	private TextView			tvTime;
	@InjectView(R.id.btnChat)
	private Button				btnChat;
	@InjectView(R.id.version)
	private TextView			tvVersion;

	public static final int		TAG_PHONE	= 1, TAG_EMAIL = 2, TAG_SUBMIT = 3, TAG_PHONE_INPUT = 4, TAG_CHAT = 5;

	@Override
	public int setLayout() {
		return R.layout.fragment_contact;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.contact_us);

		if (getActivity() instanceof ActivityLogin) {
			setToolbarColor(R.drawable.tab_unselected_actionbar_bg);
		}

		BeanProfile bean = Converter.toProfile(getPref().getString(Preference.USER_DETAILS));
		if (bean != null) {
			edtPhone.setText(bean.getPhone());
			edtEmail.setText(bean.getEmail());
			edtName.setText(bean.getFullName());
		}

		BeanSetting setting = Converter.toSettings(getPref().getString(Preference.SETTINGS));
		if (setting != null) {
			btnPhone1.setText(setting.getPhone1());
			btnPhone2.setText(setting.getPhone2());
			btnEmail.setText(setting.getEmail());
			tvTime.setText(Helper.breakLine(setting.getOpeningHour()));
		}

		edtPhone.setFocusable(false);
		edtPhone.setLongClickable(false);

		btnPhone1.setTag(TAG_PHONE);
		btnPhone2.setTag(TAG_PHONE);
		btnEmail.setTag(TAG_EMAIL);
		btnSubmit.setTag(TAG_SUBMIT);
		edtPhone.setTag(TAG_PHONE_INPUT);
		btnChat.setTag(TAG_CHAT);

		btnPhone1.setOnClickListener(this);
		btnPhone2.setOnClickListener(this);
		btnEmail.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		edtPhone.setOnClickListener(this);
		btnChat.setOnClickListener(this);

		if (Constants.IS_DEBUGGING) {
			tvVersion.setVisibility(View.VISIBLE);
			tvVersion.setText(Constants.VERSION);
		}
		else {
			tvVersion.setVisibility(View.GONE);
		}
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setTitle("");
		if (getActivity() instanceof ActivityLogin) {
			hideToolbar();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch ((Integer) v.getTag()) {
		case TAG_PHONE:
			Helper.intentCall(getActivity(), ((Button) v).getText().toString());
			break;

		case TAG_EMAIL:
			Helper.intentEmail(getActivity(), getString(R.string.con_email), "Inquiry");
			break;

		case TAG_PHONE_INPUT:
			Helper.numberPicker(this, TAG_PHONE_INPUT);
			break;

		case TAG_SUBMIT:
			if (Helper.validateEditTexts(new EditText[] {
					edtName, edtPhone, edtComment
			})) {
				String text =
						edtComment.getText().toString() + "\n\nDetails saya : \nNama : " + edtName.getText().toString()
								+ "\nMobile No. : " + getString(R.string.extension) + edtPhone.getText().toString();
				Helper.intentEmail(getActivity(), getString(R.string.con_email), "Inquiry", text);
			}
			break;

		case TAG_CHAT:
			setFragment(new FragmentWebview(getString(R.string.live_chat), Api.LIVECHAT));
			break;
		}
	}

	@Override
	public void onDialogPinSet(int reference, String pin) {
		edtPhone.setText(pin);
	}

}
