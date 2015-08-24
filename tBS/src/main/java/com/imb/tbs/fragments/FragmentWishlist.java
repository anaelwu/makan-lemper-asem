package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterWishlist;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanWish;

public class FragmentWishlist
	extends BaseFragmentTbs implements OnRefreshListener {

	@InjectView(R.id.lv)
	private ListView			lv;
	@InjectView(R.id.ld)
	private LoadingCompound		ld;
	@InjectView(R.id.sr)
	private SwipeRefreshLayout	sr;

	private ArrayList<BeanWish>	alWish	= new ArrayList<BeanWish>();
	private AdapterWishlist		adapter;
	private Menu				menu;

	@Override
	public int setLayout() {
		return R.layout.fragment_wishlist;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.wishlist);

		adapter = new AdapterWishlist(getActivity(), alWish) {

			@Override
			public void remove(int pos) {
				delete(pos);
			}

		};
		adapter.setListenerBuy(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		Helper.setRefreshColor(sr);
		sr.setOnRefreshListener(this);

		if (!alWish.isEmpty())
			ld.hide();
		else
			load();
	}

	// ================================================================================
	// Menu
	// ================================================================================
	@Override
	public int setMenuLayout() {
		return R.menu.remove;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		setMenuIcon();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_remove:
			adapter.setRemoveMode(!adapter.getRemoveMode());
			setMenuIcon();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setMenuIcon() {
		MenuItem item = menu.getItem(0);

		if (adapter.getRemoveMode())
			item.setIcon(R.drawable.ic_done);
		else
			item.setIcon(R.drawable.ic_trash);
	}

	// ================================================================================
	// Listener
	// ================================================================================
	@Override
	public void onClick(View v) {
		super.onClick(v);

		int position = (Integer) v.getTag();
		BeanWish bean = alWish.get(position);

		setFragment(new FragmentWebview(bean.getName(), bean.getUrl()));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		setFragment(new FragmentProductDetails(alWish.get(position)));
	}

	@Override
	public void onRefresh() {
		load();
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	private void load() {
		if (getProfile() != null)
			new HTTPTbs(this, ld) {

				@Override
				public String url() {
					return Api.SEARCH_WISHLIST;
				}

				@Override
				public void onSuccess(JSONObject j) {
					sr.setRefreshing(false);
					try {
						alWish.clear();
						alWish.addAll(Converter.toWishlist(j.getString(Keys.RESULTS)));
						adapter.notifyDataSetChanged();
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}

				public void onFail(int code, String message) {
					sr.setRefreshing(false);
					if (code == Constants.CODE_BACKEND_FAIL) {
						ld.showError("", getString(R.string.no_wish));
					}
					else
						super.onFail(code, message);
				};
			}.setGetParams(Keys.USER_ID, getProfile().getId()).execute();
		else
			ld.showError("", getString(R.string.please_login));
	}

	private void delete(final int pos) {
		new HTTPTbs(this, true) {

			@Override
			public String url() {
				return Api.DELETE_WISHLIST;
			}

			@Override
			public void onSuccess(JSONObject j) {
				alWish.remove(pos);
				adapter.notifyDataSetChanged();

				Toast.makeText(getActivity(), getString(R.string.success_remove), Toast.LENGTH_SHORT).show();

				if (alWish.isEmpty())
					ld.showError("", getString(R.string.no_wish));
			}

		}.setGetParams(Keys.ID, alWish.get(pos).getWishId()).setPostParams("updates", 1).execute();
	}

}
