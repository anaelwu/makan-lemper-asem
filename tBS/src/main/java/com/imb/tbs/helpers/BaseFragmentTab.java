package com.imb.tbs.helpers;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.imb.tbs.R;
import com.kmshack.newsstand.ScrollTabHolder;

public abstract class BaseFragmentTab extends BaseFragmentTbs implements ScrollTabHolder {

	protected ScrollTabHolder	mScrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}

	public void setHeader(ListView lv) {
		View placeHolderView = getHomeTbs().getLayoutInflater().inflate(
				R.layout.view_header_placeholder, lv, false);
		lv.addHeaderView(placeHolderView);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount, int pagePosition) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount,
					pagePosition);
	}

	@Override
	public void adjustScroll(int scrollHeight) {

	}

}