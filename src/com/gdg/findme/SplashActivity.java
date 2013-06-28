package com.gdg.findme;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdg.findme.net.HttpClientAdapter;
import com.gdg.findme.utils.AppVersionTool;
import com.gdg.findme.utils.CheckNetworkConnection;
import com.gdg.findme.utils.DownloadManager;
import com.gdg.findme.vo.UpdateInfo;

/**
 * Splash界面 , 控制版本升级
 * 
 * @author gyh
 * 
 */
public class SplashActivity extends Activity {

	protected static final String UPDATE_JSON = "https://raw.github.com/dustookk/find-me/master/web/update.json";
	private TextView tv_splish;
	private boolean isDownloading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splish = (TextView) findViewById(R.id.tv_splish);
		tv_splish.setShadowLayer(10F, 11F, 5F, Color.BLACK);
		new AsyncTask<Void, Void, UpdateInfo>() {
			@Override
			protected UpdateInfo doInBackground(Void... params) {
				if (!CheckNetworkConnection
						.isNetworkConnected(SplashActivity.this)) {
					return null; // 网络不可用直接返回
				}
				final HttpClientAdapter httpClientAdapter = new HttpClientAdapter();
				String jsonStr = httpClientAdapter.getJson(UPDATE_JSON);
				if (jsonStr == null) {
					return null; // 连接超时直接返回
				}
				JSONObject jsonObj = JSON.parseObject(jsonStr);
				double version = jsonObj.getDouble("version");
				if (version > AppVersionTool.getAppVersion(SplashActivity.this)) {
					// 发现新版本
					String changeLog = jsonObj.getString("change_log");
					String link = jsonObj.getString("link");
					return new UpdateInfo(version, changeLog, link);
				} else {
					return new UpdateInfo(); // 没有新版本
				}
			}

			@Override
			protected void onPostExecute(final UpdateInfo updateInfo) {
				if (updateInfo != null) {
					// 没有新版本
					if (updateInfo.getVersion() == null) {
						startFindme(false);
						return ;
					}
					//有新版本
					AlertDialog.Builder builer = new AlertDialog.Builder(
							SplashActivity.this);
					builer.setPositiveButton("立即下载", new MyOnClickListener(
							SplashActivity.this, updateInfo.getLink(),
							updateInfo.getVersion()));
					builer.setNegativeButton("以后再说",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									startFindme(false);
								}
							});
					AlertDialog alertDialog = builer.create();
					alertDialog.setTitle("发现新版本!");
					alertDialog.setMessage(updateInfo.getChangeLog());
					alertDialog.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							dialog.dismiss();
							startFindme(false);
						}
					});
					alertDialog.show();
				} else {
					startFindme(true);
				}
			}
		}.execute();
		startAnims();
	}

	/*
	 * splash 界面的动画
	 */
	private void startAnims() {
		// 创建一个AlphaAnimation对象（参数表示从完全完全透明到不透明）
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		// 设置动画执行的时间（单位：毫秒）
		alphaAnimation.setDuration(1500);
		alphaAnimation.setFillAfter(true);

		ImageView iv_splash_logo = (ImageView) findViewById(R.id.iv_splash_logo);
		// 创建一个RotateAnimation对象（从某个点移动到另一个点）
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -2f);
		// 设置动画执行的时间（单位：毫秒）
		translateAnimation.setDuration(1500);
		translateAnimation.setFillAfter(true);
		// 使用ImageView的startAnimation方法开始执行动画
		iv_splash_logo.startAnimation(translateAnimation);
		// 使用ImageView的startAnimation方法开始执行动画
		tv_splish.startAnimation(alphaAnimation);
	}

	/**
	 * 开始下载那个按钮的点击事件
	 */
	class MyOnClickListener implements DialogInterface.OnClickListener {
		private Context context;
		private String link;
		private ProgressDialog pd;
		private double version;

		public MyOnClickListener(Context context, String link, double version) {
			this.context = context;
			this.link = link;
			this.version = version;
			pd = new ProgressDialog(context);
			pd.setTitle("正在下载..");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			new AsyncTask<Void, Void, File>() {
				URL url = null;
				@Override
				protected void onPreExecute() {
					try {
						url = new URL(link);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					pd.show();
				};

				@Override
				protected File doInBackground(Void... params) {
					String savePath = null;
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						String sdPath = Environment
								.getExternalStorageDirectory().getPath();
						savePath = sdPath + "/findme/download/findme" + version
								+ ".apk";
					}
					isDownloading = true;
					return DownloadManager.download(url, savePath, pd);
				}

				@Override
				protected void onPostExecute(File file) {
					if (file == null) {
						Toast.makeText(context, "下载失败", Toast.LENGTH_LONG)
								.show();
					} else {
						installApk(file);
					}
					pd.cancel();
				}

				/**
				 * 打开软件安装界面
				 * 
				 * @param file
				 */
				private void installApk(File file) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(intent);
					SplashActivity.this.finish();
				}
			}.execute();
		}
	}

	/**
	 * 如果正在下载,点击后返回键取消下载并进入主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isDownloading) {
				DownloadManager.cancel();
				startFindme(false);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 进入主界面
	 * 
	 * @param delay
	 *            是否延迟打开,如果不需要更新则要延迟1500毫秒等待动画.
	 * 
	 */
	private void startFindme(boolean delay) {
		final Intent i = new Intent(SplashActivity.this, HomeActivity.class);
		if (delay) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					SplashActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					SplashActivity.this.finish();
				}
			}, 1500);

		} else {
			SplashActivity.this.startActivity(i);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			SplashActivity.this.finish();
		}
	}
}