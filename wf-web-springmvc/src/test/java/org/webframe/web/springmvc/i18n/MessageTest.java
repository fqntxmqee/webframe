
package org.webframe.web.springmvc.i18n;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.webframe.test.BaseSpringTests;

/**
 * 国际化消息测试
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-11 上午9:21:05
 * @version
 */
public class MessageTest extends BaseSpringTests {

	@Autowired
	private AbstractMessageSource	messageSource;

	private String[]					testCodes	= {
				"entity.saved", "entity.missing", "entity.deleted"};

	private String[]					testZhCNMsg	= {
				"成功保存。", "找不到此ID的对象。", "成功删除。"	};

	private String[]					testMsg		= {
				"Successfully saved.", "No Object found with this id.",
				"Successfully deleted."				};

	@Test
	public void testZhCNMessage() {
		Locale locale = new Locale("zh", "CN");
		for (int i = 0; i < testCodes.length; i++) {
			String code = testCodes[i];
			Assert.assertEquals(locale + " " + code + " message find error!",
				testZhCNMsg[i], messageSource.getMessage(code, null, locale));
		}
	}

	@Test
	public void testMessage() {
		Locale locale = new Locale("");
		for (int i = 0; i < testCodes.length; i++) {
			String code = testCodes[i];
			Assert.assertEquals(code + " message find error!", testMsg[i],
				messageSource.getMessage(code, null, locale));
		}
	}
}
