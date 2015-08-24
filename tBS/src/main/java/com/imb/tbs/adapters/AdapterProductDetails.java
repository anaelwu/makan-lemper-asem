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
import com.imb.tbs.R;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanDetails;

@SuppressWarnings("rawtypes")
public class AdapterProductDetails
	extends ArrayAdapter {

	@SuppressWarnings("unchecked")
	public AdapterProductDetails(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_product_details, objects);
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
		TextView	tvTitle;
		TextView	tvDesc;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = localInflater.inflate(R.layout.cell_product_details, parent, false);

		holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		BeanDetails bean = (BeanDetails) getItem(position);

		holder.tvTitle.setText(Helper.breakLine(bean.getName()));
		holder.tvDesc.setText(Helper.breakLine(bean.getDesc()));
	}
}
