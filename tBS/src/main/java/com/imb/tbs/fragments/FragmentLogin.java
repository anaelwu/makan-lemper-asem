package com.imb.tbs.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanProfile;
import com.material.widget.FloatingEditText;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentLogin
        extends BaseFragmentTbs implements PinPickerDialogHandler {
    @InjectView(R.id.btnLogin)
    private Button           btnLogin;
    @InjectView(R.id.btnHelp)
    private ImageButton      btnHelp;
    @InjectView(R.id.btnContact)
    private Button           btnContact;
    @InjectView(R.id.edtCard)
    private FloatingEditText edtCard;
    @InjectView(R.id.edtDob)
    private FloatingEditText edtDob;
    public static final int TAG_LOGIN = 1, TAG_HELP = 2, TAG_CONTACT = 3, TAG_CARD = 4,
            TAG_DOB                   = 5;

    @Override
    public int setLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        title = "Login";
        setTitle("");
        btnLogin.setTag(TAG_LOGIN);
        btnContact.setTag(TAG_CONTACT);
        btnHelp.setTag(TAG_HELP);
        edtCard.setTag(TAG_CARD);
        edtDob.setTag(TAG_DOB);

        btnLogin.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        edtCard.setOnClickListener(this);
        edtDob.setOnClickListener(this);
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    public boolean validate() {
        if (Helper.isEmpty(edtCard)) {
            Helper.showRequired(getActivity(), edtCard.getHint().toString());
            return false;
        }

        if (Helper.isEmpty(edtDob)) {
            Helper.showRequired(getActivity(), edtDob.getHint().toString());
            return false;
        }

        return true;
    }

    // ================================================================================
    // Listeners
    // ================================================================================
    @Override
    public void onClick(View v) {
        switch ((Integer) v.getTag()) {
            case TAG_LOGIN:
                if (validate()) {
                    loginLyb();
                }
                break;

            case TAG_CARD:
                popupPicker(FragmentLogin.this).setDisplayAsPassword(false)
                                               .setDecimalVisibility(View.GONE)
                                               .setPlusMinusVisibility(View.GONE).setAllowZero(true)
                                               .setReference((Integer) v.getTag()).show();
                break;

            case TAG_DOB:
                Helper.datePicker(edtDob);
                break;

            case TAG_CONTACT:
                setToolbarColor(R.drawable.tab_unselected_actionbar_bg);
                setFragment(new FragmentContact());
                break;

            case TAG_HELP:
                Helper.popupHelp(getActivity());
                break;

        }
    }

    @Override
    public void onDialogPinSet(int reference, String pin) {
        switch (reference) {
            case TAG_CARD:
                edtCard.setText(pin);
                break;
        }
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    public void loginLyb() {
        HTTPTbs check = new HTTPTbs(this, true) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    BeanProfile profile = Converter.toProfile(j.getString(Keys.RESULTS));
                    getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
                    if (!Helper.isEmpty(profile.getFbId())) {
                        getLoginActivity().changeActivity(true);
                    } else {
                        FragmentLogin.this.setFragment(new FragmentLinkFb());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String url() {
                return Api.LOGIN_WITH_CARD;
            }
        };
        check.setPostParams(Keys.CARD, edtCard);
        check.setPostParams(Keys.DOB,
                            Helper.formatDateTime(edtDob.getText().toString(), Constants.DATE_MDY,
                                                  Constants.DATE_JSON));
        check.execute();
    }
}
