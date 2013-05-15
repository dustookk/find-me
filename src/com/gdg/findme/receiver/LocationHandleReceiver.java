package com.gdg.findme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gdg.findme.service.SendTextService;

/**
 * 等着接收目标手机发回的位置信息短信
 * @author gaoyihang
 *
 */
public class LocationHandleReceiver extends BroadcastReceiver {

	private static final String TAT = "LocationHandleReceiver";
	
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
			
			String[] messageParts = messageBody.split("");
			if(messageParts.length==3){
				String longtitude = messageParts[1];
				String latitude = messageParts[2];
				//TODO gyh 得到了目标手机回复的坐标 然后干啥 目测需要同志gzy更新主界面
				
				Intent sendTextService=new Intent(context,SendTextService.class);
				context.stopService(sendTextService);
				
			}else{
				//TODO gyh 处理目标手机回复的短信不正常
			}
			abortBroadcast();
		}
	}
	
		
	
}
