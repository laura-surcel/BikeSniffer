package com.laura.bikesniffer.gui;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.online.PositionUpdateTask;
import com.laura.bikesniffer.utils.GeoPosition;
import com.laura.bikesniffer.utils.UsersManager;


/**
 * A placeholder fragment containing a simple view.
 */
public class BikesFragment extends Fragment implements Configuration 
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ActionBarActivity mActivity = null;

	private LongPressLocationSource mLocationSource;
	private MarkerHandler mMarkerHandler;
	private GoogleMap mMap;
	private GeoPosition mPrevPosition;
	private static BikesFragment sInstance;
	private View mRootView;
	private ViewGroup mPrevContainer;
	private Marker mPrevMarker; 
	private Circle mPrevCircle;
	private PositionUpdateTask mPositionUpdater;
	private List<GeoPosition> mPrevLocations;
	private boolean zoomIn = true;
	
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BikesFragment getInstance(int sectionNumber) 
    {
    	
    	if (sInstance == null)
    	{
    		sInstance =  new BikesFragment();
    		Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            sInstance.setArguments(args);
    	}
    	
        return sInstance;
    }
    
    public BikesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	 if (mRootView != null && mPrevContainer == container) 
    	 {
    	        ViewGroup parent = (ViewGroup) mRootView.getParent();
    	        if (parent != null) 
    	        {
    	            parent.removeView(mRootView);
    	        }
    	 } 
    	 else 
    	 {
    	        try 
    	        {
    	        	mRootView = inflater.inflate(R.layout.fragment_main, container, false);
    	            
    	         	// Getting Google Play availability status
    	            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mActivity.getBaseContext());

    	            // Showing status
    	            if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

    	                int requestCode = 10;
    	                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this.mActivity, requestCode);
    	                dialog.show();
    	            }
    	            else
    	            {
    	            	mLocationSource = new LongPressLocationSource();
    	            	setUpMapIfNeeded();
    	            }
    	            
    	            mPrevContainer = container;
    	        } catch (InflateException e) {
    	            Log.w("InflateException happened ou nous", e.getMessage());
    	        }
    	}
    	 
        return mRootView;
    }
    
    public final void makeUseOfNewLocation(Location location) 
    {
    	LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
    	GeoPosition gp = new GeoPosition(loc);
    	    	
    	if( mPrevPosition == null || gp.getDistanceInKmFrom(mPrevPosition) > LOCATION_TOLERANCE)
    	{
    		if (mPrevMarker != null)
    		{
    			mPrevMarker.remove();
    		}
    		
    		mPrevMarker = mMap.addMarker(new MarkerOptions().position(loc));
    		mPrevMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_bike));        
            mPrevPosition = gp;
                    	
        	// Instantiates a new CircleOptions object and defines the center and radius
            CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(((MainActivity)mActivity).getSearchRadius() * 1000)
                .fillColor(CIRCLE_COLOR)
                .strokeColor(Color.TRANSPARENT)
                .zIndex(10); // In meters

            // Get back the mutable Circle
            mPrevCircle = mMap.addCircle(circleOptions);
            
            if(zoomIn)
            {
            	zoomIn = false;
            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM_OUT));
            }
    	}        
    }    
    
    public void setMapType(int type)
    {
    	if(mMap != null)
    	{
    		mMap.setMapType(type);
    	}
    }
	    
    public void populateMapWithLocations(List<GeoPosition> locations)
    {
    	if(mMap != null)
    	{
        	for(int i = 0; i < locations.size(); ++i)
        	{
        		GeoPosition gp = locations.get(i);
        		Marker prevMarker = UsersManager.getInstance().getMarkerForUserId(gp.userId);
        		LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
        		
        		// if it wasn't before => add it
        		if(prevMarker == null)
        		{
        			Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
            		marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.their_bike));
            		UsersManager.getInstance().addUserIdForMarker(gp.userId, marker);
            		UsersManager.getInstance().addUserNameForMarker(gp.userName, marker);
        		}
        		// if it changed location => update it
        		else if(gp.getDistanceInKmFrom(new GeoPosition(prevMarker.getPosition())) > LOCATION_TOLERANCE)
        		{
        			prevMarker.setPosition(loc);
        		}
        	} 
    		
        	if(mPrevLocations != null)
        	{
        		for(int i = 0; i < mPrevLocations.size(); ++i)
        		{
        			GeoPosition gp = mPrevLocations.get(i);
            		Marker prevMarker = UsersManager.getInstance().getMarkerForUserId(gp.userId);
            		boolean exists = false;
            				
            		for(int j = 0; j < locations.size(); ++j)
            		{
            			if(locations.get(j).userId.equalsIgnoreCase(gp.userId))
            			{
            				exists = true;
            				break;
            			}
            		}
            		
            		if(!exists)
            		{
            			prevMarker.remove();
            		}
        		}
        	}
        	
        	mPrevLocations = locations;
    	}
    }
    
    public GeoPosition getPosition()
    {
    	return mPrevPosition;
    }
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        mActivity = (ActionBarActivity)activity;
    }    
    
    public void onPause() 
    {
    	super.onPause();
        mLocationSource.onPause();
    }
    
    public void onResume() 
    {
    	super.onResume();
        setUpMapIfNeeded();
        mLocationSource.onResume();
    }
    
    public void getFocus()
    {
    	Log.d("ROUTES", "getFocus ");
    	((MainActivity)mActivity).selectTab(0);
    }
    
    public void onSearchRadiusChanged()
    {
    	if (mPrevCircle != null)
    	{
    		mPrevCircle.setRadius(((MainActivity)mActivity).getSearchRadius() * 1000);
    	}
    }
    
    private void setUpMapIfNeeded() 
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) 
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            
            // Check if we were successful in obtaining the map.
            if (mMap != null) 
            {
            	mMarkerHandler = new MarkerHandler(mRootView, mActivity, mMap);
            	mMarkerHandler.init();
                setUpMap();
                startPositionUpdater();
            }
        }
    }
    
    private void setUpMap() 
    {   	
    	mMap.setLocationSource(mLocationSource);
        mMap.setOnMarkerClickListener(mMarkerHandler);
        mMap.setOnMapLongClickListener(mLocationSource);
        mMap.setMyLocationEnabled(true);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
        int mapMode = settings.getInt("map_options", R.id.map_option1);
        switch(mapMode)
		{
			case R.id.map_option1:
				setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case R.id.map_option2:
				setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
		}
        mMap.setTrafficEnabled(true);
        
     	// Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() 
        {
            public void onLocationChanged(Location location) 
            {
            	// Called when a new location is found by the network location provider.
            	makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };
         
          mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() 
          {
                @Override
                public boolean onMyLocationButtonClick() 
                {
                	// Called when a new location is found by the network location provider.
                    if(mPrevPosition != null)
                	{
                    	LatLng loc = new LatLng(mPrevPosition.getLatitude(), mPrevPosition.getLongitude());
                    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM_OUT));
                	}
                    return true;
                }
            });


        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private void startPositionUpdater()
	{
    	mPositionUpdater = new PositionUpdateTask(mActivity);
    	mPositionUpdater.startRepeatingTask();
	}
}
