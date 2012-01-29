/*
 * wf-test
 * Created on 2012-1-27-下午05:53:45
 */

package org.webframe.test.web;

import java.net.URL;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.providers.ContextProvider;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * WFJettyContextDeployer 扩展支持jar包中的xml context加载
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2012-1-27 下午05:53:45
 */
public class WFJettyContextProvider extends ContextProvider {

	@Override
	public ContextHandler createContextHandler(App app) throws Exception {
		String filename = app.getOriginId();
		if (filename != null && filename.startsWith("jar:")) {
			return jarResourceXml(new URL(filename));
		}
		return super.createContextHandler(app);
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		Resource resource = getMonitoredDirResource();
		if (resource instanceof JarResource) {
			fileAdded(resource.getName());
		}
	}

	protected ContextHandler jarResourceXml(URL url) throws Exception {
		XmlConfiguration xmlc = new XmlConfiguration(url);
		xmlc.getIdMap().put("Server", getDeploymentManager().getServer());
		if (getConfigurationManager() != null) xmlc.getProperties().putAll(getConfigurationManager().getProperties());
		return (ContextHandler) xmlc.configure();
	}
}
