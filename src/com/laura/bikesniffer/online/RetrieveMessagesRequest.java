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

import com.laura.bikesniffer.gui.messages.MessagesFragment;
import com.laura.bikesniffer.utils.Message;

public class RetrieveMessagesRequest extends HttpAsyncRequest
{
	private MessagesFragment mFragment;
	private int code = 0;
	ArrayList<Message> messages = new ArrayList<Message>();
	
	public RetrieveMessagesRequest(Context c, MessagesFragment m) 
	{
		super(c);
		mFragment = m;
	}

	protected String makeRequest()
	{
		try {
			messages.clear();
			
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
	 	       	
	 	       	JSONArray array = new JSONArray(body);
	 	       	messages = Message.fromJson(array);
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
			mFragment.addMessages(messages);
		}
	}
}
