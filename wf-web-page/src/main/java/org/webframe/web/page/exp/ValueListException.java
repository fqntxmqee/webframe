/*
 * wf-web-page
 * Created on 2011-12-20-下午01:09:54
 */

package org.webframe.web.page.exp;

/**
 * valuelist 异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-12-20 下午01:09:54
 */
public class ValueListException extends RuntimeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 471111457227305972L;

	public ValueListException(String adapter, Throwable e) {
		super("Adapter Name(" + adapter + ")", e);
	}
}
