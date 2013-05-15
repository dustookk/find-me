package com.gdg.findme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends Activity {
	ListView lvContacts;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		//找到listview
		lvContacts=(ListView) findViewById(R.id.lv_contacts);
		//构造一个Projection , 制定要查询的内容
		String[] mProjection = new String[]
		    {
			        Phone.DISPLAY_NAME, //"display_name"
			        Phone.NUMBER       //"data1"
		    };
		 Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null ,null,null,null);
		 
		 int[] mWordListItems=new int[]{R.id.tv_name,R.id.tv_phone};
		 SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(
				    this,               // The application's Context object
				    R.layout.activity_contacts_item,       // A layout in XML for one row in the ListView
				    cursor,                           // The result from the query
				    mProjection,                      		// A string array of column names in the cursor
				    mWordListItems,                        // An integer array of view IDs in the row layout
				    0);                                    // Flags (usually none are needed)
				// Sets the adapter for the ListView
		 lvContacts.setAdapter(mCursorAdapter);
		 lvContacts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tvName= (TextView) view.findViewById(R.id.tv_name);
				TextView tvPhone= (TextView) view.findViewById(R.id.tv_phone);
				String name = tvName.getText().toString().trim();
				String phone = tvPhone.getText().toString().trim();
				Intent data = new Intent();
				data.putExtra("name", name);
				data.putExtra("phone", phone);
				ContactsActivity.this.setResult(100, data);
				ContactsActivity.this.finish();
			}
		});
	}
}



//after everything you've been through , you get to look into each other's eyes and say I love you , that's beautiful;
