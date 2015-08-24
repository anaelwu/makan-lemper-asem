package com.imb.tbs.objects;

import org.joda.time.DateTime;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;

public class BeanCampaign
	extends SimpleBean {
	private String		imgUrl, desc, url;
	private DateTime	dateCreated;

	public BeanCampaign(int id, String name) {
		super(id, name);
	}

	public String getImgUrl() {
		return Api.BASE_URL + imgUrl;
	}

	public String getDesc() {
		return desc;
	}

	public String getUrl() {
		return url;
	}

	public BeanCampaign setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;

		return this;
	}

	public BeanCampaign setDesc(String desc) {
		this.desc = desc;

		return this;
	}

	public BeanCampaign setUrl(String url) {
		this.url = url;

		return this;
	}

	public DateTime getDateCreated() {
		return dateCreated;
	}

	public BeanCampaign setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;

		return this;
	}
}
