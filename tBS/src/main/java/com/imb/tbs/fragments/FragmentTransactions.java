package com.imb.tbs.fragments;

import java.util.ArrayList;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.imb.tbs.R;
import com.imb.tbs.adapters.AdapterTransactions;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.objects.BeanTransactions;

public class FragmentTransactions
	extends BaseFragmentTbs {
	@InjectView(R.id.lv)
	private ListView			lv;
	ArrayList<BeanTransactions>	al	= new ArrayList<BeanTransactions>();
	private AdapterTransactions	adapter;
	private long				total;

	public FragmentTransactions(ArrayList<BeanTransactions> al, long total) {
		this.al = al;
		this.total = total;
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stubå
		return R.layout.fragment_trans;
	}

	@Override
	public int setMenuLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		setTitle(R.string.past_transactions);
		adapter = new AdapterTransactions(getActivity(), al, R.layout.cell_trans);
		addFooter();
		lv.setAdapter(adapter);
	}

	private void addFooter() {
		View footerView = getActivity().getLayoutInflater().inflate(R.layout.cell_footer_transaction,
				null);
		lv.addFooterView(footerView);
		lv.setFooterDividersEnabled(false);

		TextView tvTotal = ((TextView) footerView.findViewById(R.id.tvTotalTransactions));
		tvTotal.setText("Total purchase : " + Helper.formatRupiah(total));
	}
}
