/*
 * wf-struts
 * Created on 2011-5-30-上午10:27:07
 */

package org.webframe.struts;

/**
 * 支持获取Struts配置文件路径
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-30 上午10:27:07
 */
public interface IStrutsSupport {

	/**
	 * 指定Struts配置文件的路径
	 * 
	 * @return
	 * @author 黄国庆 2011-5-30 上午10:31:03
	 */
	String getStrutsConfigLocation();
}
