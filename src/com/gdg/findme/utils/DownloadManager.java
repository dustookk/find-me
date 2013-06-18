package com.gdg.findme.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

public class DownloadManager {
	public static boolean flag;
	public static File download(URL url, String savePath, ProgressDialog pd) {
		flag=true;
		File file = null;
		HttpURLConnection conn;

		FileOutputStream fos=null;
		InputStream is=null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if (code == 200) {
				file = new File(savePath);
				String parent = file.getParent();
				File dir = new File(parent);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				int contentLength = conn.getContentLength();
				pd.setMax(contentLength);
				fos = new FileOutputStream(file);
				is = conn.getInputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				int total = 0;
				
				while (flag && (len = is.read(buf)) != -1) {
					fos.write(buf, 0, len);
					total += len;
					pd.setProgress(total);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	public static void cancel() {
		flag=false;
	}
	
}
