package com.gdg.findme.dao;

import java.util.ArrayList;
import java.util.List;

import com.gdg.findme.vo.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TrustsDao {
	
	private MySQLiteOpenHelper dBHelper;
	private SQLiteDatabase db;

	public TrustsDao(Context context) {
		dBHelper = new MySQLiteOpenHelper(context);
	}
	public long add(Contact contact){
		db = dBHelper.getWritableDatabase();
		ContentValues contentValues =new ContentValues();
		contentValues.put("name", contact.getName());
		contentValues.put("number", contact.getNumber());
		long insert;
		insert = db.insert("trusts", null, contentValues);
		contentValues=null;
		db.close();
		return insert;
	}
	public boolean remove(int id) {
		db = dBHelper.getWritableDatabase();
		int result = db.delete("trusts", "_id=?", new String[]{Integer.toString(id)});
		db.close();
		return result==1?true:false;
	}
	
	public List<Contact> getAll() {
		db = dBHelper.getReadableDatabase();
		Cursor cursor = db.query("trusts", null, null, null, null, null, null);
		int idIndex = cursor.getColumnIndex("_id");
		int nameIndex = cursor.getColumnIndex("name");
		int numberIndex = cursor.getColumnIndex("number");
		List<Contact> trustsList=new ArrayList<Contact>();
		while(cursor.moveToNext()) {
			int id = cursor.getInt(idIndex);
			String name = cursor.getString(nameIndex);
			String number = cursor.getString(numberIndex);
			Contact contact = new Contact(id, name, number);
			trustsList.add(contact);
		}
		cursor.close();
		db.close();
		return trustsList;
	}
	
	/**
	 * 判断数据库中是否有这个号码
	 * @param number
	 * @return
	 */
	public boolean checkExist(String number) {
		boolean flag=false;
		db = dBHelper.getReadableDatabase();
		Cursor cursor = db.query("trusts", null, "number=?", new String[]{number}, null, null, null);
		if (cursor.moveToNext()) {
			flag=true;
		}
		cursor.close();
		db.close();
		return flag;
	}
	
	/**
	 * 判断数据库是否为空
	 * @return
	 */
	public boolean checkDBEmpty() {
		boolean flag=false;
		db = dBHelper.getReadableDatabase();
		Cursor cursor = db.query("trusts", null, null, null, null, null, null);
		if (cursor.getCount()==0) {
			flag=true;
		}
		cursor.close();
		db.close();
		return flag;
	}
}
