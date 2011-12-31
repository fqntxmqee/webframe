/*
 * wf-support
 * Created on 2011-5-6-下午01:40:02
 */

package org.webframe.support.driver.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.webframe.support.driver.exception.DriverNotExistException;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-6 下午01:40:02
 */
public class DefaultModulePluginLoaderTest {

	private static final Log	log	= LogFactory.getLog(DefaultModulePluginLoaderTest.class);

	/**
	 * Test method for
	 * {@link org.webframe.support.driver.loader.DefaultModulePluginLoader#loadModulePlugin()}.
	 */
	@Test
	public void testLoadModulePlugin() {
		DefaultModulePluginLoader loader = new DefaultModulePluginLoader(null);
		try {
			loader.loadModulePlugin();
		} catch (DriverNotExistException e) {
			log.error(e.getMessage());
		}
		String[] drivers = new String[]{
					"org.webframe.support.NotExistModulePluginDriver", null};
		try {
			loader.loadModulePlugin(drivers);
		} catch (DriverNotExistException e) {
			log.error(e.getMessage());
		}
	}
}
