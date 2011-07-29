/*
 * wf-core
 * Created on 2011-7-29-下午08:58:21
 */

package org.webframe.core.util;

import org.junit.Test;
import org.springframework.util.Assert;
import org.webframe.core.module.testUser.TTestUser;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-7-29 下午08:58:21
 */
public class ReflectionUtilsTest {

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#invokeGetterMethod(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test
	public void testInvokeGetterMethod() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#invokeSetterMethod(java.lang.Object, java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testInvokeSetterMethodObjectStringObject() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#invokeSetterMethod(java.lang.Object, java.lang.String, java.lang.Object, java.lang.Class)}
	 * .
	 */
	@Test
	public void testInvokeSetterMethodObjectStringObjectClassOfQ() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#getFieldValue(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetFieldValue() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#setFieldValue(java.lang.Object, java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testSetFieldValue() {
		String name = "testUser.testUser.name!";
		String property = "testUser.testUser.name";
		TTestUser testUser = new TTestUser();
		TTestUser testUser1 = new TTestUser(testUser);
		testUser = new TTestUser(testUser1);
		ReflectionUtils.setFieldValue(testUser, property, name);
		Object obj = ReflectionUtils.getFieldValue(testUser, property);
		Assert.isTrue(name.equals(obj), "设置" + property + "属性失败！");
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#getAccessibleField(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAccessibleField() {
		TTestUser testUser = new TTestUser();
		testUser.addList("testUser.testUser.list");
		TTestUser testUser1 = new TTestUser(testUser);
		testUser = new TTestUser(testUser1);
		Object obj = ReflectionUtils.getFieldValue(testUser, "testUser.testUser.list");
		Assert.notNull(obj, "获取testUser.testUser.list属性失败！");
	}

	/**
	 * Test method for {@link org.webframe.core.util.ReflectionUtils#invokeMethod(java.lang.Object,
	 * java.lang.String, java.lang.Class<?>[], java.lang.Object[])}.
	 */
	@Test
	public void testInvokeMethodObjectStringClassOfQArrayObjectArray() {
		// TODO
	}

	/**
	 * Test method for {@link
	 * org.webframe.core.util.ReflectionUtils#getAccessibleMethod(java.lang.Object, java.lang.String,
	 * java.lang.Class<?>[])}.
	 */
	@Test
	public void testGetAccessibleMethod() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#getSuperClassGenricType(java.lang.Class)}.
	 */
	@Test
	public void testGetSuperClassGenricTypeClassOfQ() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#getSuperClassGenricType(java.lang.Class, int)}.
	 */
	@Test
	public void testGetSuperClassGenricTypeClassOfQInt() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#convertElementPropertyToList(java.util.Collection, java.lang.String)}
	 * .
	 */
	@Test
	public void testConvertElementPropertyToList() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#convertElementPropertyToString(java.util.Collection, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testConvertElementPropertyToString() {
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.util.ReflectionUtils#convertReflectionExceptionToUnchecked(java.lang.Exception)}
	 * .
	 */
	@Test
	public void testConvertReflectionExceptionToUnchecked() {
		// TODO
	}
}
