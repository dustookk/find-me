package com.gdg.findme.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class KillSystemSmsAppTool {
	private static final String TAG = "KillSystemSmsAppTool";

	public static void killSystemSmsApp(Context context) {
		// stop xiaomi and meizu message app
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appInfo : runningAppProcesses) {
			String processName = appInfo.processName;
			if (appInfo.processName.contains("sms")
					|| appInfo.processName.contains("mms")
					|| appInfo.processName.contains("ijinshan.mguard")
					|| appInfo.processName.contains("qihoo360.mobilesafe")
					|| appInfo.processName.contains("tencent.qqpimsecure")) {
				Log.i(TAG, "kill process" + processName);
				am.killBackgroundProcesses(processName);
			}
		}
	}
}