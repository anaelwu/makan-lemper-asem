package com.imb.tbs.adapters;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanProduct;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public abstract class AdapterProductCat
        extends ArrayAdapter {
    private int resLayout;

    @SuppressWarnings("unchecked")
    public AdapterProductCat(Context context, ArrayList<? extends SimpleBean> objects, int resLayout) {
        super(context, resLayout, objects);
        this.resLayout = resLayout;
    }

    @SuppressWarnings("unchecked")
    public AdapterProductCat(Context context, ArrayList<? extends SimpleBean> objects) {
        super(context, R.layout.cell_home, objects);
        this.resLayout = R.layout.cell_home;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflateView(holder, parent, position);
        } else {
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
        TextView       tv;
        ImageView      img;
        RelativeLayout rl;
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

        // dimen imgHome + margin_smallx2
        int minHeight = (int) (getContext().getResources().getDimension(R.dimen.img_home) + getContext().getResources()
                                                                                                        .getDimension(
                                                                                                                R.dimen.margin_small) *
                2);
        int height =
                ((int) (BaseUIHelper.getScreenHeight(getContext()) - actionBarHeight()))
                        / getCount();

        if (height < minHeight)
            holder.rl.getLayoutParams().height = minHeight;
        else
            holder.rl.getLayoutParams().height = height;

        convertView.setTag(holder);

        return convertView;
    }

    private void setView(ViewHolder holder, int position) {
        BeanProduct bean = (BeanProduct) getItem(position);

        holder.tv.setText(bean.getName());
        if (bean.getCode().equalsIgnoreCase("T1002"))
            holder.img.setImageResource(R.drawable.ic_cat_skincare);
        else if (bean.getCode().equalsIgnoreCase("T1001"))
            holder.img.setImageResource(R.drawable.ic_cat_bath);
        else if (bean.getCode().equalsIgnoreCase("T1005"))
            holder.img.setImageResource(R.drawable.ic_cat_fragrance);
        else if (bean.getCode().equalsIgnoreCase("T1003"))
            holder.img.setImageResource(R.drawable.ic_cat_makeup);
        else if (bean.getCode().equalsIgnoreCase("T1004"))
            holder.img.setImageResource(R.drawable.ic_cat_gift);
        else if (bean.getCode().equalsIgnoreCase("T1007"))
            holder.img.setImageResource(R.drawable.ic_cat_mens);
        else if (bean.getCode().equalsIgnoreCase("T1006"))
            holder.img.setImageResource(R.drawable.ic_cat_handcare);
        else if (bean.getCode().equalsIgnoreCase("T1008"))
            holder.img.setImageResource(R.drawable.ic_cat_range);
        else
            holder.img.setVisibility(View.GONE);

        switch (position % 9) {
            case 0:
                bean.setColor(R.drawable.home_1_xml);
                break;
            case 1:
                bean.setColor(R.drawable.home_2_xml);
                break;
            case 2:
                bean.setColor(R.drawable.home_3_xml);
                break;
            case 3:
                bean.setColor(R.drawable.home_4_xml);
                break;
            case 4:
                bean.setColor(R.drawable.home_5_xml);
                break;
            case 5:
                bean.setColor(R.drawable.home_6_xml);
                break;
            case 6:
                bean.setColor(R.drawable.home_7_xml);
                break;
            case 7:
                bean.setColor(R.drawable.home_8_xml);
                break;
            case 8:
                bean.setColor(R.drawable.home_9_xml);
                break;
            case 9:
                bean.setColor(R.drawable.home_10_xml);
            default:
                bean.setColor(R.drawable.home_10_xml);
                break;
        }

        holder.rl.setBackgroundResource(bean.getColor());
    }

    public abstract int actionBarHeight();
}
