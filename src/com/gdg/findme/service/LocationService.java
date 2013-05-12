package com.gdg.findme.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.gdg.findme.receiver.SMSReceiver;

public class LocationService extends Service {

	private static final String TAG = "com.gdg.findme";
	private SMSReceiver smsReceiver;

	@Override
	public void onCreate() {
		smsReceiver = new SMSReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// stop xiaomi message app
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appInfo : runningAppProcesses) {
			String processName = appInfo.processName;
			if (appInfo.processName.contains("sms")
					|| appInfo.processName.contains("smS")
					|| appInfo.processName.contains("sMs")
					|| appInfo.processName.contains("Sms")
					|| appInfo.processName.contains("SMs")
					|| appInfo.processName.contains("SmS")
					|| appInfo.processName.contains("sMS")) {
				Log.i(TAG, "kill process" + processName);
				am.killBackgroundProcesses(processName);
			}

		}

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
