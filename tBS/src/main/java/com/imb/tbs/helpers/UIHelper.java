package com.imb.tbs.helpers;

import android.content.Context;

import com.iapps.libs.helpers.BaseUIHelper;
import com.imb.tbs.R;

public class UIHelper extends BaseUIHelper {

	public static String getMapUrl(Context context, double latitude, double longitude) {
		String url;
		if ((latitude == 0.0 && longitude == 0.0))
			return "";

		url = Api.MAP_API;
		url += "key=" + context.getString(R.string.google_api_key);
		url += "&center=" + latitude + "," + longitude;
		// Sensor sensitive places
		url += "&sensor=" + "true";
		// Zoom in
		url += "&zoom=" + Integer.toString(Constants.DEFAULT_ZOOM_MAPS);
		// Marker color
		url += "&markers=" + "green";
		// Marker location {latitude,longitude}
		url += "|" + latitude + "," + longitude;
		// Get exact size
		url += "&size="
				// Calculate screen width - white space left and right
				+ Math.round(BaseUIHelper.convertPixelsToDp(
						BaseUIHelper.getScreenWidth(context), context))
				+ "x"
				// Height of the image
				+ Math.round(context.getResources().getDimension(R.dimen.img_header));

		return url;
	}
}
