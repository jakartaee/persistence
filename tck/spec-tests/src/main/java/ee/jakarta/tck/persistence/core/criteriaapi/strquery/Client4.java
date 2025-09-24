/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.strquery;

import java.lang.System.Logger;
import java.sql.Date;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Product;
import ee.jakarta.tck.persistence.common.schema30.UtilProductData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client4 extends UtilProductData {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client4.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_strquery4.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: queryTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:735; PERSISTENCE:SPEC:784;
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest7() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Products");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.lt(product.<Integer>get("quantity"), 10));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			List<Product> plist = tquery.getResultList();

			String[] expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "21";

			if (!checkEntityPK(plist, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ "references, got: " + plist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest7 failed");
		}
	}

	/*
	 * @testName: queryTest38
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.7
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function MOD in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupProductData")
	@Test
	public void queryTest38() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find orders that have the quantity of 50 available");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.equal(cbuilder.mod(cbuilder.literal(550), 100), product.get("quantity")));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "5";
			expectedPKs[1] = "20";
			if (!checkEntityPK(plist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + plist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest38 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"Execute two queries composed differently and verify results" + " Execute Query 1");
			CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
			Root<Product> product1 = cquery1.from(Product.class);
			cquery1.where(cbuilder.between(product1.<Integer>get("quantity"), 10, 20));
			cquery1.select(product1);
			TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
			List<Product> plist1 = tquery1.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "8";
			expectedPKs[1] = "9";
			expectedPKs[2] = "17";
			expectedPKs[3] = "27";
			expectedPKs[4] = "28";
			expectedPKs[5] = "31";
			expectedPKs[6] = "36";

			logger.log(Logger.Level.TRACE, "Execute Query 2");
			CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
			Root<Product> product2 = cquery2.from(Product.class);
			cquery2.where(cbuilder.and(cbuilder.ge(product2.<Integer>get("quantity"), 10),
					cbuilder.le(product2.<Integer>get("quantity"), 20))).select(product2);
			TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
			List<Product> plist2 = tquery2.getResultList();

			expectedPKs2 = new String[7];
			expectedPKs2[0] = "8";
			expectedPKs2[1] = "9";
			expectedPKs2[2] = "17";
			expectedPKs2[3] = "27";
			expectedPKs2[4] = "28";
			expectedPKs2[5] = "31";
			expectedPKs2[6] = "36";

			if (!checkEntityPK(plist1, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for first query in queryTest40. "
						+ "  Expected 7 references, got: " + plist1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query in queryTest40.");
				pass1 = true;
			}

			if (!checkEntityPK(plist2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for second query in queryTest40. "
						+ "  Expected 7 references, got: " + plist2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query in queryTest40.");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest40 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"Execute two queries composed differently and verify results" + " Execute first query");
			CriteriaQuery<Product> cquery1 = cbuilder.createQuery(Product.class);
			Root<Product> product1 = cquery1.from(Product.class);
			cquery1.where(cbuilder.between(product1.<Integer>get("quantity"), 10, 20).not());
			cquery1.select(product1);
			TypedQuery<Product> tquery1 = getEntityManager().createQuery(cquery1);
			List<Product> plist1 = tquery1.getResultList();

			expectedPKs = new String[31];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "10";
			expectedPKs[8] = "11";
			expectedPKs[9] = "12";
			expectedPKs[10] = "13";
			expectedPKs[11] = "14";
			expectedPKs[12] = "15";
			expectedPKs[13] = "16";
			expectedPKs[14] = "18";
			expectedPKs[15] = "19";
			expectedPKs[16] = "20";
			expectedPKs[17] = "21";
			expectedPKs[18] = "22";
			expectedPKs[19] = "23";
			expectedPKs[20] = "24";
			expectedPKs[21] = "25";
			expectedPKs[22] = "26";
			expectedPKs[23] = "29";
			expectedPKs[24] = "30";
			expectedPKs[25] = "32";
			expectedPKs[26] = "33";
			expectedPKs[27] = "34";
			expectedPKs[28] = "35";
			expectedPKs[29] = "37";
			expectedPKs[30] = "38";

			logger.log(Logger.Level.TRACE, "Execute second query");
			CriteriaQuery<Product> cquery2 = cbuilder.createQuery(Product.class);
			Root<Product> product2 = cquery2.from(Product.class);
			cquery2.where(cbuilder.or(cbuilder.lt(product2.<Integer>get("quantity"), 10),
					cbuilder.gt(product2.<Integer>get("quantity"), 20))).select(product2);
			TypedQuery<Product> tquery2 = getEntityManager().createQuery(cquery2);
			List<Product> plist2 = tquery2.getResultList();

			expectedPKs2 = new String[31];
			expectedPKs2[0] = "1";
			expectedPKs2[1] = "2";
			expectedPKs2[2] = "3";
			expectedPKs2[3] = "4";
			expectedPKs2[4] = "5";
			expectedPKs2[5] = "6";
			expectedPKs2[6] = "7";
			expectedPKs2[7] = "10";
			expectedPKs2[8] = "11";
			expectedPKs2[9] = "12";
			expectedPKs2[10] = "13";
			expectedPKs2[11] = "14";
			expectedPKs2[12] = "15";
			expectedPKs2[13] = "16";
			expectedPKs2[14] = "18";
			expectedPKs2[15] = "19";
			expectedPKs2[16] = "20";
			expectedPKs2[17] = "21";
			expectedPKs2[18] = "22";
			expectedPKs2[19] = "23";
			expectedPKs2[20] = "24";
			expectedPKs2[21] = "25";
			expectedPKs2[22] = "26";
			expectedPKs2[23] = "29";
			expectedPKs2[24] = "30";
			expectedPKs2[25] = "32";
			expectedPKs2[26] = "33";
			expectedPKs2[27] = "34";
			expectedPKs2[28] = "35";
			expectedPKs2[29] = "37";
			expectedPKs2[30] = "38";

			if (!checkEntityPK(plist1, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for first query.  Expected 31 references, got: " + plist1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query");
				pass1 = true;
			}

			if (!checkEntityPK(plist2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for second query.  Expected 31 references, got: "
								+ plist2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest41 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Check results of AND operator: False AND False = False");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.and(
					cbuilder.gt(product.<Integer>get("quantity"),
							cbuilder.sum(cbuilder.literal(500), cbuilder.parameter(Integer.class, "int1"))),
					cbuilder.isNull(product.get("partNumber"))));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("int1", Integer.valueOf(100));
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[0];
			if (!checkEntityPK(plist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + plist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest43 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "provide a null value for a comparison operation and verify the results");
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.equal(product.get("name"), cbuilder.parameter(String.class, "num1")));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("num1", null);
			List<Product> plist = tquery.getResultList();

			expectedPKs = new String[0];

			if (!checkEntityPK(plist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + plist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest44 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find SUM of all product prices");
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.select(cbuilder.sum(product.<Double>get("price")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			d3 = (Double) tquery.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "queryTest68 returned expected results: " + d1);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest68 returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("queryTest68 failed");
		}
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
		final Integer expectedValue = Integer.valueOf(3277);
		Integer result;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find SUM of all product prices");
			CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.select(cbuilder.sum(product.<Integer>get("quantity")));
			TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
			result = (Integer) tquery.getSingleResult();

			if (expectedValue.equals(result)) {
				logger.log(Logger.Level.TRACE, "queryTest70 returned expected results: " + result);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest70 returned " + result + "expected: " + expectedValue);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("queryTest70 failed");
		}
	}

	/*
	 * @testName: test_betweenDates
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349.2; PERSISTENCE:SPEC:553;
	 * PERSISTENCE:JAVADOC:15; PERSISTENCE:JAVADOC:166; PERSISTENCE:JAVADOC:189;
	 * PERSISTENCE:SPEC:1049; PERSISTENCE:SPEC:1059; PERSISTENCE:SPEC:1060;
	 * PERSISTENCE:JAVADOC:743
	 * 
	 * @test_Strategy: Execute a query containing using the operator BETWEEN with
	 * datetime_expression. Verify the results were accurately returned.
	 *
	 */

	@SetupMethod(name = "setupProductData")
	@Test
	public void test_betweenDates() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			final Date date1 = getSQLDate(2000, 2, 14);
			final Date date6 = getSQLDate(2005, 2, 18);
			logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + date6);
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.between(product.get("shelfLife").<java.sql.Date>get("soldDate"),
					cbuilder.parameter(java.sql.Date.class, "date1"),
					cbuilder.parameter(java.sql.Date.class, "date6")));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("date1", date1);
			tquery.setParameter("date6", date6);
			List<Product> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
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
		String expectedPKs[];
		final Date date1 = getSQLDate("2000-02-14");
		final Date newdate = getSQLDate("2005-02-17");
		logger.log(Logger.Level.TRACE, "The dates used in test_betweenDates is : " + date1 + " and " + newdate);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Product> cquery = cbuilder.createQuery(Product.class);
			Root<Product> product = cquery.from(Product.class);
			cquery.where(cbuilder.not(cbuilder.between(product.get("shelfLife").<java.sql.Date>get("soldDate"),
					cbuilder.parameter(java.sql.Date.class, "date1"),
					cbuilder.parameter(java.sql.Date.class, "newdate"))));
			cquery.select(product);
			TypedQuery<Product> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("date1", date1);
			tquery.setParameter("newdate", newdate);
			List<Product> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass)
			throw new Exception("test_notBetweenDates failed");
	}

}
