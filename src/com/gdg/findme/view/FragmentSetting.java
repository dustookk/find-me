package com.gdg.findme.view;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdg.findme.R;
import com.gdg.findme.TrustActivity;
import com.gdg.findme.service.LocationService;

public class FragmentSetting extends Fragment implements OnClickListener {
	private LinearLayout ll_start_service;
	private TextView tv_start_service;
	private TextView tv_set_trust;
	private ImageView  iv_start_service;
	private boolean serviceIsStarted;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View settingView = inflater.inflate(R.layout.setting, container,false);  //最后一个参数必须是false;
		ll_start_service=(LinearLayout) settingView.findViewById(R.id.ll_start_service);
		tv_start_service=(TextView) settingView.findViewById(R.id.tv_start_service);
		tv_set_trust=(TextView) settingView.findViewById(R.id.tv_set_trust);
		iv_start_service=(ImageView) settingView.findViewById(R.id.iv_start_service);
		ll_start_service.setOnClickListener(this);
		tv_set_trust.setOnClickListener(this);
		return settingView;
	}
	
	@Override
	public void onStart() {
		serviceIsStarted=checkIfServiceIsStarted();//看服务是否已经开启
		if(serviceIsStarted) {
			tv_start_service.setText(getResources().getString(R.string.service_is_started));
			iv_start_service.setImageResource(R.drawable.general__shared__switch_selected);
		}else {
			tv_start_service.setText(getResources().getString(R.string.service_is_stoped));
			iv_start_service.setImageResource(R.drawable.general__shared__switch_normal);
		}
		super.onStart();
	}
	@Override
	public void onClick(View v) {
		if(v==ll_start_service) {
			Intent locationService=new Intent(getActivity(),LocationService.class);
			if(serviceIsStarted) {//在此之前服务已经开启
				tv_start_service.setText(getResources().getString(R.string.service_is_stoped));
				iv_start_service.setImageResource(R.drawable.general__shared__switch_normal);
				getActivity().stopService(locationService);
			}else {
				tv_start_service.setText(getResources().getString(R.string.service_is_started));
				iv_start_service.setImageResource(R.drawable.general__shared__switch_selected);
				getActivity().startService(locationService);
			}
			serviceIsStarted=!serviceIsStarted;
		}else if(v==tv_set_trust){
			Intent trustIntent=new Intent(getActivity(),TrustActivity.class);
			startActivity(trustIntent);
		}
	}
	
	private boolean checkIfServiceIsStarted() {
		ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceList = activityManager.getRunningServices(Integer.MAX_VALUE);
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
