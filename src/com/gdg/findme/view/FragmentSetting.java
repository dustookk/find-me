package com.gdg.findme.view;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdg.findme.AboutActivity;
import com.gdg.findme.R;
import com.gdg.findme.TrustActivity;
import com.gdg.findme.service.LocationService;

public class FragmentSetting extends Fragment implements OnClickListener {
	private LinearLayout ll_start_service;
	private TextView tv_start_service;
	private TextView tv_set_trust;
	private TextView tv_system_about;
	private ImageView iv_start_service;
	private ImageView iv_logo;
	private int logoClickCount;
	
	private boolean serviceIsStarted;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View settingView = inflater.inflate(R.layout.fragment_setting, container, false); // 最后一个参数必须是false;
		ll_start_service = (LinearLayout) settingView
				.findViewById(R.id.ll_start_service);
		tv_start_service = (TextView) settingView
				.findViewById(R.id.tv_start_service);
		tv_set_trust = (TextView) settingView.findViewById(R.id.tv_set_trust);
		tv_system_about = (TextView) settingView
				.findViewById(R.id.tv_system_about);
		iv_start_service = (ImageView) settingView
				.findViewById(R.id.iv_start_service);
		iv_logo = (ImageView) settingView
				.findViewById(R.id.iv_logo);
		ll_start_service.setOnClickListener(this);
		tv_set_trust.setOnClickListener(this);
		tv_system_about.setOnClickListener(this);
		iv_logo.setOnClickListener(this);
		return settingView;
	}

	@Override
	public void onStart() {
		serviceIsStarted = checkIfServiceIsStarted();// 看服务是否已经开启
		if (serviceIsStarted) {
			tv_start_service.setText(getResources().getString(
					R.string.service_is_started));
			
			iv_start_service
					.setImageResource(R.drawable.general__shared__switch_selected);
		} else {
			tv_start_service.setText(getResources().getString(
					R.string.service_is_stoped));
			iv_start_service
					.setImageResource(R.drawable.general__shared__switch_normal);
		}
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		if (v == ll_start_service) {
			SharedPreferences sp=getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			Intent locationService = new Intent(getActivity(),
					LocationService.class);
			if (serviceIsStarted) {// 在此之前服务已经开启,现在要设置成关闭状态
				tv_start_service.setText(getResources().getString(
						R.string.service_is_stoped));
				iv_start_service
						.setImageResource(R.drawable.general__shared__switch_normal);
				getActivity().stopService(locationService);
				editor.putBoolean("startService", false);
			} else {
				tv_start_service.setText(getResources().getString(
						R.string.service_is_started));
				iv_start_service
						.setImageResource(R.drawable.general__shared__switch_selected);
				getActivity().startService(locationService);
				editor.putBoolean("startService", true);
			}
			editor.commit();
			serviceIsStarted = !serviceIsStarted;
		} else if (v == tv_set_trust) {
			Intent trustIntent = new Intent(getActivity(), TrustActivity.class);
			startActivity(trustIntent);
		}else if (v == tv_system_about) { //关于
			Intent aboutIntent=new Intent(getActivity(),AboutActivity.class);
			startActivity(aboutIntent);
		}else if(v==iv_logo) { //点击五次logo 弹出彩蛋
			if(logoClickCount==0) { //必须在3秒内点完五次
				logoClickCount++;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						logoClickCount=0;
					}
				}, 3000);
			}else if(logoClickCount==4) {
				Toast.makeText(getActivity(), "被发现啦..", Toast.LENGTH_SHORT).show();
				Vibrator vibrator= (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(300);
				TranslateAnimation translateAnimation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 1f,
						Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
				translateAnimation.setRepeatCount(5);
				translateAnimation.setDuration(70);
				translateAnimation.setRepeatMode(Animation.REVERSE);
				iv_logo.setAnimation(translateAnimation);
				iv_logo.startAnimation(translateAnimation);
				logoClickCount=0;
			}else{
				logoClickCount++;
			}
		}
	}

	private boolean checkIfServiceIsStarted() {
		ActivityManager activityManager = (ActivityManager) getActivity()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		for (RunningServiceInfo runningServiceInfo : runningServiceList) {
			ComponentName service = runningServiceInfo.service;
			String className = service.getClassName();
			if ("com.gdg.findme.service.LocationService".equals(className)) {
				return true;
			}
		}
		return false;
	}

}