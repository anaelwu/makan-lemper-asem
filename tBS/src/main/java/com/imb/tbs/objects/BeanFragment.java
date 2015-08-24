package com.imb.tbs.objects;

import android.support.v4.app.Fragment;

import com.iapps.libs.objects.SimpleBean;

public class BeanFragment extends SimpleBean {

	Fragment	fragment;

	public BeanFragment(int id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}

	public Fragment getFragment() {
		return fragment;
	}

	public BeanFragment setFragment(Fragment fragment) {
		this.fragment = fragment;

		return this;
	}

}
