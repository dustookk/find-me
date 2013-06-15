package com.gdg.findme.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.gdg.findme.observer.SmsObserver;
import com.gdg.findme.receiver.SMSReceiver;
import com.gdg.findme.utils.KillSystemSmsAppTool;

public class LocationService extends Service {

	private SMSReceiver smsReceiver;
	private Uri uri;
	private SmsObserver smsObserver;

	@Override
	public void onCreate() {
		smsReceiver = new SMSReceiver();
		uri = Uri.parse("content://sms/");
		smsObserver = new SmsObserver(new Handler(), this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		KillSystemSmsAppTool.killSystemSmsApp(this);
		register();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregister();
	}

	/**
	 * register the broadcast receiver , observer.
	 */
	private void register() {
		// receiver
		IntentFilter intent = new IntentFilter();
		intent.setPriority(Integer.MAX_VALUE);
		intent.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, intent);
		
		// observer
		getContentResolver().registerContentObserver(uri, true, smsObserver);
		
	}
	
	/**
	 * unregister the broadcast receiver, observer.
	 */
	private void unregister() {
		unregisterReceiver(smsReceiver);
		getContentResolver().unregisterContentObserver(smsObserver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
