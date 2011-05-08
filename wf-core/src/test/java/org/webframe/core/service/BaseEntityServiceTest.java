
package org.webframe.core.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.module.testUser.TTestUser;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-25 上午11:07:07
 */
public class BaseEntityServiceTest extends BaseSpringTests {

	@Autowired
	private IBaseEntityService<TTestUser>	baseEntityService;

	private static Map<String, TTestUser>	userMap			= new HashMap<String, TTestUser>(8);

	private String									testUserName	= "testuser";

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#saveEntity(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testSaveEntity() {
		TTestUser testUser = new TTestUser();
		testUser.setName(testUserName);
		testUser.setPassword("password");
		testUser.setEnabled(true);
		testUser.setCreateTime(DateUtils.getStandTime());
		baseEntityService.saveEntity(testUser);
		userMap.put(testUser.getId(), testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#saveOrUpdateEntity(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testSaveOrUpdateEntity() {
		TTestUser testUser = new TTestUser();
		testUser.setName(testUserName);
		testUser.setPassword("password");
		testUser.setEnabled(true);
		testUser.setCreateTime(DateUtils.getStandTime());
		baseEntityService.saveOrUpdateEntity(testUser);
		userMap.put(testUser.getId(), testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#updateEntity(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testUpdateEntity() {
		for (TTestUser testUser : userMap.values()) {
			testUser.setModifyTime(DateUtils.getStandTime());
			baseEntityService.updateEntity(testUser);
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#findEntity(java.lang.Class, java.io.Serializable)}
	 * .
	 */
	@Test
	public void testFindEntityClassOfTSerializable() {
		for (String userId : userMap.keySet()) {
			TTestUser testUser = (TTestUser) baseEntityService.findEntity(TTestUser.class, userId);
			assertNotNull("findEntity方法加载对象，如果指定主键的id不存在，返回null！", testUser);
		}
		TTestUser testUser = (TTestUser) baseEntityService.findEntity(TTestUser.class, "1");
		assertNull("findEntity方法加载对象，如果指定主键的id不存在，返回null！", testUser);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#findEntity(java.io.Serializable)}.
	 */
	@Test
	public void testFindEntitySerializable() {
		try {
			// baseEntityService.findEntity("1");
			// TODO 虽然捕获了异常，但事务也回滚了
		} catch (EntityException e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.service.BaseEntityService#deleteEntity(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testDeleteEntity() {
		String userId = null;
		for (TTestUser testUser : userMap.values()) {
			userId = testUser.getId();
			baseEntityService.deleteEntity(testUser);
			userMap.remove(userId);
			break;
		}
		assertNull("删除id为：" + userId + "的对象失败！", baseEntityService.findEntity(TTestUser.class, userId));
	}
}
