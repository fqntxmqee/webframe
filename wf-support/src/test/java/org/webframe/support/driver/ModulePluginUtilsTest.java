
package org.webframe.support.driver;

import java.io.IOException;
import java.util.Enumeration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.webframe.support.BaseTests;
import org.webframe.support.driver.exception.ModulePluginConfigException;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-1 下午12:23:56
 */
public class ModulePluginUtilsTest extends BaseTests {

	public ModulePluginUtilsTest() {
		super(new String[]{
					"org.webframe.support.driver.TestModulePluginDriver",
					"org.webframe.support.driver.NotExistModulePluginDriver"});
	}

	/**
	 * Test method for
	 * {@link org.webframe.support.driver.ModulePluginUtils#cacheModulePluginConfig(java.lang.String)}
	 * .
	 */
	@Test
	public void testCacheModulePluginConfig() {
		try {
			ModulePluginUtils.cacheModulePluginConfig(null);
		} catch (ModulePluginConfigException e) {
			e.printStackTrace();
		}
		try {
			String realPath = new ClassPathResource("/LICENSE.txt", getClass()).getFile().getAbsolutePath();
			ModulePluginUtils.cacheModulePluginConfig(realPath);
		} catch (ModulePluginConfigException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String realPath = getClass().getResource("/").getPath();
		ModulePluginUtils.cacheModulePluginConfig(realPath);
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
