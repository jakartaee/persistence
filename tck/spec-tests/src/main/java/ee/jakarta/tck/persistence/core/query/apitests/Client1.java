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

package ee.jakarta.tck.persistence.core.query.apitests;

import java.lang.System.Logger;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;

public class Client1 extends PMClientBase {
	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	private final Employee empRef[] = new Employee[21];

	private final Date d1 = getSQLDate("2000-02-14");

	private final java.util.Date dateId = getUtilDate("2009-01-10");

	final Department deptRef[] = new Department[5];

	private static final DecimalFormat df = new DecimalFormat();

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DataTypes2", pkgName + "Department", pkgName + "Employee",
				pkgName + "Insurance" };
		return createDeploymentJar("jpa_core_query_apitests1.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught in Setup: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * BEGIN Test Cases
	 */
	/*
	 * @testName: setFirstResultTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:728; PERSISTENCE:SPEC:400;
	 * PERSISTENCE:JAVADOC:636; PERSISTENCE:JAVADOC:172; PERSISTENCE:JAVADOC:399;
	 * PERSISTENCE:JAVADOC:440; PERSISTENCE:JAVADOC:665; PERSISTENCE:JAVADOC:682;
	 * 
	 * @test_Strategy: Verify results of setFirstResult using JOIN in the FROM
	 * clause projecting on state_field in the select clause. Verify that number of
	 * rows skipped are 1-1 with specified value for setFirstResult.
	 *
	 * The elements of a query result whose SELECT clause consists of more than one
	 * value are of type Object[].
	 *
	 * Create a TypedQuery where id <= 10 sorted by id. setFirstResult(5) and verify
	 * the results that were returned
	 */
	@Test
	public void setFirstResultTest() throws Exception {
		List q;
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = true;
		boolean pass11 = false;
		boolean pass12 = false;
		boolean pass13 = false;
		boolean pass14 = true;
		final Object[][] expectedResultSet = new Object[][] { new Object[] { 4, 4 }, new Object[] { 4, 9 },
				new Object[] { 4, 14 }, new Object[] { 4, 19 }, new Object[] { 5, 5 }, new Object[] { 5, 10 },
				new Object[] { 5, 15 }, new Object[] { 5, 20 } };

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing Query version");

			Query query = getEntityManager().createQuery(
					"select d.id, e.id from Department d join d.employees e where d.id <= 5 " + " order by d.id, e.id");
			int gfr = query.getFirstResult();
			if (gfr != 0) {
				logger.log(Logger.Level.ERROR, "getFirstResult() - Expecting result=0, actual=" + gfr);
			} else {
				pass1 = true;
			}
			query.setFirstResult(13);
			gfr = query.getFirstResult();
			if (gfr != 13) {
				logger.log(Logger.Level.ERROR, "getFirstResult() - Expecting result=11, actual=" + gfr);
			} else {
				pass2 = true;
			}

			q = query.getResultList();

			if (TestUtil.traceflag) {
				logger.log(Logger.Level.TRACE, "query returned " + q.size() + " results.");
				int i = 0;
				for (Object obj : q) {
					logger.log(Logger.Level.TRACE, (i++) + "=" + Arrays.asList((Object[]) obj));
				}

			}
			if (q.size() == 8) {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				// each element of the list q should be a size-2 array,
				// for instance [4,5]
				int i = 0;
				pass3 = true;

				for (Object obj : q) {
					Object[] expected = expectedResultSet[i++];
					Object[] departmentIdEmpId = null;
					if (obj instanceof Object[]) {
						logger.log(Logger.Level.TRACE,
								"The element in the result list is of type Object[], continue . . .");
						// good, this element of type Object[]
						departmentIdEmpId = (Object[]) obj;
						if (!Arrays.equals(expected, departmentIdEmpId)) {
							logger.log(Logger.Level.ERROR, "Expecting element value: " + Arrays.asList(expected)
									+ ", actual element value: " + Arrays.asList(departmentIdEmpId));
							pass4 = false;
							break;
						}
					} else {
						logger.log(Logger.Level.ERROR, "The element in the result list is not of type Object[]:" + obj);
						pass4 = false;
						break;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: 10, " + "got: " + q.size());
				logger.log(Logger.Level.ERROR, "Expected results:");
				int i = 0;
				for (Object obj[] : expectedResultSet) {
					logger.log(Logger.Level.ERROR, (i++) + "=" + Arrays.toString(obj));
				}

				logger.log(Logger.Level.ERROR, "Actual results:");
				i = 0;
				for (Object obj : q) {
					logger.log(Logger.Level.ERROR, (i++) + "=" + Arrays.asList((Object[]) obj));
				}

			}
			getEntityTransaction().commit();
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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Integer> query = getEntityManager()
					.createQuery("select e.id from Employee e where e.id <= 10 order by e.id", Integer.class);
			int gfr = query.getFirstResult();
			if (gfr != 0) {
				logger.log(Logger.Level.ERROR, "getFirstResult() - Expecting result=0, actual=" + gfr);
			} else {
				pass11 = true;
			}
			query.setFirstResult(5);
			gfr = query.getFirstResult();
			if (gfr != 5) {
				logger.log(Logger.Level.ERROR, "getFirstResult() - Expecting result=5, actual=" + gfr);
			} else {
				pass12 = true;
			}

			Collection<Integer> actual = query.getResultList();
			String[] expected = new String[5];
			expected[0] = "6";
			expected[1] = "7";
			expected[2] = "8";
			expected[3] = "9";
			expected[4] = "10";

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass13 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass14 = false;
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

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass11 || !pass12 || !pass13 || !pass14) {
			throw new Exception("setFirstResultTest failed");
		}

	}

	/*
	 * @testName: setFirstResultIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:585; PERSISTENCE:JAVADOC:618
	 * 
	 * @test_Strategy: Create a Query. setFirstResult(-5) and verify
	 * IllegalArgumentException is thrown
	 *
	 * Create a TypedQuery. setFirstResult(-5) and verify IllegalArgumentException
	 * is thrown
	 */
	@Test
	public void setFirstResultIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			Query query = getEntityManager().createQuery("select e.id from Employee e where e.id <= 10 order by e.id");
			query.setFirstResult(-5);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {

			TypedQuery<Integer> query = getEntityManager()
					.createQuery("select e.id from Employee e where e.id <= 10 order by e.id", Integer.class);
			query.setFirstResult(-5);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("setFirstResultIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:405; PERSISTENCE:JAVADOC:645
	 * 
	 * @test_Strategy: call query.getParameter(String, Class) and verify returned
	 * Parameter or that IllegalStateException is thrown. call
	 * TypedQuery.getParameter(String, Class) and verify returned Parameter or that
	 * IllegalStateException is thrown.
	 */
	@Test
	public void getParameterTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			Query q = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter("fName", "Stephen");

			Parameter p = q.getParameter("fName", String.class);
			String s = p.getName();
			if (!s.equals("fName")) {
				logger.log(Logger.Level.ERROR, "getName() - Expected:fName, actual:" + s);
			} else {
				pass1 = true;
			}
		} catch (IllegalStateException ise) {
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> q = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter("fName", "Stephen");

			Parameter p = q.getParameter("fName", String.class);
			String s = p.getName();
			if (!s.equals("fName")) {

				logger.log(Logger.Level.ERROR, "getName() - Expected:fName, actual:" + s);
			} else {
				pass2 = true;
			}
		} catch (IllegalStateException ise) {
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("getParameterTest failed");
		}
	}

	/*
	 * @testName: getParameterIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:558; PERSISTENCE:JAVADOC:646
	 * 
	 * @test_Strategy: call Query.getParameter(String, String) with a name that does
	 * not exist and verify that IllegalArgumentException is thrown.
	 *
	 * call TypedQuery.getParameter(String, String) with a name that does not match
	 * and verify that IllegalArgumentException is thrown.
	 */
	@Test
	public void getParameterIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query q = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName");

			q.getParameter("doesnotexist", String.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> q = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class);

			q.getParameter("doesnotexist", String.class);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("getParameterIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterIllegalArgumentException2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:556; PERSISTENCE:JAVADOC:643
	 * 
	 * @test_Strategy: call Query.getParameter(String) with name that does not exist
	 * and verify that IllegalArgumentException is thrown. call
	 * TypedQuery.getParameter(String) with name that does not exist and verify that
	 * IllegalArgumentException is thrown.
	 */
	@Test
	public void getParameterIllegalArgumentException2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query q = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName");

			q.getParameter("doesnotexist");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> q = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class);

			q.getParameter("doesnotexist");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterIllegalArgumentException2Test failed");
		}
	}

	/*
	 * @testName: getParameterIntClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:407; PERSISTENCE:JAVADOC:648
	 * 
	 * @test_Strategy: call Query.getParameter(int, Class) and verify returned
	 * Parameter or that IllegalStateException is thrown. Also a null should be
	 * returned by getName() for a positional parameter call
	 * TypedQuery.getParameter(int, Class) and verify returned Parameter or that
	 * IllegalStateException is thrown. Also a null should be returned by getName()
	 * for a positional parameter
	 *
	 */
	@Test
	public void getParameterIntClassTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query q = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1").setParameter(1,
					"Stephen");

			Parameter p = q.getParameter(1, String.class);
			String s = p.getName();
			if (s != null) {
				logger.log(Logger.Level.ERROR, "getName() - Expected:null, actual:" + s);
			} else {
				pass1 = true;
			}

		} catch (IllegalStateException ise) {
			// implementation does not support this use
			pass1 = true;
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> q = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, "Stephen");

			Parameter p = q.getParameter(1, String.class);
			String s = p.getName();
			if (s != null) {
				logger.log(Logger.Level.ERROR, "getName() - Expected:null, actual:" + s);
			} else {
				pass2 = true;
			}

		} catch (IllegalStateException ise) {
			// implementation does not support this use
			pass2 = true;
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterIntClassTest failed");
		}
	}

	/*
	 * @testName: getParameterIntIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:560; PERSISTENCE:JAVADOC:649
	 * 
	 * @test_Strategy: create query and set a positional parameter. Verify
	 * getParameter for a position that does not exist throws
	 * IllegalArgumentException create TypedQuery and set a positional parameter.
	 * Verify getParameter for a position that does not exist throws
	 * IllegalArgumentException*
	 */
	@Test
	public void getParameterIntIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.TRACE, "Creating query for getParameterIntIllegalArgumentExceptionTest");
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1")
					.setParameter(1, "foo");

			query.getParameter(99);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, "foo");
			query.getParameter(99);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterIntIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueParameterTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:404; PERSISTENCE:JAVADOC:409;
	 * PERSISTENCE:JAVADOC:645; PERSISTENCE:JAVADOC:656; PERSISTENCE:SPEC:1540;
	 * 
	 * @test_Strategy: create query and set a String parameter. Verify
	 * getParameterValue can retrieve that value
	 *
	 * create TypedQuery and set a String parameter. Verify getParameterValue can
	 * retrieve that value
	 */
	@Test
	public void getParameterValueParameterTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedName = "fName";
		String expectedValue = "Stephen";

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter(expectedName, expectedValue);

			Parameter p = query.getParameter(expectedName);
			if (p != null) {
				String s = (String) query.getParameterValue(p);
				if (!s.equals(expectedValue)) {
					logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
				} else {
					pass1 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParameter returned null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter(expectedName, expectedValue);

			Parameter p = query.getParameter(expectedName);
			if (p != null) {
				String s = (String) query.getParameterValue(p);

				if (!s.equals(expectedValue)) {
					logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
				} else {
					pass2 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParameter returned null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueParameterTest failed");
		}
	}

	/*
	 * @testName: getParameterValueParameterIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:565; PERSISTENCE:JAVADOC:657
	 * 
	 * @test_Strategy: create two querys and set a String parameter. Try to get the
	 * first parameter value from the second query and verify
	 * IllegalArgumentException is thrown create two TypedQuerys and set a String
	 * parameter. Try to get the first parameter value from the second query and
	 * verify IllegalArgumentException is thrown*
	 */
	@Test
	public void getParameterValueParameterIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query1 = getEntityManager().createQuery("select e from Employee e where e.firstName = :fname1")
					.setParameter("fname1", "fnameValue1");
			Query query2 = getEntityManager().createQuery("select e from Employee e where e.firstName = :fname2")
					.setParameter("fname2", "fnameValue2");

			Parameter p = query2.getParameter("fname2");
			query1.getParameterValue(p);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query1 = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fname1", Employee.class)
					.setParameter("fname1", "fnameValue1");
			TypedQuery<Employee> query2 = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fname2", Employee.class)
					.setParameter("fname2", "fnameValue2");

			Parameter p = query2.getParameter("fname2");
			query1.getParameterValue(p);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueParameterIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueParameterIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:566; PERSISTENCE:JAVADOC:658
	 * 
	 * @test_Strategy: create query with a parameter that is not set. call
	 * getParameterValue(Parameter) for that parameter and verify
	 * IllegalArgumentException is thrown create TypedQuery with a parameter that is
	 * not set. call getParameterValue(Parameter) for that parameter and verify
	 * IllegalArgumentException is thrown
	 */
	@Test
	public void getParameterValueParameterIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");
		Query query1 = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName1");

		Set<Parameter<?>> set = query1.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass1 = true;
		}
		try {
			for (Parameter p : set) {
				query1.getParameterValue(p);
			}
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass2 = true;

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		TypedQuery<Employee> tquery1 = getEntityManager()
				.createQuery("select e from Employee e where e.firstName = :fName1", Employee.class);
		set = tquery1.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass3 = true;
		}
		try {
			for (Parameter p : set) {
				tquery1.getParameterValue(p);
			}
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass4 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("getParameterValueParameterIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterParameterObjectIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:589; PERSISTENCE:JAVADOC:622;
	 * PERSISTENCE:JAVADOC:692;
	 * 
	 * @test_Strategy: create query with a parameter that is not set. call
	 * setParameter(Parameter,Object) for that parameter and verify
	 * IllegalArgumentException is thrown create TypedQuery with a parameter that is
	 * not set. call setParameter(Parameter,Object) for that parameter and verify
	 * IllegalArgumentException is thrown
	 */
	@Test
	public void setParameterParameterObjectIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");
		Query query1 = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName1");
		Query query2 = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName2");

		Set<Parameter<?>> set = query2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass1 = true;
		}
		try {
			for (Parameter p : set) {
				query1.setParameter(p, "object1");
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		TypedQuery<Employee> tquery1 = getEntityManager()
				.createQuery("select e from Employee e where e.firstName = :fName1", Employee.class);

		TypedQuery<Employee> tquery2 = getEntityManager()
				.createQuery("select e from Employee e where e.firstName = :fName2", Employee.class);
		set = tquery2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass3 = true;
		}
		try {
			for (Parameter p : set) {
				tquery1.setParameter(p, "object1");
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass4 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setParameterParameterObjectIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName:
	 * setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:590; PERSISTENCE:JAVADOC:623;
	 * PERSISTENCE:JAVADOC:694;
	 * 
	 * @test_Strategy: create query with a parameter that is not set. call
	 * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
	 * IllegalArgumentException is thrown create TypedQuery with a parameter that is
	 * not set. call setParameter(Parameter,Calendar,TemporalType) for that
	 * parameter and verify IllegalArgumentException is thrown
	 */
	@Test
	public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");
		Query query1 = getEntityManager().createQuery("select e from Employee e where e.hireDate = :date1");
		Query query2 = getEntityManager().createQuery("select e from Employee e where e.hireDate = :date2");

		Set<Parameter<?>> set = query2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass1 = true;
		}
		try {
			for (Parameter p : set) {
				query1.setParameter(p, getCalDate(), TemporalType.DATE);
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		TypedQuery<Employee> tquery1 = getEntityManager()
				.createQuery("select e from Employee e where e.hireDate = :date1", Employee.class);

		TypedQuery<Employee> tquery2 = getEntityManager()
				.createQuery("select e from Employee e where e.hireDate = :date2", Employee.class);
		set = tquery2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass3 = true;
		}
		try {
			for (Parameter p : set) {
				tquery1.setParameter(p, getCalDate(), TemporalType.DATE);
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass4 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:591; PERSISTENCE:JAVADOC:624;
	 * PERSISTENCE:JAVADOC:696;
	 * 
	 * @test_Strategy: create query with a parameter that is not set. call
	 * setParameter(Parameter,Date,TemporalType) for that parameter and verify
	 * IllegalArgumentException is thrown create TypedQuery with a parameter that is
	 * not set. call setParameter(Parameter,Date,TemporalType) for that parameter
	 * and verify IllegalArgumentException is thrown
	 */
	@Test
	public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");
		Query query1 = getEntityManager().createQuery("select e from Employee e where e.hireDate = :date1");
		Query query2 = getEntityManager().createQuery("select e from Employee e where e.hireDate = :date2");

		Set<Parameter<?>> set = query2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass1 = true;
		}
		try {
			for (Parameter p : set) {
				query1.setParameter(p, dateId, TemporalType.DATE);
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		TypedQuery<Employee> tquery1 = getEntityManager()
				.createQuery("select e from Employee e where e.hireDate = :date1", Employee.class);

		TypedQuery<Employee> tquery2 = getEntityManager()
				.createQuery("select e from Employee e where e.hireDate = :date2", Employee.class);
		set = tquery2.getParameters();
		if (set.size() != 1) {
			logger.log(Logger.Level.ERROR, "Expected one parameter, actual=" + set.size());
			for (Parameter p : set) {
				logger.log(Logger.Level.ERROR, "Parameter:" + p.toString());
			}
		} else {
			pass3 = true;
		}
		try {
			for (Parameter p : set) {
				tquery1.setParameter(p, dateId, TemporalType.DATE);
			}
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass4 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:410;PERSISTENCE:JAVADOC:659
	 * 
	 * @test_Strategy: create query and set a String parameter. Verify
	 * getParameterValue can retrieve that value create TypedQuery and set a String
	 * parameter. Verify getParameterValue can retrieve that value
	 */
	@Test
	public void getParameterValueStringTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedName = "fName";
		String expectedValue = "Stephen";

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter(expectedName, expectedValue);

			String s = (String) query.getParameterValue(expectedName);

			if (!s.equals(expectedValue)) {
				logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
			} else {
				pass1 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter(expectedName, expectedValue);
			String s = (String) query.getParameterValue(expectedName);

			if (!s.equals(expectedValue)) {
				logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
			} else {
				pass2 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueStringTest failed");
		}
	}

	/*
	 * @testName: getParameterValueStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:568; PERSISTENCE:JAVADOC:661
	 * 
	 * @test_Strategy: create a query and set a String parameter. Try to get the
	 * parameter value from the query and verify IllegalArgumentException is thrown
	 * create a TypedQuery and set a String parameter. Try to get the parameter
	 * value from the query and verify IllegalArgumentException is thrown*
	 */
	@Test
	public void getParameterValueStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName1")
					.setParameter("fName1", "fnameValue");

			query.getParameterValue("fName2");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName1", Employee.class)
					.setParameter("fName1", "fnameValue");

			query.getParameterValue("fName2");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueStringIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:567; PERSISTENCE:JAVADOC:660
	 * 
	 * @test_Strategy: create a query and don't set a name parameter. Verify
	 * getParameterValue for a parameter that is not bound throws an
	 * IllegalStateException
	 */
	@Test
	public void getParameterValueStringIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fname1");

			query.getParameterValue("fname1");
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fname1", Employee.class);

			query.getParameterValue("fname1");
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueStringIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:411; PERSISTENCE:JAVADOC:662
	 * 
	 * @test_Strategy: create query and set a positional parameter. Verify
	 * getParameterValue can retrieve that value create TypedQuery and set a
	 * positional parameter. Verify getParameterValue can retrieve that value
	 */
	@Test
	public void getParameterValueIntTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedValue = "Stephen";

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1")
					.setParameter(1, expectedValue);

			String s = (String) query.getParameterValue(1);

			if (!s.equals(expectedValue)) {
				logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
			} else {
				pass1 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, expectedValue);
			String s = (String) query.getParameterValue(1);

			if (!s.equals(expectedValue)) {
				logger.log(Logger.Level.ERROR, "Expected:" + expectedValue + ",Actual=" + s);
			} else {
				pass2 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueIntTest failed");
		}
	}

	/*
	 * @testName: getParameterValueIntIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:570; PERSISTENCE:JAVADOC:664
	 * 
	 * @test_Strategy: create query and set a positional parameter. Verify
	 * getParameterValue for a position that does not exist throws
	 * IllegalArgumentException create TypedQuery and set a positional parameter.
	 * Verify getParameterValue for a position that does not exist throws
	 * IllegalArgumentException
	 */
	@Test
	public void getParameterValueIntIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1")
					.setParameter(1, "foo");

			query.getParameterValue(99);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, "foo");
			query.getParameterValue(99);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueIntIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getParameterValueIntIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:569; PERSISTENCE:JAVADOC:663
	 * 
	 * @test_Strategy: create query and don't set a positional parameter. Verify
	 * getParameterValue for a position that is not bound throws
	 * IllegalStateException create TypedQuery and don't set a positional parameter.
	 * Verify getParameterValue for a position that is not bound throws
	 * IllegalStateException
	 */
	@Test
	public void getParameterValueIntIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1");
			query.getParameterValue(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class);
			query.getParameterValue(1);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		if (!pass1 || !pass2) {
			throw new Exception("getParameterValueIntIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameter1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:176; PERSISTENCE:JAVADOC:412;
	 * PERSISTENCE:JAVADOC:448; PERSISTENCE:JAVADOC:697; PERSISTENCE:SPEC:1305;
	 * 
	 * @test_Strategy: Obtain employees using valid name/value data in query. Obtain
	 * employees using valid name/value data in a TypedQuery.
	 */
	@Test
	public void setParameter1Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Integer[] expected = new Integer[2];
		expected[0] = empRef[4].getId();
		expected[1] = empRef[6].getId();

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter("fName", "Stephen");

			List<Employee> result = query.getResultList();

			if (!checkEntityPK(result, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter("fName", "Stephen");

			List<Employee> result = query.getResultList();

			if (!checkEntityPK(result, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameter1Test failed");
		}
	}

	/*
	 * @testName: setParameter2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1306; PERSISTENCE:SPEC:1307;
	 * 
	 * @test_Strategy: Test named parameters for case-sensitive
	 */
	@Test
	public void setParameter2Test() throws Exception {
		boolean pass = false;
		Integer[] expected = new Integer[3];
		expected[0] = empRef[0].getId();
		expected[1] = empRef[4].getId();
		expected[2] = empRef[6].getId();

		try {
			getEntityTransaction().begin();

			Query query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName or e.firstName = :Fname")
					.setParameter("fName", "Stephen").setParameter("Fname", "Alan");

			List<Employee> result = query.getResultList();

			if (!checkEntityPK(result, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();

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

		if (!pass) {
			throw new Exception("setParameter2Test failed");
		}
	}

	/*
	 * @testName: setParameterStringObject1IllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:732; PERSISTENCE:JAVADOC:176;
	 * PERSISTENCE:JAVADOC:625
	 * 
	 * @test_Strategy: setParameter(String, Object) containing an argument of an
	 * incorrect type should throw an IllegalArgumentException.
	 * TypedQuery.setParameter(String, Object) containing an argument of an
	 * incorrect type should throw an IllegalArgumentException.*
	 */
	@Test
	public void setParameterStringObject1IllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select e from Employee e where e.firstName = :fName").setParameter("fName",
					5.0F);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter("fName", 5.0F);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterStringObject1IllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterStringObject2IllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:732; PERSISTENCE:JAVADOC:176;
	 * PERSISTENCE:JAVADOC:592; PERSISTENCE:JAVADOC:625; PERSISTENCE:JAVADOC:698;
	 * 
	 * @test_Strategy: setParameter(String, Object) containing a parameter name that
	 * does not exist should throw an IllegalArgumentException.
	 * TypedQuery.setParameter(String, Object) containing a parameter name that does
	 * not exist should throw an IllegalArgumentException.
	 *
	 */
	@Test
	public void setParameterStringObject2IllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter("doesnotexist", "foo");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter("doesnotexist", "foo");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterStringObject2IllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterStringDateTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:177; PERSISTENCE:JAVADOC:450;
	 * PERSISTENCE:JAVADOC:701; PERSISTENCE:SPEC:1514; PERSISTENCE:SPEC:1514.1;
	 * 
	 * @test_Strategy: Obtain employees using valid name/value data in query. Obtain
	 * employees using valid name/value data in TypedQuery.
	 */
	@Test
	public void setParameterStringDateTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[0]);
		cEmp.add(empRef[19]);

		logger.log(Logger.Level.INFO, "Testing Query version");

		try {
			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select e from Employee e where e.hireDate = :hDate");
			Collection<Employee> q = query.setParameter("hDate", d1, TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {

			getEntityTransaction().begin();

			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = :hDate", Employee.class);

			Collection<Employee> q = tquery.setParameter("hDate", d1, TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameterStringDateTemporalTypeTest failed");
		}
	}

	/*
	 * @testName: setParameterStringDateTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:730; PERSISTENCE:JAVADOC:594;
	 * PERSISTENCE:JAVADOC:627; PERSISTENCE:JAVADOC:702;
	 * 
	 * @test_Strategy: Query.setParameter(String name, Date value, TemporalType
	 * type) containing a parameter name that does not correspond to parameter in
	 * query string should throw an IllegalArgumentException.
	 * TypedQuery.setParameter(String name, Date value, TemporalType type)
	 * containing a parameter name that does not correspond to parameter in query
	 * string should throw an IllegalArgumentException.
	 */
	@Test
	public void setParameterStringDateTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select e from Employee e where e.hireDate = :hDate")
					.setParameter("doesnotexist", d1, TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select e from Employee e where e.hireDate = :hDate", Employee.class)
					.setParameter("doesnotexist", d1, TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterStringDateTemporalTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterStringCalendarTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:178; PERSISTENCE:JAVADOC:449;
	 * PERSISTENCE:JAVADOC:699;
	 * 
	 * @test_Strategy: Obtain employees using Query.setParameter(String, Calendar,
	 * TemporalType). Obtain employees using TypedQuery.setParameter(String,
	 * Calendar, TemporalType).
	 */
	@Test
	public void setParameterStringCalendarTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[20]);

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select e from Employee e where e.hireDate = :hDate");
			Collection<Employee> q = query.setParameter("hDate", getCalDate(), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();
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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = :hDate", Employee.class);
			Collection<Employee> q = tquery.setParameter("hDate", getCalDate(), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();
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
			throw new Exception("setParameterStringCalendarTemporalTypeTest failed");
		}
	}

	/*
	 * @testName:
	 * setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:178; PERSISTENCE:JAVADOC:593;
	 * PERSISTENCE:JAVADOC:626; PERSISTENCE:JAVADOC:700;
	 * 
	 * @test_Strategy: Query.setParameter(String, Calendar, TemporalType) containing
	 * a parameter name that does not correspond to parameter in query string should
	 * throw an IllegalArgumentException.
	 *
	 * TypedQuery.setParameter(String, Calendar, TemporalType) containing a
	 * parameter name that does not correspond to parameter in query string should
	 * throw an IllegalArgumentException.
	 */
	@Test
	public void setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest() throws Exception {
		final java.util.Calendar c = Calendar.getInstance();
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select d from Department d where :param > 1").setParameter("badName", c,
					TemporalType.TIMESTAMP);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select d from Department d where :param > 1", Department.class)
					.setParameter("badName", c, TemporalType.TIMESTAMP);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterIntObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:179; PERSISTENCE:JAVADOC:451;
	 * PERSISTENCE:JAVADOC:703;
	 * 
	 * @test_Strategy: Obtain employees using positional parameter data in query.
	 * Obtain employees using positional parameter data in TypedQuery.
	 */
	@Test
	public void setParameterIntObjectTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[4]);
		cEmp.add(empRef[6]);

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1");
			Collection<Employee> q = query.setParameter(1, "Stephen").getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class);
			Collection<Employee> q = tquery.setParameter(1, "Stephen").getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameterIntObjectTest failed");
		}
	}

	/*
	 * @testName: setParameterIntObjectIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:595; PERSISTENCE:JAVADOC:628;
	 * PERSISTENCE:JAVADOC:704;
	 * 
	 * @test_Strategy: Query.setParameter(int position, Object value) which sets a
	 * positional parameter which is not used in the query string. An
	 * IllegalArgumentException should be thrown.
	 *
	 * TypedQuery.setParameter(int position, Object value) which sets a positional
	 * parameter which is not used in the query string. An IllegalArgumentException
	 * should be thrown.
	 */
	@Test
	public void setParameterIntObjectIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;

		logger.log(Logger.Level.INFO, "Testing query version");
		try {
			logger.log(Logger.Level.INFO, "Testing a parm that does not exist ");
			getEntityManager().createQuery("select e from Employee e where e.firstName = ?1").setParameter(1, "Kellie")
					.setParameter(2, "Lee");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		try {
			logger.log(Logger.Level.INFO, "Testing a parm of incorrect type");
			getEntityManager().createQuery("select e from Employee e where e.firstName = ?1").setParameter(1, 1);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			logger.log(Logger.Level.INFO, "Testing a parm that does not exist");
			getEntityManager().createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, "Kellie").setParameter(2, "Lee");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass3 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		try {
			logger.log(Logger.Level.INFO, "Testing a parm of incorrect type");
			getEntityManager().createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, 1);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass4 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("setParameterIntObjectIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameterIntDateTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:180; PERSISTENCE:JAVADOC:453;
	 * PERSISTENCE:JAVADOC:707;
	 * 
	 * @test_Strategy: Obtain employees using positional parameter data in the
	 * query.
	 *
	 * Obtain employees using positional parameter data in the TypedQuery.
	 */
	@Test
	public void setParameterIntDateTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[0]);
		cEmp.add(empRef[19]);

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();

			Collection<Employee> q = getEntityManager().createQuery("select e from Employee e where e.hireDate = ?1")
					.setParameter(1, d1, TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = ?1", Employee.class);
			Collection<Employee> q = tquery.setParameter(1, d1, TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameterIntDateTemporalTypeTest failed");
		}
	}

	/*
	 * @testName: setParameterIntDateTemporalTypeIllegalArgumentException1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:180; PERSISTENCE:JAVADOC:597;
	 * PERSISTENCE:JAVADOC:630; PERSISTENCE:JAVADOC:708;
	 * 
	 * @test_Strategy: Query.setParameter(int position, Date value, TemporalType
	 * type) containing a positional parameter that does not correspond to parameter
	 * in query string should throw an IllegalArgumentException.
	 * TypedQuery.setParameter(int position, Date value, TemporalType type)
	 * containing a positional parameter that does not correspond to parameter in
	 * query string should throw an IllegalArgumentException.
	 */
	@Test
	public void setParameterIntDateTemporalTypeIllegalArgumentException1Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select d from Department d where :hDate > 1").setParameter(5, d1,
					TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select d from Department d where :hDate > 1", Department.class)
					.setParameter(5, d1, TemporalType.DATE);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterIntDateTemporalTypeIllegalArgumentException1Test failed");
		}
	}

	/*
	 * @testName: setParameterIntCalendarTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:181; PERSISTENCE:JAVADOC:452;
	 * PERSISTENCE:JAVADOC:705;
	 * 
	 * @test_Strategy: Obtain employees using Query.setParameter(int position,
	 * Calendar value, TemporalType type).
	 *
	 * Obtain employees using TypedQuery.setParameter(int position, Calendar value,
	 * TemporalType type).
	 */
	@Test
	public void setParameterIntCalendarTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[20]);

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();

			Collection<Employee> q = getEntityManager().createQuery("select e from Employee e where e.hireDate = ?1")
					.setParameter(1, getCalDate(), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = ?1", Employee.class);

			Collection<Employee> q = tquery.setParameter(1, getCalDate(), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameterIntCalendarTemporalTypeTest failed");
		}
	}

	/*
	 * @testName: setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:596; PERSISTENCE:JAVADOC:629;
	 * PERSISTENCE:JAVADOC:706
	 * 
	 * @test_Strategy: Query.setParameter(int position, Calendar value, TemporalType
	 * type) containing a parameter name that does not correspond to parameter in
	 * query string should throw an IllegalArgumentException.
	 *
	 * TypedQuery.setParameter(int position, Calendar value, TemporalType type)
	 * containing a parameter name that does not correspond to parameter in query
	 * string should throw an IllegalArgumentException.
	 */
	@Test
	public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest() throws Exception {
		final java.util.Calendar c = Calendar.getInstance();
		boolean pass1 = false;
		boolean pass2 = false;
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityManager().createQuery("select d from Department d where ?1 > 1").setParameter(5, c,
					TemporalType.TIMESTAMP);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityManager().createQuery("select d from Department d where ?1 > 1", Department.class).setParameter(5,
					c, TemporalType.TIMESTAMP);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: setParameter7Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:414; PERSISTENCE:JAVADOC:445;
	 * PERSISTENCE:JAVADOC:691;
	 * 
	 * @test_Strategy: create Query and set a positional parameter. Use
	 * setParameter(Parameter, Object) to change the original parameter value then
	 * execute query.
	 *
	 * create TypedQuery and set a positional parameter. Use setParameter(Parameter,
	 * Object) to change the original parameter value then execute query.
	 */
	@Test
	public void setParameter7Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[1]);

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = ?1")
					.setParameter(1, "Stephen");

			Parameter p = query.getParameter(1);
			query.setParameter(p, "Arthur");

			Collection<Employee> q = query.getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = ?1", Employee.class)
					.setParameter(1, "Stephen");

			Parameter p = query.getParameter(1);
			query.setParameter(p, "Arthur");

			Collection<Employee> q = query.getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameter7Test failed");
		}
	}

	/*
	 * @testName: setParameterParameterCalendarTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:415; PERSISTENCE:JAVADOC:446;
	 * PERSISTENCE:JAVADOC:693;
	 * 
	 * @test_Strategy: create query with a parameter. call
	 * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
	 * the correct result is returned create TypedQuery with a parameter. call
	 * setParameter(Parameter,Calendar,TemporalType) for that parameter and verify
	 * the correct result is returned
	 */
	@Test
	public void setParameterParameterCalendarTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[20]);
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.hireDate = ?1");

			Parameter p = query.getParameter(1);
			query.setParameter(p, getCalDate(), TemporalType.DATE);

			Collection<Employee> q = query.getResultList();
			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = ?1", Employee.class);

			Parameter p = tquery.getParameter(1);
			tquery.setParameter(p, getCalDate(), TemporalType.DATE);

			Collection<Employee> q = tquery.getResultList();
			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("setParameterParameterCalendarTemporalTypeTest failed");
		}

	}

	/*
	 * @testName: setParameterParameterDateTemporalTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:416; PERSISTENCE:JAVADOC:447;
	 * PERSISTENCE:JAVADOC:695;
	 * 
	 * @test_Strategy: create query with a parameter. call
	 * setParameter(Parameter,Date,TemporalType) for that parameter and verify the
	 * correct result is returned create TypedQuery with a parameter. call
	 * setParameter(Parameter,Date,TemporalType) for that parameter and verify the
	 * correct result is returned
	 */
	@Test
	public void setParameterParameterDateTemporalTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[5]);
		cEmp.add(empRef[14]);
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.INFO, "Testing Query version");
			Query query = getEntityManager().createQuery("select e from Employee e where e.hireDate = ?1");

			Parameter p = query.getParameter(1);
			query.setParameter(p, getUtilDate("2005-02-18"), TemporalType.DATE);

			Collection<Employee> q = query.getResultList();
			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Employee> tquery = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = ?1", Employee.class);

			Parameter p = tquery.getParameter(1);
			tquery.setParameter(p, getUtilDate("2005-02-18"), TemporalType.DATE);

			Collection<Employee> q = tquery.getResultList();
			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass1 || !pass2) {
			throw new Exception("setParameterParameterDateTemporalTypeTest failed");
		}

	}

	/*
	 * @testName: setParameter8Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:416
	 * 
	 * @test_Strategy: Obtain employees using Query.setParameter(int position, Date
	 * value, TemporalType type).
	 *
	 * Obtain employees using TypedQuery.setParameter(int position, Date value,
	 * TemporalType type).
	 */
	@Test
	public void setParameter8Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		Collection<Employee> cEmp = new ArrayList<Employee>();
		cEmp.add(empRef[5]);
		cEmp.add(empRef[14]);
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();

			Collection<Employee> q = getEntityManager().createQuery("select e from Employee e where e.hireDate = ?1")
					.setParameter(1, getUtilDate("2005-02-18"), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			getEntityTransaction().commit();

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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			Collection<Employee> q = getEntityManager()
					.createQuery("select e from Employee e where e.hireDate = ?1", Employee.class)
					.setParameter(1, getUtilDate("2005-02-18"), TemporalType.DATE).getResultList();

			if (!checkEntityPK(q, cEmp)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + cEmp.size() + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("setParameter8Test failed");
		}
	}

	/*
	 * @testName: getSingleResultNoResultExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:744; PERSISTENCE:JAVADOC:577;
	 * PERSISTENCE:JAVADOC:610; PERSISTENCE:JAVADOC:2706; PERSISTENCE:JAVADOC:673;
	 * 
	 * @test_Strategy: Query.getSingleResult() is expected to return a single
	 * result. If the query does not return a result, an NoResultException is
	 * thrown. TypedQuery.getSingleResult() is expected to return a single result.
	 * If the query does not return a result, an NoResultException is thrown.
	 */
	@Test
	public void getSingleResultNoResultExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityManager().createQuery("select d.name from Department d where d.id = 99").getSingleResult();
			logger.log(Logger.Level.ERROR, "NoResultException was not thrown");
			getEntityTransaction().commit();
		} catch (NoResultException nre) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected:" + nre);
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
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			getEntityManager().createQuery("select d.name from Department d where d.id = 99", String.class)
					.getSingleResult();
			logger.log(Logger.Level.ERROR, "NoResultException was not thrown");
			getEntityTransaction().commit();
		} catch (NoResultException nre) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected:" + nre);
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
			throw new Exception("getSingleResultNoResultExceptionTest failed");
		}
	}

	/*
	 * @testName: getSingleResultTransactionRequiredException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:581; PERSISTENCE:JAVADOC:614;
	 * PERSISTENCE:JAVADOC:2710; PERSISTENCE:JAVADOC:677;
	 * 
	 * @test_Strategy: create query that is an update which has a lock mode set
	 * without a transaction being active then call getSingleResult() and verify a
	 * TransactionRequiredException is thrown create TypedQuery that is an update
	 * which has a lock mode set without a transaction being active then call
	 * getSingleResult() and verify a TransactionRequiredException is thrown
	 */
	@Test
	public void getSingleResultTransactionRequiredException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");

		try {
			Query q = getEntityManager().createQuery("select d from  Department d where d.id = 1");
			q.setLockMode(LockModeType.WRITE);
			q.getSingleResult();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException was not thrown");
		} catch (TransactionRequiredException ise) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected");
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
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Department> q = getEntityManager().createQuery("select d from  Department d where d.id = 1",
					Department.class);

			q.setLockMode(LockModeType.WRITE);
			q.getSingleResult();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException was not thrown");
		} catch (TransactionRequiredException ise) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected");
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
			throw new Exception("getSingleResultTransactionRequiredException failed");
		}
	}

	/*
	 * @testName: getSingleResultNonUniqueResultExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:745; PERSISTENCE:JAVADOC:578;
	 * PERSISTENCE:JAVADOC:611; PERSISTENCE:JAVADOC:2707; PERSISTENCE:JAVADOC:674;
	 * 
	 * @test_Strategy: Query.getSingleResult() is expected to return a single
	 * result. If the query returns more than one result, a NonUniqueResultException
	 * is thrown. TypedQuery.getSingleResult() is expected to return a single
	 * result. If the query returns more than one result, a NonUniqueResultException
	 * is thrown.
	 */
	@Test
	public void getSingleResultNonUniqueResultExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();
			getEntityManager().createQuery("select d.name from Department d where d.id > 1").getSingleResult();
			logger.log(Logger.Level.ERROR, "NoResultException was not thrown");
			getEntityTransaction().commit();
		} catch (NonUniqueResultException nure) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected:" + nure);
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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			getEntityManager().createQuery("select d.name from Department d where d.id > 1", String.class)
					.getSingleResult();
			logger.log(Logger.Level.ERROR, "NoResultException was not thrown");
			getEntityTransaction().commit();
		} catch (NonUniqueResultException nure) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected:" + nure);
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
			throw new Exception("getSingleResultNonUniqueResultExceptionTest failed");
		}
	}

	/*
	 * @testName: isBoundTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:412; PERSISTENCE:JAVADOC:681
	 * 
	 * @test_Strategy: Create a query and set a parameter. Verify isbound knows
	 * setParameter has set a value. Create a TypedQuery and set a parameter. Verify
	 * isbound knows setParameter has set a value.
	 */
	@Test
	public void isBoundTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedName = "fName";
		String expectedValue = "Stephen";
		try {
			logger.log(Logger.Level.TRACE, "Creating query for isBoundTest");
			logger.log(Logger.Level.INFO, "Testing Query version");

			Query query = getEntityManager().createQuery("select e from Employee e where e.firstName = :fName")
					.setParameter(expectedName, expectedValue);

			Parameter p = query.getParameter(expectedName);
			if (p != null) {
				if (!query.isBound(p)) {
					logger.log(Logger.Level.ERROR,
							"isbound returned false even though a value is bound to the parameter " + expectedName + "["
									+ p.getName() + "]");
				} else {
					pass1 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParameter returned null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Employee> query = getEntityManager()
					.createQuery("select e from Employee e where e.firstName = :fName", Employee.class)
					.setParameter(expectedName, expectedValue);

			Parameter p = query.getParameter(expectedName);
			if (p != null) {
				if (!query.isBound(p)) {
					logger.log(Logger.Level.ERROR,
							"isbound returned false even though a value is bound to the parameter " + expectedName + "["
									+ p.getName() + "]");
				} else {
					pass2 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParameter returned null");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("isBoundTest failed");
		}
	}

	/*
	 * @testName: setFirstResult
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:172
	 * 
	 * @test_Strategy: If the select clause selects an object, then the number of
	 * rows skipped with setFirstResult will correspond to the number of objects
	 * specified by setFirstResult."
	 */
	@Test
	public void setFirstResult() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(2);
		expected.add(3);
		expected.add(3);
		expected.add(4);
		expected.add(4);
		expected.add(5);

		Collection<Integer> actual;

		logger.log(Logger.Level.INFO, "Testing query");
		try {
			getEntityTransaction().begin();

			Query q = getEntityManager()
					.createQuery("select e.department.id from Employee e where e.id < 10 order by e.department.id")
					.setFirstResult(3);
			actual = q.getResultList();
			if (!checkEntityPK(actual, expected, true)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.INFO, "Testing TypedQuery");
			TypedQuery<Integer> tq = getEntityManager()
					.createQuery("select e.department.id from Employee e where e.id < 10 order by e.department.id",
							Integer.class)
					.setFirstResult(3);
			actual = tq.getResultList();
			if (!checkEntityPK(actual, expected, true)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.size()
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("setFirstResult failed");
		}
	}

	/*
	 * @testName: queryAPITest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:729; PERSISTENCE:SPEC:731
	 * 
	 * @test_Strategy: setParameter(int position, Object value) which has a
	 * positional parameter value specified that does not correspond to a positional
	 * parameter in the query string. An IllegalArgumentException is thrown.
	 */
	@Test
	public void queryAPITest11() throws Exception {
		boolean pass = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();
			Query query = null;
			try {
				query = getEntityManager()
						.createQuery("select e from Employee e where e.firstName = ?1 and e.lastName = ?3")
						.setParameter(1, "Kellie").setParameter(2, "Lee");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exeception", e);
			}
			if (!pass) {
				try {
					query.getResultList();
				} catch (RuntimeException e) {
					logger.log(Logger.Level.INFO,
							"Did not get expected IllegalArgumentException when "
									+ "setting an invalid parameter on a query, but got "
									+ "expected RuntimeException when executing the query: " + e);
					pass = true;
				}
			}
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

		if (!pass) {
			throw new Exception("queryAPITest11 failed");
		}
	}

	/*
	 * @testName: queryAPITest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:729; PERSISTENCE:SPEC:731
	 * 
	 * @test_Strategy: setParameter(int position, Object value) which defines a
	 * value of the incorrect type should throw an IllegalArgumentException.
	 */
	@Test
	public void queryAPITest12() throws Exception {
		boolean pass = false;
		logger.log(Logger.Level.TRACE, "invoke query for queryAPITest12 ...");
		logger.log(Logger.Level.INFO, "Testing Query version");
		try {
			getEntityTransaction().begin();
			Query query = null;

			try {
				query = getEntityManager()
						.createQuery("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
						.setParameter(1, "Kate").setParameter(2, 10);
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exeception", e);
			}
			if (!pass) {
				try {
					query.getResultList();
				} catch (RuntimeException e) {
					logger.log(Logger.Level.INFO,
							"Did not get expected IllegalArgumentException when "
									+ "setting an invalid parameter on a query, but got "
									+ "expected RuntimeException when executing the query: " + e);
					pass = true;
				}
			}
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

		if (!pass) {
			throw new Exception("queryAPITest12 failed");
		}
	}

	/*
	 * @testName: setFirstResultIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:172; PERSISTENCE:JAVADOC:683;
	 * 
	 * @test_Strategy: setFirstResult(int startPosition) with a negative value for
	 * startPosition should throw an IllegalArgumentException.
	 */
	@Test
	public void setFirstResultIllegalArgumentException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		getEntityTransaction().begin();

		logger.log(Logger.Level.INFO, "Testing query version");
		try {

			getEntityManager().createQuery("select d from Department d").setFirstResult(-3);

			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");
		try {

			getEntityManager().createQuery("select d from Department d", Department.class).setFirstResult(-3);

			logger.log(Logger.Level.ERROR, "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		getEntityTransaction().rollback();

		if (!pass1 || !pass2) {
			throw new Exception("setFirstResultIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: setGetMaxResultsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:175; PERSISTENCE:JAVADOC:403;
	 * PERSISTENCE:JAVADOC:444; PERSISTENCE:JAVADOC:641; PERSISTENCE:JAVADOC:170;
	 * PERSISTENCE:JAVADOC:438
	 * 
	 * @test_Strategy: Using Query.setMaxResult() set the maximum number of results
	 * to a value which exceeds number of expected result and verify the result set.
	 * Using TypedQuery.setMaxResult() set the maximum number of results to a value
	 * which exceeds number of expected result and verify the result set.
	 */
	@Test
	public void setGetMaxResultsTest() throws Exception {
		Collection<Department> q;
		boolean pass1 = false;
		boolean pass2 = false;

		final Integer expected[] = { 1, 2, 3, 4, 5 };

		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();

			Query query = getEntityManager().createQuery("select d from Department d order by d.id");
			int gmr = query.getMaxResults();
			if (gmr != Integer.MAX_VALUE) {
				logger.log(Logger.Level.ERROR, "getMaxResults() called when setMaxResults() not called - Expected:"
						+ Integer.MAX_VALUE + ", actual:" + gmr);
				pass1 = false;
			} else {
				query.setMaxResults(15);
				gmr = query.getMaxResults();
				if (gmr != 15) {
					logger.log(Logger.Level.ERROR, "getMaxResults() - Expected: 15, Actual:" + gmr);
					pass1 = false;
				} else {
					q = query.getResultList();
					if (!checkEntityPK(q, expected)) {
						logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
								+ " references, got: " + q.size());
					} else {
						logger.log(Logger.Level.TRACE, "Expected results received");
						pass1 = true;
					}
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass1 = false;
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

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			TypedQuery<Department> query = getEntityManager().createQuery("select d from Department d order by d.id",
					Department.class);

			int gmr = query.getMaxResults();
			if (gmr != Integer.MAX_VALUE) {
				logger.log(Logger.Level.ERROR, "getMaxResults() called when setMaxResults() not called - Expected:"
						+ Integer.MAX_VALUE + ", actual:" + gmr);
				pass2 = false;
			} else {
				query.setMaxResults(15);
				gmr = query.getMaxResults();
				if (gmr != 15) {
					logger.log(Logger.Level.ERROR, "getMaxResults() - Expected: 15, Actual:" + gmr);
					pass2 = false;
				} else {
					q = query.getResultList();
					if (!checkEntityPK(q, expected)) {
						logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
								+ " references, got: " + q.size());
					} else {
						logger.log(Logger.Level.TRACE, "Expected results received");
						pass2 = true;
					}
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass2 = false;
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
			throw new Exception("setGetMaxResultsTest failed");
		}
	}

	/*
	 * @testName: setMaxResultsIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:588; PERSISTENCE:JAVADOC:621;
	 * PERSISTENCE:JAVADOC:690;
	 * 
	 * @test_Strategy: Call Query.setMaxResult(-1) and verify an
	 * IllegalArgumentException is thrown Call TypedQuery.setMaxResult(-1) and
	 * verify an IllegalArgumentException is thrown
	 */
	@Test
	public void setMaxResultsIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			getEntityTransaction().begin();

			getEntityManager().createQuery("select d from Department d order by d.id").setMaxResults(-15);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
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
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();

			getEntityManager().createQuery("select d from Department d order by d.id", Department.class)
					.setMaxResults(-15);
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
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
			throw new Exception("setMaxResultsIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: getResultListTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:573; PERSISTENCE:JAVADOC:606;
	 * PERSISTENCE:JAVADOC:668;
	 *
	 * @test_Strategy: Using Query.setMaxResult() set the maximum number of results
	 * to a value which exceeds number of expected result and verify the result set.
	 * Using TypedQuery.setMaxResult() set the maximum number of results to a value
	 * which exceeds number of expected result and verify the result set.
	 */
	@Test
	public void getResultListTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");

			Query query = getEntityManager().createQuery("select d from Department d ");
			query.setLockMode(LockModeType.READ);

			query.getResultList();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "Received expected TransactionRequiredException ");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			TypedQuery<Department> query = getEntityManager().createQuery("select d from Department d ",
					Department.class);
			query.setLockMode(LockModeType.READ);

			query.getResultList();
		} catch (TransactionRequiredException tre) {
			logger.log(Logger.Level.TRACE, "Received expected TransactionRequiredException ");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("getResultListTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: setMaxResults
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:175
	 * 
	 * @test_Strategy: Using setMaxResult() set the maximum number of results to a
	 * value which is less than that of the expected results and verify the result
	 * set returned is only contains the number of results requested to be
	 * retrieved.
	 */
	@Test
	public void setMaxResults() throws Exception {
		Collection<Department> q;
		boolean pass = false;
		int found = 0;
		final Integer expected[] = { 4, 1 };

		try {
			logger.log(Logger.Level.TRACE, "Invoking query");
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("select d from Department d order by d.name").setMaxResults(2)
					.getResultList();

			if (!checkEntityPK(q, expected)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected " + expected.length + " references, got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
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

		if (!pass) {
			throw new Exception("setMaxResults failed");
		}
	}

	/*
	 * @testName: queryAPITest16
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:571;
	 * 
	 * @test_Strategy: getResultList() should throw an IllegalStateException if
	 * called for an EJB QL Update statement.
	 */
	@Test
	public void queryAPITest16() throws Exception {
		boolean pass1 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();
			Query q = getEntityManager()
					.createQuery("UPDATE Employee e SET e.salary = e.salary * 10.0 where e.salary > :minsal")
					.setParameter("minsal", (float) 50000.0);
			q.getResultList();

			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
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

		if (!pass1) {
			throw new Exception("queryAPITest16 failed");
		}
	}

	/*
	 * @testName: queryAPITest17
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:170
	 * 
	 * @test_Strategy: getResultList() should throw an IllegalStateException if
	 * called for an EJB QL Delete statement.
	 */
	@Test
	public void queryAPITest17() throws Exception {
		Query q;
		boolean pass = false;

		try {
			logger.log(Logger.Level.TRACE, "Invoking query");
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("DELETE FROM Employee e where e.salary > :minsal").setParameter("minsal",
					(float) 50000.0);
			q.getResultList();

			getEntityTransaction().commit();
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass = true;
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

		if (!pass) {
			throw new Exception("queryAPITest17 failed");
		}
	}

	/*
	 * @testName: getSingleResultTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:171; PERSISTENCE:JAVADOC:439;
	 * PERSISTENCE:JAVADOC:672;
	 * 
	 * @test_Strategy: create query and call getSingleResult() and verify result
	 * create TypedQuery and call getSingleResult() and verify result
	 */
	@Test
	public void getSingleResultTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		try {
			logger.log(Logger.Level.INFO, "Testing Query version");
			getEntityTransaction().begin();
			Query q = getEntityManager().createQuery("select e from Employee e where e.id = 1");
			Object o = q.getSingleResult();
			if (o instanceof Employee) {
				Employee e = (Employee) o;
				if (e.getId() != 1) {
					logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
				} else {
					pass1 = true;
				}

			}

			getEntityTransaction().commit();

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
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			TypedQuery<Employee> q = getEntityManager().createQuery("select e from Employee e where e.id = 1",
					Employee.class);
			Employee e = q.getSingleResult();
			if (e.getId() != 1) {
				logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
			} else {
				pass2 = true;
			}

			getEntityTransaction().commit();

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
			throw new Exception("getSingleResultTest failed");
		}
	}

	/*
	 * @testName: getSingleResultIllegalStateException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:2708; PERSISTENCE:JAVADOC:579;
	 *
	 * @test_Strategy: getSingleResult() should throw an IllegalStateException if
	 * called for an update or delete statement.
	 */
	@Test
	public void getSingleResultIllegalStateException() throws Exception {
		Query q;
		boolean pass1 = false;
		boolean pass2 = false;
		getEntityTransaction().begin();

		logger.log(Logger.Level.INFO, "Testing delete query");
		try {
			q = getEntityManager().createQuery("DELETE FROM Employee e where e.salary > 50000.0");
			q.getSingleResult();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		logger.log(Logger.Level.INFO, "Testing update query");
		try {
			q = getEntityManager().createQuery("Update Employee e SET e.salary = e.salary * 10.0");
			q.getSingleResult();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		try {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in while rolling back TX:", re);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getSingleResultIllegalStateException failed");
		}
	}

	/*
	 * @testName: executeUpdateIllegalStateException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:551; PERSISTENCE:JAVADOC:632
	 * 
	 * @test_Strategy: Query.executeUpdate() should throw an IllegalStateException
	 * if called for a JPQL Select statement. TypedQuery.executeUpdate() should
	 * throw an IllegalStateException if called for a JPQL Select statement.
	 */
	@Test
	public void executeUpdateIllegalStateException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");

		try {

			getEntityTransaction().begin();
			getEntityManager().createQuery("select d.id from Department d").executeUpdate();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");

			getEntityTransaction().commit();

		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
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
		logger.log(Logger.Level.INFO, "Testing TypedQuery version");

		try {
			getEntityTransaction().begin();
			getEntityManager().createQuery("select d.id from Department d", Integer.class).executeUpdate();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
			getEntityTransaction().commit();

		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected");
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
			throw new Exception("executeUpdateIllegalStateException failed");
		}
	}

	/*
	 * @testName: executeUpdateTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:552;
	 * 
	 * @test_Strategy: Query.executeUpdate() should throw an
	 * TransactionRequiredException when no Transaction is active
	 */
	@Test
	public void executeUpdateTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;

		logger.log(Logger.Level.INFO, "Testing Query version");

		try {
			getEntityManager().createQuery("update Department d  set d.name = NULLIF(d.name, 'Engineering')")
					.executeUpdate();
			logger.log(Logger.Level.ERROR, "TransactionRequiredException was not thrown");
		} catch (TransactionRequiredException ise) {
			logger.log(Logger.Level.TRACE, "TransactionRequiredException Caught as Expected");
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

		if (!pass1) {
			throw new Exception("executeUpdateTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: queryAPITest21
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:173
	 * 
	 * @test_Strategy: setFlushMode - AUTO
	 *
	 */
	@Test
	public void queryAPITest21() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Starting queryAPITest21");
			Department dept1 = getEntityManager().find(Department.class, 1);
			dept1.setName("Research and Development");
			getEntityManager().createQuery("SELECT d FROM Department d WHERE d.name = 'Research and Development'")
					.setFlushMode(FlushModeType.AUTO).getResultList();

			Department newDepartment = getEntityManager().find(Department.class, 1);
			if (newDepartment.getName().equals("Research and Development")) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected result:Research and Development, actual:" + newDepartment.getName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in while rolling back TX:", re);
			}
		}

		if (!pass) {
			throw new Exception("queryAPITest21 failed");
		}
	}

	/*
	 * @testName: queryAPITest22
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:746.2
	 * 
	 * @test_Strategy: Update Query
	 *
	 */
	@Test
	public void queryAPITest22() throws Exception {
		Query q;
		int result_size = 0;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Setting department name to IT for id 1");
			q = getEntityManager().createQuery("update Department d set d.name='IT' where d.id=1");

			result_size = q.executeUpdate();
			if (result_size == 1) {
				logger.log(Logger.Level.TRACE, "Updated 1 rows");
			}

			doFlush();
			clearCache();
			Department dept = getEntityManager().find(Department.class, 1);
			if (dept != null) {
				if (dept.getId() == 1) {
					if (dept.getName().equals("IT")) {
						logger.log(Logger.Level.TRACE, "Received expected name:" + dept.getName());
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected d.name =" + dept.getName());
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received incorrect Department, expected id=1 and name=IT, actual id=" + dept.getId()
									+ " and name=" + dept.getName());
				}

			} else {
				logger.log(Logger.Level.ERROR, "department returned was null");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass) {
			throw new Exception("queryAPITest22 failed");
		}
	}

	/*
	 * @testName: queryAPITest23
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:746.3; PERSISTENCE:SPEC:786;
	 * PERSISTENCE:SPEC:840; PERSISTENCE:SPEC:841; PERSISTENCE:SPEC:1596;
	 * 
	 * @test_Strategy: Delete Query
	 *
	 */
	@Test
	public void queryAPITest23() throws Exception {
		Query q;
		int result_size = 0;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Deleting Employee where id 1");
			q = getEntityManager().createQuery("delete from Employee e where e.id=1");

			result_size = q.executeUpdate();
			if (result_size == 1) {
				logger.log(Logger.Level.TRACE, "Updated 1 rows");
			}

			doFlush();
			clearCache();
			Employee emp = getEntityManager().find(Employee.class, 1);
			if (emp == null) {
				logger.log(Logger.Level.TRACE, "Employee returned expected null");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected null Employee, instead got:" + emp.toString());
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass) {
			throw new Exception("queryAPITest23 failed");
		}
	}

	/*
	 * @testName: queryAPITest24
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:759; PERSISTENCE:SPEC:787;
	 * PERSISTENCE:SPEC:837; PERSISTENCE:SPEC:838; PERSISTENCE:SPEC:839;
	 * 
	 * @test_Strategy: Bulk Update Query
	 *
	 */
	@Test
	public void queryAPITest24() throws Exception {
		Query q;
		int result_size = 0;
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Setting department name to IT for ids < 5");
			q = getEntityManager().createQuery("update Department d set d.name='IT' where d.id<5");

			result_size = q.executeUpdate();
			if (result_size == 4) {
				logger.log(Logger.Level.TRACE, "Updated 4 rows");
			}

			doFlush();
			clearCache();
			logger.log(Logger.Level.INFO, "Testing ids 1 to 4");

			for (int i = 1; i < 5; i++) {
				Department dept = getEntityManager().find(Department.class, i);
				if (dept != null) {
					if (dept.getId() == i) {
						if (dept.getName().equals("IT")) {
							logger.log(Logger.Level.TRACE, "Received expected name:" + dept.getName());
						} else {
							logger.log(Logger.Level.ERROR, "Received unexpected d.name =" + dept.getName());
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR,
								"Received incorrect Department, expected id=1 and name=IT, actual id=" + dept.getId()
										+ " and name=" + dept.getName());
					}

				} else {
					logger.log(Logger.Level.ERROR, "department returned was null");
				}
			}
			logger.log(Logger.Level.INFO, "Testing id 5");
			Department dept = getEntityManager().find(Department.class, 5);
			if (dept != null) {
				if (dept.getId() == 5) {
					if (dept.getName().equals(deptRef[4].getName())) {
						logger.log(Logger.Level.TRACE, "Received expected name:" + dept.getName());
					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected name =" + dept.getName());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received incorrect Department, expected id=1 and name=IT, actual id=" + dept.getId()
									+ " and name=" + dept.getName());
				}

			} else {
				logger.log(Logger.Level.ERROR, "department returned was null");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass) {
			throw new Exception("queryAPITest24 failed");
		}
	}

	/*
	 * @testName: queryAPITest25
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:759
	 * 
	 * @test_Strategy: Bulk Delete Query
	 *
	 */
	@Test
	public void queryAPITest25() throws Exception {
		Query q;
		int result_size = 0;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Deleting Employee where id < 21");
			q = getEntityManager().createQuery("delete from Employee e where e.id<21");

			result_size = q.executeUpdate();
			if (result_size == 20) {
				logger.log(Logger.Level.TRACE, "Updated 20 rows");
			}

			doFlush();
			clearCache();
			for (int i = 1; i < 21; i++) {
				Employee emp = getEntityManager().find(Employee.class, i);
				if (emp == null) {
					logger.log(Logger.Level.TRACE, "Employee " + i + " returned was null");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected null Employee, instead got:" + emp.toString());
				}
			}
			Employee emp = getEntityManager().find(Employee.class, 21);
			if (emp != null) {
				logger.log(Logger.Level.TRACE, "Employee 21 returned was expected non-null");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected non-null Employee for id 21");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);

		}

		if (!pass) {
			throw new Exception("queryAPITest25 failed");
		}
	}

	/*
	 * @testName: queryAPITest27
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:527;
	 * 
	 * @test_Strategy: Usage of Date literals in Query
	 */
	@Test
	public void queryAPITest27() throws Exception {
		Query q;
		Collection<Date> result;
		int result_size = 0;
		boolean pass1 = false;
		boolean pass2 = true;

		try {
			logger.log(Logger.Level.TRACE, "Invoking query");
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("select e.hireDate from Employee e where e.hireDate = {d '2000-02-14'}");

			result = q.getResultList();
			result_size = result.size();
			logger.log(Logger.Level.TRACE, "Result Size = " + result_size);

			// There are two employees hired on 2000-02-14
			if (result_size == 2) {
				Date expectedHireDate = Date.valueOf("2000-02-14");
				pass1 = true;
				logger.log(Logger.Level.TRACE, "Received expected count 2");
				for (Date d : result) {
					logger.log(Logger.Level.TRACE, "date=" + d);
					if (d.equals(expectedHireDate)) {
						logger.log(Logger.Level.TRACE, "Received expected HireDate ");
					} else {
						logger.log(Logger.Level.ERROR, "Received unexpected Employee HireDate = " + d.toString());
						pass2 = false;
					}
				}

			} else {
				logger.log(Logger.Level.TRACE, "Received unexpected count " + result);
			}

			getEntityTransaction().commit();

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
			throw new Exception("queryAPITest27 failed");
		}
	}

	/*
	 * @testName: getResultListIllegalStateException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:2699;PERSISTENCE:JAVADOC:666
	 * 
	 * @test_Strategy: Try to execute a delete query
	 *
	 */
	@Test
	public void getResultListIllegalStateException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Testing Delete");
		getEntityTransaction().begin();
		Query query = getEntityManager().createQuery("DELETE FROM Employee e where e.id in (1,2,3)");
		try {
			query.getResultList();
			logger.log(Logger.Level.TRACE, "Did not throw IllegalStateException");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass1 = true;
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		logger.log(Logger.Level.INFO, "Testing Update");

		Query q = getEntityManager().createQuery("Update Employee e SET e.salary=0 where e.id in (1,2,3)");
		try {
			q.getResultList();
			logger.log(Logger.Level.TRACE, "Did not throw IllegalStateException");
		} catch (IllegalStateException ise) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			pass2 = true;
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
		}

		try {
			if (getEntityTransaction().isActive()) {
				getEntityTransaction().rollback();
			}
		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception while rolling back TX:", re);
		}
		if (!pass1 || !pass2) {
			throw new Exception("getResultListIllegalStateException failed");
		}

	}

	/*
	 * @testName: noTransactionLockModeTypeNoneTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1518;PERSISTENCE:SPEC:1519;
	 * PERSISTENCE:SPEC:1520; PERSISTENCE:SPEC:1521;
	 * 
	 * @test_Strategy: execute query/TypedQuery with no transaction and lock mode
	 * type set to none and getSingleResult and getResultList should execute
	 * successfully.
	 */
	@Test
	public void noTransactionLockModeTypeNoneTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		logger.log(Logger.Level.INFO, "Query getSingleResult test");
		try {
			Query q = getEntityManager().createQuery("select e from Employee e where e.id = 1");
			q.setLockMode(LockModeType.NONE);
			Object o = q.getSingleResult();
			if (o instanceof Employee) {
				Employee e = (Employee) o;
				if (e.getId() == 1) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Received expected employee id:" + e.getId());
				} else {
					logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Received non Employee object:" + o);
			}

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
		logger.log(Logger.Level.INFO, "TypedQuery getSingleResult test");
		try {
			TypedQuery<Employee> q = getEntityManager().createQuery("select e from Employee e where e.id = 1",
					Employee.class);
			q.setLockMode(LockModeType.NONE);
			Employee e = q.getSingleResult();
			if (e.getId() == 1) {
				pass2 = true;
				logger.log(Logger.Level.TRACE, "Received expected employee id:" + e.getId());
			} else {
				logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
			}
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
		logger.log(Logger.Level.INFO, "Query getResultList test");
		try {
			Query q = getEntityManager().createQuery("select e from Employee e where e.id = 1");
			q.setLockMode(LockModeType.NONE);
			Collection c = q.getResultList();
			if (c.size() == 1) {
				Object o = c.iterator().next();
				if (o instanceof Employee) {
					Employee e = (Employee) o;
					if (e.getId() == 1) {
						pass3 = true;
						logger.log(Logger.Level.TRACE, "Received expected employee id:" + e.getId());
					} else {
						logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Received non Employee object:" + o);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Got more than one result");
			}
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
		logger.log(Logger.Level.INFO, "TypedQuery getResultList test");
		try {
			TypedQuery<Employee> q = getEntityManager().createQuery("select e from Employee e where e.id = 1",
					Employee.class);
			q.setLockMode(LockModeType.NONE);
			List<Employee> le = q.getResultList();
			if (le.size() == 1) {
				Employee e = le.get(0);
				if (e.getId() == 1) {
					pass4 = true;
					logger.log(Logger.Level.TRACE, "Received expected employee id:" + e.getId());
				} else {
					logger.log(Logger.Level.ERROR, "Expected employee with id:1, actual:" + e.getId());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Got more than one result");
			}

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

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("noTransactionLockModeTypeNoneTest failed");
		}
	}

	private void createTestData() throws Exception {
		logger.log(Logger.Level.TRACE, "createTestData");

		final Insurance insRef[] = new Insurance[5];
		final Date d2 = getSQLDate("2001-06-27");
		final Date d3 = getSQLDate("2002-07-07");
		final Date d4 = getSQLDate("2003-03-03");
		final Date d5 = getSQLDate("2004-04-10");
		final Date d6 = getSQLDate("2005-02-18");
		final Date d7 = getSQLDate("2000-09-17");
		final Date d8 = getSQLDate("2001-11-14");
		final Date d9 = getSQLDate("2002-10-04");
		final Date d10 = getSQLDate("2003-01-25");
		final Date d11 = getSQLDate();

		try {

			getEntityTransaction().begin();

			// logger.log(Logger.Level.TRACE,"Create 5 Departments");
			deptRef[0] = new Department(1, "Engineering");
			deptRef[1] = new Department(2, "Marketing");
			deptRef[2] = new Department(3, "Sales");
			deptRef[3] = new Department(4, "Accounting");
			deptRef[4] = new Department(5, "Training");

			logger.log(Logger.Level.TRACE, "Start to persist departments ");
			for (Department d : deptRef) {
				if (d != null) {
					getEntityManager().persist(d);
					logger.log(Logger.Level.TRACE, "persisted department " + d);
				}
			}

			// logger.log(Logger.Level.TRACE,"Create 3 Insurance Carriers");
			insRef[0] = new Insurance(1, "Prudential");
			insRef[1] = new Insurance(2, "Cigna");
			insRef[2] = new Insurance(3, "Sentry");

			logger.log(Logger.Level.TRACE, "Start to persist insurance ");
			for (Insurance i : insRef) {
				if (i != null) {
					getEntityManager().persist(i);
					logger.log(Logger.Level.TRACE, "persisted insurance " + i);
				}
			}

			// logger.log(Logger.Level.TRACE,"Create 20 employees");
			empRef[0] = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef[0].setDepartment(deptRef[0]);
			empRef[0].setInsurance(insRef[0]);

			empRef[1] = new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0);
			empRef[1].setDepartment(deptRef[1]);
			empRef[1].setInsurance(insRef[1]);

			empRef[2] = new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0);
			empRef[2].setDepartment(deptRef[2]);
			empRef[2].setInsurance(insRef[2]);

			empRef[3] = new Employee(4, "Robert", "Bissett", d4, (float) 55000.0);
			empRef[3].setDepartment(deptRef[3]);
			empRef[3].setInsurance(insRef[0]);

			empRef[4] = new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0);
			empRef[4].setDepartment(deptRef[4]);
			empRef[4].setInsurance(insRef[1]);

			empRef[5] = new Employee(6, "Karen", "Tegan", d6, (float) 80000.0);
			empRef[5].setDepartment(deptRef[0]);
			empRef[5].setInsurance(insRef[2]);

			empRef[6] = new Employee(7, "Stephen", "Cruise", d7, (float) 90000.0);
			empRef[6].setDepartment(deptRef[1]);
			empRef[6].setInsurance(insRef[0]);

			empRef[7] = new Employee(8, "Irene", "Caruso", d8, (float) 20000.0);
			empRef[7].setDepartment(deptRef[2]);
			empRef[7].setInsurance(insRef[1]);

			empRef[8] = new Employee(9, "William", "Keaton", d9, (float) 35000.0);
			empRef[8].setDepartment(deptRef[3]);
			empRef[8].setInsurance(insRef[2]);

			empRef[9] = new Employee(10, "Kate", "Hudson", d10, (float) 20000.0);
			empRef[9].setDepartment(deptRef[4]);
			empRef[9].setInsurance(insRef[0]);

			empRef[10] = new Employee(11, "Jonathan", "Smith", d10, 40000.0F);
			empRef[10].setDepartment(deptRef[0]);
			empRef[10].setInsurance(insRef[1]);

			empRef[11] = new Employee(12, "Mary", "Macy", d9, 40000.0F);
			empRef[11].setDepartment(deptRef[1]);
			empRef[11].setInsurance(insRef[2]);

			empRef[12] = new Employee(13, "Cheng", "Fang", d8, 40000.0F);
			empRef[12].setDepartment(deptRef[2]);
			empRef[12].setInsurance(insRef[0]);

			empRef[13] = new Employee(14, "Julie", "OClaire", d7, 60000.0F);
			empRef[13].setDepartment(deptRef[3]);
			empRef[13].setInsurance(insRef[1]);

			empRef[14] = new Employee(15, "Steven", "Rich", d6, 60000.0F);
			empRef[14].setDepartment(deptRef[4]);
			empRef[14].setInsurance(insRef[2]);

			empRef[15] = new Employee(16, "Kellie", "Lee", d5, 60000.0F);
			empRef[15].setDepartment(deptRef[0]);
			empRef[15].setInsurance(insRef[0]);

			empRef[16] = new Employee(17, "Nicole", "Martin", d4, 60000.0F);
			empRef[16].setDepartment(deptRef[1]);
			empRef[16].setInsurance(insRef[1]);

			empRef[17] = new Employee(18, "Mark", "Francis", d3, 60000.0F);
			empRef[17].setDepartment(deptRef[2]);
			empRef[17].setInsurance(insRef[2]);

			empRef[18] = new Employee(19, "Will", "Forrest", d2, 60000.0F);
			empRef[18].setDepartment(deptRef[3]);
			empRef[18].setInsurance(insRef[0]);

			empRef[19] = new Employee(20, "Katy", "Hughes", d1, 60000.0F);
			empRef[19].setDepartment(deptRef[4]);
			empRef[19].setInsurance(insRef[1]);

			empRef[20] = new Employee(21, "Jane", "Smmith", d11, 60000.0F);
			empRef[20].setDepartment(deptRef[0]);
			empRef[20].setInsurance(insRef[2]);

			// logger.log(Logger.Level.TRACE,"Start to persist employees ");
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logger.log(Logger.Level.TRACE, "persisted employee " + e);
				}
			}

			getEntityTransaction().commit();
			logger.log(Logger.Level.TRACE, "Created TestData");

		} catch (Exception re) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData while rolling back TX:", re);
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
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
