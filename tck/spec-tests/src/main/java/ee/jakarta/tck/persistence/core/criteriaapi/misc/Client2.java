/*
 * Copyright (c) 2012, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.criteriaapi.misc;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.UtilCustomerData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;

public class Client2 extends UtilCustomerData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_misc2.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: predicateGetExpressionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1116
	 *
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void predicateGetExpressionsTest() throws Exception {
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			EntityType<Customer> Customer_ = customer.getModel();

			List<Integer> actual = new ArrayList<Integer>();
			List<Integer> expected = new ArrayList<Integer>();
			expected.add(Integer.parseInt(customerRef[0].getId()));
			expected.add(Integer.parseInt(customerRef[1].getId()));

			logger.log(Logger.Level.INFO, "Testing disjunction");
			getEntityTransaction().begin();
			Expression expr1 = cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1");
			Expression expr2 = cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "2");
			Predicate predicate = cbuilder.or(expr1, expr2);
			List<Expression<Boolean>> c = predicate.getExpressions();
			if (c.size() != 2) {
				logger.log(Logger.Level.ERROR, "Expected a predicate expression size of:2, actual:" + c.size());
			} else {
				pass2 = true;
			}
			cquery.select(customer);
			cquery.where(cbuilder.or(c.get(0), c.get(1)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();
			for (Customer cust : result) {
				logger.log(Logger.Level.TRACE, "result:" + cust);
				actual.add(Integer.parseInt(cust.getId()));
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + i);
				}
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + i);
				}
			}

			actual = new ArrayList<Integer>();
			expected = new ArrayList<Integer>();
			expected.add(Integer.parseInt(customerRef[0].getId()));
			cquery = null;
			cquery = cbuilder.createQuery(Customer.class);
			customer = cquery.from(Customer.class);

			logger.log(Logger.Level.INFO, "Testing conjunction");
			expr1 = cbuilder.equal(customer.get(Customer_.getSingularAttribute("id", String.class)), "1");
			expr2 = cbuilder.equal(customer.get(Customer_.getSingularAttribute("name", String.class)),
					"Alan E. Frechette");
			predicate = cbuilder.and(expr1, expr2);
			c = predicate.getExpressions();
			if (c.size() != 2) {
				logger.log(Logger.Level.ERROR, "Expected a predicate expression size of:2, actual:" + c.size());
			} else {
				pass4 = true;
			}
			cquery.select(customer);
			cquery.where(cbuilder.and(c.get(0), c.get(1)));

			tquery = getEntityManager().createQuery(cquery);
			result = tquery.getResultList();
			for (Customer cust : result) {
				logger.log(Logger.Level.TRACE, "result:" + cust);
				actual.add(Integer.parseInt(cust.getId()));
			}
			Collections.sort(actual);
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {

				logger.log(Logger.Level.TRACE, "Received expected results");
				pass5 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + i);
				}
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + i);
				}
			}
			getEntityTransaction().commit();

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		if (!pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("predicateGetExpressionsTest failed");

		}
	}

	/*
	 * @testName: predicateIsNotNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1125;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NOT NULL
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void predicateIsNotNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[19];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[4].getId();
		expected[5] = customerRef[5].getId();
		expected[6] = customerRef[6].getId();
		expected[7] = customerRef[7].getId();
		expected[8] = customerRef[8].getId();
		expected[9] = customerRef[9].getId();
		expected[10] = customerRef[10].getId();
		expected[11] = customerRef[12].getId();
		expected[12] = customerRef[13].getId();
		expected[13] = customerRef[14].getId();
		expected[14] = customerRef[15].getId();
		expected[15] = customerRef[16].getId();
		expected[16] = customerRef[17].getId();
		expected[17] = customerRef[18].getId();
		expected[18] = customerRef[19].getId();

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			getEntityTransaction().begin();

			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			if (customer != null) {

				Predicate pred = customer.<String>get("name").isNotNull();
				cquery.where(pred);
				cquery.select(customer);

				TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
				List<Customer> clist = tquery.getResultList();

				if (!checkEntityPK(clist, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
							+ " references, got: " + clist.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("predicateIsNotNullTest failed");
		}
	}

	/*
	 * @testName: predicateIsNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1126;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NULL
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void predicateIsNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[11].getId();

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			getEntityTransaction().begin();

			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			if (customer != null) {

				Predicate pred = customer.<String>get("name").isNull();
				cquery.where(pred);
				cquery.select(customer);

				TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
				List<Customer> clist = tquery.getResultList();

				if (!checkEntityPK(clist, expected)) {
					logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
							+ " references, got: " + clist.size());
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("predicateIsNullTest failed");
		}
	}

	/*
	 * @testName: pathInObjectArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1106;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathInObjectArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Object[] o = { "1", "2" };
			cquery.where(idPath.in(o));
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathInObjectArrayTest failed");
		}
	}

	/*
	 * @testName: pathInExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1109;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.ID = 1
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathInExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Expression e = cbuilder.literal("1");
			cquery.where(idPath.in(e));

			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathInExpressionTest failed");
		}
	}

	/*
	 * @testName: pathInExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1107;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
	 *
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathInExpressionArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Expression[] e = { cbuilder.literal("1"), cbuilder.literal("2") };
			cquery.where(idPath.in(e));

			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathInExpressionArrayTest failed");
		}
	}

	/*
	 * @testName: pathInCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1108;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.ID IN (1,2)
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathInCollectionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("id", String.class));
			Collection<String> col = new ArrayList<String>();
			col.add("1");
			col.add("2");

			cquery.where(idPath.in(col));
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathInCollectionTest failed");
		}
	}

	/*
	 * @testName: pathIsNotNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1110;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NOT NULL
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathIsNotNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[19];
		expected[0] = customerRef[0].getId();
		expected[1] = customerRef[1].getId();
		expected[2] = customerRef[2].getId();
		expected[3] = customerRef[3].getId();
		expected[4] = customerRef[4].getId();
		expected[5] = customerRef[5].getId();
		expected[6] = customerRef[6].getId();
		expected[7] = customerRef[7].getId();
		expected[8] = customerRef[8].getId();
		expected[9] = customerRef[9].getId();
		expected[10] = customerRef[10].getId();
		expected[11] = customerRef[12].getId();
		expected[12] = customerRef[13].getId();
		expected[13] = customerRef[14].getId();
		expected[14] = customerRef[15].getId();
		expected[15] = customerRef[16].getId();
		expected[16] = customerRef[17].getId();
		expected[17] = customerRef[18].getId();
		expected[18] = customerRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("name", String.class));
			cquery.where(idPath.isNotNull());

			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathIsNotNullTest failed");
		}
	}

	/*
	 * @testName: pathIsNullTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1111;
	 *
	 * @test_Strategy: SELECT c FROM Customer c WHERE c.name IS NULL
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void pathIsNullTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[11].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			EntityType<Customer> Customer_ = customer.getModel();
			Path<String> idPath = customer.get(Customer_.getSingularAttribute("name", String.class));
			cquery.where(idPath.isNull());
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("pathIsNullTest failed");
		}
	}

	/*
	 * @testName: subqueryInObjectArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1207;
	 *
	 * @test_Strategy:
	 *
	 * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
	 * WHERE (c1.ID IN (1)))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryInObjectArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			cquery.select(customer);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			Object[] o = { "1" };
			sq.where(sqc.get(Customer_.id).in(o));
			sq.select(sqc.get(Customer_.id));

			cquery.where(customer.get(Customer_.id).in(sq));

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryInObjectArrayTest failed");
		}
	}

	/*
	 * @testName: subqueryInExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1208;
	 *
	 * @test_Strategy:
	 *
	 * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
	 * WHERE (c1.ID IN (1)))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryInExpressionArrayTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			cquery.select(customer);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			Expression[] exp = { cbuilder.literal("1") };
			sq.where(sqc.get(Customer_.id).in(exp));
			sq.select(sqc.get(Customer_.id));

			cquery.where(customer.get(Customer_.id).in(sq));

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryInExpressionArrayTest failed");
		}
	}

	/*
	 * @testName: subqueryInExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1210;
	 *
	 * @test_Strategy:
	 *
	 * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
	 * WHERE (c1.ID IN (1)))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryInExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			cquery.select(customer);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			Expression exp = cbuilder.literal("1");
			sq.where(sqc.get(Customer_.id).in(exp));
			sq.select(sqc.get(Customer_.id));

			cquery.where(customer.get(Customer_.id).in(sq));

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryInExpressionTest failed");
		}
	}

	/*
	 * @testName: subqueryInCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1209;
	 *
	 * @test_Strategy:
	 *
	 * SELECT c.id FROM Customer c WHERE c.ID IN (SELECT c1.id FROM Customer c1
	 * WHERE (c1.ID IN (1)))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryInCollectionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			cquery.select(customer);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			Collection col = new ArrayList();
			col.add("1");
			sq.where(sqc.get(Customer_.id).in(col));
			sq.select(sqc.get(Customer_.id));

			// sq.where(cbuilder.in(customer.get("id"), "1"));
			cquery.where(customer.get(Customer_.id).in(sq));

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);

			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryInCollectionTest failed");
		}
	}

	/*
	 * @testName: subqueryIsNotNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1211;
	 *
	 * @test_Strategy: SELECT c.id FROM Customer c WHERE c.id IN (SELECT c1.ID FROM
	 * Customer c1 WHERE ((c1.NAME IS NOT NULL) AND (t1.ID = "1")))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryIsNotNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[0].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			sq.where(cbuilder.and(cbuilder.isNotNull(sqc.get(Customer_.name)),
					cbuilder.equal(sqc.get(Customer_.id), "1")));
			sq.select(sqc.get(Customer_.id));

			cquery.where(customer.get(Customer_.id).in(sq));
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryIsNotNull test failed");

		}

	}

	/*
	 * @testName: subqueryIsNull
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1212;
	 *
	 * @test_Strategy: SELECT c.id FROM Customer c WHERE c.id IN (SELECT c1.ID FROM
	 * Customer c1 WHERE (c1.NAME IS NULL))
	 *
	 */
	@SetupMethod(name = "setupCustomerData")
	@Test
	public void subqueryIsNull() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = customerRef[11].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();

		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		Root<Customer> customer = cquery.from(Customer.class);
		if (customer != null) {

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Customer> sqc = sq.from(Customer.class);
			sq.where(cbuilder.isNull(sqc.get(Customer_.name)));
			sq.select(sqc.get(Customer_.id));

			cquery.where(customer.get(Customer_.id).in(sq));
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null root");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("subqueryIsNull test failed");

		}

	}

	/*
	 * @testName: getGroupRestriction
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:938
	 *
	 * @test_Strategy:
	 */

	@SetupMethod(name = "setupCustomerData")
	@Test
	public void getGroupRestriction() throws Exception {
		boolean pass = false;

		try {
			CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

			CriteriaQuery cquery = qbuilder.createQuery();
			if (cquery != null) {
				Root<Customer> customer = cquery.from(Customer.class);
				Predicate[] pred = { qbuilder.like(customer.get(Customer_.name), "K%") };
				cquery.having(pred);
				Predicate restriction = cquery.getGroupRestriction();
				if (restriction != null) {
					cquery.groupBy(customer.get("name"));
					EntityType<Customer> Customer_ = customer.getModel();

					cquery.select(customer.get(Customer_.getSingularAttribute("name", String.class)));

					List<String> actual = getEntityManager().createQuery(cquery).getResultList();

					List<String> expected = new ArrayList<String>();
					expected.add(customerRef[5].getName());
					expected.add(customerRef[9].getName());
					expected.add(customerRef[13].getName());

					if (expected.containsAll(actual) && actual.containsAll(expected)
							&& expected.size() == actual.size()) {

						logger.log(Logger.Level.TRACE, "Received expected results");
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Did not get expected results");
						for (String s : expected) {
							logger.log(Logger.Level.ERROR, "expected:" + s);
						}
						for (String s : actual) {
							logger.log(Logger.Level.ERROR, "actual:" + s);
						}
					}

				} else {
					logger.log(Logger.Level.ERROR, "getGroupRestriction returned null instead of groupBy expressions");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception getGroupRestriction: " + e);
		}

		if (!pass) {
			throw new Exception("getGroupRestriction failed");
		}
	}
}
