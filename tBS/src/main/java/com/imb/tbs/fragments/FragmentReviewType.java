package com.imb.tbs.fragments;

import org.joda.time.DateTime;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iapps.libs.helpers.BaseUIHelper;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;

public class FragmentReviewType
	extends BaseFragmentTbs {
	@InjectView(R.id.edt)
	private EditText	edt;
	@InjectView(R.id.btnSend)
	private ImageButton	btnSend;

	private int			id;

	public FragmentReviewType(int id) {
		this.id = id;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.review_text;
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		sendReview();
	}

	private void sendReview() {
		if (Helper.isEmpty(edt)) {
			Helper.showAlert(getActivity(), R.string.review_empty);
			return;
		}

		new HTTPTbs(this, true) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.ADD_REVIEW;
			}

			@Override
			public void onSuccess(JSONObject j) {
				Toast.makeText(getActivity(), R.string.review_submit, Toast.LENGTH_SHORT).show();
				BaseUIHelper.hideKeyboard(getActivity());
				onBackPressed();
			}
		}.setPostParams(Keys.TESTI_TEXT, edt).setPostParams(Keys.TESTI_PRODUCT_ID, id)
				.setPostParams(Keys.TESTI_ACC_ID, getProfile().getId())
				.setPostParams(Keys.TESTI_DATE, DateTime.now().toString(Constants.DATE_JSON_FULL)).execute();
	}
}
