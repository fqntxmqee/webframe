
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
 * @since 2012-1-29 下午10:08:25
 * @version @param <T>
 * @version @param <PK>
 */
public abstract class BaseRestController<T extends BaseEntity, PK extends Serializable> extends BaseValueListController {

	/**
	 * 业务模块新增页面跳转方法，访问连接为模块名称加上"/new"； 默认HTTP方法为GET或POST； 例如：/moduleName/new
	 * 
	 * @param req
	 * @param res
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:43:24
	 */
	@RequestMapping({
		"/new"})
	public abstract ModelAndView _new(HttpServletRequest req, HttpServletResponse res, T model) throws WebFrameException;

	/**
	 * 业务模块执行删除方法，访问连接为模块名称； HTTP方法一定为DELETE； 例如：/moduleName
	 * 
	 * @param ids @RequestParam 主键id集合
	 * @param req
	 * @param res
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:58:48
	 * @throws ServiceException
	 */
	@RequestMapping(method = {
		RequestMethod.DELETE})
	public abstract ModelAndView batchDelete(@RequestParam("ids") PK[] ids, HttpServletRequest req, HttpServletResponse res)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行保存方法，访问连接为模块名称； HTTP方法一定为POST； 例如：/moduleName
	 * 
	 * @param req
	 * @param res
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(method = {
		RequestMethod.POST})
	public abstract ModelAndView create(HttpServletRequest req, HttpServletResponse res, T model)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行删除方法，访问连接为模块名称加上主键id； HTTP方法一定为DELETE； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param req
	 * @param res
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(value = {
		"/{id}"}, method = {
		RequestMethod.DELETE})
	public abstract ModelAndView delete(@PathVariable PK id, HttpServletRequest req, HttpServletResponse res)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块修改页面跳转方法，访问连接为模块名称加上主键id加上/edit； 默认HTTP方法为GET或POST； 例如：/moduleName/ID值/edit
	 * 
	 * @param id @PathVariable, 主键id
	 * @param req
	 * @param res
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping({
		"/{id}/edit"})
	public abstract ModelAndView edit(@PathVariable PK id, HttpServletRequest req, HttpServletResponse res)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块列表页跳转方法，访问连接为模块名称； 默认HTTP方法为GET； 例如：/moduleName
	 * 
	 * @param req
	 * @param model 业务模块
	 * @return
	 * @throws WebFrameException
	 * @author: 黄国庆 2011-1-22 上午11:42:45
	 */
	@RequestMapping
	public abstract ModelAndView index(HttpServletRequest req, T model) throws WebFrameException;

	/**
	 * 业务模块查看页面跳转方法，访问连接为模块名称加上主键id； 默认HTTP方法为GET或POST； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param req
	 * @param res
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping({
		"/{id}"})
	public abstract ModelAndView show(@PathVariable PK id, HttpServletRequest req, HttpServletResponse res)
				throws WebFrameException, ServiceException;

	/**
	 * 业务模块执行更新方法，访问连接为模块名称加上主键id； HTTP方法一定为PUT； 例如：/moduleName/ID值
	 * 
	 * @param id @PathVariable, 主键id
	 * @param req
	 * @param res
	 * @return
	 * @throws WebFrameException
	 * @throws ServiceException
	 */
	@RequestMapping(value = {
		"/{id}"}, method = {
		RequestMethod.PUT})
	public abstract ModelAndView update(@PathVariable PK id, HttpServletRequest req, HttpServletResponse res)
				throws WebFrameException, ServiceException;
}
