package com.imb.tbs.helpers;

import com.imb.tbs.objects.BeanCampaign;
import com.imb.tbs.objects.BeanCarousel;
import com.imb.tbs.objects.BeanProduct;
import com.imb.tbs.objects.BeanProductDetails;
import com.imb.tbs.objects.BeanProfile;
import com.imb.tbs.objects.BeanReview;
import com.imb.tbs.objects.BeanRewardsCatalog;
import com.imb.tbs.objects.BeanSetting;
import com.imb.tbs.objects.BeanStc;
import com.imb.tbs.objects.BeanStore;
import com.imb.tbs.objects.BeanTransactions;
import com.imb.tbs.objects.BeanWish;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Converter {

	public static BeanProfile toProfile(String text) {
		try {
			BeanProfile bean = new BeanProfile();
			// JSONArray jArr = new JSONArray(text);
			// for (int i = 0; i < jArr.length(); i++) {
			// JSONObject j = jArr.getJSONObject(i);
			JSONObject j = new JSONObject(text);
			bean.setId(j.getInt(Keys.USER_ID));
			bean.setCard(j.getLong(Keys.USER_CARD_NO));
			bean.setLastname(Helper.capitalize(j.getString(Keys.USER_LAST_NAME)));
			bean.setName(Helper.capitalize(j.getString(Keys.USER_FIRST_NAME)));
			bean.setGender(j.getString(Keys.USER_GENDER));
			bean.setDob(Helper.parseDateTime(j.getString(Keys.USER_DOB), Constants.DATE_JSON));
			bean.setEmail(j.getString(Keys.USER_EMAIL));
			bean.setPhone(j.getString(Keys.USER_PHONE));
			bean.setHome(j.getString(Keys.USER_HOME));
			bean.setAddress(j.getString(Keys.USER_ADDRESS));
			bean.setCity(j.getString(Keys.USER_CITY));
			bean.setPostalCode(j.getString(Keys.USER_POSTAL));
			bean.setCountry(j.getString(Keys.USER_COUNTRY));
			bean.setPoints(j.getInt(Keys.USER_POINTS));
			bean.setFbId(j.getString(Keys.USER_FB_ID));
			bean.setImage(j.getString(Keys.USER_PHOTO));
			bean.setStatus(j.getString(Keys.USER_STATUS));
			if (!j.getString(Keys.USER_EXPIRY).equalsIgnoreCase("notfound")) {
				DateTime dt = Helper.parseDateTime(j.getString(Keys.USER_EXPIRY),
						Constants.DATE_JSON);
				if (dt.getYear() == 1970)
					bean.setExpiry(null);
				else
					bean.setExpiry(dt);
			}

			if (!Helper.isEmpty(bean.getLastname()) && !bean.getLastname().equalsIgnoreCase("0"))
				bean.setFullName(bean.getName() + " " + bean.getLastname());
			else
				bean.setFullName(bean.getName());

			if (bean.getPhone().startsWith(Constants.PHONE_EXTENSION))
				bean.setPhone(bean.getPhone().substring(Constants.PHONE_EXTENSION.length()));

			if (bean.getFbId().equalsIgnoreCase("0"))
				bean.setFbId("");

			try {
				ArrayList<BeanTransactions> al = new ArrayList<BeanTransactions>();
				JSONArray jArr = j.getJSONObject(Keys.USER_STATEMENT).getJSONArray(Keys.USER_TRANSACTIONS);
				DateTime dtCheck = DateTime.now();
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					if (jObj.getString(Keys.USER_TRANS_DESC).equalsIgnoreCase("Purchase")
							&& jObj.getString(Keys.USER_TRANS_CURRENCY).equalsIgnoreCase("IDR")
							&& jObj.getLong(Keys.USER_TRANS_VALUE) > 0) {
						BeanTransactions trans = new BeanTransactions();
						trans.setIdStr(jObj.getString(Keys.USER_TRANS_ID));
						trans.setName(jObj.getString(Keys.USER_TRANS_NAME));
						trans.setPointsEarned(jObj.getInt(Keys.USER_TRANS_POINTS));
						trans.setValue(jObj.getLong(Keys.USER_TRANS_VALUE));
						trans.setRef(jObj.getString(Keys.USER_TRANS_REF));
						trans.setDate(Helper.parseDateTime(jObj.getString(Keys.USER_TRANS_DATE), Constants.DATE_JSON));
						if (dtCheck.isEqual(trans.getDate().getMillis()))
							trans.setShowDate(false);
						else {
							trans.setShowDate(true);
							dtCheck = trans.getDate();
						}

						bean.setTotalPurchase(bean.getTotalPurchase() + trans.getValue());

						if (!jObj.getString("Cash_Type").equalsIgnoreCase("POINT EXPIRED"))
							al.add(trans);
					}
				}

				Collections.reverse(al);
				bean.setTransactions(al);
				return bean;
			}
			catch (JSONException e) {
				bean.setTransactions(null);
				return bean;
			}
		}
		catch (JSONException e) {
			// try {
			// JSONObject j = new JSONObject(text);
			// JSONArray jArr = new JSONArray();
			// jArr.put(j);
			//
			// return Converter.toProfile(jArr.toString());
			// }
			// catch (JSONException e2) {
			// e2.printStackTrace();
			// }
			e.printStackTrace();
		}

		return null;
	}

	public static BeanSetting toSettings(String text) {
		try {
			JSONArray jArr = new JSONArray(text);
			JSONObject j = new JSONObject();
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject jObj = jArr.getJSONObject(i);
				j.put(jObj.getString(Keys.SET_ID), jObj.getString(Keys.SET_VALUE));
			}

			BeanSetting bean = new BeanSetting();
			bean.setAddress(j.getString(Keys.SET_ADDRESS));
			bean.setEmail(j.getString(Keys.SET_EMAIL));
			bean.setFacebook(j.getString(Keys.SET_FACEBOOK));
			bean.setTwitter(j.getString(Keys.SET_TWITTER));
			bean.setTwitterId(j.getString(Keys.SET_TWITTER_ID));
			bean.setYoutube(j.getString(Keys.SET_YOUTUBE));
			bean.setPinterest(j.getString(Keys.SET_PINTEREST));
			bean.setInstagram(j.getString(Keys.SET_INSTAGRAM));
			// bean.setTnc(j.getString(Keys.SET_TNC));
			bean.setPhone1(j.getString(Keys.SET_PHONE_1));
			bean.setPhone2(j.getString(Keys.SET_PHONE_2));
			bean.setOpeningHour(j.getString(Keys.SET_OPENING_HOUR));

			return bean;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BeanCarousel toCarousel(String text) {
		try {
			BeanCarousel bean = new BeanCarousel();

			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);
				bean.setId(j.getInt(Keys.CAR_ID));
				bean.setImg(j.getString(Keys.CAR_IMAGE));
				bean.setName(j.getString(Keys.CAR_TITLE));
				bean.setUrl(j.getString(Keys.CAR_URL));
			}

			return bean;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<BeanCampaign> toCampaign(String text) {
		ArrayList<BeanCampaign> al = new ArrayList<BeanCampaign>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);

				BeanCampaign bean = new BeanCampaign(j.getInt(Keys.CAMP_ID), j.getString(Keys.CAMP_NAME));
				bean.setUrl(j.getString(Keys.CAMP_URL));
				bean.setDesc(j.getString(Keys.CAMP_DESC));
				bean.setImgUrl(j.getString(Keys.CAMP_IMG));
				bean.setDateCreated(Helper.parseDateTime(j.getString(Keys.CAMP_CREATED), Constants.DATE_YMDHIS));

				al.add(bean);
			}

		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return al;
	}

	public static ArrayList<BeanCampaign> toNews(String text) {
		ArrayList<BeanCampaign> al = new ArrayList<BeanCampaign>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);

				BeanCampaign bean = new BeanCampaign(j.getInt(Keys.NEWS_ID), j.getString(Keys.NEWS_NAME));
				bean.setUrl(j.getString(Keys.NEWS_URL));
				bean.setDesc(j.getString(Keys.NEWS_DESC));
				bean.setImgUrl(j.getString(Keys.NEWS_IMG));
				bean.setDateCreated(Helper.parseDateTime(j.getString(Keys.NEWS_CREATED), Constants.DATE_YMDHIS));

				al.add(bean);
			}

		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return al;
	}

	public static ArrayList<BeanRewardsCatalog> toRewards(String text) {
		ArrayList<BeanRewardsCatalog> al = new ArrayList<BeanRewardsCatalog>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);

				BeanRewardsCatalog bean = new BeanRewardsCatalog(j.getInt(Keys.REW_ID), "");
				bean.setImgUrl(j.getString(Keys.REW_IMG));
				bean.setPoint(j.getInt(Keys.REW_POINT));
				int status = j.getInt(Keys.REW_STATUS);
				if (status == 1) {
					status = Constants.STATE_REWARDS_OK;
				}
				else if (status == 2) {
					status = Constants.STATE_REWARDS_POINT_REQ;
				}
				else {
					int memberType = Integer.parseInt(j.getString(Keys.REW_TYPE));
					if (memberType == 1)
						status = Constants.STATE_REWARDS_CLUB_REQ;
					else if (memberType == 2)
						status = Constants.STATE_REWARDS_FAN_REQ;
					else
						status = Constants.STATE_REWARDS_CLUB_REQ;
				}
				bean.setStatus(status);

				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return al;
	}

	public static ArrayList<BeanProduct> toProducts(String text, String dict, String order) {
		ArrayList<BeanProduct> al = new ArrayList<BeanProduct>();
		try {
			JSONObject j = new JSONObject(text);
			JSONObject jDict = new JSONObject(dict);
			JSONObject jOrder = new JSONObject(order);

			Iterator<String> iter = j.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				JSONObject j2 = j.getJSONObject(key);

				BeanProduct bean = new BeanProduct();
				bean.setCode(key);
				bean.setName(Helper.capitalize(jDict.getString(key).toLowerCase()));
				bean.setOrder(Integer.parseInt(jOrder.getString(key)));

				ArrayList<BeanProduct> al2 = new ArrayList<BeanProduct>();
				Iterator<String> iter2 = j2.keys();
				while (iter2.hasNext()) {
					String key2 = iter2.next();
					BeanProduct bean2 = new BeanProduct();
					bean2.setCode(key2);
					bean2.setName(Helper.capitalize(jDict.getString(key2).toLowerCase()));
					bean2.setOrder(Integer.parseInt(jOrder.getString(key2)));
					
					ArrayList<BeanProduct> al3 = new ArrayList<BeanProduct>();
					JSONArray jArr = j2.getJSONArray(key2);
					for (int i = 0; i < jArr.length(); i++) {
						BeanProduct bean3 = new BeanProduct();
						bean3.setCode(jArr.getString(i));
						bean3.setName(Helper.capitalize(jDict.getString(bean3.getCode()).toLowerCase()));
						bean3.setOrder(Integer.parseInt(jOrder.getString(bean3.getCode())));
						
						al3.add(bean3);
					}

					bean2.setChildList(Converter.sort(al3));
					al2.add(bean2);
				}

				bean.setChildList(Converter.sort(al2));
				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return Converter.sort(al);
	}

	public static ArrayList<BeanProduct> sort(ArrayList<BeanProduct> al) {
		Collections.sort(al, new CustomComparator());

		return al;
	}

	public static class CustomComparator implements Comparator<BeanProduct> {
		@Override
		public int compare(BeanProduct o1, BeanProduct o2) {
			return o1.getOrder() - o2.getOrder();
		}
	}

	public static ArrayList<BeanProductDetails> toProductList(String text) {
		ArrayList<BeanProductDetails> al = new ArrayList<BeanProductDetails>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);
				BeanProductDetails bean = new BeanProductDetails();
				bean.setName(Helper.capitalize(j.getString(Keys.PROD_NAME).toLowerCase()));
				bean.setNameIn(Helper.capitalize(j.getString(Keys.PROD_NAME_IN).toLowerCase()));
				bean.setId(j.getInt(Keys.PROD_ID));
				bean.setVariantId(j.getInt(Keys.PROD_VARIANT_ID));
				bean.setVariant(j.getString(Keys.PROD_VARIANT_NAME));
				bean.setVariantIn(j.getString(Keys.PROD_VARIANT_NAME_IN));
				bean.setCode(j.getString(Keys.PROD_INACODE));
				bean.setHowto(j.getString(Keys.PROD_HOW_TO));
				bean.setHowtoIn(j.getString(Keys.PROD_HOW_TO_IN));
				bean.setArticle(j.getString(Keys.PROD_ARTICLE));
				bean.setArticleIn(j.getString(Keys.PROD_ARTICLE_IN));
				bean.setIngre(j.getString(Keys.PROD_INGRE));
				bean.setIngreIn(j.getString(Keys.PROD_INGRE_IN));
				bean.setPrice(j.getLong(Keys.PROD_PRICE));
				bean.setImg(j.getString(Keys.PROD_MAIN_IMG));
				bean.setImgVariant(j.getString(Keys.PROD_VARIANT_IMG));
				bean.setWeight(j.getString(Keys.PROD_WEIGHT) + " " + j.getString(Keys.PROD_WEIGHT_UNIT));
				bean.setUrl(Api.ECOMM_DETAILS + Integer.toString(bean.getId()));

				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return al;
	}

	public static ArrayList<BeanReview> toReview(String text) {
		ArrayList<BeanReview> al = new ArrayList<BeanReview>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);

				BeanReview bean = new BeanReview(j.getInt(Keys.TESTI_ID), "");
				bean.setComment(j.getString(Keys.TESTI_TEXT));
				// bean.setDate(Helper.parseDateTime(j.getString(Keys.TESTI_DATE),
				// Constants.DATE_JSON_FULL));
				bean.setDate(DateTime.now());
				bean.setImgUrl(j.getString(Keys.USER_PHOTO));

				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		Collections.reverse(al);
		return al;
	}

	public static ArrayList<BeanStore> toStore(String text) {
		ArrayList<BeanStore> al = new ArrayList<BeanStore>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);
				BeanStore bean =
						new BeanStore(j.getInt(Keys.STORE_ID), j.getString(Keys.STORE_NAME));
				bean.setLatitude(j.getDouble(Keys.STORE_LAT));
				bean.setLongitude(j.getDouble(Keys.STORE_LONG));
				bean.setAddress(j.getString(Keys.STORE_ADDRESS));
				bean.setEmail(j.getString(Keys.STORE_EMAIL));
				bean.setPhone(j.getString(Keys.STORE_PHONE));

				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return al;
	}

	public static ArrayList<BeanWish> toWishlist(String text) {
		ArrayList<BeanWish> al = new ArrayList<BeanWish>();
		try {
			JSONArray jArr = new JSONArray(text);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject j = jArr.getJSONObject(i);
				BeanWish bean = new BeanWish();
				bean.setWishId(j.getInt(Keys.WISH_ID));
				bean.setId(j.getInt(Keys.PROD_ID));
				bean.setName(Helper.capitalize(j.getString(Keys.PROD_NAME).toLowerCase()));
				bean.setNameIn(Helper.capitalize(j.getString(Keys.PROD_NAME_IN).toLowerCase()));
				bean.setVariantId(j.getInt(Keys.PROD_VARIANT_ID));
				bean.setVariant(j.getString(Keys.PROD_VARIANT_NAME));
				bean.setVariantIn(j.getString(Keys.PROD_VARIANT_NAME_IN));
				bean.setCode(j.getString(Keys.PROD_INACODE));
				bean.setHowto(j.getString(Keys.PROD_HOW_TO));
				bean.setHowtoIn(j.getString(Keys.PROD_HOW_TO_IN));
				bean.setArticle(j.getString(Keys.PROD_ARTICLE));
				bean.setArticleIn(j.getString(Keys.PROD_ARTICLE_IN));
				bean.setIngre(j.getString(Keys.PROD_INGRE));
				bean.setIngreIn(j.getString(Keys.PROD_INGRE_IN));
				bean.setImg(j.getString(Keys.PROD_MAIN_IMG));
				bean.setImgVariant(j.getString(Keys.PROD_VARIANT_IMG));
				bean.setPrice(j.getLong(Keys.PROD_PRICE));
				bean.setWeight(j.getString(Keys.PROD_WEIGHT) + " " + j.getString(Keys.PROD_WEIGHT_UNIT));
				bean.setUrl(Api.ECOMM_DETAILS + Integer.toString(bean.getId()));

				al.add(bean);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}

	public static BeanStc toStcSetting(String text) {
		BeanStc bean = new BeanStc();
		try {
			JSONObject jObj = new JSONObject(text);
//			bean.setText(jObj.getString(Keys.STC_TEXT));
			bean.setLimit(jObj.getLong(Keys.STC_LIMIT));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return bean;
	}
}