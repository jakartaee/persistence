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

package ee.jakarta.tck.persistence.core.entityManager;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	List<Employee> empRef = new ArrayList<Employee>();

	Employee emp0 = null;

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee", pkgName + "Order" };
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
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	public List<List> getResultSetsFromStoredProcedure(StoredProcedureQuery spq) {
		logger.log(Logger.Level.TRACE, "in getResultSetsFromStoredProcedure");
		boolean results = true;
		List<List> listOfList = new ArrayList<List>();
		int rsnum = 1;
		int rowsAffected = 0;

		do {
			if (results) {
				logger.log(Logger.Level.TRACE, "Processing set:" + rsnum);
				List<Employee> empList = new ArrayList<Employee>();
				List list = spq.getResultList();
				if (list != null) {
					logger.log(Logger.Level.TRACE, "Getting result set: " + (rsnum) + ", size:" + list.size());
					for (Object o : list) {
						if (o instanceof Employee) {
							Employee e = (Employee) o;
							logger.log(Logger.Level.TRACE, "Saving:" + e);
							empList.add(e);
						} else {
							logger.log(Logger.Level.ERROR,
									"Did not get instance of Employee, instead got:" + o.getClass().getName());
						}
					}
					if (empList.size() > 0) {
						listOfList.add(empList);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Result set[" + rsnum + "] returned was null");
				}
				rsnum++;
			} else {
				rowsAffected = spq.getUpdateCount();
				if (rowsAffected >= 0)
					logger.log(Logger.Level.TRACE, "rowsAffected:" + rowsAffected);
			}
			results = spq.hasMoreResults();
			logger.log(Logger.Level.TRACE, "Results:" + results);

		} while (results || rowsAffected != -1);
		return listOfList;
	}

	public boolean verifyListOfListEmployeeIds(List<Integer> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : lEmp) {
					actual.add(e.getId());
				}

				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					logger.log(Logger.Level.TRACE, "Received expected result:");
					for (Integer a : actual) {
						logger.log(Logger.Level.TRACE, "id:" + a);
					}
					count++;
				} else {
					logger.log(Logger.Level.ERROR, "Did not receive expected result:");
					for (Integer e : expected) {
						logger.log(Logger.Level.ERROR, " Expected id:" + e);
					}
					for (Integer a : actual) {
						logger.log(Logger.Level.ERROR, "Actual id:" + a);
					}
				}

			} else {
				logger.log(Logger.Level.ERROR, "Result set that was returned had 0 length");
			}

		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListOfListEmployees(List<Employee> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Employee> actual = new ArrayList<Employee>();
				for (Employee e : lEmp) {
					actual.add(e);
				}
				if (verifyListEmployees(expected, actual)) {
					count++;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Result set that was returned had 0 length");
			}
		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListEmployees(List<Employee> expected, List<Employee> actual) {
		boolean result = false;
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
			for (Employee e : expected) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + e);
			}
			result = true;
		} else {
			logger.log(Logger.Level.ERROR, "Did not receive expected result:");
			for (Employee e : expected) {
				logger.log(Logger.Level.ERROR, "expected employee:" + e);
			}
			for (Employee e : actual) {
				logger.log(Logger.Level.ERROR, "actual employee :" + e);
			}
		}
		return result;
	}

	/*
	 * @testName: persistExceptionsTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:31; PERSISTENCE:JAVADOC:506;
	 * PERSISTENCE:JAVADOC:507; PERSISTENCE:SPEC:618.1; PERSISTENCE:SPEC:618.2
	 *
	 * @test_Strategy: Call EntityManager.persist()
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	@Test
	public void persistExceptionsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing persisting an entity twice ");

		try {
			getEntityManager().detach(orders[0]);
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Try to persist an existing Order");
			getEntityManager().persist(orders[0]);
			getEntityManager().flush();
			getEntityTransaction().commit();

			logger.log(Logger.Level.ERROR, "A PersistenceException was not thrown");
		} catch (EntityExistsException eee) {
			logger.log(Logger.Level.TRACE, "EntityExistsException Caught as Expected:", eee);
			pass1 = true;
		} catch (PersistenceException pe) {
			logger.log(Logger.Level.TRACE, "A PersistentException was caught:", pe);
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in while rolling back TX:", re);
			}
		}

		logger.log(Logger.Level.INFO, "Testing non-entity ");
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(this);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException caught as expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("persistExceptionsTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:511
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	@Test
	public void refreshRemovedObjectEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Finding Order");
			Order o = getEntityManager().find(Order.class, 1);
			logger.log(Logger.Level.TRACE, "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			logger.log(Logger.Level.TRACE, "Refreshing previous order");
			getEntityManager().refresh(o);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logger.log(Logger.Level.TRACE, "EntityNotFoundException Caught as Expected.");
			pass = true;
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

		if (!pass) {
			throw new Exception("refreshRemovedObjectEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectMapEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:514;
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	@Test
	public void refreshRemovedObjectMapEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 2);
			logger.log(Logger.Level.TRACE, "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			logger.log(Logger.Level.TRACE, "Refreshing previous order");
			getEntityManager().refresh(o, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logger.log(Logger.Level.TRACE, "EntityNotFoundException Caught as Expected.");
			pass = true;
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

		if (!pass) {
			throw new Exception("refreshRemovedObjectMapEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:517
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	@Test
	public void refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 3);
			logger.log(Logger.Level.TRACE, "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			logger.log(Logger.Level.TRACE, "Refreshing previous order");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logger.log(Logger.Level.TRACE, "EntityNotFoundException Caught as Expected.");
			pass = true;
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

		if (!pass) {
			throw new Exception("refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:523
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	@Test
	public void refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			logger.log(Logger.Level.TRACE, "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			logger.log(Logger.Level.TRACE, "Refreshing previous order");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logger.log(Logger.Level.TRACE, "EntityNotFoundException Caught as Expected.");
			pass = true;
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

		if (!pass) {
			throw new Exception("refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest failed");
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

	private void createEmployeeData() {

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Creating Employees");

			final Date d1 = getUtilDate("2000-02-14");
			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			emp0 = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef.add(emp0);
			empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
			empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
			empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
			empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logger.log(Logger.Level.TRACE, "persisted employee:" + e);
				}
			}
			getEntityManager().flush();
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
