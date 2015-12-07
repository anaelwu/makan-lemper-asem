package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanSetting;

import roboguice.inject.InjectView;

public class FragmentVcardMember
        extends BaseFragmentTbs {
    @InjectView(R.id.tvName)
    private TextView        tvName;
    @InjectView(R.id.tvStatus)
    private TextView        tvStatus;
    @InjectView(R.id.tvPoint)
    private TextView        tvPoint;
    @InjectView(R.id.tvExpiry)
    private TextView        tvExpiry;
    @InjectView(R.id.tvCard)
    private TextView        tvCard;
    @InjectView(R.id.imgQr)
    private ImageViewLoader imgQr;
    @InjectView(R.id.imgBarcode)
    private ImageViewLoader imgBarcode;

    @Override
    public int setLayout() {
        return R.layout.fragment_vcard_member;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Virtual Card LYB";
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

        BeanSetting setting = Converter.toSettings(getPref().getString(Preference.SETTINGS));
        if (setting != null) {
            imgQr.loadImage(setting.getQrcode() + Long.toString(getProfile().getCard()));
            imgBarcode.loadImage(setting.getBarcode().replace("$1", Long.toString(getProfile().getCard())));
        }
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
