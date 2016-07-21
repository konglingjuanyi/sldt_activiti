package com.sldt.mds.dmc.mp.util;

import org.apache.commons.lang.StringUtils;

public class ObjectToString {
	
	public static String retResult(Object obj){
		if(obj == null){
			obj = "";
		}
		return String.valueOf(obj);
	}

	public static String loadSql(String str){
		String str_ = "";
		if(StringUtils.isNotBlank(str)){
			String [] strs = str.split(",");
			for(String s : strs){
				str_ +="'"+s+"',";
			}
			if(str_ !="" && str_.endsWith(",") ){
				str_=str_.substring(0,str_.lastIndexOf(","));
			}
		}
		return str_;
	}
}
