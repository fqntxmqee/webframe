
package org.webframe.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 这个类提供各种日期格式的处理函数，标准的日期字符串写法：yyyy-MM-dd 或 yyyy-MM-dd HH:mm
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午09:10:46
 */
public class DateUtils {

	private final static Log	log					= LogFactory.getLog(DateUtils.class);

	static final String			defaultDateFormat	= "yyyy-MM-dd HH:mm:ss";

	static final String			defaultDayFormat	= "yyyy-MM-dd";

	/**
	 * 通过DATE类型得到当前DATE星期
	 * 
	 * @param date 当前日期
	 * @return int (1~7 , 1 代表星期日, 7 代表星期六)
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static int getWeekByDate(Date date) {
		int a = 0;
		GregorianCalendar cal = new GregorianCalendar();
		try {
			cal.setTime(date);
			a = cal.get(Calendar.DAY_OF_WEEK);
		} catch (Exception ex) {
			log.error("通过Date得到week发生异常", ex);
		}
		return a;
	}

	/**
	 * 得到中文日期格式 在获取默认日期时，如果有异常，则返回null
	 * 
	 * @return "2011年5月6日星期五" 字符串
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getCNDefaultDate() {
		try {
			Calendar cal = Calendar.getInstance();
			String str = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DATE) + "日";
			str = str + getCNWeek((cal.get(Calendar.DAY_OF_WEEK) - 1));
			return str;
		} catch (Exception ex) {
			log.error("得到默认中文日期格式发生异常", ex);
		}
		return null;
	}

	/**
	 * 得到中文星期
	 * 
	 * @param week 数字（1,2,3,4,5,6,7）
	 * @return
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getCNWeek(int week) {
		String str_week = "";
		switch (week) {
			case 1 :
				str_week = "星期一";
				break;
			case 2 :
				str_week = "星期二";
				break;
			case 3 :
				str_week = "星期三";
				break;
			case 4 :
				str_week = "星期四";
				break;
			case 5 :
				str_week = "星期五";
				break;
			case 6 :
				str_week = "星期六";
				break;
			case 7 :
				str_week = "星期日";
				break;
		}
		return str_week;
	}

	/**
	 * @param date
	 * @return 返回格式为 'yyyy-MM-dd HH:mm:ss' 的字符串
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getFormatedDate(Date date) {
		return getFormatedDate(date, defaultDateFormat);
	}

	/**
	 * @param now Date 日期
	 * @param format String 日期格式，例如：'yyyy-MM-dd HH:mm:ss'，'yyyy-MM-dd
	 *           HH:mm'，'yyyy-MM-dd'，'yyyy年MM月dd日 HH时mm分ss秒'
	 * @return String 日期时间字符串
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getFormatedDate(Date now, String format) {
		try {
			DateFormat df = format == null ? new SimpleDateFormat() : new SimpleDateFormat(format);
			String str = (now == null ? null : df.format(now));
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回格式为 'yyyy-MM-dd' 的字符串
	 * 
	 * @param now
	 * @return 返回格式为 'yyyy-MM-dd' 的字符串
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getSimpleFormatDate(Date date) {
		return getFormatedDate(date, defaultDayFormat);
	}

	/**
	 * 返回当前时间，格式为“yyyy-MM-dd”
	 * 
	 * @return String
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getSimpleFormatedDateNow() {
		return getSimpleFormatDate(new Date());
	}

	/**
	 * 格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 'yyyy-MM-dd HH:mm:ss'
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static String getStandTime() {
		return getFormatedDate(new Date());
	}

	/**
	 * 通过一个'yyyy-MM-dd'日期格式字符串得到 日期对象；如果转换失败返回null
	 * 
	 * @param date 'yyyy-MM-dd'
	 * @return null or date
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static Date parseStringToDate(String date) {
		return parseStringToDate(date, defaultDayFormat);
	}

	/**
	 * 按照指定日期格式解析字符串
	 * 
	 * @param date 日期字符串
	 * @param format 当前日期字符串格式
	 * @return null or date
	 * @author 黄国庆 2011-5-6 下午09:12:03
	 */
	public static Date parseStringToDate(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date nowDate = null;
		try {
			nowDate = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			log.debug("无法解析" + date);
		}
		return nowDate;
	}
}