package com.imb.tbs.objects;

import org.joda.time.DateTime;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;

public class BeanReview
	extends SimpleBean {

	private String		imgUrl, comment;
	private DateTime	date;

	public BeanReview(int id, String name) {
		super(id, name);
	}

	public String getImgUrl() {
		return Api.BASE_URL_LOCAL + imgUrl;
	}

	public String getComment() {
		return comment;
	}

	public BeanReview setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
		return this;
	}

	public BeanReview setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

}
