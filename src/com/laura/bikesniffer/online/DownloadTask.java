package com.laura.bikesniffer.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.laura.bikesniffer.utils.ParserTask;

//Fetches data from url passed
public class DownloadTask extends AsyncTask<String, Void, String>
{
    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) 
    {
        // For storing data from web service
        String data = "";

        try
        {
            // Fetching the data from web service
            data = downloadUrl(url[0]);
        }
        catch(Exception e)
        {
            Log.d("Background Task",e.toString());
        }
        return data;
    }
    
    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }
        catch(Exception e)
        {
            Log.d("Exception while downloading url", e.toString());
        }
        finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) 
    {
        super.onPostExecute(result);
        ParserTask parserTask = new ParserTask();
        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);
    }
}