/*
 * wf-test
 * Created on 2011-12-31-下午01:38:42
 */

package org.webframe.test.web;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-31 下午01:38:42
 */
public class JettyInitException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8337550656193930708L;

	public JettyInitException(String msg) {
		super(msg);
	}

	public JettyInitException(String msg, Throwable e) {
		super(msg, e);
	}

	public JettyInitException(Throwable e) {
		this("Server init failed!", e);
	}
}
