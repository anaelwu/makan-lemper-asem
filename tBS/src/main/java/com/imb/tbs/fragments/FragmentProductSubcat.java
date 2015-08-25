package com.imb.tbs.fragments;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.iapps.external.ExpandableListViewAnimated.ExpandableListViewAnimated;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterProductSubcat;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanProduct;

import java.util.ArrayList;
import java.util.Comparator;

import roboguice.inject.InjectView;

public class FragmentProductSubcat
	extends BaseFragmentTbs implements OnGroupClickListener,
		OnChildClickListener, OnQueryTextListener {
	@InjectView(R.id.lv)
	private ExpandableListViewAnimated	lv;

	private AdapterProductSubcat		adapter;
	private BeanProduct					beanCategory;

	public FragmentProductSubcat(BeanProduct beanCategory) {
		this.beanCategory = beanCategory;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_product_subcat;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(beanCategory.getName());
		// setToolbarColor(beanCategory.getColor());

		initList();
	}

	@Override
	public int setMenuLayout() {
		return R.menu.search;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		((SearchView) MenuItemCompat.getActionView(searchItem)).setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String search) {
		setFragment(new FragmentProductList(Helper.toSearchQuery(Keys.PROD_TYPE, Constants.BASE)
				+ Helper.toSearchQuery(Keys.PROD_NAME, search, Keys.ANYWHERE)));

		return true;
	}

	private void initList() {
		adapter = new AdapterProductSubcat(getActivity()).setData((ArrayList<BeanProduct>) beanCategory.getChildList());
		lv.setAdapter(adapter);
		lv.setOnGroupClickListener(this);
		lv.setOnChildClickListener(this);
		lv.setGroupIndicator(null);
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		if (lv.isGroupExpanded(groupPosition)) {
			lv.collapseGroupWithAnimation(groupPosition);
		}
		else {
			lv.expandGroupWithAnimation(groupPosition);
		}

		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			int childPosition, long id) {
		setFragment(new FragmentProductList(adapter.getChild(groupPosition, childPosition),
				beanCategory.getColor()));

		return true;
	}

}
