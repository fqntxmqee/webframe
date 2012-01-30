/*
 * wf-web-springmvc
 * Created on 2012-1-30-下午08:09:56
 */

package org.webframe.web.springmvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webframe.core.exception.ServiceException;
import org.webframe.web.page.ValueList;
import org.webframe.web.page.exp.ValueListException;
import org.webframe.web.springmvc.exp.AjaxException;
import org.webframe.web.springmvc.test.TTest;
import org.webframe.web.valuelist.ValueListUtils;

/**
 * 测试Controller
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-1-30 下午08:09:56
 * @version
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseValueListController {

	@RequestMapping("/exp")
	public String exp(HttpServletRequest req, HttpServletResponse res) {
		throw new NullPointerException("测试Exception异常");
	}

	@RequestMapping("/serviceExp")
	public String serviceExp(HttpServletRequest req, HttpServletResponse res) throws ServiceException {
		throw new ServiceException("测试Service异常");
	}

	@RequestMapping("/ajaxExp")
	public String ajaxExp(HttpServletRequest req, HttpServletResponse res) {
		throw new AjaxException("测试Ajax异常");
	}

	@RequestMapping("/getQueryMap")
	public String getQueryMap(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		Map<String, Object> queries = getQueryMap(req);
		outWriteJSON(res, JSONObject.fromObject(queries).toString());
		return null;
	}

	@RequestMapping("/getQueryMapClazz")
	public String getQueryMapClazz(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		Map<String, Object> queries = getQueryMap(req, TTest.class);
		outWriteJSON(res, JSONObject.fromObject(queries).toString());
		return null;
	}

	@RequestMapping("/valuelistExp")
	public String valuelistExp(HttpServletRequest req, HttpServletResponse res) {
		throw new ValueListException("测试ValueListException异常", new Exception());
	}

	@RequestMapping("/getValueList")
	public String getValueList(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		ValueList<?> valueList = getValueList(req);
		JSONArray array = ValueListUtils.valueListToJson(valueList);
		outWriteJSON(res, array.toString());
		return null;
	}
}
