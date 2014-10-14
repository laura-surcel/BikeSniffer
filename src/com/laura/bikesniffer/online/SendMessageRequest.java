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

import com.google.android.gms.maps.model.LatLng;
import com.laura.bikesniffer.gui.GeoPosition;

import android.content.Context;
import android.util.Log;

public class SendMessageRequest extends HttpAsyncRequest
{
	String message;

	public SendMessageRequest(Context c, String message) 
	{
		super(c);
		this.message = message;
	}

	protected String makeRequest() throws JSONException, IOException
	{
		try {
 			// URL
 	        URL url = new URL("http://86.124.214.98:3128/position");
 	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 	        connection.setDoOutput(true);
 	        connection.setRequestMethod("POST");

 	        // Body
 	        JSONObject json = new JSONObject();
 			json.put("msg", message);
 			json.put("deviceId", deviceId);
 			
 			// Send
 	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
 	        writer.write(json.toString()+"\n");
 	        writer.close();

 	        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
 	        {
 	        	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
 	        	String body = "", line;
	 	       	while((line = inFromServer.readLine()) != null)
	 	       	{
	 	       		body = body + line;
	 	       	}
	 	       	Log.d("SendMessageRequest", body);
	 	       	
	 	       	return "OK";
 	        } 
 	        else 
 	        {
 	        	Log.d("SendMessageRequest", "" + connection.getResponseCode());
 	 			return connection.getResponseMessage();
 	        }
 	    } 
 		catch (IOException e) 
 		{
 	        return e.getMessage();
 	    }
	}
}
