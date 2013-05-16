package com.gdg.findme.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gdg.findme.GeoCoderDemo;
import com.gdg.findme.R;

public class Fragment3 extends Fragment implements OnClickListener {
	private TextView tv_addr;
	private Button showInMap;
	private Button back;
	private String addr;
	private String longitude;
	private String latitude;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment3, container, false);
		// 获取参数
		addr = getArguments().getString("addr");
		longitude = getArguments().getString("longitude");
		latitude = getArguments().getString("latitude");
		// 注册按钮的点击事件
		showInMap = (Button) view.findViewById(R.id.fragment3_showinmap);
		showInMap.setOnClickListener(this);
		back = (Button) view.findViewById(R.id.fragment3_backbtn);
		back.setOnClickListener(this);
		// 设置地址的显示
		tv_addr = (TextView) view.findViewById(R.id.fragment3_tv);
		tv_addr.setText(addr);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment3_backbtn:// 返回按钮的事件处理
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.container, new Fragment1());
			ft.commit();
			break;
		case R.id.fragment3_showinmap:// 打开地图的事件处理
			Intent i = new Intent();
			i.setClass(getActivity(), GeoCoderDemo.class);
			i.putExtra("longitude", longitude);
			i.putExtra("latitude", latitude);
			startActivity(i);
			break;
		}
	}

}
