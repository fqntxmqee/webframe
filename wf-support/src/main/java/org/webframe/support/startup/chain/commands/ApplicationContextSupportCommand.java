
package org.webframe.support.startup.chain.commands;

import org.apache.commons.chain.Context;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-6-7 下午3:47:52
 * @version
 */
public abstract class ApplicationContextSupportCommand
			extends
				AbstractStopWatchCommand {

	public static final String	SPRING_CONTEXT_CLASS		= "spring_context_class";

	public static final String	SPRING_CONTEXT_CURRENT	= "spring_context_current";

	@SuppressWarnings("unchecked")
	/**
	 * 根据chain上下文中的spring上下文class(SPRING_CONTEXT_CLASS变量)，初始化spring上下文，该上下文没有fresh
	 * 
	 * @param context
	 * @author 黄国庆 2012-6-14 下午4:28:30
	 */
	protected void initApplicationContext(Context context) {
		Class<AbstractApplicationContext> contextClass = (Class<AbstractApplicationContext>) context.get(SPRING_CONTEXT_CLASS);
		AbstractApplicationContext currentContext = BeanUtils.instantiateClass(contextClass);
		currentContext.setParent(getCurrentApplicationContext(context));
		putApplicationContext(context, currentContext);
	}

	protected void initApplicationContext(String[] locations, Context context) {
		AbstractApplicationContext currentContext = new ClassPathXmlApplicationContext(locations, true, getCurrentApplicationContext(context));
		putApplicationContext(context, currentContext);
	}

	@SuppressWarnings("unchecked")
	protected void putApplicationContext(Context context, AbstractApplicationContext currentContext) {
		context.put(SPRING_CONTEXT_CURRENT, currentContext);
	}

	protected ApplicationContext getCurrentApplicationContext(Context context) {
		return (ApplicationContext) context.get(SPRING_CONTEXT_CURRENT);
	}
}