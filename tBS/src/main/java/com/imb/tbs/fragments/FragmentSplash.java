package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;

import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.objects.Response;
import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterSplash;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.Preference;
import com.imb.tbs.objects.BeanSplash;

public class FragmentSplash
	extends BaseFragmentTbs {
	@InjectView(R.id.gv)
	private GridView				gv;

	private AdapterSplash			adapter;
	private ArrayList<BeanSplash>	alSplash	= new ArrayList<BeanSplash>();

	public static final int			TAG_LOGIN	= 1, TAG_TNC = 2, TAG_CHANGE_ACTIVITY = 3;

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_splash;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		initSplash();
		animate();

		if (getPref().getBoolean(Preference.IS_LOGGED_IN)) {
			// startTimer(TAG_CHANGE_ACTIVITY);
			loadHome();
		}
		else {
			if (Helper.isEmpty(getPref().getString(Preference.SETTINGS))) {
				loadSettings();
			}
			else if (getPref().getBoolean(Preference.IS_ACCEPT_TNC)) {
				startTimer(TAG_LOGIN);
			}
			else {
				startTimer(TAG_TNC);
			}
		}
	}

	public void startTimer(final int tag) {
		new CountDownTimer(Constants.TIMER_SPLASH * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				switch (tag) {
				case TAG_LOGIN:
					clearFragment();
					setFragment(new FragmentPrelogin());
					break;

				case TAG_TNC:
					clearFragment();
					setFragment(new FragmentTnc());
					break;

				case TAG_CHANGE_ACTIVITY:
					getLoginActivity().changeActivity(true);
					break;
				}
			}
		}.start();
	}

	@Override
	public int setMenuLayout() {
		return 0;
	}

	// ================================================================================
	// Splash
	// ================================================================================
	public void initSplash() {
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#5a99b0")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_1).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#b5e0f8")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#3e8095")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#b5e0f8")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#9ed8db")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#6fc6b5")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_2).setImg(true));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_3).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#b1cc94")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_4).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#63903f")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#989936")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_5).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#cccb31")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#ffcc67")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#cd6632")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#cb9832")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_6).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#ffcd33")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_7).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#961b1e")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_8).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#c75c28")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#c91b74")));
		alSplash.add(new BeanSplash().setContent(R.drawable.img_9).setImg(true));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#9c2059")));
		alSplash.add(new BeanSplash().setContent(Color.parseColor("#b02074")));

		adapter = new AdapterSplash(getActivity(), alSplash);
		gv.setAdapter(adapter);

		// Disable scroll
		gv.setVerticalScrollBarEnabled(false);
		gv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}

		});
	}

	public void animate() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Animation animation = AnimationUtils.loadAnimation(getActivity(),
				// R.animator.layout_random_fade);

				// AnimationSet set = new AnimationSet(true);
				// Animation animation = AnimationUtils.loadAnimation(getActivity(),
				// R.anim.fadein);
				// // animation.setDuration(50);
				// set.addAnimation(animation);
				// animation.setRepeatCount(-1);
				// animation.setRepeatMode(-2);
				// animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				// Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				// Animation.RELATIVE_TO_SELF, 0.0f);
				// // animation.setDuration(100);
				// // set.addAnimation(animation);
				// LayoutAnimationController controller = new LayoutAnimationController(set);
				//
				// gv.setLayoutAnimation(controller);
				// while (true) {
				// int index = new Random().nextInt((alSplash.size() - 1));
				// if (index != curAnimIndex) {
				// Animation anim = new AlphaAnimation(0.0f, 1.0f);
				// anim.setDuration(50); // You can manage the time
				// anim.setStartOffset(20);
				// anim.setRepeatMode(Animation.REVERSE);
				// // anim.setRepeatCount(Animation.INFINITE);
				// gv.getChildAt(index).startAnimation(anim);
				//
				// curAnimIndex = index;
				// }
				// }
			}
		}, 500);
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	public void loadSettings() {
		GetAsync get = new GetAsync();
		get.setUrl(Api.SETTINGS);
		get.execute();
	}

	public class GetAsync
		extends HTTPAsyncTask {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Response response) {
			if (!Helper.isValidResponse(response, FragmentSplash.this))
				return;

			JSONObject json = Helper.handleResponse(response, true, getActivity());
			if (json != null) {
				try {
					if (json.getInt(Keys.STATUS_CODE) == 1) {
						getPref().setString(Preference.SETTINGS, json.getString(Keys.RESULTS));
						setFragment(new FragmentTnc());
					}
					else
						Helper.showAlert(getActivity(), json.getString(Keys.STATUS_MESSAGE));
				}
				catch (JSONException e) {
					Helper.showAlert(getActivity(), e.getMessage());
					e.printStackTrace();
				}
			}
			else {
				// Failed to parse JSON
				Helper.showUnknownResponseError(getActivity());
			}
		}
	}

	public void loadHome() {
		new HTTPTbs(this, false) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.GET_CAROUSEL;
			}

			@Override
			public void onSuccess(JSONObject j) {
				try {
					getPref().setString(Preference.CAROUSEL, j.getString(Keys.RESULTS));
					getLoginActivity().changeActivity(true);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public String search() {
				return Helper.toSearchQuery("carousel_active", 1);
//				return "carousel_active=1";
//				return "";
			};
		}.execute();
	}

}
