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

public class SendMessageRequest extends HttpAsyncRequest
{
	String recipientId;
	int type;

	public SendMessageRequest(Context c, String recipient, int type) 
	{
		super(c);
		this.recipientId = recipient;
		this.type = type;
	}

	protected String makeRequest()
	{
		try {
 			// URL
 	        URL url = new URL(serverUrl + "/message");
 	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
 	        connection.setDoOutput(true);
 	        connection.setRequestMethod("POST");

 	        // Body
 	        JSONObject json = new JSONObject();
 			json.put("msgType", type);
 			json.put("senderId", deviceId);
 			json.put("receiverId", recipientId);
 			
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
 		catch (IOException | JSONException e) 
 		{
 	        return e.getMessage();
 	    }
	}
}
