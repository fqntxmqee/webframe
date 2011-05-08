
package org.webframe.core.exception.datasource;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: DataBaseChangeException.java,v 1.1.2.1 2010/04/22 09:12:35 huangguoqing Exp $
 *          Create: 2010-4-20 上午10:41:57
 */
public class DataBaseChangeException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7963766984313425418L;

	public DataBaseChangeException(String databaseType) {
		super(databaseType + " 类型数据库切换失败！");
	}
}
