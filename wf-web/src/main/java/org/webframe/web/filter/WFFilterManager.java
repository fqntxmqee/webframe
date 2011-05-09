
package org.webframe.web.filter;

import org.webframe.web.spring.processor.BeanPropertyListAppender;

/**
 * @author <a href="mailto:guoqing.huang@berheley.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-4-21 下午02:24:21
 */
public class WFFilterManager extends BeanPropertyListAppender {

	private static final String	webframeFilterContextBeanName	= "webframeFilterContext";

	@Override
	public String getBeanName() {
		return super.getBeanName() == null ? webframeFilterContextBeanName : super.getBeanName();
	}
}
