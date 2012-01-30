
package org.webframe.web.springmvc.view.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Jar包中freemarker模板视图解析器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午12:46:57
 * @version
 */
public class JarFreeMarkerViewResolver extends FreeMarkerViewResolver {

	@Override
	protected Class<?> requiredViewClass() {
		return JarFreeMarkerView.class;
	}
}
