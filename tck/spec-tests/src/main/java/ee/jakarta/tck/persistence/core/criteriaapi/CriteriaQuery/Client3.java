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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaQuery;

import java.lang.System.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Country;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.UtilCustomerData;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client3 extends UtilCustomerData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		classes = concat(getSchema30classes(), classes);
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery3.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: multiselect
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:924; PERSISTENCE:SPEC:1751;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select c.id,
	 * c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void multiselect() throws Exception {
		boolean pass = false;
		final int expectedResultSize = 20;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			cquery.multiselect(customer.get(Customer_.getSingularAttribute("id", String.class)),
					customer.get(Customer_.getSingularAttribute("name", String.class)));

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();

			if (result.size() == expectedResultSize) {
				logger.log(Logger.Level.TRACE, "Result size =" + result.size());
				pass = true;

			} else {
				logger.log(Logger.Level.ERROR, "Received incorrect result size =" + result.size());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("multiselect test failed");
		}
	}

	/*
	 * @testName: multiselectListTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:926
	 *
	 * @test_Strategy: Select c.id, c.name from Customer c
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void multiselectListTest() throws Exception {
		boolean pass = false;
		final int expectedResultSize = 20;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();
			logger.log(Logger.Level.TRACE, "Use Tuple Query");

			List list = new ArrayList();
			list.add(customer.get(Customer_.getSingularAttribute("id", String.class)));
			list.add(customer.get(Customer_.getSingularAttribute("name", String.class)));

			cquery.multiselect(list);

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();

			if (result.size() == expectedResultSize) {
				logger.log(Logger.Level.TRACE, "Result size =" + result.size());
				pass = true;

			} else {
				logger.log(Logger.Level.ERROR, "Received incorrect result size =" + result.size());
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("multiselectListTest failed");
		}
	}

	/*
	 * @testName: where
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:932; PERSISTENCE:SPEC:1725;
	 * PERSISTENCE:SPEC:1726; PERSISTENCE:SPEC:1735; PERSISTENCE:SPEC:1735.2;
	 *
	 * @test_Strategy: Use Conjunction Select c FROM Customer c where customer.name
	 * = 'Robert E. Bissett'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void where() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();
			cquery.select(customer);
			cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("name", String.class)),
					"Robert E. Bissett"));

			Query q = getEntityManager().createQuery(cquery);

			List result = q.getResultList();
			int expectedResultSize = 1;

			if (result != null) {
				if (result.size() == expectedResultSize) {
					logger.log(Logger.Level.TRACE, "Successfully returned expected results");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Mismatch in received results - expected = " + expectedResultSize
							+ " received = " + result.size());
				}
			} else {
				logger.log(Logger.Level.ERROR, "Missing expected result");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("where test failed");

		}
	}

	/*
	 * @testName: createQueryCriteriaUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1516
	 *
	 * @test_Strategy: UPDATE Customer c SET c.name = 'foobar' WHERE c.id = 1
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void createQueryCriteriaUpdateTest() throws Exception {
		boolean pass = false;
		String expected = "foobar";
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaUpdate<Customer> cquery = cbuilder.createCriteriaUpdate(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		cquery.set(customer.get("name"), expected);
		cquery.where(cbuilder.equal(customer.get("id"), "1"));

		getEntityManager().createQuery(cquery).executeUpdate();
		getEntityTransaction().commit();
		clearCache();

		getEntityTransaction().begin();

		Customer actual = getEntityManager().find(Customer.class, "1");
		if (actual == null) {
			logger.log(Logger.Level.ERROR, "Received null result from find");
		} else {
			if (actual.getName().equals(expected)) {
				logger.log(Logger.Level.TRACE, "Name was successfully updated");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + expected + ", actual:" + actual.getName());
			}
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQueryCriteriaUpdateTest failed");

		}
	}

	/*
	 * @testName: createQueryCriteriaDeleteTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1518
	 *
	 * @test_Strategy: DELETE FROM Customer c WHERE c.id = "1"
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void createQueryCriteriaDeleteTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaDelete<Customer> cquery = cbuilder.createCriteriaDelete(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		EntityType<Customer> Customer_ = customer.getModel();
		cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1"));

		getEntityManager().createQuery(cquery).executeUpdate();
		getEntityTransaction().commit();
		clearCache();

		getEntityTransaction().begin();

		Customer actual = getEntityManager().find(Customer.class, "1");
		if (actual != null) {
			logger.log(Logger.Level.ERROR, "Expected null result from find, actual = " + actual.toString());
		} else {
			logger.log(Logger.Level.TRACE, "Customer was successfully deleted");
			pass = true;
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("createQueryCriteriaDeleteTest failed");

		}
	}

	/*
	 * @testName: fromGetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1029;
	 * 
	 * @test_Strategy: Verify get(String) returns the correct results
	 *
	 * Select c FROM Customer c where customer.name = 'Karen R. Tegan'
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void fromGetStringTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.equal(customer.get("name"), "Karen R. Tegan"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			if (result.size() == 1) {
				if (!result.get(0).getId().equals("6")) {
					logger.log(Logger.Level.ERROR, "Expected id:6, actual:" + result.get(0).getId());
				} else {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get correct number of results, expected:1, actual:" + result.size());
				for (Customer c : result) {
					logger.log(Logger.Level.ERROR, "id:" + c.getId());
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("fromGetStringTest failed");
		}
	}

	/*
	 * @testName: groupBy
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:321; PERSISTENCE:SPEC:1770;
	 *
	 * @test_Strategy: select c.country.code, count(c.country.code) FROM Customer c
	 * GROUP BY c.country.code ORDER BY c.country.code"
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void groupBy() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
		expected.add(new ExpectedResult("CHA", "4"));
		expected.add(new ExpectedResult("GBR", "2"));
		expected.add(new ExpectedResult("IRE", "2"));
		expected.add(new ExpectedResult("JPN", "1"));
		expected.add(new ExpectedResult("USA", "11"));

		try {

			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

			Metamodel mm = getEntityManagerFactory().getMetamodel();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Customer> customer = cquery.from(Customer.class);
			EntityType<Customer> Customer_ = customer.getModel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

			cquery.multiselect(
					customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class)),
					cbuilder.count(customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class))));
			cquery.groupBy(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));
			cquery.orderBy(cbuilder.asc(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class))));

			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			Collection<Tuple> result = tquery.getResultList();

			int i = 0;
			int passCount1 = 0;
			int passCount2 = 0;
			if (result.size() == expected.size()) {
				for (Tuple actual : result) {
					logger.log(Logger.Level.TRACE, "code=" + actual.get(0) + ", count=" + actual.get(1));
					if (!actual.get(0).equals(expected.get(i).arg1)) {
						logger.log(Logger.Level.ERROR,
								"Expected: " + expected.get(i).arg1 + ", actual:" + actual.get(0));
					} else {
						passCount1++;
					}
					if (!actual.get(1).equals(Long.parseLong(expected.get(i).arg2))) {
						logger.log(Logger.Level.ERROR,
								"Expected: " + expected.get(i).arg2 + ", actual:" + actual.get(1));
					} else {
						passCount2++;
					}
					i++;
				}
				if (passCount1 == expected.size()) {
					pass1 = true;
				}
				if (passCount2 == expected.size()) {
					pass2 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected number of entries, expected:" + expected.size()
						+ ", actual:" + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception groupBy: ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("groupBy failed");
		}
	}

	/*
	 * @testName: groupByExpArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:920; PERSISTENCE:JAVADOC:943;
	 * PERSISTENCE:SPEC:1772; PERSISTENCE:SPEC:1775;
	 *
	 * @test_Strategy: Create a groupBy clause with one expression, then create a
	 * second one and verify the second overrides the first. select c.country.code,
	 * c.id FROM Customer c GROUP BY c.country.code, c.id ORDER BY c.country.code,
	 * c.id"
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void groupByExpArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
		expected.add(new ExpectedResult("CHA", "13"));
		expected.add(new ExpectedResult("CHA", "18"));
		expected.add(new ExpectedResult("CHA", "19"));
		expected.add(new ExpectedResult("CHA", "20"));
		expected.add(new ExpectedResult("GBR", "11"));
		expected.add(new ExpectedResult("GBR", "16"));
		expected.add(new ExpectedResult("IRE", "12"));
		expected.add(new ExpectedResult("IRE", "17"));
		expected.add(new ExpectedResult("JPN", "14"));
		expected.add(new ExpectedResult("USA", "1"));
		expected.add(new ExpectedResult("USA", "10"));
		expected.add(new ExpectedResult("USA", "15"));
		expected.add(new ExpectedResult("USA", "2"));
		expected.add(new ExpectedResult("USA", "3"));
		expected.add(new ExpectedResult("USA", "4"));
		expected.add(new ExpectedResult("USA", "5"));
		expected.add(new ExpectedResult("USA", "6"));
		expected.add(new ExpectedResult("USA", "7"));
		expected.add(new ExpectedResult("USA", "8"));
		expected.add(new ExpectedResult("USA", "9"));

		try {

			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

			Metamodel mm = getEntityManagerFactory().getMetamodel();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);
			Selection[] selection = {
					customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class)),
					customer.get(Customer_.getSingularAttribute("id", String.class)) };

			Expression[] expressionArray1 = { customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)) };
			Expression[] expressionArray2 = {
					customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class)),
					customer.get(Customer_.getSingularAttribute("id", String.class)) };
			cquery.multiselect(selection);
			cquery.groupBy(expressionArray1);
			cquery.groupBy(expressionArray2);
			cquery.orderBy(
					cbuilder.asc(customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class))),
					cbuilder.asc(customer.get(Customer_.getSingularAttribute("id", String.class))));

			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			Collection<Tuple> result = tquery.getResultList();
			if (TestUtil.traceflag) {
				for (Tuple actual : result) {
					logger.log(Logger.Level.TRACE, "Actual - code=" + actual.get(0) + ", id=" + actual.get(1));
				}
			}

			int i = 0;
			int passCount1 = 0;
			int passCount2 = 0;
			for (Tuple actual : result) {
				logger.log(Logger.Level.TRACE, "verifying: code=" + actual.get(0) + ", id=" + actual.get(1));

				if (!actual.get(0).equals(expected.get(i).getArg1())) {
					logger.log(Logger.Level.ERROR,
							"Expected getArg1:" + expected.get(i).getArg1() + ", actual.get(0):" + actual.get(0));
				} else {
					passCount1++;
				}
				if (!actual.get(1).equals(expected.get(i).getArg2())) {
					logger.log(Logger.Level.ERROR,
							"Expected getArg2:" + expected.get(i).getArg2() + ", actual.get(1):" + actual.get(1));
				} else {
					passCount2++;
				}

				i++;
			}
			if (passCount1 == expected.size()) {
				pass1 = true;
			}
			if (passCount2 == expected.size()) {
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("groupByExpArrayTest failed");
		}
	}

	/*
	 * @testName: groupByListTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:921; PERSISTENCE:JAVADOC:929;
	 * PERSISTENCE:JAVADOC:944
	 *
	 * @test_Strategy: Create a groupBy clause using a List with one expression,
	 * then create a second one and verify the second overrides the first. Create a
	 * orderBy clause using a List with one expression, then create a second one and
	 * verify the second overrides the first. select c.country.code, c.id FROM
	 * Customer c GROUP BY c.country.code, c.id ORDER BY c.country.code, c.id"
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void groupByListTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		final ArrayList<ExpectedResult> expected = new ArrayList<ExpectedResult>();
		expected.add(new ExpectedResult("CHA", "13"));
		expected.add(new ExpectedResult("CHA", "18"));
		expected.add(new ExpectedResult("CHA", "19"));
		expected.add(new ExpectedResult("CHA", "20"));
		expected.add(new ExpectedResult("GBR", "11"));
		expected.add(new ExpectedResult("GBR", "16"));
		expected.add(new ExpectedResult("IRE", "12"));
		expected.add(new ExpectedResult("IRE", "17"));
		expected.add(new ExpectedResult("JPN", "14"));
		expected.add(new ExpectedResult("USA", "1"));
		expected.add(new ExpectedResult("USA", "10"));
		expected.add(new ExpectedResult("USA", "15"));
		expected.add(new ExpectedResult("USA", "2"));
		expected.add(new ExpectedResult("USA", "3"));
		expected.add(new ExpectedResult("USA", "4"));
		expected.add(new ExpectedResult("USA", "5"));
		expected.add(new ExpectedResult("USA", "6"));
		expected.add(new ExpectedResult("USA", "7"));
		expected.add(new ExpectedResult("USA", "8"));
		expected.add(new ExpectedResult("USA", "9"));

		try {

			CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

			Metamodel mm = getEntityManagerFactory().getMetamodel();

			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);
			Selection[] selection = {
					customer.get(Customer_.getSingularAttribute("country", Country.class))
							.get(Country_.getSingularAttribute("code", String.class)),
					customer.get(Customer_.getSingularAttribute("id", String.class)) };

			List groupByList1 = new ArrayList();
			groupByList1.add(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));

			List groupByList2 = new ArrayList();
			groupByList2.add(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));
			groupByList2.add(customer.get(Customer_.getSingularAttribute("id", String.class)));

			cquery.multiselect(selection);
			cquery.groupBy(groupByList1);
			cquery.groupBy(groupByList2);

			List orderList1 = new ArrayList();
			orderList1.add(cbuilder.asc(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class))));

			List orderList2 = new ArrayList();
			orderList2.add(cbuilder.asc(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class))));
			orderList2.add(cbuilder.asc(customer.get(Customer_.getSingularAttribute("id", String.class))));

			cquery.orderBy(orderList1);
			cquery.orderBy(orderList2);

			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			Collection<Tuple> result = tquery.getResultList();

			if (TestUtil.traceflag) {
				for (Tuple actual : result) {
					logger.log(Logger.Level.TRACE, "Actual - code=" + actual.get(0) + ", id=" + actual.get(1));
				}
			}

			int i = 0;
			int passCount1 = 0;
			int passCount2 = 0;
			for (Tuple actual : result) {
				logger.log(Logger.Level.TRACE, "verifying: code=" + actual.get(0) + ", id=" + actual.get(1));

				if (!actual.get(0).equals(expected.get(i).getArg1())) {
					logger.log(Logger.Level.ERROR,
							"Expected getArg1:" + expected.get(i).getArg1() + ", actual.get(0):" + actual.get(0));
				} else {
					passCount1++;
				}
				if (!actual.get(1).equals(expected.get(i).getArg2())) {
					logger.log(Logger.Level.ERROR,
							"Expected getArg2:" + expected.get(i).getArg2() + ", actual.get(1):" + actual.get(1));
				} else {
					passCount2++;
				}

				i++;
			}
			if (passCount1 == expected.size()) {
				pass1 = true;
			}
			if (passCount2 == expected.size()) {
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("groupByListTest failed");
		}
	}

	/*
	 * @testName: having
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:922; PERSISTENCE:JAVADOC:945;
	 * PERSISTENCE:SPEC:1735; PERSISTENCE:SPEC:1735.1; PERSISTENCE:SPEC:1735.3;
	 * PERSISTENCE:SPEC:1771;
	 *
	 * @test_Strategy: SELECT COUNT(c) FROM Customer c GROUP BY c.country.code
	 * HAVING c.country.code in ('GR', 'CHA')
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void having() throws Exception {
		boolean pass = false;
		final Long expectedGBR = 2L;
		final Long expectedCHA = 4L;
		final int expectedRows = 2;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		try {
			getEntityTransaction().begin();

			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			EntityType<Customer> Customer_ = customer.getModel();
			cquery.groupBy(customer.get(Customer_.getSingularAttribute("country", Country.class)).get("code"));
			Expression exp = customer.get(Customer_.getSingularAttribute("country", Country.class)).get("code")
					.in("GBR", "CHA");
			cquery.having(exp).select(cbuilder.count(customer));

			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			List<Long> result = tquery.getResultList();

			int numOfExpected = 0;
			for (Long val : result) {
				if ((val.equals(expectedGBR)) || (val.equals(expectedCHA))) {
					numOfExpected++;
				}
			}

			if (numOfExpected == expectedRows) {
				logger.log(Logger.Level.TRACE, "Expected results received.");
				pass = true;

			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 2 Values returned : "
						+ "2 with Country Code GBR and 4 with Country Code CHA. " + "Received: " + result.size());
				for (Long val : result) {
					logger.log(Logger.Level.ERROR, "Count of Codes Returned: " + val);
				}

			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass) {
			throw new Exception("having failed");
		}
	}

	/*
	 * @testName: distinct
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:917; PERSISTENCE:JAVADOC:934;
	 * PERSISTENCE:SPEC:1760; PERSISTENCE:SPEC:1761;
	 *
	 * @test_Strategy: SELECT DISTINCT CODE FROM CUSTOMER_TABLE SELECT CODE FROM
	 * CUSTOMER_TABLE SELECT CODE FROM CUSTOMER_TABLE *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void distinct() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		getEntityTransaction().begin();
		logger.log(Logger.Level.INFO, "True test");
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			Metamodel mm = getEntityManagerFactory().getMetamodel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

			cquery.select(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));

			cquery.distinct(true);

			Query q = getEntityManager().createQuery(cquery);

			List<String> actual = q.getResultList();
			Collections.sort(actual);

			List<String> expected = new ArrayList<String>();
			expected.add("CHA");
			expected.add("GBR");
			expected.add("IRE");
			expected.add("JPN");
			expected.add("USA");
			Collections.sort(expected);

			if (actual != null) {
				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					pass1 = true;
					if (TestUtil.traceflag) {

						logger.log(Logger.Level.TRACE, "Received expected results(" + actual.size() + "):");
						for (String s : actual) {
							logger.log(Logger.Level.TRACE, "code:" + s);
						}
					}
				} else {
					logger.log(Logger.Level.TRACE, "Expected(" + expected.size() + "):");
					for (String s : expected) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
					logger.log(Logger.Level.TRACE, "Actual(" + actual.size() + "):");
					for (String s : actual) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "getResultList() returned null result");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		logger.log(Logger.Level.INFO, "False test");
		cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			Metamodel mm = getEntityManagerFactory().getMetamodel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

			cquery.select(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));

			cquery.distinct(false);

			Query q = getEntityManager().createQuery(cquery);

			List<String> actual = q.getResultList();
			Collections.sort(actual);

			List<String> expected = new ArrayList<String>();
			expected.add("CHA");
			expected.add("CHA");
			expected.add("CHA");
			expected.add("CHA");
			expected.add("GBR");
			expected.add("GBR");
			expected.add("IRE");
			expected.add("IRE");
			expected.add("JPN");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			Collections.sort(expected);

			if (actual != null) {
				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					pass2 = true;
					if (TestUtil.traceflag) {

						logger.log(Logger.Level.TRACE, "Received expected results(" + actual.size() + "):");
						for (String s : expected) {
							logger.log(Logger.Level.TRACE, "code:" + s);
						}
					}
				} else {
					logger.log(Logger.Level.TRACE, "Expected(" + expected.size() + "):");
					for (String s : expected) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
					logger.log(Logger.Level.TRACE, "Actual(" + actual.size() + "):");
					for (String s : actual) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "getResultList() returned null result");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}
		logger.log(Logger.Level.INFO, "Default test");
		cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodel from Root
			EntityType<Customer> Customer_ = customer.getModel();

			Metamodel mm = getEntityManagerFactory().getMetamodel();
			EmbeddableType<Country> Country_ = mm.embeddable(Country.class);

			cquery.select(customer.get(Customer_.getSingularAttribute("country", Country.class))
					.get(Country_.getSingularAttribute("code", String.class)));

			Query q = getEntityManager().createQuery(cquery);

			List<String> actual = q.getResultList();
			Collections.sort(actual);

			List<String> expected = new ArrayList<String>();
			expected.add("CHA");
			expected.add("CHA");
			expected.add("CHA");
			expected.add("CHA");
			expected.add("GBR");
			expected.add("GBR");
			expected.add("IRE");
			expected.add("IRE");
			expected.add("JPN");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			expected.add("USA");
			Collections.sort(expected);

			if (actual != null) {
				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					pass3 = true;
					if (TestUtil.traceflag) {

						logger.log(Logger.Level.TRACE, "Received expected results(" + actual.size() + "):");
						for (String s : expected) {
							logger.log(Logger.Level.TRACE, "code:" + s);
						}
					}
				} else {
					logger.log(Logger.Level.TRACE, "Expected(" + expected.size() + "):");
					for (String s : expected) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
					logger.log(Logger.Level.TRACE, "Actual(" + actual.size() + "):");
					for (String s : actual) {
						logger.log(Logger.Level.TRACE, "code:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "getResultList() returned null result");
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}
		getEntityTransaction().commit();

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("distinct test failed");

		}
	}

	/*
	 * @testName: orderBy
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:928; PERSISTENCE:JAVADOC:1083;
	 * PERSISTENCE:JAVADOC:1099; PERSISTENCE:JAVADOC:1082; PERSISTENCE:SPEC:1736;
	 * 
	 * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
	 * NULL ORDER BY c.work.zip ASC
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void orderBy() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "00252", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "11345" };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")))
					.select(customer.get("work").<String>get("zip"));
			Expression<Path> exp1 = customer.get("work").get("zip");
			cquery.orderBy(cbuilder.asc(exp1));
			List<jakarta.persistence.criteria.Order> lOrder = cquery.getOrderList();
			if (lOrder.size() == 1) {
				jakarta.persistence.criteria.Order o = lOrder.get(0);
				if (!o.isAscending()) {
					logger.log(Logger.Level.ERROR, "isAscending() did not return an order of ascending");
				} else {
					pass1 = true;
				}
				if (o.getExpression() != null) {
					logger.log(Logger.Level.TRACE, "getExpression() returned non-null expression");
					pass2 = true;
				} else {
					logger.log(Logger.Level.ERROR, "getExpression() returned null");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected a size of 1, actual:" + lOrder.size());
			}

			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			List<String> clist = tquery.getResultList();

			String[] result = clist.toArray(new String[clist.size()]);
			logger.log(Logger.Level.TRACE, "Compare results of work zip codes");
			// if pass = false, don't call next comparison, it could
			// cause a false positive depending on the result
			pass3 = Arrays.equals(expectedZips, result);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception queryTest47: ", e);
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("orderBy failed");
		}
	}

	/*
	 * @testName: orderReverseTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1083; PERSISTENCE:JAVADOC:1084
	 * 
	 * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
	 * NULL ORDER BY c.work.zip ASC
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void orderReverseTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		final String[] expectedZips = new String[] { "11345", "00252", "00252", "00252", "00252", "00252", "00252",
				"00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252", "00252" };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")));
			cquery.select(customer.get("work").<String>get("zip"));
			cquery.orderBy(cbuilder.asc(customer.get("work").get("zip")).reverse());
			List<jakarta.persistence.criteria.Order> lOrder = cquery.getOrderList();
			if (lOrder.size() == 1) {
				jakarta.persistence.criteria.Order o = lOrder.get(0);
				if (o.isAscending()) {
					logger.log(Logger.Level.ERROR, "isAscending() did not return an order of descending");
				} else {
					pass1 = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected a size of 1, actual:" + lOrder.size());
			}

			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			List<String> clist = tquery.getResultList();

			String[] result = clist.toArray(new String[clist.size()]);
			logger.log(Logger.Level.TRACE, "Compare results of work zip codes");
			// if pass = false, don't call next comparison, it could
			// cause a false positive depending on the result
			pass2 = Arrays.equals(expectedZips, result);
			if (!pass2) {
				logger.log(Logger.Level.ERROR, "Results are incorrect:");
				for (String s : expectedZips) {
					logger.log(Logger.Level.ERROR, "Expected:" + s);
				}
				for (String s : result) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("orderReverseTest failed");
		}
	}

	/*
	 * @testName: getOrderList
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:918
	 * 
	 * @test_Strategy: Select c.work.zip from Customer c where c.work.zip IS NOT
	 * NULL ORDER BY c.work.zip ASC
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void getOrderList() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find work zip codes that are not null");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotNull(customer.get("work").get("zip")))
					.select(customer.get("work").<String>get("zip"))
					.orderBy(cbuilder.asc(customer.get("work").get("zip")));

			List<jakarta.persistence.criteria.Order> orderedList = cquery.getOrderList();

			if (orderedList != null) {
				if (orderedList.size() == 1) {
					pass = true;
					logger.log(Logger.Level.TRACE, "Received expected results");
				} else {
					logger.log(Logger.Level.ERROR, "Received Unexpected results");
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception queryTest47: ", e);
		}

		if (!pass) {
			throw new Exception("getOrderList failed");
		}
	}

	/*
	 * @testName: modifiedQueryTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1787; PERSISTENCE:SPEC:1790;
	 *
	 * @test_Strategy: Modify a query after it has been executed and verify results.
	 * Select c FROM Customer c where customer.name = 'Robert E. Bissett' Select c
	 * FROM Customer c where customer.name = 'Irene M. Caruso'
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void modifiedQueryTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(Integer.valueOf(customerRef[3].getId()));

		List<Integer> actual = new ArrayList<Integer>();
		logger.log(Logger.Level.INFO, "Testing initial query");

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		getEntityTransaction().begin();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();
			cquery.select(customer);
			cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("name", String.class)),
					"Robert E. Bissett"));
			Query q = getEntityManager().createQuery(cquery);

			List<Customer> result = q.getResultList();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for first query. Expected "
						+ expected.size() + " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}
			logger.log(Logger.Level.INFO, "Testing modified query");

			expected.clear();
			expected.add(Integer.valueOf(customerRef[7].getId()));
			actual.clear();
			cquery.select(customer.get("id"));
			cquery.where(cbuilder.equal(customer.get(Customer_.getSingularAttribute("name", String.class)),
					"Irene M. Caruso"));
			q = getEntityManager().createQuery(cquery);
			List<String> lResult = q.getResultList();
			if (lResult.size() == 1) {
				Object o = lResult.get(0);
				actual.add(Integer.parseInt((String) o));
			} else {
				logger.log(Logger.Level.ERROR, "Expected 1 result, actual:" + lResult.size());
			}

			Collections.sort(actual);

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results for second query. Expected "
						+ expected.size() + " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("modifiedQueryTest test failed");

		}
	}

	/*
	 *
	 * Setup for Query Language Tests
	 *
	 */

	public static class ExpectedResult {
		String arg1 = null;

		String arg2 = null;

		public ExpectedResult(String arg1) {
			this.arg1 = arg1;
		}

		public ExpectedResult(String arg1, String arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		public String getArg1() {
			return this.arg1;
		}

		public void setArg1(String arg1) {
			this.arg1 = arg1;
		}

		public String getArg2() {
			return this.arg2;
		}

		public void setArg2(String arg2) {
			this.arg2 = arg2;
		}

	}

	public void createATestData() {
		try {
			getEntityTransaction().begin();
			final Integer integer = 1234;
			final short basicShort = 12;
			final Short basicBigShort = basicShort;
			final float basicFloat = 12.3f;
			final Float basicBigFloat = basicFloat;
			final long basicLong = 1234l;
			final Long basicBigLong = basicLong;
			final double basicDouble = 1234.5;
			final Double basicBigDouble = basicDouble;
			final char[] charArray = { 'a', 'b', 'c' };
			final Character[] bigCharacterArray = { 'a', 'b', 'c' };
			final byte[] byteArray = "abc".getBytes();
			final Byte[] bigByteArray = { (byte) 111, (byte) 101, (byte) 100 };
			final BigInteger bigInteger = new BigInteger("12345");
			final BigDecimal bigDecimal = new BigDecimal(bigInteger);
			final Date date = new Date();
			final long timeInSeconds = date.getTime();
			final Time time = new Time(timeInSeconds);
			final Timestamp timeStamp = new Timestamp(timeInSeconds);
			final Calendar calendar = Calendar.getInstance();

			A aRef = new A("9", null, 9, integer, basicShort, basicBigShort, basicFloat, basicBigFloat, basicLong,
					basicBigLong, basicDouble, basicBigDouble, 'a', charArray, bigCharacterArray, byteArray,
					bigByteArray, bigInteger, bigDecimal, date, time, timeStamp, calendar);

			getEntityManager().persist(aRef);
			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in createTestData:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception during Rollback:", re);
			}
		}

	}

	private void removeATestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
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
