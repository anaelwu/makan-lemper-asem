package com.imb.tbs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.facebook.Response;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.helpers.FacebookImage;
import com.iapps.libs.helpers.FacebookLogin;
import com.iapps.libs.views.ImageViewLoader;
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

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

public class FragmentSignup
        extends BaseFragmentTbs implements OnClickListener,
                                           PinPickerDialogHandler {
    @InjectView(R.id.imgBg)
    private ImageViewLoader imgBg;
    @InjectView(R.id.imgProfile)
    private ImageViewLoader imgProfile;
    @InjectView(R.id.edtName)
    private EditText        edtName;
    @InjectView(R.id.edtFamily)
    private EditText        edtFamily;
    @InjectView(R.id.edtEmail)
    private EditText        edtEmail;
    @InjectView(R.id.edtPhone)
    private EditText        edtPhone;
    @InjectView(R.id.edtDob)
    private EditText        edtDob;
    @InjectView(R.id.btnSubmit)
    private Button          btnSubmit;
    @InjectView(R.id.btnFb)
    private Button          btnFb;
    @InjectView(R.id.btnSkip)
    private Button          btnSkip;
    @InjectView(R.id.sv)
    private ScrollView      sv;
    @InjectView(R.id.rbMale)
    private RadioButton     rbMale;
    @InjectView(R.id.rbFemale)
    private RadioButton     rbFemale;
    private FacebookLogin   fb;
    private String          fbId;
    private boolean         isUploadPhoto;
    public  Response        response;
    public static final int TAG_PHONE = 1, TAG_DOB = 2, TAG_SUBMIT = 3, TAG_FB = 4,
            TAG_SKIP                  = 5, TAG_IMAGE = 6;

    public FragmentSignup() {
    }

    public FragmentSignup(Response response) {
        this.response = response;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_signup;
    }

    @Override
    public int setMenuLayout() {
        return 0;
    }

    @Override
    public void setView(View view, Bundle savedInstanceState) {
        setTitle(R.string.signup);
        setToolbarColor(R.drawable.tab_unselected_actionbar_bg);

        imgProfile.loadImage(R.drawable.ic_person);

        edtPhone.setFocusable(false);
        edtDob.setFocusable(false);
        edtPhone.setLongClickable(false);
        edtDob.setLongClickable(false);

        edtPhone.setTag(TAG_PHONE);
        edtDob.setTag(TAG_DOB);
        btnSubmit.setTag(TAG_SUBMIT);
        btnFb.setTag(TAG_FB);
        btnSkip.setTag(TAG_SKIP);
        imgProfile.setTag(TAG_IMAGE);

        edtPhone.setOnClickListener(this);
        edtDob.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        setScrollListener(sv);

        if (response != null) {
            fbId = Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID).toString();
            fillFbDetails();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoginActivity().getToolbar().setTitle("");
        hideToolbar();
    }

    public boolean validate() {
        return Helper.validateEditTexts(new EditText[]{
                edtName, edtFamily, edtEmail, edtPhone, edtDob
        });
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // ================================================================================
    // Event Listeners
    // ================================================================================
    @Override
    public void onClick(View v) {
        switch ((Integer) v.getTag()) {
            case TAG_PHONE:
                Helper.numberPicker(this, (Integer) v.getTag());
                break;

            case TAG_DOB:
                BaseHelper.datePicker(edtDob);
                break;

            case TAG_FB:
                fb = new FacebookLogin(getString(R.string.fb_app_id), this) {
                    public void onFbLoginSuccess(Response response) {
                        setResponse(response);
                        checkFb(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID).toString());
                        // checkFb(fbId);
                        // fillFbDetails();
                    }
                };
                fb.execute();
                break;

            case TAG_SUBMIT:
                if (validate()) {
                    if (!Helper.validateEmail(edtEmail))
                        return;

                    // register();
                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.USER_FIRST_NAME, edtName.getText().toString());
                    bundle.putString(Keys.USER_LAST_NAME, edtFamily.getText().toString());
                    bundle.putString(Keys.USER_EMAIL, edtEmail.getText().toString());
                    bundle.putString(Keys.USER_PHONE, Constants.PHONE_EXTENSION + edtPhone.getText().toString());
                    bundle.putString(Keys.USER_DOB,
                                     Helper.formatDateTime(edtDob.getText().toString(), Constants.DATE_MDY,
                                                           Constants.DATE_JSON));
                    bundle.putString(Keys.USER_GENDER, rbMale.isChecked() ? Constants.MALE : Constants.FEMALE);
                    if (isUploadPhoto)
                        bundle.putString(Keys.USER_PHOTO, imgProfile.getUploadFormat());
                    bundle.putString(Keys.USER_FB_ID, fbId);
                    setFragment(new FragmentLinkLyb(bundle));
                }
                break;

            case TAG_SKIP:
                getLoginActivity().changeActivity(false);
                break;

            case TAG_IMAGE:
                Helper.pickImage(this);
                break;
        }
    }

    @Override
    public void onDialogPinSet(int reference, String pin) {
        switch (reference) {
            case TAG_PHONE:
                edtPhone.setText(pin);
                break;
        }
    }

    //	@Override
    //	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
    //		DateTime dt = DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
    //		edtDob.setText(dt.toString(Constants.DATE_MDY));
    //	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_GET_IMAGE_CODE && resultCode == -1) {
            BaseUIHelper.processImage(getActivity(), imgProfile.getImage());
            isUploadPhoto = true;
        } else if (requestCode != Helper.REQUEST_GET_IMAGE_CODE) {
            fb.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void fillFbDetails() {
        // Fill details
        edtName.setText(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_FIRST_NAME).toString());
        edtFamily.setText(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_LAST_NAME)
                                .toString());
        edtEmail.setText(Helper.getObjectFromFacebook(response, Keys.FACEBOOK_EMAIL).toString());
        try {
            String bday = Helper.getObjectFromFacebook(response, Keys.FACEBOOK_BDAY).toString();
            edtDob.setText(Helper.parseDateTime(bday, Constants.DATE_FACEBOOK).toString(
                    Constants.DATE_MDY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Helper.getObjectFromFacebook(response, Keys.FACEBOOK_GENDER).equals(Keys.FACEBOOK_MALE))
            rbMale.setChecked(true);
        else
            rbFemale.setChecked(true);

        fbId = Helper.getObjectFromFacebook(response, Keys.FACEBOOK_ID).toString();
        new FacebookImage(imgProfile, fbId) {
            @Override
            public void onSuccess(byte[] avatar) {
                isUploadPhoto = true;
            }
        }.execute();

        btnFb.setVisibility(View.GONE);
    }

    // ================================================================================
    // Webservice
    // ================================================================================
    public void register() {
        HTTPTbs register = new HTTPTbs(this, true) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    // result will be returned as object, to make it consistent, convert to array
                    String result = j.getString(Keys.RESULTS);
                    // JSONArray jArray = new JSONArray();
                    // jArray.put(new JSONObject(result));
                    // result = jArray.toString();
                    getPref().setString(Preference.USER_DETAILS, result);

                    BeanProfile bean = Converter.toProfile(result);
                    if (bean.getCard() == 0) {
                        // setFragment(new FragmentLinkLyb(bean.getDob()));
                    } else {
                        getLoginActivity().changeActivity(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String url() {
                return Api.REGISTER_USER;
            }

            @Override
            public int connectionTimeout() {
                return 0;
            }
        };

        register.setPostParams(Keys.USER_EMAIL, edtEmail.getText().toString());
        register.setPostParams(Keys.USER_FIRST_NAME, edtName.getText().toString());
        register.setPostParams(Keys.USER_LAST_NAME, edtFamily.getText().toString());
        register.setPostParams(Keys.USER_PHONE, edtPhone.getText().toString());
        register.setPostParams(Keys.USER_GENDER, rbMale.isChecked() ? Constants.MALE : Constants.FEMALE);
        register.setPostParams(
                Keys.USER_DOB,
                Helper.parseDateTime(edtDob.getText().toString(), Constants.DATE_MDY).toString(
                        Constants.DATE_JSON));
        if (!isUploadPhoto)
            register.setPostParams(Keys.USER_PHOTO, imgProfile.getUploadFormat());

        if (!Helper.isEmpty(fbId))
            register.setPostParams(Keys.USER_FB_ID, fbId);

        register.execute();
    }

    public void checkFb(String fbId) {
        new HTTPTbs(this, true) {
            @Override
            public void onSuccess(JSONObject j) {
                try {
                    getPref().setString(Preference.USER_DETAILS, j.getString(Keys.RESULTS));
                    getLoginActivity().changeActivity(true);
                } catch (JSONException e) {
                    Helper.showUnknownResponseError(getActivity());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code, JSONObject j) {
                fillFbDetails();
            }

            @Override
            public String url() {
                // TODO Auto-generated method stub
                return Api.LOGIN_WITH_FB;
            }
        }.setPostParams(Keys.USER_FB_ID, fbId).execute();
    }

}
