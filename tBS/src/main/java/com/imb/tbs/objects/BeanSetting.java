package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanSetting
        extends SimpleBean {
    private String address;
    private String email;
    private String facebook;
    private String twitter;
    private String youtube;
    private String pinterest;
    private String instagram;
    private String tnc;
    private String twitterId;
    private String phone1;
    private String phone2;
    private String openingHour;
    private String barcode;

    public String getQrcode() {
        return qrcode;
    }

    public BeanSetting setQrcode(String qrcode) {
        this.qrcode = qrcode;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public BeanSetting setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    private String qrcode;

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getPinterest() {
        return pinterest;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }
}
