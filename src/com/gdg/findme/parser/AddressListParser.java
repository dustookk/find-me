package com.gdg.findme.parser;

import java.util.List;

import org.json.JSONException;

import com.alibaba.fastjson.JSONObject;
import com.gdg.findme.utils.Logger;
import com.gdg.findme.vo.Address;


public class AddressListParser extends BaseParser<List<Address>> {

	private static final String TAG = "AddressListParser";

	@Override
	public List<Address> parseJSON(String paramString) throws JSONException {
		String result = checkResponse(paramString);
		if (result == null) {
			Logger.e(TAG, "error");
			return null;
		}

		JSONObject jsonObject = JSONObject.parseObject(paramString);
		String address = jsonObject.getString("results");
		List<Address> addressList = JSONObject.parseArray(address, Address.class);
		return addressList;
	}

}
