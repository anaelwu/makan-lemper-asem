package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterInbox;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanNotif;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import roboguice.inject.InjectView;

/**
 * Created by marcelsantoso.
 * <p/>
 * 9/4/15
 */
public class FragmentInbox extends BaseFragmentTbs {
    @InjectView(R.id.ld)
    private LoadingCompound ld;
    @InjectView(R.id.lv)
    private ListView        lv;
    private ArrayList<BeanNotif> al = new ArrayList<>();
    private AdapterInbox adapter;
    private Menu         menu;
    
    @Override
    public int setLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public int setMenuLayout() {
        return R.menu.remove;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        setMenuIcon();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_remove:
                adapter.toggleRemove();
                setMenuIcon();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMenuIcon() {
        MenuItem item = menu.getItem(0);

        if (adapter.isShowRemove())
            item.setIcon(R.drawable.ic_done);
        else
            item.setIcon(R.drawable.ic_trash);
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        setTitle(R.string.inbox);
        getNotif();

        adapter = new AdapterInbox(getActivity(), al, R.layout.cell_inbox) {
            @Override
            public void onRemove(int pos) {
                al.remove(pos);
                adapter.notifyDataSetChanged();
                updateNotif();

                if (al.size() == 0) {
                    ld.showError("", getString(R.string.no_inbox));
                }
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        setFragment(new FragmentWebview(al.get(position).getTitle(), al.get(position).getUrl()).setCampId(
                al.get(position).getCampId()));
    }

    private void getNotif() {
        al.clear();

        String text = getPref().getString(Preference.PUSH_NOTIF);
        if (Helper.isEmpty(text)) {
            ld.showError("", getString(R.string.no_inbox));
            return;
        }

        try {
            JSONArray jArr = new JSONArray(text);
            for (int i = jArr.length() - 1; i >= 0; i--) {
                al.add(new BeanNotif().parseToObject(jArr.getJSONObject(i)));
            }

            ld.hide();

            if (al.size() == 0) {
                ld.showError("", getString(R.string.no_inbox));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateNotif() {
        JSONArray jArr = new JSONArray();
        for (BeanNotif bean : al) {
            jArr.put(bean.parseToText());
        }

        getPref().setString(Preference.PUSH_NOTIF, jArr.toString());
    }
}
