/*
 * wf-support
 * Created on 2011-5-5-下午05:29:52
 */

package org.webframe.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webframe.support.driver.exception.DriverNotExistException;
import org.webframe.support.driver.loader.DefaultModulePluginLoader;
import org.webframe.support.util.SystemLogUtils;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午05:29:52
 */
public class BaseTests {

	protected Log	log	= LogFactory.getLog(getClass());

	public BaseTests() {
		this(new String[]{
			"org.webframe.support.driver.TestModulePluginDriver"});
	}

	public BaseTests(String[] drivers) {
		try {
			new DefaultModulePluginLoader(drivers).loadModulePlugin();
		} catch (DriverNotExistException e) {
			SystemLogUtils.errorPrintln(e.getMessage());
		}
	}
}
