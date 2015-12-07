package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.UIHelper;
import com.imb.tbs.objects.BeanStore;

import roboguice.inject.InjectView;

public class FragmentStoreDetails
        extends BaseFragmentTbs {
    @InjectView(R.id.tvName)
    private TextView        tvName;
    @InjectView(R.id.tvAddress)
    private TextView        tvAddress;
    @InjectView(R.id.btnCall)
    private ImageButton     btnCall;
    @InjectView(R.id.btnEmail)
    private ImageButton     btnEmail;
    @InjectView(R.id.img)
    private ImageViewLoader imgMap;
    @InjectView(R.id.btnDirections)
    private Button          btnDirections;
    @InjectView(R.id.tvOpening)
    private TextView        tvOpening;
    private String          mapUrl;
    private BeanStore       store;
    public static final int TAG_CALL = 1, TAG_EMAIL = 2, TAG_DIRECTION = 3;

    public FragmentStoreDetails(BeanStore store) {
        this.store = store;
    }

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.fragment_store_details;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        tvName.setText(store.getName());
        if (Helper.isEmpty(store.getAddress()))
            tvAddress.setText("Address is not available");
        else
            tvAddress.setText(store.getAddress());

        if (Helper.isLangIndo() && !Helper.isEmpty(store.getOpeningIn()))
            tvOpening.setText("Business hour :\n" + store.getOpeningIn());
        else if (!Helper.isEmpty(store.getOpeningEng()))
            tvOpening.setText("Business hour :\n" + store.getOpeningEng());
        else
            tvOpening.setVisibility(View.GONE);

        mapUrl = UIHelper
                .getMapUrl(getActivity(), store.getLatitude(), store.getLongitude());
        if (!Helper.isEmpty(mapUrl)) {
            imgMap.loadImage(mapUrl);
        } else
            imgMap.showFail();

        btnCall.setTag(TAG_CALL);
        btnEmail.setTag(TAG_EMAIL);
        btnDirections.setTag(TAG_DIRECTION);

        btnCall.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnDirections.setOnClickListener(this);
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    // ================================================================================
    // Listener
    // ================================================================================
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch ((Integer) v.getTag()) {
            case TAG_CALL:
                if (Helper.isEmpty(store.getEmail()))
                    Helper.showAlert(getActivity(), "Phone is not available");
                else
                    Helper.intentCall(getActivity(), store.getPhone());
                break;

            case TAG_EMAIL:
                if (Helper.isEmpty(store.getEmail()))
                    Helper.showAlert(getActivity(), "Email is not available");
                else
                    Helper.intentEmail(getActivity(), store.getEmail(), store.getName() + " Inquiry");
                break;

            case TAG_DIRECTION:
                Helper.intentDirection(getActivity(), store.getLatitude(), store.getLongitude());
                break;
        }
    }

}
