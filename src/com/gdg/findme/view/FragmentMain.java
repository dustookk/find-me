package com.gdg.findme.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
public class FragmentMain extends Fragment implements OnClickListener {
	protected static final int COUNTDOWNTIME = 45;
	protected int count = COUNTDOWNTIME;
	protected static final int MESSAGE_RECEIVED = 0;
	protected static final int NOTHING_RECEIVED = 1;
	private EditText et_number;
	private String number;
	private String[] messageParts; // 如果收到了回复的短信则不为null
	private LocationHandleReceiver locationHandleReceiver;
	private LocationHandleObserver locationHandleObserver;
	private Button btn;
	private ImageView iv_contact;
	// 倒计时dialog;
	public Dialog dialog;
	// 什么也没有收到,确认的dialog.
	private AlertDialog alertDialog;
	private CountDownTimer countDownTimer; // 倒计时扇形
	private HomeActivity homeActivity;
	private SharedPreferences sp;
	private boolean isDismissed; //用户最小化了界面
	private boolean ifNeedChangeUI; //onstart以后是否需要将界面换成homeActivity.fragment;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == MESSAGE_RECEIVED) { // 收到短信时,Dialog消失,并跳转到Fragment3
				Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(300);
				countDownTimer.cancel();
				dialog.dismiss();
				Bundle bundle = new Bundle();
				bundle.putString("addr", messageParts[1]);
				bundle.putString("latitude", messageParts[2]);
				bundle.putString("longitude", messageParts[3]);
				homeActivity.fragment3.setArguments(bundle);
				if(isDismissed) {
					ifNeedChangeUI=true;
				}else {
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.container, homeActivity.fragment3);
					ft.commit();
				}
			} else if (msg.what == NOTHING_RECEIVED) { // 时间到了,什么也没有收到,Dialog消失,不跳转
				dialog.dismiss();
				alertDialog.show();
			}
		}
	};
	
	
	

	/**
	 * 做初始化工作
	 * 
	 * @param context
	 */
	public void init(Context context) {
		// 初始化倒计时dialog
		dialog = new Dialog(context, R.style.myDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.countdown);
		final TextView tv_countdown = (TextView) dialog
				.findViewById(R.id.tv_countdown);
		final CountDownIndicator countdown_indicator = (CountDownIndicator) dialog
				.findViewById(R.id.countdown_indicator);
		countDownTimer = new CountDownTimer(COUNTDOWNTIME * 1000, 1000) {
			public void onTick(long millisUntilFinished) {
				double phase = count / (double) COUNTDOWNTIME;
				countdown_indicator.setPhase(phase);
				count--;
				tv_countdown.setText("将在 " + count + " 秒内找到他/她..");
			}
			public void onFinish() {
				countdown_indicator.setPhase(0d);
				handler.sendEmptyMessage(NOTHING_RECEIVED);
			}
		};

		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				countDownTimer.cancel();
				count = COUNTDOWNTIME;
				unregister();
			}
		});

		// 初始化alertDialog
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		builer.setTitle("定位失败T_T");
		builer.setMessage("对方手机没有开启\"允许别人找到我\"功能");
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		alertDialog = builer.create();
	}

	@Override
	public void onStop() {
		isDismissed=true;
		super.onStop();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(ifNeedChangeUI) {
			FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.container, homeActivity.fragment3);
			ft.commit();
			ifNeedChangeUI=false;
		}
		isDismissed=false;
	}
	
	
	@Override
	public void onDestroy() {
		unregister();
		super.onDestroy();
	}

	// 反注册内容观察者 和 广播接收者
	private void unregister() {
		if (locationHandleReceiver != null) {
			getActivity().unregisterReceiver(locationHandleReceiver);
			locationHandleReceiver = null;
		}
		if (locationHandleObserver != null) {
			getActivity().getContentResolver().unregisterContentObserver(
					locationHandleObserver);
			locationHandleObserver = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		et_number = (EditText) view.findViewById(R.id.fragment1_number);
		iv_contact = (ImageView) view.findViewById(R.id.fragment1_contact);
		btn = (Button) view.findViewById(R.id.fragment1_bt);
		iv_contact.setOnClickListener(this);
		btn.setOnClickListener(this);
		homeActivity = (HomeActivity) getActivity();
		getLastLocateNumber(et_number);
		return view;
	}

	private void getLastLocateNumber(TextView tv) {
		sp = homeActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		String number = sp.getString("get_lastLocate_number",null);
		if(number!=null) {
			tv.setText(number);
		}
	}
	private void setLastLocateNumber(String number) {
		Editor editor = sp.edit();
		editor.putString("get_lastLocate_number", number);
		editor.commit();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment1_bt:// 点击了确定按钮，发送短信，显示dialog，注册监听着
			number = et_number.getText().toString().trim();
			if (number.matches("1[3,5,8]\\d{9}")) {
				setLastLocateNumber(number);
				SmsManager.getDefault().sendTextMessage(number, null,"#findme_location#", null, null);
				dialog.show();
				countDownTimer.start();
				IntentFilter intent = new IntentFilter();
				intent.setPriority(Integer.MAX_VALUE);
				intent.addAction("android.provider.Telephony.SMS_RECEIVED");
				KillSystemSmsAppTool.killSystemSmsApp(getActivity());
				locationHandleReceiver = new LocationHandleReceiver();
				locationHandleObserver = new LocationHandleObserver(handler,
						getActivity());
				getActivity().registerReceiver(locationHandleReceiver, intent);
				Uri uri = Uri.parse("content://sms/");
				getActivity().getContentResolver().registerContentObserver(uri,
						true, locationHandleObserver);
			} else {
				TranslateAnimation translateAnimation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, -0.1f, Animation.RELATIVE_TO_SELF, 0.1f,
						Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setRepeatCount(4);
				translateAnimation.setDuration(70);
				translateAnimation.setRepeatMode(Animation.REVERSE);
				et_number.setAnimation(translateAnimation);
				et_number.startAnimation(translateAnimation);
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
			String phoneRaw = data.getStringExtra("phone");
			String phone = phoneRaw.replace("+86", "").replace("-", "");
			et_number.setText(phone);
		}
	}

	
	private class LocationHandleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objs[0]);
			String originatingAddress = smsMessage.getOriginatingAddress();
			String messageBody = smsMessage.getMessageBody();
			if (originatingAddress.contains(number)
					&& messageBody.startsWith("#findme#")) {
				messageParts = messageBody.split("_");
				if (messageParts.length == 4) {
					handler.sendEmptyMessage(MESSAGE_RECEIVED);
				} 
				abortBroadcast();
			}
		}
	}

	private class LocationHandleObserver extends ContentObserver {
		private Context context;

		public LocationHandleObserver(Handler handler, Context context) {
			super(handler);
			this.context = context;
		}

		@Override
		public void onChange(boolean selfChange) {
			Uri uri = Uri.parse("content://sms/");
			Cursor c = context.getContentResolver().query(uri, null, null,
					null, "date desc");
			if (c.moveToNext()) {
				String messageBody = c.getString(c.getColumnIndex("body"));
				String originatingAddress = c.getString(c
						.getColumnIndex("address"));
				long date = c.getLong(c.getColumnIndex("date"));
				long currentTimeMillis = System.currentTimeMillis();
				long abs = Math.abs(currentTimeMillis - date);
				if (abs < 5000 && originatingAddress.contains(number)
						&& messageBody.startsWith("#findme#")) {
					messageParts = messageBody.split("_");
					if (messageParts.length == 4) {
						handler.sendEmptyMessage(MESSAGE_RECEIVED);
					} 
				}

			}
			c.close();
		}
	}
}
