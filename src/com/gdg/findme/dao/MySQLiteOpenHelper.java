package com.gdg.findme.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "com.gyh.databasetest";

	public MySQLiteOpenHelper(Context context) {
		super(context,"trust.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "oncreate 方法被调用了...");
		db.execSQL("create table trusts(_id integer primary key autoincrement , name varchar(20) ,number varchar(20) UNIQUE)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
