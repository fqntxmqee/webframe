/*
 * wf-web-springmvc
 * Created on 2011-6-28-下午08:38:31
 */

package org.webframe.easy.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UrlPathHelper;
import org.webframe.support.util.StringUtils;

/**
 * 模块url工具处理类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:38:31
 */
public class ModuleUrlPathHelper extends UrlPathHelper {

	public static String	IS_MODULE_HANDLER			= "is_module_hander";

	public static String	MODULE_MAPPING_REQUEST	= "/module";

	@Override
	public String getLookupPathForRequest(HttpServletRequest request) {
		String isModuleHandler = (String) request.getAttribute(IS_MODULE_HANDLER);
		String path = super.getLookupPathForRequest(request);
		if (isModuleHandler == null) {
			return path;
		} else {
			return getModuleUrlPath(path);
		}
	}

	public static String getModuleUrlPath(String path) {
		if (!path.contains("/") || path.length() == 1) {
			return path;
		}
		String[] arr = StringUtils.tokenizeToStringArray(path, "/");
		String res = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) continue;
			res += "/" + arr[i];
		}
		return MODULE_MAPPING_REQUEST + res;
	}
}
