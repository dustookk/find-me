package com.gdg.findme.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNetworkConnection {
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return  cm.getActiveNetworkInfo() != null;
	}
}
