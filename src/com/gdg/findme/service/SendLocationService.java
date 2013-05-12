package com.gdg.findme.service;


import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gdg.findme.baidu.Location;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

public class SendLocationService extends IntentService {
	private LocationClient mLocClient;

	private static final String TAT = "Locationreporter";

	public SendLocationService() {
		super("name");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAT, "IntentService ");
		String originatingAddress = intent.getStringExtra("originatingAddress");
		mLocClient = ((Location)getApplication()).mLocationClient;
		setLocationOption();
		mLocClient.start();
		
		
		while(Location.locationResult==""){
			try {
				Thread.sleep(1000);
				Log.i(TAT, "no locationResult "+Location.locationResult);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Log.i(TAT, "location "+Location.locationResult);
//		关闭gps位置服务
		mLocClient.stop();
		//发送短信的功能
		//SmsManager.getDefault().sendTextMessage(originatingAddress, null,locationResult, null, null);
	}
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setScanSpan(3000);
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPoiNumber(10);
		option.disableCache(true);		
		mLocClient.setLocOption(option);
	}
}
