
package org.webframe.core.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.core.exception.ServiceException;
import org.webframe.core.exception.entity.EntityNotExistException;
import org.webframe.core.exception.entity.EntityNullException;
import org.webframe.core.module.testUser.TTestUser;
import org.webframe.core.util.DateHelper;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-24 上午10:37:40
 */
public class BaseServiceTest extends BaseSpringTests {

	@Autowired
	private IBaseService							baseService;

	private static Map<String, TTestUser>	userMap			= new HashMap<String, TTestUser>(8);

	private String									testUserName	= "testuser";

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#save(org.webframe.core.model.BaseEntity)} .
	 */
	@Test
	public void testSave() {
		TTestUser testUser = null;
		try {
			// baseService.save(testUser);
		} catch (ServiceException e) {
			assertTrue("业务对象为null，没有抛出EntityNullException异常！", e instanceof EntityNullException);
		}
		testUser = new TTestUser();
		testUser.setName(testUserName);
		testUser.setPassword("password");
		testUser.setEnabled(true);
		testUser.setCreateTime(DateHelper.getStandTime());
		baseService.save(testUser);
		userMap.put(testUser.getId(), testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#saveOrUpdate(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testSaveOrUpdate() {
		TTestUser testUser = null;
		try {
			// baseService.saveOrUpdate(testUser);
		} catch (ServiceException e) {
			assertTrue("业务对象为null，没有抛出EntityNullException异常！", e instanceof EntityNullException);
		}
		testUser = new TTestUser();
		testUser.setName(testUserName);
		testUser.setPassword("password");
		testUser.setEnabled(true);
		testUser.setCreateTime(DateHelper.getStandTime());
		baseService.saveOrUpdate(testUser);
		userMap.put(testUser.getId(), testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#update(org.webframe.core.model.BaseEntity)} .
	 */
	@Test
	public void testUpdate() {
		try {
			// baseService.saveOrUpdate(null);
		} catch (ServiceException e) {
			assertTrue("业务对象为null，没有抛出EntityNullException异常！", e instanceof EntityNullException);
		}
		for (TTestUser testUser : userMap.values()) {
			testUser.setModifyTime(DateHelper.getStandTime());
			baseService.update(testUser);
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#get(java.lang.Class, java.io.Serializable)} .
	 */
	@Test
	public void testGet() {
		for (String userId : userMap.keySet()) {
			TTestUser testUser = (TTestUser) baseService.get(TTestUser.class, userId);
			assertNotNull("get方法加载对象，如果指定主键的id不存在，返回null！", testUser);
		}
		TTestUser testUser = (TTestUser) baseService.get(TTestUser.class, "1");
		assertNull("get方法加载对象，如果指定主键的id不存在，返回null！", testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#load(java.lang.Class, java.io.Serializable)} .
	 */
	@Test
	public void testLoad() {
		for (String userId : userMap.keySet()) {
			TTestUser testUser = (TTestUser) baseService.load(TTestUser.class, userId);
			assertNotNull("load方法加载对象，如果指定主键的id不存在，不返回null，返回代理，在使用的时候会抛出异常！", testUser);
		}
		TTestUser testUser = (TTestUser) baseService.load(TTestUser.class, "1");
		assertNotNull("load方法加载对象，如果指定主键的id不存在，不返回null，返回代理，在使用的时候会抛出异常！", testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#delete(org.webframe.core.model.BaseEntity)} .
	 */
	@Test
	public void testDelete() {
		String userId = null;
		for (TTestUser testUser : userMap.values()) {
			userId = testUser.getId();
			baseService.delete(testUser);
			userMap.remove(userId);
			break;
		}
		assertNull("删除id为：" + userId + "的对象失败！", baseService.get(TTestUser.class, userId));
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseService#delete(java.lang.Class, java.io.Serializable)} .
	 */
	@Test
	public void testDeleteClass() {
		String userId = null;
		try {
			// baseService.delete(TTestUser.class, "1");
		} catch (ServiceException e) {
			assertTrue("业务对象ID为1的对象不存在，没有抛出EntityNotExistException异常！", e instanceof EntityNotExistException);
		}
		for (TTestUser testUser : userMap.values()) {
			userId = testUser.getId();
			baseService.delete(TTestUser.class, userId);
			userMap.remove(userId);
		}
		assertNull("删除id为：" + userId + "的对象失败！", baseService.get(TTestUser.class, userId));
	}
}
