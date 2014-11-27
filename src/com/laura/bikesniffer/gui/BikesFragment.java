package com.laura.bikesniffer.gui;

import java.util.ArrayList;
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
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.online.ConnectionRequest;
import com.laura.bikesniffer.online.DownloadTask;
import com.laura.bikesniffer.online.LocateUserRequest;
import com.laura.bikesniffer.online.MapUpdateRequest;
import com.laura.bikesniffer.utils.UsersManager;


/**
 * A placeholder fragment containing a simple view.
 */
public class BikesFragment extends Fragment 
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
    
    ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
    public void setupDirections()
    {
    	mMap.setOnMapClickListener(new OnMapClickListener() {

    	        @Override
    	        public void onMapClick(LatLng point) {

    	            if(markerPoints.size()>1){
    	                markerPoints.clear();
    	                mMap.clear();
    	            }

    	            markerPoints.add(point);

    	            MarkerOptions options = new MarkerOptions();

    	            options.position(point);

    	            if(markerPoints.size()==1){
    	                   options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    	            }else if(markerPoints.size()==2){
    	                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    	            }

    	            mMap.addMarker(options);

    	            if(markerPoints.size() >= 2){
    	                LatLng origin = markerPoints.get(0);
    	                LatLng dest = markerPoints.get(1);

    	                String url = getDirectionsUrl(origin, dest);

    	                DownloadTask downloadTask = new DownloadTask();

    	                downloadTask.execute(url);
    	            }
    	        }
    	    });
    }
    
    public final void makeUseOfNewLocation(Location location) 
    {
    	LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
    	GeoPosition gp = new GeoPosition(loc);
    	    	
    	if( mPrevPosition == null || gp.getDistanceInKmFrom(mPrevPosition) > 0.5)
    	{
    		Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
    		marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
    		
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));            
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
    	new MapUpdateRequest(mActivity, this).execute();
    }
	    
    public void populateMapWithLocations(List<GeoPosition> locations)
    {
    	if(mMap != null)
    	{
    		mMap.clear();
    		UsersManager.getInstance().clearHistory();
    	}
    	
    	LatLng loc = new LatLng(mPrevPosition.getLatitude(), mPrevPosition.getLongitude());
    	Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
		marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		
    	for(int i = 0; i < locations.size(); ++i)
    	{
    		GeoPosition gp = locations.get(i);
    		loc = new LatLng(gp.getLatitude(), gp.getLongitude());
    		marker = mMap.addMarker(new MarkerOptions().position(loc));
    		UsersManager.getInstance().addUserIdForMarker(gp.userId, marker);
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
        disconnect();    	
    }
    
    public void onResume() 
    {
    	super.onResume();
        setUpMapIfNeeded();
        connect();
        mLocationSource.onResume();
    }
    
    public void getFocus()
    {
    	Log.d("ROUTES", "getFocus ");
    	((MainActivity)mActivity).selectTab(0);
    }
    
    public void getRouteToUser(String userId)
    {
    	Log.d("ROUTES", "getRouteToUser " + userId);
    	new LocateUserRequest(mActivity, userId).execute();
    }
    
    public void showRouteToUser(double lat, double longit)
    {
    	if(mPrevPosition == null)
    		return;
    				
    	Log.d("ROUTES", "showRouteToUser");
    	LatLng origin = new LatLng(mPrevPosition.getLatitude(), mPrevPosition.getLongitude());
        LatLng dest = new LatLng(lat, longit);
        
    	String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
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
            }
        }
    }
    
    private void setUpMap() 
    {   	
    	mMap.setLocationSource(mLocationSource);
        mMap.setOnMarkerClickListener(mMarkerHandler);
        mMap.setOnMapLongClickListener(mLocationSource);
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
                    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                	}
                    return true;
                }
            });


        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private void connect()
    {
    	new ConnectionRequest(mActivity, true).execute();
    }
    
    private void disconnect()
    {
    	new ConnectionRequest(mActivity, false).execute();
    }
    
    private String getDirectionsUrl(LatLng origin,LatLng dest)
    {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // parameters = parameters + "&" + "mode=walking"; 
        
        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        
        Log.d("ROUTES", url);
        
        return url;
    }
    
    public void showDirections(PolylineOptions lineOptions)
    {
    	mMap.addPolyline(lineOptions);
    }
}
