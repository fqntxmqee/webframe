
package org.webframe.web.springmvc.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.model.BaseEntity;
import org.webframe.web.exception.WebFrameException;

/**
 * 提供REST控制器的基类 <li>/moduleName => index()</li> <li>/moduleName/new => _new()</li>
 * <li>/moduleName/{id} => show()</li> <li>/moduleName/{id}/edit => edit()</li> <li>/moduleName POST
 * => create()</li> <li>/moduleName/{id} PUT => update()</li> <li>/moduleName/{id} DELETE =>
 * delete()</li> <li>/moduleName DELETE => batchDelete()</li>
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp @param <T>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp @param <PK> $ Create: 2011-6-28
 *          下午08:22:13
 */
public abstract class BaseRestController<T extends BaseEntity, PK extends Serializable> extends BaseValueListController {

	protected final String	FORWORD_EDIT		= "/edit";

	protected final String	FORWORD_LIST		= "/list";

	protected final String	FORWORD_NEW			= "/new";

	private final String		FORWORD_REDIRECT	= "redirect:/";

	protected final String	FORWORD_SHOW		= "/show";

	/**
	 * 业务模块新增页面跳转方法，访问连接为模块名称加上"/new"； 默认HTTP方法为GET或POST； 例如：/moduleName/new
	 * 
	 * @param request
	 * @param response
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:43:24
	 */
	@RequestMapping({
		"/new"})
	public abstract ModelAndView _new(HttpServletRequest request, HttpServletResponse response, T model)
				throws WebFrameException;

	/**
	 * 业务模块执行删除方法，访问连接为模块名称； HTTP方法一定为DELETE； 例如：/moduleName
	 * 
	 * @param ids @RequestParam 主键id集合
	 * @param request
	 * @param response
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:58:48
	 * @throws ServiceException
	 */
	@RequestMapping(method = {
		RequestMethod.DELETE})
	public abstract ModelAndView batchDelete(@RequestParam("ids") PK[] ids, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行保存方法，访问连接为模块名称； HTTP方法一定为POST； 例如：/moduleName
	 * 
	 * @param request
	 * @param response
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(method = {
		RequestMethod.POST})
	public abstract ModelAndView create(HttpServletRequest request, HttpServletResponse response, T model)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行删除方法，访问连接为模块名称加上主键id； HTTP方法一定为DELETE； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param request
	 * @param response
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(value = {
		"/{id}"}, method = {
		RequestMethod.DELETE})
	public abstract ModelAndView delete(@PathVariable PK id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块修改页面跳转方法，访问连接为模块名称加上主键id加上/edit； 默认HTTP方法为GET或POST； 例如：/moduleName/ID值/edit
	 * 
	 * @param id @PathVariable, 主键id
	 * @param request
	 * @param response
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping({
		"/{id}/edit"})
	public abstract ModelAndView edit(@PathVariable PK id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException, ServiceException;

	/**
	 * 根据规则的跳转类型，值为字符串，获取该模块相关操作方法的跳转链接
	 * 
	 * @param forwordType 一般为方法上@RequestMapping定义的值
	 * @return
	 * @author: 黄国庆 2011-1-21 下午10:29:03
	 */
	protected String getForword(String forwordType) {
		String moduleName = getModuleName();
		if (moduleName == null) return "";
		if (forwordType == null) return "/" + moduleName;
		return "/" + moduleName + forwordType;
	}

	/**
	 * 获取模块的列表页跳转链接，用户getModuleName()
	 * 
	 * @return
	 * @author: 黄国庆 2011-1-21 下午10:28:55
	 */
	protected String getListPageRedirect() {
		String moduleName = getModuleName();
		return getListPageRedirect(moduleName);
	}

	/**
	 * 获取模块的列表页跳转链接
	 * 
	 * @param moduleName 模块名称
	 * @return
	 * @author: 黄国庆 2011-1-21 下午10:28:47
	 */
	protected String getListPageRedirect(String moduleName) {
		if (moduleName == null) return FORWORD_REDIRECT;
		return FORWORD_REDIRECT + moduleName;
	}

	/**
	 * 获取模块名称，不能为null或空，子类需要实现该方法
	 * 
	 * @return
	 * @author: 黄国庆 2011-1-21 下午10:29:07
	 */
	protected abstract String getModuleName();

	/**
	 * 业务模块列表页跳转方法，访问连接为模块名称； 默认HTTP方法为GET； 例如：/moduleName
	 * 
	 * @param request
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:42:45
	 */
	@RequestMapping
	public abstract ModelAndView index(HttpServletRequest request, T model) throws WebFrameException;

	/**
	 * 业务模块查看页面跳转方法，访问连接为模块名称加上主键id； 默认HTTP方法为GET或POST； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param request
	 * @param response
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping({
		"/{id}"})
	public abstract ModelAndView show(@PathVariable PK id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行更新方法，访问连接为模块名称加上主键id； HTTP方法一定为PUT； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param request
	 * @param response
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(value = {
		"/{id}"}, method = {
		RequestMethod.PUT})
	public abstract ModelAndView update(@PathVariable PK id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException, ServiceException;
}
