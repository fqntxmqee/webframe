package org.webframe.struts;

import org.webframe.support.driver.AbstractModulePluginDriver;


/**
 * 指定默认struts根目录文件夹，供有提供struts配置的模块使用
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-10 下午9:11:45
 * @version
 */
public abstract class AbstractStrutsModulePluginDriver
			extends
				AbstractModulePluginDriver implements IStrutsSupport {

	/**
	 * 指定默认struts根目录文件夹："/struts"
	 * 
	 * @see org.webframe.struts.IStrutsSupport#getStrutsConfigLocation()
	 */
	@Override
	public String getStrutsConfigLocation() {
		return "/struts";
	}
}
