package com.gdg.findme.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdg.findme.ContactsActivity;
import com.gdg.findme.R;
import com.gdg.findme.utils.KillSystemSmsAppTool;

/**
 * splash页面后的住页面，主要有发送短信等功能。
 * 
 * @author mioo
 * 
 */
public class Fragment1 extends Fragment implements OnClickListener {
	private static int RECEIVE_MSG_OK = 0;
	private EditText et_number;
	private LocationHandleReceiver locationHandleReceiver;
	private Button btn;
	private ImageView iv_contact;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog_btn.setText("查看位置");
				onClick(dialog_btn);
				break;
			}
			super.handleMessage(msg);
		}

	};
	private Dialog dialog;
	private Button dialog_btn;
	private String addr;
	private String longitude;
	private String latitude;

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
		case R.id.fragment1_bt:// 点击了确定按钮，发送了短信，显示dialog，注册监听着
			if (TextUtils.isEmpty(et_number.getText().toString())) {
				// TODO 实际的时候需要打开此项
				// SmsManager.getDefault().sendTextMessage(
				// et_number.getText().toString().trim(), null,
				// "#location#", null, null);
				dialog = new Dialog(getActivity(), R.style.myDialog);
				dialog.setContentView(R.layout.wait_message);
				dialog_btn = (Button) dialog.findViewById(R.id.dialog_btn);
				dialog_btn.setText("请稍后。。。。");
				dialog_btn.setClickable(false);
				dialog_btn.setOnClickListener(this);
				getActivity();
				dialog.show();
				IntentFilter intent = new IntentFilter();
				intent.setPriority(Integer.MAX_VALUE);
				intent.addAction("android.provider.Telephony.SMS_RECEIVED");
				locationHandleReceiver = new LocationHandleReceiver();
				KillSystemSmsAppTool.killSystemSmsApp(getActivity());
				getActivity().registerReceiver(locationHandleReceiver, intent);
			}
			break;
		case R.id.fragment1_contact:// 点击了获取联系人的按钮，打开了系统的联系人页面
			Intent intent = new Intent();
			intent.setClass(getActivity(), ContactsActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.dialog_btn:
			dialog.dismiss();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment fragment3 = new Fragment3();
			Bundle bundle = new Bundle();
			bundle.putString("addr", addr);
			bundle.putString("longitude", longitude);
			bundle.putString("latitude", latitude);
			fragment3.setArguments(bundle);
			ft.replace(R.id.container, fragment3);
			ft.commit();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			et_number.setText(data.getStringExtra("phone"));
		}
	}

	public class LocationHandleReceiver extends BroadcastReceiver {

		private static final String TAT = "LocationHandleReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAT, "LocationHandleReceiver received a new message");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
			String originatingAddress = smsMessage.getOriginatingAddress();
			String messageBody = smsMessage.getMessageBody();
			Log.i(TAT, "messageBody " + messageBody);
			if (messageBody.startsWith("#findme#")) {
				// FIXME gyh 判断originatingAddress在不在白名单里;

				String[] messageParts = messageBody.split("_");
				if (messageParts.length == 4) {
					addr = messageParts[1];
					longitude = messageParts[2];
					latitude = messageParts[3];
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				} else {
					// TODO gyh 处理目标手机回复的短信不正常
				}
				abortBroadcast();
			}
		}

	}

}
