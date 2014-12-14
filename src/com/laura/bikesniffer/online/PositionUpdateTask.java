package com.laura.bikesniffer.online;

import android.content.Context;

import com.laura.bikesniffer.gui.BikesFragment;
import com.laura.bikesniffer.utils.PeriodicTask;

public class PositionUpdateTask extends PeriodicTask
{
	protected int mInterval = 30000; // milliseconds
	Context mContext;
	
	public PositionUpdateTask(Context c)
	{
		super();
		mContext = c;
	}
	
	@Override
	protected void performTask()
	{
		new PositionUpdateRequest(mContext, BikesFragment.getInstance(0)).execute();
	}
}
