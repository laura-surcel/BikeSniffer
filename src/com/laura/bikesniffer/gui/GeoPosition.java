package com.laura.bikesniffer.gui;

import com.google.android.gms.maps.model.*;

public class GeoPosition
{
	private LatLng latLng;
	
	public String userId;
	
	public GeoPosition(LatLng latLng)
	{
		this.latLng = latLng;
	}
	
	public double getLatitude()
	{
		return latLng.latitude;
	}
	
	public double getLongitude()
	{
		return latLng.longitude;
	}
	
	public double getDistanceInKmFrom(GeoPosition gp)
	{
		return distance(latLng, gp.latLng, 'K');
	}
	
	public double getDistanceInMilesFrom(GeoPosition gp)
	{
		return distance(latLng, gp.latLng, 'M');
	}
	
	/** unit:
     * K  - kilometers
     * M  - miles
     * N  - nautical miles 
     * */
    private double distance(LatLng position1, LatLng position2, char unit) {
    	double lon1 = position1.longitude;
    	double lon2 = position2.longitude;
    	double lat1 = position1.latitude;
    	double lat2 = position1.latitude;
    	
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        
        // in Km
        if (unit == 'K') { 
          dist = dist * 1.609344;
        } 
        // in Nautical Miles
        else if (unit == 'N') {
          dist = dist * 0.8684;
        }
        
        return dist;
      }

      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      /*::  This function converts decimal degrees to radians             :*/
      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
      }

      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      /*::  This function converts radians to decimal degrees             :*/
      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
      private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
      }
}
