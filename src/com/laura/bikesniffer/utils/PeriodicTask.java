package com.laura.bikesniffer.utils;

import android.os.Handler;

public class PeriodicTask 
{
	protected int mInterval = 5000; // 5 seconds by default, can be changed later
	protected Handler mHandler;
	protected Runnable mStatusChecker = null;
			
	public PeriodicTask()
	{
		mHandler = new Handler();
		mStatusChecker = new Runnable() 
		{
		    @Override 
		    public void run() 
		    {
		    	performTask();
		    	mHandler.postDelayed(mStatusChecker, mInterval);
		    }
		  };
	}

	protected void performTask()
	{
		
	}
	
	public void startRepeatingTask() 
	{
		mStatusChecker.run(); 
	}

	public void stopRepeatingTask() 
	{
		mHandler.removeCallbacks(mStatusChecker);
	}
}
