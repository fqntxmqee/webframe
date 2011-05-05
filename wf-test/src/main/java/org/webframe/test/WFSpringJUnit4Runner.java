
package org.webframe.test;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-11 下午04:22:43
 */
public class WFSpringJUnit4Runner extends SpringJUnit4ClassRunner {

	public WFSpringJUnit4Runner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected String getDefaultContextLoaderClassName(Class<?> clazz) {
		return WFXmlContextLoader.class.getName();
	}
}
