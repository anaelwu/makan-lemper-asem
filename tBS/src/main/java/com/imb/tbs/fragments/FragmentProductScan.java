package com.imb.tbs.fragments;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment.PinPickerDialogHandler;
import com.imb.tbs.R;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.material.widget.FloatingEditText;

public class FragmentProductScan
	extends BaseFragmentTbs implements PinPickerDialogHandler {
	@InjectView(R.id.btnSubmit)
	private Button				btnSubmit;
	@InjectView(R.id.edtCode)
	private FloatingEditText	edtCode;
	@InjectView(R.id.scanner)
	private FrameLayout			fl;

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.fragment_scan;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.scan_product);
		Helper.numberPicker(edtCode, this, 1);

		btnSubmit.setOnClickListener(this);

		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.scanner, new FragmentScanner())
				.commit();
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onClick(View v) {
		if (Helper.validateEditTexts(new EditText[] {
				edtCode
		}))
			setFragment(new FragmentProductList(edtCode.getText().toString(), true));
	}

	@Override
	public void onDialogPinSet(int reference, String pin) {
		edtCode.setText(pin);
	}
}
