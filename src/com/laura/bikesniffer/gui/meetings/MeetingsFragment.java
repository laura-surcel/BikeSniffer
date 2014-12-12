package com.laura.bikesniffer.gui.meetings;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laura.bikesniffer.online.MeetingsRetrieverTask;
import com.laura.bikesniffer.utils.Meeting;

public class MeetingsFragment extends ListFragment  
{
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static MeetingsFragment sInstance;
    
    private MeetingsListViewAdapter mAdapter;
    private ArrayList<Meeting> mMeetingsList;
    private MeetingsRetrieverTask mMeetingsRetriever;
	private ActionBarActivity mActivity;
    
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
        mAdapter.addAll(mMeetingsList);
        
        // listen for new messages
        startMessageRetriever();
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        mActivity = (ActionBarActivity)activity;
    }
    
    void startMessageRetriever()
	{
    	mMeetingsRetriever = new MeetingsRetrieverTask(mActivity, this);
    	mMeetingsRetriever.startRepeatingTask();
	}
    
    public void addMeetings(ArrayList<Meeting> meetings)
	{
		ArrayList<Meeting> newOnes = new ArrayList<Meeting>();
		for(Meeting m:meetings)
		{			
			if(!meetingExists(m))
			{
				mMeetingsList.add(m);
				newOnes.add(m);
			}
		}
		mAdapter.addAll(newOnes);
		// ((MainActivity)getActivity()).addNewMessagesNumber(newOnes.size());
	}
	
	private boolean meetingExists(Meeting m)
	{
		for(Meeting mm:mMeetingsList)
		{
			if(m.id == mm.id)
			{
				return true;
			}
		}
		return false;
	}
}
