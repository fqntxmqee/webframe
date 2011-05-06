
package org.webframe.support.driver;

/**
 * webframe框架模块插件驱动接口，每一个分包(jar)中需要有一个实现该接口的实现类。
 * 需要实现的方法有getEntityLocation()和getViewTempletLocation()； 实现类也可以继承抽象类AbstractModulePluginDriver
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-18 下午06:47:35
 */
public interface ModulePluginDriver {

	/**
	 * 获取数据库model Bean相对于该实现类的相对路径，或者相对于该jar的相对路径。
	 * 例如：我们的模块插件驱动实现类com.berheley.wf.test.TestModulePluginDriver， model
	 * Bean的位置com.berheley.wf.test.Test，那么该实现类getEntityLocation()
	 * 方法应该返回"/com/berheley/wf/test"或者"*"字符串；
	 * 
	 * @return
	 * @author 黄国庆 2011-4-2 上午10:53:01
	 */
	String getEntityLocation();

	/**
	 * 获取model页面freemarker模板的路径，例如：我们的模块插件驱动实现类 com.berheley.wf.test.TestModulePluginDriver， model
	 * freemarker模板的位置com/berheley/wf/test/new.ftl，那么该实现类
	 * getViewTempletLocation()方法应该返回"/com/berheley/wf"。
	 * 
	 * @return
	 * @author 黄国庆 2011-4-2 上午11:13:24
	 */
	String getViewTempletLocation();

	/**
	 * 支持", ; \t \n"字符串表示多路径，例如：返回值为/js,/jsp,/style，表示模块插件包 根目录下的js、jsp、style三个目录中的资源
	 * 
	 * @return
	 * @author 黄国庆 2011-4-5 下午10:14:40
	 */
	String getWebSourcesLocation();

	/**
	 * 不支持", ; \t \n"字符串表示多路径
	 * 
	 * @return
	 * @author 黄国庆 2011-4-5 下午10:17:51
	 */
	String getSpringContextLocation();

	String getModuleName();
}
