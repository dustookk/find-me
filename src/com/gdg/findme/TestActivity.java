package com.gdg.findme;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gdg.findme.service.SendLocationService;

public class TestActivity extends Activity implements OnClickListener {
	private static final String TAT = "com.gdg.findme";
	private LinearLayout ll_group;
	private Button bt_gyh;
	private Button bt_dpt;
	private Button bt_gzp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll_group = new LinearLayout(this);
		ll_group.setOrientation(LinearLayout.VERTICAL);

		bt_gyh = new Button(this);
		bt_gyh.setText("gyh测试");
		ll_group.addView(bt_gyh);
		bt_gyh.setOnClickListener(this);

		bt_dpt = new Button(this);
		bt_dpt.setText("dpt测试");
		ll_group.addView(bt_dpt);
		bt_dpt.setOnClickListener(this);

		bt_gzp = new Button(this);
		bt_gzp.setText("gzp测试");
		ll_group.addView(bt_gzp);
		bt_gzp.setOnClickListener(this);

		setContentView(ll_group);
	}

	@Override
	public void onClick(View v) {
		if (v == bt_gyh) {
			Intent intent = new Intent(this, SendLocationService.class);
			startService(intent);
		} else if (v == bt_dpt) {
			/*Intent i = new Intent(this, GeoCoderDemo.class);
			startActivity(i);*/
		} else if (v == bt_gzp) {
			Intent i = new Intent(this, HomeActivity.class);
			startActivity(i);
			// gzp_test
		}
	}
}
