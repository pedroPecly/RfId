package com.uro.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 功能描述：
 *
 * @author Administrator
 * @Date Jul 19, 2008
 * @Time 9:47:53 AM
 * @version 1.0
 */
public class DateUtil {

	public static Date date = null;

	public static DateFormat dateFormat = null;

	public static Calendar calendar = null;

	/**
	 *
	 * @return
	 */
	public static String getCurrentTimeZone() {

		TimeZone tz = TimeZone.getDefault();

		return createGmtOffsetString(true, true, tz.getRawOffset());

	}

	public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {

		int offsetMinutes = offsetMillis / 60000;
		char sign = '+';
		if (offsetMinutes < 0) {

			sign = '-';

			offsetMinutes = -offsetMinutes;

		}

		StringBuilder builder = new StringBuilder(9);

		if (includeGmt) {

			builder.append("GMT");

		}

		builder.append(sign);

		appendNumber(builder, 2, offsetMinutes / 60);

		if (includeMinuteSeparator) {

			builder.append(':');

		}

		appendNumber(builder, 2, offsetMinutes % 60);

		return builder.toString();

	}

	private static void appendNumber(StringBuilder builder, int count, int value) {

		String string = Integer.toString(value);

		for (int i = 0; i < count - string.length(); i++) {

			builder.append('0');

		}

		builder.append(string);

	}

