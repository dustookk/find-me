package com.gdg.findme;


import java.util.Date;

import com.gdg.findme.ui.SlideLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
/**
 * gzp gyh dpt
 */
	private SlideLayout slideLayout;
	private TextView tv_system_about;
	private LinearLayout ll_sys_about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		
		tv_system_about =(TextView) findViewById(R.id.tv_system_about);
		tv_system_about.setOnClickListener(this);
		
		ll_sys_about=(LinearLayout) findViewById(R.id.ll_sys_about);
		ll_sys_about.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_sys_about:
			slideLayout.snapToScreen(0, false);
			break;
		case R.id.tv_system_about:
			slideLayout.snapToScreen(1, false);
			break;
		
		}
		
	}

}
