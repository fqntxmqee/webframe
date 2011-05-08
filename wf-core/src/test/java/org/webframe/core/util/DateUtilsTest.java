/*
 * wf-core
 * Created on 2011-5-8-下午07:17:08
 */

package org.webframe.core.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-8 下午07:17:08
 */
public class DateUtilsTest {

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getWeekByDate(java.util.Date)}.
	 */
	@Test
	public void testGetWeekByDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		Assert.assertEquals("DateUtils.getWeekByDate返回值应该为：" + week, week, DateUtils.getWeekByDate(date));
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getCNDefaultDate()}.
	 */
	@Test
	public void testGetCNDefaultDate() {
		String cnDate = DateUtils.getCNDefaultDate();
		Assert.assertNotNull("默认中文日期不应该为null！", cnDate);
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getCNWeek(int)}.
	 */
	@Test
	public void testGetCNWeek() {
		int week = DateUtils.getWeekByDate(new Date());
		String cnWeek = DateUtils.getCNWeek(week);
		Assert.assertTrue("默认中文星期不应该为空字符串！week(" + week + ")", !"".equals(cnWeek));
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getFormatedDate(java.util.Date)}.
	 */
	@Test
	public void testGetFormatedDateDate() {
		String strDate = DateUtils.getFormatedDate(new Date());
		Assert.assertEquals(strDate + "应该为'yyyy-MM-dd HH:mm:ss'字符串！" + 19, 19, strDate.length());
		strDate = DateUtils.getFormatedDate(null);
		Assert.assertNull("DateUtils.getFormatedDate(null)应该返回null！", strDate);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.DateUtils#getFormatedDate(java.util.Date, java.lang.String)}.
	 */
	@Test
	public void testGetFormatedDateDateString() {
		String format = "yyyy年MM月dd日 HH时mm分";
		String strDate = DateUtils.getFormatedDate(new Date(), format);
		Assert.assertNotNull("格式化日期失败！format(" + format + ")", strDate);
		format = null;
		strDate = DateUtils.getFormatedDate(new Date(), format);
		Assert.assertNotNull("格式化日期失败！format=null", strDate);
		format = "日期格式化";
		strDate = DateUtils.getFormatedDate(new Date(), format);
		Assert.assertEquals("格式化日期失败！format(" + format + ")", format, strDate);
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getSimpleFormatDate(java.util.Date)}.
	 */
	@Test
	public void testGetSimpleFormatDate() {
		String strDate = DateUtils.getSimpleFormatDate(new Date());
		Assert.assertEquals(strDate + "应该为'yyyy-MM-dd'字符串！" + 10, 10, strDate.length());
		strDate = DateUtils.getSimpleFormatDate(null);
		Assert.assertNull("DateUtils.getSimpleFormatDate(null)应该返回null！", strDate);
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getSimpleFormatedDateNow()}.
	 */
	@Test
	public void testGetSimpleFormatedDateNow() {
		String strDate = DateUtils.getSimpleFormatedDateNow();
		Assert.assertEquals(strDate + "应该为'yyyy-MM-dd'字符串！" + 10, 10, strDate.length());
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#getStandTime()}.
	 */
	@Test
	public void testGetStandTime() {
		String strDate = DateUtils.getStandTime();
		Assert.assertEquals(strDate + "应该为'yyyy-MM-dd HH:mm:ss'字符串！" + 19, 19, strDate.length());
	}

	/**
	 * Test method for {@link org.webframe.core.util.DateUtils#parseStringToDate(java.lang.String)}.
	 */
	@Test
	public void testParseStringToDate() {
		String strDate = "2011-5-6";
		Date date = DateUtils.parseStringToDate(strDate);
		Assert.assertNotNull(strDate + "字符串日期转换为Date失败！", date);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.DateUtils#parseStringToDate(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testParseStringToDateFormat() {
		String strDate1 = "2011年05月08日";
		String strDate2 = "2011-05-08";
		String format = "yyyy年MM月dd日";
		Date date1 = DateUtils.parseStringToDate(strDate1, format);
		Assert.assertNotNull(strDate1 + "字符串日期转换为Date失败！", date1);
		Date date2 = DateUtils.parseStringToDate(strDate2);
		Assert.assertEquals("字符串日期转换为Date失败！" + 10, date2.getTime(), date1.getTime());
	}
}
