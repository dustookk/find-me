package com.gdg.findme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.gdg.findme.service.SendLocationService;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
		String originatingAddress = smsMessage.getOriginatingAddress();
		String messageBody = smsMessage.getMessageBody();
		if ("#findme_location#".equals(messageBody)) {
			Intent sendIntent=new Intent(context,SendLocationService.class);
			sendIntent.putExtra("originatingAddress", originatingAddress);
			context.startService(sendIntent);
			abortBroadcast();
		}
	}
	
		
	
}