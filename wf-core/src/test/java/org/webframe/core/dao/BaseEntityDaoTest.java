
package org.webframe.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webframe.core.exception.entity.EntityException;
import org.webframe.core.module.loginfo.TLogInfo;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com>黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-24 下午09:45:08
 */
public class BaseEntityDaoTest extends BaseSpringTests {

	private IBaseEntityDao<TLogInfo>			baseEntityDao;

	private static Map<String, TLogInfo>	userMap			= new HashMap<String, TLogInfo>(8);

	private final String							testUserName	= "testuserdao";

	private volatile static boolean			init				= true;

	@Before
	public void clean() {
		if (init) {
			for (Object o : baseEntityDao.loadAll(TLogInfo.class)) {
				baseEntityDao.delete(o);
			}
			init = false;
		}
	}

	@Autowired
	public void setBaseEntityDao(IBaseEntityDao<TLogInfo> baseEntityDao) {
		this.baseEntityDao = baseEntityDao.getSubClassEntityDao(TLogInfo.class);
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseEntityDao#saveEntity(java.lang.Object)}.
	 */
	@Test
	public void testSaveEntity() {
		TLogInfo entity = new TLogInfo();
		entity.setName(testUserName);
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
		TLogInfo entity = new TLogInfo();
		entity.setName(testUserName);
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
		for (TLogInfo testUser : userMap.values()) {
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
		for (TLogInfo testUser : userMap.values()) {
			List<TLogInfo> entityList = baseEntityDao.findByExample(testUser);
			assertEquals("findByExample方法加载对象记录不全！", userMap.size(), entityList.size());
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
		List<TLogInfo> entityList;
		try {
			entityList = baseEntityDao.findByProperty(TLogInfo.class, "name",
				testUserName);
			assertEquals("findByProperty方法加载对象记录不全！", userMap.size(), entityList.size());
		} catch (EntityException e) {
			logger.info(e.getMessage());
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
			TLogInfo entity = baseEntityDao.findByUniqueProperty(TLogInfo.class,
				"id", userId);
			assertNotNull("findByUniqueProperty方法加载对象，未找到指定主键的对象错误！", entity);
		}
		TLogInfo entity = baseEntityDao.findByUniqueProperty(TLogInfo.class,
			"id", "1");
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
		for (TLogInfo testUser : userMap.values()) {
			baseEntityDao.deleteEntity(testUser);
			userMap.remove(testUser.getId());
			break;
		}
	}
}
