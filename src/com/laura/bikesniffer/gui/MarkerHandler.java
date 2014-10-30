package com.laura.bikesniffer.gui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.laura.bikesniffer.online.SendMessageRequest;
import com.laura.bikesniffer.utils.UsersManager;

public class MarkerHandler implements OnMarkerClickListener
{
	CustomMessage mMessagePopup;
	Context context;
	
	public MarkerHandler(Activity a)
	{
		mMessagePopup = new CustomMessage(a);
		context = a;
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) 
	{
		Log.d("MarkerHandler", arg0.getId());
		// mMessagePopup.show();
		String smth = UsersManager.getInstance().getUserIdForMarkerId(arg0.getId());
		if(smth != null)
		{
			new SendMessageRequest(context, "Meet me!", smth).execute();
		}
		return true;
	}
}
