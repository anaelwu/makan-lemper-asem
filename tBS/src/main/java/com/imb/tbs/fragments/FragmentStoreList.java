package com.imb.tbs.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterStore;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.objects.BeanStore;

public class FragmentStoreList
	extends BaseFragmentTbs {

	@InjectView(R.id.lv)
	private ListView		lv;

	ArrayList<BeanStore>	alStore	= new ArrayList<BeanStore>();
	AdapterStore			adapter;
	String					search;

	public FragmentStoreList(ArrayList<BeanStore> al, String search) {
		this.alStore = al;
		this.search = search;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_store_list;
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.search);
		adapter = new AdapterStore(getActivity(), alStore, R.layout.cell_store);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

		search();
	}

	private void search() {
		Iterator<BeanStore> list = alStore.iterator();
		while (list.hasNext()) {
			BeanStore bean = list.next();

			if (!Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(bean.getAddress()).find()
					&& !Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(bean.getName())
							.find()) {
				list.remove();
			}
		}

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		setFragment(new FragmentStore(alStore.get(position)));

	}
}
