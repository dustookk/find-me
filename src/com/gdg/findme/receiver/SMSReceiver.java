package com.gdg.findme.receiver;

import com.gdg.findme.service.SendLocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAT = "Locationreporter";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAT, "a new message received");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
		String originatingAddress = smsMessage.getOriginatingAddress();
		String messageBody = smsMessage.getMessageBody();
		Log.i(TAT, "messageBody "+messageBody);
		if ("#location#".equals(messageBody)) {
			Intent sendIntent=new Intent(context,SendLocationService.class);
			sendIntent.putExtra("originatingAddress", originatingAddress);
			context.startService(sendIntent);
			abortBroadcast();
		}
	}
	
		
	
}
