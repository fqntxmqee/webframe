
package org.webframe.web.springmvc.view.freemarker;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.webframe.web.springmvc.util.ModuleUrlPathHelper;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:47:40
 */
public class ModuleFreeMarkerView extends FreeMarkerView {

	protected Log	log	= LogFactory.getLog(getClass());

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return super.checkResource(locale) ? true : moduleCheckResource(getUrl(), locale);
	}

	/**
	 * 如果模块默认包下不存在指定模板，使用系统模块模板
	 * 
	 * @param url
	 * @param locale
	 * @return
	 * @throws Exception
	 * @author: 黄国庆 2011-1-8 下午01:33:19
	 */
	private boolean moduleCheckResource(String url, Locale locale) throws Exception {
		String defaultUrl = ModuleUrlPathHelper.getModuleUrlPath(url);
		log.info(url + " 视图模板不存在，使用默认模块框架的(" + defaultUrl + ")模板进行渲染！");
		setUrl(defaultUrl);
		return super.checkResource(locale);
	}

	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String ctx = request.getContextPath();
		// 添加上下文常量，用于freemarker模板解析时使用
		model.put("ctx", ctx);
	}
}
