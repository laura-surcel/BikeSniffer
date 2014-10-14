package com.laura.bikesniffer.gui;

import com.laura.bikesniffer.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
		setContentView(R.layout.dialog);
		
		/*EditText text = (EditText) findViewById(R.id.message);
	    text.setText("Test");
	    text.setCompoundDrawablesWithIntrinsicBounds(null, null,
	                       c.getResources().getDrawable(R.drawable.dialog), null);*/
		    
		/*yes = (Button) findViewById(R.id.yes);
		no = (Button) findViewById(R.id.no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);*/
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