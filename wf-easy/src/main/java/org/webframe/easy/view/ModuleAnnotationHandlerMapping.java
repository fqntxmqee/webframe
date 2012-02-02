
package org.webframe.easy.view;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.webframe.core.service.IBaseEntityService;
import org.webframe.core.util.EntityUtils;
import org.webframe.easy.controller.ModuleRestController;
import org.webframe.easy.model.EasyEntity;
import org.webframe.easy.util.ModuleUrlPathHelper;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:55:54
 */
public class ModuleAnnotationHandlerMapping extends DefaultAnnotationHandlerMapping {

	protected Log						log								= LogFactory.getLog(getClass());

	private Map<String, Object>	moduleHandlerMap				= new LinkedHashMap<String, Object>();

	private String						defaultModuleHandlerName	= "moduleRestController";

	private String						defaultServiceSuffix			= "Service";

	protected Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	@Override
	protected void detectHandlers() throws BeansException {
		super.detectHandlers();
		detectModuleHandlers();
	}

	@Override
	protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
		Object handler = super.lookupHandler(urlPath, request);
		if (handler == null && urlPath != null) {
			String moduleName = urlPath.substring(urlPath.indexOf("/") + 1);
			String[] matchs = moduleName.split("/");
			String key = "/" + matchs[0];
			if (moduleHandlerMap.containsKey(key)) {
				request.setAttribute(ModuleUrlPathHelper.IS_MODULE_HANDLER, matchs[0]);
				return moduleHandlerMap.get(key);
			}
		}
		return handler;
	}

	@SuppressWarnings("unchecked")
	protected void detectModuleHandlers() {
		Map<String, Object> handlerMap = getHandlerMap();
		for (String moduleName : EntityUtils.getEntitiesName()) {
			if (handlerMap.containsKey("/" + moduleName)) continue;
			Object moduleHandler = getModuleHandler();
			if (moduleHandler == null) {
				log.error("没有配置ModuleHandler(" + ModuleRestController.class + ")！");
				continue;
			}
			if (moduleHandler instanceof ModuleRestController) {
				ModuleRestController moduleRestController = (ModuleRestController) moduleHandler;
				moduleRestController.setModuleName(moduleName);
				EasyEntity entity = (EasyEntity) BeanUtils.instantiateClass(EntityUtils.getEntityClass(moduleName));
				moduleRestController.setDefaultActions(entity.defaultModuleActionTypes());
				try {
					// 根据业务模块名称获取业务模块特有的Servcie，如果找到，保存到moduleRestController中。
					Object moduleService = getBean(moduleName + defaultServiceSuffix);
					if (moduleService != null && moduleService instanceof IBaseEntityService) {
						moduleRestController.setBaseService((IBaseEntityService<EasyEntity>) moduleService);
						if (log.isInfoEnabled()) {
							log.info("业务模块：" + moduleName + "使用自定义的Service：(" + moduleService.getClass().getName() + ")！");
						}
					}
				} catch (NoSuchBeanDefinitionException nde) {
					log.info("业务模块：" + moduleName + "没有自定义的Service！");
				}
				moduleHandlerMap.put("/" + moduleName, moduleHandler);
			} else {
				log.error("名称为：" + moduleName + "（" + moduleHandler.getClass() + "）非ModuleRestController子类");
			}
		}
	}

	/**
	 * 默认业务模块Handler scope采用prototype配置，用多例模式获取 默认业务模块Handler； 如果没有找到默认业务模块Handler，返回null。
	 * 
	 * @return
	 * @author: 黄国庆 2011-1-23 下午09:40:19
	 */
	private Object getModuleHandler() {
		try {
			return getBean(getDefaultModuleHandlerName());
		} catch (NoSuchBeanDefinitionException nde) {
			return null;
		}
	}

	public String getDefaultModuleHandlerName() {
		return defaultModuleHandlerName;
	}

	public void setDefaultModuleHandlerName(String defaultModuleHandlerName) {
		this.defaultModuleHandlerName = defaultModuleHandlerName;
	}
}
