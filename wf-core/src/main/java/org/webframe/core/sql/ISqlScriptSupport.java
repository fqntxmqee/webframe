/*
 * wf-core
 * Created on 2012-2-7-上午08:49:10
 */

package org.webframe.core.sql;

import org.webframe.support.driver.ModulePluginDriver;

/**
 * 模块SqlScript初始化接口
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @since 2012-2-7 上午08:49:10
 * @version
 */
public interface ISqlScriptSupport extends ModulePluginDriver {

	/**
	 * 不支持", ; \t \n"字符串表示多路径
	 * 
	 * @return
	 * @author 黄国庆 2012-2-7 上午08:51:15
	 */
	String getSqlScriptLocation();
}
