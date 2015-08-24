package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

import com.imb.tbs.helpers.BaseFragmentTab;
import com.imb.tbs.objects.BeanFragment;
import com.kmshack.newsstand.ScrollTabHolder;

public class AdapterPager extends FragmentPagerAdapter {

	private SparseArrayCompat<ScrollTabHolder>	mScrollTabHolders;
	private ArrayList<BeanFragment>				alfrag;
	private ScrollTabHolder						mListener;

	public AdapterPager(FragmentManager fm, ArrayList<BeanFragment> alfrag) {
		super(fm);
		this.alfrag = alfrag;
		mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
	}

	public void setTabHolderScrollingContent(ScrollTabHolder listener) {
		mListener = listener;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return alfrag.get(position).getName();
	}

	@Override
	public int getCount() {
		return alfrag.size();
	}

	@Override
	public Fragment getItem(int position) {
		BaseFragmentTab fragment = (BaseFragmentTab) alfrag.get(position).getFragment();

		mScrollTabHolders.put(position, fragment);
		if (mListener != null) {
			fragment.setScrollTabHolder(mListener);
		}

		return fragment;
	}

	public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
		return mScrollTabHolders;
	}

}
