package com.imb.tbs.helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;
import com.imb.tbs.R;

import org.apache.commons.lang.WordUtils;

import java.text.DecimalFormat;
import java.util.Locale;

public class Helper
	extends BaseHelper {
	public static String formatNumber(int number) {
		DecimalFormat formatter = new DecimalFormat("#,###,###");
		return formatter.format(number);
	}

	public static void setRefreshColor(SwipeRefreshLayout sr) {
		BaseUIHelper.setRefreshColor(sr);
	}

	public static void popupHelp(Context context) {
		final Dialog popup = new Dialog(context);
		popup.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_help,
				null);
		popup.setContentView(view);

		popup.setCancelable(true);
		popup.show();
	}

//	public static String parseStatus(String status) {
//		if (status == null)
//			return "";
//
//		if (status.equalsIgnoreCase(Constants.STATUS_LYB)) {
//			return "LYB Fan";
//		}
//		if (status.equalsIgnoreCase(Constants.STATUS_LYB_CLUB)) {
//			return "LYB Club";
//		}
//		else if (status.equalsIgnoreCase(Constants.STATUS_STAMP)) {
//			return "Stamp Card";
//		}
//
//		return "";
//	}

	public static int parseStatusCode(String status) {
		if (status == null)
			return 0;

		if (status.equalsIgnoreCase(Constants.STATUS_LYB)) {
			return 2;
		}
		if (status.equalsIgnoreCase(Constants.STATUS_LYB_CLUB)) {
			return 1;
		}
		else if (status.equalsIgnoreCase(Constants.STATUS_STAMP)) {
			return 0;
		}

		return 0;
	}

	public static boolean isMale(String gender) {
		if (Helper.isEmpty(gender))
			return true;

		if (gender.equalsIgnoreCase(Constants.MALE))
			return true;
		return false;
	}

	public static String capitalize(String text) {
		return WordUtils.capitalize(text.toLowerCase()).replace("Tbs", "TBS");
	}

	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

}
