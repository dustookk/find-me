package com.gdg.findme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;

import com.baidu.mapapi.search.MKSearch;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class GeoCoderDemo extends Activity {
	Button mBtnReverseGeoCode = null;	// 将坐标反编码为地址
	Button mBtnGeoCode = null;	// 将地址编码为坐标
	
	MapView mMapView = null;	// 地图View
	MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	BMapManager mBMapMan = null;
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mBMapMan=new BMapManager(getApplication());
		mBMapMan.init("F5431BE73CD4D1561756E5A538065BB82AC77F32", null); 
		
		
        setContentView(R.layout.geocoder);        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);//允许点击
        mMapView.getController().setZoom(12);//设置地图的缩放级别。 这个值的取值范围是[3,19]
        mMapView.displayZoomControls(true);//显示缩放控件，可以选择是否请求焦点选中以便通过按键访问。
        mMapView.setDoubleClickZooming(true);//设置mapview是否支持双击放大效果
        Intent intent = getIntent();
        String _longitude=intent.getStringExtra("longitude");
        String _latitude=intent.getStringExtra("latitude");
		int longitude = (int) (1000000 * Double.parseDouble("40.048778")); 
		int latitude = (int) (1000000 * Double.parseDouble("116.308036"));  
		GeoPoint ptCenter = new GeoPoint(longitude, latitude);
		Drawable marker = getResources().getDrawable(R.drawable.icon_marka);  //得到需要标在地图上的资源
		mMapView.getOverlays().add(new OverItemT(marker, GeoCoderDemo.this, ptCenter, null));
        mMapView.getController().setCenter(ptCenter);
        mMapView.refresh();
	}
	
	@Override
	protected void onDestroy(){
	        mMapView.destroy();
	        if(mBMapMan!=null){
	                mBMapMan.destroy();
	                mBMapMan=null;
	        }
	        super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        if(mBMapMan!=null){
	                mBMapMan.stop();
	        }
	        super.onPause();
	}
	@Override
	protected void onResume(){
	        mMapView.onResume();
	        if(mBMapMan!=null){
	                mBMapMan.start();
	        }
	        super.onResume();
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    
	class OverItemT extends ItemizedOverlay {
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
			super(marker);
			mGeoList.add(new OverlayItem(pt, title, null));
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

	}
    
}
