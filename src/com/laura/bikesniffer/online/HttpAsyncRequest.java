package com.laura.bikesniffer.online;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class HttpAsyncRequest extends AsyncTask<Object, Object, Object>
{
	protected Context context;
	protected static String deviceId = null;
	
	public HttpAsyncRequest(Context c)
	{
		this.context = c;
		if(deviceId == null)
		{
			deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		}
	}
	
	@Override
	protected Object doInBackground(Object... arg0) 
	{
		try
		{
			return makeRequest();
	    }
		catch(Exception e)
		{
	         return new String("Exception: " + e.getMessage());
	    }
	}
	
	//check Internet connection.
	private void checkInternetConenction()
	{
      ConnectivityManager check = (ConnectivityManager) this.context.
      getSystemService(Context.CONNECTIVITY_SERVICE);
      if (check != null) 
      {
         NetworkInfo[] info = check.getAllNetworkInfo();
         if (info != null) 
            for (int i = 0; i <info.length; i++) 
            	if (info[i].getState() == NetworkInfo.State.CONNECTED)
            	{
            		Toast.makeText(context, "Internet is connected",
            		Toast.LENGTH_SHORT).show();
            	}

      }
      else
      {
		  	Toast.makeText(context, "not conencted to internet",
		  	Toast.LENGTH_SHORT).show();
      }
   }
	
	@Override
	protected void onPreExecute()
	{
		checkInternetConenction();
	}
	
	@Override
	protected void onPostExecute(Object obj)
	{
		Log.d("CONNECTION", obj.toString());
	}
	
	protected String makeRequest() throws JSONException, IOException
	{
		try {
			// URL
	        URL url = new URL("http://86.124.214.98:3128/connect");
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
		catch (IOException e) 
		{
	        return e.getMessage();
	    }
	}
}
