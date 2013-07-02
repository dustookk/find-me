package com.gdg.findme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gdg.findme.service.LocationService;

public class OnBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean startService = sp.getBoolean("startService", false);
		if(startService) {
			Intent locationService = new Intent(context, LocationService.class);
			context.startService(locationService);
		}
	}
}
