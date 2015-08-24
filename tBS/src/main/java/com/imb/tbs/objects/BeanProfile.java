package com.imb.tbs.objects;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.Helper;

public class BeanProfile
	extends SimpleBean {
	private String						lastname, gender, email, phone, home, address, city,
										postalCode, country, fbId, image, fullName, status;
	private int							points;
	private long						card;
	private long						totalPurchase;
	private DateTime					dob, expiry;
	private ArrayList<BeanTransactions>	transactions;

	public long getCard() {
		return card;
	}

	public String getLastname() {
		return lastname;
	}

	public String getGender() {
		return gender;
	}

	public DateTime getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getHome() {
		return home;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public int getPoints() {
		return points;
	}

	public BeanProfile setCard(long card) {
		this.card = card;

		return this;
	}

	public BeanProfile setLastname(String lastname) {
		this.lastname = lastname;

		return this;
	}

	public BeanProfile setGender(String gender) {
		this.gender = gender;

		return this;
	}

	public BeanProfile setDob(DateTime dob) {
		this.dob = dob;

		return this;
	}

	public BeanProfile setEmail(String email) {
		this.email = email;

		return this;
	}

	public BeanProfile setPhone(String phone) {
		this.phone = phone;

		return this;
	}

	public BeanProfile setHome(String home_phone) {
		this.home = home_phone;

		return this;
	}

	public BeanProfile setAddress(String address) {
		this.address = address;

		return this;
	}

	public BeanProfile setCity(String city) {
		this.city = city;

		return this;
	}

	public BeanProfile setPostalCode(String postalCode) {
		this.postalCode = postalCode;

		return this;
	}

	public BeanProfile setCountry(String country) {
		this.country = country;

		return this;
	}

	public BeanProfile setPoints(int points) {
		this.points = points;

		return this;
	}

	public String getFbId() {
		return fbId;
	}

	public BeanProfile setFbId(String fbId) {
		this.fbId = fbId;

		return this;
	}

	public String getImage() {
		if (Helper.isEmpty(image))
			return "";

		return Api.BASE_URL_LOCAL + image;
	}

	public BeanProfile setImage(String image) {
		this.image = image;

		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public BeanProfile setFullName(String fullName) {
		this.fullName = fullName;

		return this;
	}

	public String getStatus() {
		return status;
	}

	public BeanProfile setStatus(String status) {
		this.status = status;

		return this;
	}

	public DateTime getExpiry() {
		return expiry;
	}

	public BeanProfile setExpiry(DateTime expiry) {
		this.expiry = expiry;

		return this;
	}

	public ArrayList<BeanTransactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<BeanTransactions> transactions) {
		this.transactions = transactions;
	}

	public long getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(long totalPurchase) {
		this.totalPurchase = totalPurchase;
	}
}
