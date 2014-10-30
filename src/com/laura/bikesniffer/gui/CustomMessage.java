package com.laura.bikesniffer.gui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.laura.bikesniffer.R;

public class CustomMessage extends Dialog implements
android.view.View.OnClickListener 
{

	public Activity c;
	public Dialog d;
	public Button send, cancel;
	
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
		/*send = (Button) findViewById(R.id.btn_send);
		cancel = (Button) findViewById(R.id.btn_cancel);
		send.setOnClickListener(this);
		cancel.setOnClickListener(this);*/
	}
	
	@Override
	public void onClick(View v) 
	{
		/*switch (v.getId()) 
		{
			case R.id.btn_send:
				Log.d("MarkerHandler", "send");
			case R.id.btn_cancel:
				Log.d("MarkerHandler", "cancel");
				break;
			default:
				break;
		}*/
		
		dismiss();
	}
}
