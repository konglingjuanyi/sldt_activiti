package com.sldt.mds.dmc.mp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 获得当前日期数据
	 * @return yyyyMMdd
	 */
	public static String getYmdDate(){
		
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	/**
	 * 获得当前日期的前三天日期数据
	 * @return yyyyMMdd
	 */
	public static String getBeforeDate(){
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();

		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -3);  //设置为三天
		dBefore = calendar.getTime();   //得到前三天的时间

		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    //格式化前三天
		return defaultStartDate;
	}
	
}
