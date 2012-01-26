/*
 * wf-test
 * Created on 2011-5-6-下午01:15:54
 */

package org.webframe.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午01:15:54
 */
public class BaseSpringTransactionalTestsTest extends BaseSpringTransactionalTests {

	@Autowired
	private BaseHttpClientTests	baseHttpClientTests;

	/**
	 * Test method for
	 * {@link org.webframe.test.BaseSpringTransactionalTests#BaseSpringTransactionalTests()}.
	 */
	@Test
	public void testBaseSpringTransactionalTests() {
		Assert.assertNotNull("BaseHttpClientTests 注入失败！", baseHttpClientTests);
	}
}
