package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoveMessagesRequest extends HttpAsyncRequest
{
	private int code = 0;
	private List<Long> ids;
	
	public RemoveMessagesRequest(Context c, List<Long> ids) 
	{
		super(c);
		this.ids = ids;
	}

	protected String makeRequest()
	{
		try {
			// URL
 	        URL url = new URL(serverUrl + "/remove-messages");
 	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 	        connection.setDoOutput(true);
 	        connection.setRequestMethod("POST");

 	        // Body
 	        JSONObject json = new JSONObject();
 	        JSONArray array = new JSONArray();
 			json.put("deviceId", deviceId);
 			
 			for (Long id:ids)
 			{
 				array.put(id);
 			}
 			
 			json.put("messages", array);
 			
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
		
	}
}
