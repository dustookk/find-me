package com.gdg.findme.net;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class HttpClientAdapter {

	private HttpClient httpClient;

	public HttpClientAdapter() {
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3 * 1000);
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3 * 1000);
	}

	public String getJson(String uri) {
		final HttpGet httpGet = new HttpGet(uri);
		String jsResult = null;
		try {
			jsResult = httpClient.execute(httpGet, new BasicResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsResult;
	}
}
