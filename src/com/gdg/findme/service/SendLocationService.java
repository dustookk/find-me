package com.gdg.findme.service;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gdg.findme.baidu.Location;

/**
 * 把自己的坐标回复给请求手机
 * 
 * @author gaoyihang
 * 
 */
public class SendLocationService extends IntentService {
	private LocationClient mLocClient;

	private static final String TAT = "com.gdg.findme";

	public SendLocationService() {
		super("name");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAT, "IntentService ");
		String originatingAddress = intent.getStringExtra("originatingAddress");
		mLocClient = ((Location) getApplication()).mLocationClient;
		setLocationOption();
		mLocClient.start();

		while (Location.locationResult == "") {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String result = Location.locationResult;
		String[] strings = result.split("\n");
		String addr = strings[0];
		String longt = strings[1];
		String[] longts = longt.split(":");
		String longitude = longts[1];
		String lat = strings[2];
		String[] lats = lat.split(":");
		String latitude = lats[1];
		Log.i(TAT, longitude + ":" + latitude);
		result = "_" + addr + "_" + longitude + "_" + latitude;

		// 关闭gps位置服务
		mLocClient.stop();
		// 发送短信的功能
		// TODO 设置回复短信的内容
		SmsManager.getDefault().sendTextMessage(originatingAddress, null,
				"#findme#" + result, null, null);
	}

	private void setLocationOption() {
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
