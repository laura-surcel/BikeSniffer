package com.laura.bikesniffer.online;

import android.content.Context;

import com.laura.bikesniffer.gui.BikesFragment;
import com.laura.bikesniffer.utils.PeriodicTask;

public class MessageRetrieverTask extends PeriodicTask
{
	private BikesFragment mMap;
	protected int mInterval = 60000;
	Context mContext;
	
	public MessageRetrieverTask(Context c, BikesFragment m)
	{
		super();
		mContext = c;
		mMap = m;
	}
	
	@Override
	protected void performTask()
	{
		new RetrieveMessagesRequest(mContext, mMap).execute();
	}
}
