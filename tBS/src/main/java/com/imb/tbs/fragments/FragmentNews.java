package com.imb.tbs.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterCampaign;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.objects.BeanCampaign;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

public class FragmentNews
        extends BaseFragmentTbs implements OnItemClickListener, OnRefreshListener {
    @InjectView(R.id.lv)
    private ListView           lv;
    @InjectView(R.id.ld)
    private LoadingCompound    ld;
    @InjectView(R.id.sr)
    private SwipeRefreshLayout sr;
    private ArrayList<BeanCampaign> alCampaign = new ArrayList<BeanCampaign>();
    private AdapterCampaign mAdapter;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.fragment_campaign;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        lv = (ListView) view.findViewById(R.id.lv);

        init();

        if (alCampaign == null || alCampaign.isEmpty()) {
            loadList();
        } else {
            ld.hide();
        }
    }

    @Override
    public int setMenuLayout() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void init() {
        setTitle(R.string.news);

        mAdapter = new AdapterCampaign(getActivity(), alCampaign);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);
        lv.setOnScrollListener(this);

        Helper.setRefreshColor(sr);
        sr.setOnRefreshListener(this);
    }

    // ================================================================================
    // Listener
    // ================================================================================
    @Override
    public boolean pagination() {
        return true;
    }

    @Override
    public int paginationCount() {
        return alCampaign.size();
    }

    @Override
    public void onPagination(int page) {
        super.onPagination(page);
        loadList();
    }

    @Override
    public void onRefresh() {
        paginationPage(1);
        loadList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BeanCampaign bean = alCampaign.get(position);
        if (Helper.isEmpty(bean.getUrl())) {
            setFragment(new FragmentCampaignDetails(bean));
        } else {
            setFragment(new FragmentWebview(bean.getName(), bean.getUrl()));
        }
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    private void loadList() {
        new HTTPTbs(this, ld) {
            @Override
            public String url() {
                return Api.GET_NEWS;
            }

            @Override
            public void onSuccess(JSONObject j) {
                sr.setRefreshing(false);
                try {
                    alCampaign.clear();
                    alCampaign.addAll(Converter.toNews(j.getString(Keys.RESULTS)));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, String message) {
                sr.setRefreshing(false);
                if (paginationPage() == 1) {
                    super.onFail(code, message);
                }
            }

            @Override
            public int page() {
                return paginationPage();
            }

            public String search() {
                return Helper.toSearchQuery(Keys.NEWS_ACTIVE, 1);
            }
        }.execute();
    }

}
