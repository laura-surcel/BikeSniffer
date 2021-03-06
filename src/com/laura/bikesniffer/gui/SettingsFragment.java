package com.laura.bikesniffer.gui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.laura.bikesniffer.R;
import com.laura.bikesniffer.online.SetNameRequest;

public class SettingsFragment extends Fragment 
{
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static SettingsFragment sInstance;
    
    private ActionBarActivity mActivity;
    private EditText nameEditor;
    private CharSequence name = "";
    private View mRootView;
    private RadioGroup mMapSettings;
    private Spinner mRadiusSpinner;
    
    public static SettingsFragment getInstance(int sectionNumber) 
	{
		if (sInstance == null)
    	{
    		sInstance =  new SettingsFragment();
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
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
        
    	mRootView = inflater.inflate(R.layout.settings_layout, container, false);
    	nameEditor = (EditText)mRootView.findViewById(R.id.editName);
    	Log.d("PREFS", settings.getString("biker_name", ""));
    	
    	name = settings.getString("biker_name", "");
    	nameEditor.setText(name);
    	
    	if(!name.toString().equalsIgnoreCase(""))
    	{
    		mActivity.setTitle("Welcome, " + name);
    	}
        
    	nameEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				if (actionId == EditorInfo.IME_ACTION_DONE) 
				{
					if(name != "")
					{
						mActivity.setTitle("Welcome, " + name);		
						SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
						SharedPreferences.Editor edit = settings.edit();
						edit.putString("biker_name", "" + name);
						edit.commit();
						Log.d("SAVE", name + "");
						new SetNameRequest(mActivity, name + "").execute();
					}
    	        }
				nameEditor.setText(name);
				return false;
			}
    	  });
    	
    	nameEditor.addTextChangedListener(new TextWatcher() 
    	{
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				name = s;
			}
    	});
    	
    	mMapSettings = (RadioGroup)mRootView.findViewById(R.id.mapOptionsGroup);
    	mMapSettings.check(settings.getInt("map_options", R.id.map_option1));
    	Log.d("PREFS", "" + settings.getInt("map_options", R.id.map_option1));
    	
    	mMapSettings.setOnCheckedChangeListener(new OnCheckedChangeListener() 
    	{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
				SharedPreferences.Editor edit = settings.edit();
				edit.putInt("map_options", checkedId);
				edit.commit();
				Log.d("SAVE", checkedId + "");
				
				switch(checkedId)
				{
					case R.id.map_option1:
						BikesFragment.getInstance(0).setMapType(GoogleMap.MAP_TYPE_NORMAL);
						break;
					case R.id.map_option2:
						BikesFragment.getInstance(0).setMapType(GoogleMap.MAP_TYPE_SATELLITE);
						break;
				}
			}
		});
    	
    	mRadiusSpinner = (Spinner)mRootView.findViewById(R.id.radiusSpinner);
    	mRadiusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
				SharedPreferences.Editor edit = settings.edit();
				edit.putInt("radius_options", arg2);
				edit.commit();
				BikesFragment.getInstance(0).onSearchRadiusChanged();
				Log.d("SAVE", arg2 + "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
    		
		});
    	Log.d("PREFS", "" + settings.getInt("radius_options", 0));
    	mRadiusSpinner.setSelection(settings.getInt("radius_options", 0));
        return mRootView;
    }

    public void onPause()
    {
    	if(mActivity != null)
    	{
        	InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    		imm.hideSoftInputFromWindow(nameEditor.getWindowToken(), 0);    		
    	}
		super.onPause();
    }
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        mActivity = (ActionBarActivity)activity;
    }
    
    @Override
    public void onDetach()
    {
        super.onDetach();
        mActivity = null;
    }
}
