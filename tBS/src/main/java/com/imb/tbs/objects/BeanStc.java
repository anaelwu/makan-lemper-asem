package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanStc
	extends SimpleBean {

	private String	text;
	private long	limit;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}
}
