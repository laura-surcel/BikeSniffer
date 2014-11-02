package com.laura.bikesniffer.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Message 
{
	public String senderName;
	public String senderId;
    public int type;
    public long id;

    public Message(String senderName, String senderId, int type, long id) 
    {
       this.senderName = senderName;
       this.senderId = senderId;
       this.type = type;
       this.id = id;
    }
    
    // Constructor to convert JSON object into a Java class instance
    public Message(JSONObject object)
    {
       try 
       {
    	   	this.id = object.getLong("id");
            this.senderId = object.getString("sender_id");
            this.type = object.getInt("type");
       } 
       catch (JSONException e) 
       {
            e.printStackTrace();
       }
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
            	   Log.d("MESSAGES", jsonObjects.getJSONObject(i).getString("sender_id"));
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
