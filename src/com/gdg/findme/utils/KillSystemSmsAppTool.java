package com.gdg.findme.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class KillSystemSmsAppTool {
	private static final String TAG = "KillSystemSmsAppTool";

	public static void killSystemSmsApp(Context context) {
		// stop xiaomi message app
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
	}
}