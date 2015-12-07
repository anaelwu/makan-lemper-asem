package com.imb.tbs.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.activities.ActivityHome;
import com.imb.tbs.activities.ActivityVcard;
import com.imb.tbs.adapters.AdapterHome;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanCarousel;
import com.imb.tbs.objects.BeanHome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.InjectView;

public class FragmentHome
        extends BaseFragmentTbs implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.lv)
    private ListView           lv;
    @InjectView(R.id.sr)
    private SwipeRefreshLayout sr;
    private ImageViewLoader    imgHeader;
    private ArrayList<BeanHome> alHome = new ArrayList<BeanHome>();
    private AdapterHome  adapter;
    private boolean      loadProfile;
    private BeanCarousel bean;

    public FragmentHome() {
        // TODO Auto-generated constructor stub
    }

    public FragmentHome(boolean loadProfile) {
        this.loadProfile = loadProfile;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Home";
        getHomeActivity().resetIndex();
        setTitle(R.string.app_name);
        setToolbarColor(R.drawable.tab_unselected_actionbar_bg);

        Helper.setRefreshColor(sr);
        sr.setOnRefreshListener(this);

        initList();
        initView();

        if (loadProfile && getProfile() != null)
            loadProfile();

        loadSettings();
        checkUpdate();
    }

    @Override
    public int setMenuLayout() {
        if (getProfile() != null)
            return R.menu.qr;
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_qr) {
            if (getProfile().getStatus().equalsIgnoreCase(Constants.STATUS_STAMP))
                startActivity(new Intent(getActivity(), ActivityVcard.class));
            else
                setFragment(new FragmentVcardMember());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        alHome.clear();

        alHome.add(new BeanHome(ActivityHome.TAG_CAMPAIGN, getString(R.string.campaign)).setColor(
                R.drawable.home_1_xml).setImg(R.drawable.ic_home_programs));
        alHome.add(new BeanHome(ActivityHome.TAG_REWARDS, getString(R.string.rewards)).setColor(
                R.drawable.home_2_xml).setImg(R.drawable.ic_home_rewards));
        alHome.add(new BeanHome(ActivityHome.TAG_NEWS, getString(R.string.news)).setColor(
                R.drawable.home_3_xml).setImg(R.drawable.ic_home_news));
        alHome.add(new BeanHome(ActivityHome.TAG_PRODUCT, getString(R.string.product)).setColor(
                R.drawable.home_4_xml).setImg(R.drawable.ic_home_product));
        alHome.add(new BeanHome(ActivityHome.TAG_WISHLIST, getString(R.string.wishlist)).setColor(
                R.drawable.home_5_xml).setImg(R.drawable.ic_home_wishlist));
        alHome.add(new BeanHome(ActivityHome.TAG_SCAN, getString(R.string.scan_product)).setColor(
                R.drawable.home_6_xml).setImg(R.drawable.ic_home_scan));
        alHome.add(new BeanHome(ActivityHome.TAG_STORE, getString(R.string.store_locators))
                           .setColor(
                                   R.drawable.home_7_xml).setImg(R.drawable.ic_home_store));
        alHome.add(new BeanHome(ActivityHome.TAG_CONNECT, getString(R.string.connect)).setColor(
                R.drawable.home_8_xml).setImg(R.drawable.ic_home_connect));
        alHome.add(new BeanHome(ActivityHome.TAG_CONTACT, getString(R.string.contact_us)).setColor(
                R.drawable.home_9_xml).setImg(R.drawable.ic_home_contact));
        alHome.add(new BeanHome(ActivityHome.TAG_INBOX, getString(R.string.inbox)).setImg(R.drawable.ic_inbox)
                                                                                  .setColor(R.drawable.home_10_xml));
    }

    private void initView() {
        lv.addHeaderView(initHeader());
        adapter = new AdapterHome(getActivity(), alHome);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    private View initHeader() {
        View header = getActivity().getLayoutInflater().inflate(R.layout.cell_home_header, null);
        imgHeader = (ImageViewLoader) header.findViewById(R.id.img);
        imgHeader.setSquareToWidth(true);
        imgHeader.setBackgroundColor(Color.WHITE);

        getCarousel();
        loadCarousel();

        return header;
    }

    private void getCarousel() {
        if (!Helper.isEmpty(getPref().getString(Preference.CAROUSEL))) {
            bean = Converter.toCarousel(getPref().getString(Preference.CAROUSEL));
            showCarousel();
        }
    }

    private void showCarousel() {
        if (bean != null)
            imgHeader
                    .loadImage(bean.getImg());
        else
            getCarousel();
    }

    // ================================================================================
    // Listeners
    // ================================================================================
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0)
            getHomeActivity().clickDrawer(alHome.get(position - 1).getId());
        else {
            if (bean != null)
                setFragment(new FragmentWebview(bean.getName(), bean.getUrl()));
        }
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    public void loadProfile() {
        loadProfile = false;
        loadStcSetting();

        new HTTPTbs(this, false) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
                    getHomeActivity().updateUser();
                    sr.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, JSONObject j) {
                super.onFail(code, j);
                sr.setRefreshing(false);
            }

            @Override
            public String url() {
                return Api.LOGIN_WITH_CARD;
            }
        }.setPostParams(Keys.CARD, Long.toString(getProfile().getCard()))
         .setPostParams(Keys.DOB, getProfile().getDob().toString(Constants.DATE_JSON)).execute();
    }

    private void loadStcSetting() {
        new HTTPTbs(this, false) {
            @Override
            public String url() {
                return Api.STC_SETTING;
            }

            @Override
            public void onSuccess(JSONObject j) {
                getPref().setString(Preference.STC_LIMIT, j.toString());
            }
        }.execute();
    }

    private void loadCarousel() {
        new HTTPTbs(this, false) {
            @Override
            public String url() {
                return Api.GET_CAROUSEL;
            }

            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.CAROUSEL, j.getString(Keys.RESULTS));
                    getCarousel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public String search() {
                return Helper.toSearchQuery("carousel_active", 1);
            }
        }.execute();
    }

    public void loadSettings() {
        new HTTPTbs(this, false) {
            @Override
            public String url() {
                // TODO Auto-generated method stub
                return Api.SETTINGS;
            }

            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.SETTINGS, j.getString(Keys.RESULTS));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void checkUpdate() {
        new HTTPTbs(this, false) {
            @Override
            public void onSuccess(final JSONObject j) {
                try {
                    if (j.getJSONObject(Keys.RESULTS).getInt("ada_update") == 1) {
                        Helper.confirm(getActivity(), R.string.update_title, R.string.update_content,
                                       new BaseHelper.ConfirmListener() {
                                           @Override
                                           public void onYes() {
                                               try {
                                                   Helper.intentPlaystore(getActivity(),
                                                                          j.getJSONObject(Keys.RESULTS).getString(
                                                                                  "url"));
                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }
                                           }
                                       });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String url() {
                return Api.UPDATE_CHECK;
            }
        }.setGetParams("version", Constants.VERSION_INT).setGetParams("os", "android").execute();
    }

    @Override
    public void onRefresh() {
        if (getProfile() != null)
            loadProfile();
        else
            sr.setRefreshing(false);

        loadSettings();
        loadCarousel();
        checkUpdate();
    }
}
