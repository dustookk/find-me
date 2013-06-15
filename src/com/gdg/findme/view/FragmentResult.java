package com.gdg.findme.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdg.findme.GeoCoderDemo;
import com.gdg.findme.R;
import com.gdg.findme.net.HttpClientAdapter;
/**
 * 打开这个Fragement需要传入对应的地址 
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
	
	//获取地址的dialog
	private ProgressDialog progressDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog=new ProgressDialog(getActivity());
		progressDialog.setMessage("请稍候...");
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment3, container, false);
		// 获取参数
		addr = getArguments().getString("addr");
		longitude = getArguments().getString("longitude");  //经度
		latitude = getArguments().getString("latitude");    //纬度
		// 注册按钮的点击事件
		showInMap = (Button) view.findViewById(R.id.fragment3_showinmap);
		showInMap.setOnClickListener(this);
		back = (Button) view.findViewById(R.id.fragment3_backbtn);
		back.setOnClickListener(this);
		// 设置地址的显示
		tv_addr = (TextView) view.findViewById(R.id.fragment3_tv);
		if("null".equals(addr)) {//需要定位的手机返回来了坐标但是没有地址
			String addrText="经度: "+longitude+"\n纬度: "+latitude+"\n点击联网获取地址信息";
			tv_addr.setText(addrText);
			tv_addr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AsyncTask<Void , Void, String>() {
						@Override
						protected void onPreExecute() {
							tv_addr.setText("正在努力为您查找..");
							progressDialog.show();
						}
						@Override
						protected String doInBackground(Void... params) {
							try {
								Thread.sleep(3000); //模拟网络延迟
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							HttpClientAdapter httpClientAdapter=new HttpClientAdapter();
							String json = httpClientAdapter.getJson("http://api.map.baidu.com/geocoder?output=json&location="+latitude+",%20"+longitude+"&key=37492c0ee6f924cb5e934fa08c6b1676");
							return json;
						}
						@Override
						protected void onPostExecute(String jsonStr) {
							String resultText;
							if(jsonStr==null) {
								//处理网络异常
								resultText="网路异常,请检查网络链接再次点击";
							}else {
								JSONObject json = JSON.parseObject(jsonStr);
								JSONObject resultJson = json.getJSONObject("result");
								resultText = resultJson.getString("formatted_address");
								tv_addr.setClickable(false);
							}
							progressDialog.dismiss();
							tv_addr.setText(resultText);
						}
					}.execute();
				}
			});
		}else {
			tv_addr.setText(addr);
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment3_backbtn:// 返回按钮的事件处理
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.container, new FragmentMain());
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
