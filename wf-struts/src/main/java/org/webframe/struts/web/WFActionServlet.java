
package org.webframe.struts.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.apache.commons.digester.Digester;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import org.webframe.struts.util.StrutsConfigUtils;
import org.xml.sax.SAXException;

/**
 * 继承 Struts ActionServlet类，重写parseModuleConfigFile方法，
 * 支持配置文件路径Pattern配置例如：classpath:/struts/struts-config*.xml
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 上午09:36:09
 */
public class WFActionServlet extends ActionServlet {

	/**
	 * 
	 */
	private static final long							serialVersionUID	= -4701138526978668909L;

	private PathMatchingResourcePatternResolver	rpr					= null;

	@Override
	public void init() throws ServletException {
		rpr = new ServletContextResourcePatternResolver(getServletContext());
		super.init();
	}

	@Override
	protected void initOther() throws ServletException {
		config = getServletConfig().getInitParameter("config");
		super.initOther();
	}

	@Override
	protected ModuleConfig initModuleConfig(String prefix, String paths) throws ServletException {
		if (paths == null || "".equals(paths)) {
			// Parse the configuration for this module
			ModuleConfigFactory factoryObject = ModuleConfigFactory.createFactory();
			ModuleConfig config = factoryObject.createModuleConfig(prefix);
			// Configure the Digester instance we will use
			Digester digester = initConfigDigester();
			digester.push(config);
			this.parseModuleConfigFile(digester, null);
			getServletContext().setAttribute(Globals.MODULE_KEY + config.getPrefix(), config);
			// Force creation and registration of DynaActionFormClass instances
			// for all dynamic form beans we wil be using
			FormBeanConfig fbs[] = config.findFormBeanConfigs();
			for (int i = 0; i < fbs.length; i++) {
				if (fbs[i].getDynamic()) {
					fbs[i].getDynaActionFormClass();
				}
			}
			return config;
		} else {
			return super.initModuleConfig(prefix, paths);
		}
	}

	@Override
	protected void parseModuleConfigFile(Digester digester, String path) throws UnavailableException {
		try {
			Resource[] resources;
			if (path == null) {
				resources = StrutsConfigUtils.getStrutsConfigResources();
			} else {
				resources = rpr.getResources(path);
			}
			// 获取digester Root，用于多次解析文件
			Object root = digester.getRoot();
			for (int i = 0; i < resources.length; i++) {
				// digester多次解析文件时，需要push root
				if (i > 0) digester.push(root);
				InputStream input = resources[i].getInputStream();
				try {
					digester.parse(input);
				} finally {
					input.close();
				}
			}
		} catch (SAXException e) {
			String msg = internal.getMessage("configParse", path);
			log.error(msg, e);
			throw new UnavailableException(msg);
		} catch (IOException e) {
			throw new UnavailableException(e.getMessage());
		}
	}
}
