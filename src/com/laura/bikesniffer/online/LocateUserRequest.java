package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.laura.bikesniffer.gui.MeetingsMapFragment;

public class LocateUserRequest extends HttpAsyncRequest 
{
	private String userId;
	private int code = 0;
	private double lat = 0;
	private double longit = 0;
	
	public LocateUserRequest(Context c, String userId) 
	{
		super(c);
		this.userId = userId;
	}

	protected String makeRequest()
	{
		try {			
 			// URL
 	        URL url = new URL(serverUrl + "/get-location-of-user");
 	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 	        connection.setDoOutput(true);
 	        connection.setRequestMethod("POST");

 	        // Body
 	        JSONObject json = new JSONObject();
 			json.put("deviceId", userId);
 			
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
	 	       	
	 	       	JSONObject json1 = new JSONObject(body);
	 	       	lat = json1.getDouble("latitude");
	 	       	longit = json1.getDouble("longitude");
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
			MeetingsMapFragment.getInstance(4).showRouteToUser(lat, longit);
		}
	}
}
