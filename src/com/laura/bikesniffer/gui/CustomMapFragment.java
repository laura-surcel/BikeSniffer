package com.laura.bikesniffer.gui;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.online.HttpAsyncRequest;
import com.laura.bikesniffer.online.MapUpdater;


/**
 * A placeholder fragment containing a simple view.
 */
public class CustomMapFragment extends Fragment 
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
	private static CustomMapFragment mInstance;
	private View mRootView;
	private ViewGroup mPrevContainer;
	
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CustomMapFragment getInstance(int sectionNumber) {
    	
    	if (mInstance == null)
    	{
    		mInstance =  new CustomMapFragment();
    		Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            mInstance.setArguments(args);
    	}
    	
        return mInstance;
    }
    
    public CustomMapFragment() {}

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
    	            	mMarkerHandler = new MarkerHandler(mActivity);
    	            	setUpMapIfNeeded();
    	            	connect();
    	            }
    	            mPrevContainer = container;
    	        } catch (InflateException e) {
    	            Log.w("InflateException happened ou nous", e.getMessage());
    	        }
    	}
    	 
        return mRootView;
    }
    
    public final void makeUseOfNewLocation(Location location) {
    	LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
    	GeoPosition gp = new GeoPosition(loc);
    	if( mPrevPosition != null)
    	{
    		Log.d("LOCATION", ""+gp.getDistanceInKmFrom(mPrevPosition));
    	}
    	
    	if( mPrevPosition == null || gp.getDistanceInKmFrom(mPrevPosition) > 0.5)
    	{
    		mMap.addMarker(new MarkerOptions().position(loc));
            if(mMap != null){
            	//Log.d("LOCATION", "Animate");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
            mPrevPosition = gp;
    	}        
    }
    
    
    public void setMapType(int type)
    {
    	if(mMap != null)
    	{
    		mMap.setMapType(type);
    	}
    }
    
    public void refreshLocation()
    {
    	Log.d("LOCATION", "Refresh");
    	new MapUpdater(mActivity, this).execute();
    }
    
    public void populateMapWithLocations(List<GeoPosition> locations)
    {
    	Log.d("LOCATION", "populateMapWithLocations");
    	mPrevPosition = null;
    	if(mMap != null)
    	{
    		mMap.clear();
    	}
    	
    	for(int i = 0; i < locations.size(); ++i)
    	{
    		GeoPosition gp = locations.get(i);
    		LatLng loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    		mMap.addMarker(new MarkerOptions().position(loc));
    		
    		if(mMap != null)
    		{
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
    	}
    }
    
    public GeoPosition getPosition()
    {
    	return mPrevPosition;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ActionBarActivity)activity;
    }    
    
    public void onPause() {
    	super.onPause();
        mLocationSource.onPause();
    }
    
    public void onResume() {
    	super.onResume();
        setUpMapIfNeeded();
        mLocationSource.onResume();
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            	setUpMap();
            }
        }
    }  
    
    private void connect()
    {
    	new HttpAsyncRequest(mActivity).execute();
    }
    
    private void setUpMap() {
    	mMap.setLocationSource(mLocationSource);
        mMap.setOnMarkerClickListener(mMarkerHandler);
        mMap.setOnMapLongClickListener(mLocationSource);
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
     	// Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
