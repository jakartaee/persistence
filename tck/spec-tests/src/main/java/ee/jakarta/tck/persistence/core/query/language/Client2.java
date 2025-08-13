/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Country;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.UtilCustomerData;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class Client2 extends UtilCustomerData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_language2.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */

	/*
	 * @testName: queryTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:317.1; PERSISTENCE:SPEC:750;
	 * PERSISTENCE:SPEC:764; PERSISTENCE:SPEC:746.1
	 * 
	 * @test_Strategy: Find All Customers. Verify the results were accurately
	 * returned.
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest2() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute findAllCustomers");
			List result = getEntityManager().createQuery("Select Distinct Object(c) FROM Customer AS c")
					.getResultList();

			expectedPKs = new String[customerRef.length];
			for (int i = 0; i < customerRef.length; i++)
				expectedPKs[i] = Integer.toString(i + 1);

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + customerRef.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest2 failed");
	}

	/*
	 * @testName: queryTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:322; PERSISTENCE:SPEC:394;
	 * PERSISTENCE:SPEC:751; PERSISTENCE:SPEC:753; PERSISTENCE:SPEC:754;
	 * PERSISTENCE:SPEC:755
	 * 
	 * @test_Strategy: This query is defined on a one-one relationship and used
	 * conditional AND in query. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest4() throws Exception {
		boolean pass = false;
		Customer c;
		Query q;

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "3");
			logger.log(Logger.Level.TRACE, "find Customer with Home Address in Swansea");
			q = getEntityManager().createQuery(
					"SELECT c from Customer c WHERE c.home.street = :street AND c.home.city = :city AND c.home.state = :state and c.home.zip = :zip")
					.setParameter("street", "125 Moxy Lane").setParameter("city", "Swansea").setParameter("state", "MA")
					.setParameter("zip", "11345");

			c = (Customer) q.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest4 failed");
	}

	/*
	 * @testName: queryTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:338;
	 * 
	 * @test_Strategy: This query is defined on a one-one relationship using
	 * conditional OR in query. Verify the results were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest6() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with Home Address Information");

			c = getEntityManager().createQuery(
					"SELECT DISTINCT c from Customer c WHERE c.home.street = :street OR c.home.city = :city OR c.home.state = :state or c.home.zip = :zip")
					.setParameter("street", "47 Skyline Drive").setParameter("city", "Chelmsford")
					.setParameter("state", "VT").setParameter("zip", "02155").getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest6 failed");
	}

	/*
	 * @testName: queryTest15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:330;
	 * 
	 * @test_Strategy: Execute a query method with a string literal enclosed in
	 * single quotes (the string includes a single quote) in the conditional
	 * expression of the WHERE clause. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest15() throws Exception {
		boolean pass = false;
		Customer c;
		Query q;

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "5");
			logger.log(Logger.Level.TRACE, "find customer with name: Stephen S. D'Milla");
			q = getEntityManager().createQuery("sElEcT c FROM Customer c Where c.name = :cName").setParameter("cName",
					"Stephen S. D'Milla");

			c = (Customer) q.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest15 failed");
	}

	/*
	 * @testName: queryTest16
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query method using comparison operator IN in a
	 * comparison expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest16() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers IN home city: Lexington");
			c = getEntityManager().createQuery("select distinct c FROM Customer c WHERE c.home.city IN ('Lexington')")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest16 failed");
	}

	/*
	 * @testName: queryTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:353
	 * 
	 * @test_Strategy: Execute a query using comparison operator NOT IN in a
	 * comparison expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest17() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers NOT IN home city: Swansea or Brookline");
			c = getEntityManager()
					.createQuery("SELECT DISTINCT Object(c) FROM Customer c Left Outer Join c.home h WHERE "
							+ " h.city Not iN ('Swansea', 'Brookline')")
					.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";
			expectedPKs[4] = "7";
			expectedPKs[5] = "8";
			expectedPKs[6] = "10";
			expectedPKs[7] = "11";
			expectedPKs[8] = "12";
			expectedPKs[9] = "13";
			expectedPKs[10] = "14";
			expectedPKs[11] = "15";
			expectedPKs[12] = "16";
			expectedPKs[13] = "17";
			expectedPKs[14] = "18";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest17 failed");
	}

	/*
	 * @testName: queryTest18
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The pattern-value includes a
	 * percent character. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest18() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers with home ZIP CODE that ends in 77");
			c = getEntityManager().createQuery("select distinct Object(c) FROM Customer c WHERE c.home.zip LIKE '%77'")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest18 failed");
	}

	/*
	 * @testName: queryTest19
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query using the comparison operator NOT LIKE in a
	 * comparison expression within the WHERE clause. The pattern-value includes a
	 * percent character and an underscore. Verify the results were accurately
	 * returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest19() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with a home zip code that does not contain"
					+ " 44 in the third and fourth position");
			c = getEntityManager()
					.createQuery("Select Distinct Object(c) FROM Customer c WHERE c.home.zip not like '%44_'")
					.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "9";
			expectedPKs[6] = "10";
			expectedPKs[7] = "11";
			expectedPKs[8] = "12";
			expectedPKs[9] = "13";
			expectedPKs[10] = "14";
			expectedPKs[11] = "15";
			expectedPKs[12] = "16";
			expectedPKs[13] = "17";
			expectedPKs[14] = "18";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest19 failed");
	}

	/*
	 * @testName: queryTest22
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359; PERSISTENCE:SPEC:763
	 * 
	 * @test_Strategy: Execute a query using the IS NULL comparison operator in the
	 * WHERE clause. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest22() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who have a null work zip code");
			c = getEntityManager().createQuery("sELEct dIsTiNcT oBjEcT(c) FROM Customer c WHERE c.work.zip IS NULL")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest22 failed");
	}

	/*
	 * @testName: queryTest23
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359
	 * 
	 * @test_Strategy: Execute a query using the IS NOT NULL comparison operator
	 * within the WHERE clause. Verify the results were accurately returned. (This
	 * query is executed against non-NULL data. For NULL data, see test queryTest47)
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest23() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who do not have null work zip code entry");
			c = getEntityManager().createQuery("Select Distinct Object(c) FROM Customer c WHERE c.work.zip IS NOT NULL")
					.getResultList();

			expectedPKs = new String[17];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "12";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 17 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest23 failed");
	}

	/*
	 * @testName: queryTest36
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352
	 * 
	 * @test_Strategy: Execute a query using comparison operator IN in a conditional
	 * expression within the WHERE clause where the value for the IN expression is
	 * an input parameter. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest36() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who lives in city Attleboro");
			c = getEntityManager().createQuery("SELECT c From Customer c where c.home.city IN(:city)")
					.setParameter("city", "Attleboro").getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest36 failed");
	}

	/*
	 * @testName: queryTest37
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:354
	 * 
	 * @test_Strategy: Execute two methods using the comparison operator IN in a
	 * comparison expression within the WHERE clause and verify the results of the
	 * two queries are equivalent regardless of the way the expression is composed.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest37() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		String expectedPKs2[];
		List c1;
		List c2;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute two queries composed differently and verify results");
			c1 = getEntityManager()
					.createQuery("SELECT DISTINCT Object(c) from Customer c where c.home.state IN('NH', 'RI')")
					.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "5";
			expectedPKs[1] = "6";
			expectedPKs[2] = "12";
			expectedPKs[3] = "14";
			expectedPKs[4] = "16";

			c2 = getEntityManager().createQuery(
					"SELECT DISTINCT Object(c) from Customer c WHERE (c.home.state = 'NH') OR (c.home.state = 'RI')")
					.getResultList();

			expectedPKs2 = new String[5];
			expectedPKs2[0] = "5";
			expectedPKs2[1] = "6";
			expectedPKs2[2] = "12";
			expectedPKs2[3] = "14";
			expectedPKs2[4] = "16";

			if (!checkEntityPK(c1, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for first query.  Expected 5 reference, got: " + c1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query");
				pass1 = true;
			}

			if (!checkEntityPK(c2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for second query.  Expected 5 reference, got: " + c2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest37 failed");
	}

	/*
	 * @testName: queryTest45
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:361
	 * 
	 * @test_Strategy: Execute a query using IS NOT EMPTY in a
	 * collection_valued_association_field where the field is EMPTY.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest45() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"find customers whose id is greater than 1 " + "OR where the relationship is NOT EMPTY");
			c = getEntityManager()
					.createQuery("Select Object(c) from Customer c where c.aliasesNoop IS NOT EMPTY or c.id <> '1'")
					.getResultList();

			expectedPKs = new String[19];
			expectedPKs[0] = "2";
			expectedPKs[1] = "3";
			expectedPKs[2] = "4";
			expectedPKs[3] = "5";
			expectedPKs[4] = "6";
			expectedPKs[5] = "7";
			expectedPKs[6] = "8";
			expectedPKs[7] = "9";
			expectedPKs[8] = "10";
			expectedPKs[9] = "11";
			expectedPKs[10] = "12";
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";
			expectedPKs[17] = "19";
			expectedPKs[18] = "20";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest45 failed");
	}

	/*
	 * @testName: queryTest47
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:376; PERSISTENCE:SPEC:401;
	 * PERSISTENCE:SPEC:399.3; PERSISTENCE:SPEC:422; PERSISTENCE:SPEC:752;
	 * PERSISTENCE:SPEC:753
	 * 
	 * @test_Strategy: The IS NOT NULL construct can be used to eliminate the null
	 * values from the result set of the query. Verify the results are accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest47() throws Exception {
		boolean pass = false;
		List c;
		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			c = getEntityManager()
					.createQuery(
							"Select c.work.zip from Customer c where c.work.zip IS NOT NULL ORDER BY c.work.zip ASC")
					.getResultList();

			String[] result = (String[]) (c.toArray(new String[c.size()]));
			logger.log(Logger.Level.TRACE, "Compare results of work zip codes");
			pass = Arrays.equals(expectedZips, result);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest47 failed");
	}

	/*
	 * @testName: queryTest51
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359
	 * 
	 * @test_Strategy: Use the operator IS NOT NULL in a null comparision expression
	 * within the WHERE clause where the single_valued_path_expression is NULL.
	 * Verify the results were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest51() throws Exception {
		boolean pass = false;
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who do not have null relationship");
			c = getEntityManager().createQuery(
					"sElEcT Distinct oBJeCt(c) FROM Customer c, IN(c.aliases) a WHERE a.customerNoop IS NOT NULL")
					.getResultList();

			if (c.size() != 0) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest51 failed");
	}

	/*
	 * @testName: queryTest54
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:426
	 * 
	 * @test_Strategy: Define a query using Boolean operator NOT in a conditional
	 * test (NOT True = False) where the relationship is NULL. Verify the results
	 * were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest54() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine if customers have a NULL relationship");
			c = getEntityManager().createQuery(
					"SELECT DISTINCT Object(c) from Customer c, in(c.aliases) a where NOT a.customerNoop IS NULL")
					.getResultList();

			if (c.size() != 0) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest54 failed");
	}

	/*
	 * @testName: queryTest56
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:375; PERSISTENCE:SPEC:410;
	 * PERSISTENCE:SPEC:403; PERSISTENCE:SPEC:814; PERSISTENCE:SPEC:816
	 * 
	 * @test_Strategy: This query returns a null
	 * single_valued_association_path_expression. Verify the results were accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest56() throws Exception {

		boolean pass1 = false;
		boolean pass2 = true;
		List c;
		String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all work zip codes");
			c = getEntityManager().createQuery("Select c.work.zip from Customer c").getResultList();

			if (c.size() != 18) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + c.size());
			} else {
				Iterator i = c.iterator();
				int numOfNull = 0;
				int foundZip = 0;
				while (i.hasNext()) {
					pass1 = true;
					logger.log(Logger.Level.TRACE, "Check contents of List for null");
					Object o = i.next();
					if (o == null) {
						numOfNull++;
						continue;
					}

					logger.log(Logger.Level.TRACE, "Check List for expected zip codes");

					for (int l = 0; l < 17; l++) {
						if (expectedZips[l].equals(o)) {
							foundZip++;
							break;
						}
					}
				}
				if ((numOfNull != 1) || (foundZip != 17)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results");
					pass2 = false;
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest56 failed");
	}

	/*
	 * @testName: queryTest58
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:410;
	 * 
	 * @test_Strategy: This query returns a null
	 * single_valued_association_path_expression. Verify the results are accurately
	 * returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest58() throws Exception {
		boolean pass = false;
		Object s;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find home zip codes");
			s = getEntityManager()
					.createQuery("Select c.name from Customer c where c.home.street = '212 Edgewood Drive'")
					.getSingleResult();

			if (s != null) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected null.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass)
			throw new Exception("queryTest58 failed");
	}

	/*
	 * @testName: queryTest59
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:408
	 * 
	 * @test_Strategy: This tests a null single_valued_association_path_expression
	 * is returned using IS NULL. Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest59() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine which customers have an null name");
			c = getEntityManager().createQuery("Select Distinct Object(c) from Customer c where c.name is null")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest59 failed");
	}

	/*
	 * @testName: queryTest61
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:778;PERSISTENCE:SPEC:780;
	 * PERSISTENCE:SPEC:1714; PERSISTENCE:SPEC:1715;
	 * 
	 * @test_Strategy: Execute a query defining an identification variable for
	 * c.work in an OUTER JOIN clause. The JOIN operation will include customers
	 * without addresses. Verify the results are accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest61() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			c = getEntityManager().createQuery("select Distinct c FROM Customer c LEFT OUTER JOIN "
					+ "c.work workAddress where workAddress.zip IS NULL").getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "13";
			expectedPKs[1] = "19";
			expectedPKs[2] = "20";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest61 failed");
	}

	/*
	 * @testName: queryTest64
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:372.5;PERSISTENCE:SPEC:817;
	 * PERSISTENCE:SPEC:395
	 * 
	 * @test_Strategy: A constructor may be used in the SELECT list to return a
	 * collection of Java instances. The specified class is not required to be an
	 * entity or mapped to the database. The constructor name must be fully
	 * qualified.
	 *
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest64() throws Exception {
		boolean pass = false;
		List c;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			c = getEntityManager()
					.createQuery("SELECT NEW ee.jakarta.tck.persistence.common.schema30.Customer "
							+ "(c.id, c.name, c.country, c.work) FROM Customer c where " + " c.work.city = :workcity")
					.setParameter("workcity", "Burlington").getResultList();

			expectedPKs = new String[18];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "9";
			expectedPKs[9] = "10";
			expectedPKs[10] = "11";
			expectedPKs[11] = "12";
			expectedPKs[12] = "13";
			expectedPKs[13] = "14";
			expectedPKs[14] = "15";
			expectedPKs[15] = "16";
			expectedPKs[16] = "17";
			expectedPKs[17] = "18";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest64 failed");

	}

	/*
	 * @testName: queryTest69
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:384; PERSISTENCE:SPEC:389;
	 * PERSISTENCE:SPEC:406; PERSISTENCE:SPEC:824; PERSISTENCE:SPEC:392;
	 * PERSISTENCE:SPEC:393;
	 * 
	 * @test_Strategy: This test verifies the same results of two queries using the
	 * keyword DISTINCT or not using DISTINCT in the query with the aggregate
	 * keyword COUNT to verity the NULL values are eliminated before the aggregate
	 * is applied.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest69() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Query q1;
		Query q2;
		final Long expectedResult1 = 17L;
		final Long expectedResult2 = 16L;

		try {
			logger.log(Logger.Level.TRACE, "Execute two queries composed differently and verify results");

			q1 = getEntityManager().createQuery("Select Count(c.home.city) from Customer c");
			Long result1 = (Long) q1.getSingleResult();

			if (!(result1.equals(expectedResult1))) {
				logger.log(Logger.Level.ERROR,
						"Query1 in queryTest69 returned:" + result1 + " expected: " + expectedResult1);
			} else {
				logger.log(Logger.Level.TRACE, "pass:  Query1 in queryTest69 returned expected results");
				pass1 = true;
			}

			q2 = getEntityManager().createQuery("Select Count(Distinct c.home.city) from Customer c");

			Long result2 = (Long) q2.getSingleResult();

			if (!(result2.equals(expectedResult2))) {
				logger.log(Logger.Level.ERROR,
						"Query 2 in queryTest69 returned:" + result2 + " expected: " + expectedResult2);
			} else {
				logger.log(Logger.Level.TRACE, "pass:  Query 2 in queryTest69 returned expected results");
				pass2 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest69 failed");

	}

	/*
	 * @testName: queryTest71
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:744;PERSISTENCE:JAVADOC:128
	 * 
	 * @test_Strategy: The NoResultException is thrown by the persistence provider
	 * when Query.getSingleResult is invoked and there are not results to return.
	 * Verify the results are accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest71() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Check if a spouse is related to a customer");
			getEntityManager().createQuery("Select s.customer from Spouse s where s.id = '7'").getSingleResult();

			getEntityTransaction().commit();
		} catch (NoResultException e) {
			logger.log(Logger.Level.TRACE, "queryTest71: NoResultException caught as expected : " + e);
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught in queryTest71: " + e);
		}

		if (!pass)
			throw new Exception("queryTest71 failed");
	}

	/*
	 * @testName: test_leftouterjoin_1xM
	 ** 
	 * @assertion_ids: PERSISTENCE:SPEC:780
	 * 
	 * @test_Strategy: LEFT OUTER JOIN for 1-M relationship. Retrieve credit card
	 * information for a customer with name like Caruso.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_leftouterjoin_1xM() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT DISTINCT c from Customer c LEFT OUTER JOIN c.creditCards cc where c.name LIKE '%Caruso'")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_leftouterjoin_1x1 failed");
	}

	/*
	 * @testName: test_groupBy
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:810; PERSISTENCE:SPEC:756;
	 * PERSISTENCE:SPEC:1623;
	 * 
	 * @test_Strategy: Test for Only Group By in a simple select statement. Country
	 * is an Embeddable entity.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_groupBy() throws Exception {
		boolean pass = false;
		List result;
		String expected[] = new String[] { "CHA", "GBR", "IRE", "JPN", "USA" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("select c.country.code FROM Customer c GROUP BY c.country.code")
					.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expected, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "Expected:" + s);
				}
				for (String s : output) {
					logger.log(Logger.Level.ERROR, "Actual:" + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_groupBy failed");
	}

	/*
	 * @testName: test_innerjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:372;
	 * PERSISTENCE:SPEC:372.2
	 * 
	 * @test_Strategy: Inner Join for 1-1 relationship. Select all customers with
	 * spouses.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_innerjoin_1x1() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();

			result = getEntityManager().createQuery("SELECT c from Customer c INNER JOIN c.spouse s").getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_innerjoin_1x1 failed");
	}

	/*
	 * @testName: test_fetchjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
	 * PERSISTENCE:SPEC:776
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_fetchjoin_1x1() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("SELECT c from Customer c JOIN FETCH c.spouse ").getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results. Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_fetchjoin_1x1 failed");
	}

	/*
	 * @testName: test_groupByHaving
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:808; PERSISTENCE:SPEC:353;
	 * PERSISTENCE:SPEC:757; PERSISTENCE:SPEC:391; PERSISTENCE:SPEC:786;
	 * PERSISTENCE:SPEC:1595; PERSISTENCE:SPEC:1624;
	 * 
	 * @test_Strategy: Test for Group By and Having in a select statement Select the
	 * count of customers in each country where Country is China, England
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_groupByHaving() throws Exception {
		boolean pass = false;
		List result;
		final Long expectedGBR = 2L;
		final Long expectedCHA = 4L;

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("select Count(c) FROM Customer c GROUP BY c.country.code "
					+ "HAVING c.country.code IN ('GBR', 'CHA') ").getResultList();

			Iterator i = result.iterator();
			int numOfExpected = 0;
			while (i.hasNext()) {
				logger.log(Logger.Level.TRACE, "Check result received . . . ");
				Long l = (Long) i.next();
				if ((l.equals(expectedGBR)) || (l.equals(expectedCHA))) {
					numOfExpected++;
				}
			}

			if (numOfExpected != 2) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 2 Values returned : "
						+ "2 with Country Code GBR and 4 with Country Code CHA. " + "Received: " + result.size());
				Iterator it = result.iterator();
				while (it.hasNext()) {
					logger.log(Logger.Level.TRACE, "count of Codes Returned: " + it.next());
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);

		}

		if (!pass)
			throw new Exception("test_groupByHaving failed");
	}

	/*
	 * @testName: test_concatHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:803;
	 * PERSISTENCE:SPEC:804; PERSISTENCE:SPEC:805; PERSISTENCE:SPEC:806;
	 * PERSISTENCE:SPEC:734
	 * 
	 * @test_Strategy:Test for Functional Expression: concat in Having Clause Find
	 * customer Margaret Mills by firstname-lastname concatenation.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_concatHavingClause() throws Exception {
		boolean pass = false;
		Query q;
		String result;
		final String expectedCustomer = "Margaret Mills";

		try {
			getEntityTransaction().begin();
			q = getEntityManager()
					.createQuery(
							"select c.name FROM Customer c Group By c.name HAVING c.name = concat(:fmname, :lname) ")
					.setParameter("fmname", "Margaret ").setParameter("lname", "Mills");
			result = (String) q.getSingleResult();

			if (result.equals(expectedCustomer)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "test_concatHavingClause:  Did not get expected results. " + "Expected: "
						+ expectedCustomer + ", got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_concatHavingClause failed");
	}

	/*
	 * @testName: test_lowerHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.10
	 * 
	 * @test_Strategy:Test for Functional Expression: lower in Having Clause Select
	 * all customers in country with code GBR
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_lowerHavingClause() throws Exception {
		boolean pass = false;
		List result;
		final Long expectedCount = 2L;

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select count(c.country.code) FROM Customer c GROUP BY c.country.code "
							+ " HAVING LOWER(c.country.code) = 'gbr' ")
					.getResultList();

			Iterator it = result.iterator();
			while (it.hasNext()) {
				Long l = (Long) it.next();
				if (l.equals(expectedCount)) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. Expected 2 references, got: " + result.size());
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_lowerHavingClause failed");
	}

	/*
	 * @testName: test_upperHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.11
	 * 
	 * @test_Strategy:Test for Functional Expression: upper in Having Clause Select
	 * all customers in country ENGLAND
	 */

	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_upperHavingClause() throws Exception {
		boolean pass = false;
		List result;
		final Long expectedCount = 2L;

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select count(c.country.country) FROM Customer c GROUP BY c.country.country "
							+ "HAVING UPPER(c.country.country) = 'ENGLAND' ")
					.getResultList();

			Iterator it = result.iterator();
			while (it.hasNext()) {
				Long l = (Long) it.next();
				if (l.equals(expectedCount)) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Did not get expected results. Expected 2 references, got: " + result.size());
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_upperHavingClause failed");
	}

	/*
	 * @testName: test_lengthHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.4;
	 * PERSISTENCE:SPEC:1626;
	 * 
	 * @test_Strategy:Test for Functional Expression: length in Having Clause Select
	 * all customer names having the length of the city of the home address = 10
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_lengthHavingClause() throws Exception {
		boolean pass = false;
		List result;
		final String[] expected = new String[] { "Burlington", "Chelmsford", "Roslindale" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select a.city  FROM Customer c JOIN c.home a GROUP BY a.city HAVING LENGTH(a.city) = 10 ")
					.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expected, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : output) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}

			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_lengthHavingClause failed");
	}

	/*
	 * @testName: test_locateHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.3
	 * 
	 * @test_Strategy: Test for LOCATE expression in the Having Clause Select
	 * customer names if there the string "Frechette" is located in the customer
	 * name.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_locateHavingClause() throws Exception {
		boolean pass = false;
		List result;
		final String[] expected = new String[] { "Alan E. Frechette", "Arthur D. Frechette" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select c.name FROM Customer c GROUP BY c.name HAVING LOCATE('Frechette', c.name) > 0 ")
					.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expected, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : output) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_locateHavingClause failed");
	}

	/*
	 * @testName: test_subquery_in
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:352.2
	 * 
	 * @test_Strategy: Use IN expression in a sub query.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_subquery_in() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("Select DISTINCT c from Customer c WHERE c.home.state IN"
							+ "(Select distinct w.state from c.work w where w.state = :state ) ")
					.setParameter("state", "MA").getResultList();

			expectedPKs = new String[11];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "7";
			expectedPKs[5] = "8";
			expectedPKs[6] = "9";
			expectedPKs[7] = "11";
			expectedPKs[8] = "13";
			expectedPKs[9] = "15";
			expectedPKs[10] = "18";
			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_in:  Did not get expected results. "
						+ " Expected 11 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_in failed");
	}

	/*
	 * @testName: fetchStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:980
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchStringJoinTypeTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("SELECT c FROM Customer c INNER JOIN fetch c.spouse")
					.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "7";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("fetchStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: embeddableNotManagedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1671;
	 * 
	 * @test_Strategy: verify modified detached entity does not effect managed
	 * entity
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void embeddableNotManagedTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		List<Object[]> q;
		Customer cust;
		try {
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("SELECT c, c.country FROM Customer c where c.home.city = :homecity")
					.setParameter("homecity", "Bedford").getResultList();

			if (q.size() == 1) {
				for (Object[] o : q) {
					logger.log(Logger.Level.INFO, "Testing initial values");
					cust = (Customer) o[0];
					Country country = (Country) o[1];
					logger.log(Logger.Level.TRACE, "Customer:" + cust.toString());
					logger.log(Logger.Level.TRACE, "Country:" + country.toString());
					if (cust.getCountry() != country) {
						logger.log(Logger.Level.TRACE,
								"Customer country object does not equal Country from query as expected");
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR, "Customer country object equals Country from query");
					}
					logger.log(Logger.Level.INFO, "Change values of country");
					country.setCode("CHA");
					country.setCountry("China");
					logger.log(Logger.Level.TRACE, "Customer:" + cust.toString());
					logger.log(Logger.Level.TRACE, "Country:" + country.toString());
					logger.log(Logger.Level.TRACE, "Flush and refresh");
					getEntityManager().flush();
					getEntityManager().refresh(cust);
					logger.log(Logger.Level.INFO, "Test values again");
					logger.log(Logger.Level.TRACE, "Customer:" + cust.toString());
					logger.log(Logger.Level.TRACE, "Country:" + country.toString());
					if (cust.getCountry() != country) {
						if (!cust.getCountry().getCountry().equals("China")
								&& (!cust.getCountry().getCode().equals("CHA"))) {
							logger.log(Logger.Level.TRACE,
									"Customer.country does not contain the modifications made to the Country object");
							pass2 = true;
						} else {
							logger.log(Logger.Level.ERROR,
									"Customer.country contains the modifications made to the Country object");
						}
					} else {
						logger.log(Logger.Level.ERROR, "Customer country object equals Country from query");
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get 1 result back:" + q.size());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("embeddableNotManagedTest failed");

	}
}
