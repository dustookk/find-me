package com.gdg.findme.service;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gdg.findme.baidu.Location;
import com.gdg.findme.dao.TrustsDao;

/**
 * 把自己的坐标回复给请求手机
 * 
 * @author gaoyihang
 * 
 */
public class SendLocationService extends IntentService {
	private LocationClient mLocClient;
	private TrustsDao trustsDao;

	public SendLocationService() {
		super("name");
	}
	@Override
	public void onCreate() {
		trustsDao=new TrustsDao(this);
		super.onCreate();
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		String originatingAddress = intent.getStringExtra("originatingAddress");
		if (!trustsDao.checkDBEmpty() && !trustsDao.checkExist(originatingAddress)) {
			return;
		}
		mLocClient = ((Location)getApplication()).mLocationClient;
		setLocationOption();
		mLocClient.start(); 
		do{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while (Location.locationResult == "");
		
		String result = "#findme#_" +Location.locationResult;
		Location.locationResult = "";//还原
		// 关闭gps位置服务
		mLocClient.stop();
		// 发送短信的功能
		SmsManager.getDefault().sendTextMessage(originatingAddress, null,
				result, null, null);
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