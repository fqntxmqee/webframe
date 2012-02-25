/*
 * wf-support
 * Created on 2012-2-25-下午12:24:32
 */

package org.webframe.support.util;

import org.springframework.context.ApplicationContext;

/**
 * 提供ApplicationContext
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-25 下午12:24:32
 * @version
 */
public abstract class ApplicationContextSupport {

	/*
	 * spring 上下文
	 */
	private static ApplicationContext	ac	= null;

	protected static void initAc(ApplicationContext applicationContext) {
		if (applicationContext == null) return;
		ac = applicationContext;
	}

	protected static ApplicationContext getAc() {
		return ac;
	}
}
