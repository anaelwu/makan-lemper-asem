package com.imb.tbs.adapters;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public abstract class BaseAdapterList
        extends ArrayAdapter {
    public int resLayout;

    @SuppressWarnings("unchecked")
    public BaseAdapterList(Context context, ArrayList<? extends Object> objects, int resLayout) {
        super(context, resLayout, objects);
        this.resLayout = resLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object holder;

        if (convertView == null) {
            holder = setViewHolder();
            convertView = inflateView(holder, parent, position);
        } else {
            holder = (Object) convertView.getTag();
        }

        setView(holder, position);

        return convertView;
    }

    private View inflateView(Object holder, ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
                                                                    this.setAppTheme());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View convertView = null;
        convertView = initView(localInflater.inflate(this.resLayout, parent, false), holder);
        convertView.setTag(holder);

        return convertView;
    }

    /**
     * set your app_theme here. R.style.CustomAppTheme
     *
     * @return
     */
    public abstract int setAppTheme();

    /**
     * Give a custom object to hold all the views Example : public class ViewHolder{ TextView tvTitle; TextView tvDesc;
     * ImageView img; }
     *
     * @return
     */
    public abstract Object setViewHolder();

    /**
     * Declare items in the Holder Object links to which element How to use : ViewHolder holder = (ViewHolder)
     * objectHolder; holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
     * <p/>
     * *ViewHolder can be replaced with any other custom object*
     *
     * @param view
     * @param objectHolder - Cast this object to your custom object, so that u can use the methods within
     *
     * @return
     */
    public abstract View initView(View view, Object objectHolder);

    /**
     * Fill in custom objects with the data. holder.tvTitle.setText("text goes here");
     *
     * @param objectHolder
     * @param position
     */
    public abstract void setView(Object objectHolder, int position);

}
