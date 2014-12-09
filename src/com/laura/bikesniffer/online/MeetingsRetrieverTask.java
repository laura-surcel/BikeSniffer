package com.laura.bikesniffer.online;

import android.content.Context;

import com.laura.bikesniffer.gui.meetings.MeetingsFragment;
import com.laura.bikesniffer.utils.PeriodicTask;

public class MeetingsRetrieverTask extends PeriodicTask
{
	private MeetingsFragment mFragment;
	protected int mInterval = 60000;
	Context mContext;
	
	public MeetingsRetrieverTask(Context c, MeetingsFragment m)
	{
		super();
		mContext = c;
		mFragment = m;
	}
	
	@Override
	protected void performTask()
	{
		new RetrieveMeetingsRequest(mContext, mFragment).execute();
	}
}
