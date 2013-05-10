package com.gdg.findme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Setting Guide
 * 
 * @author gaoyihang
 * 
 */
public class GuideActivity extends FragmentActivity {
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		fragmentManager=getSupportFragmentManager();
	}

}
