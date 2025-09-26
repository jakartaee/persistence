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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.metamodel.Metamodel;

public class Client1 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	List<Employee> empRef = new ArrayList<Employee>();

	Employee emp0 = null;

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManager1.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * setup() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
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
	 * @testName: autoCloseableTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2517;
	 *
	 * @test_Strategy: Create EntityManager in try with resources block and verify
	 * whether it's open inside and outside of the try block.
	 */
	@Test
	public void autoCloseableTest() throws Exception {
		EntityManager em = null;
		try (final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory(getPersistenceUnitName(),
				getPersistenceUnitProperties())) {
			try (final EntityManager emLocal = emfLocal.createEntityManager()) {
				em = emLocal;
				if (em == null) {
					throw new Exception("autoCloseableTest failed: createEntityManager() returned null");
				}
				if (!em.isOpen()) {
					throw new Exception("autoCloseableTest failed: EntityManager isOpen() returned false in try block");
				}
			} finally {
				if (em != null && em.isOpen()) {
					throw new Exception(
							"autoCloseableTest failed: EntityManager isOpen() returned true outside try block");
				}
			}
		} catch (Exception f) {
			throw f;
		} catch (Throwable t) {
			throw new Exception("autoCloseableTest failed with Exception", t);
		}
	}

	/*
	 * @testName: mergeTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:660
	 *
	 * @test_Strategy: Merge new entity
	 */
	@Test
	public void mergeTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Order o1 = getEntityManager().merge(new Order(9, 999, "desc999"));
			getEntityTransaction().commit();
			clearCache();
			Order o2 = getEntityManager().find(Order.class, 9);
			if (o1.equals(o2)) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results - expected:" + o1.toString() + ", actual:" + o2.toString());
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("mergeTest failed");
		}
	}

	/*
	 * @testName: mergeExceptionsTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:504
	 *
	 * @test_Strategy: Call EntityManager.merge() method
	 */
	@Test
	public void mergeExceptionsTest() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.INFO, "Testing merge(Object");

		logger.log(Logger.Level.INFO, "Testing invalid object ");
		try {
			getEntityTransaction().begin();
			getEntityManager().merge(this);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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

		logger.log(Logger.Level.INFO, "Testing removed entity ");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 1);
			getEntityManager().remove(o);
			getEntityTransaction().commit();

			getEntityTransaction().begin();
			getEntityManager().merge(o);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("mergeExceptionsTest failed");
		}
	}

	/*
	 * @testName: removeExceptionsTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:527
	 *
	 * @test_Strategy: Call EntityManager.remove() method
	 */
	@Test
	public void removeExceptionsTest() throws Exception {
		boolean pass = false;
		logger.log(Logger.Level.INFO, "Testing findClassObjectIllegalStateException");

		logger.log(Logger.Level.INFO, "Invalid Object test");
		try {
			getEntityTransaction().begin();
			getEntityManager().remove(this);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
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
			throw new Exception("removeExceptionsTest failed");
		}
	}

	/*
	 * @testName: lockIllegalStateExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:490; PERSISTENCE:JAVADOC:497;
	 *
	 * @test_Strategy: Call EntityManager.lock() method
	 */
	@Test
	public void lockIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		logger.log(Logger.Level.INFO, "Testing invalid object for lock(Object, LockModeType)");
		try {
			getEntityTransaction().begin();
			getEntityManager().lock(this, LockModeType.WRITE);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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

		logger.log(Logger.Level.INFO, "Testing invalid object for lock(Object, LockModeType, Map)");
		try {
			getEntityTransaction().begin();
			getEntityManager().lock(this, LockModeType.WRITE, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("lockIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshInvalidObjectIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:509
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshInvalidObjectIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(this);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshInvalidObjectIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshNonManagedObjectIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:509
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshNonManagedObjectIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(new Order(99, 999));
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshNonManagedObjectIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshInvalidObjectMapIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:512
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshInvalidObjectMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(this, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshInvalidObjectMapIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshNonManagedObjectMapIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:512
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshNonManagedObjectMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(new Order(99, 999), myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshNonManagedObjectMapIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:515
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(this, LockModeType.WRITE);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:515
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(new Order(99, 999), LockModeType.WRITE);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:521
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(this, LockModeType.WRITE, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:521
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@Test
	public void refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(new Order(99, 999), LockModeType.WRITE, myMap);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
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
			throw new Exception("refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: containsIllegalArgumentException
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:458
	 *
	 * @test_Strategy: Call EntityManager.contains() method passing an Object that
	 * is not an Entity
	 */
	@Test
	public void containsIllegalArgumentException() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();

			getEntityManager().contains("notanentity");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
			getEntityTransaction().commit();
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
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
			throw new Exception("containsIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: createNamedQueryIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:459; PERSISTENCE:JAVADOC:460
	 *
	 * @test_Strategy: Call EntityManager.createNamedQuery() with a query string
	 * that is invalid, verify IllegalArgumentException is thrown Call
	 * EntityManager.createNamedQuery() with a TypedQuery string that is invalid,
	 * verify IllegalArgumentException is thrown. Call
	 * EntityManager.createNamedQuery() with a TypedQuery string with a result type
	 * not assignable to the specified type, verify IllegalArgumentException is
	 * thrown.
	 */
	@Test
	public void createNamedQueryIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false, pass2 = false;
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createNamedQuery("CTS NamedQuery");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");
		try {
			getEntityManager().createNamedQuery("CTS NamedQuery", Order.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery with incorrect result type version");
		try {
			getEntityManager().createNamedQuery("SELECT o from ORDER o", String.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("createNamedQueryIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: createQueryIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:461; PERSISTENCE:JAVADOC:463;
	 * PERSISTENCE:JAVADOC:462; PERSISTENCE:SPEC:609; PERSISTENCE:SPEC:1372;
	 *
	 * @test_Strategy: Call EntityManager.createQuery(String) with a query string
	 * that is invalid, verify IllegalArgumentException is thrown Call
	 * EntityManager.createQuery(String, Class) with a TypedQuery string with a
	 * result type not assignable to the specified type, verify
	 * IllegalArgumentException is thrown. Call
	 * EntityManager.createQuery(CriteriaQuery) with an invalid CriteriaQuery verify
	 * IllegalArgumentException is thrown.*
	 */
	@Test
	public void createQueryIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false, pass2 = false, pass3 = false;
		logger.log(Logger.Level.TRACE, "Testing String version");

		try {
			Query q = getEntityManager().createQuery("CTS Query");
			logger.log(Logger.Level.INFO, "IllegalArgumentException was not thrown");
			try {
				q.getResultList();
				logger.log(Logger.Level.ERROR, "Neither IllegalArgumentException nor PersistenceException was thrown");
			} catch (PersistenceException e) {
				logger.log(Logger.Level.TRACE, "PersistenceException Caught during execution.");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred during execution", e);
			}
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing String, Class version");
		try {
			getEntityManager().createQuery("SELECT o from ORDER o", String.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing CriteriaQuery version");
		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();
			CriteriaQuery cquery = qbuilder.createQuery(null);
			Query q = getEntityManager().createQuery(cquery);
			logger.log(Logger.Level.INFO, "IllegalArgumentException was not thrown");
			try {
				q.getResultList();
				logger.log(Logger.Level.ERROR, "Neither IllegalArgumentException nor PersistenceException was thrown");
			} catch (PersistenceException e) {
				logger.log(Logger.Level.TRACE, "PersistenceException Caught during execution.");
				pass3 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred during execution", e);
			}
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass3 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("createQueryIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: detachIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:464
	 *
	 * @test_Strategy: Call EntityManager.detach(String), verify
	 * IllegalArgumentException is thrown
	 */
	@Test
	public void detachIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().detach(Client1.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception", re);
			}
		}

		if (!pass) {
			throw new Exception("detachIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getEntityManagerFactoryTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:328;
	 *
	 * @test_Strategy: Get EntityManagerFactory
	 */
	@Test
	public void getEntityManagerFactoryTest() throws Exception {
		boolean pass = false;
		try {
			EntityManager em = getEntityManager();
			EntityManagerFactory emf = em.getEntityManagerFactory();
			if (emf == null) {
				logger.log(Logger.Level.ERROR, "getEntityManagerFactory() returned a null result");
			} else {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getEntityManagerFactoryTest failed");
		}
	}

	/*
	 * @testName: emGetMetamodelTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:330;
	 *
	 * @test_Strategy: Get a MetaModel Object from the EntityManager an make sure it
	 * is not null
	 */
	@Test
	public void emGetMetamodelTest() throws Exception {
		boolean pass = false;
		try {
			EntityManager em = getEntityManager();
			Metamodel mm = em.getMetamodel();
			if (mm == null) {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned a null result");
			} else {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("emGetMetamodelTest failed");
		}
	}

	/*
	 * @testName: setPropertyTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:336;
	 *
	 * @test_Strategy: Set a standard property in the EntityManager and retrieve it.
	 */
	@Test
	public void setPropertyTest() throws Exception {
		boolean foundKey = false;
		boolean foundValue = false;

		try {
			EntityTransaction t = getEntityTransaction();
			t.begin();
			EntityManager em = getEntityManager();
			String expectedKey = "jakarta.persistence.cache.retrieveMode";
			CacheRetrieveMode expectedValue = CacheRetrieveMode.USE;
			logger.log(Logger.Level.TRACE, "Setting property:" + expectedKey + "," + expectedValue.toString());
			em.setProperty(expectedKey, expectedValue);

			// gather the props from the EntityManager

			logger.log(Logger.Level.TRACE, "Retrieve all EntityManger properties:");
			Map<String, Object> em_entry = em.getProperties();

			for (Map.Entry<String, Object> entry : em_entry.entrySet()) {
				String key = entry.getKey();
				logger.log(Logger.Level.INFO, "Key = " + key);
				if (key.contains(expectedKey)) {
					foundKey = true;
					Object oValue = entry.getValue();
					if (oValue instanceof CacheRetrieveMode) {
						CacheRetrieveMode value = (CacheRetrieveMode) oValue;
						if (value.equals(expectedValue)) {
							logger.log(Logger.Level.INFO, "Received expected value:" + value.toString());
							foundValue = true;
						} else {
							logger.log(Logger.Level.ERROR, "Key:" + expectedKey + " -  expected value:" + expectedKey
									+ ", actual value" + value);
						}
					} else {
						logger.log(Logger.Level.ERROR,
								"The value for Key:" + expectedKey + "was not an instance of String:" + oValue);
					}
				}
			}
			if (!foundKey) {
				logger.log(Logger.Level.ERROR, "Property key:" + expectedKey + ", not found in EntityManager");
			}
			if (!foundValue) {
				logger.log(Logger.Level.ERROR, "The value for Key:" + expectedKey + ", was not found in EntityManager");
			}
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
		if (!foundKey || !foundValue) {
			throw new Exception("setPropertyTest failed");
		}
	}

	/*
	 * @testName: getCriteriaBuilderTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:327; PERSISTENCE:SPEC:1702;
	 *
	 * @test_Strategy: access EntityManager.getCriteriaBuilder and verify it can be
	 * used to create a query
	 *
	 */
	@Test
	public void getCriteriaBuilderTest() throws Exception {
		boolean pass = false;
		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			if (cbuilder != null) {
				getEntityTransaction().begin();
				CriteriaQuery<Object> cquery = cbuilder.createQuery();
				if (cquery != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "getCriteriaBuilder() returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getCriteriaBuilderTest failed");
		}
	}

	/*
	 * @testName: isJoinedToTransactionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1526
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void isJoinedToTransactionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Test when no transaction active");
		if (getEntityManager().isJoinedToTransaction() == false) {
			logger.log(Logger.Level.TRACE, "Received expected result:false");
			pass1 = true;
		} else {
			logger.log(Logger.Level.ERROR, "Returned true when not in a transaction");
		}
		logger.log(Logger.Level.INFO, "Test when transaction active");

		getEntityTransaction().begin();
		if (getEntityManager().isJoinedToTransaction() == true) {
			logger.log(Logger.Level.TRACE, "Received expected result:true");
			pass2 = true;
		} else {
			logger.log(Logger.Level.ERROR, "Returned false when in a transaction");
		}
		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("isJoinedToTransactionTest failed");
		}

	}

	/*
	 * @testName: createStoredProcedureQueryStringIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1521
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		StringBuilder msg = new StringBuilder();
		try {
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("DOESNOTEXIST");
			msg.append("Did not throw IllegalArgumentException");
			try {
				spq.execute();
				msg.append("or a PersistenceException from execute()");
			} catch (PersistenceException pe) {
				logger.log(Logger.Level.TRACE, "Received PersistenceException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception after execute()", e);
			}
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			logger.log(Logger.Level.ERROR, msg.toString());

			throw new Exception("createStoredProcedureQueryStringIllegalArgumentExceptionTest failed");
		}

	}

	/*
	 * @testName:
	 * createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1523
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		Class[] cArray = { Integer.class };
		StringBuilder msg = new StringBuilder();
		try {
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("DOESNOTEXIST", cArray);
			msg.append("Did not throw IllegalArgumentException");
			try {
				spq.execute();
				msg.append("or a PersistenceException after execute()");

			} catch (PersistenceException pe) {
				logger.log(Logger.Level.TRACE, "Received PersistenceException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception from execute()", e);
			}
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			logger.log(Logger.Level.ERROR, msg.toString());
			throw new Exception("createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest failed");
		}

	}

	/*
	 * @testName:
	 * createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1525
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		StringBuilder msg = new StringBuilder();
		try {
			String[] sArray = { "doesnotexist" };
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("DOESNOTEXIST", sArray);
			msg.append("Did not throw IllegalArgumentException");
			try {
				spq.execute();
				msg.append("or a PersistenceException from execute()");
			} catch (PersistenceException pe) {
				logger.log(Logger.Level.TRACE, "Received PersistenceException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception after execute()", e);
			}

		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			logger.log(Logger.Level.ERROR, msg.toString());
			throw new Exception("createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest failed");
		}

	}

	/*
	 * @testName: createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1515
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		try {
			getEntityManager().createNamedStoredProcedureQuery("DOESNOTEXIST");
			logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		}
		getEntityTransaction().rollback();

		if (!pass) {
			throw new Exception("createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest failed");
		}

	}

}
