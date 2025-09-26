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

package ee.jakarta.tck.persistence.core.query.language;

import java.lang.System.Logger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.SoftwareProduct;
import ee.jakarta.tck.persistence.common.schema30.UtilProductData;
import jakarta.persistence.Query;

public class Client4 extends UtilProductData {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_language4.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */

	/*
	 * @testName: queryTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:319; PERSISTENCE:SPEC:735;
	 * PERSISTENCE:SPEC:784
	 * 
	 * @test_Strategy: Ensure identification variables can be interpreted correctly
	 * regardless of case. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest7() throws Exception {
		boolean pass = false;
		List p;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Products");
			p = getEntityManager().createQuery("Select DISTINCT Object(P) From Product p where P.quantity < 10")
					.getResultList();

			String[] expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "21";

			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest7 failed");
	}

	/*
	 * @testName: queryTest38
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.7; PERSISTENCE:SPEC:368;
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function MOD in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest38() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		List p;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing MOD with numeric Java object types");
			logger.log(Logger.Level.TRACE, "find orders that have the quantity of 50 available");
			Integer value1 = 550;
			Integer value2 = 100;
			p = getEntityManager().createQuery(
					"Select DISTINCT Object(p) From Product p where MOD(" + value1 + "," + value2 + ") = p.quantity")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "5";
			expectedPKs[1] = "20";
			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			logger.log(Logger.Level.INFO, "Testing MOD with primitive numeric type");
			logger.log(Logger.Level.TRACE, "find orders that have the quantity of 50 available");
			p = getEntityManager()
					.createQuery("Select DISTINCT Object(p) From Product p where MOD(550, 100) = p.quantity")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "5";
			expectedPKs[1] = "20";
			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest38 failed");
	}

	/*
	 * @testName: queryTest40
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:350
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator BETWEEN in
	 * a comparison expression within the WHERE clause and verify the results of the
	 * two queries are equivalent regardless of the way the expression is composed.
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest40() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];
		List p1;
		List p2;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"Execute two queries composed differently and verify results" + " Execute Query 1");
			p1 = getEntityManager()
					.createQuery("Select DISTINCT OBJECT(p) From Product p where p.quantity BETWEEN 10 AND 20")
					.getResultList();
			expectedPKs = new String[7];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "17";
			expectedPKs[3] = "27";
			expectedPKs[4] = "28";
			expectedPKs[5] = "31";
			expectedPKs[6] = "36";

			logger.log(Logger.Level.TRACE, "Execute Query 2");
			p2 = getEntityManager()
					.createQuery(
							"Select DISTINCT OBJECT(p) From Product p where (p.quantity >= 10) AND (p.quantity <= 20)")
					.getResultList();

			expectedPKs2 = new String[7];
			expectedPKs2[0] = "8";
			expectedPKs2[1] = "9";
			expectedPKs2[2] = "17";
			expectedPKs2[3] = "27";
			expectedPKs2[4] = "28";
			expectedPKs2[5] = "31";
			expectedPKs2[6] = "36";

			if (!checkEntityPK(p1, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for first query in queryTest40. "
						+ "  Expected 7 references, got: " + p1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query in queryTest40.");
				pass1 = true;
			}

			if (!checkEntityPK(p2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for second query in queryTest40. "
						+ "  Expected 7 references, got: " + p2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query in queryTest40.");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest40 failed");
	}

	/*
	 * @testName: queryTest41
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:350
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator NOT BETWEEN
	 * in a comparison expression within the WHERE clause and verify the results of
	 * the two queries are equivalent regardless of the way the expression is
	 * composed.
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest41() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];
		List p1;
		List p2;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"Execute two queries composed differently and verify results" + " Execute first query");
			p1 = getEntityManager()
					.createQuery("Select DISTINCT Object(p) From Product p where p.quantity NOT BETWEEN 20 AND 200")
					.getResultList();

			expectedPKs = new String[10];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "10";
			expectedPKs[3] = "11";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "17";
			expectedPKs[7] = "21";
			expectedPKs[8] = "29";
			expectedPKs[9] = "31";

			if (!checkEntityPK(p1, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for first query.  Expected 31 references, got: " + p1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query");
				pass1 = true;
			}

			logger.log(Logger.Level.TRACE, "Execute second query");
			p2 = getEntityManager()
					.createQuery(
							"Select DISTINCT Object(p) From Product p where (p.quantity < 20) OR (p.quantity > 200)")
					.getResultList();

			expectedPKs2 = new String[10];
			expectedPKs2[0] = "8";
			expectedPKs2[1] = "9";
			expectedPKs2[2] = "10";
			expectedPKs2[3] = "11";
			expectedPKs2[4] = "14";
			expectedPKs2[5] = "15";
			expectedPKs2[6] = "17";
			expectedPKs2[7] = "21";
			expectedPKs2[8] = "29";
			expectedPKs2[9] = "31";

			if (!checkEntityPK(p2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for second query.  Expected 31 references, got: " + p2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest41 failed");
	}

	/*
	 * @testName: queryTest43
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: Execute a query using Boolean operator AND in a conditional
	 * test ( False AND False = False) where the second condition is not NULL.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest43() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List p;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Check results of AND operator: False AND False = False");
			p = getEntityManager().createQuery(
					"Select Distinct Object(p) from Product p where (p.quantity > (500 + :int1)) AND (p.partNumber IS NULL)")
					.setParameter("int1", 100).getResultList();

			expectedPKs = new String[0];
			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest43 failed");
	}

	/*
	 * @testName: queryTest44
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:416
	 * 
	 * @test_Strategy: If an input parameter is NULL, comparison operations
	 * involving the input parameter will return an unknown value.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest44() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List p;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "provide a null value for a comparison operation and verify the results");
			p = getEntityManager().createQuery("Select Distinct Object(p) from Product p where p.name = ?1")
					.setParameter(1, null).getResultList();

			expectedPKs = new String[0];

			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest44 failed");
	}

	/*
	 * @testName: queryTest68
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function SUM.
	 * SUM returns Double when applied to state-fields of floating types. Verify the
	 * results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest68() throws Exception {
		boolean pass = false;
		final Double d1 = 33387.14D;
		final Double d2 = 33387.15D;
		Double d3;
		Query q;

		try {
			logger.log(Logger.Level.TRACE, "find SUM of all product prices");
			q = getEntityManager().createQuery("SELECT Sum(p.price) FROM Product p");

			d3 = (Double) q.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "Returned expected results: " + d1);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "Returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass)
			throw new Exception("queryTest68 failed");
	}

	/*
	 * @testName: queryTest70
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:383; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:827; PERSISTENCE:SPEC:821
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function SUM.
	 * SUM returns Long when applied to state-fields of integral types. Verify the
	 * results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest70() throws Exception {
		boolean pass = false;
		final Long expectedValue = 3277L;
		Long result;
		Query q;

		try {
			logger.log(Logger.Level.TRACE, "find SUM of all product prices");
			q = getEntityManager().createQuery("SELECT Sum(p.quantity) FROM Product p");

			result = (Long) q.getSingleResult();

			if (expectedValue.equals(result)) {
				logger.log(Logger.Level.TRACE, "Returned expected results: " + result);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "Returned " + result + "expected: " + expectedValue);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception queryTest70: ", e);
		}
		if (!pass)
			throw new Exception("queryTest70 failed");
	}

	/*
	 * @testName: test_betweenDates
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:JAVADOC:15; PERSISTENCE:JAVADOC:166; PERSISTENCE:JAVADOC:189;
	 * PERSISTENCE:SPEC:1049; PERSISTENCE:SPEC:1059; PERSISTENCE:SPEC:1060
	 * 
	 * @test_Strategy: Execute a query containing using the operator BETWEEN with
	 * datetime_expression. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void test_betweenDates() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			Date date1 = getSQLDate(2000, 2, 14);
			Date date6 = getSQLDate(2005, 2, 18);
			logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + date6);
			result = getEntityManager()
					.createQuery(
							"SELECT DISTINCT p From Product p where p.shelfLife.soldDate BETWEEN :date1 AND :date6")
					.setParameter("date1", date1).setParameter("date6", date6).getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "31";
			expectedPKs[1] = "32";
			expectedPKs[2] = "33";
			expectedPKs[3] = "37";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + result.size());
			} else {

				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_betweenDates failed");
	}

	/*
	 * @testName: test_notBetweenDates
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:600
	 * 
	 * @test_Strategy: Execute a query containing using the operator NOT BETWEEN.
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void test_notBetweenDates() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];
		final Date date1 = getSQLDate("2000-02-14");
		final Date newdate = getSQLDate("2005-02-17");
		logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + newdate);

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT DISTINCT p From Product p where p.shelfLife.soldDate NOT BETWEEN :date1 AND :newdate")
					.setParameter("date1", date1).setParameter("newdate", newdate).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "31";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_notBetweenDates failed");
	}

	/*
	 * @testName: aggregateFunctionsWithNoValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:828; PERSISTENCE:SPEC:829;
	 * 
	 * @test_Strategy: Execute a query which contains aggregate functions when there
	 * are no values and see that null or zero is returned.
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void aggregateFunctionsWithNoValuesTest() throws Exception {
		boolean pass = false;
		Query q;

		try {
			logger.log(Logger.Level.INFO, "Testing SUM");
			logger.log(Logger.Level.TRACE, "find SUM of all product prices");
			q = getEntityManager().createQuery("SELECT Sum(p.price) FROM Product p where p.id='9999' ");
			Object o = q.getSingleResult();
			if (o == null) {
				logger.log(Logger.Level.TRACE, "Returned expected null results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Returned " + o.toString() + ", instead of null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Testing AVG");
			logger.log(Logger.Level.TRACE, "find AVG of all product prices");
			q = getEntityManager().createQuery("SELECT AVG(p.price) FROM Product p where p.id='9999' ");
			Object o = q.getSingleResult();
			if (o == null) {
				logger.log(Logger.Level.TRACE, "Returned expected null results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Returned " + o.toString() + ", instead of null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Testing MAX");
			logger.log(Logger.Level.TRACE, "find MAX of all product prices");
			q = getEntityManager().createQuery("SELECT MAX(p.price) FROM Product p where p.id='9999' ");
			Object o = q.getSingleResult();
			if (o == null) {
				logger.log(Logger.Level.TRACE, "Returned expected null results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Returned " + o.toString() + ", instead of null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Testing MIN");
			logger.log(Logger.Level.TRACE, "find MIN of all product prices");
			q = getEntityManager().createQuery("SELECT MIN(p.price) FROM Product p where p.id='9999' ");
			Object o = q.getSingleResult();
			if (o == null) {
				logger.log(Logger.Level.TRACE, "Returned expected null results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Returned " + o.toString() + ", instead of null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			logger.log(Logger.Level.INFO, "Testing COUNT");
			logger.log(Logger.Level.TRACE, "find COUNT of all product prices");
			q = getEntityManager().createQuery("SELECT COUNT(p.price) FROM Product p where p.id='9999' ");
			Object o = q.getSingleResult();
			if (o != null) {
				if (o instanceof Long) {
					Long i = (Long) o;
					if (i == 0L) {
						logger.log(Logger.Level.TRACE, "Returned expected 0 result");
						pass = true;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Did not get instance of Long");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Received null instead of 0");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass)
			throw new Exception("aggregateFunctionsWithNoValuesTest failed");
	}

	/*
	 * @testName: primaryKeyJoinColumnTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1120; PERSISTENCE:SPEC:1121;
	 * PERSISTENCE:SPEC:1121.1;
	 *
	 * @test_Strategy: Select p from Product p where p.whouse = "WH5"
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void primaryKeyJoinColumnTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List<Product> actual;
		try {
			getEntityTransaction().begin();
			actual = getEntityManager().createQuery("Select p from Product p where p.wareHouse = 'WH5'")
					.getResultList();

			if (actual.size() == 1 && actual.get(0).getWareHouse().equals("WH5")) {
				logger.log(Logger.Level.TRACE, "Expected results received:" + actual.get(0).getWareHouse());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test returned: " + actual.get(0).getWareHouse() + ", expected: WH5");
				for (Product p : actual) {
					logger.log(Logger.Level.ERROR, "**id=" + p.getId() + ", model=" + p.getWareHouse());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass) {
			throw new Exception("primaryKeyJoinColumnTest  failed");
		}
	}

	/*
	 * @testName: typeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1738; PERSISTENCE:SPEC:1658;
	 * 
	 * @test_Strategy: test path.type()
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void typeTest() throws Exception {
		boolean pass = false;
		List<Integer> expected = new ArrayList<Integer>();
		for (Product p : hardwareRef) {
			expected.add(Integer.valueOf(p.getId()));
		}
		Collections.sort(expected);
		List<Integer> actual = new ArrayList<Integer>();

		getEntityTransaction().begin();
		List<Product> result = getEntityManager().createQuery("Select p from Product p where TYPE(p) = HardwareProduct")
				.getResultList();

		for (Product p : result) {
			actual.add(Integer.parseInt(p.getId()));
		}

		Collections.sort(actual);

		if (!checkEntityPK(actual, expected)) {
			logger.log(Logger.Level.ERROR,
					"Did not get expected results. Expected " + expected.size() + " references, got: " + actual.size());
		} else {
			logger.log(Logger.Level.TRACE, "Expected results received");
			pass = true;
		}
		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("primaryKeyJoinColumnTest  failed");
		}
	}

	/*
	 * @testName: entityTypeLiteralTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1632; PERSISTENCE:SPEC:1658;
	 * 
	 * @test_Strategy: Test an entity type literal can be specified in a query
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void entityTypeLiteralTest() throws Exception {
		boolean pass = false;
		List p;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Products");
			p = getEntityManager().createQuery("Select p From Product p where TYPE(p) in (SoftwareProduct)")
					.getResultList();

			String[] expectedPKs = new String[softwareRef.length];
			int i = 0;
			for (SoftwareProduct sf : softwareRef) {
				expectedPKs[i++] = sf.getId();
			}

			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("entityTypeLiteralTest failed");
	}

	/*
	 * @testName: scalarExpressionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2512;
	 * 
	 * @test_Strategy: Test various scalar expressions test
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void scalarExpressionsTest() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		List p;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Testing arithmetic expression:");
			p = getEntityManager().createQuery("Select p From Product p where ((p.quantity) + 10 < 25)")
					.getResultList();

			String[] expectedPKs = new String[5];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "15";
			expectedPKs[3] = "17";
			expectedPKs[4] = "21";

			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Testing string expression:");
			p = getEntityManager().createQuery("Select p From Product p where (p.name like 'Java%')").getResultList();

			String[] expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "20";
			expectedPKs[3] = "34";

			if (!checkEntityPK(p, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + p.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("scalarExpressionsTest failed");
	}

}
