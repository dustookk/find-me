package com.gdg.findme.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdg.findme.R;

/**
 * splash页面后的住页面，主要有发送短信等功能。
 * 
 * @author mioo
 * 
 */
public class Fragment1 extends Fragment implements OnClickListener {
	private EditText et_number;
	private Button btn;
	private ImageView iv_contact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment1, container, false);
		et_number = (EditText) view.findViewById(R.id.fragment1_number);
		iv_contact = (ImageView) view.findViewById(R.id.fragment1_contact);
		btn = (Button) view.findViewById(R.id.fragment1_bt);
		iv_contact.setOnClickListener(this);
		btn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment1_bt:
			String number = et_number.getText().toString().trim();
			String messageBody = "#location#";
			SmsManager.getDefault().sendTextMessage(number, null, messageBody,
					null, null);
			break;
		case R.id.fragment1_contact:// 点击了获取联系人的按钮，打开了系统的联系人页面
			Intent intent = new Intent();
			intent.setClassName("com.gdg.findme", "ContactsActivity");
			startActivityForResult(intent, 0);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			et_number.setText(data.getStringExtra("phone"));
		}
	}
}
