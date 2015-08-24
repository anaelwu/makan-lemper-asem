package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanCampaign;

@SuppressWarnings("rawtypes")
public class AdapterCampaign
	extends ArrayAdapter {
	private int	lastPosition	= -1;

	@SuppressWarnings("unchecked")
	public AdapterCampaign(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_campaign, objects);
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

		if (position > lastPosition) {
			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.up_from_bottom);
			convertView.startAnimation(animation);

			lastPosition = position;
		}

		setView(holder, position);

		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView		tvTitle;
		ImageViewLoader	img;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = localInflater.inflate(R.layout.cell_campaign, parent, false);

		holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		holder.img = (ImageViewLoader) convertView.findViewById(R.id.imgCampaign);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		BeanCampaign bean = (BeanCampaign) getItem(position);

		holder.tvTitle.setText(bean.getName());
		if (Helper.isEmpty(bean.getImgUrl())) {
			holder.img.setVisibility(View.GONE);
		}
		else {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setSquareToWidth(true);
			holder.img.loadImage(bean.getImgUrl());
			holder.img.getImage().setScaleType(ScaleType.FIT_XY);
			holder.img.setClipChildren(true);
		}
	}

}
