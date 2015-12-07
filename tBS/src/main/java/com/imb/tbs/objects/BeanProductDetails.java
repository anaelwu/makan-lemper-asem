package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.Helper;

public class BeanProductDetails
        extends SimpleBean {
    String nameIn, variant, variantIn, code, howto, howtoIn, article, articleIn, ingre, ingreIn, weight, img,
            imgVariant, url, tips, tipsIn;
    int  variantId;
    long price;
    // Debug
    int  resImg;

    public String getNameIn() {
        return nameIn;
    }

    public BeanProductDetails setNameIn(String nameIn) {
        this.nameIn = nameIn;

        return this;
    }

    public String getCode() {
        return code;
    }

    public BeanProductDetails setCode(String code) {
        this.code = code;

        return this;
    }

    public String getHowto() {
        return howto;
    }

    public BeanProductDetails setHowto(String howto) {
        this.howto = howto;

        return this;
    }

    public String getHowtoIn() {
        return howtoIn;
    }

    public BeanProductDetails setHowtoIn(String howtoIn) {
        this.howtoIn = howtoIn;

        return this;
    }

    public String getArticle() {
        return article;
    }

    public BeanProductDetails setArticle(String article) {
        this.article = article;

        return this;
    }

    public String getArticleIn() {
        return articleIn;
    }

    public BeanProductDetails setArticleIn(String articleIn) {
        this.articleIn = articleIn;

        return this;
    }

    public String getIngre() {
        return ingre;
    }

    public BeanProductDetails setIngre(String ingre) {
        this.ingre = ingre;

        return this;
    }

    public String getIngreIn() {
        return ingreIn;
    }

    public BeanProductDetails setIngreIn(String ingreIn) {
        this.ingreIn = ingreIn;

        return this;
    }

    public String getWeight() {
        return weight;
    }

    public BeanProductDetails setWeight(String weight) {
        this.weight = weight;

        return this;
    }

    public long getPrice() {
        return price;
    }

    public BeanProductDetails setPrice(long price) {
        this.price = price;

        return this;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getVariant() {
        return variant;
    }

    public String getVariantIn() {
        return variantIn;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setVariantIn(String variantIn) {
        this.variantIn = variantIn;
    }

    public String getImg() {
        if (Helper.isEmpty(img))
            return "";

        return Api.PRODUCT_IMAGE_BY_NAME + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResImg(int resImg) {
        this.resImg = resImg;
    }

    public String getImgVariant() {
        if (Helper.isEmpty(imgVariant))
            return "";

        return Api.PRODUCT_IMAGE_BY_NAME + imgVariant;
    }

    public void setImgVariant(String imgVariant) {
        this.imgVariant = imgVariant;
    }

    public int getResImg() {
        return resImg;
    }

    public String getTipsIn() {
        return tipsIn;
    }

    public BeanProductDetails setTipsIn(String tipsIn) {
        this.tipsIn = tipsIn;
        return this;
    }

    public String getTips() {

        return tips;
    }

    public BeanProductDetails setTips(String tips) {
        this.tips = tips;
        return this;
    }
}
