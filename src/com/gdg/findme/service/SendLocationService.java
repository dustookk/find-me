package com.gdg.findme.service;


import com.baidu.locTest.Location;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

public class SendLocationService extends IntentService {
	private LocationClient mLocClient;
	private TextView mTv;
	private static final String TAT = "Locationreporter";

	public SendLocationService() {
		super("name");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAT, "IntentService ");
		String originatingAddress = intent.getStringExtra("originatingAddress");
		mTv = new TextView(getApplicationContext());
		String locationResult = mTv.getText().toString();
		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = mTv;
		setLocationOption();
		mLocClient.start();
		
		
		while(locationResult==""){
			//TODO 获得位置信息
			locationResult = mTv.getText().toString();
			/*locationResult = GPSInfoProvider.getInstance(this).getLocation();
			try {
				Thread.sleep(1000);
				Log.i(TAT, "no locationResult "+locationResult);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		
		Log.i(TAT, "location "+locationResult);
//		关闭gps位置服务
		mLocClient.stop();
		SmsManager.getDefault().sendTextMessage(originatingAddress, null,locationResult, null, null);
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
