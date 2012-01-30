
package org.webframe.web.springmvc.view.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * Jar包中freemarker模板视图
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午12:45:07
 * @version
 */
public class JarFreeMarkerView extends FreeMarkerView {

	protected Log	log	= LogFactory.getLog(getClass());

	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String ctx = request.getContextPath();
		// 添加上下文常量，用于freemarker模板解析时使用
		model.put("ctx", ctx);
	}
}
