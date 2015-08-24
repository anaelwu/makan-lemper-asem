package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;

public class FragmentRewards extends BaseFragmentTbs {
	@InjectView(R.id.llMember)
	private LinearLayout	llMember;
	@InjectView(R.id.llBook)
	private LinearLayout	llBook;
	@InjectView(R.id.llBday)
	private LinearLayout	llBday;
	@InjectView(R.id.llRedeem)
	private LinearLayout	llRedeem;

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_rewards;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.rewards);

		llMember.setTag(Constants.TAG_REWARDS_MEMBER);
		llBook.setTag(Constants.TAG_REWARDS_BOOK);
		llBday.setTag(Constants.TAG_REWARDS_BDAY);
		llRedeem.setTag(Constants.TAG_REWARDS_REDEEM);

		llMember.setOnClickListener(this);
		llBook.setOnClickListener(this);
		llBday.setOnClickListener(this);
		llRedeem.setOnClickListener(this);
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	@Override
	public void onClick(View v) {
		super.onClick(v);

		setFragment(new FragmentRewardsDetails((Integer) v.getTag()));
	}

}
