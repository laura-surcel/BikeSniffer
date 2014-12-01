package com.laura.bikesniffer.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class Meeting 
{
	public String userName;
	public String userId;
	public GeoPosition location;

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
    	   	this.userName = object.getString("user_name");
            this.userId = object.getString("user_id");
       } 
       catch (JSONException e) 
       {
            e.printStackTrace();
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
    public static ArrayList<Message> fromJson(JSONArray jsonObjects) 
    {
           ArrayList<Message> messages = new ArrayList<Message>();
           for (int i = 0; i < jsonObjects.length(); i++) 
           {
               try 
               {
            	   Log.d("MEETINGS", jsonObjects.getJSONObject(i).getString("user_id"));
            	   messages.add(new Message(jsonObjects.getJSONObject(i)));
               } 
               catch (JSONException e) 
               {
                  e.printStackTrace();
               }
          }
          return messages;
    }
}
