package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;

public class FragmentVcardMember
	extends BaseFragmentTbs {
	@InjectView(R.id.tvName)
	private TextView		tvName;
	@InjectView(R.id.tvStatus)
	private TextView		tvStatus;
	@InjectView(R.id.tvPoint)
	private TextView		tvPoint;
	@InjectView(R.id.tvExpiry)
	private TextView		tvExpiry;
	@InjectView(R.id.tvCard)
	private TextView		tvCard;
	@InjectView(R.id.imgQr)
	private ImageViewLoader	imgQr;
	@InjectView(R.id.imgBarcode)
	private ImageViewLoader	imgBarcode;

	@Override
	public int setLayout() {
		return R.layout.fragment_vcard_member;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		getToolbar().setVisibility(View.GONE);
		hideToolbar();
		tvName.setText(getProfile().getFullName());
		tvStatus.setText(getProfile().getStatus());
		tvPoint.setText("Points : " + Helper.formatNumber(getProfile().getPoints()));
		if (getProfile().getTotalPurchase() > 0 && getProfile().getExpiry().isAfterNow())
			tvExpiry.setText("Expiry Date : " + getProfile().getExpiry().toString(Constants.DATE_MDY));
		else
			tvExpiry.setVisibility(View.GONE);
		tvCard.setText("Card # : " + Long.toString(getProfile().getCard()));
		imgQr.loadImage(Api.QR_CODE + Long.toString(getProfile().getCard()));
		imgBarcode.loadImage(Api.BAR_CODE.replace("$1", Long.toString(getProfile().getCard())));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		resetToolbarColor();
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getToolbar().setVisibility(View.VISIBLE);
	}

}
