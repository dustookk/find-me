package com.gdg.findme.test;

import java.util.List;

import org.json.JSONException;

import com.gdg.findme.net.HttpClientAdapter;
import com.gdg.findme.parser.AddressListParser;
import com.gdg.findme.utils.Logger;
import com.gdg.findme.vo.Address;

import android.test.AndroidTestCase;

public class TestJson extends AndroidTestCase {

	private static final String TAG = "TestJson";

	public void getAddress() {
		
		String json = new HttpClientAdapter().getJson("http://maps.google.com/maps/api/geocode/json?latlng=40.0413193,116.2950664&language=zh-CN&sensor=false");
		Logger.i(TAG, json);
		try {
			List<Address> parseJSON = new AddressListParser().parseJSON(json);
			Logger.i(TAG, "dddddddd");
			for (Address address : parseJSON) {
				Logger.i(TAG, address.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
