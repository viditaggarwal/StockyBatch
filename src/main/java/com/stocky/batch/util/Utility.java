package com.stocky.batch.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Utility {
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getCurrentDate() {
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.sql.Date date = new java.sql.Date(Calendar.getInstance(). getTime().getTime());			
		String formattedDate = formatter.format(date);
		return formattedDate;
	}
	
	public static String getHistoryDate(int days) {
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.add(Calendar.DATE, -days);
		java.sql.Date date = new java.sql.Date(cal.getTime().getTime());
		String formattedDate = formatter.format(date);
		return formattedDate;
	}
	
	public static String getFormattedDate(Date date){
		formatter.setTimeZone(TimeZone.getTimeZone("PST"));
		return formatter.format(date);
	}
}
