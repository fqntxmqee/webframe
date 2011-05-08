/*
 * wf-core
 * Created on 2011-5-8-下午07:03:41
 */

package org.webframe.core.exception.datasource;

/**
 * 数据源异常
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-8 下午07:03:41
 */
public class DataSourceException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1555864813148348942L;

	public DataSourceException(String msg) {
		super(msg);
	}
}
