/*
 * com.berheley.bi.basic
 * Created on 2011-12-3-上午10:26:20
 */

package org.webframe.web.front.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BasicDynaBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webframe.web.page.ValueList;
import org.webframe.web.springmvc.bean.AjaxJson;
import org.webframe.web.springmvc.controller.BaseValueListController;
import org.webframe.web.springmvc.exp.AjaxException;
import org.webframe.web.valuelist.ValueListUtils;

/**
 * Ext控制类，用于grid、tree等组件后台请求数据
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-3 上午10:26:20
 */
@Controller
@RequestMapping("/ext")
public class ExtController extends BaseValueListController {

	/**
	 * 通用easygrid数据获取方法
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @author 黄国庆 2011-12-6 上午08:41:40
	 */
	@RequestMapping("/easygrid")
	public String easygrid(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		ValueList<?> list = getValueList(req);
		outWriteJSON(res, wrapJson(list));
		return null;
	}

	/**
	 * 通用tree数据获取方法
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @author 黄国庆 2011-12-6 上午09:29:13
	 */
	@RequestMapping("/tree")
	public String tree(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		ValueList<?> list = getValueList(req);
		outWriteJSON(res, ValueListUtils.valueListToJson(list).toString());
		return null;
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @author 黄国庆 2011-12-6 上午09:29:51
	 */
	@RequestMapping("/childid")
	public String childid(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		String id = req.getParameter("id");
		ValueList<?> list = getValueList(req);
		if ("-1".equals(id)) {
			outWriteJSON(res, queryById(id, list.getList(), true).toString());;
		} else {
			if (id == null) throw new AjaxException("父ID不能为空！");
			JSONArray array = queryById(id, list.getList(), false);
			array.add(id);
			outWriteJSON(res, array.toString());;
		}
		return null;
	}

	/**
	 * 通用easycombo数据获取方法
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @author 黄国庆 2011-12-6 上午08:42:31
	 */
	@RequestMapping("/combo")
	public String combo(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		ValueList<?> list = getValueList(req);
		// 判断是否封装版本
		JSONArray data = JSONArray.fromObject(list.getList());
		if (req.getParameter("is_new_version_") == null) {
			outWriteJSON(res, data.toString());
		} else {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.put("data", data);
			ajaxJson.put("totalCount", list.getValueListInfo().getTotalNumberOfEntries());
			outWriteJSON(res, ajaxJson.toString());
		}
		return null;
	}

	@RequestMapping("/gridexport")
	public String gridexport(HttpServletRequest req, HttpServletResponse res) {
		setAjaxRequest(req);
		return null;
	}

	/**
	 * 获取指定父id的子id集合
	 * 
	 * @param id 父id, 不能为null
	 * @param list 数据集合
	 * @param all 是否全部加载数据
	 * @return
	 * @author 黄国庆 2011-12-6 上午10:06:08
	 */
	protected JSONArray queryById(String id, List<?> list, boolean all) {
		JSONArray temp = new JSONArray();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			BasicDynaBean dynaBean = (BasicDynaBean) iterator.next();
			if (all) {
				temp.add(dynaBean.get("id_"));
			} else {
				if (id.equals(dynaBean.get("parentid_"))) {
					temp.add(dynaBean.get("id_"));
				}
			}
		}
		if (all) return temp;
		for (Object obj : temp) {
			if (obj == null) continue;
			temp.addAll(queryById(obj.toString(), list, false));
		}
		return temp;
	}

	/**
	 * 将valuelist中的集合包装成固定格式的JSON字符串
	 * 
	 * @param list
	 * @return JSON字符串
	 * @author 黄国庆 2012-2-8 下午10:35:14
	 */
	protected String wrapJson(ValueList<?> list) {
		JSONObject json = new JSONObject();
		List<?> list_ = list.getList();
		json.put("totalCount", list.getValueListInfo().getTotalNumberOfEntries());
		if (list_ != null && list_.size() > 0) {
			json.put("data", ValueListUtils.valueListToJson(list).toString());
		} else {
			// 无数据
			json.put("data", "[{\"信息\": \"无数据\"}]");
			json.put("norecord", true);
		}
		return json.toString();
	}
}
