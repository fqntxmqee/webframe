
package org.webframe.core.exception.datasource;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: ConnectionPoolChangeException.java,v 1.1.2.1 2010/04/22 09:12:35 huangguoqing Exp $
 *          Create: 2010-4-20 上午10:41:35
 */
public class ConnectionPoolChangeException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7911256590926123658L;

	public ConnectionPoolChangeException(String poolType) {
		super(poolType + " 类型连接池切换失败！");
	}
}
