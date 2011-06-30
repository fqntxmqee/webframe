/*
 * wf-web-page
 * Created on 2011-6-30-下午07:29:37
 */

package org.webframe.web.page;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.service.IBaseService;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTests;
import org.webframe.web.page.test.TTest;
import org.webframe.web.page.web.mvc.ValueListHandlerHelper;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-30 下午07:29:37
 */
@ContextConfiguration(locations = {
	"classpath:wf-page-test.xml"})
public class ValueListTest extends BaseSpringTests {

	@Autowired
	private ValueListHandlerHelper	valueListHelper;

	@Autowired
	private IBaseService					baseService;

	private static int					initDataTimes	= 1;

	public ValueListTest() {
		super();
	}

	@Test
	public void testGetValueListBySqlAdapter() {
		String sqlAdapter = "testListSqlAdapter";
		ValueListInfo info = new ValueListInfo();
		ValueList valueList = valueListHelper.getValueList(sqlAdapter, info);
		List<Object> list = valueList.getList();
		Assert.assertEquals("testListSqlAdapter 查询数据记录不正确！", 2, list.size());
		System.out.println(list.toString());
	}

	@Test
	public void testGetValueListByHqlAdapter() {
		String hqlAdapter = "testListHqlAdapter";
		ValueListInfo info = new ValueListInfo();
		ValueList valueList = valueListHelper.getValueList(hqlAdapter, info);
		List<Object> list = valueList.getList();
		Assert.assertEquals("testListHqlAdapter 查询数据记录不正确！", 2, list.size());
		for (Object object : list) {
			System.out.println(object.toString());
		}
	}

	@Before
	public void initData() throws ServiceException {
		if (initDataTimes <= 0) return;
		initDataTimes--;
		TTest test = new TTest();
		test.setName("tt\\sdf\"\'");
		test.setPassword("password");
		test.setEnabled(true);
		test.setCreateTime(DateUtils.getStandTime());
		baseService.save(test);
		test.setName("we423234we");
		test.setPassword("password");
		test.setEnabled(true);
		test.setCreateTime(DateUtils.getStandTime());
		baseService.save(test);
	}
}
