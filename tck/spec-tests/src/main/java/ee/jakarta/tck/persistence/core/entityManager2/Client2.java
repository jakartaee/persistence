/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entityManager2;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TransactionRequiredException;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	Employee[] empRef = new Employee[5];

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DoesNotExist", pkgName + "Employee", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManager2.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * setupOrderData() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	@BeforeEach
	public void setupOrderData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createOrderData();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
			dataBaseName = System.getProperty("jdbc.db");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupData() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
			removeTestData();
			cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: lockTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:491; PERSISTENCE:JAVADOC:492;
	 * PERSISTENCE:JAVADOC:498; PERSISTENCE:JAVADOC:499
	 * 
	 * @test_Strategy: Call EntityManager.lock() method
	 */
	@Test
	public void lockTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Map<String, Object> myMap = new HashMap<String, Object>();

		myMap.put("some.cts.specific.property", "nothing.in.particular");

		logger.log(Logger.Level.INFO, "Testing TransactionRequiredException for lock(Object, LockModeType)");
		try {
			Order o = getEntityManager().find(Order.class, 1);
			getEntityManager().lock(o, LockModeType.WRITE);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TransactionRequiredException for lock(Object, LockModeType, Map)");
		try {
			Order o = getEntityManager().find(Order.class, 1);
			getEntityManager().lock(o, LockModeType.WRITE, myMap);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("lockTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1313;
	 *
	 * @test_Strategy: Call EntityManager.refresh() method without a transaction
	 */
	@Test
	public void refreshTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing refresh(Object, LockModeType)");

		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			o.setdescription("FOOBAR");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		logger.log(Logger.Level.INFO, "Testing refresh(Object, LockModeType, Map)");
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			o.setdescription("FOOBAR");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("refreshTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: lockTransactionRequiredException2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1313;
	 *
	 * @test_Strategy: Call EntityManager.lock() method without a transaction
	 */
	@Test
	public void lockTransactionRequiredException2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing lock(Object, LockModeType)");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			removeTestData();
			getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		logger.log(Logger.Level.INFO, "Testing lock(Object, LockModeType, Map)");
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			removeTestData();
			getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ, myMap);
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("lockTransactionRequiredException2Test failed");
		}
	}

	private void createOrderData() {

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Creating Orders");
			orders[0] = new Order(1, 111, "desc1");
			orders[1] = new Order(2, 222, "desc2");
			orders[2] = new Order(3, 333, "desc3");
			orders[3] = new Order(4, 444, "desc4");
			orders[4] = new Order(5, 555, "desc5");
			for (Order o : orders) {
				logger.log(Logger.Level.TRACE, "Persisting order:" + o.toString());
				getEntityManager().persist(o);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
