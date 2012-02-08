/*
 * com.berheley.bi.system
 * Created on 2011-11-30-下午02:27:10
 */

package org.webframe.web.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webframe.web.springmvc.controller.BaseController;

/**
 * 后台管理首页Controller
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-8 下午10:52:56
 * @version
 */
@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController {

	@RequestMapping
	public String index(HttpServletRequest request) {
		return "/manage";
	}
}
