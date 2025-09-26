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

package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Address;
import ee.jakarta.tck.persistence.common.schema30.Address_;
import ee.jakarta.tck.persistence.common.schema30.Country_;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Spouse;
import ee.jakarta.tck.persistence.common.schema30.Spouse_;
import ee.jakarta.tck.persistence.common.schema30.UtilCustomerData;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;

public class Client1 extends UtilCustomerData {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery1.jar", pkgNameWithoutSuffix, classes);
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
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute findAllCustomers");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> c = cquery.from(Customer.class);
			cquery.select(c);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[customerRef.length];
			for (int i = 0; i < customerRef.length; i++) {
				expectedPKs[i] = Integer.toString(i + 1);
			}

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + customerRef.length
						+ " references, got: " + clist.size());

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest2 failed");
		}
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
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "3");
			logger.log(Logger.Level.TRACE, "find Customer with Home Address in Swansea");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(
					cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
							cbuilder.parameter(String.class, "street")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
							cbuilder.parameter(String.class, "city")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
							cbuilder.parameter(String.class, "state")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
							cbuilder.parameter(String.class, "zip")))
					.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("street", "125 Moxy Lane").setParameter("city", "Swansea").setParameter("state", "MA")
					.setParameter("zip", "11345");
			c = tquery.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");

			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest4 failed");
		}
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
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with Home Address Information");

			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			cquery.where(cbuilder.or(
					cbuilder.equal(customer.get(Customer_.home).get(Address_.street),
							cbuilder.parameter(String.class, "street")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.city),
							cbuilder.parameter(String.class, "city")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.state),
							cbuilder.parameter(String.class, "state")),
					cbuilder.equal(customer.get(Customer_.home).get(Address_.zip),
							cbuilder.parameter(String.class, "zip"))));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("street", "47 Skyline Drive");
			tquery.setParameter("city", "Chelmsford");
			tquery.setParameter("state", "VT");
			tquery.setParameter("zip", "02155");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "10";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest6 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			Customer expected = getEntityManager().find(Customer.class, "5");
			logger.log(Logger.Level.TRACE, "find customer with name: Stephen S. D'Milla");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.name), cbuilder.parameter(String.class, "cName")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("cName", "Stephen S. D'Milla");
			c = tquery.getSingleResult();

			if (expected == c) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest15 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers IN home city: Lexington");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.home).get(Address_.city).in("Lexington"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest16 failed");
		}
	}

	/*
	 * @testName: queryTest17
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352; PERSISTENCE:SPEC:353;
	 * PERSISTENCE:JAVADOC:988
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers NOT IN home city: Swansea or Brookline");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> h = customer.join(Customer_.home, JoinType.LEFT);
			cquery.where(h.get(Address_.city).in("Swansea", "Brookline").not());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

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

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest17 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers with home ZIP CODE that ends in 77");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.like(customer.get(Customer_.home).get(Address_.zip), "%77"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest18 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with a home zip code that does not contain"
					+ " 44 in the third and fourth position");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.notLike(customer.get(Customer_.home).get(Address_.zip), "%44_"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

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
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest19 failed");
		}
	}

	/*
	 * @testName: queryTest22
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359;
	 * 
	 * @test_Strategy: Execute a query using the IS NULL comparison operator in the
	 * WHERE clause. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest22() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who have a null work zip code");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.work).get(Address_.zip).isNull());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest22 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who do not have null work zip code entry");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.work).get(Address_.zip).isNotNull());
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

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

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 17 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest23 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who lives in city Attleboro");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(customer.get(Customer_.home).get(Address_.city).in(cbuilder.parameter(String.class, "city")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("city", "Attleboro");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "13";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest36 failed");
		}
	}

	/*
	 * @testName: queryTest37
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:354; PERSISTENCE:JAVADOC:833
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute two queries composed differently and verify results");
			CriteriaQuery<Customer> cquery1 = cbuilder.createQuery(Customer.class);
			Root<Customer> customer1 = cquery1.from(Customer.class);
			cquery1.where(customer1.get(Customer_.home).get(Address_.state).in("NH", "RI")).select(customer1);
			TypedQuery<Customer> tquery1 = getEntityManager().createQuery(cquery1);
			tquery1.setMaxResults(customerRef.length);
			List<Customer> clist1 = tquery1.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "5";
			expectedPKs[1] = "6";
			expectedPKs[2] = "12";
			expectedPKs[3] = "14";
			expectedPKs[4] = "16";

			CriteriaQuery<Customer> cquery2 = cbuilder.createQuery(Customer.class);
			Root<Customer> customer2 = cquery2.from(Customer.class);
			cquery2.where(cbuilder.or(cbuilder.equal(customer2.get(Customer_.home).get(Address_.state), "NH"),
					cbuilder.equal(customer2.get(Customer_.home).get(Address_.state), "RI"))).select(customer2);
			TypedQuery<Customer> tquery2 = getEntityManager().createQuery(cquery2);
			tquery2.setMaxResults(customerRef.length);
			List<Customer> clist2 = tquery2.getResultList();

			expectedPKs2 = new String[5];
			expectedPKs2[0] = "5";
			expectedPKs2[1] = "6";
			expectedPKs2[2] = "12";
			expectedPKs2[3] = "14";
			expectedPKs2[4] = "16";

			if (!checkEntityPK(clist1, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for first query.  Expected 5 reference, got: " + clist1.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for first query");
				pass1 = true;
			}

			if (!checkEntityPK(clist2, expectedPKs2)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results for second query.  Expected 5 reference, got: " + clist2.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received for second query");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest37 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotNull(customer.get(Customer_.work).get(Address_.zip)))
					.select(customer.get(Customer_.work).get(Address_.zip))
					.orderBy(cbuilder.asc(customer.get(Customer_.work).get(Address_.zip)));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> clist = tquery.getResultList();

			String[] result = (String[]) (clist.toArray(new String[clist.size()]));
			logger.log(Logger.Level.TRACE, "Compare results of work zip codes");
			pass = Arrays.equals(expectedZips, result);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest47 failed");
		}
	}

	/*
	 * @testName: queryTest52
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424; PERSISTENCE:SPEC:789
	 * 
	 * @test_Strategy: Define a query using Boolean operator AND in a conditional
	 * test ( True AND True = True) where the second condition is NULL. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void queryTest52() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.and(cbuilder.isNotNull(customer.get(Customer_.country)),
					cbuilder.equal(customer.get(Customer_.name), cbuilder.parameter(String.class, "cName"))));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("cName", "Shelly D. McGowan");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest52 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all work zip codes");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.work).get(Address_.zip));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> clist = tquery.getResultList();

			if (clist.size() != 18) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + clist.size());
			} else {
				pass1 = true;
				int numOfNull = 0;
				int foundZip = 0;
				for (String s : clist) {
					logger.log(Logger.Level.TRACE, "Check contents of List for null");
					Object o = s;
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest56 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find home zip codes");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.home).get(Address_.street), "212 Edgewood Drive"))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			s = tquery.getSingleResult();

			if (s != null) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected null.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest58 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine which customers have an null name");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNull(customer.get(Customer_.name)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest59 failed");
		}
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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> w = customer.join(Customer_.work, JoinType.LEFT);
			cquery.where(cbuilder.isNull(w.get(Address_.zip)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "13";
			expectedPKs[1] = "19";
			expectedPKs[2] = "20";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest61 failed");
		}
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
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get(Customer_.work).get(Address_.city),
					cbuilder.parameter(String.class, "workcity")));
			cquery.select(cbuilder.construct(Customer.class, customer.get(Customer_.id), customer.get(Customer_.name)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("workcity", "Burlington");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

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
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 18 references, got: " + clist.size());
				pass = false;
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest64 failed");
		}

	}

	/*
	 * @testName: queryTest69
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:384; PERSISTENCE:SPEC:389;
	 * PERSISTENCE:SPEC:406; PERSISTENCE:SPEC:824; PERSISTENCE:SPEC:392;
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
		final Long expectedResult1 = Long.valueOf(17);
		final Long expectedResult2 = Long.valueOf(16);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			CriteriaQuery<Long> cquery1 = cbuilder.createQuery(Long.class);
			Root<Customer> customer1 = cquery1.from(Customer.class);
			cquery1.select(cbuilder.count(customer1.get(Customer_.home).get(Address_.city)));
			TypedQuery<Long> tquery1 = getEntityManager().createQuery(cquery1);
			Long result1 = tquery1.getSingleResult();

			if (!(result1.equals(expectedResult1))) {
				logger.log(Logger.Level.ERROR,
						"Query1 in queryTest69 returned:" + result1 + " expected: " + expectedResult1);
			} else {
				logger.log(Logger.Level.TRACE, "query1 in queryTest69 returned expected results");
				pass1 = true;
			}
			CriteriaQuery<Long> cquery2 = cbuilder.createQuery(Long.class);
			Root<Customer> customer2 = cquery2.from(Customer.class);
			cquery2.select(cbuilder.countDistinct(customer2.get(Customer_.home).get(Address_.city)));
			TypedQuery<Long> tquery2 = getEntityManager().createQuery(cquery2);
			Long result2 = tquery2.getSingleResult();

			if (!(result2.equals(expectedResult2))) {
				logger.log(Logger.Level.ERROR,
						"Query 2 in queryTest69 returned:" + result2 + " expected: " + expectedResult2);
			} else {
				logger.log(Logger.Level.TRACE, "query 2 in queryTest69 returned expected results");
				pass2 = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("queryTest69 failed");
		}

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

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Check if a spouse is related to a customer");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Spouse> spouse = cquery.from(Spouse.class);
			cquery.where(cbuilder.equal(spouse.get(Spouse_.id), "7")).select(spouse.get(Spouse_.customer));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.getSingleResult();

			getEntityTransaction().commit();
		} catch (NoResultException e) {
			logger.log(Logger.Level.TRACE, "queryTest71: NoResultException caught as expected : " + e);
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest71 failed");
		}
	}

	/*
	 * @testName: test_leftouterjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:780
	 * 
	 * @test_Strategy: LEFT OUTER JOIN for 1-M relationship. Retrieve credit card
	 * information for a customer with name like Caruso.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_leftouterjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join(Customer_.creditCards, JoinType.LEFT);
			cquery.where(cbuilder.like(customer.get(Customer_.name), "%Caruso")).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_leftouterjoin_1xM failed");
		}
	}

	/*
	 * @testName: test_groupBy
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:810; PERSISTENCE:SPEC:756;
	 * 
	 * @test_Strategy: Test for Only Group By in a simple select statement. Country
	 * is an Embeddable entity.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_groupBy() throws Exception {
		boolean pass = false;
		final String expectedCodes[] = new String[] { "CHA", "GBR", "IRE", "JPN", "USA" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.country).get(Country_.code));
			cquery.groupBy(customer.get(Customer_.country).get(Country_.code));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(country.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCodes, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 4 Country Codes: "
						+ "CHA, GBR, JPN, USA. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.TRACE, " Credit Card Type: " + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_groupBy failed");
		}
	}

	/*
	 * @testName: test_innerjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:372;
	 * PERSISTENCE:SPEC:372.2; PERSISTENCE:JAVADOC:987
	 * 
	 * @test_Strategy: Inner Join for 1-1 relationship. Select all customers with
	 * spouses.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_innerjoin_1x1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join(Customer_.spouse);
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

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
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_innerjoin_1x1 failed");
		}
	}

	/*
	 * @testName: fetchFetchSingularAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:965
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchFetchSingularAttributeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.spouse);

			Fetch f1 = f.fetch(Customer_.spouse);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {

				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}
			JoinType jt = f1.getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;
			} else {

				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {

			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fetchFetchSingularAttributeTest failed");
		}
	}

	/*
	 * @testName: fetchFetchSingularAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:966
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchFetchSingularAttributeJoinTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.spouse);

			Fetch f1 = f.fetch(Customer_.spouse, JoinType.INNER);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}
			JoinType jt = f1.getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fetchFetchSingularAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fetchTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:962; PERSISTENCE:JAVADOC:963;
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> c1 = cquery.from(Customer.class);

			JoinType jt = c1.fetch(Customer_.spouse).getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			From<Customer, Customer> c2 = cquery.from(Customer.class);
			jt = c2.fetch(Customer_.spouse, JoinType.INNER).getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass2 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.INNER.name() + ", actual:" + jt.name());
			}
			From<Customer, Customer> c3 = cquery.from(Customer.class);
			jt = c3.fetch(Customer_.spouse, JoinType.LEFT).getJoinType();
			if (jt.equals(JoinType.LEFT)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + jt.name());
				pass3 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType:" + JoinType.LEFT.name() + ", actual:" + jt.name());
			}

			/*
			 * JoinType.RIGHT Not Required in JPA 2.0 jt =
			 * customer.fetch(Customer_.spouse,JoinType.RIGHT).getJoinType(); if
			 * (jt.equals(JoinType.RIGHT)) {
			 * logger.log(Logger.Level.TRACE,"Received expected JoinType"); pass=true; }
			 * else { logger.log(Logger.Level.ERROR,"Expected JoinType:" +
			 * JoinType.RIGHT.name() + ", actual:" + jt.name()); }
			 */

			From<Customer, Customer> c4 = cquery.from(Customer.class);
			Attribute attr = c4.fetch(Customer_.spouse).getAttribute();
			if (attr.getName().equals(Customer_.spouse.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass4 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.spouse.getName() + ", actual:" + attr.getName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("fetchTest failed");
		}
	}

	/*
	 * @testName: fetchGetParentTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:964
	 * 
	 * @test_Strategy: Get FetchParent then get the fetches that it contains and
	 * verify we got the correct one.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchGetParentTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			FetchParent fp = customer.fetch(Customer_.spouse).getParent();
			Set<Fetch<?, ?>> f = fp.getFetches();
			if (f.size() == 1) {
				for (Fetch tmpFetch : f) {
					String name = tmpFetch.getAttribute().getName();
					if (name.equals("spouse")) {
						logger.log(Logger.Level.TRACE, "Received expected Fetch:" + name
								+ ", which means the correct FetchParent was returned");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Expected: spouse, actual:" + name);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected 1 fetch to be returned from FetchParent.getFetches(), got:" + f.size());
				for (Fetch tmpFetch : f) {
					String name = tmpFetch.getAttribute().getName();
					logger.log(Logger.Level.ERROR, "Fetch returned was:" + name);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchGetParentTest failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_1x1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:774;
	 * PERSISTENCE:SPEC:776; PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:982;
	 * PERSISTENCE:JAVADOC:974; PERSISTENCE:JAVADOC:1017;
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_fetchjoin_1x1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			Collection cFetches = customer.getFetches();
			if (cFetches.size() != 0) {
				logger.log(Logger.Level.ERROR,
						"Did not get correct number of fetches, expected:0, actual:" + cFetches.size());
				for (Object o : cFetches) {
					logger.log(Logger.Level.ERROR, "Fetch:" + o.toString());
				}
			}

			customer.fetch(Customer_.spouse);
			cFetches = customer.getFetches();
			if (cFetches.size() == 1) {
				logger.log(Logger.Level.TRACE, "Received expected number of fetches" + cFetches.size());
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get correct number of fetches, expected:1, actual:" + cFetches.size());
				for (Object o : cFetches) {
					logger.log(Logger.Level.ERROR, "Fetch:" + o.toString());
				}

			}
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_fetchjoin_1x1 failed");
		}
	}

	/*
	 * @testName: fetchSingularAttributeJoinType1X1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:975; PERSISTENCE:JAVADOC:1018;
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch the spouses for all
	 * Customers.
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fetchSingularAttributeJoinType1X1Test() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.spouse, JoinType.INNER);
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchSingularAttributeJoinType1X1Test failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:782; PERSISTENCE:SPEC:374;
	 * PERSISTENCE:SPEC:777; PERSISTENCE:SPEC:783; PERSISTENCE:JAVADOC:977;
	 * PERSISTENCE:SPEC:1715;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_fetchjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders, JoinType.LEFT);
			cquery.where(customer.get(Customer_.home).get(Address_.state).in("NY", "RI"));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "14";
			expectedPKs[1] = "17";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "test_fetchjoin_1xM failed", e);
		}

		if (!pass) {
			throw new Exception("test_fetchjoin_1xM failed");
		}
	}

	/*
	 * @testName: test_groupByHaving
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:808; PERSISTENCE:SPEC:353;
	 * PERSISTENCE:SPEC:757; PERSISTENCE:SPEC:391
	 * 
	 * @test_Strategy: Test for Group By and Having in a select statement Select the
	 * count of customers in each country where Country is China, England
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_groupByHaving() throws Exception {
		boolean pass = false;
		final Long expectedGBR = Long.valueOf(2);
		final Long expectedCHA = Long.valueOf(4);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.groupBy(customer.get(Customer_.country).get(Country_.code))
					.having(customer.get(Customer_.country).get(Country_.code).in("GBR", "CHA"))
					.select(cbuilder.count(customer));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(country.length);
			List<Long> result = tquery.getResultList();

			int numOfExpected = 0;
			for (Long l : result) {
				logger.log(Logger.Level.TRACE, "Check result received . . . ");
				if ((l.equals(expectedGBR)) || (l.equals(expectedCHA))) {
					numOfExpected++;
				}
			}

			if (numOfExpected != 2) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 2 Values returned : "
						+ "2 with Country Code GBR and 4 with Country Code CHA. " + "Received: " + result.size());
				for (Long l : result) {
					logger.log(Logger.Level.TRACE, "count of Codes Returned: " + l);
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_groupByHaving failed");
		}
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
		String result;
		final String expectedCustomer = "Margaret Mills";

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer.get(Customer_.name)).groupBy(customer.get(Customer_.name))
					.having(cbuilder.equal(customer.get(Customer_.name),
							cbuilder.concat("Margaret ", cbuilder.parameter(String.class, "lname"))));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("lname", "Mills");
			result = tquery.getSingleResult();

			if (result.equals(expectedCustomer)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {

				logger.log(Logger.Level.TRACE, "test_concatHavingClause:  Did not get expected results. " + "Expected: "
						+ expectedCustomer + ", got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_concatHavingClause failed");
		}
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
		boolean pass1 = false;
		boolean pass2 = false;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(cbuilder.count(customer.get(Customer_.country).get(Country_.code)))
					.groupBy(customer.get(Customer_.country).get(Country_.code))
					.having(cbuilder.equal(cbuilder.lower(customer.get(Customer_.country).get(Country_.code)), "gbr"));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			List<Long> result = tquery.getResultList();

			if (result.size() == 1) {
				pass1 = true;
				Long l = (Long) result.get(0);
				if (l.equals(expectedCount)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected result:" + expectedCount + ", actual:" + l);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected 1, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception(" test_lowerHavingClause failed");
		}
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
		boolean pass1 = false;
		boolean pass2 = false;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(cbuilder.count(customer.get(Customer_.country).get(Country_.country)))
					.groupBy(customer.get(Customer_.country).get(Country_.country)).having(cbuilder
							.equal(cbuilder.upper(customer.get(Customer_.country).get(Country_.country)), "ENGLAND"));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			List<Long> result = tquery.getResultList();

			if (result.size() == 1) {
				pass1 = true;
				Long l = (Long) result.get(0);
				if (l.equals(expectedCount)) {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected result:" + expectedCount + ", actual:" + l);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected 1, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("test_upperHavingClause failed");
		}
	}

	/*
	 * @testName: test_lengthHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:SPEC:369.4
	 * 
	 * @test_Strategy:Test for Functional Expression: length in Having Clause Select
	 * all customer names having the length of the city of the home address = 10
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void test_lengthHavingClause() throws Exception {
		boolean pass = false;
		final String[] expectedCities = new String[] { "Burlington", "Chelmsford", "Roslindale" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Address> a = customer.join(Customer_.home);
			cquery.groupBy(a.get(Address_.city)).having(cbuilder.equal(cbuilder.length(a.get(Address_.city)), 10))
					.select(a.get(Address_.city));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCities, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 Cities, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_lengthHavingClause failed");
		}
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
		final String[] expectedCusts = new String[] { "Alan E. Frechette", "Arthur D. Frechette" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.groupBy(customer.get(Customer_.name))
					.having(cbuilder.gt(cbuilder.locate(customer.get(Customer_.name), "Frechette"), 0))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedCusts, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 Customers, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_locateHavingClause failed");
		}
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
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Address> w = sqc.join(Customer_.work);
			sq.select(w.get(Address_.state));
			sq.where(cbuilder.equal(w.get(Address_.state), cbuilder.parameter(String.class, "state")));
			cquery.where(customer.get(Customer_.home).get(Address_.state).in(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("state", "MA");
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_in failed");
		}
	}
}
