package com.laura.bikesniffer.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.online.SendMessageRequest;
import com.laura.bikesniffer.utils.UsersManager;

public class MarkerHandler implements OnMarkerClickListener
{
	private ActionBarActivity context;
	private GoogleMap map;
	private View view;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private OnInfoWindowElemTouchListener[] listeners = new OnInfoWindowElemTouchListener[3];
    
	public MarkerHandler(View view, ActionBarActivity a, GoogleMap map)
	{
		this.view = view;
		this.context = a;
		this.map = map;
	}
	
	@SuppressLint("InflateParams")
	public void init()
	{
		final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)view.findViewById(R.id.map_relative_layout);
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
        mapWrapperLayout.init(map, getPixelsFromDp(context, 39 + 20));
        
        // We want to reuse the info window for all the markers, 
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)context.getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        
        int[] ids = {R.id.button1, R.id.button2, R.id.button3};
        
        for (int i = 0; i<ids.length; ++i)
        {
        	ImageButton button = (ImageButton)infoWindow.findViewById(ids[i]);
            
            // Setting custom OnTouchListener which deals with the pressed state
            // so it shows up 
        	OnInfoWindowElemTouchListener infoButtonListener = new OnInfoWindowElemTouchListener(button,
            		context.getResources().getDrawable(R.drawable.accept),
            		context.getResources().getDrawable(R.drawable.accept_pressed)) 
            {
                @Override
                protected void onClickConfirmed(View v, Marker marker) 
                {
                	int id = v.getId();
                	String userId = UsersManager.getInstance().getUserIdForMarkerId(marker.getId());
                	if (userId != null)
                	{
                		String name = UsersManager.getInstance().getUserNameForMarkerId(marker.getId());
                		switch (id) 
                    	{
    						case R.id.button1:
    							new SendMessageRequest(context, userId, 1).execute();
    							Toast.makeText(context,"Your 'Meet me at my location!' message was sent to "+name, Toast.LENGTH_SHORT).show();
    							break;
    						
    						case R.id.button2:
    							new SendMessageRequest(context, userId, 2).execute();
    							Toast.makeText(context,"Your 'Meet me at your location!' message was sent to "+name, Toast.LENGTH_SHORT).show();
    							break;
    						
    						case R.id.button3:
    							Toast.makeText(context,"Temporary unavailable!", Toast.LENGTH_SHORT).show();
    							break;
    						
    						default:
    							break;
    					}
                		
                	}
                }
            }; 
            listeners[i] = infoButtonListener;
            button.setOnTouchListener(listeners[i]);
        }

        map.setInfoWindowAdapter(new InfoWindowAdapter() 
        {
            @Override
            public View getInfoWindow(Marker marker) 
            {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) 
            {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                
                for (OnInfoWindowElemTouchListener listener:listeners)
                {
                	listener.setMarker(marker);
                }

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) 
	{
		String userId = UsersManager.getInstance().getUserIdForMarkerId(arg0.getId());
    	if (userId != null)
    	{
    		String name = UsersManager.getInstance().getUserNameForMarkerId(arg0.getId());
    		String title = name + " meet me:";
    		arg0.setTitle(title);		
    		arg0.showInfoWindow();
    	}
		
		return true;
	}
	
	public static int getPixelsFromDp(Context context, float dp) 
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}
