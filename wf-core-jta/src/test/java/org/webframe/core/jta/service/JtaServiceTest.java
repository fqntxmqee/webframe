/*
 * wf-core-jta
 * Created on 2011-6-29-下午05:08:00
 */

package org.webframe.core.jta.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.jta.test.TTest;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-29 下午05:08:00
 */
public class JtaServiceTest extends BaseSpringTests {

	@Autowired
	private IJtaService	jtaService;

	@Test
	public void testCreate() throws ServiceException {
		TTest test = new TTest();
		test.setName("tt\\sdf\"\'");
		test.setPassword("password");
		test.setEnabled(true);
		test.setCreateTime(DateUtils.getStandTime());
		jtaService.save(test);
		Assert.assertNotNull(test.getId());
		Assert.assertNotNull(jtaService.get(TTest.class, test.getId()));
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.jta.service.JtaService#jtaCreateTable(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testJtaCreateTable() {
		TTest test = new TTest();
		test.setName("tt\\sdf\"\'");
		test.setPassword("password");
		test.setEnabled(true);
		test.setCreateTime(DateUtils.getStandTime());
		try {
			jtaService.jtaCreateTable(test);
		} catch (Exception e) {
			logger.error(e);
		}
		Assert.assertNotNull(test.getId());
		Assert.assertNull("数据库中不存在id: " + test.getId() + " 的记录！", jtaService.get(TTest.class, test.getId()));
	}
}
