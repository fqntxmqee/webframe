
package org.webframe.web.springmvc.view.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:47:58
 */
public class ModuleFreeMarkerViewResolver extends FreeMarkerViewResolver {

	@Override
	protected Class<?> requiredViewClass() {
		return ModuleFreeMarkerView.class;
	}
}
