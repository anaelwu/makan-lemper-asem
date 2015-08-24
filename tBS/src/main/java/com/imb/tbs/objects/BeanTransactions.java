package com.imb.tbs.objects;

import org.joda.time.DateTime;

import com.iapps.libs.objects.SimpleBean;

public class BeanTransactions
	extends SimpleBean {
	DateTime	date;
	long		value;
	int			pointsEarned;
	String		ref, idStr;
	boolean		showDate = true;

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getPointsEarned() {
		return pointsEarned;
	}

	public void setPointsEarned(int pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String id) {
		this.idStr = id;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}
}
