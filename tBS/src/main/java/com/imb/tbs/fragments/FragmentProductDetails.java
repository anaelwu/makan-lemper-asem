package com.imb.tbs.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.TabHost;

import com.iapps.adapters.TabsAdapter;
import com.iapps.libs.helpers.BaseKeys;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.views.CustomViewPager;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.objects.BeanProductDetails;

import roboguice.inject.InjectView;

public class FragmentProductDetails
        extends BaseFragmentTbs {
    @InjectView(R.id.imgHeader)
    private ImageViewLoader    imgHeader;
    // @InjectView(R.id.header)
    // private View mHeader;
    @InjectView(R.id.vpTab)
    private CustomViewPager    vp;
    @InjectView(android.R.id.tabhost)
    private TabHost            tabHost;
    private TabsAdapter        tabsAdapter;
    private BeanProductDetails bean;
    public static final String TAG_ITEM = "item", TAG_REVIEW = "review";

    // private int toolbarColor = 0;

    public FragmentProductDetails(BeanProductDetails bean) {
        this.bean = bean;
    }

    public FragmentProductDetails(BeanProductDetails bean, int toolbarColor) {
        this.bean = bean;
        // this.toolbarColor = toolbarColor;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_product_details;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Details - " + bean.getName();
        setTitle(bean.getName());
        // if (toolbarColor > 0)
        // setToolbarColor(toolbarColor);

        setupTab(view, savedInstanceState);

        imgHeader.setBackgroundColor(Color.WHITE);
        imgHeader.setPopupOnClick(true);
        imgHeader.setSquareToWidth(true);
    }

    @Override
    public int setMenuLayout() {
        return R.menu.buy;
    }

    // ================================================================================
    // Menu
    // ================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_buy:
                setFragment(new FragmentWebview(bean.getName(), bean.getUrl()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // ================================================================================
    // Tab
    // ================================================================================
    public void setupTab(View view, Bundle savedInstance) {
        tabsAdapter = new TabsAdapter(this, tabHost, vp);

        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(null);

        Bundle details = new Bundle();
        details.putParcelable(Constants.OBJECT, bean);

        tabsAdapter.addTab(tabHost.newTabSpec(TAG_ITEM).setIndicator("Product Details"),
                           FragmentProductItem.class, details);
        tabsAdapter.addTab(tabHost.newTabSpec(TAG_REVIEW).setIndicator("Reviews"),
                           FragmentProductReview.class, details);

        BaseUIHelper.adjustTabHost(tabHost, getActivity(), R.drawable.tab_indicator_ab_tbsgreen,
                                   R.color.Black);

        if (savedInstance != null) {
            tabHost.setCurrentTabByTag(savedInstance.getString(BaseKeys.TAB));
        }

        imgHeader.getImage().setScaleType(ScaleType.FIT_CENTER);
        imgHeader.setImgFail(R.drawable.ic_prod_fail);
        imgHeader.loadImage(bean.getImg());
    }

}
