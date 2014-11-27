package com.laura.bikesniffer.online;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.Log;

public class HttpAsyncRequest extends AsyncTask<Object, Object, Object>
{
	protected Context context;
	protected static String deviceId = null;
	protected static String serverUrl = "http://86.127.25.65:3128";
	protected boolean isConnected = false;
	
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
			if(isConnected)
				return makeRequest();
			
			return new String("No connection ");
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
            		isConnected = true;
            	}

      }
      else
      {
		  	isConnected = false;
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
	
	protected String makeRequest()
	{
		return "";
	}
}
