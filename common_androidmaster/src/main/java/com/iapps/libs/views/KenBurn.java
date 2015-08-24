package com.iapps.libs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

public class KenBurn
	extends KenBurnsView {

	public KenBurn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public KenBurn(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}

	public KenBurn(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		super.setScaleType(ScaleType.MATRIX);

		// Pause by default
		this.pause();
	}

	// ================================================================================
	// Kenburn
	// ================================================================================
	public void startKenburn() {
		this.resume();
	}

	public void startKenburn(long duration, Interpolator interpolator) {
		RandomTransitionGenerator generator = new RandomTransitionGenerator(duration,
				interpolator);
		this.setTransitionGenerator(generator);
		this.resume();
	}

	public void stopKenburn() {
		this.pause();
	}
}
