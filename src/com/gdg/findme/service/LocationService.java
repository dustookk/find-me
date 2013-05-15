package com.gdg.findme.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.gdg.findme.receiver.SMSReceiver;
import com.gdg.findme.utils.KillSystemSmsAppTool;

public class LocationService extends Service {

	private SMSReceiver smsReceiver;

	@Override
	public void onCreate() {
		smsReceiver = new SMSReceiver();
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
	 * register the broadcast receiver
	 */
	private void register() {

		IntentFilter intent = new IntentFilter();
		intent.setPriority(Integer.MAX_VALUE);
		intent.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, intent);
	}

	/**
	 * unregister the broadcast receiver
	 */
	private void unregister() {
		unregisterReceiver(smsReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
