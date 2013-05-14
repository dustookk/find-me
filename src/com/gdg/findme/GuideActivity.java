package com.gdg.findme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdg.findme.utils.Logger;
import com.gdg.findme.view.GuideFragment1;
import com.gdg.findme.view.GuideFragment2;
import com.gdg.findme.view.GuideFragment3;

/**
 * Setting Guide
 * 
 * @author gaoyihang
 * 
 */
public class GuideActivity extends FragmentActivity implements OnClickListener {
	private static final String TAG = "com.gdg.findme";
	private FragmentManager fragmentManager;
	private GuideFragment1 guideFragment1;
	private GuideFragment2 guideFragment2;
	private GuideFragment3 guideFragment3;
	private Fragment[] fragments;
	
	
	private ImageView iv_guide_top;
	private Button bt_next;

	private int index; // current GuideFragment (0,1,2)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		findView();
		init();
	}

	private void findView() {
		iv_guide_top = (ImageView) findViewById(R.id.iv_guide_top);
		bt_next = (Button) findViewById(R.id.bt_next);
	}

	private void init() {
		fragmentManager = getSupportFragmentManager();
		guideFragment1 = new GuideFragment1();
		guideFragment2 = new GuideFragment2();
		guideFragment3 = new GuideFragment3();
		fragments=new Fragment[]{guideFragment1,guideFragment2,guideFragment3};
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.replace(R.id.ll_fragment, guideFragment1);
		index = 0;
		beginTransaction.commit();
		bt_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		// FIXME gyh 设置向导界面动画每次都闪一下
		// beginTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		switch (index) {
		case 0: 
			EditText et_keyword = (EditText) guideFragment1.getView().findViewById(R.id.et_keyword);
			String keyword = et_keyword.getText().toString().trim();
			Logger.i(TAG, "keyword "+keyword);
			index++;
			beginTransaction.replace(R.id.ll_fragment, fragments[index]);
			beginTransaction.addToBackStack(null);
			break;
		case 1: 
			index++;
			beginTransaction.replace(R.id.ll_fragment, fragments[index]);
			beginTransaction.addToBackStack(null);
			break;
		case 2: 
			break;
		}
		beginTransaction.commit();
		setLayout();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && index > 0) {
			index--;
			setLayout();
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * reset the layout of the Image at the top and the button at the bottom
	 */
	private void setLayout() {
		switch (index) {
		case 0: 
			iv_guide_top.setImageResource(R.drawable.kn_protect_guide_first);
			bt_next.setText("下一步");
			break;
		case 1: 
			iv_guide_top.setImageResource(R.drawable.kn_protect_guide_second);
			bt_next.setText("下一步");
			break;
		case 2: 
			iv_guide_top.setImageResource(R.drawable.kn_protect_guide_third);
			bt_next.setText("完成");
			break;
		}

	}

}