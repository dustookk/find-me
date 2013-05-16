package com.gdg.findme.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gdg.findme.service.SendTextService;
import com.gdg.findme.service.SendTextService.MyBinder;

/**
 * 等着接收目标手机发回的位置信息短信
 * @author gaoyihang
 *
 */
public class LocationHandleReceiver extends BroadcastReceiver {

	private static final String TAT = "LocationHandleReceiver";
	
	private MyServiceConnection myServiceConnection;
	private MyBinder myBinder;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAT, "LocationHandleReceiver received a new message");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
		String originatingAddress = smsMessage.getOriginatingAddress();
		String messageBody = smsMessage.getMessageBody();
		Log.i(TAT, "messageBody "+messageBody);
		//if ("#location#".equals(messageBody)) {
		if (messageBody.startsWith("#findme#")) {
			//TODO gyh 判断originatingAddress在不在白名单里;
			
			String[] messageParts = messageBody.split("_");
			if(messageParts.length==4){
				String longtitude = messageParts[1];
				String latitude = messageParts[2];
				String address = messageParts[3];
				//TODO gyh 得到了目标手机回复的坐标 然后干啥 目测需要同志gzy更新主界面
				Intent sendTextService=new Intent(context,SendTextService.class);
				
				myServiceConnection=new MyServiceConnection();
				context.bindService(sendTextService, myServiceConnection, 0);
				
				//FIXME gyh到底绑定没绑定
				
				//  #findme#_longtitude_latitude_address
				myBinder.excuteUpdateUI(longtitude,latitude,address);
				
				context.unbindService(myServiceConnection);
				
				context.stopService(sendTextService);
				
			}else{
				//TODO gyh 处理目标手机回复的短信不正常
			}
			abortBroadcast();
		}
	}
	
	
	
	private class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder=(MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
		
	
}
