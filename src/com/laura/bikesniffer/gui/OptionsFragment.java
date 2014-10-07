package com.laura.bikesniffer.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.R.id;
import com.laura.bikesniffer.R.layout;

public class OptionsFragment extends Fragment {
	
	private LinearLayout mView = null;
	
	public OptionsFragment() {}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	mView = (LinearLayout)inflater.inflate(R.layout.options, container, false);
    	setup();
    	return mView;
    }
    
    private void setup()
    {
    	Switch switch1 = (Switch) mView.findViewById(R.id.switch1); 
    	if(switch1 != null)
    	{
    		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

   	         @Override
   	         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
   	        	 Log.d("LOCATION", "satellite map view");
   	        	 if(isChecked)
   	        	 {
   	        		 CustomMapFragment.getInstance(1).setMapType(GoogleMap.MAP_TYPE_SATELLITE);
   	        	 }
   	        	 else
   	        	 {
   	        		 CustomMapFragment.getInstance(1).setMapType(GoogleMap.MAP_TYPE_NORMAL);
   	        	 }
   	         }     

            });
    	}
    	switch1.setChecked(true);
    }
}
