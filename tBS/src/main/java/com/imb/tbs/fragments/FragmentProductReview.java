package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterReview;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanProductDetails;
import com.imb.tbs.objects.BeanReview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

public class FragmentProductReview
	extends BaseFragmentTbs {

	@InjectView(R.id.lv)
	private ListView				lv;
	@InjectView(R.id.ld)
	private LoadingCompound			ld;
	@InjectView(R.id.btnComment)
	private Button					btnComment;

	private ArrayList<BeanReview>	alReview	= new ArrayList<BeanReview>();
	private AdapterReview			adapter;
	private BeanProductDetails		bean;

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_product_review;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		bean = getArguments().getParcelable(Constants.OBJECT);

		adapter = new AdapterReview(getActivity(), alReview);
		lv.setAdapter(adapter);
		btnComment.setOnClickListener(this);

		if (alReview.isEmpty()) {
			loadReviews();
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

	@Override
	public void onClick(View v) {
		super.onClick(v);
		getHomeActivity().addFragmentWithAnim(new FragmentReviewType(bean.getVariantId()), R.anim.abc_slide_in_bottom,
				R.anim.abc_slide_out_bottom);
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	private void loadReviews() {
		new HTTPTbs(this, ld) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.GET_REVIEW;
			}

			@Override
			public void onSuccess(JSONObject j) {
				try {
					alReview.clear();
					alReview.addAll(Converter.toReview(j.getString(Keys.RESULTS)));
					adapter.notifyDataSetChanged();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFail(int code, String message) {
				if (code == Constants.CODE_BACKEND_FAIL) {
					ld.showError("", getString(R.string.no_reviews));
				}
			};
		}.setGetParams("prod_id", bean.getVariantId()).execute();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
