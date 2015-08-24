package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iapps.external.FlowLayout.FlowLayout;
import com.iapps.libs.helpers.HTTPAsyncImb;
import com.iapps.libs.views.ImageViewLoader;
import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterProductDetails;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanDetails;
import com.imb.tbs.objects.BeanProductDetails;

public class FragmentProductItem
	extends BaseFragmentTbs {
	@InjectView(R.id.lv)
	private ListView				lv;
	@InjectView(R.id.ld)
	private LoadingCompound			ld;

	private Button					btnWishlist;
	private TextView				tvPrice;
	private FlowLayout				llVariant;
	private LinearLayout			llVariantParent;

	private ArrayList<BeanDetails>	alDetails		= new ArrayList<BeanDetails>();
	private AdapterProductDetails	adapter;
	private BeanProductDetails		bean;

	public static final int			TAG_WISHLIST	= 1;

	@Override
	public int setLayout() {
		return R.layout.fragment_product_details_item;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		bean = getArguments().getParcelable(Constants.OBJECT);
		initList();

		adapter = new AdapterProductDetails(getActivity(), alDetails);

		getVariants();
		initHeader();
		lv.setAdapter(adapter);

		if (getProfile() == null)
			// btnWishlist.setEnabled(false);
			btnWishlist.setVisibility(View.GONE);

		btnWishlist.setTag(TAG_WISHLIST);
		btnWishlist.setOnClickListener(this);

		checkWishlist();
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	public void initList() {
		alDetails.clear();

		if (!Helper.isEmpty(bean.getArticle()))
			if (!Helper.isLangIndo())
				alDetails.add(new BeanDetails(getString(R.string.info), bean.getArticle()));
			else
				alDetails.add(new BeanDetails(getString(R.string.info), bean.getArticleIn()));

		if (!Helper.isEmpty(bean.getIngre()))
			if (!Helper.isLangIndo())
				alDetails.add(new BeanDetails(getString(R.string.ingredients), bean.getIngre()));
			else
				alDetails.add(new BeanDetails(getString(R.string.ingredients), bean.getIngreIn()));

		if (!Helper.isEmpty(bean.getHowto()))
			if (!Helper.isLangIndo())
				alDetails.add(new BeanDetails(getString(R.string.how_to_use), bean.getHowto()));
			else
				alDetails.add(new BeanDetails(getString(R.string.how_to_use), bean.getHowto()));

		if (!Helper.isEmpty(bean.getWeight()))
			alDetails.add(new BeanDetails(getString(R.string.weight), bean.getWeight()));
	}

	public void initHeader() {
		View header = getHomeTbs().getLayoutInflater().inflate(R.layout.cell_product_details_header,
				null);
		lv.addHeaderView(header);

		btnWishlist = (Button) header.findViewById(R.id.btnWishlist);
		tvPrice = (TextView) header.findViewById(R.id.tvPrice);
		llVariant = (FlowLayout) header.findViewById(R.id.flVariant);
		llVariantParent = (LinearLayout) header.findViewById(R.id.llVariantContainer);

		tvPrice.setText(Helper.formatRupiah(bean.getPrice()));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		addWishlist();
	}

	private void createVariants(ArrayList<BeanProductDetails> alVariants) {
		if (alVariants.size() > 0)
			llVariantParent.setVisibility(View.VISIBLE);
		else {
			llVariantParent.setVisibility(View.GONE);
			return;
		}

		for (BeanProductDetails beanProductDetails : alVariants) {
			ImageViewLoader variant = new ImageViewLoader(getActivity());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					getResources().getDimensionPixelSize(R.dimen.img_variant),
					getResources().getDimensionPixelSize(
							R.dimen.img_variant));
			variant.setLayoutParams(params);
			variant.loadImage(beanProductDetails.getImgVariant());
			variant.setPopupOnClick(true);
			llVariant.addView(variant);
		}
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	private void checkWishlist() {
		if (getProfile() != null)
			new HTTPTbs(this, ld) {

				@Override
				public String url() {
					return Api.CHECK_WISHLIST;
				}

				public String search() {
					return Helper.toSearchQuery(Keys.WISH_ARTICLE, bean.getVariantId())
							+ Helper.toSearchQuery(Keys.WISH_ACC_ID, getProfile().getId());
				};

				@Override
				public void onSuccess(JSONObject j) {
					btnWishlist.setVisibility(View.GONE);
				}

				public void onFail(int code, String message) {
					if (code == Constants.CODE_BACKEND_FAIL) {
						ld.hide();
					}
				};
			}.execute();
		else {
			ld.hide();
			btnWishlist.setVisibility(View.GONE);
		}
	}

	private void getVariants() {
		new HTTPAsyncImb(this, false) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.PRODUCT_VARIANT;
			}

			@Override
			public String search() {
				// TODO Auto-generated method stub
				return Helper.toSearchQuery(Keys.PROD_TYPE, Constants.VARIANT)
						+ Helper.toSearchQuery(Keys.PROD_ID, bean.getId());
			}

			@Override
			public void onSuccess(JSONObject j) {
				// Toast.makeText(getActivity(), j.toString(), Toast.LENGTH_SHORT).show();
				try {
					createVariants(Converter.toProductList(j.getString(Keys.RESULTS)));
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onFail(int code, String message) {
				// Do nothing
			};
		}.execute();
	}

	private void addWishlist() {
		new HTTPTbs(this, true) {

			@Override
			public String url() {
				return Api.ADD_WISHLIST;
			}

			@Override
			public void onSuccess(JSONObject j) {
				btnWishlist.setVisibility(View.GONE);
				Toast.makeText(getActivity(), getString(R.string.success_wish), Toast.LENGTH_SHORT).show();
			}
		}.setPostParams(Keys.WISH_ACC_ID, getProfile().getId()).setPostParams(Keys.WISH_ARTICLE, bean.getVariantId())
				.execute();
	}
}
