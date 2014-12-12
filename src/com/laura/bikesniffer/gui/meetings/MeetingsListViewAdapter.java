package com.laura.bikesniffer.gui.meetings;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laura.bikesniffer.R;
import com.laura.bikesniffer.gui.MeetingsMapFragment;
import com.laura.bikesniffer.utils.Meeting;

public class MeetingsListViewAdapter extends ArrayAdapter<Meeting> 
{
    public MeetingsListViewAdapter(Context context, ArrayList<Meeting> meetings) 
    {
    	super(context, R.layout.meetings_layout, meetings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	MeetingViewHolder vh;
    	// Get the data item for this position
    	Meeting meeting = getItem(position);    
    	// Check if an existing view is being reused, otherwise inflate the view
    	if (convertView == null) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.meetings_layout, parent, false);
    		setViewHolder(convertView);
    	}
    	else if (((MeetingViewHolder)convertView.getTag()).needInflate) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.meetings_layout, parent, false);
			setViewHolder(convertView);
		}
      
    	vh = (MeetingViewHolder)convertView.getTag();
    	// Lookup view for data population
    	TextView userName = vh.userName;
    	TextView location = vh.location;
    	// Populate the data into the template view using the data object
    	userName.setText(meeting.userName);
    	location.setText("Location: " + meeting.location.getLatitude() + ", " + meeting.location.getLongitude());
    	
    	ImageButton reject = vh.watch;
    	reject.setContentDescription("" + meeting.userId);
    	reject.setOnClickListener(new OnClickListener() 
    	{			
			@Override
			public void onClick(View view) 
			{
				String userId = (String) view.getContentDescription();
				Log.d("MEETINGS", userId);
				MeetingsMapFragment.getInstance(4).getFocus();
				MeetingsMapFragment.getInstance(4).getRouteToUser(userId);
			}
		});
    	
    	// Return the completed view to render on screen
    	return convertView;
    }
    
    private void setViewHolder(View view) 
	{
		MeetingViewHolder vh = new MeetingViewHolder();
		vh.userName = (TextView)view.findViewById(R.id.biker);
		vh.location = (TextView) view.findViewById(R.id.location);
		vh.watch = (ImageButton) view.findViewById(R.id.watch);
		vh.needInflate = false;
		view.setTag(vh);
	}
}