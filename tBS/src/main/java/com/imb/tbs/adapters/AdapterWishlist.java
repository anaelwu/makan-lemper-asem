package com.imb.tbs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iapps.libs.objects.SimpleBean;
import com.iapps.libs.views.ImageViewLoader;
import com.imb.tbs.R;
import com.imb.tbs.objects.BeanWish;

@SuppressWarnings("rawtypes")
public abstract class AdapterWishlist
	extends ArrayAdapter {
	private boolean					isRemoveMode;
	private View.OnClickListener	listenerBuy;

	@SuppressWarnings("unchecked")
	public AdapterWishlist(Context context, ArrayList<? extends SimpleBean> objects) {
		super(context, R.layout.cell_wishlist, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflateView(holder, parent, position);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		setView(holder, position);

		return convertView;
	}

	public void setRemoveMode(boolean isRemoveMode) {
		this.isRemoveMode = isRemoveMode;

		this.notifyDataSetChanged();
	}

	public boolean getRemoveMode() {
		return this.isRemoveMode;
	}

	public void setListenerBuy(View.OnClickListener listenerBuy) {
		this.listenerBuy = listenerBuy;
	}

	public class ViewHolder {
		TextView		tvName;
		ImageViewLoader	img;
		Button			btnBuy;
		ImageButton		btnRemove;
	}

	private View inflateView(ViewHolder holder, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		final Context contextThemeWrapper = new ContextThemeWrapper(getContext(),
				R.style.CustomActionBarTheme);

		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

		View convertView = null;
		convertView = localInflater.inflate(R.layout.cell_wishlist, parent, false);

		holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
		holder.img = (ImageViewLoader) convertView.findViewById(R.id.img);
		holder.btnBuy = (Button) convertView.findViewById(R.id.btnBuy);
		holder.btnRemove = (ImageButton) convertView.findViewById(R.id.btnDelete);

		convertView.setTag(holder);

		return convertView;
	}

	private void setView(ViewHolder holder, int position) {
		BeanWish bean = (BeanWish) getItem(position);

		holder.tvName.setText(bean.getName());
//		holder.img.loadImage(bean.getImg(), true);
		holder.img.loadImage(bean.getImg());
		holder.img.hideOverlay();

		if (listenerBuy != null) {
			holder.btnBuy.setOnClickListener(listenerBuy);
			holder.btnBuy.setTag(position);
		}

		if (isRemoveMode) {
			holder.btnBuy.setVisibility(View.GONE);
			holder.btnRemove.setVisibility(View.VISIBLE);
		}
		else {
			holder.btnBuy.setVisibility(View.VISIBLE);
			holder.btnRemove.setVisibility(View.GONE);
		}

		holder.btnRemove.setOnClickListener(new ListenerRemove(position));
	}

	public abstract void remove(int pos);

	public class ListenerRemove implements OnClickListener {
		private int	pos;

		public ListenerRemove(int pos) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View v) {
			remove(pos);
		}
	}
}
