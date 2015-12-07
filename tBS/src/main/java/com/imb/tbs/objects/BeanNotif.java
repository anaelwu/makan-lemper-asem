package com.imb.tbs.objects;

import com.iapps.libs.objects.SimpleBean;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Keys;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcelsantoso.
 * <p/>
 * 9/4/15
 */
public class BeanNotif extends SimpleBean {

    private String campId, title, url;
    private DateTime time;

    public String getUrl() {
        return url;
    }

    public BeanNotif setUrl(String url) {
        this.url = url;
        return this;
    }

    public DateTime getTime() {
        return time;
    }

    public BeanNotif setTime(DateTime time) {
        this.time = time;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BeanNotif setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCampId() {
        return campId;
    }

    public BeanNotif setCampId(String campId) {
        this.campId = campId;
        return this;
    }

    public JSONObject parseToText() {
        JSONObject o = new JSONObject();
        try {
            o.put(Keys.PUSH_ACC, getId());
            o.put(Keys.PUSH_TITLE, title);
            o.put(Keys.PUSH_URL, url);
            o.put(Keys.PUSH_ID, campId);
            o.put(Keys.PUSH_TIME, time.toString(Constants.DATE_JSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }

    public BeanNotif parseToObject(JSONObject j) {
        try {
            setId(j.getInt(Keys.PUSH_ACC));
            this.title = j.getString(Keys.PUSH_TITLE);
            this.url = j.getString(Keys.PUSH_URL);
            this.time = DateTime.parse(j.getString(Keys.PUSH_TIME), DateTimeFormat.forPattern(Constants.DATE_JSON));
            this.campId = j.getString(Keys.PUSH_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }
}
