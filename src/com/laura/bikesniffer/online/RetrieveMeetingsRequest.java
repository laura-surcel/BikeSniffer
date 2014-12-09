package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.laura.bikesniffer.gui.meetings.MeetingsFragment;
import com.laura.bikesniffer.utils.Meeting;

public class RetrieveMeetingsRequest extends HttpAsyncRequest
{
	private MeetingsFragment mFragment;
	private int code = 0;
	ArrayList<Meeting> meetings = new ArrayList<Meeting>();
	
	public RetrieveMeetingsRequest(Context c, MeetingsFragment m) 
	{
		super(c);
		this.mFragment = m;
	}

	protected String makeRequest()
	{
		try {
			// URL
 	        URL url = new URL(serverUrl + "/get-meetings");
 	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 	        connection.setDoOutput(true);
 	        connection.setRequestMethod("POST");

 	        // Body
 	        JSONObject json = new JSONObject();
 	        json.put("deviceId", deviceId);
 			
 			// Send
 	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
 	        writer.write(json.toString()+"\n");
 	        writer.close();

 	        code = connection.getResponseCode();
 	       if (code == HttpURLConnection.HTTP_OK) 
	        {
	        	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        	String body = "", line;
	 	       	while((line = inFromServer.readLine()) != null)
	 	       	{
	 	       		body = body + line;
	 	       	}
	 	       	
	 	       	JSONArray array = new JSONArray(body);
	 	       	meetings = Meeting.fromJson(array);
	 	       	return body;
	        } 
	        else 
	        {
	        	Log.d("MapUpdater", "" + connection.getResponseCode());
	 			return connection.getResponseMessage();
	        }
 	    } 
 		catch (IOException | JSONException e) 
 		{
 	        return e.getMessage();
 	    }
	}

	@Override
	protected void onPostExecute(Object obj)
	{
		if (code == HttpURLConnection.HTTP_OK) 
		{
			mFragment.addMeetings(meetings);
		}
	}
}
