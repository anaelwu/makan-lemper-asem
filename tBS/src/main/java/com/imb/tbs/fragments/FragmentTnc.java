package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.iapps.libs.helpers.HTTPAsyncImb;
import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterTnc;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanTnc;

public class FragmentTnc
	extends BaseFragmentTbs {

	@InjectView(R.id.btnAccept)
	private Button				btnAccept;
	// @InjectView(R.id.tvTnc)
	// private TextView tvTnc;
	@InjectView(R.id.lv)
	private ListView			lv;
	@InjectView(R.id.ld)
	private LoadingCompound		ld;

	private ArrayList<BeanTnc>	al	= new ArrayList<BeanTnc>();
	private AdapterTnc			adapter;
	private boolean				isPopup;

	public FragmentTnc() {
		// TODO Auto-generated constructor stub
	}

	public FragmentTnc(boolean isPopup) {
		this.isPopup = isPopup;
	}

	@Override
	public int setLayout() {
		return R.layout.fragment_tnc;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		getToolbar().setVisibility(View.GONE);
		btnAccept.setOnClickListener(this);

		if (al.isEmpty()) {
			loadTnc();
		}

		adapter = new AdapterTnc(getActivity(), al, R.layout.cell_tnc);
		lv.setAdapter(adapter);
	}

	@Override
	public void onDestroyView() {
		getToolbar().setVisibility(View.VISIBLE);
		super.onDestroyView();
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	private void loadTnc() {
		new HTTPAsyncImb(this, ld) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.TNC;
			}

			@Override
			public void onSuccess(JSONObject j) {
				JSONArray jArr;
				try {
					jArr = j.getJSONArray(Keys.RESULTS);
					for (int i = 0; i < jArr.length(); i++) {
						JSONObject jObj = jArr.getJSONObject(i);
						al.add(new BeanTnc().setContent(jObj.getString(Keys.TNC_CONTENT))
								.setTitle(jObj.getString(Keys.TNC_TITLE)));

						adapter.notifyDataSetChanged();

						btnAccept.setVisibility(View.VISIBLE);
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}

	// ================================================================================
	// Listener
	// ================================================================================
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (isPopup)
			onBackPressed();
		else {
			getPref().setBoolean(Preference.IS_ACCEPT_TNC, true);
			clearFragment();
			setFragment(new FragmentPrelogin());
		}
	}

}
