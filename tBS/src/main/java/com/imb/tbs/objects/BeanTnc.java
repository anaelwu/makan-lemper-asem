package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanTnc
	extends SimpleBean {
	private boolean	expanded;
	private String	title, content;

	public boolean isExpanded() {
		return expanded;
	}

	public BeanTnc setExpanded(boolean expanded) {
		this.expanded = expanded;

		return this;
	}

	public String getTitle() {
		return title;
	}

	public BeanTnc setTitle(String title) {
		this.title = title;

		return this;
	}

	public String getContent() {
		return content;
	}

	public BeanTnc setContent(String content) {
		this.content = content;

		return this;
	}

}
