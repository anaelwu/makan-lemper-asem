package com.imb.tbs.fragments;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityVcard;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;

public class FragmentProfile
	extends BaseFragmentTbs implements OnClickListener, OnRefreshListener {
	@InjectView(R.id.tvStatus)
	private TextView			tvStatus;
	@InjectView(R.id.tvPoints)
	private TextView			tvPoint;
	@InjectView(R.id.tvName)
	private TextView			tvName;
	@InjectView(R.id.tvGreeting)
	private TextView			tvGreeting;
	@InjectView(R.id.tvExpiry)
	private TextView			tvExpiry;
	@InjectView(R.id.imgProfile)
	private ImageViewLoader		imgProfile;
	@InjectView(R.id.imgQr)
	private ImageViewLoader		imgQr;
	@InjectView(R.id.sr)
	private SwipeRefreshLayout	sr;
	@InjectView(R.id.btnTransactions)
	private Button				btnTransactions;

	public static final int		TAG_EDIT	= 1, TAG_CARD = 2, TAG_TRANS = 3;

	@Override
	public int setLayout() {
		return R.layout.fragment_profile;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.profile_white);

		getProfile();
		displayProfile();

		sr.setOnRefreshListener(this);
		Helper.setRefreshColor(sr);
		imgQr.loadImage(R.drawable.ic_qr);
		imgQr.setTag(TAG_CARD);
		imgQr.setOnClickListener(this);
	}

	@Override
	public int setMenuLayout() {
		return R.menu.profile_white;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_profile:
			setFragment(new FragmentProfileDetails());
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// ================================================================================
	// Profile
	// ================================================================================
	public void displayProfile() {
		imgProfile.loadImage(getProfile().getImage(), R.drawable.ic_person, true);
		tvGreeting.setText("Good Morning,");
		Calendar c = Calendar.getInstance();
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

		if (timeOfDay >= 0 && timeOfDay < 12)
			tvGreeting.setText("Good Morning,");
		else if (timeOfDay >= 12 && timeOfDay < 16)
			tvGreeting.setText("Good Afternoon,");
		else if (timeOfDay >= 16 && timeOfDay < 21)
			tvGreeting.setText("Good Evening,");
		else if (timeOfDay >= 21 && timeOfDay < 24)
			tvGreeting.setText("Good Night,");

		tvName.setText(getProfile().getFullName());
		tvStatus.setText(getProfile().getStatus());
		if (getProfile().getStatus().equalsIgnoreCase(Constants.STATUS_STAMP)) {
			tvPoint.setText("Purchase to upgrade :\n"
					+ Helper.formatRupiah(getProfile().getTotalPurchase()) + " / "
					+ Helper.formatRupiah(Converter.toStcSetting(getPref().getString(Preference.STC_LIMIT)).getLimit()));
		}
		else
			tvPoint.setText("Points : " + Helper.formatNumber(getProfile().getPoints()));

		if (getProfile().getExpiry() != null && !getProfile().getExpiry().isBeforeNow())
			tvExpiry.setText("Expiry Date : "
					+ getProfile().getExpiry().toString(Constants.DATE_MDY));
		else
			tvExpiry.setVisibility(View.GONE);

		if (getProfile().getTransactions() == null) {
			btnTransactions.setVisibility(View.GONE);
		}
		else {
			btnTransactions.setVisibility(View.VISIBLE);
			btnTransactions.setTag(TAG_TRANS);
			btnTransactions.setOnClickListener(this);
		}

	}

	// ================================================================================
	// Listeners
	// ================================================================================
	@Override
	public void onClick(View v) {
		switch ((Integer) v.getTag()) {
		case TAG_EDIT:
			setFragment(new FragmentProfileDetails());
			break;

		case TAG_CARD:
			if (getProfile().getStatus().equalsIgnoreCase(Constants.STATUS_STAMP))
				startActivity(new Intent(getActivity(), ActivityVcard.class));
			else
				setFragment(new FragmentVcardMember());
			break;

		case TAG_TRANS:
			setFragment(new FragmentTransactions(getProfile().getTransactions(), getProfile().getTotalPurchase()));
			break;
		}
	}

	@Override
	public void onRefresh() {
		loadProfile();
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	public void loadProfile() {
		new HTTPTbs(this, false) {

			@Override
			public void onSuccess(JSONObject j) {
				try {
					getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
					getHomeActivity().updateUser();
					displayProfile();

					sr.setRefreshing(false);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFail(int code, String message) {
				super.onFail(code, message);
				sr.setRefreshing(false);
			};

			@Override
			public String url() {
				return Api.LOGIN_WITH_CARD;
			}
		}.setPostParams(Keys.CARD, Long.toString(getProfile().getCard()))
				.setPostParams(Keys.DOB, getProfile().getDob().toString(Constants.DATE_JSON)).execute();
	}
}
