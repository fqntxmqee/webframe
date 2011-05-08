
package org.webframe.core.exception.datasource;

/**
 * 类功能描述：
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: ConnectionPoolNotExistException.java,v 1.1.2.1 2010/04/22 09:12:35 huangguoqing Exp
 *          $ Create: 2010-4-20 上午10:07:22
 */
public class ConnectionPoolNotExistException extends DataSourceException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7356956903282614990L;

	public ConnectionPoolNotExistException(String poolType) {
		super(poolType + " 类型连接池配置不存在！");
	}
}
