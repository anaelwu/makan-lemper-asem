package com.imb.tbs.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iapps.libs.views.LoadingCompound;
import com.imb.tbs.R;
import com.imb.tbs.helpers.Api;
import com.imb.tbs.helpers.BaseFragmentTbs;
import com.imb.tbs.helpers.Constants;
import com.imb.tbs.helpers.Converter;
import com.imb.tbs.helpers.HTTPTbs;
import com.imb.tbs.helpers.Helper;
import com.imb.tbs.helpers.Keys;
import com.imb.tbs.helpers.UIHelper;
import com.imb.tbs.objects.BeanStore;

public class FragmentStore
	extends BaseFragmentTbs implements OnClickListener,
		OnMarkerClickListener, OnQueryTextListener, LocationListener {
	@InjectView(R.id.imgCurrentLocation)
	private ImageView				imgLocation;
	@InjectView(R.id.mapView)
	private MapView					mMapView;
	@InjectView(R.id.rlDetails)
	private RelativeLayout			rlDetails;
	@InjectView(R.id.rlHolder)
	private RelativeLayout			rlHolder;
	@InjectView(R.id.tvName)
	private TextView				tvName;
	@InjectView(R.id.tvAddress)
	private TextView				tvAddress;
	@InjectView(R.id.btnDetails)
	private Button					btnDetails;
	@InjectView(R.id.ld)
	private LoadingCompound			ld;

	private GoogleMap				googleMap;
	private MarkerOptions			curMarker;
	private ArrayList<BeanStore>	alStore	= new ArrayList<BeanStore>();
	private int						curIndex;
	private BeanStore				bean;

	public static final int			TAG_CURRENT	= 1, TAG_DETAILS = 2;

	@Override
	public int setLayout() {
		return R.layout.fragment_map;
	}

	public FragmentStore() {
		// TODO Auto-generated constructor stub
	}

	public FragmentStore(BeanStore bean) {
		this.bean = bean;
		alStore.add(bean);
	}

	@Override
	public void setView(View view, Bundle savedInstanceState) {
		if (bean != null)
			setTitle(bean.getName());
		else
			setTitle(R.string.store_locators);

		initMap(savedInstanceState);

		imgLocation.setTag(TAG_CURRENT);
		btnDetails.setTag(TAG_DETAILS);
		imgLocation.setOnClickListener(this);
		btnDetails.setOnClickListener(this);

		if (!alStore.isEmpty() || bean != null) {
			ld.hide();
			zoomToCurrentLocation();
			addMarker();
		}
		else
			loadMap();
	}

	@Override
	public int setMenuLayout() {
		return R.menu.search;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		((SearchView) MenuItemCompat.getActionView(searchItem)).setOnQueryTextListener(this);
	}

	// ================================================================================
	// Map
	// ================================================================================
	private void initMap(Bundle savedInstanceState) {
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();// needed to get the map to display immediately

		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		googleMap = mMapView.getMap();
		googleMap.getUiSettings().setZoomControlsEnabled(false);
	}

	private void addMarker() {
		for (int i = 0; i < alStore.size(); i++) {
			BeanStore store = alStore.get(i);
			// create marker
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(store.getLatitude(), store.getLongitude())).title(
					Integer.toString(i));

			// Changing marker icon
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store));

			// adding marker
			googleMap.addMarker(marker);
		}
		googleMap.setOnMarkerClickListener(this);
	}

	public Location getLocation() {
		Location location = null;
		try {
			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			}
			else {
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							100,
							100, this);
					log("Network Enabled");
					// Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								100,
								100, this);
						log("GPS Enabled");
						// Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						}
					}
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	private void zoomToCurrentLocation() {
		googleMap.clear();
		addMarker();

		// Zoom to current location
		if (bean == null) {
			// LocationManager locationManager = (LocationManager) getActivity().getSystemService(
			// Context.LOCATION_SERVICE);
			// Criteria criteria = new Criteria();

			Location location = getLocation();
			if (location != null)
			{
				markCurrentLocation(location);

				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(location.getLatitude(), location.getLongitude()),
						Constants.DEFAULT_ZOOM_MAPS));

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(location.getLatitude(), location.getLongitude()))
						.zoom(13) // Sets the zoom
						.build(); // Creates a CameraPosition from the builder
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
			else {
				// lat long indonesia
				LatLng latLong = new LatLng(-0.960989, 116.973166);
				
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, Constants.DEFAULT_ZOOM_MAPS));

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(latLong)
						.zoom(4) // Sets the zoom
						.build(); // Creates a CameraPosition from the builder
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
		}
		else {
			// Zoom to chosen location
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(bean.getLatitude(), bean.getLongitude()),
					Constants.DEFAULT_ZOOM_MAPS));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(bean.getLatitude(), bean.getLongitude()))
					.zoom(13) // Sets the zoom
					.build(); // Creates a CameraPosition from the builder
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	private void markCurrentLocation(Location location) {
		// googleMap.clear();
		LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
		curMarker = new MarkerOptions()
				.position(currentPosition)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cur));

		googleMap.addMarker(curMarker);
	}

	private void hideDetails() {
		// UIHelper.slideToBottom(rlDetails);
		// UIHelper.slideOutFromTop(rlDetails);
		rlDetails.setVisibility(View.GONE);
	}

	private void showDetails() {
		UIHelper.animSlideInFromBottom(rlDetails);
		// rlDetails.setVisibility(View.VISIBLE);
	}

	// ================================================================================
	// Listeners
	// ================================================================================
	@Override
	public void onClick(View v) {
		switch ((Integer) v.getTag()) {
		case TAG_CURRENT:
			zoomToCurrentLocation();
			break;

		case TAG_DETAILS:
			setFragment(new FragmentStoreDetails(alStore.get(curIndex)));
			break;
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		int index = Integer.valueOf(marker.getTitle());
		if (rlDetails.getVisibility() == View.GONE) {
			showDetails();
		}
		else if (rlDetails.getVisibility() == View.VISIBLE && curIndex == index) {
			hideDetails();
		}

		BeanStore store = alStore.get(index);
		tvName.setText(store.getName());
		if (Helper.isEmpty(store.getAddress())) {
			tvAddress.setVisibility(View.GONE);
			tvName.setSingleLine(false);
		}
		else {
			tvAddress.setVisibility(View.VISIBLE);
			tvAddress.setText(store.getAddress());
			tvName.setSingleLine(true);
		}

		curIndex = index;
		return true;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String search) {
		setFragment(new FragmentStoreList(new ArrayList<BeanStore>(alStore), search));
		return true;
	}

	// ================================================================================
	// Webservice
	// ================================================================================
	private void loadMap() {
		new HTTPTbs(this, ld) {

			@Override
			public String url() {
				// TODO Auto-generated method stub
				return Api.GET_STORE;
			}

			@Override
			public void onSuccess(JSONObject j) {
				try {
					alStore.clear();
					alStore.addAll(Converter.toStore(j.getString(Keys.RESULTS)));

					zoomToCurrentLocation();
					addMarker();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