	/**
	 * MMdd
	 * @return
	 */
	public static String getDateNow() {
		return new SimpleDateFormat("MMdd", Locale.ENGLISH).format(new Date());
	}

	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		try {
			dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
			if ((!dateStr.equals("")) && (dateStr.length() < format.length())) {
				dateStr += format.substring(dateStr.length()).replaceAll("[YyMmDdHhSs]", "0");
			}
			date = (Date) dateFormat.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr
	 *            String 字符型日期：YYYY/MM/DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr
	 *            String 字符型日期：yyyy-MM-dd HH:mm:ss 格式
	 * @return Date
	 */
	public static Date parseCompleteDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr
	 *            String 字符型日期：yyyy/MM/dd HH:mm:ss 格式
	 * @return Date
	 */
	public static Date parseCompleteDate1(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr
	 *            String 字符型日期：yyyyMMddHHmmss 格式
	 * @return
	 */
	public static Date parseCompleteDate2(String dateStr) {
		return parseDate(dateStr, "yyyyMMddHHmmss");
	}

	/**
	 * 功能描述：格式化输出日期
	 *
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 功能描述：格式化输出日期
	 *
	 * @param date
	 *            Date 日期
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回年份
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日期
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取今年过了多少天
	 * @param date
	 * @return
	 */
	public static int getDayOfYear(Date date) {
		int julianDay = 0;

//		GregorianCalendar gc = new GregorianCalendar();
//		julianDay = gc.get(GregorianCalendar.DAY_OF_YEAR);

		calendar = Calendar.getInstance();
		calendar.setTime(date);
		julianDay = calendar.get(Calendar.DAY_OF_YEAR);
		return julianDay;
	}

	/**
	 * 获取当前的年月日
	 *
	 * @return
	 */
	public static String getYMD() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		return dateFormat.format(now);
		// calendar = Calendar.getInstance();
		// return calendar.get(Calendar.YEAR) + "-"
		// + (calendar.get(Calendar.MONTH) + 1) + "-"
		// + calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 *
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 *
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 *
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：返回字符型日期
	 *
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回字符型日期
	 *
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy-MM-dd 格式
	 */
	public static String getDate1(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：返回字符型时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String getDateTime1(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyyMMddHHmmss 格式
	 */
	public static String getDateTime2(Date date) {
		return format(date, "yyyyMMddHHmmss");
	}

	/**
	 * 功能描述：返回字符型日期时间，GMT格式
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyyMMddHHmmss 格式
	 */
	public static String getDateTimeWithGMT(Date date) {
		String format = "yyyyMMddHHmmss";
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 功能描述：返回字符型时间，精度到毫秒SSS
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyyMMddHHmmssSSS 格式
	 */
	public static String getTimeWithSSS(Date date) {
		String format = "HHmmssSSS";
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyyMMdd 格式
	 */
	public static String getDateTime3(Date date) {
		return format(date, "yyyyMMdd");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 MMdd 格式
	 */
	public static String getDateTime4(Date date) {
		return format(date, "MMdd");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 HHmmss 格式
	 */
	public static String getDateTime5(Date date) {
		return format(date, "HHmmss");
	}

	/**
	 * 功能描述：日期相加
	 *
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 *
	 * @param date
	 *            Date 日期
	 * @param day
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static Date diffDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) - ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 *
	 * @param strdate
	 *            String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 *
	 * @param strdate
	 *            String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate) {
		date = parseDate(getMonthBegin(strdate));
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 功能描述：常用的格式化日期
	 *
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 *
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String getFormatNowTime() {
		Date date = new Date();

		return getDateTime(date);

	}

	/**
	 * 通过和今日的天数差异获得date
	 *
	 * @param count
	 * @return date
	 */
	public static Date getDate(int count) {
		Calendar d = Calendar.getInstance();
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		d.set(year, month, day + count);
		Date date = d.getTime();
		return date;
	}

	/**
	 * 将yyyy/MM/dd HH:mm:ss 格式转成yyyy-MM-dd HH:mm:ss 格式
	 *
	 * @param dateString
	 * @return
	 */
	public static String transformDateFormat(String dateString) {
		if (!(dateString != null && !dateString.equals(""))) {
			return "";
		}
		String[] partArr = dateString.split(" |-|/");
		for (int i = 1; i < 3; i++) {
			if (partArr[i].length() == 1) {
				partArr[i] = "0" + partArr[i];
			}
		}
		return partArr[0] + "-" + partArr[1] + "-" + partArr[2] + " " + partArr[3];
	}

	/**
	 * yyyyMMddHHmmss 格式转成yyyy-MM-dd HH:mm:ss 格式
	 *
	 * @param dateString
	 * @return
	 */
	public static String transformDateFormat1(String dateString) {
		if (!(dateString != null && !dateString.equals(""))) {
			return "";
		}
		Date date = parseDate(dateString, "yyyyMMddHHmmss");
		return getDateTime1(date);
	}

	/**
	 * 获取指定的时间（当天，昨天，本周，上周）的凌晨
	 *
	 * @param type
	 *            0:当天，1：昨天，2：本周，3：上周
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getSpecialDateStrStart(int type) {
		Calendar d = Calendar.getInstance();
		int year = d.get(Calendar.YEAR);
		int month = d.get(Calendar.MONTH);
		int day = d.get(Calendar.DAY_OF_MONTH);
		int whatDay = d.get(Calendar.DAY_OF_WEEK);
		if (whatDay == 1) {
			whatDay = 7;
		} else {
			whatDay--;
		}

		switch (type) {
			case 0:

				break;
			case 1:
				d.set(year, month, day - 1);
				break;
			case 2:
				d.set(year, month, day - (whatDay - 1));
				break;
			case 3:
				d.set(year, month, day - (whatDay - 1) - 7);
				break;
		}

		Date date = d.getTime();
		return getDate1(date) + " 00:00:00";
	}


	/**
	 * 格式化时间
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
		return dateFormat.format(date);
	}

	/**
	 * @param cal
	 * @return
	 */
	public static int getJulianDay(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1; //Note January returns 0
		int date = cal.get(Calendar.DATE);
		return (1461 * (year + 4800 + (month - 14) / 12)) / 4
				+ (367 * (month - 2 - 12 * ((month - 14) / 12))) / 12
				- (3 * ((year + 4900 + (month - 14) / 12) / 100)) / 4 + date - 32075;
	}

}
