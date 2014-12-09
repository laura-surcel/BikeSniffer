package com.laura.bikesniffer.gui.messages;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laura.bikesniffer.R;
import com.laura.bikesniffer.gui.BikesFragment;
import com.laura.bikesniffer.utils.Message;

public class MessagesListViewAdapter extends ArrayAdapter<Message> 
{
    public MessagesListViewAdapter(Context context, ArrayList<Message> messages) 
    {
    	super(context, R.layout.messages_layout, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	MessageViewHolder vh;
    	// Get the data item for this position
    	Message message = getItem(position);    
    	// Check if an existing view is being reused, otherwise inflate the view
    	if (convertView == null) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_layout, parent, false);
    		setViewHolder(convertView);
    	}
    	else if (((MessageViewHolder)convertView.getTag()).needInflate) 
    	{
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_layout, parent, false);
			setViewHolder(convertView);
		}
      
    	final View finalView = convertView;
    	vh = (MessageViewHolder)convertView.getTag();
    	// Lookup view for data population
    	TextView userName = vh.userName;
    	TextView tvMessage = vh.message;
    	// Populate the data into the template view using the data object
    	userName.setText(message.senderName);
    	
    	switch(message.type)
    	{
    		case 1:
    			tvMessage.setText("Meet me at my location");
    			break;
    		case 2:
    			tvMessage.setText("Meet me at your location");
    			break;
    		case 3:
    			tvMessage.setText("Meet me at this location");
    			break;
    		default:
    			break;
    	}
    	
    	ImageButton reject = vh.reject;
    	reject.setContentDescription("" + message.id);
    	reject.setOnClickListener(new OnClickListener() 
    	{			
			@Override
			public void onClick(View view) 
			{
				long messageId = Long.parseLong(view.getContentDescription().toString());
				MessagesFragment.getInstance(1).removeMessage(finalView, messageId);
				MessagesFragment.getInstance(1).performRemoveRequest(messageId);
			}
		});
    	
    	ImageButton accept = vh.accept;
    	accept.setContentDescription(message.toJson().toString());
    	accept.setOnClickListener(new OnClickListener() 
    	{			
			@Override
			public void onClick(View view) 
			{
				try 
				{
					JSONObject reconstructed = new JSONObject(view.getContentDescription().toString());
					long messageId = reconstructed.getLong("id");
					String senderId = reconstructed.getString("sender_id");
					//BikesFragment.getInstance(0).getFocus();
					//BikesFragment.getInstance(0).getRouteToUser(senderId);
					MessagesFragment.getInstance(1).removeMessage(finalView, messageId);
					MessagesFragment.getInstance(1).performAcceptRequest(senderId, messageId);
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
		});
    	
    	// Return the completed view to render on screen
    	return convertView;
    }
    
    private void setViewHolder(View view) 
	{
		MessageViewHolder vh = new MessageViewHolder();
		vh.userName = (TextView)view.findViewById(R.id.userName);
		vh.message = (TextView) view.findViewById(R.id.message);
		vh.accept = (ImageButton) view.findViewById(R.id.accept);
		vh.reject = (ImageButton) view.findViewById(R.id.reject);
		vh.needInflate = false;
		view.setTag(vh);
	}
}