package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanSplash;

@SuppressWarnings("rawtypes")
public class AdapterSplash extends ArrayAdapter {

	@SuppressWarnings("unchecked")
	public AdapterSplash(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_splash, objects);
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
		ImageViewLoader	img;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = localInflater.inflate(R.layout.cell_splash, parent, false);

		holder.img = (ImageViewLoader) convertView.findViewById(R.id.img);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		holder.img.setSquareToWidth(true);

		BeanSplash bean = (BeanSplash) getItem(position);

		if (bean.isImg()) {
			holder.img.loadImage(bean.getContent());
		} else {
			holder.img.getImage().setBackgroundColor(bean.getContent());
			holder.img.hideProgress();
		}

		holder.img.setSquareToWidth(true);
		holder.img.setImageOverlay(0);
	}
}
