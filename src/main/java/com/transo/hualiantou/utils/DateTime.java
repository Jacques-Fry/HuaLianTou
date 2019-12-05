package com.transo.hualiantou.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatter2 = new SimpleDateFormat(
			"yyyy年MM月dd日 HH时mm分");
	static SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 返回String日期
	public static String getTimeString(Date date) {
		return formatter2.format(date);
	}

	// 返回String日期
	public static String getDateString(Date date) {
		return formatter1.format(date);
	}

	// 返回Date日期
	public static Date getDate(String dateString) {
		Date date = new Date();
		try {
			date = formatter1.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


	// 返回当前时间-string
	public static String currentDateString3() {
		return formatter3.format(new Date());

	}

	// 返回当前时间-string
	public static String currentDateString() {
		return formatter1.format(new Date());

	}
	// 返回当前时间-date
	public static Date currentDate()  {
		try {
			return formatter1.parse(currentDateString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	// 返回String日期
	public static String getDateTimeString(Date date) {
		return formatter.format(date);
	}

	// 返回Date日期
	public static Date getDateTime(String dateString) {
		Date date = new Date();

		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算时间差，可以按照需求定制自己的计算逻辑
	 * 
	 * @throws ParseException
	 */
	public static String getTimeGap(Date date) throws ParseException {
		// SimpleDateFormat simpleFormat = new
		// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// Date date = simpleFormat.parse("2019-04-24 17:34:25");
		Date now = new Date();
		long timegap = now.getTime() - date.getTime();
		/* 年差 */
		int years = (int) (timegap / (1000 * 60 * 60 * 24)) / 365;
		if (years > 0) {
			return years + "年前";
		}
		/* 月差 */
		int months = (int) (timegap / (1000 * 60 * 60 * 24)) / 30;
		if (months > 0) {
			return months + "月前";
		}
		/* 星期差 */
		int weeks = (int) (timegap / (1000 * 60 * 60 * 24)) / 7;
		if (weeks > 0) {
			return weeks + "星期前";
		}
		/* 天数差 */
		int days = (int) (timegap / (1000 * 60 * 60 * 24));
		if (days > 0) {
			return days + "天前";
		}
		/* 小时差 */
		int hours = (int) (timegap / (1000 * 60 * 60));
		if (hours > 0) {
			return hours + "小时前";
		}
		/* 分钟差 */
		int minutes = (int) (timegap / (1000 * 60));
		if (minutes > 0) {
			return minutes + "分钟前";
		}
		/* 秒差 */
		int seconds = (int) (timegap / 1000);
		if (seconds > 0) {
			return seconds + "秒前";
		}
		return "刚刚";

	}

	public static void main(String[] args) throws ParseException {
		System.out.println(currentDate());
	}
}
