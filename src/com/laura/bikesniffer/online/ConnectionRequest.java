package com.laura.bikesniffer.online;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ConnectionRequest extends HttpAsyncRequest
{
	boolean login = false;
	
	public ConnectionRequest(Context c, boolean login) 
	{
		super(c);
		this.login = login;
	}

	protected String makeRequest()
	{
		try {
			// URL
	        URL url = new URL(serverUrl + "/connect");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");

	        // Body
	        JSONObject json = new JSONObject();
			json.put("deviceId", deviceId);
			json.put("connect", login);
			
			// Send
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
	        writer.write(json.toString()+"\n");
	        writer.close();

	        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
	        {
	        	Log.d("CONNECTION", connection.getResponseMessage());
	        } 
	        else 
	        {
	        	Log.d("CONNECTION", "" + connection.getResponseCode());
	        }
			return connection.getResponseMessage();
	    } 
		catch (IOException | JSONException e) 
		{
	        return e.getMessage();
	    }
	}
}
