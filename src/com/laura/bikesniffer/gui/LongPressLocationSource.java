package com.laura.bikesniffer.gui;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

class LongPressLocationSource implements LocationSource, OnMapLongClickListener 
{
	private OnLocationChangedListener mListener;

	/**
	 * Flag to keep track of the activity's lifecycle. This is not strictly necessary in this
	 * case because onMapLongPress events don't occur while the activity containing the map is
	 * paused but is included to demonstrate best practices (e.g., if a background service were
	 * to be used).
	 */
	private boolean mPaused;

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
	}

	@Override
	public void deactivate() {
		mListener = null;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		if (mListener != null && !mPaused) {
			Location location = new Location("LongPressLocationProvider");
			location.setLatitude(point.latitude);
			location.setLongitude(point.longitude);
			location.setAccuracy(100);
			mListener.onLocationChanged(location);
		}
	}

	public void onPause() {
		mPaused = true;
	}

	public void onResume() {
		mPaused = false;
	}
}