package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanReview;

@SuppressWarnings("rawtypes")
public class AdapterReview extends ArrayAdapter {

	@SuppressWarnings("unchecked")
	public AdapterReview(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_product_review_left, objects);
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
		TextView		tvComment;
		ImageViewLoader	img;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
		
		View convertView = null;
		if (position % 2 == 0)
			convertView = localInflater.inflate(R.layout.cell_product_review_left, parent, false);
		else
			convertView = localInflater.inflate(R.layout.cell_product_review_right, parent, false);

		holder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
		holder.img = (ImageViewLoader) convertView.findViewById(R.id.img);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		BeanReview bean = (BeanReview) getItem(position);

		holder.tvComment.setText(bean.getComment());
		holder.img.loadImage(bean.getImgUrl(), true);
		holder.img.setImageOverlay(0);
	}
}
