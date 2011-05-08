/*
 * wf-core
 * Created on 2011-5-8-下午07:18:58
 */

package org.webframe.core.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.webframe.core.TestBean;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-8 下午07:18:58
 */
public class BeanUtilsTest {

	/**
	 * Test method for
	 * {@link org.webframe.core.util.BeanUtils#setBeanProperties(java.lang.Object, java.util.Map)}.
	 */
	@Test
	public void testSetBeanProperties() {
		TestBean testBean = new TestBean();
		int intProp = 123;
		long longProp = 1234567891;
		double doubleProp = 12345.6789;
		boolean boolProp = true;
		float floatProp = 12345.6789f;
		byte byteProp = Byte.parseByte("1");
		String strProp = "123456789";
		Map<String, Object> propMap = new HashMap<String, Object>();
		propMap.put("intProp", intProp);
		propMap.put("longProp", longProp);
		propMap.put("doubleProp", doubleProp);
		propMap.put("boolProp", boolProp);
		propMap.put("floatProp", floatProp);
		propMap.put("byteProp", byteProp);
		propMap.put("strProp", strProp);
		BeanUtils.setBeanProperties(testBean, propMap);
		Assert.assertEquals("TestBean 属性(intProp)值应该为: " + intProp, intProp, testBean.getIntProp());
		Assert.assertEquals("TestBean 属性(longProp)值应该为: " + longProp, longProp, testBean.getLongProp());
		Assert.assertEquals("TestBean 属性(boolProp)值应该为: " + boolProp, boolProp, testBean.isBoolProp());
		Assert.assertEquals("TestBean 属性(byteProp)值应该为: " + byteProp, byteProp, testBean.getByteProp());
		Assert.assertEquals("TestBean 属性(strProp)值应该为: " + strProp, strProp, testBean.getStrProp());
		Assert.assertTrue("TestBean 属性(doubleProp)值应该为: ", longProp == testBean.getLongProp());
		Assert.assertTrue("TestBean 属性(floatProp)值应该为: ", floatProp == testBean.getFloatProp());
	}
}
