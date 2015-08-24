package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterRewardsCatalog;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanRewardsCatalog;

public class FragmentRewardsCatalog
	extends BaseFragmentTbs implements OnRefreshListener,
		OnScrollListener {
	@InjectView(R.id.gv)
	private GridView						gv;
	@InjectView(R.id.sr)
	private SwipeRefreshLayout				sr;
	@InjectView(R.id.ld)
	private LoadingCompound					ld;

	private ArrayList<BeanRewardsCatalog>	alCatalog	= new ArrayList<BeanRewardsCatalog>();
	private AdapterRewardsCatalog			adapter;

	@Override
	public int setLayout() {
		return R.layout.fragment_catalog;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {

		setTitle(R.string.rewards_catalog);
		initGridList();

		Helper.setRefreshColor(sr);
		sr.setOnRefreshListener(this);
		gv.setOnScrollListener(this);

		if (alCatalog.isEmpty()) {
			loadList();
		}
		else {
			ld.hide();
		}
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void initGridList() {
		adapter = new AdapterRewardsCatalog(getActivity(), alCatalog);
		gv.setAdapter(adapter);
		gv.setOnScrollListener(this);
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	@Override
	public void onRefresh() {
		loadList();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {

		boolean enable = false;

		// Detect if already on top of Gridview
		View childView = gv.getChildAt(0);
		int topIndex = (childView == null) ? 0 : childView.getTop();

		if (gv != null && gv.getChildCount() > 0 && topIndex > 0 && firstVisibleItem == 0) {
			// check if the first item of the list is visible
			boolean firstItemVisible = gv.getFirstVisiblePosition() == 0;
			sr.setEnabled(firstItemVisible);
		}
		else {
			sr.setEnabled(enable);
		}
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	private void loadList() {
		new HTTPTbs(this, ld) {

			@Override
			public String url() {
				return Api.GET_REWARDS;
			}

			@Override
			public void onSuccess(JSONObject j) {
				sr.setRefreshing(false);
				try {
					alCatalog.clear();
					alCatalog.addAll(Converter.toRewards(j.getString(Keys.RESULTS)));
					adapter.notifyDataSetChanged();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFail(int code, String message) {
				super.onFail(code, message);
				sr.setRefreshing(false);
			};
		}.setGetParams(Keys.ID, getProfile() != null ? getProfile().getId() : 1).execute();
	}

}
