package com.laura.bikesniffer.gui.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

import com.laura.bikesniffer.gui.MainActivity;
import com.laura.bikesniffer.online.AcceptMeetingRequest;
import com.laura.bikesniffer.online.MessageRetrieverTask;
import com.laura.bikesniffer.online.RemoveMessagesRequest;
import com.laura.bikesniffer.utils.Message;

public class MessagesFragment extends ListFragment 
{
	static final int ANIMATION_DURATION = 200;
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static MessagesFragment sInstance;

	private MessageRetrieverTask mMessageRetriever;
	private ActionBarActivity mActivity;
	private MessagesListViewAdapter mAdapter;
	private ArrayList<Message> mMessagesList;
	
	public static MessagesFragment getInstance(int sectionNumber) 
	{
		if (sInstance == null)
    	{
    		sInstance =  new MessagesFragment();
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
        mAdapter = new MessagesListViewAdapter(getActivity(), new ArrayList<Message>());
        // Attach the adapter to a ListView
        setListAdapter(mAdapter);
        
        mMessagesList = new ArrayList<Message>();
        
        // listen for new messages
        startMessageRetriever();
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
    public void setMenuVisibility(final boolean visible) 
	{
        super.setMenuVisibility(visible);
        
        if (visible) 
        {
        	((MainActivity)getActivity()).clearBadgeNumber();
        }
    }
	
	@Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        mActivity = (ActionBarActivity)activity;
    } 
	
	void startMessageRetriever()
	{
		mMessageRetriever = new MessageRetrieverTask(mActivity, this);
		mMessageRetriever.startRepeatingTask();
	}
	
	public void removeMessage(final View view, final long messageId)
	{
		AnimationListener al = new AnimationListener() 
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				Message m = null;
				for(Message mm:mMessagesList)
				{
					if(messageId == mm.id)
					{
						m = mm;
					}
				}
				
				if(m != null)
				{
					mMessagesList.remove(m);
					mAdapter.remove(m);
				}
				
				MessageViewHolder vh = (MessageViewHolder)view.getTag();
				vh.needInflate = true;
				mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationStart(Animation arg0) {}
		};
		
		collapse(view, al);
	}
	
	public void performRemoveRequest(long messageId)
	{
		List<Long> list = new Vector<Long>();
		list.add(Long.valueOf(messageId));
		new RemoveMessagesRequest(getActivity(), list).execute();
	}
	
	public void performAcceptRequest(String senderId, long messageId)
	{
		new AcceptMeetingRequest(getActivity(), senderId, messageId).execute();
	}
	
	private void collapse(final View v, AnimationListener al) 
	{
		final int initialHeight = v.getMeasuredHeight();
		
		Animation anim = new Animation() 
		{
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) 
			{
				if (interpolatedTime == 1) 
				{
					v.setVisibility(View.GONE);
				}
				else 
				{
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}
			@Override
			public boolean willChangeBounds() 
			{
				return true;
			}
		};
		
		if (al!=null) 
		{
			anim.setAnimationListener(al);
		}
		
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}
	
	public void addMessages(ArrayList<Message> messages)
	{
		ArrayList<Message> newOnes = new ArrayList<Message>();
		for(Message m:messages)
		{			
			if(!messageExists(m))
			{
				mMessagesList.add(m);
				newOnes.add(m);
			}
		}
		mAdapter.addAll(newOnes);
		((MainActivity)getActivity()).addNewMessagesNumber(newOnes.size());
	}
	
	private boolean messageExists(Message m)
	{
		for(Message mm:mMessagesList)
		{
			if(m.id == mm.id)
			{
				return true;
			}
		}
		return false;
	}
}
