package com.gdg.findme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gdg.findme.utils.AppVersionTool;

public class AboutActivity extends Activity implements OnClickListener {
	private TextView tv_version;
	private TextView tv_email_gyh;
	private TextView tv_emails_gzp;
	private TextView tv_emails_dpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		tv_version = (TextView) findViewById(R.id.tv_version);
		String version = String.valueOf(AppVersionTool.getAppVersion(this));
		tv_version.setText(version);
		tv_email_gyh = (TextView) findViewById(R.id.tv_email_gyh);
		tv_email_gyh.setOnClickListener(this);
		tv_emails_gzp = (TextView) findViewById(R.id.tv_emails_gzp);
		tv_emails_gzp.setOnClickListener(this);
		tv_emails_dpt = (TextView) findViewById(R.id.tv_emails_dpt);
		tv_emails_dpt.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		String email=((TextView)v).getText().toString().trim();
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				"mailto",email, null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Findme意见反馈");
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
}