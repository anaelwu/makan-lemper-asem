package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProfile;

public class FragmentVcardStamp
	extends BaseFragmentTbs {
	@InjectView(R.id.pb)
	private ProgressBar		pb;
	@InjectView(R.id.tvPoint)
	private TextView		tvPoint;
	@InjectView(R.id.tvMin)
	private TextView		tvMin;
	@InjectView(R.id.tvLimit)
	private TextView		tvLimit;
	@InjectView(R.id.tvName)
	private TextView		tvName;
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
		// TODO Auto-generated method stub
		return R.layout.fragment_vcard_stamp;
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		BeanProfile bean = Converter.toProfile(getPref().getString(Preference.USER_DETAILS));

		int limit = (int) Converter.toStcSetting(getPref().getString(Preference.STC_LIMIT)).getLimit();

		pb.getProgressDrawable().setColorFilter(Color.WHITE, Mode.SRC_IN);
		pb.setProgress((int) bean.getTotalPurchase());
		pb.setMax(limit);

		tvName.setText(bean.getFullName());
		tvPoint.setText("Current purchase : " + Helper.formatRupiah(bean.getTotalPurchase()));
		tvLimit.setText(Helper.formatRupiah(limit));
		tvMin.setText(Helper.formatRupiah(bean.getTotalPurchase()));
		tvCard.setText("Card # : " + Long.toString(bean.getCard()));
		if (bean.getTotalPurchase() > 0 && bean.getExpiry().isAfterNow())
			tvExpiry.setText("Expiry Date : " + bean.getExpiry().toString(Constants.DATE_MDY));
		else
			tvExpiry.setVisibility(View.GONE);

		imgQr.loadImage(Api.QR_CODE + Long.toString(bean.getCard()));
		imgBarcode.loadImage(Api.BAR_CODE.replace("$1", Long.toString(bean.getCard())));

	}

}
