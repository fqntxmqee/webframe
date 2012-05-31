package org.webframe.test;

import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.executor.FlowExecutorImpl;
import org.springframework.webflow.test.execution.AbstractFlowExecutionTests;


/**
 * spring webflow 测试用例继承基类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-16 下午1:43:02
 * @version
 */
@RunWith(WFSpringJUnit4Runner.class)
@ContextConfiguration(locations = {
	"wf-test.xml"})
public abstract class BaseFlowTests extends AbstractFlowExecutionTests {

	private FlowExecutorImpl	flowExecutor	= null;

	/**
	 * 指定webflowId
	 * 
	 * @return
	 * @author 黄国庆 2012-5-17 下午9:52:08
	 */
	protected abstract String getFlowId();

	protected String getFlowExecutorName() {
		return "flowExecutor";
	}

	protected FlowExecutorImpl getFlowExecutor() {
		if (flowExecutor != null) {
			return flowExecutor;
		}
		ApplicationContext ac = TestApplicationContext.getApplicationContext();
		if (ac != null) {
			return (FlowExecutorImpl) ac.getBean(getFlowExecutorName());
		}
		throw new ApplicationContextException("ApplicationContext not exist!");
	}

	protected FlowDefinitionLocator getExecutionRepository() {
		return getFlowExecutor().getDefinitionLocator();
	}

	@Override
	protected FlowDefinition getFlowDefinition() {
		Assert.notNull(getFlowId(), "flow id 不能为null！");
		if (flowExecutor == null) {
			flowExecutor = getFlowExecutor();
		}
		return getExecutionRepository().getFlowDefinition(getFlowId());
	}
}
