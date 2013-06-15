package com.gdg.findme.observer;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.gdg.findme.service.SendLocationService;

public class SmsObserver extends ContentObserver{
	private Context context;
	public static final String TAG = "com.gdg.findme";
	
	
	public SmsObserver(Handler handler,Context context) {
		super(handler);
		this.context=context;
	}
	@Override
	public void onChange(boolean selfChange) {
		Uri uri = Uri.parse("content://sms/");
		Cursor c = context.getContentResolver().query(uri, null, null, null,"date desc");
		if(c.moveToNext()) {
			 String messageBody = c.getString(c.getColumnIndex("body"));
			 String originatingAddress = c.getString(c.getColumnIndex("address"));
			 long date=c.getLong(c.getColumnIndex("date"));
			 long currentTimeMillis = System.currentTimeMillis();
			 long abs = Math.abs(currentTimeMillis-date);
			 if (abs<5000 && "#findme_location#".equals(messageBody)) {
					Intent sendIntent=new Intent(context,SendLocationService.class);
					sendIntent.putExtra("originatingAddress", originatingAddress);
					context.startService(sendIntent);
				}
		}
		c.close();
	}
}