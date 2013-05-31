package com.gdg.findme.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gdg.findme.ContactsActivity;
import com.gdg.findme.HomeActivity;
import com.gdg.findme.R;
import com.gdg.findme.utils.KillSystemSmsAppTool;

/**
 * splash页面后的主页面，主要有发送短信等功能。
 * 
 * @author mioo gyh
 * 
 */
@SuppressLint("HandlerLeak")
public class Fragment_main extends Fragment implements OnClickListener {
	private final static String TAG = "com.gdg.findme";
	protected static final int MESSAGE_RECEIVED = 0;
	private EditText et_number;
	private String number;
	private String[] messageStrs = new String[3]; // 如果收到了回复的短信则不为null
	private LocationHandleReceiver locationHandleReceiver;
	private Button btn;
	private ImageView iv_contact;
	private Dialog dialog;
	private CountDownTimer countDownTimer; // 倒计时扇形
	private HomeActivity homeActivity;

	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what == MESSAGE_RECEIVED) { // 收到短信时,Dialog消失,并跳转到Fragment3
				countDownTimer.cancel();
				dialog.dismiss();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				Bundle bundle = new Bundle();
				bundle.putString("addr", messageStrs[0]);
				bundle.putString("longitude", messageStrs[1]);
				bundle.putString("latitude", messageStrs[2]);
				homeActivity.fragment3.setArguments(bundle);
				ft.replace(R.id.container, homeActivity.fragment3);
				ft.commit();
			}
		}
	};

	@Override
	public void onDestroy() {
		if (locationHandleReceiver != null) {
			getActivity().unregisterReceiver(locationHandleReceiver);
		}
		super.onDestroy();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment1, container,false);
		et_number = (EditText) view.findViewById(R.id.fragment1_number);
		iv_contact = (ImageView) view.findViewById(R.id.fragment1_contact);
		btn = (Button) view.findViewById(R.id.fragment1_bt);
		iv_contact.setOnClickListener(this);
		btn.setOnClickListener(this);
		homeActivity = (HomeActivity) getActivity();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment1_bt:// 点击了确定按钮，发送短信，显示dialog，注册监听着
			number = et_number.getText().toString().trim();
			if (number.matches("1[3,5,8]\\d{9}")) {
				SmsManager.getDefault().sendTextMessage(number, null,
						"#location#", null, null);
				dialog = new Dialog(getActivity(), R.style.myDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.countdown);
				final CountDownIndicator countdown_indicator = (CountDownIndicator) dialog
						.findViewById(R.id.countdown_indicator);
				countDownTimer = new CountDownTimer(30000, 1000) {
					int count = 30;
					public void onTick(long millisUntilFinished) {
						double phase = count / 30d;
						countdown_indicator.setPhase(phase);
						count--;
					}
					public void onFinish() {
						countdown_indicator.setPhase(0d);
					}
				};
				dialog.show();
				countDownTimer.start();
				//FIXME gyh 这个时候用户按了返回键
				IntentFilter intent = new IntentFilter();
				intent.setPriority(Integer.MAX_VALUE);
				intent.addAction("android.provider.Telephony.SMS_RECEIVED");
				locationHandleReceiver = new LocationHandleReceiver();
				KillSystemSmsAppTool.killSystemSmsApp(getActivity());
				getActivity().registerReceiver(locationHandleReceiver, intent);
			} else {
				Toast.makeText(getActivity(), "请输入一个正确的手机号", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.fragment1_contact:// 点击了获取联系人的按钮，打开了系统的联系人页面
			Intent intent = new Intent();
			intent.setClass(getActivity(), ContactsActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			et_number.setText(data.getStringExtra("phone"));
		}
	}

	public class LocationHandleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
			String originatingAddress = smsMessage.getOriginatingAddress();
			String messageBody = smsMessage.getMessageBody();
			if (originatingAddress.contains(number)
					&& messageBody.startsWith("#findme#")) {
				String[] messageParts = messageBody.split("_");
				if (messageParts.length == 4) {
					messageStrs[0] = messageParts[1];
					messageStrs[1] = messageParts[2];
					messageStrs[2] = messageParts[3];
					handler.sendEmptyMessage(MESSAGE_RECEIVED);
				} else {
					// TODO gyh 处理目标手机回复的短信不正常
				}
				abortBroadcast();
			}
		}
	}
}
