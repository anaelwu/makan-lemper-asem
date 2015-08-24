package com.iapps.libs.views;

import java.util.Calendar;

import org.apache.commons.io.output.ByteArrayOutputStream;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iapps.common_library.R;
import com.iapps.external.photoview.PhotoView;
import com.iapps.libs.helpers.BaseConstants;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.helpers.CircleTransform;
import com.iapps.libs.helpers.RoundedShadowTransform;
import com.iapps.libs.objects.ListenerDoubleTap;
import com.iapps.libs.objects.ListenerLoad;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

public class ImageViewLoader
	extends RelativeLayout implements View.OnClickListener {
	private ImageView		image;
	private ImageView		imageOverlay;
	private ProgressBar		progress;
	@SuppressWarnings("unused")
	private boolean			isFade	= true, isSquareToWidth = false,
									isSquareToHeight = false, popup = false;
	private long			delay;
	private int				placeholder;
	private ListenerLoad	listenerLoad;

	public ImageViewLoader(Context context) {
		this(context, null);
	}

	public ImageViewLoader(Context context, AttributeSet attr) {
		super(context, attr);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_view_loader, this, true);

		image = (ImageView) this.findViewById(R.id.image);
		imageOverlay = (ImageView) this.findViewById(R.id.imgOverlay);
		progress = (ProgressBar) this.findViewById(R.id.loader);

		// Default scale type
		image.setScaleType(ScaleType.CENTER_CROP);
	}

	public void hideProgress() {
		progress.setVisibility(View.GONE);
		this.image.setVisibility(View.VISIBLE);
	}

	public void showProgress() {
		progress.setVisibility(View.VISIBLE);
		this.image.setVisibility(View.INVISIBLE);
	}

	public void showFail() {
		if (placeholder == 0) {
			image.setScaleType(ScaleType.CENTER);
			image.setImageResource(R.drawable.ic_cross_light);
		}
		this.image.setVisibility(View.VISIBLE);
	}

	// ================================================================================
	// Image Loader Functions
	// ================================================================================
	public void loadImage(String url) {
		this.loadImage(url, 0, false);
	}

	public void loadImage(String url, boolean isCircular) {
		this.loadImage(url, 0, isCircular);
	}

	public void loadImage(String url, int resPlaceHolder, boolean isCircular) {
		if (isCircular)
			this.loadImage(url, resPlaceHolder, new CircleTransform());
		else
			this.loadImage(url, resPlaceHolder, null);
	}

	/**
	 * Rounded corner & shadow
	 * 
	 * @param url
	 * @param radius
	 * @param margin
	 */
	public void loadImage(String url, int radius, int margin) {
		this.loadImage(
				url,
				0,
				new RoundedShadowTransform((int) BaseUIHelper
						.convertDpToPixel(radius, getContext()), margin));
	}

	/**
	 * 
	 * @param url
	 * @param resPlaceHolder
	 * @param radius
	 * @param margin
	 */
	public void loadImage(String url, int resPlaceHolder, int radius, int margin) {
		this.loadImage(url, resPlaceHolder, new RoundedShadowTransform(radius, margin));
	}

	public void loadImage(final String url, int resPlaceHolder, Transformation transformation) {
		this.placeholder = resPlaceHolder;
		showProgress();

		if (BaseHelper.isEmpty(url)) {
			hideProgress();
			showFail();
			return;
		}

		Log.d("ImageViewLoader", "Load : " + url);
		if (this.image != null && this.progress != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.build();
			ImageLoader.getInstance().displayImage(url, this.image, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					showProgress();
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					showFail();
					Log.d(BaseConstants.LOG, "Failed to load : " + url);

					if (listenerLoad != null)
						listenerLoad.onFail();

					hideProgress();
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (listenerLoad != null)
						listenerLoad.onSuccess();
					Log.d(BaseConstants.LOG, "Load : " + url);
					hideProgress();
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	public void loadImage(int resImage) {
		if (resImage != 0) {
			RequestCreator imageLoader = Picasso
					.with(this.getContext())
					.load(resImage);
			imageLoader.into(this.image);
		}
		hideProgress();
	}

	// ================================================================================
	// Getter & Setter
	// ================================================================================
	public ImageView getImage() {
		return image;
	}

	public boolean isFade() {
		return isFade;
	}

	public void setFade(boolean isFade) {
		this.isFade = isFade;
	}

	public void setOnDoubleTapListener(ListenerDoubleTap listenerDoubleTap) {
		this.setOnClickListener(new ListenerClick(listenerDoubleTap));
	}

	public void hideOverlay() {
		this.imageOverlay.setVisibility(View.GONE);
	}

	public void setImageOverlay(int res) {
		if (res == 0)
			this.imageOverlay.setVisibility(View.GONE);
		else
			this.imageOverlay.setImageResource(res);
	}

	public void setListenerLoad(ListenerLoad listenerLoad) {
		this.listenerLoad = listenerLoad;
	}

	// ================================================================================
	// Double Tap Functions
	// ================================================================================
	public class ListenerClick implements OnClickListener {

		private ListenerDoubleTap	listener;

		public ListenerClick(ListenerDoubleTap listener) {
			this.listener = listener;
		}

		@Override
		public void onClick(View v) {
			long curTime = Calendar.getInstance().getTimeInMillis();
			long diffTime = curTime - delay;

			if (diffTime < 500) {
				listener.onDoubleTap(v);
				delay = 0;
			}
			else {
				delay = curTime;
			}

		}
	}

	// ================================================================================
	// Popup
	// ================================================================================
	public void setPopupOnClick(boolean popup) {
		this.popup = popup;

		if (popup) {
			this.setOnClickListener(this);
		}
	}

	public void popupImage() {
		final Dialog popup = new Dialog(getContext());
		popup.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		View view = ((LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_image,
				null);
		popup.setContentView(view);

		PhotoView img = (PhotoView) view.findViewById(R.id.img);
		img.setImageDrawable(this.image.getDrawable());
		img.setSquareToWidth(true);
		img.setZoomable(true);
		img.setScaleType(ScaleType.FIT_CENTER);

		// img.set
		// img.setImageOverlay(0);
		// img.hideProgress();

		popup.setCancelable(true);
		popup.show();
	}

	@Override
	public void onClick(View v) {
		popupImage();
	}

	// ================================================================================
	// Make it always square, adjusting to width
	// ================================================================================
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isSquareToWidth) {
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
			// image.getLayoutParams().height = widthMeasureSpec;
		}
		else if (isSquareToHeight) {
			super.onMeasure(heightMeasureSpec, heightMeasureSpec);
			// image.getLayoutParams().width = heightMeasureSpec;
		}
		else
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public boolean isSquareToWidth() {
		return isSquareToWidth;
	}

	public boolean isSquareToHeight() {
		return isSquareToHeight;
	}

	public void setSquareToWidth(boolean isSquareToWidth) {
		this.isSquareToWidth = isSquareToWidth;
	}

	public void setSquareToHeight(boolean isSquareToHeight) {
		this.isSquareToHeight = isSquareToHeight;
	}

	// ================================================================================
	// Convert to upload format
	// ================================================================================
	public String getUploadFormat() {
		try {
			Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
			if (bitmap != null) {
				showProgress();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
				byte[] image = stream.toByteArray();

				hideProgress();
				return Base64.encodeToString(image, 0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
