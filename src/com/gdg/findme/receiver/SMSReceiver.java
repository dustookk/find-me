package com.gdg.findme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gdg.findme.service.SendLocationService;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAT = "com.gdg.findme";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAT, "a new message received");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
		String originatingAddress = smsMessage.getOriginatingAddress();
		String messageBody = smsMessage.getMessageBody();
		Log.i(TAT, "messageBody "+messageBody);
		if ("#findme_location#".equals(messageBody)) {
			Intent sendIntent=new Intent(context,SendLocationService.class);
			sendIntent.putExtra("originatingAddress", originatingAddress);
			context.startService(sendIntent);
			abortBroadcast();
		}
	}
	
		
	
}
