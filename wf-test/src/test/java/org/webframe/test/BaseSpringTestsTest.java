/*
 * wf-test
 * Created on 2011-5-5-下午04:42:17
 */

package org.webframe.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午04:42:17
 */
public class BaseSpringTestsTest extends BaseSpringTests {

	@Autowired
	private BaseHttpClientTests	baseHttpClientTests;

	public BaseSpringTestsTest() {
		super(new DefaultModulePluginLoader(new String[]{
			"org.webframe.test.TestModulePluginDriver"}));
	}

	@Test
	public void testAutuwiredBean() {
		Assert.assertNotNull("BaseHttpClientTests 注入失败！", baseHttpClientTests);
	}
}
