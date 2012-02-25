/*
 * wf-web
 * Created on 2012-2-25-下午12:42:31
 */

package org.webframe.web.spring;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.test.BaseSpringTests;

/**
 * ServiceHelperTest
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-25 下午12:42:31
 * @version
 */
public class ServiceHelperTest extends BaseSpringTests {

	/**
	 * Test method for {@link org.webframe.web.spring.ServiceHelper#getApplicationContext()}.
	 */
	@Test
	public void testGetApplicationContext() {
		Assert.assertNotNull("ServiceHelper中ApplicationContext未初始化！", ServiceHelper.getApplicationContext());
	}
}
