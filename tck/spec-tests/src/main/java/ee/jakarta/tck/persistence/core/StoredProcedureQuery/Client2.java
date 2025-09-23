/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.StoredProcedureQuery;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Parameter;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TemporalType;

public class Client2 extends Client {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { MAPPING_FILE_XML };
		String[] classes = { pkgName + "Employee", pkgName + "Employee2", pkgName + "EmployeeMappedSC" };
		return createDeploymentJar("jpa_core_types_StoredProcedureQuery2.jar", pkgNameWithoutSuffix, classes, xmlFiles);

	}

	/*
	 * setupEmployee2Data() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	@BeforeEach
	public void setupEmployee2Data() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createEmployee2TestData();
			dataBaseName = System.getProperty("jdbc.db");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
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
									"Did not get instance of Employee, instead got:" + o.getClass().getSimpleName());
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

	public boolean verifyEmployeeIds(List<Integer> expected, List<List> listOfList) {
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
	 * @testName: setParameterIntCalendarTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1576;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void setParameterIntCalendarTemporalTypeTest() throws Exception {
		boolean pass2 = false;
		boolean pass4 = false;
		try {
			getEntityTransaction().begin();
			try {
				logger.log(Logger.Level.INFO, "Testing StoredProcedureQuery object");

				StoredProcedureQuery spq = getEntityManager()
						.createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
				spq.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
				spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
				spq.setParameter(1, calDate, TemporalType.DATE);

				if (!spq.execute()) {
					Object o = spq.getOutputParameterValue(2);
					if (o instanceof Integer) {
						int actual = (Integer) o;
						if (actual == emp2.getId()) {
							logger.log(Logger.Level.TRACE, "Received expected id:" + actual);
							pass2 = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected id: " + emp2.getId() + ", actual:" + actual);
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not get instance of Integer back:" + o);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected execute() to return false, actual: true");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			}
			try {
				logger.log(Logger.Level.INFO, "Testing Query object");
				StoredProcedureQuery spq1 = getEntityManager()
						.createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
				spq1.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
				spq1.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
				Query q = spq1;
				q.setParameter(1, getCalDate(), TemporalType.DATE);
				StoredProcedureQuery spq2 = (StoredProcedureQuery) q;
				if (!spq2.execute()) {
					Object o = spq2.getOutputParameterValue(2);
					if (o instanceof Integer) {
						int actual = (Integer) o;
						if (actual == 5) {
							logger.log(Logger.Level.TRACE, "Received expected id:" + actual);
							pass4 = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected id: 5, actual:" + actual);
						}

					} else {
						logger.log(Logger.Level.ERROR, "Did not get instance of Integer back:" + o);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected execute() to return false, actual: true");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass2 || !pass4)
			throw new Exception("setParameterIntCalendarTemporalTypeTest failed");

	}

	/*
	 * @testName: setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1577;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing StoredProcedureQuery with incorrect position specified");

			StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq1.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			try {
				spq1.setParameter(99, getCalDate(), TemporalType.DATE);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing Query object with incorrect position specified");
			StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq3.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			Query q1 = spq3.setParameter(1, getCalDate(), TemporalType.DATE);
			try {
				q1.setParameter(99, getCalDate());
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
			}

			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception", e);
		}
		if (!pass1 || !pass2)
			throw new Exception("setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest failed");

	}

	/*
	 * @testName: setParameterParameterCalendarTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1564; PERSISTENCE:SPEC:1576;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void setParameterParameterCalendarTemporalTypeTest() throws Exception {
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass5 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing StoredProcedure");
			getEntityTransaction().begin();
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			spq.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);

			spq.setParameter(1, getCalDate(), TemporalType.DATE);
			Parameter p = spq.getParameter(1);
			spq.setParameter(p, calDate, TemporalType.DATE);

			if (!spq.execute()) {
				Object o = spq.getOutputParameterValue(2);
				if (o instanceof Integer) {
					int actual = (Integer) o;
					if (actual == emp2.getId()) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + actual);
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id: " + emp2.getId() + ", actual:" + actual);
					}

				} else {
					logger.log(Logger.Level.ERROR, "Did not get instance of Integer back:" + o);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected execute() to return false, actual: true");
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		}
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing Query object");
			StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq1.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			spq1.registerStoredProcedureParameter(2, Integer.class, ParameterMode.OUT);
			spq1.setParameter(1, getCalDate(), TemporalType.DATE);
			Query q = spq1;

			Parameter p = q.getParameter(1);
			q.setParameter(p, calDate, TemporalType.DATE);
			Parameter p2 = q.getParameter(1);
			if (p.getPosition().equals(p2.getPosition()) && p.getParameterType().equals(p2.getParameterType())) {
				logger.log(Logger.Level.TRACE, "Received expected parameter");
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected parameter:" + p + ", actual:" + p2);
			}

			StoredProcedureQuery spq2 = (StoredProcedureQuery) q;

			if (!spq2.execute()) {
				Object o = spq2.getOutputParameterValue(2);
				if (o instanceof Integer) {
					int actual = (Integer) o;
					if (actual == emp2.getId()) {
						logger.log(Logger.Level.TRACE, "Received expected id:" + actual);
						pass5 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected id: " + emp2.getId() + ", actual:" + actual);
					}

				} else {
					logger.log(Logger.Level.ERROR, "Did not get instance of Integer back:" + o);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected execute() to return false, actual: true");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		}

		if (!pass2 || !pass3 || !pass5)
			throw new Exception("setParameterParameterCalendarTemporalTypeTest failed");
	}

	/*
	 * @testName:
	 * setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1565;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Get parameter from other stored procedure");
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("GetEmpLastNameFromInOut");
			spq.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
			spq.setParameter(1, "INOUT");
			// Parameter to be used in next StoredProcedure
			Parameter p = spq.getParameter(1);

			logger.log(Logger.Level.INFO, "Testing StoredProcedureQuery with parameter specified from another query");
			StoredProcedureQuery spq1 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq1.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			spq1.setParameter(1, getCalDate(), TemporalType.DATE);
			try {
				spq1.setParameter(p, getCalDate(), TemporalType.DATE);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
			}

			logger.log(Logger.Level.INFO, "Testing Query object with parameter specified from another query");
			StoredProcedureQuery spq3 = getEntityManager().createStoredProcedureQuery("GetEmpIdUsingHireDateFromOut");
			spq3.registerStoredProcedureParameter(1, Calendar.class, ParameterMode.IN);
			Query q1 = spq3.setParameter(1, getCalDate());
			try {
				q1.setParameter(p, getCalDate(), TemporalType.DATE);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
			}

			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest failed");
	}

	private void createEmployee2TestData() {

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.INFO, "Creating Employee2");

			final Calendar d2 = getCalDate(2001, 06, 27);
			final Calendar d3 = getCalDate(2002, 07, 07);
			final Calendar d4 = getCalDate(2003, 03, 03);
			final Calendar d5 = getCalDate();

			emp2 = new Employee2(1, "Alan", "Frechette", calDate, (float) 35000.0);
			empRef2.add(emp2);
			empRef2.add(new Employee2(2, "Arthur", "Frechette", d2, (float) 35000.0));
			empRef2.add(new Employee2(3, "Shelly", "McGowan", d3, (float) 50000.0));
			empRef2.add(new Employee2(4, "Robert", "Bissett", d4, (float) 55000.0));
			empRef2.add(new Employee2(5, "Stephen", "DMilla", d5, (float) 25000.0));
			for (Employee2 e : empRef2) {
				if (e != null) {
					getEntityManager().persist(e);
					logger.log(Logger.Level.TRACE, "persisted Employee2:" + e);
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

}
