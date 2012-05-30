
package org.webframe.core.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.webframe.core.module.testUser.TTestUser;
import org.webframe.core.util.DateUtils;
import org.webframe.test.BaseSpringTransactionalTests;

/**
 * @author <a href="mailto:guoqing.huang@foxmail.com">黄国庆 </a>
 * @version $Id: codetemplates.xml,v 1.1 2009/09/07 08:48:12 Exp $ Create: 2011-3-22 下午02:32:41
 */
@SuppressWarnings("unchecked")
@TransactionConfiguration(defaultRollback = false)
public class BaseDaoTest extends BaseSpringTransactionalTests {

	@Autowired
	private IBaseDao											baseDao;

	private volatile static Map<String, TTestUser>	userMap			= new HashMap<String, TTestUser>(8);

	private final String										testUserName	= "testuser";

	private volatile static boolean						init				= true;

	synchronized static Map<String, TTestUser> getUserMap() {
		return userMap;
	}

	@Before
	public void clean() {
		if (init) {
			for (Object o : baseDao.loadAll(TTestUser.class)) {
				baseDao.delete(o);
			}
			init = false;
		}
	}

	@Test
	public void testNull() {
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#saveOrUpdate(java.lang.Object)}.
	 */
	@Test
	public void testSaveOrUpdateObject() {
		TTestUser entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		baseDao.saveOrUpdate(entity);
		getUserMap().put(entity.getId(), entity);
		assertSame("saveOrUpdate错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#save(java.lang.Object)}.
	 */
	@Test
	public void testSave() {
		TTestUser entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		baseDao.save(entity);
		getUserMap().put(entity.getId(), entity);
		assertSame("save错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#saveOrUpdate(java.util.Collection)}.
	 */
	@Test
	public void testSaveOrUpdateCollectionOfQ() {
		List<TTestUser> entityList = new ArrayList<TTestUser>();
		TTestUser entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		entityList.add(entity);
		entity = new TTestUser();
		entity.setName(testUserName);
		entity.setPassword("password");
		entity.setEnabled(true);
		entity.setCreateTime(DateUtils.getStandTime());
		entityList.add(entity);
		baseDao.saveOrUpdate(entityList);
		for (TTestUser testUser : entityList) {
			getUserMap().put(testUser.getId(), testUser);
		}
		assertSame("saveOrUpdate错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#update(java.lang.Object)}.
	 */
	@Test
	public void testUpdateObject() {
		for (TTestUser testUser : getUserMap().values()) {
			testUser.setModifyTime(DateUtils.getStandTime());
			baseDao.update(testUser);
		}
		assertSame("update错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#update(java.lang.String)}.
	 */
	@Test
	public void testUpdateString() {
		String updateHql = "update TTestUser set modifyTime='" + DateUtils.getStandTime() + "'";
		baseDao.update(updateHql);
		assertSame("update错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#refresh(java.lang.Object)}.
	 */
	@Test
	public void testRefresh() {
		for (TTestUser testUser : getUserMap().values()) {
			String modifyTime = testUser.getModifyTime();
			baseDao.refresh(testUser);
			assertNotSame("hql语句update数据库记录，内存中的数据应该与数据库数据不一致！", modifyTime, testUser.getModifyTime());
		}
		assertSame("refresh错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#load(java.lang.Class, java.io.Serializable)}.
	 */
	@Test
	public void testLoad() {
		for (String userId : getUserMap().keySet()) {
			TTestUser testUser = (TTestUser) baseDao.load(TTestUser.class, userId);
			assertNotNull("load方法加载对象，如果指定主键的id不存在，不返回null，返回代理，在使用的时候会抛出异常！", testUser);
		}
		TTestUser testUser = (TTestUser) baseDao.load(TTestUser.class, "1");
		assertNotNull("load方法加载对象，如果指定主键的id不存在，不返回null，返回代理，在使用的时候会抛出异常！", testUser);
		assertSame("load错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#loadAll(java.lang.Class)}.
	 */
	@Test
	public void testLoadAll() {
		List<TTestUser> list = (List<TTestUser>) baseDao.loadAll(TTestUser.class);
		assertSame("loadAll方法加载数据不完整！", list.size(), getUserMap().size());
		for (TTestUser testUser : list) {
			if (!getUserMap().containsKey(testUser.getId())) {
				fail("loadAll方法加载数据不完整！");
			}
		}
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#get(java.lang.Class, java.io.Serializable)}.
	 */
	@Test
	public void testGet() {
		for (String userId : getUserMap().keySet()) {
			TTestUser testUser = (TTestUser) baseDao.get(TTestUser.class, userId);
			assertNotNull("get方法加载对象，如果指定主键的id不存在，返回null！", testUser);
		}
		TTestUser testUser = (TTestUser) baseDao.get(TTestUser.class, "1");
		assertNull("get方法加载对象，如果指定主键的id不存在，返回null！", testUser);
		assertSame("get错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#loadNotLazy(java.lang.Class, java.io.Serializable)} .
	 */
	@Test
	public void testLoadNotLazy() {
		for (String userId : getUserMap().keySet()) {
			TTestUser testUser = (TTestUser) baseDao.loadNotLazy(TTestUser.class, userId);
			assertNotNull("loadNotLazy方法加载对象，是使用get方法加载，没有指定记录，返回null！", testUser);
		}
		assertSame("loadNotLazy错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#iterate(java.lang.String)}.
	 */
	@Test
	public void testIterate() {
		String queryHql = "from TTestUser";
		Iterator<TTestUser> iter = (Iterator<TTestUser>) baseDao.iterate(queryHql);
		while (iter.hasNext()) {
			TTestUser testUser = iter.next();
			assertNotNull("iterate方法加载对象，会级联查询，不延迟加载！", testUser);
		}
		assertSame("iterate错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#queryUniqueObject(java.lang.String, java.lang.Class)} .
	 */
	@Test
	public void testQueryUniqueObject() {
		String queryHql = "from TTestUser where name='" + testUserName + "'";
		String id = "";
		try {
			baseDao.queryUniqueObject(queryHql, TTestUser.class);
		} catch (NonUniqueObjectException e) {
			assertSame("name为：" + testUserName + "的记录不唯一！", e.getEntityName(), TTestUser.class.getName());
		}
		try {
			for (String userId : getUserMap().keySet()) {
				id = userId;
				queryHql = "from TTestUser where id='" + id + "'";
				baseDao.queryUniqueObject(queryHql, TTestUser.class);
			}
		} catch (NonUniqueObjectException e) {
			fail("id为：" + id + "的记录不唯一！");
		}
		assertSame("queryUniqueObject错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#query(java.lang.String)}.
	 */
	@Test
	public void testQueryString() {
		String queryHql = "from TTestUser";
		List<?> list = baseDao.query(queryHql);
		assertSame("query方法查询数据不完整！", list.size(), getUserMap().size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#query(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])}
	 * .
	 */
	@Test
	public void testQueryStringObjectArrayTypeArray() {
		String queryHql = "from TTestUser u where u.name = ? and u.enabled is ?";
		Object[] values = {
					testUserName, true};
		Type[] types = {
					Hibernate.STRING, Hibernate.BOOLEAN};
		List<?> list = baseDao.query(queryHql, values, types);
		assertSame("query方法查询数据不完整！", getUserMap().size(), list.size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#query(java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public void testQueryStringInteger() {
		String queryHql = "from TTestUser";
		int max = 3;
		List<?> list = baseDao.query(queryHql, max);
		assertTrue("query方法查询记录条数错误！", list.size() <= max);
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#queryByNamedParam(java.lang.String, java.lang.String[], java.lang.Object[])}
	 * .
	 */
	@Test
	public void testQueryByNamedParam() {
		String queryHql = "from TTestUser u where u.name = :name and u.enabled is :enabled";
		String[] params = {
					"name", "enabled"};
		Object[] values = {
					testUserName, true};
		List<?> list = baseDao.queryByNamedParam(queryHql, params, values);
		assertSame("queryByNamedParam方法查询数据不完整！", getUserMap().size(), list.size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#findBySql(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])}
	 * .
	 */
	@Test
	public void testFindBySql() {
		String querySql = "select * from T_TEST_USER u where u.NAME_ = ? and u.ENABLED_ = ?";
		Object[] values = {
					testUserName, true};
		Type[] types = {
					Hibernate.STRING, Hibernate.BOOLEAN};
		List<?> list = baseDao.findBySql(querySql, values, types);
		assertSame("findBySql方法查询数据不完整！", getUserMap().size(), list.size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#findBySql(java.lang.String, int)}.
	 */
	@Test
	public void testFindBySqlStringInt() {
		String findSql = "select * from T_TEST_USER";
		int max = 3;
		List<?> list = baseDao.findBySql(findSql, max);
		assertTrue("findBySql方法查询数据不完整！", list.size() <= max);
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#findBySql(java.lang.String)}.
	 */
	@Test
	public void testFindBySqlString() {
		String findSql = "select * from T_TEST_USER";
		List<?> list = baseDao.findBySql(findSql);
		assertSame("findBySql方法查询数据不完整！", getUserMap().size(), list.size());
	}

	/**
	 * Test method for
	 * {@link org.webframe.core.dao.BaseDao#findBySql(java.lang.String, java.lang.Class, int)} .
	 */
	@Test
	public void testFindBySqlStringClassOfQInt() {
		String findSql = "select * from T_TEST_USER";
		int max = 3;
		List<TTestUser> list = (List<TTestUser>) baseDao.findBySql(findSql, TTestUser.class, max);
		assertTrue("findBySql方法查询数据不完整！", list.size() <= max);
		assertSame("findBySql错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#clear()}.
	 */
	@Test
	public void testClear() {
		baseDao.clear();
		assertSame("clear错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#evict(java.lang.Object)}.
	 */
	@Test
	public void testEvict() {
		for (TTestUser testUser : getUserMap().values()) {
			baseDao.evict(testUser);
		}
		assertSame("evict错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#evictSecondLevelCache(java.lang.Class)}.
	 */
	@Test
	public void testEvictSecondLevelCache() {
		baseDao.evictSecondLevelCache(TTestUser.class);
		assertSame("evictSecondLevelCache错误！", baseDao.loadAll(TTestUser.class).size(), getUserMap().size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#flush()}.
	 */
	@Test
	public void testFlush() {
		baseDao.flush();
		assertSame("flush错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#delete(java.lang.Object)}.
	 */
	@Test
	public void testDeleteObject() {
		String userId = null;
		for (TTestUser testUser : getUserMap().values()) {
			userId = testUser.getId();
			baseDao.delete(testUser);
			getUserMap().remove(userId);
			break;
		}
		assertNull("删除id为：" + userId + "的对象失败！", baseDao.get(TTestUser.class, userId));
		assertSame("delete错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#delete(java.lang.String)}.
	 */
	@Test
	public void testDeleteString() {
		String userId = null;
		for (String id : getUserMap().keySet()) {
			userId = id;
			String deleteHql = "delete from TTestUser where id='" + id + "'";
			baseDao.delete(deleteHql);
			getUserMap().remove(userId);
			break;
		}
		assertNull("删除id为：" + userId + "的对象失败！", baseDao.get(TTestUser.class, userId));
		assertSame("delete错误！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}

	/**
	 * Test method for {@link org.webframe.core.dao.BaseDao#deleteAll(java.util.Collection)} .
	 */
	@Test
	public void testDeleteAll() {
		Collection<TTestUser> users = getUserMap().values();
		baseDao.deleteAll(users);
		userMap = new HashMap<String, TTestUser>(8);
		assertSame("TestUser记录没有删除完！", getUserMap().size(), baseDao.loadAll(TTestUser.class).size());
	}
}
