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

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

public class Client3 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	List<Employee> empRef = new ArrayList<Employee>();

	Employee emp0 = null;

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

	public Client3() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManager3.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * setupEmployeeData() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	@BeforeEach
	public void setupEmployeeData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createEmployeeData();
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
	 * @testName: createStoredProcedureQueryStringTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1520
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringTest() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		try {
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpOneFirstNameFromOut");
			spq.registerStoredProcedureParameter(1, String.class, ParameterMode.OUT);
			spq.execute();

			Object oActual = spq.getOutputParameterValue(1);
			if (oActual instanceof String) {
				String actual = (String) oActual;
				if (actual.equals(emp0.getFirstName())) {
					logger.log(Logger.Level.TRACE, "Received expected result:" + actual);
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected result: " + emp0.getFirstName() + ", actual:" + actual);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected String to be returned, actual:" + oActual.getClass());
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringTest failed");
		}

	}

	/*
	 * @testName: createStoredProcedureQueryStringClassArrayTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1522
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringClassArrayTest() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		try {
			Class[] cArray = { Employee.class };
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpASCFromRS", cArray);
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logger.log(Logger.Level.TRACE, "register refcursor parameter");
				spq.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
			}
			if (spq.execute()) {
				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Integer> expected = new ArrayList<Integer>();
					for (Employee e : empRef) {
						expected.add(e.getId());
					}
					pass = verifyListOfListEmployeeIds(expected, listOfList);
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected execute() to return true, actual: false");
			}

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringClassArrayTest failed");
		}

	}

	/*
	 * @testName: createStoredProcedureQueryStringStringArrayTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1524; PERSISTENCE:SPEC:1571;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createStoredProcedureQueryStringStringArrayTest() throws Exception {
		boolean pass = false;

		getEntityTransaction().begin();
		clearCache();
		try {
			String[] sArray = { "id-firstname-lastname" };
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdFNameLNameFromRS",
					sArray);
			spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logger.log(Logger.Level.TRACE, "register refcursor parameter");
				spq.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);
			}
			spq.setParameter(1, 1);

			if (spq.execute()) {

				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Employee> expected = new ArrayList<Employee>();
					expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
					pass = verifyListOfListEmployees(expected, listOfList);
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected execute() to return true, actual: false");
			}
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createStoredProcedureQueryStringStringArrayTest failed");
		}

	}

	/*
	 * @testName: createNamedStoredProcedureQueryStringTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:1514; PERSISTENCE:JAVADOC:1530;
	 * PERSISTENCE:JAVADOC:1532; PERSISTENCE:JAVADOC:1533; PERSISTENCE:JAVADOC:1534;
	 * PERSISTENCE:JAVADOC:1541; PERSISTENCE:JAVADOC:1543;
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void createNamedStoredProcedureQueryStringTest() throws Exception {
		boolean pass = false;
		getEntityTransaction().begin();
		clearCache();
		try {
			StoredProcedureQuery spq = null;
			if (dataBaseName.equalsIgnoreCase(ORACLE) || dataBaseName.equalsIgnoreCase(POSTGRESQL)) {
				logger.log(Logger.Level.TRACE, "Calling refcursor specific named stored procedure query");
				spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname-refcursor");
			} else {
				spq = getEntityManager().createNamedStoredProcedureQuery("get-id-firstname-lastname");
			}
			spq.setParameter(1, 1);
			if (spq.execute()) {
				List<List> listOfList = getResultSetsFromStoredProcedure(spq);
				if (listOfList.size() == 1) {
					List<Employee> expected = new ArrayList<Employee>();
					expected.add(new Employee(emp0.getId(), emp0.getFirstName(), emp0.getLastName()));
					pass = verifyListOfListEmployees(expected, listOfList);
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get the correct number of result sets returned, expected: 1, actual:"
									+ listOfList.size());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected execute() to return true, actual: false");
			}

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception:", ex);
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createNamedStoredProcedureQueryStringTest failed");
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
