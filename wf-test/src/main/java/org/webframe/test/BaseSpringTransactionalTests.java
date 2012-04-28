/*
 * wf-test
 * Created on 2011-5-5-下午04:35:27
 */

package org.webframe.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 基于spring配置环境的测试用例基类,并提供事务支持
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午04:35:27
 */
@RunWith(WFSpringJUnit4Runner.class)
@ContextConfiguration(locations = {
	"wf-test.xml"})
public class BaseSpringTransactionalTests extends AbstractTransactionalJUnit4SpringContextTests {

}
