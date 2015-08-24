package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanHome;

@SuppressWarnings("rawtypes")
public class AdapterHome extends ArrayAdapter {
	private int	resLayout;

	@SuppressWarnings("unchecked")
	public AdapterHome(Context context, ArrayList<? extends SimpleBean> objects, int resLayout) {
		super(context, resLayout, objects);
		this.resLayout = resLayout;
	}

	@SuppressWarnings("unchecked")
	public AdapterHome(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_home, objects);
		this.resLayout = R.layout.cell_home;
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
		TextView		tv;
		ImageView		img;
		RelativeLayout	rl;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = localInflater.inflate(resLayout, parent, false);

		holder.tv = (TextView) convertView.findViewById(R.id.tv);
		holder.img = (ImageView) convertView.findViewById(R.id.img);
		holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		BeanHome bean = (BeanHome) getItem(position);

		holder.tv.setText(bean.getName());
		holder.img.setImageResource(bean.getImg());
		holder.rl.setBackgroundResource(bean.getColor());
	}
}
