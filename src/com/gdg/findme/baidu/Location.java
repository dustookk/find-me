package com.gdg.findme.baidu;

import android.app.Application;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class Location extends Application{

	public static String locationResult = "";

	public LocationClient mLocationClient = null;
	// public LocationClient locationClient = null;
	// public LocationClient LocationClient = null;
	//private String mData;
	public MyLocationListenner myListener = new MyLocationListenner();
	// public MyLocationListenner listener = new MyLocationListenner();
	// public MyLocationListenner locListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";

	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(this);
		// locationClient = new LocationClient( this );
		// LocationClient = new LocationClient( this );
		mLocationClient.registerLocationListener(myListener);
		// locationClient.registerLocationListener( listener );
		// LocationClient.registerLocationListener( locListener );
		// 位置提醒相关代码
		// mNotifyer = new NotifyLister();
		// mNotifyer.SetNotifyLocation(40.047883,116.312564,3000,"gps");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		// mLocationClient.registerNotify(mNotifyer);

		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}

	/**
	 * 显示字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			locationResult = str;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append(location.getAddrStr());
			sb.append("_");
			sb.append(location.getLatitude());
			sb.append("_");
			sb.append(location.getLongitude());
			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
		}
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude:");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude:");
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr:");
				sb.append(poiLocation.getAddrStr());
			}
			logMsg(sb.toString());
		}
	}
/*	*//**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 *//*
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("addr:");
			sb.append(location.getAddrStr());
			sb.append("\nlatitude:");
			sb.append(location.getLatitude());
			sb.append("\nlontitude:");
			sb.append(location.getLongitude());
			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude:");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude:");
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr:");
				sb.append(poiLocation.getAddrStr());
			}
			logMsg(sb.toString());
		}
	}
*/
	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			mVibrator01.vibrate(1000);
		}
	}
}