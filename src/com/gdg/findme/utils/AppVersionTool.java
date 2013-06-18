package com.gdg.findme.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppVersionTool {
	public static double getAppVersion(Context context) {
		try {
			String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			return Double.parseDouble(versionName);
		} catch (NameNotFoundException e) {
			// can not reach
			return 0;
		}
	}
}
