package com.imb.tbs.fragments;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterProductCat;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import roboguice.inject.InjectView;

public class FragmentProductCategory
        extends BaseFragmentTbs implements OnItemClickListener, OnRefreshListener, OnQueryTextListener {
    @InjectView(R.id.lv)
    private ListView           lv;
    @InjectView(R.id.ld)
    private LoadingCompound    ld;
    @InjectView(R.id.sr)
    private SwipeRefreshLayout sr;
    private AdapterProductCat  adapter;
    private ArrayList<BeanProduct> alCategory = new ArrayList<BeanProduct>();

    @Override
    public int setLayout() {
        return R.layout.fragment_product_category;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        setTitle(R.string.product);

        adapter = new AdapterProductCat(getActivity(), alCategory, R.layout.cell_product_category) {
            @Override
            public int actionBarHeight() {
                return getToolbar().getLayoutParams().height;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        if (alCategory.isEmpty() && Helper.isEmpty(getPref().getString(Preference.PRODUCT_DETAILS)))
            loadProducts();
        else if (alCategory.isEmpty()) {
            ld.hide();
            alCategory.addAll(Converter.toProducts(getPref().getString(Preference.PRODUCT_LAYERS),
                                                   getPref().getString(Preference.PRODUCT_DETAILS),
                                                   getPref().getString(Preference.PRODUCT_ORDER)));
            sort();
            adapter.notifyDataSetChanged();
        } else {
            ld.hide();
        }

        Helper.setRefreshColor(sr);
        sr.setOnRefreshListener(this);

    }

    private void sort() {
        // Don't sort
        Collections.sort(alCategory, new CustomComparator());
    }

    public class CustomComparator implements Comparator<BeanProduct> {
        @Override
        public int compare(BeanProduct o1, BeanProduct o2) {
            return o1.getOrder() - o2.getOrder();
        }
    }

    // ================================================================================
    // Listener
    // ================================================================================
    @Override
    public int setMenuLayout() {
        return R.menu.search;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        ((SearchView) MenuItemCompat.getActionView(searchItem)).setOnQueryTextListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setFragment(new FragmentProductSubcat(alCategory.get(position)));
    }

    @Override
    public void onRefresh() {
        loadProducts();
    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String search) {
        setFragment(new FragmentProductList(Helper.toSearchQuery(Keys.PROD_TYPE, Constants.BASE)
                                                    + Helper.toSearchQuery(Keys.PROD_NAME, search, Keys.ANYWHERE)));
        return true;
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    private void loadProducts() {
        new HTTPTbs(this, ld) {
            @Override
            public String url() {
                return Api.GET_PRODUCTS;
            }

            @Override
            public void onSuccess(JSONObject j) {
                sr.setRefreshing(false);
                try {
                    getPref().setString(Preference.PRODUCT_LAYERS, j.getString(Keys.PROD_LAYER));
                    getPref().setString(Preference.PRODUCT_DETAILS, j.getString(Keys.PROD_DETAILS));
                    getPref().setString(Preference.PRODUCT_ORDER, j.getString(Keys.PROD_ORDER));

                    alCategory.clear();
                    alCategory
                            .addAll(Converter.toProducts(j.getString(Keys.PROD_LAYER), j.getString(Keys.PROD_DETAILS),
                                                         j.getString(Keys.PROD_ORDER)));
                    sort();
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFail(int code, String message) {
                super.onFail(code, message);
                sr.setRefreshing(false);
            }

            ;
        }.execute();
    }

}
