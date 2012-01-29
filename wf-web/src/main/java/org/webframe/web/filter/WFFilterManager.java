
package org.webframe.web.filter;

import org.webframe.core.spring.processor.BeanPropertyListAppender;

/**
 * 管理、加载spring配置文件中的Filter
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 上午09:18:32
 * @version
 */
public class WFFilterManager extends BeanPropertyListAppender {

	public static final String	BEAN_NAME_WF_FILTER_CONTEXT	= "wfFilterContext";

	@Override
	public String getBeanName() {
		return super.getBeanName() == null ? BEAN_NAME_WF_FILTER_CONTEXT : super.getBeanName();
	}
}
