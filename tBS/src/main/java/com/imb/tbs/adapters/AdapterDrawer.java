package com.imb.tbs.adapters;

import java.util.List;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityHome;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanDrawer;

public class AdapterDrawer
		extends ArrayAdapter<BeanDrawer> {

	Preference	pref;

	public AdapterDrawer(Context context, List<BeanDrawer> objects) {
		super(context, R.layout.cell_drawer, objects);

		pref = Preference.getInstance(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);

			final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
					R.style.CustomActionBarTheme);

			// clone the inflater using the ContextThemeWrapper
			LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

			convertView = localInflater.inflate(R.layout.cell_drawer, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.tvDrawer);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		BeanDrawer objDrawer = getItem(position);
		holder.name.setText(objDrawer.getName());

		if (objDrawer.getId() == ActivityHome.TAG_LOGOUT) {
			holder.name.setText(getContext().getString(R.string.back_to_login));
		}

		return convertView;
	}

	private class ViewHolder {
		TextView	name;
	}

}
