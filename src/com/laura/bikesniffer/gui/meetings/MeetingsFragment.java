package com.laura.bikesniffer.gui.meetings;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.laura.bikesniffer.utils.GeoPosition;
import com.laura.bikesniffer.utils.Meeting;

public class MeetingsFragment extends ListFragment  
{
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static MeetingsFragment sInstance;
    
    private MeetingsListViewAdapter mAdapter;
    private ArrayList<Meeting> mMeetingsList;
    
    public static MeetingsFragment getInstance(int sectionNumber) 
	{
		if (sInstance == null)
    	{
    		sInstance =  new MeetingsFragment();
    		Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            sInstance.setArguments(args);
    	}
    	
        return sInstance;
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
	{
    	// Instantiating an adapter to store each items
        mAdapter = new MeetingsListViewAdapter(getActivity(), new ArrayList<Meeting>());
        // Attach the adapter to a ListView
        setListAdapter(mAdapter);
        
        mMeetingsList = new ArrayList<Meeting>();
        for(int i = 0; i < 10; ++i)
        {
        	mMeetingsList.add(new Meeting("Laura", "6bd", new GeoPosition(new LatLng(23.21, 45.12))));
        }
        
        // listen for new messages
        mAdapter.addAll(mMeetingsList);
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
