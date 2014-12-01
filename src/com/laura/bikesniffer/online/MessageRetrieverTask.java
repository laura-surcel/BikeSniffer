package com.laura.bikesniffer.online;

import android.content.Context;

import com.laura.bikesniffer.gui.messages.MessagesFragment;
import com.laura.bikesniffer.utils.PeriodicTask;

public class MessageRetrieverTask extends PeriodicTask
{
	private MessagesFragment mFragment;
	protected int mInterval = 60000;
	Context mContext;
	
	public MessageRetrieverTask(Context c, MessagesFragment m)
	{
		super();
		mContext = c;
		mFragment = m;
	}
	
	@Override
	protected void performTask()
	{
		new RetrieveMessagesRequest(mContext, mFragment).execute();
	}
}
