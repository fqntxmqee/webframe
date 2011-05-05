
package org.webframe.support.driver;

import java.util.Enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.support.BaseTests;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-1 下午12:23:56
 */
public class ModulePluginUtilsTest extends BaseTests {

	/**
	 * Test method for
	 * {@link org.webframe.support.driver.ModulePluginUtils#cacheModulePluginConfig(java.lang.String)}
	 * .
	 */
	@Test
	public void testCacheModulePluginConfig() {
		String realPath = getClass().getResource("/").getPath();
		ModulePluginUtils.cacheModulePluginConfig(realPath);
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.driver.ModulePluginUtils#getDrivers(java.lang.Class)}.
	 */
	@Test
	public void testGetDrivers() {
		Enumeration<ModulePluginDriver> drivers = ModulePluginUtils.getDrivers(ModulePluginDriver.class);
		Assert.assertTrue("获取指定接口" + ModulePluginDriver.class + "实现类失败！", drivers.hasMoreElements());
	}
}
