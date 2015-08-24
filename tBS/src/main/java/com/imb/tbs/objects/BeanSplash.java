package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanSplash extends SimpleBean {

	int	content;
	boolean	isImg;

	public BeanSplash() {
		super(0, "");
	}

	public int getContent() {
		return content;
	}

	public boolean isImg() {
		return isImg;
	}

	public BeanSplash setContent(int content) {
		this.content = content;

		return this;
	}

	public BeanSplash setImg(boolean isImg) {
		this.isImg = isImg;

		return this;
	}

}
