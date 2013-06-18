package com.gdg.findme.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdg.findme.GeoCoderDemo;
import com.gdg.findme.HomeActivity;
import com.gdg.findme.R;
import com.gdg.findme.net.HttpClientAdapter;
import com.gdg.findme.utils.CheckNetworkConnection;

/**
 * 打开这个Fragement需要传入对应的地址
 * 
 * @author gyh,gzp
 * 
 */
public class FragmentResult extends Fragment implements OnClickListener {
	protected static final String TAG = "com.gdg.findme";

	private TextView tv_addr;
	private Button showInMap;
	private Button back;
	private String addr;
	private String longitude;
	private String latitude;

	// 获取地址的dialog
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("请稍候...");
		progressDialog.setCanceledOnTouchOutside(false);
		View view = inflater.inflate(R.layout.fragment_result, container, false);
		// 获取参数
		addr = getArguments().getString("addr");
		longitude = getArguments().getString("longitude"); // 经度
		latitude = getArguments().getString("latitude"); // 纬度
		// 注册按钮的点击事件
		showInMap = (Button) view.findViewById(R.id.fragment3_showinmap);
		showInMap.setOnClickListener(this);
		back = (Button) view.findViewById(R.id.fragment3_backbtn);
		back.setOnClickListener(this);
		// 设置地址的显示
		tv_addr = (TextView) view.findViewById(R.id.fragment3_tv);
		if ("null".equals(addr)) {// 需要定位的手机返回来了坐标但是没有地址
			setClickableResult();
		} else {
			tv_addr.setText(addr);
		}
		return view;
	}

	private void setClickableResult() {
		String addrText = "经度: " + longitude + "\n纬度: " + latitude
				+ "\n点击联网获取地址信息";
		int index = addrText.indexOf("点击");
		SpannableString ss = new SpannableString(addrText);
		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
					new MyAsyncTask().execute();
				} else {
					Toast.makeText(getActivity(), "请先连接网络", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}, index, addrText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		ss.setSpan(new ForegroundColorSpan(0xffffffff), 0, addrText.length(), 0);
		tv_addr.setText(ss);
		tv_addr.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment3_backbtn:// 返回按钮的事件处理
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			HomeActivity activity = (HomeActivity) getActivity();
			ft.replace(R.id.container, activity.fragment_main);
			ft.commit();
			break;
		case R.id.fragment3_showinmap:// 打开地图的事件处理
			if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
				showInMap.setClickable(false); // 防止延迟后用户重复点击
				Intent i = new Intent();
				i.setClass(getActivity(), GeoCoderDemo.class);
				i.putExtra("longitude", longitude);
				i.putExtra("latitude", latitude);
				startActivity(i);
			} else {
				Toast.makeText(getActivity(), "请先连接网络", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
	}

	@Override
	public void onResume() {
		showInMap.setClickable(true);
		super.onResume();
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			tv_addr.setText("正在努力为您查找中...");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClientAdapter httpClientAdapter = new HttpClientAdapter();
			String json = httpClientAdapter
					.getJson("http://api.map.baidu.com/geocoder?output=json&location="
							+ latitude
							+ ",%20"
							+ longitude
							+ "&key=37492c0ee6f924cb5e934fa08c6b1676");
			return json;
		}

		@Override
		protected void onPostExecute(String jsonStr) {
			if (jsonStr == null) {
				// 处理网络异常
				setClickableResult();
				Toast.makeText(getActivity(), "连接异常,请稍后重试", Toast.LENGTH_LONG)
						.show();

			} else {
				JSONObject json = JSON.parseObject(jsonStr);
				JSONObject resultJson = json.getJSONObject("result");
				String resultText = resultJson.getString("formatted_address");
				tv_addr.setText(resultText);
			}
			progressDialog.dismiss();
		}
	}

}
