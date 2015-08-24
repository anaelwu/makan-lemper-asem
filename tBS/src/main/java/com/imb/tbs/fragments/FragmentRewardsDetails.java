package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

public class FragmentRewardsDetails
	extends BaseFragmentTbs {
	@InjectView(R.id.img)
	private ImageViewLoader		img;
	// @InjectView(R.id.tvDesc)
	// private TextView tvDesc;
	@InjectView(R.id.btnCatalog)
	private Button				btnCatalog;
	@InjectView(R.id.sv)
	private ParallaxScrollView	sv;
	@InjectView(R.id.tvTitle)
	private TextView			tvTitle;
	@InjectView(R.id.tvContent)
	private TextView			tvDesc;

	private int					tag;

	public FragmentRewardsDetails(int tag) {
		this.tag = tag;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_rewards_details;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		img.setSquareToWidth(true);
		img.getImage().setScaleType(ScaleType.FIT_CENTER);

		switch (tag) {
		case Constants.TAG_REWARDS_MEMBER:
			img.loadImage(R.drawable.dum_lyb_header);
			setTitle(R.string.member_upgrade_rewards);
			tvTitle.setText(R.string.rewards_details_upgrade_title);
			tvDesc.setText(R.string.rewards_details_upgrade_desc);
			break;

		case Constants.TAG_REWARDS_BOOK:
			img.loadImage(R.drawable.dum_book_header);
			setTitle(R.string.welcome_book);
			tvTitle.setText(R.string.rewards_details_book_title);
			String text = getString(R.string.rewards_details_book_desc);
			String contains = "REAL TREATS, REAL BENEFITS!";
			Spannable span = Helper.fontColor(text, text.indexOf(contains),
					text.indexOf(contains) + new String(contains).length(), "#e10785");
			span = Helper.bold(span, text.indexOf(contains),
					text.indexOf(contains) + new String(contains).length());
			tvDesc.setText(span);
			break;

		case Constants.TAG_REWARDS_BDAY:
			img.loadImage(R.drawable.dum_bday_header);
			setTitle(R.string.birthday_gift);
			tvTitle.setText(R.string.rewards_details_bday_title);
			tvDesc.setText(R.string.rewards_details_bday_desc);
			break;

		case Constants.TAG_REWARDS_REDEEM:
			btnCatalog.setVisibility(View.VISIBLE);
			btnCatalog.setOnClickListener(this);

			img.loadImage(R.drawable.dum_redeem_header);
			setTitle(R.string.redeem_point);
			tvTitle.setText(R.string.rewards_details_redeem_title);
			tvDesc.setText(R.string.rewards_details_redeem_desc);
			break;
		}

		setScrollListener(sv);
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	// ================================================================================
	// Listener
	// ================================================================================
	@Override
	public void onClick(View v) {
		super.onClick(v);

		setFragment(new FragmentRewardsCatalog());
	}

}
