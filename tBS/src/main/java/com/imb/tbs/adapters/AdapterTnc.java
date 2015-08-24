package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iapps.adapters.BaseAdapterList;
import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.UIHelper;
import com.imb.tbs.objects.BeanTnc;

public class AdapterTnc
	extends BaseAdapterList {

	public AdapterTnc(Context context, ArrayList<? extends SimpleBean> objects, int resLayout) {
		super(context, objects, resLayout);
	}

	@Override
	public int setAppTheme() {
		return R.style.CustomActionBarTheme;
	}

	@Override
	public Object setViewHolder() {
		return new ViewHolder();
	}

	@Override
	public View initView(View view, Object objectHolder) {
		ViewHolder holder = (ViewHolder) objectHolder;
		holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		holder.tvContent = (TextView) view.findViewById(R.id.tvContent);
		holder.holder = (LinearLayout) view.findViewById(R.id.holder);

		return view;
	}

	@Override
	public void setView(Object objectHolder, int position) {
		BeanTnc bean = (BeanTnc) getItem(position);
		final ViewHolder holder = (ViewHolder) objectHolder;
		holder.tvTitle.setText(Html.fromHtml(Helper.breakLine(bean.getTitle())));
		holder.tvContent.setText(Html.fromHtml(Helper.breakLine(bean.getContent())));
		holder.holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.tvContent.getVisibility() == View.VISIBLE) {
					UIHelper.animCollapse(holder.tvContent);
				}
				else {
					UIHelper.animExpand(holder.tvContent);
				}
			}
		});
	}

	public class ViewHolder {
		TextView		tvTitle;
		TextView		tvContent;
		LinearLayout	holder;
	}
}
