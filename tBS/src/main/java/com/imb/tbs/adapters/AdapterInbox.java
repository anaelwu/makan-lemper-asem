package com.imb.tbs.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imb.tbs.R;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.objects.BeanNotif;

import java.util.ArrayList;

/**
 * Created by marcelsantoso.
 * <p/>
 * 9/4/15
 */
public abstract class AdapterInbox extends BaseAdapterList {
    public AdapterInbox(Context context, ArrayList<? extends Object> objects, int resLayout) {
        super(context, objects, resLayout);
    }

    private boolean showRemove;

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
        holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
        holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        holder.btnRemove = (ImageButton) view.findViewById(R.id.btnRemove);

        return view;
    }

    @Override
    public void setView(Object objectHolder, int position) {
        BeanNotif  bean   = (BeanNotif) getItem(position);
        ViewHolder holder = (ViewHolder) objectHolder;

        holder.tvTitle.setText(bean.getTitle());
        holder.tvTime.setText(bean.getTime().toString(Constants.DATE_MDY));
        if(showRemove){
            holder.btnRemove.setVisibility(View.VISIBLE);
//            holder.tvTime.setVisibility(View.GONE);
        }else{
            holder.btnRemove.setVisibility(View.GONE);
//            holder.tvTime.setVisibility(View.VISIBLE);
        }

        holder.btnRemove.setTag(position);
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemove((Integer) v.getTag());
            }
        });
    }

    public class ViewHolder {
        TextView    tvTitle;
        TextView    tvTime;
        ImageButton btnRemove;
    }

    public void toggleRemove() {
        this.showRemove = !showRemove;
        this.notifyDataSetChanged();
    }

    public boolean isShowRemove() {
        return this.showRemove;
    }

    public abstract void onRemove(int pos);
}
