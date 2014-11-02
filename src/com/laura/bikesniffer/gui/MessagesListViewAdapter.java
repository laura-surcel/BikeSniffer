package com.laura.bikesniffer.gui;

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
import com.laura.bikesniffer.utils.Message;

public class MessagesListViewAdapter extends ArrayAdapter<Message> 
{
    public MessagesListViewAdapter(Context context, ArrayList<Message> messages) 
    {
    	super(context, R.layout.listview_layout, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder vh;
    	// Get the data item for this position
    	Message message = getItem(position);    
    	// Check if an existing view is being reused, otherwise inflate the view
    	if (convertView == null) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
    		setViewHolder(convertView);
    	}
    	else if (((ViewHolder)convertView.getTag()).needInflate) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
			setViewHolder(convertView);
		}
      
    	final View finalView = convertView;
    	vh = (ViewHolder)convertView.getTag();
    	// Lookup view for data population
    	TextView userName = vh.userName;
    	TextView tvMessage = vh.message;
    	// Populate the data into the template view using the data object
    	userName.setText(message.senderId);
    	tvMessage.setText("Meet me");
    	
    	ImageButton reject = vh.reject;
    	reject.setContentDescription("" + message.id);
    	reject.setOnClickListener(new OnClickListener() 
    	{			
			@Override
			public void onClick(View view) 
			{
				long messageId = Long.parseLong(view.getContentDescription().toString());
				Log.d("CONNECTION", "reject " + messageId);
				MessagesFragment.getInstance(1).removeMessage(finalView, messageId);				
			}
		});
    	
    	ImageButton accept = vh.accept;
    	accept.setContentDescription("" + message.id);
    	accept.setOnClickListener(new OnClickListener() 
    	{			
			@Override
			public void onClick(View view) 
			{
				long messageId = Long.parseLong(view.getContentDescription().toString());
				Log.d("CONNECTION", "accept " + messageId);
				//MessagesFragment.getInstance(1).removeElement(messageId);
			}
		});
    	
    	// Return the completed view to render on screen
    	return convertView;
    }
    
    private void setViewHolder(View view) 
	{
		ViewHolder vh = new ViewHolder();
		vh.userName = (TextView)view.findViewById(R.id.userName);
		vh.message = (TextView) view.findViewById(R.id.message);
		vh.accept = (ImageButton) view.findViewById(R.id.accept);
		vh.reject = (ImageButton) view.findViewById(R.id.reject);
		vh.needInflate = false;
		view.setTag(vh);
	}
}