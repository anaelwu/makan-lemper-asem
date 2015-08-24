package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iapps.adapters.BaseAdapterList;
import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanTransactions;

public class AdapterTransactions
	extends BaseAdapterList {
	boolean	expand;

	public AdapterTransactions(Context context, ArrayList<? extends SimpleBean> objects, int resLayout) {
		super(context, objects, resLayout);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int setAppTheme() {
		// TODO Auto-generated method stub
		return R.style.CustomActionBarTheme;
	}

	@Override
	public Object setViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	public View initView(View view, Object objectHolder) {
		ViewHolder holder = (ViewHolder) objectHolder;
		holder.tvName = (TextView) view.findViewById(R.id.tvName);
		holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
		holder.tvRef = (TextView) view.findViewById(R.id.tvRef);
		holder.tvValue = (TextView) view.findViewById(R.id.tvValue);
		holder.tvPoints = (TextView) view.findViewById(R.id.tvPoints);
		holder.viewHolder = (View) view.findViewById(R.id.viewHolder);
		holder.llDetails = (LinearLayout) view.findViewById(R.id.llDetails);

		return view;
	}

	@Override
	public void setView(Object objectHolder, int position) {
		BeanTransactions bean = (BeanTransactions) getItem(position);
		final ViewHolder holder = (ViewHolder) objectHolder;
		
		if(position % 2 == 0){
			holder.viewHolder.setBackgroundResource(R.color.gray_light);
		}else{
			holder.viewHolder.setBackgroundResource(R.color.white);
		}
		
		holder.tvName.setText(bean.getName());
		if (bean.isShowDate())
			holder.tvDate.setText(bean.getDate().toString(Constants.DATE_MDY));
		else{
			holder.tvDate.setText("");
		}
		
		holder.tvRef.setText("Transaction ref : " + bean.getRef());
		if (bean.getValue() > 0) {
			holder.tvValue.setText(Helper.formatRupiah(bean.getValue()));
			holder.tvPoints.setText("Points earned : " + Integer.toString(bean.getPointsEarned()));
		}
		else
		{
			holder.tvValue.setTextColor(getContext().getResources().getColor(R.color.DarkRed));
			// holder.tvValue.setText(Helper.formatRupiah(0));
			holder.tvValue.setText("Points redeemed : " + Long.toString(bean.getValue() * -1));
			holder.tvPoints.setVisibility(View.GONE);
		}

		holder.viewHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (holder.llDetails.getVisibility() == View.VISIBLE) {
//					UIHelper.animCollapse(holder.llDetails);
//				}
//				else {
//					UIHelper.animExpand(holder.llDetails);
//				}
			}
		});
	}

	public class ViewHolder {
		TextView		tvName;
		TextView		tvDate;
		TextView		tvRef;
		TextView		tvValue;
		TextView		tvPoints;
		View			viewHolder;
		LinearLayout	llDetails;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public boolean isExpand() {
		return this.expand;
	}

}
