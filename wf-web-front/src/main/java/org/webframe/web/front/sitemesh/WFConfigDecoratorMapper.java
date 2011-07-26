/*
 * wf-web-front
 * Created on 2011-7-25-下午04:50:23
 */

package org.webframe.web.front.sitemesh;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.webframe.support.driver.resource.jar.JarResourceLoader;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;

/**
 * 更改ConfigLoader构造函数实例化ConfigLoader
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-25 下午04:50:23
 */
public class WFConfigDecoratorMapper extends AbstractDecoratorMapper {

	private ConfigLoader	configLoader	= null;

	/** Create new ConfigLoader using '/WEB-INF/decorators.xml' file. */
	public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
		super.init(config, properties, parent);
		try {
			String fileName = properties.getProperty("config", "classpath:decorators.xml");
			configLoader = new ConfigLoader(new JarResourceLoader(getClass()).getResource(fileName));
		} catch (Exception e) {
			throw new InstantiationException(e.toString());
		}
	}

	/** Retrieve {@link com.opensymphony.module.sitemesh.Decorator} based on 'pattern' tag. */
	public Decorator getDecorator(HttpServletRequest request, Page page) {
		String thisPath = request.getServletPath();
		// getServletPath() returns null unless the mapping corresponds to a servlet
		if (thisPath == null) {
			String requestURI = request.getRequestURI();
			if (request.getPathInfo() != null) {
				// strip the pathInfo from the requestURI
				thisPath = requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
			} else {
				thisPath = requestURI;
			}
		}
		String name = null;
		try {
			name = configLoader.getMappedName(thisPath);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		Decorator result = getNamedDecorator(request, name);
		return result == null ? super.getDecorator(request, page) : result;
	}

	/** Retrieve Decorator named in 'name' attribute. Checks the role if specified. */
	public Decorator getNamedDecorator(HttpServletRequest request, String name) {
		Decorator result = null;
		try {
			result = configLoader.getDecoratorByName(name);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		if (result == null || (result.getRole() != null && !request.isUserInRole(result.getRole()))) {
			// if the result is null or the user is not in the role
			return super.getNamedDecorator(request, name);
		} else {
			return result;
		}
	}
}
