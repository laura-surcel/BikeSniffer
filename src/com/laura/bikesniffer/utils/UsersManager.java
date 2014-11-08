package com.laura.bikesniffer.utils;

import java.util.HashMap;

import com.google.android.gms.maps.model.Marker;

public class UsersManager 
{
	private static UsersManager mInstance = null;
	private HashMap<String, Marker> mMarkers = null;
	private HashMap<String,String> mMarkersToUsers = null;
	
	public static UsersManager getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new UsersManager();
		}
		return mInstance;
	}
	
	private UsersManager()
	{
		mMarkersToUsers = new HashMap<String, String>();
		mMarkers = new HashMap<String, Marker>();
	}
	
	public void clearHistory()
	{
		mMarkersToUsers.clear();
		mMarkers.clear();
	}
	
	public void addUserIdForMarker(String userId, Marker marker)
	{
		mMarkersToUsers.put(marker.getId(), userId);
		mMarkers.put(marker.getId(), marker);
	}
	
	public String getUserIdForMarkerId(String id)
	{
		return mMarkersToUsers.get(id);
	}
}
