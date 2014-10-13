package com.laura.bikesniffer.gui;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

public class MarkerHandler implements OnMarkerClickListener
{
	CustomMessage mMessagePopup;
	
	public MarkerHandler(Activity a)
	{
		mMessagePopup = new CustomMessage(a);
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) 
	{
		Log.d("MarkerHandler", arg0.getId());
		mMessagePopup.show();
		return false;
	}
}
