package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.Helper;

public class BeanCarousel
	extends SimpleBean {
	private String	img, url;

	public String getImg() {
		if (Helper.isEmpty(url))
			return "";

		return Api.BASE_URL + img;
	}

	public BeanCarousel setImg(String img) {
		this.img = img;

		return this;
	}

	public String getUrl() {
		return url;
	}

	public BeanCarousel setUrl(String url) {
		this.url = url;

		return this;
	}

}
