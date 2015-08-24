package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanCampaign;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

public class FragmentCampaignDetails extends BaseFragmentTbs {
	@InjectView(R.id.img)
	private ImageViewLoader		img;
	@InjectView(R.id.tvDesc)
	private TextView			tvDesc;
	@InjectView(R.id.sv)
	private ParallaxScrollView	sv;

	private BeanCampaign		bean;

	public FragmentCampaignDetails(BeanCampaign bean) {
		this.bean = bean;
	}

	@Override
	public int setLayout() {
		return R.layout.fragment_campaign_details;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(bean.getName());
		setScrollListener(sv);

		if (Helper.isEmpty(bean.getImgUrl()))
			img.setVisibility(View.GONE);
		else {
			img.loadImage(bean.getImgUrl());
			img.setSquareToWidth(true);
		}

		tvDesc.setText(bean.getDesc());
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}
}
