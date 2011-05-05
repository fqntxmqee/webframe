/*
 * wf-test
 * Created on 2011-5-5-下午04:35:27
 */

package org.webframe.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.loader.ModulePluginLoader;
import org.webframe.support.driver.loader.PropertiesModulePluginLoader;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午04:35:27
 */
@RunWith(WFSpringJUnit4Runner.class)
@ContextConfiguration(locations = {
	"wf-test.xml"})
public class BaseSpringTransactionalTests extends AbstractTransactionalJUnit4SpringContextTests {

	public BaseSpringTransactionalTests() {
		this(new PropertiesModulePluginLoader());
	}

	public BaseSpringTransactionalTests(ModulePluginLoader modulePluginLoader) {
		try {
			modulePluginLoader.loadModulePlugin();
		} catch (DriverNotExistException e) {
			SystemLogUtils.errorPrintln(e.getMessage());
		}
	}
}
