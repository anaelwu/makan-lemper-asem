package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanDetails
	extends SimpleBean {
	private String	desc;

	public BeanDetails(String name, String desc) {
		setName(name);
		setDesc(desc);
	}

	public String getDesc() {
		return desc;
	}

	public BeanDetails setDesc(String desc) {
		this.desc = desc;

		return this;
	}
}
