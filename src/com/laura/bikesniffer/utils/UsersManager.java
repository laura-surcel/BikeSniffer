package com.laura.bikesniffer.utils;

import java.util.HashMap;

public class UsersManager 
{
	private static UsersManager mInstance = null;
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
	}
	
	public void clearHistory()
	{
		mMarkersToUsers.clear();
	}
	
	public void addUserIdForMarker(String userId, String markerId)
	{
		mMarkersToUsers.put(markerId, userId);
	}
	
	public String getUserIdForMarkerId(String id)
	{
		return mMarkersToUsers.get(id);
	}
}
