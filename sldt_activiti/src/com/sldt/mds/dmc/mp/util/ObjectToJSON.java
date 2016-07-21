package com.sldt.mds.dmc.mp.util;

import java.lang.reflect.Method;

import com.sldt.framework.common.PageResults;

import net.sf.json.JSONObject;

public class ObjectToJSON {
	
	public static String objectToJSON(Object t) {
		java.lang.reflect.Field[] fs = t.getClass().getDeclaredFields();
//		HashMap<String, String> jsonMap = new HashMap<String, String>();
		String json = new String();
		try {
			for (java.lang.reflect.Field field : fs) {
				String propertyName = field.getName();
				String methodName = "get"
						+ propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1);
				Method m = t.getClass().getMethod(methodName);
				if (m != null) {
					Object o = m.invoke(t);
					if (o != null)
						// jsonMap.put(field.getName(),o.toString());
						json = json + "|" + o.toString();
				}
			}
			// json=JSONUtil.serialize(jsonMap);
		} catch (Exception e) {

		}
		return json;
	}
	
	public static JSONObject retResult(Object obj,Page page){
		JSONObject json = new JSONObject();
		json.put("data", obj);
		if(page!=null){
			page.refreshPageNum();
			json.put("totalPages", page.getTotalPages());
			json.put("currPage", page.getCurrPage());
		}
		return json;
	}
	
	public static JSONObject returnResult(Object obj,PageResults page){
		JSONObject json = new JSONObject();
		json.put("data", obj);
		if(page!=null){
			page.setTotalPages(((page.getTotalRecs()%page.getPageSize())==0 ? page.getTotalRecs()/page.getPageSize() : (page.getTotalRecs()/page.getPageSize())+1));
			page.refreshPageNum();
			json.put("totalPages", page.getTotalPages());
			json.put("currPage", page.getCurrPage());
		}
		return json;
	}
	
}
