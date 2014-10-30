package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.laura.bikesniffer.gui.BikesFragment;

public class RetrieveMessagesRequest extends HttpAsyncRequest
{
	private BikesFragment map;
	private String msg;
	private int code = 0;
	
	public RetrieveMessagesRequest(Context c, BikesFragment m) 
	{
		super(c);
		map = m;
	}

	protected String makeRequest()
	{
		try {
 			// URL
 	        URL url = new URL(serverUrl + "/get-messages");
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
	 	       	Log.d("MapUpdater", body);
	 	       	
	 	       	JSONArray array = new JSONArray(body);
	 	       	for(int i = 0; i < array.length();)
	 	       	{
	 	       		msg = array.getString(i);
	 	       		break;
	 	       	}
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
			map.showNewMessage(msg);
		}
	}
}
