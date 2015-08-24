package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;

public class BeanStore
	extends SimpleBean {

	private double	latitude, longitude;
	private String	address, phone, email;

	public BeanStore(int id, String name) {
		super(id, name);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getAddress() {
		return address;
	}

	public BeanStore setLatitude(double latitude) {
		this.latitude = latitude;

		return this;
	}

	public BeanStore setLongitude(double longitude) {
		this.longitude = longitude;

		return this;
	}

	public BeanStore setAddress(String address) {
		this.address = address;

		return this;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
