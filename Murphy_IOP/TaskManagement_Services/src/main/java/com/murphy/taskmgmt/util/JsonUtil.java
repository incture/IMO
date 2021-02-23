package com.murphy.taskmgmt.util;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.murphy.taskmgmt.util.ServicesUtil;

public class JsonUtil {

	@SuppressWarnings("unchecked")
	public static String addObjects(String entity, Map<?, ?> jsonObjects) throws ParseException {
		
		JSONParser parser = new JSONParser();
		
		if(ServicesUtil.isEmpty(entity)) {
			entity = "{}";
		}
		JSONObject jsonObject = (JSONObject) parser.parse(entity);
		for(Map.Entry<?, ?> entry : jsonObjects.entrySet()) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}
		return jsonObject.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String addInObject(String entity, String object, Map<?, ?> jsonObjects) throws ParseException {
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		if(ServicesUtil.isEmpty(entity)) {
			return null;
		} else {
			jsonObject = (JSONObject) parser.parse(entity);
			JSONObject inObject = (JSONObject) jsonObject.get(object);
			for(Map.Entry<?, ?> entry : jsonObjects.entrySet()) {
				inObject.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put(object, inObject);
		}
		return jsonObject.toJSONString();
	}
	
	public static void main(String[] args) throws ParseException {
//		Map<String, String> credentials = new HashMap<String, String>();
//		credentials.put("userId", GeoTabConstants.GEO_TAB_USER_ID);
//		credentials.put("password", GeoTabConstants.GEO_TAB_USER_PASS);
//		String json = JsonUtil.addObjects(null, credentials);
		String json = "{\"userId\":{}}";
		
		Map<String, String> ids = new HashMap<String, String>();
		ids.put("id1", "id_one");
		ids.put("id2", "id_two");
		
		System.out.println(JsonUtil.addInObject(json, "userId", ids));
	}
}
