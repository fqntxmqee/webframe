/*
 * wf-struts
 * Created on 2011-5-30-下午04:16:19
 */

package org.webframe.struts.util;

import static org.junit.Assert.fail;

import java.util.Enumeration;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.webframe.struts.IStrutsSupport;
import org.webframe.support.driver.ModulePluginDriver;
import org.webframe.support.driver.ModulePluginDriverInfo;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 下午04:16:19
 */
public class StrutsConfigUtilsTest extends BaseSpringTests {

	/**
	 * Test method for {@link org.webframe.struts.util.StrutsConfigUtils#getStrutsConfigResources()}.
	 */
	@Test
	public void testGetStrutsConfigResources() {
		Resource[] resources = StrutsConfigUtils.getStrutsConfigResources();
		Assert.assertTrue("加载Struts 配置文件错误！", resources.length == 1);
	}

	/**
	 * Test method for
	 * {@link org.webframe.struts.util.StrutsConfigUtils#getStrutsConfigResources(org.webframe.support.driver.ModulePluginDriverInfo)}
	 * .
	 */
	@Test
	public void testGetStrutsConfigResourcesModulePluginDriverInfo() {
		Enumeration<IStrutsSupport> strutsSupports = StrutsConfigUtils.getDrivers(IStrutsSupport.class);
		int i = 0;
		while (strutsSupports.hasMoreElements()) {
			ModulePluginDriver driver = (ModulePluginDriver) strutsSupports.nextElement();
			ModulePluginDriverInfo driverInfo = StrutsConfigUtils.getDriverInfo(driver.getClass().getName());
			List<Resource> list = StrutsConfigUtils.getStrutsConfigResources(driverInfo);
			Assert.assertTrue("加载Struts 配置文件错误！", list.size() == 1);
			i++;
		}
		if (i == 0) {
			fail("没有找到Struts 配置文件驱动！");
		}
	}
}
