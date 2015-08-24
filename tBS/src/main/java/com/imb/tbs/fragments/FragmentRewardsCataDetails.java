package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanRewardsCatalog;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

public class FragmentRewardsCataDetails extends BaseFragmentTbs {
	@InjectView(R.id.img)
	private ImageViewLoader		img;
	@InjectView(R.id.tvDesc)
	private TextView			tvDesc;
	@InjectView(R.id.sv)
	private ParallaxScrollView	sv;

	BeanRewardsCatalog			bean;

	public FragmentRewardsCataDetails(BeanRewardsCatalog bean) {
		this.bean = bean;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_rewards_cata_details;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setToolbarOpacity(255);
		setTitle(Helper.formatNumber(bean.getPoint()) + " Points");

		img.loadImage(bean.getImgUrl());
		img.setSquareToWidth(true);
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

}
