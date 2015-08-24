package com.imb.tbs.fragments;

import android.os.Bundle;

import com.google.zxing.Result;
import com.imb.tbs.activities.ActivityHome;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

public class FragmentScanner
	extends BarCodeScannerFragment {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setmCallBack(new IResultCallback() {
			@Override
			public void result(final Result lastResult) {
				search(lastResult.toString());
			}
		});
	}

	private void search(String search) {
		((ActivityHome) getActivity()).setFragment(new FragmentProductList(search, true));
	}
}
