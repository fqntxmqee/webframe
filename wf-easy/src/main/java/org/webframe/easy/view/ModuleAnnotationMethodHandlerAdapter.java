
package org.webframe.easy.view;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.webframe.core.util.EntityUtils;
import org.webframe.easy.util.ModuleUrlPathHelper;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-6-28 下午08:45:19
 */
public class ModuleAnnotationMethodHandlerAdapter extends AnnotationMethodHandlerAdapter {

	public ModuleAnnotationMethodHandlerAdapter() {
		setUrlPathHelper(new ModuleUrlPathHelper());
	}

	@Override
	public boolean supports(Object handler) {
		return super.supports(handler);
	}

	@Override
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target, String objectName)
				throws Exception {
		String moduleName = (String) request.getAttribute(ModuleUrlPathHelper.IS_MODULE_HANDLER);
		Object realTarget = target;
		if (moduleName != null) {
			Class<?> moduleClass = EntityUtils.getEntityClass(moduleName);
			// 如果moduleClass是target类的子类，则从新根据子类实例化
			if (target != null && target.getClass().isAssignableFrom(moduleClass) && target.getClass() != moduleClass) {
				realTarget = BeanUtils.instantiateClass(moduleClass);
			}
		}
		return super.createBinder(request, realTarget, objectName);
	}
}
