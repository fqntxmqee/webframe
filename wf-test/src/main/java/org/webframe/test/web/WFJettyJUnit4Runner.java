/*
 * wf-test
 * Created on 2011-12-31-上午10:08:12
 */

package org.webframe.test.web;

import java.io.InputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 上午10:08:12
 */
public class WFJettyJUnit4Runner extends BlockJUnit4ClassRunner {

	static Server	server		= null;

	static boolean	tomcatRun	= false;

	public WFJettyJUnit4Runner(Class<?> klass) throws InitializationError, JettyInitException {
		super(klass);
		if (server == null && !tomcatRun) {
			server = initServer();
		}
	}

	protected Server initServer() throws JettyInitException {
		XmlConfiguration configuration = null;
		try {
			InputStream is = this.getClass().getResourceAsStream("/jetty.xml");
			configuration = new XmlConfiguration(is);
			return (Server) configuration.configure();
		} catch (Exception e) {
			throw new JettyInitException(e);
		}
	}

	@Override
	protected Object createTest() throws Exception {
		if (server != null && !server.isRunning()) {
			server.start();
		}
		return super.createTest();
	}
}
