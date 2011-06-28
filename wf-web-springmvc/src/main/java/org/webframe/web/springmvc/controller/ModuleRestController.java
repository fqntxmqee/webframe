
package org.webframe.web.springmvc.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.webframe.core.model.BaseEntity;
import org.webframe.core.model.action.ModuleActionType;
import org.webframe.core.service.IBaseEntityService;
import org.webframe.core.util.BeanUtils;
import org.webframe.core.util.EntityUtils;
import org.webframe.web.exception.WebFrameException;
import org.webframe.web.springmvc.util.ModuleUrlPathHelper;

/**
 * 默认业务模块控制类，提供一般业务模块的增删改查、禁用、启用功能。 如果一般业务模块的功能在这个控制类业务方法的包括范围类，可以不需要为该业务模块单独添加控制类。
 * 如果业务操作方法有写独特，可以定制自己的Service，框架会使用以模块名+"Service"的方式从spring
 * 上下文中获取，并进行关联到baseService属性，自定义的Service重写父类的相关业务方法。例如：
 * 业务模块名为user，那么我们自定义的Service通过注解的方式@Service("userService")进行注解关联。
 * 或者通过配置文件方式注入id为“userService”的Service Bean。<br/> 类注解@RequestMapping("/module")不可修改，如若修改，
 * 请保持与ModuleUrlPathHelper.MODULE_MAPPING_REQUEST一致.<br/>
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:21:47
 */
@RequestMapping("/module")
public class ModuleRestController extends BaseRestController<BaseEntity, String> {

	@Autowired
	protected IBaseEntityService<BaseEntity>	baseEntityService;

	private Set<ModuleActionType>					defaultActions	= new HashSet<ModuleActionType>(0);

	private String										moduleName		= "module";

	@Override
	public ModelAndView _new(HttpServletRequest request, HttpServletResponse response, BaseEntity model)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.新增)) {
			throwModuleFunctionExcludeException(ModuleActionType.新增);
		}
		ModelAndView mv = getModuleModelAndView(request, FORWORD_NEW, model);
		ModelMap modelMap = mv.getModelMap();
		modelMap.addAttribute("action", "/" + getModuleName(request));
		return mv;
	}

	@Override
	public ModelAndView batchDelete(@RequestParam("ids") String[] ids, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.删除)) {
			throwModuleFunctionExcludeException(ModuleActionType.删除);
		}
		BaseEntity model = BeanUtils.instantiateClass(getModuleClass(request));
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			model = baseEntityService.findEntity(getModuleClass(request), id);
			baseEntityService.deleteEntity(model);
		}
		return index(request, model);
	}

	@Override
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response, BaseEntity model)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.新增)) {
			throwModuleFunctionExcludeException(ModuleActionType.新增);
		}
		baseEntityService.saveEntity(model);
		return index(request, model);
	}

	@Override
	public ModelAndView delete(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.删除)) {
			throwModuleFunctionExcludeException(ModuleActionType.删除);
		}
		BaseEntity model = baseEntityService.findEntity(getModuleClass(request), id);
		baseEntityService.deleteEntity(model);
		return index(request, model);
	}

	@Override
	public ModelAndView edit(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.修改)) {
			throwModuleFunctionExcludeException(ModuleActionType.修改);
		}
		BaseEntity model = baseEntityService.findEntity(getModuleClass(request), id);
		ModelAndView mv = getModuleModelAndView(request, FORWORD_EDIT, model);
		ModelMap modelMap = mv.getModelMap();
		modelMap.addAttribute("action", "/" + getModuleName(request));
		return mv;
	}

	/**
	 * 获取业务模块列表页面的视图，进行外部跳转，request 参数无法保留
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @author: 黄国庆 2011-1-22 下午12:24:09
	 */
	protected ModelAndView getListPageModelAndView(HttpServletRequest request, BaseEntity model) {
		ModelAndView mv = new ModelAndView(getListPageRedirect(getModuleName(request)));
		processRequestAttribute(request, model, mv.getModelMap());
		return mv;
	}

	/**
	 * 获取模块Class
	 * 
	 * @param request
	 * @return
	 * @author: 黄国庆 2011-1-7 上午11:21:12
	 */
	@SuppressWarnings("unchecked")
	protected Class<BaseEntity> getModuleClass(HttpServletRequest request) {
		return (Class<BaseEntity>) EntityUtils.getEntityClass(getModuleName(request));
	}

	/**
	 * 根据视图名称，获取视图，并添加queryMap和model到modelMap中
	 * 
	 * @param request
	 * @param forwordType
	 * @param model
	 * @return
	 * @author: 黄国庆 2011-1-7 上午11:21:44
	 */
	protected ModelAndView getModuleModelAndView(HttpServletRequest request, String forwordType, BaseEntity model) {
		String moduleName = getModuleName(request);
		ModelAndView mv = new ModelAndView("/" + moduleName + forwordType);
		processRequestAttribute(request, model, mv.getModelMap());
		return mv;
	}

	@Override
	protected String getModuleName() {
		return moduleName;
	}

	/**
	 * 获取模块名称
	 * 
	 * @param request
	 * @return
	 * @author: 黄国庆 2011-1-7 上午11:20:50
	 */
	private String getModuleName(HttpServletRequest request) {
		return (String) request.getAttribute(ModuleUrlPathHelper.IS_MODULE_HANDLER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView index(HttpServletRequest request, BaseEntity model) throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.查询)) {
			throwModuleFunctionExcludeException(ModuleActionType.查询);
		}
		ModelAndView mv = getModuleModelAndView(request, FORWORD_LIST, model);
		ModelMap modelMap = mv.getModelMap();
		modelMap.addAttribute("action", "/" + getModuleName(request));
		// 使用valuelist进行列表页查询
		setValueListToRequest(model.getClass(), (Map<String, Object>) modelMap.get("queryMap"), request);
		return mv;
	}

	private void processRequestAttribute(HttpServletRequest request, BaseEntity model, ModelMap modelMap) {
		// 获取查询条件，以attribute(id)为name的form元素
		Map<String, Object> queryMap = getQueryMap(request, model.getClass());
		modelMap.addAttribute("queryMap", queryMap);
		modelMap.addAttribute("model", model);
		// 获取model的viewElement列表，在ftl模板中使用
		modelMap.addAttribute("attrList", model.viewElementList());
	}

	public void setBaseService(IBaseEntityService<BaseEntity> baseService) {
		this.baseEntityService = baseService;
	}

	public void setDefaultActions(Set<ModuleActionType> defaultActions) {
		this.defaultActions = defaultActions;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public ModelAndView show(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.查看)) {
			throwModuleFunctionExcludeException(ModuleActionType.查看);
		}
		BaseEntity model = baseEntityService.findEntity(getModuleClass(request), id);
		ModelAndView mv = getModuleModelAndView(request, FORWORD_SHOW, model);
		return mv;
	}

	@Override
	public ModelAndView update(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
				throws WebFrameException {
		if (!defaultActions.contains(ModuleActionType.修改)) {
			throwModuleFunctionExcludeException(ModuleActionType.修改);
		}
		BaseEntity model = baseEntityService.findEntity(getModuleClass(request), id);
		bind(request, model);
		baseEntityService.updateEntity(model);
		return index(request, model);
	}

	protected void throwModuleFunctionExcludeException(ModuleActionType actionType) throws WebFrameException {
		throw new WebFrameException("业务模块：" + getModuleName() + " 不允许" + actionType.getLabel() + "操作！");
	}
}
