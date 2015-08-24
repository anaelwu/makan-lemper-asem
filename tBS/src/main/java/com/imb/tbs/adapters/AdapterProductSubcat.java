package com.imb.tbs.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iapps.external.ExpandableListViewAnimated.AdapterExpandableListView;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanProduct;

public class AdapterProductSubcat extends AdapterExpandableListView {
	private LayoutInflater		inflater;

	private List<BeanProduct>	items;

	public AdapterProductSubcat(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public AdapterProductSubcat setData(List<BeanProduct> items) {
		this.items = items;

		return this;
	}

	@Override
	public BeanProduct getChild(int groupPosition, int childPosition) {
		return (BeanProduct) items.get(groupPosition).getChildList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent) {
		ChildHolder holder;
		BeanProduct item = getChild(groupPosition, childPosition);
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.cell_product_subcat_child, parent, false);
			holder.textChild = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		holder.textChild.setText(item.getName());

		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return items.get(groupPosition).getChildList().size();
	}

	@Override
	public BeanProduct getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {
		GroupHolder holder;
		BeanProduct item = getGroup(groupPosition);
		if (convertView == null) {
			holder = new GroupHolder();
			convertView = inflater.inflate(R.layout.cell_product_subcat, parent, false);
			holder.textGroup = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}

		holder.textGroup.setText(item.getName());

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class GroupHolder {
		TextView	textGroup;
	}

	class ChildHolder {
		TextView	textChild;
	}

}