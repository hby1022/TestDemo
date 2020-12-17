package com.transpeed.park.util;

import com.google.gson.*;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import org.codehaus.jettison.json.JSONObject;

public class JsonUtils {
	private static Gson gson = new Gson();
	//JSON转Map
	public static Map<String, String> getMapFromJson(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map<String, String> map = new HashMap<String, String>();
		for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			String value=jsonObject.getString(key);
			map.put(key, jsonObject.getString(key));
//			if(org.apache.commons.lang.StringUtils.isNotBlank(value)&&!"null".equalsIgnoreCase(value))
//			{
//				map.put(key, jsonObject.getString(key));
//			}
		}
		return map;
	}
	//Map转JSON
	public static JSONObject mapToJson(Map<String, String> map) throws Exception {
		//JSONArray array_test = new JSONArray();
		//array_test.add(map);
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject;
	}


	public static String beanToJson(Object obj) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(obj);
	}


	public static String formatAsJSON(Object obj) {
		return formatAsJSON(gson.toJson(obj));
	}

	/**
	 * 格式JSON,使Json美化
	 *
	 * @param content
	 * @return
	 */

	public static String formatAsJSON(String content) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		JsonParser jsonPar = new JsonParser();
		JsonElement jsonEl = jsonPar.parse(content);
		return gson.toJson(jsonEl);
	}

	// list转json
	public static String listToJson(Object listobj) {
		return gson.toJson(listobj);

	}

	 public final static boolean isValidJson(String jsonInString) {
	        try {
	            gson.fromJson(jsonInString, Object.class);
	            return true;
	        } catch(JsonSyntaxException ex) {
	            return false;
	        }
	    }

}
