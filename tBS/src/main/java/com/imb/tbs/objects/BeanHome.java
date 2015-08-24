package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.BaseFragmentTbs;

public class BeanHome
	extends SimpleBean {

	private BaseFragmentTbs	frag;
	private int				color, img;

	public BeanHome(int id, String name) {
		super(id, name);
	}

	public BeanHome() {
	}

	public BaseFragmentTbs getFrag() {
		return frag;
	}

	public BeanHome setFrag(BaseFragmentTbs frag) {
		this.frag = frag;

		return this;
	}

	public int getColor() {
		return color;
	}

	public int getImg() {
		return img;
	}

	public BeanHome setColor(int color) {
		this.color = color;

		return this;
	}

	public BeanHome setImg(int img) {
		this.img = img;

		return this;
	}
}
