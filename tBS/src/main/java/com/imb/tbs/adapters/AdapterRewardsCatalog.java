package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanRewardsCatalog;

@SuppressWarnings("rawtypes")
public class AdapterRewardsCatalog extends ArrayAdapter {

	@SuppressWarnings("unchecked")
	public AdapterRewardsCatalog(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_rewards_catalog, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflateView(holder, parent, position);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		setView(holder, position);

		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView		tvName;
		ImageViewLoader	img;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = localInflater.inflate(R.layout.cell_rewards_catalog, parent, false);

		holder.tvName = (TextView) convertView.findViewById(R.id.tvPoints);
		holder.img = (ImageViewLoader) convertView.findViewById(R.id.img);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		holder.img.setSquareToWidth(true);

		BeanRewardsCatalog bean = (BeanRewardsCatalog) getItem(position);

		if (Helper.isEmpty(bean.getImgUrl())) {
			holder.img.setVisibility(View.GONE);
		} else {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.loadImage(bean.getImgUrl());
			holder.img.setSquareToWidth(true);
			holder.img.setPopupOnClick(true);
		}

		holder.tvName.setVisibility(View.VISIBLE);

		switch (bean.getStatus()) {
			case Constants.STATE_REWARDS_OK:
				holder.tvName.setVisibility(View.GONE);
				break;

			case Constants.STATE_REWARDS_POINT_REQ:
				holder.tvName.setBackgroundResource(R.drawable.overlay);
				holder.tvName.setText(Helper.formatNumber(bean.getPoint()) + "\nPoints");
				holder.tvName.setTextColor(Color.LTGRAY);
				break;

			case Constants.STATE_REWARDS_FAN_REQ:
				holder.tvName.setBackgroundResource(R.drawable.overlay);
				holder.tvName.setText(getContext().getString(R.string.state_fan_req));
				holder.tvName.setTextColor(Color.LTGRAY);
				break;

			case Constants.STATE_REWARDS_CLUB_REQ:
				holder.tvName.setBackgroundResource(R.drawable.overlay);
				holder.tvName.setText(getContext().getString(R.string.state_club_req));
				holder.tvName.setTextColor(Color.LTGRAY);
				break;
		}
	}
}
