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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.LineItem;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.Order_;
import ee.jakarta.tck.persistence.common.schema30.UtilOrderData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CollectionJoin;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class Client6 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client6.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client6.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder6.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: avg
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:742; PERSISTENCE:SPEC:1510;
	 * PERSISTENCE:SPEC:1740;
	 *
	 * @test_Strategy: Convert the following JPQL to CriteriaQuery SELECT
	 * AVG(o.totalPrice) FROM Order o
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void avg() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> order_ = order.getModel();
			cquery.select(cbuilder.avg(order.get(order_.getSingularAttribute("totalPrice", Double.class))));

			TypedQuery<Double> tq = getEntityManager().createQuery(cquery);
			Double d1 = 1487.29;
			Double d2 = 1487.30;

			Double d3 = tq.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "Avg test returned expected results: " + d1);
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("avg test  failed");
		}
	}

	/*
	 * @testName: max
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:815; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1674;
	 *
	 * @test_Strategy: Convert following JPQL to CriteriaQuery SELECT DISTINCT
	 * MAX(l.quantity) FROM LineItem l
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void max() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 8;
		List<Integer> i2, i3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery1 = cbuilder.createQuery(Integer.class);
		CriteriaQuery<Integer> cquery2 = cbuilder.createQuery(Integer.class);
		if (cquery1 != null && cquery2 != null) {
			logger.log(Logger.Level.TRACE,
					"select DISTINCT MAXIMUM number of lineItem quantities available an order may have");
			Root<LineItem> lineItem = cquery1.from(LineItem.class);
			// Get Metamodel from Root
			EntityType<LineItem> lineItem_ = lineItem.getModel();
			cquery1.select(cbuilder.max(lineItem.get(lineItem_.getSingularAttribute("quantity", Integer.class))));
			cquery1.distinct(true);
			TypedQuery<Integer> tq1 = getEntityManager().createQuery(cquery1);
			i2 = tq1.getResultList();

			logger.log(Logger.Level.TRACE, "select MAXIMUM number of lineItem quantities available an order may have");
			Root<LineItem> lineItem2 = cquery2.from(LineItem.class);

			// Get Metamodel from Root
			EntityType<LineItem> lineItem2_ = lineItem2.getModel();
			cquery2.select(cbuilder.max(lineItem2.get(lineItem2_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq2 = getEntityManager().createQuery(cquery2);

			i3 = tq2.getResultList();

			logger.log(Logger.Level.INFO, "Verify select WITH DISTINCT keyword");
			if (i2.size() == 1) {
				Integer result = i2.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass1 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i2) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}
			logger.log(Logger.Level.INFO, "Verify select WITHOUT DISTINCT keyword");
			if (i3.size() == 1) {
				Integer result = i3.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass2 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i3) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("max test failed");
		}
	}

	/*
	 * @testName: min
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:816; PERSISTENCE:SPEC:1740;
	 * PERSISTENCE:SPEC:1674;
	 *
	 * @test_Strategy: convert the forllowing JPQL into CriteriaQuery SELECT
	 * DISTINCT MIN(l.quantity) FROM LineItem l
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void min() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 1;
		List<Integer> i2, i3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Integer> cquery1 = cbuilder.createQuery(Integer.class);
		CriteriaQuery<Integer> cquery2 = cbuilder.createQuery(Integer.class);
		if (cquery1 != null && cquery2 != null) {
			logger.log(Logger.Level.TRACE,
					"select DISTINCT MIN number of lineItem quantities available an order may have");
			Root<LineItem> lineItem = cquery1.from(LineItem.class);
			// Get Metamodel from Root
			EntityType<LineItem> lineItem_ = lineItem.getModel();
			cquery1.select(cbuilder.min(lineItem.get(lineItem_.getSingularAttribute("quantity", Integer.class))));
			cquery1.distinct(true);
			TypedQuery<Integer> tq1 = getEntityManager().createQuery(cquery1);
			i2 = tq1.getResultList();

			logger.log(Logger.Level.TRACE, "select MIN number of lineItem quantities available an order may have");
			Root<LineItem> lineItem2 = cquery2.from(LineItem.class);

			// Get Metamodel from Root
			EntityType<LineItem> lineItem2_ = lineItem2.getModel();
			cquery2.select(cbuilder.min(lineItem2.get(lineItem2_.getSingularAttribute("quantity", Integer.class))));

			TypedQuery<Integer> tq2 = getEntityManager().createQuery(cquery2);

			i3 = tq2.getResultList();

			logger.log(Logger.Level.INFO, "Verify select WITH DISTINCT keyword");
			if (i2.size() == 1) {
				Integer result = i2.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass1 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i2) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}
			logger.log(Logger.Level.INFO, "Verify select WITHOUT DISTINCT keyword");
			if (i3.size() == 1) {
				Integer result = i3.get(0);
				if (result != null) {
					if (result.equals(i1)) {
						logger.log(Logger.Level.TRACE, "Received expected results:" + result);
						pass2 = true;
					} else {
						logger.log(Logger.Level.TRACE, "Expected: " + i1 + ", actual:" + result);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Receive null result from query");
				}
			} else {
				logger.log(Logger.Level.ERROR, "Receive more than one result:");
				for (Integer i : i3) {
					logger.log(Logger.Level.ERROR, "Received:" + i);
				}
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("max test failed");
		}
	}

	/*
	 * @testName: greatest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:777
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void greatest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			logger.log(Logger.Level.TRACE, "find Greatest Order id Using lexicographic comparision");

			cquery.select(cbuilder.greatest(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			// lexicographic comparision should return 9 as the greatest id
			// from the following list of ids
			// { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 }
			String expectedResult = "9";
			String queryOutput = tq.getSingleResult();

			if (queryOutput.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result : " + expectedResult);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Query returned : " + queryOutput + " Expected result : " + expectedResult);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("greatest test failed");
		}
	}

	/*
	 * @testName: least
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:794
	 *
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void least() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			logger.log(Logger.Level.TRACE, "find least Order id Using lexicographic comparision");

			cquery.select(cbuilder.least(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			// lexicographic comparision should return 1 as the least id
			// from the following list of ids
			// { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 }
			String expectedResult = "1";
			String queryOutput = tq.getSingleResult();

			if (queryOutput.equals(expectedResult)) {
				logger.log(Logger.Level.TRACE, "Received expected result : " + expectedResult);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Query returned : " + queryOutput + " Expected result : " + expectedResult);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("least test failed");
		}
	}

	/*
	 * @testName: count
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:754; PERSISTENCE:SPEC:1740;
	 *
	 * @test_Strategy: convert the following JPQL to CriteriaQuery Select COUNT
	 * (o.id) From Order o
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void count() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);
			EntityType<Order> Order_ = order.getModel();
			cquery.select(cbuilder.count(order.get(Order_.getSingularAttribute("id", String.class))));

			TypedQuery<Long> tq = getEntityManager().createQuery(cquery);

			Long countResult = tq.getSingleResult();
			Long expectedCount = 20L;

			if (countResult.equals(expectedCount)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "count test returned:" + countResult + "expected: " + expectedCount);

			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");

		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("count test failed");
		}
	}

	/*
	 * @testName: some
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:847; PERSISTENCE:JAVADOC:1136;
	 * PERSISTENCE:SPEC:1766; PERSISTENCE:SPEC:1766.3;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery SELECT DISTINCT
	 * c FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= SOME(Select
	 * o.totalPrice FROM Order o, IN(o.lineItems) l WHERE l.quantity = 3 )
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void some() throws Exception {
		boolean pass = false;

		String[] expected = new String[18];
		for (int i = 0; i < 18; i++) {
			expected[i] = customerRef[i].getId();
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			// Get Root Customer
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodels
			EntityType<Order> Order_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Order.class);

			EntityType<LineItem> LineItem_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.LineItem.class);

			EntityType<Customer> Customer_ = customer.getModel();

			// Join Customer-Order
			Join<Customer, Order> orders = customer.join(Customer_.getCollection("orders", Order.class));

			// create Subquery instance
			Subquery<Double> sq = cquery.subquery(Double.class);

			// Create Roots
			Root<Order> order = sq.from(Order.class);

			// Join Order-LineItem
			Join<Order, LineItem> lineItems = order.join(Order_.getCollection("lineItemsCollection", LineItem.class));

			// Create SubQuery
			sq.select(order.get(Order_.getSingularAttribute("totalPrice", Double.class)))
					.where(cbuilder.equal(lineItems.get(LineItem_.getSingularAttribute("quantity", Integer.class)), 3));

			cquery.select(customer);

			// Create Main Query with SubQuery
			cquery.where(cbuilder.le(orders.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					cbuilder.some(sq)));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("some test failed");
		}
	}

	/*
	 * @testName: any
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:738; PERSISTENCE:SPEC:1766;
	 * PERSISTENCE:SPEC:1766.2;
	 *
	 * @test_Strategy: convert the following JPQL into CriteriaQuery SELECT DISTINCT
	 * object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice < ANY (Select
	 * o.totalPrice FROM Order o, IN(o.lineItems) l WHERE l.quantity = 3 )
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void any() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 18; i++) {
			if (i != 9) {
				expected[j++] = customerRef[i].getId();
			}
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
		if (cquery != null) {
			// Get Root Customer
			Root<Customer> customer = cquery.from(Customer.class);

			// Get Metamodels
			EntityType<Order> Order_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Order.class);

			EntityType<LineItem> LineItem_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.LineItem.class);

			EntityType<Customer> Customer_ = customer.getModel();

			// Join Customer-Order
			Join<Customer, Order> orders = customer.join(Customer_.getCollection("orders", Order.class));

			// create Subquery instance
			Subquery<Double> sq = cquery.subquery(Double.class);

			// Create Roots
			Root<Order> order = sq.from(Order.class);

			// Join Order-LineItem
			Join<Order, LineItem> lineItems = order.join(Order_.getCollection("lineItemsCollection", LineItem.class));

			// Create SubQuery
			sq.select(order.get(Order_.getSingularAttribute("totalPrice", Double.class)))
					.where(cbuilder.equal(lineItems.get(LineItem_.getSingularAttribute("quantity")), 3));

			cquery.select(customer);

			// Create Main Query with SubQuery
			cquery.where(
					cbuilder.lt(orders.get(Order_.getSingularAttribute("totalPrice", Double.class)), cbuilder.any(sq)));
			cquery.distinct(true);

			TypedQuery<Customer> tq = getEntityManager().createQuery(cquery);
			List<Customer> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Customer c : result) {
				actual.add(Integer.parseInt(c.getId()));
			}

			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("any test failed");
		}
	}

	/*
	 * @testName: notPredicate
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:821; PERSISTENCE:SPEC:1729;
	 *
	 * @test_Strategy: Select Distinct o FROM Order o WHERE NOT o.totalPrice < 4500
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notPredicate() throws Exception {
		boolean pass = false;

		final Double expectedTotalPrice = 4500.0D;

		String[] expected = new String[3];
		expected[0] = orderRef[4].getId();
		expected[1] = orderRef[10].getId();
		expected[2] = orderRef[15].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.lt(order.get(Order_.getSingularAttribute("totalPrice", Double.class)),
					expectedTotalPrice)));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notPredicate test failed");

		}
	}

	/*
	 * @testName: conjunction
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:751
	 *
	 * @test_Strategy: Use Conjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.conjunction() always returns true
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void conjunction() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isTrue(cbuilder.conjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("conjunction test failed");

		}
	}

	/*
	 * @testName: disjunction
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:766
	 *
	 * @test_Strategy: Use Disjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.disjunction() always returns false
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void disjunction() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isFalse(cbuilder.disjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("disjunction test failed");

		}
	}

	/*
	 * @testName: isTrue
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:790
	 *
	 * @test_Strategy: Use Conjunction Select Distinct o FROM Order o where
	 * o.customer.name = 'Robert E. Bissett'
	 *
	 * Note: cbuilder.conjunction() always returns true
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void isTrue() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isTrue(cbuilder.conjunction())));

			cquery.distinct(true);

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isTrue test failed");

		}
	}

	/*
	 * @testName: isFalse
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:782
	 *
	 * @test_Strategy: Use Disjunction Select o FROM Order o where o.customer.name =
	 * 'Robert E. Bissett'
	 *
	 * Note: cbuilder.disjunction() always returns false
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void isFalse() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = orderRef[3].getId();
		expected[1] = orderRef[8].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.and(
					cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"),
					cbuilder.isFalse(cbuilder.disjunction())));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("isFalse test failed");

		}
	}

	/*
	 * @testName: equalExpObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:768
	 *
	 * @test_Strategy: Use equal and not Select o FROM Order o where o.customer.name
	 * <> 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void equalExpObjectTest() throws Exception {
		boolean pass = false;
		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.equal(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett")));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("equalExpObjectTest failed");

		}
	}

	/*
	 * @testName: equalExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:767; PERSISTENCE:SPEC:1748;
	 *
	 * @test_Strategy: Use equal and not Select o FROM Order o where o.customer.name
	 * <> 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void equalExpExpTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.not(cbuilder.equal(
					order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)),
					cbuilder.literal("Robert E. Bissett"))));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("equalExpExpTest failed");

		}
	}

	/*
	 * @testName: notEqualExpObjectTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:823
	 *
	 * @test_Strategy: Use notEqual Select o FROM Order o where o.customer.name <>
	 * 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notEqualExpObjectTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.notEqual(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("name", String.class)), "Robert E. Bissett"));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notEqualExpObjectTest test failed");

		}
	}

	/*
	 * @testName: notEqualExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:822
	 *
	 * @test_Strategy: Use notEqual Select o FROM Order o where o.customer.name <>
	 * 'Robert E. Bissett'
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void notEqualExpExpTest() throws Exception {
		boolean pass = false;

		int j = 0;
		String[] expected = new String[17];
		for (int i = 0; i < 20; i++) {
			if (i != 3 && i != 12 & i != 8) {
				expected[j++] = orderRef[i].getId();
			}
		}
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		Metamodel mm = getEntityManager().getMetamodel();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			EntityType<Customer> Customer_ = mm.entity(ee.jakarta.tck.persistence.common.schema30.Customer.class);
			cquery.select(order);
			cquery.where(cbuilder.notEqual(
					order.get(Order_.getSingularAttribute("customer", Customer.class))
							.get(Customer_.getSingularAttribute("name", String.class)),
					cbuilder.literal("Robert E. Bissett")));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("notEqualExpExpTest test failed");

		}
	}

	/*
	 * @testName: abs
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:734
	 *
	 * @test_Strategy: Select o From Order o WHERE :dbl < ABS(- o.totalPrice)
	 *
	 * Note :dbl = 1180D
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void abs() throws Exception {
		boolean pass = false;

		String[] expected = new String[9];
		expected[0] = orderRef[0].getId();
		expected[1] = orderRef[1].getId();
		expected[2] = orderRef[3].getId();
		expected[3] = orderRef[4].getId();
		expected[4] = orderRef[5].getId();
		expected[5] = orderRef[10].getId();
		expected[6] = orderRef[15].getId();
		expected[7] = orderRef[16].getId();
		expected[8] = orderRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
		if (cquery != null) {
			Root<Order> order = cquery.from(Order.class);

			// Get Metamodel from Root
			EntityType<Order> Order_ = order.getModel();
			cquery.select(order);
			cquery.where(cbuilder.lt(cbuilder.parameter(Double.class, "dbl"),
					cbuilder.abs(cbuilder.neg(order.get(Order_.getSingularAttribute("totalPrice", Double.class))))));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("dbl", 1180D);

			List<Order> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Order o : result) {
				actual.add(Integer.parseInt(o.getId()));
			}
			if (!checkEntityPK(actual, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("abs test failed");

		}
	}

	/*
	 * @testName: joinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1716; PERSISTENCE:SPEC:1717;
	 * PERSISTENCE:SPEC:1613;
	 * 
	 * @test_Strategy: select o FROM Order o INNER JOIN o.lineItems l ON l.quantity
	 * > 5
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinOnExpressionTest() throws Exception {
		boolean pass = false;

		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection, JoinType.INNER);
			Expression exp = cbuilder.equal(lineItem.get("id"), "1");

			lineItem.on(exp);
			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			throw new Exception("queryTest61 failed", e);
		}

		if (!pass)
			throw new Exception("joinOnExpressionTest failed");
	}

	/*
	 * @testName: joinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1717; PERSISTENCE:JAVADOC:1715;
	 * 
	 * @test_Strategy: select o FROM Order o LEFT JOIN o.lineItems l ON (l.quantity
	 * > 5 AND l.quantity < 9)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinOnPredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection, JoinType.INNER);
			Predicate pred = lineItem.getOn();
			if (pred == null) {
				logger.log(Logger.Level.TRACE, "Received expected null from getOn()");
				pass1 = true;

			} else {
				logger.log(Logger.Level.ERROR, "Expected getOn() to return null:");
				List<Expression<Boolean>> lExp = pred.getExpressions();
				for (Expression exp : lExp) {
					logger.log(Logger.Level.ERROR, "actual:" + exp.toString());
				}
			}
			Predicate[] predArray = { cbuilder.equal(lineItem.get("id"), "1") };

			lineItem.on(predArray);

			pred = lineItem.getOn();
			if (pred == null) {
				logger.log(Logger.Level.ERROR, "Received null from getOn()");
			} else {
				pass2 = true;
				logger.log(Logger.Level.TRACE, "getOn() returned non-null:");
				List<Expression<Boolean>> lExp = pred.getExpressions();
				for (Expression exp : lExp) {
					logger.log(Logger.Level.TRACE, "actual:" + exp.toString());
				}
			}

			cquery.select(customer);

			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass3 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
			throw new Exception("queryTest61 failed", e);
		}

		if (!pass1 || !pass2 || !pass3)
			throw new Exception("joinOnPredicateArrayTest failed");
	}

}
