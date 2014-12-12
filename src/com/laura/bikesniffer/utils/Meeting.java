package com.laura.bikesniffer.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class Meeting 
{
	public long id;
	public String userName;
	public String userId;
	public GeoPosition location;
	public int type;

    public Meeting(String userName, String userId, GeoPosition gp) 
    {
       this.userName = userName;
       this.userId = userId;
       this.location = gp;
    }
    
    // Constructor to convert JSON object into a Java class instance
    public Meeting(JSONObject object)
    {
       try 
       {	
    	   	this.id = object.getLong("id");
    	   	this.userName = object.getString("interrogator_name");
            this.userId = object.getString("interrogator_id");
            this.location = new GeoPosition(new LatLng(object.getDouble("lat"), object.getDouble("longit")));
            this.type = 1;
       } 
       catch (JSONException e) 
       {
    	   try 
    	   {
    		   this.userName = object.getString("interrogated_name");
    		   this.userId = object.getString("interrogated_id");
               this.location = new GeoPosition(new LatLng(object.getDouble("lat"), object.getDouble("longit")));
               this.type = 2;
    	   } 
    	   catch (JSONException e1) 
    	   {
    		   e1.printStackTrace();
    	   }
       }
    }
    
    public JSONObject toJson()
    {
    	JSONObject json = new JSONObject();
    	try 
    	{
			json.put("user_name", userName);
			json.put("user_id", userId);
		} 
    	catch (JSONException e) 
    	{
			e.printStackTrace();
		}
    	return json;
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Meeting> fromJson(JSONArray jsonObjects) 
    {
           ArrayList<Meeting> meetings = new ArrayList<Meeting>();
           for (int i = 0; i < jsonObjects.length(); i++) 
           {
               try 
               {
            	   meetings.add(new Meeting(jsonObjects.getJSONObject(i)));
               } 
               catch (JSONException e) 
               {
                  e.printStackTrace();
               }
          }
          return meetings;
    }
}
