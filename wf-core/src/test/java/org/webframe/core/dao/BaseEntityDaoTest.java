
package org.webframe.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.module.testUser.TTestUser;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-24 下午09:45:08
 */
public class BaseEntityDaoTest extends BaseSpringTests {

	private IBaseEntityDao<TTestUser>		baseEntityDao;

	private static Map<String, TTestUser>	userMap			= new HashMap<String, TTestUser>(8);

	private String									testUserName	= "testuserdao";

	@Autowired
	public void setBaseEntityDao(IBaseEntityDao<TTestUser> baseEntityDao) {
		this.baseEntityDao = baseEntityDao.getSubClassEntityDao(TTestUser.class);
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseEntityDao#saveEntity(java.lang.Object)}.
	 */
	@Test
	public void testSaveEntity() {
		TTestUser entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		baseEntityDao.saveEntity(entity);
		userMap.put(entity.getId(), entity);
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseEntityDao#findEntity(java.io.Serializable)}.
	 */
	@Test
	public void testFindEntity() {
		for (String userId : userMap.keySet()) {
			try {
				baseEntityDao.findEntity(userId);
			} catch (EntityException e) {
				logger.info(e.getMessage());
				break;
			}
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#saveOrUpdateEntity(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testSaveOrUpdateEntity() {
		TTestUser entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		baseEntityDao.saveOrUpdateEntity(entity);
		userMap.put(entity.getId(), entity);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#updateEntity(org.webframe.core.model.BaseEntity)} .
	 */
	@Test
	public void testUpdateEntity() {
		for (TTestUser testUser : userMap.values()) {
			testUser.setModifyTime(DateUtils.getStandTime());
			baseEntityDao.updateEntity(testUser);
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#findByExample(org.webframe.core.model.BaseEntity)}
	 * .
	 */
	@Test
	public void testFindByExample() {
		for (TTestUser testUser : userMap.values()) {
			List<TTestUser> entityList = baseEntityDao.findByExample(testUser);
			assertEquals("findByExample方法加载对象记录不全！", entityList.size(), userMap.size());
		}
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseEntityDao#findAll()}.
	 */
	@Test
	public void testFindAll() {
		try {
			baseEntityDao.findAll();
		} catch (EntityException e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#findByProperty(java.lang.Class, java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testFindByPropertyClassOfTStringObject() {
		List<TTestUser> entityList;
		try {
			entityList = baseEntityDao.findByProperty(TTestUser.class, "name", testUserName);
			assertEquals("findByProperty方法加载对象记录不全！", entityList.size(), userMap.size());
		} catch (EntityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#findByUniqueProperty(java.lang.Class, java.lang.String, java.lang.Object)}
	 * .
	 * 
	 * @throws EntityException
	 */
	@Test
	public void testFindByUniquePropertyClassOfTStringObject() throws EntityException {
		for (String userId : userMap.keySet()) {
			TTestUser entity = baseEntityDao.findByUniqueProperty(TTestUser.class, "id", userId);
			assertNotNull("findByUniqueProperty方法加载对象，未找到指定主键的对象错误！", entity);
		}
		TTestUser entity = baseEntityDao.findByUniqueProperty(TTestUser.class, "id", "1");
		assertNull("findByUniqueProperty方法加载对象，找到指定主键的对象错误！", entity);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#findByProperty(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testFindByPropertyStringObject() {
		try {
			baseEntityDao.findByProperty("name", testUserName);
		} catch (EntityException e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#findByUniqueProperty(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testFindByUniquePropertyStringObject() {
		for (String userId : userMap.keySet()) {
			try {
				baseEntityDao.findByUniqueProperty("id", userId);
			} catch (EntityException e) {
				logger.info(e.getMessage());
				break;
			}
		}
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseEntityDao#deleteEntity(java.io.Serializable)}
	 * .
	 */
	@Test
	public void testDeleteEntitySerializable() {
		for (String id : userMap.keySet()) {
			try {
				baseEntityDao.findByUniqueProperty("id", id);
			} catch (EntityException e) {
				logger.info(e.getMessage());
				break;
			}
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseEntityDao#deleteEntity(org.webframe.core.model.BaseEntity)} .
	 */
	@Test
	public void testDeleteEntityT() {
		for (TTestUser testUser : userMap.values()) {
			baseEntityDao.deleteEntity(testUser);
			userMap.remove(testUser.getId());
			break;
		}
	}
}
