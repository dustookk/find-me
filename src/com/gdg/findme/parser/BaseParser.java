package com.gdg.findme.parser;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * json 的基础解析器，大家自己写解析器的时候要继承这个类
 * @author dupengtao
 *
 * @param <T>
 */
public abstract class BaseParser<T> {
	
	 public abstract T parseJSON(String paramString) throws JSONException;
	 
	 /**
	  * 判断 响应数据是否有效
	  * @param res
	  * @throws JSONException 
	  */
	 public String checkResponse(String paramString) throws JSONException{
		if(paramString==null){ 
			return null;
		}else{
			JSONObject jsonObject = new JSONObject(paramString); 
			String result = jsonObject.getString("status");
			if(result!=null && !result.equals("ok")){
				return result;
			}else{
				return null;
			}
			
		}
	 }
}
