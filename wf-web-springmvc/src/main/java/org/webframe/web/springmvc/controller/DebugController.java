/*
 * com.berheley.bi.basic
 * Created on 2011-12-3-上午11:26:56
 */

package org.webframe.web.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Debug控制器
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-29 下午09:51:11
 * @version
 */
@Controller
@RequestMapping("/debug")
public class DebugController extends BaseController {

	private static boolean	IS_DEBUG	= false;

	protected static void setDebug(boolean isdebug) {
		IS_DEBUG = isdebug;
	}

	public final static boolean isDebug() {
		return IS_DEBUG;
	}

	@RequestMapping("/{isDebug}")
	public String index(@PathVariable String isDebug, HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		setDebug(BooleanUtils.toBoolean(isDebug));
		if (log.isDebugEnabled()) {
			log.debug("设置系统调试模式Debug = " + isDebug);
		}
		return null;
	}
}
