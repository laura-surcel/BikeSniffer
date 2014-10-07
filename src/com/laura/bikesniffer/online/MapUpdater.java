package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.laura.bikesniffer.gui.CustomMapFragment;
import com.laura.bikesniffer.gui.GeoPosition;

public class MapUpdater extends HttpAsyncRequest
{
	private CustomMapFragment map;
	private List<GeoPosition> locations;
	
	public MapUpdater(Context c, CustomMapFragment m)
	{
		super(c);
		this.map = m;
		this.locations = new Vector<GeoPosition>();
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
 			GeoPosition pos = map.getPosition();
 			json.put("lat", pos.getLatitude());
 			json.put("longit", pos.getLongitude());
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
	 	       	Log.d("MapUpdater", body);
	 	       	
	 	       	locations.clear();
	 	       	JSONArray array = new JSONArray(body);
	 	       	for(int i = 0; i < array.length(); ++i)
	 	       	{
	 	       		JSONObject obj = array.getJSONObject(i);
	 	       		locations.add(new GeoPosition(new LatLng(obj.getDouble("lat"), obj.getDouble("longit"))));
	 	       	}
	 	       	return body;
 	        } 
 	        else 
 	        {
 	        	Log.d("MapUpdater", "" + connection.getResponseCode());
 	 			return connection.getResponseMessage();
 	        }
 	    } 
 		catch (IOException e) 
 		{
 	        return e.getMessage();
 	    }
	}

	@Override
	protected void onPostExecute(Object obj)
	{
		Log.d("MapUpdater", obj.toString());
		map.populateMapWithLocations(locations);
	}
}
