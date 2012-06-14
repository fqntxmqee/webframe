package org.webframe.support.startup;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.webframe.support.driver.loader.PropertiesModulePluginLoader;


/**
 *
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 上午10:57:51
 * @version 
 */
public class StartupTest {

	/**
	 * Test method for {@link org.webframe.support.startup.Startup#startup()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStartup() throws Exception {
		new Startup(ClassPathXmlApplicationContext.class, PropertiesModulePluginLoader.class).startup();
	}
}
