package com.laura.bikesniffer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class OptionsFragment extends Fragment {
	
	private RadioGroup mView;
	
	public OptionsFragment() {}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	mView = (RadioGroup) inflater.inflate(
                R.layout.options, container, false);
        return mView;
    }
}
