/*
 * wf-support
 * Created on 2011-5-5-下午05:34:24
 */

package org.webframe.support.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-5-5 下午05:34:24
 */
public class ClassUtilsTest {

	/**
	 * Test method for {@link org.webframe.support.util.ClassUtils#isInJar(java.lang.Class)}.
	 */
	@Test
	public void testIsInJar() {
		Assert.assertTrue("org.webframe.support.util.ClassUtils类不应该在jar包中！", !ClassUtils.isInJar(getClass()));
		Assert.assertTrue("org.springframework.util.ClassUtils类应该在jar包中！",
			ClassUtils.isInJar(org.springframework.util.ClassUtils.class));
	}

	/**
	 * Test method for {@link org.webframe.support.util.ClassUtils#getResource(java.lang.Class)}.
	 */
	@Test
	public void testGetResource() {
		Resource resource = ClassUtils.getResource(getClass());
		Assert.assertTrue("org.webframe.support.util.ClassUtils类文件应该存在！", resource.exists());
	}
}
