package com.imb.tbs.objects;

import java.util.ArrayList;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.objects.SimpleBeanParent;
import com.imb.tbs.helpers.Api;

public class BeanProduct
	extends SimpleBeanParent {
	private String	imgUrl, url, code;
	private int		color, order;

	public BeanProduct(int id, String name) {
		super(id, name);
	}

	public BeanProduct() {
	}

	public String getImgUrl() {
		return Api.PRODUCT_IMAGE_BY_NAME + imgUrl;
	}

	public String getUrl() {
		return url;
	}

	public BeanProduct setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;

		return this;
	}

	public BeanProduct setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public BeanProduct setChildList(ArrayList<? extends SimpleBean> childList) {
		return (BeanProduct) super.setChildList(childList);
	}

	public String getCode() {
		return code;
	}

	public BeanProduct setCode(String code) {
		this.code = code;

		return this;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
