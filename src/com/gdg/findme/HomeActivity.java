package com.gdg.findme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.gdg.findme.view.Fragment2;
import com.gdg.findme.view.Fragment3;
import com.gdg.findme.view.Fragment_main;
import com.gdg.findme.view.Fragment_setting;

public class HomeActivity extends FragmentActivity implements OnClickListener {
	private TextView iv1;
	private TextView iv2;

	public Fragment_main fragment_main;
	public Fragment_setting fragment_setting;
	public Fragment3 fragment3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home_activity);

		fragment_main = new Fragment_main();
		fragment_setting = new Fragment_setting();
		fragment3 = new Fragment3();

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
