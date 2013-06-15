package com.gdg.findme.net;



import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class HttpClientAdapter {

	private HttpClient httpClient;
	
	public HttpClientAdapter(){
		httpClient = new DefaultHttpClient();
		
	}
	
	public String getJson(String uri){
		
		HttpGet httpGet = new HttpGet(uri);
		try {
			return httpClient.execute(httpGet, new BasicResponseHandler());
		} catch (Exception e) {
			//联网异常
			return null;
		}
	
		
	}
	
}
