
package org.webframe.struts.i18n;

import java.util.Locale;

import junit.framework.Assert;

import org.apache.struts.util.MessageResources;
import org.junit.Test;
import org.webframe.test.BaseSpringTests;

/**
 * 国际化消息测试
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆</a>
 * @since 2012-5-11 上午10:04:26
 * @version
 */
public class MessageTest extends BaseSpringTests {

	private MessageResources	resources	= null;

	private String[]				testCodes	= {
				"entity.saved", "entity.missing", "entity.deleted"};

	private String[]				testZhCNMsg	= {
				"成功保存。", "找不到此ID的对象。", "成功删除。"};

	private String[]				testMsg		= {
				"Successfully saved.", "No Object found with this id.",
				"Successfully deleted."			};

	public MessageTest() {
		MultiPropertyMessageResourcesFactory.setFactoryClass(MultiPropertyMessageResourcesFactory.class.getName());
		resources = MultiPropertyMessageResourcesFactory.createFactory()
			.createResources("/i18n/messages");
	}

	@Test
	public void testZhCNMessage() {
		Locale locale = new Locale("zh", "CN");
		for (int i = 0; i < testCodes.length; i++) {
			String code = testCodes[i];
			Assert.assertEquals(locale + " " + code + " message find error!",
				testZhCNMsg[i], resources.getMessage(locale, code, null));
		}
	}

	@Test
	public void testMessage() {
		Locale locale = new Locale("");
		for (int i = 0; i < testCodes.length; i++) {
			String code = testCodes[i];
			Assert.assertEquals(code + " message find error!", testMsg[i],
				resources.getMessage(locale, code, null));
		}
	}
}
