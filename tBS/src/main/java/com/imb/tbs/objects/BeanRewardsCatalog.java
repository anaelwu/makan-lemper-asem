package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;

public class BeanRewardsCatalog
	extends SimpleBean {

	private int		status, point;
	private String	imgUrl;

	public BeanRewardsCatalog(int id, String name) {
		super(id, name);
	}

	public int getStatus() {
		return status;
	}

	public int getPoint() {
		return point;
	}

	public String getImgUrl() {
		return Api.BASE_URL + imgUrl;
	}

	public BeanRewardsCatalog setStatus(int status) {
		this.status = status;

		return this;
	}

	public BeanRewardsCatalog setPoint(int point) {
		this.point = point;

		return this;
	}

	public BeanRewardsCatalog setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;

		return this;
	}

}
