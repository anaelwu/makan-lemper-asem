package com.imb.tbs.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterCatalog;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanProduct;
import com.imb.tbs.objects.BeanProductDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

public class FragmentProductList
        extends BaseFragmentTbs implements OnRefreshListener,
                                           OnScrollListener {
    @InjectView(R.id.gv)
    private GridView           gv;
    @InjectView(R.id.sr)
    private SwipeRefreshLayout sr;
    @InjectView(R.id.ld)
    private LoadingCompound    ld;
    private ArrayList<BeanProductDetails> alProduct = new ArrayList<BeanProductDetails>();
    private AdapterCatalog adapter;
    private BeanProduct    bean;
    private int toolbarColor = 0;
    private String  search;
    private boolean isScan, isSearch;

    public FragmentProductList(String scan, boolean isScan) {
        this.isScan = isScan;
        this.search = scan;
    }

    public FragmentProductList(String search) {
        this.search = search;
    }

    public FragmentProductList(BeanProduct bean, int toolbarColor) {
        this.bean = bean;
        this.toolbarColor = toolbarColor;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_catalog;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        if (bean != null) {
            setTitle(bean.getName());
            // setToolbarColor(toolbarColor);
        } else {
            setTitle("Search");
        }

        Helper.setRefreshColor(sr);
        sr.setOnRefreshListener(this);

        initGridList();
        if (alProduct.isEmpty())
            if (bean != null)
                loadProducts();
            else if (isScan)
                scan();
            else {
                isSearch = true;
                search();
            }
        else
            ld.hide();
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    public void initGridList() {
        adapter = new AdapterCatalog(getActivity(), alProduct);
        gv.setAdapter(adapter);
        gv.setOnScrollListener(this);
        gv.setOnItemClickListener(new ListenerItem());
    }

    // ================================================================================
    // Listeners
    // ================================================================================
    @Override
    public boolean pagination() {
        return isSearch;
    }

    @Override
    public int paginationCount() {
        return alProduct.size();
    }

    @Override
    public void onPagination(int page) {
        super.onPagination(page);
        search();
    }

    @Override
    public void onRefresh() {
        paginationPage(1);
        if (bean != null)
            loadProducts();
        else
            search();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

        boolean enable = false;

        // Detect if already on top of Gridview
        View childView = gv.getChildAt(0);
        int  topIndex  = (childView == null) ? 0 : childView.getTop();

        if (gv != null && gv.getChildCount() > 0 && topIndex > 0 && firstVisibleItem == 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = gv.getFirstVisiblePosition() == 0;
            sr.setEnabled(firstItemVisible);
        } else {
            sr.setEnabled(enable);
        }
    }

    public class ListenerItem implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setFragment(new FragmentProductDetails(alProduct.get(position), toolbarColor));
        }
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    private void loadProducts() {
        new HTTPTbs(this, ld) {
            @Override
            public String url() {
                return Api.GET_PRODUCT_LIST;
            }

            @Override
            public void onSuccess(JSONObject j) {
                sr.setRefreshing(false);
                try {
                    alProduct.clear();
                    alProduct.addAll(Converter.toProductList(j.getString(Keys.RESULTS)));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFail(int code, String message) {
                super.onFail(code, message);
                sr.setRefreshing(false);
            }
        }.setGetParams(Keys.ID, bean.getCode()).setGetParams("type", "base").setGetParams(Keys.PROD_GET_VARIANT, "1")
         .execute();
    }

    private void search() {
        new HTTPTbs(this, ld) {
            @Override
            public int page() {
                return paginationPage();
            }

            @Override
            public String url() {
                return Api.SEARCH_PRODUCT_BY_ID;
            }

            @Override
            public String search() {
                return search;
            }

            @Override
            public void onSuccess(JSONObject j) {
                sr.setRefreshing(false);
                try {
                    if (paginationPage() == 1)
                        alProduct.clear();

                    alProduct.addAll(Converter.toProductList(j.getString(Keys.RESULTS)));
                    if (alProduct.size() == 1) {
                        ld.hide();
                        setFragment(new FragmentProductDetails(alProduct.get(0)));
                    } else
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

    private void scan() {
        new HTTPTbs(this, ld) {
            @Override
            public String url() {
                // TODO Auto-generated method stub
                return Api.SEARCH_SCAN;
            }

            @Override
            public void onSuccess(JSONObject j) {
                sr.setRefreshing(false);
                try {
                    alProduct.clear();
                    alProduct.addAll(Converter.toProductList("[" + j.getString(Keys.BASE) + "]"));
                    if (alProduct.size() == 1) {
                        ld.hide();
                        setFragment(new FragmentProductDetails(alProduct.get(0)));
                    } else
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
        }.setGetParams(Keys.ID, search).execute();
    }

}
