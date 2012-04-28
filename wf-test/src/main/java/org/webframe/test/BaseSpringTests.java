
package org.webframe.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 基于spring配置环境的测试用例基类
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2010-12-24
 *          下午09:09:44
 */
@RunWith(WFSpringJUnit4Runner.class)
@ContextConfiguration(locations = {
	"wf-test.xml"})
public class BaseSpringTests extends AbstractJUnit4SpringContextTests {
}
