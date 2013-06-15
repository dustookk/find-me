package com.gdg.findme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gdg.findme.view.FragmentResult;
import com.gdg.findme.view.FragmentMain;
import com.gdg.findme.view.FragmentSetting;

public class HomeActivity extends FragmentActivity implements OnClickListener {
	private TextView iv1;
	private TextView iv2;

	public FragmentMain fragment_main;
	public FragmentSetting fragment_setting;
	public FragmentResult fragment3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home_activity);

		fragment_main = new FragmentMain();
		fragment_setting = new FragmentSetting();
		fragment3 = new FragmentResult();

		iv1 = (TextView) findViewById(R.id.iv1);
		iv2 = (TextView) findViewById(R.id.iv2);
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		onClick(iv1);
		
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.iv1:
			ft.replace(R.id.container, fragment_main);
			iv1.setBackgroundResource(R.drawable.store__top_view__tab1_focus);
			iv2.setBackgroundResource(R.drawable.store__top_view__tab2_normal);
			ft.commit();
			break;
		case R.id.iv2:
			ft.replace(R.id.container, fragment_setting);
			iv2.setBackgroundResource(R.drawable.store__top_view__tab2_focus);
			iv1.setBackgroundResource(R.drawable.store__top_view__tab1_normal);
			ft.commit();
			break;
		}
	}

}
