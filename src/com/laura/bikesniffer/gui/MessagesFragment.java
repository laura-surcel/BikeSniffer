package com.laura.bikesniffer.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.laura.bikesniffer.R;

public class MessagesFragment extends ListFragment 
{
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static MessagesFragment sInstance;
	
	// Array of strings storing country names
    String[] countries = new String[] {
        "George",
        "Claudiu",
        "Silviu",
        "Cristi",
        "Bogdan",
        "Nelu",
        "Ana",
        "Nicu",
        "Simona",
        "Ion"
    };
 
    // Array of integers points to images stored in /res/drawable/
    int[] flags = new int[]{
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher,
        R.drawable.ic_launcher
    };
 
    // Array of strings to store currencies
    String[] currency = new String[]{
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
        "Meet me",
    };
	
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
		init();		
		return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	void init()
	{
		// Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
 
        for(int i=0;i<10;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", "User : " + countries[i]);
            hm.put("cur", "Request : " + currency[i]);
            hm.put("flag", Integer.toString(flags[i]));
            hm.put("identifier", Integer.toString(i));
            aList.add(hm);
        }
 
        // Keys used in Hashmap
        String[] from = { "flag","txt","cur"};
 
        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.txt,R.id.cur};
 
        // Instantiating an adapter to store each items
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
        setListAdapter(adapter);
	}
	
	public void removeElement()
	{
		
	}
}
