/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.core.lock.entitymanager;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.LockOption;
import jakarta.persistence.Timeout;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.LockModeType;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Coffee" };
		return createDeploymentJar("jpa_core_lock_entitymanager.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

			logger.log(Logger.Level.TRACE, "Cleanup data");
			removeTestData();
			logger.log(Logger.Level.TRACE, "Create Test data");
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: findTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:325
	 * 
	 * @test_Strategy:
	 * 
	 * find(Class entityClass, Object PK, LockModeType lck)
	 * 
	 * Find by primary key and lock. Search for an entity of the specified class and
	 * primary key and lock it with respect to the specified lock type. If the
	 * entity instance is contained in the persistence context it is returned from
	 * there, and the effect of this method is the same as if the lock method had
	 * been called on the entity. If the entity is found within the persistence
	 * context and the lock mode type is pessimistic and the entity has a version
	 * attribute, the persistence provider must perform optimistic version checks
	 * when obtaining the database lock. If these checks fail, the
	 * OptimisticLockException will be thrown. If the lock mode type is pessimistic
	 * and the entity instance is found but cannot be locked: - the
	 * PessimisticLockException will be thrown if the database locking failure
	 * causes transaction-level rollback. - the LockTimeoutException will be thrown
	 * if the database locking failure causes only statement-level rollback
	 * 
	 */
	@Test
	public void findTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin findTest1");
		boolean pass = false;

		getEntityTransaction().begin();

		try {

			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1, LockModeType.PESSIMISTIC_READ);

			if (coffeeFound != null) {
				logger.log(Logger.Level.TRACE, "Found coffee as expected");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("findTest1 failed");
		}
	}

	/*
	 * @testName: findTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:326
	 * 
	 * @test_Strategy:
	 * 
	 * find(Class entityClass, Object PK, LockModeType lck, Map<String, Object>
	 * properties)
	 *
	 * 
	 * Find by primary key and lock, using specified properties. Search for an
	 * entity of the specified class and primary key and lock it with respect to the
	 * specified lock type. If the entity instance is contained in the persistence
	 * context it is returned from there. If the entity is found within the
	 * persistence context and the lock mode type is pessimistic and the entity has
	 * a version attribute, the persistence provider must perform optimistic version
	 * checks when obtaining the database lock. If these checks fail, the
	 * OptimisticLockException will be thrown. If the lock mode type is pessimistic
	 * and the entity instance is found but cannot be locked: - the
	 * PessimisticLockException will be thrown if the database locking failure
	 * causes transaction-level rollback. - the LockTimeoutException will be thrown
	 * if the database locking failure causes only statement-level rollback If a
	 * vendor-specific property or hint is not recognized, it is silently ignored.
	 * Portable applications should not rely on the standard timeout hint. Depending
	 * on the database in use and the locking mechanisms used by the provider, the
	 * hint may or may not be observed
	 * 
	 */
	@Test
	public void findTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin findTest2");
		boolean pass = false;

		getEntityTransaction().begin();

		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		try {

			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1, LockModeType.PESSIMISTIC_READ, myMap);

			if (coffeeFound != null) {
				logger.log(Logger.Level.TRACE, "Found coffee as expected");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("findTest2 failed");
		}
	}

	/*
	 * @testName: findTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:325
	 * 
	 * @test_Strategy:
	 * 
	 * find(Class entityClass, Object PK, LockModeType lck)
	 *
	 * 1) Create an entity manager 2) Lock entity (coffee) in em1 using
	 * PESSIMISTIC_READ lock 3) Try to Lock the same entity for coffee with a
	 * PESSIMISTIC_WRITE lock
	 *
	 */
	@Test
	public void findTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin findTest3");
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1, LockModeType.PESSIMISTIC_READ);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em2 and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1, LockModeType.PESSIMISTIC_WRITE);

			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("findTest3 failed");
		}
	}

	/*
	 * @testName: findTest4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:324
	 * 
	 * @test_Strategy:
	 *
	 * find(Class entityClass, Object PK, Map<String, Object> properties)
	 *
	 * Find by primary key, using the specified properties. Search for an entity of
	 * the specified class and primary key. If the entity instance is contained in
	 * the persistence context, it is returned from there. If a vendor-specific
	 * property or hint is not recognized, it is silently ignored.
	 *
	 */
	@Test
	public void findTest4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin findTest1");
		boolean pass = false;

		getEntityTransaction().begin();

		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		try {

			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1, myMap);

			if (coffeeFound != null) {
				logger.log(Logger.Level.TRACE, "Found coffee as expected");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("findTest4 failed");
		}
	}

	/*
	 * @testName: lockTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:51
	 * 
	 * @test_Strategy:
	 * 
	 * public void lock(Object entity, LockModeType lockMode); 1) Create One entity
	 * manager 2) Lock entity (coffee) in em1 using PESSIMISTIC_WRITE lock 3) Try to
	 * obtain same Lock for cofee the same object and modify its contents
	 *
	 */
	@Test
	public void lockTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin lockTest1");
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
			getEntityManager().lock(coffeeFound, LockModeType.PESSIMISTIC_WRITE);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
			getEntityManager().lock(coffeeFound2, LockModeType.PESSIMISTIC_WRITE);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("lockTest1 failed");
		}
	}

	/*
	 * @testName: lockTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:332
	 * 
	 * @test_Strategy: public void lock(Object entity, LockModeType lockMode,
	 * Map<String, Object> properties);
	 * 
	 * 1) Create One entity manager 2) Lock entity (coffee) in em1 using
	 * PESSIMISTIC_WRITE lock 3) Try to obtain same Lock for cofee the same object
	 * and modify its contents
	 * 
	 * Note: This test uses lock with property map (unlike lockTest1)
	 */
	@Test
	public void lockTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin lockTest2");
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
			Map<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("some.cts.specific.property", "nothing.in.particular");
			getEntityManager().lock(coffeeFound, LockModeType.PESSIMISTIC_WRITE, myMap);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
			getEntityManager().lock(coffeeFound2, LockModeType.PESSIMISTIC_WRITE, myMap);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("lockTest2 failed");
		}
	}

	/*
	 * @testName: lockTest3
	 *
	 * @test_Strategy: public void lock(Object entity, LockModeType lockMode,
	 * LockOption... options);
	 *
	 * 1) Create One entity manager 2) Lock entity (coffee) in em1 using
	 * PESSIMISTIC_WRITE lock 3) Try to obtain same Lock for cofee the same object
	 * and modify its contents
	 *
	 * Note: This test uses lock LockOption... options
	 */
	@Test
	public void lockTest3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin lockTest3");
		boolean pass = false;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
			LockOption[] lockOptions = new LockOption[]{Timeout.ms(0)};
			getEntityManager().lock(coffeeFound, LockModeType.PESSIMISTIC_WRITE, lockOptions);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
			getEntityManager().lock(coffeeFound2, LockModeType.PESSIMISTIC_WRITE);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
		if (!pass) {
			throw new Exception("lockTest3 failed");
		}
	}

	/*
	 * @testName: refreshTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:334
	 * 
	 * @test_Strategy:
	 * 
	 * public void refresh(Object entity, LockModeType lockMode); 1) Create entity
	 * manager 2) Find entity and refresh in em1 using PESSIMISTIC_READ lock 3) Try
	 * to obtain refresh same entity using PESSIMISTIC_WRITE lock and modify its
	 * contents
	 *
	 */
	@Test
	public void refreshTest1() throws Exception {

		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 1);
			getEntityManager().refresh(coffeeFound, LockModeType.PESSIMISTIC_READ);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager  and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 1);
			getEntityManager().refresh(coffeeFound2, LockModeType.PESSIMISTIC_WRITE);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("refreshTest1 failed");
		}
	}

	/*
	 * @testName: refreshTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:335
	 * 
	 * @test_Strategy:
	 * 
	 * public void refresh(Object entity, LockModeType lockMode, Map<String, Object>
	 * properties); 1) Create entity manager 2) Find entity and refresh in em1 using
	 * PESSIMISTIC_READ lock 3) Try to obtain refresh same entity using
	 * PESSIMISTIC_WRITE lock and modify its contents Note: This test uses refresh
	 * with property map (unlike refreshTest1)
	 *
	 */
	@Test
	public void refreshTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin refreshTest2");
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager em1 and lock");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 2);
			Map<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("some.cts.specific.property", "nothing.in.particular");
			getEntityManager().refresh(coffeeFound, LockModeType.PESSIMISTIC_READ, myMap);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 2);
			getEntityManager().refresh(coffeeFound2, LockModeType.PESSIMISTIC_WRITE, myMap);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("refreshTest2 failed");
		}
	}

	/*
	 * @testName: refreshTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:333
	 * 
	 * @test_Strategy:
	 *
	 * public void refresh(Object entity, LockModeType lockMode, Map<String, Object>
	 * properties); 1) Create entity manager 2) Find entity and refresh 3) Try to
	 * obtain refresh same entity and modify its contents
	 *
	 */
	@Test
	public void refreshTest3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin refreshTest3");
		boolean pass = false;

		try {

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager");
			Coffee coffeeFound = getEntityManager().find(Coffee.class, 2);
			Map<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("some.cts.specific.property", "nothing.in.particular");
			getEntityManager().refresh(coffeeFound, myMap);

			logger.log(Logger.Level.TRACE, "locate Entity Coffee in EntityManager and update");
			Coffee coffeeFound2 = getEntityManager().find(Coffee.class, 2);
			getEntityManager().refresh(coffeeFound2, myMap);
			coffeeFound2.setPrice(6.0F);
			getEntityTransaction().commit();
			pass = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("refreshTest3 failed");
		}
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */
	private void createTestData() throws Exception {
		try {

			logger.log(Logger.Level.TRACE, "createTestData");

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Create 5 Coffees");
			Coffee cRef[] = new Coffee[5];
			cRef[0] = new Coffee(1, "hazelnut", 1.0F);
			cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
			cRef[2] = new Coffee(3, "decaf", 3.0F);
			cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
			cRef[4] = new Coffee(5, "mocha", 5.0F);

			logger.log(Logger.Level.TRACE, "Start to persist coffees ");
			for (Coffee c : cRef) {
				if (c != null) {
					getEntityManager().persist(c);
					logger.log(Logger.Level.TRACE, "persisted coffee " + c);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM COFFEE").executeUpdate();

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
