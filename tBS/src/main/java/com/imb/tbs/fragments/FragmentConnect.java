package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanSetting;

import roboguice.inject.InjectView;

public class FragmentConnect
        extends BaseFragmentTbs implements OnClickListener {
    @InjectView(R.id.imgFb)
    private ImageView imgFb;
    @InjectView(R.id.imgTwitter)
    private ImageView imgTwitter;
    @InjectView(R.id.imgYoutube)
    private ImageView imgYoutube;
    @InjectView(R.id.imgPinterest)
    private ImageView imgPinterest;
    @InjectView(R.id.imgInstagram)
    private ImageView imgInstagram;
    public static final int TAG_FB = 1, TAG_TWITTER = 2, TAG_IG = 3, TAG_PINTEREST = 4, TAG_YOUTUBE = 5;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.fragment_connect;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        setTitle(R.string.connect);
        imgFb.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgInstagram.setOnClickListener(this);
        imgPinterest.setOnClickListener(this);
        imgYoutube.setOnClickListener(this);

        imgFb.setTag(TAG_FB);
        imgTwitter.setTag(TAG_TWITTER);
        imgInstagram.setTag(TAG_IG);
        imgPinterest.setTag(TAG_PINTEREST);
        imgYoutube.setTag(TAG_YOUTUBE);
    }

    @Override
    public int setMenuLayout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onClick(View v) {
        BeanSetting bean = Converter.toSettings(getPref().getString(Preference.SETTINGS));
        if (bean == null)
            return;

        switch ((Integer) v.getTag()) {
            case TAG_FB:
                Helper.intentFacebook(getActivity(), bean.getFacebook());
                break;

            case TAG_TWITTER:
                Helper.intentTwitter(getActivity(), bean.getTwitter(), bean.getTwitterId());
                break;

            case TAG_IG:
                Helper.intentInstagram(getActivity(), bean.getInstagram());
                break;

            case TAG_PINTEREST:
                Helper.intentPinterest(getActivity(), bean.getPinterest());
                break;

            case TAG_YOUTUBE:
                Helper.intentYoutube(getActivity(), bean.getYoutube());
                break;
        }
    }

}
