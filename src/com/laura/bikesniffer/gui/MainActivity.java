package com.laura.bikesniffer.gui;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laura.bikesniffer.R;
import com.laura.bikesniffer.gui.meetings.MeetingsFragment;
import com.laura.bikesniffer.gui.messages.MessagesFragment;

public class MainActivity extends ActionBarActivity 
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	ActionBar actionBar;
	int numOfMessages = 0;
	Fragment mCurrentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		actionBar = getSupportActionBar();
	    
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setOffscreenPageLimit(5);
		
		 // Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	// When the tab is selected, switch to the
	            // corresponding page in the ViewPager.
	            mViewPager.setCurrentItem(tab.getPosition());
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        	SettingsFragment.getInstance(3).onPause();
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }
	    };
	    
	    mViewPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                    // When swiping between pages, select the
	                    // corresponding tab.
	                    getActionBar().setSelectedNavigationItem(position);
	                }
	            });

	    
	    int[] icons = {R.drawable.bikes, R.drawable.meetings, R.drawable.message, R.drawable.settings, R.drawable.bikes_meet};

	    // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 5; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setIcon(icons[i])
	                        .setTabListener(tabListener));
	    }
	    
	    actionBar.getTabAt(2).setCustomView(renderTabView(this, R.string.action_example, R.drawable.abc_item_background_holo_light, numOfMessages));
	}
	
	@SuppressLint("InflateParams")
	public static View renderTabView(Context context, int titleResource, int backgroundResource, int badgeNumber) {
        FrameLayout view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.tab_default, null);
        // We need to manually set the LayoutParams here because we don't have a view root
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((TextView) view.findViewById(R.id.tab_text)).setText(titleResource);
        ((ImageView) view.findViewById(R.id.tab_icon)).setBackgroundResource(R.drawable.message);
        view.findViewById(R.id.tab_text).setBackgroundResource(backgroundResource);
        updateTabBadge((TextView) view.findViewById(R.id.tab_badge), badgeNumber);
        return view;
    }

    public static void updateTabBadge(ActionBar.Tab tab, int badgeNumber) {
        updateTabBadge((TextView) tab.getCustomView().findViewById(R.id.tab_badge), badgeNumber);
    }

    private static void updateTabBadge(TextView view, int badgeNumber) {
        if (badgeNumber > 0) {
            view.setVisibility(View.VISIBLE);
            view.setText(Integer.toString(badgeNumber));
        }
        else {
            view.setVisibility(View.GONE);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}
	
	public void addNewMessagesNumber(int no)
	{
		numOfMessages = numOfMessages + no;
		updateTabBadge(actionBar.getTabAt(2), numOfMessages);
	}
	
	public void clearBadgeNumber()
	{
		numOfMessages = 0;
		updateTabBadge(actionBar.getTabAt(2), numOfMessages);
	}
	
	public void selectTab(int position)
	{
		actionBar.selectTab(actionBar.getTabAt(position));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
        return super.onOptionsItemSelected(item);
	}

	@Override
    public void onBackPressed() 
	{
        if (mViewPager.getCurrentItem() == 0) 
        {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } 
        else 
        {
            // Otherwise, select the previous step.
        	mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
    
	public double getSearchRadius()
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		int option = settings.getInt("radius_options", 0);
		
		switch(option)
		{
		case 0:
			return 5;
		case 1:
			return 10;
		case 2:
			return 20;
		case 3:
			return 30;
		case 4:
			return 50;
		default:
			return 100;
		}
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter 
	{

		public SectionsPagerAdapter(FragmentManager fm) 
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			// getItem is called to instantiate the fragment for the given page.
			switch(position)
			{
			case 0:
				mCurrentFragment = BikesFragment.getInstance(position);
				break;
			case 1:
				mCurrentFragment = MeetingsFragment.getInstance(position);
				break;
			case 2:
				mCurrentFragment = MessagesFragment.getInstance(position);
				break;
			case 3:
				mCurrentFragment = SettingsFragment.getInstance(position);
				break;
			case 4:
				mCurrentFragment = MeetingsMapFragment.getInstance(position);
				break;
			}
			return mCurrentFragment;
		}

		@Override
		public int getCount() 
		{
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) 
		{
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section3).toUpperCase(l);
			case 4:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
