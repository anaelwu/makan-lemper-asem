package com.imb.tbs.helpers;

import android.support.v4.app.Fragment;

import com.iapps.libs.helpers.HTTPAsyncImb;
import com.iapps.libs.helpers.HTTPAsyncTask;
import com.iapps.libs.views.LoadingCompound;

public abstract class HTTPTbs
	extends HTTPAsyncImb {

	public HTTPTbs(Fragment frag, boolean displayProgress) {
		super(frag, displayProgress);
	}

	public HTTPTbs(Fragment frag, LoadingCompound ld) {
		super(frag, ld);
	}

	@Override
	public HTTPAsyncTask execute() {
		return super.execute();
	}

}
