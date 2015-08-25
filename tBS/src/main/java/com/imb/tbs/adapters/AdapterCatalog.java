package com.imb.tbs.adapters;

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
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanProductDetails;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class AdapterCatalog
        extends ArrayAdapter {
    @SuppressWarnings("unchecked")
    public AdapterCatalog(Context context, ArrayList<? extends SimpleBean> objects) {
        super(context, R.layout.cell_catalog, objects);
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
        TextView        tvName;
        ImageViewLoader img;
    }

    private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
                                                                    R.style.CustomActionBarTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View convertView = localInflater.inflate(R.layout.cell_catalog, parent, false);

        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        holder.img = (ImageViewLoader) convertView.findViewById(R.id.img);

        convertView.setTag(holder);

        return convertView;
    }

    private void setView(ViewHolder holder, int position) {
        BeanProductDetails bean = (BeanProductDetails) getItem(position);

        holder.img.setSquareToWidth(true);
        holder.tvName.setText(bean.getName());
        if (Helper.isEmpty(bean.getImg())) {
            holder.img.loadImage(R.drawable.ic_cross_light);
        } else {
            holder.img.setVisibility(View.VISIBLE);
            holder.img.loadImage(bean.getImg());
            holder.img.setImageOverlay(0);
            holder.img.setPopupOnClick(false);
        }
    }
}
