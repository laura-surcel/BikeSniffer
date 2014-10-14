package com.laura.bikesniffer.gui;

import com.laura.bikesniffer.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomMessage extends Dialog implements
android.view.View.OnClickListener 
{

	public Activity c;
	public Dialog d;
	public Button yes, no;
	
	public CustomMessage(Activity a) 
	{
		super(a);
		this.c = a;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message);
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.btn_yes:
				Log.d("MarkerHandler", "send");
			case R.id.btn_no:
				Log.d("MarkerHandler", "cancel");
				break;
			default:
				break;
		}
		
		dismiss();
	}
}
