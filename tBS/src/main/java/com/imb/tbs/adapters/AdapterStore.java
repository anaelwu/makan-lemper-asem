package com.imb.tbs.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.iapps.adapters.BaseAdapterList;
import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanStore;

import java.util.ArrayList;

public class AdapterStore
        extends BaseAdapterList {
    public AdapterStore(Context context, ArrayList<? extends SimpleBean> objects, int resLayout) {
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
        holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        holder.tvDesc = (TextView) view.findViewById(R.id.tvAddress);

        return view;
    }

    @Override
    public void setView(Object objectHolder, int position) {
        ViewHolder holder = (ViewHolder) objectHolder;
        BeanStore  bean   = (BeanStore) getItem(position);
        holder.tvTitle.setText(bean.getName());
        holder.tvDesc.setText(bean.getAddress());
    }

    public class ViewHolder {
        TextView tvTitle;
        TextView tvDesc;
    }

}
