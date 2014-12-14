package com.laura.bikesniffer.utils;

import java.util.HashMap;

import com.google.android.gms.maps.model.Marker;

public class UsersManager 
{
	private static UsersManager mInstance = null;
	private HashMap<String, Marker> mMarkers = null;
	private HashMap<String, String> mMarkersToUsers = null;
	private HashMap<String, String> mMarkersToUsersName = null;
	
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
		mMarkersToUsersName = new HashMap<String, String>();
		mMarkers = new HashMap<String, Marker>();
	}
	
	public void clearHistory()
	{
		mMarkersToUsers.clear();
		mMarkersToUsersName.clear();
		mMarkers.clear();
	}
	
	public void addUserIdForMarker(String userId, Marker marker)
	{
		mMarkersToUsers.put(marker.getId(), userId);
		mMarkers.put(marker.getId(), marker);
	}
	
	public void addUserNameForMarker(String userName, Marker marker)
	{
		mMarkersToUsersName.put(marker.getId(), userName);
		mMarkers.put(marker.getId(), marker);
	}
	
	public String getUserIdForMarkerId(String id)
	{
		return mMarkersToUsers.get(id);
	}
	
	public Marker getMarkerForUserId(String id)
	{
		for(String markerId: mMarkersToUsers.keySet())
		{
			if(mMarkersToUsers.get(markerId).equalsIgnoreCase(id))
			{
				return mMarkers.get(markerId);
			}
		}
		
		return null;
	}
	
	public void removeUser(String userId)
	{
		Marker m = getMarkerForUserId(userId);
		if(m != null)
		{
			mMarkersToUsers.remove(m.getId());
			mMarkersToUsersName.remove(m.getId());
			mMarkers.remove(m.getId());
		}
	}
	
	public String getUserNameForMarkerId(String id)
	{
		return mMarkersToUsersName.get(id);
	}
}
