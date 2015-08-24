package com.imb.tbs.activities;

import roboguice.inject.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import android.os.Bundle;

import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.fragments.FragmentVcardStamp;
import com.imb.tbs.helpers.BaseActivityTbs;

public class ActivityVcard
	extends BaseActivityTbs {
	@InjectView(R.id.imgBg)
	private ImageViewLoader	imgBg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath(getString(R.string.font_default))
				.setFontAttrId(R.attr.fontPath)
				.build());
		setContentView(R.layout.activity_vcard);

		setContainerId(R.id.flFragment);
		setFragment(new FragmentVcardStamp());
		// setFragment(new FragmentVcardMember());

		imgBg.loadImage(R.drawable.background_landscape);
	}

	// @Override
	// protected void attachBaseContext(Context newBase) {
	// super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	// }
}
