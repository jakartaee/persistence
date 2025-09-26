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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.CreditCard;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.LineItem;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilOrderData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class Client2 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_strquery2.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */
	/*
	 * @testName: queryTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:312; PERSISTENCE:SPEC:322;
	 * PERSISTENCE:SPEC:602; PERSISTENCE:SPEC:603; PERSISTENCE:JAVADOC:91;
	 * PERSISTENCE:SPEC:785; PERSISTENCE:JAVADOC:689;
	 * 
	 * @test_Strategy: This query is defined on a many-one relationship. Verify the
	 * results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest1() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Orders for Customer: Robert E. Bissett");

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);

			cquery.where(cbuilder.equal(order.get("customer").get("name"), cbuilder.parameter(String.class, "name")))
					.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("name", "Robert E. Bissett");
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest1 failed");
		}
	}

	/*
	 * @testName: queryTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:760;
	 * PERSISTENCE:SPEC:761
	 * 
	 * @test_Strategy: Execute a query to find customers with a certain credit card
	 * type. This query is defined on a one-many relationship. Verify the results
	 * were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest5() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all Customers with AXP Credit Cards");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, CreditCard> a = customer.join("creditCards");
			cquery.where(cbuilder.equal(a.get("type"), cbuilder.parameter(String.class, "ccard")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("ccard", "AXP");
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "1";
			expectedPKs[1] = "4";
			expectedPKs[2] = "5";
			expectedPKs[3] = "8";
			expectedPKs[4] = "9";
			expectedPKs[5] = "12";
			expectedPKs[6] = "15";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 7 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest5 failed");
		}
	}

	/*
	 * @testName: queryTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:345
	 * 
	 * @test_Strategy: Execute a query containing a conditional expression composed
	 * with logical operator NOT. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest8() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where the total price is NOT less than $4500");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.lt(order.<Double>get("totalPrice"), 4500).not());
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "5";
			expectedPKs[1] = "11";
			expectedPKs[2] = "16";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest8 failed");
		}
	}

	/*
	 * @testName: queryTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:348.4; PERSISTENCE:SPEC:345
	 * 
	 * @test_Strategy: Execute a query containing a a conditional expression
	 * composed with logical operator OR. Verify the results were accurately
	 * returned.
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest9() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where the customer name is Karen R. Tegan"
					+ " OR the total price is less than $100");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get("customer").get("name"), "Karen R. Tegan"),
					cbuilder.lt(order.<Double>get("totalPrice"), 100)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "10";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest9 failed");
		}
	}

	/*
	 * @testName: queryTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:346; PERSISTENCE:SPEC:347;
	 * PERSISTENCE:SPEC:348.2; PERSISTENCE:SPEC:344
	 * 
	 * @test_Strategy: Execute a query containing a conditional expression composed
	 * with AND and OR and using standard bracketing () for ordering. The comparison
	 * operator < and arithmetic operations are also used in the query. Verify the
	 * results were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest10() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where line item quantity is 1 AND the"
					+ " order total less than 100 or customer name is Robert E. Bissett");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			Join<Order, LineItem> l = order.join("lineItemsCollection");
			cquery.where(
					cbuilder.or(
							cbuilder.and(cbuilder.lt(l.<Integer>get("quantity"), 2),
									cbuilder.lt(order.<Double>get("totalPrice"),
											cbuilder.sum(cbuilder.sum(cbuilder.literal(3),
													cbuilder.prod(cbuilder.literal(54), 2)), -8))),
							cbuilder.equal(order.get("customer").get("name"), "Robert E. Bissett")));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest10 failed");
		}
	}

	/*
	 * @testName: queryTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:338; PERSISTENCE:JAVADOC:736
	 * 
	 * @test_Strategy: Execute the findOrdersByQuery9 method using conditional
	 * expression composed with AND with an input parameter as a conditional factor.
	 * The comparison operator < is also used in the query. Verify the results were
	 * accurately returned. //CHANGE THIS TO INPUT/NAMED PARAMETER
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest11() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"find all orders with line item quantity < 2" + " for customer Robert E. Bissett");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			Join<Order, LineItem> l = order.join("lineItemsCollection");
			cquery.where(cbuilder.and(cbuilder.lt(l.<Integer>get("quantity"), 2),
					cbuilder.equal(order.get("customer").get("name"), "Robert E. Bissett")));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest11 failed");
		}
	}

	/*
	 * @testName: queryTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query containing the comparison operator BETWEEN.
	 * Verify the results were accurately returned.
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest12() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders with a total price BETWEEN $1000 and $1200");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.between(order.<Double>get("totalPrice"), 1000D, 1200D)).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "7";
			expectedPKs[3] = "8";
			expectedPKs[4] = "14";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest12 failed");
		}
	}

	/*
	 * @testName: queryTest13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349
	 * 
	 * @test_Strategy: Execute a query containing NOT BETWEEN. Verify the results
	 * were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest13() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders with a total price NOT BETWEEN $1000 and $1200");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.between(order.<Double>get("totalPrice"), 1000D, 1200D).not()).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "2";
			expectedPKs[1] = "4";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";
			expectedPKs[4] = "9";
			expectedPKs[5] = "10";
			expectedPKs[6] = "11";
			expectedPKs[7] = "12";
			expectedPKs[8] = "13";
			expectedPKs[9] = "15";
			expectedPKs[10] = "16";
			expectedPKs[11] = "17";
			expectedPKs[12] = "18";
			expectedPKs[13] = "19";
			expectedPKs[14] = "20";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest13 failed");
		}
	}

	/*
	 * @testName: queryTest14
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:345; PERSISTENCE:SPEC:334
	 * 
	 * @test_Strategy: Conditional expressions are composed of other conditional
	 * expressions, comparison operators, logical operations, path expressions that
	 * evaluate to boolean values and boolean literals.
	 *
	 * Execute a query method that contains a conditional expression with a path
	 * expression that evaluates to a boolean literal. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest14() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders that do not have approved Credit Cards");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.isFalse(order.get("creditCard").<Boolean>get("approved")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[6];
			expectedPKs[0] = "1";
			expectedPKs[1] = "7";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "18";
			expectedPKs[5] = "20";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 6 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest14 failed");
		}
	}

	/*
	 * @testName: queryTest27
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.5
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function ABS in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest27() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all Orders with a total price greater than 1180");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.lt(cbuilder.parameter(Double.class, "dbl"),
					cbuilder.abs(order.<Double>get("totalPrice"))));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("dbl", 1180D);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[9];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "4";
			expectedPKs[3] = "5";
			expectedPKs[4] = "6";
			expectedPKs[5] = "11";
			expectedPKs[6] = "16";
			expectedPKs[7] = "17";
			expectedPKs[8] = "18";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 9 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest27 failed");
		}
	}

	/*
	 * @testName: queryTest32
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363
	 * 
	 * @test_Strategy: Execute a query using the comparison operator MEMBER in a
	 * collection member expression with an identification variable and omitting the
	 * optional reserved word OF. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest32() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where line items are members of the orders");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			Root<LineItem> l = cquery.from(LineItem.class);
			cquery.where(cbuilder.isMember(l, order.<Collection<LineItem>>get("lineItemsCollection")));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[orderRef.length];
			for (int i = 0; i < orderRef.length; i++) {
				expectedPKs[i] = Integer.toString(i + 1);
			}

			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + orderRef.length
						+ "references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest32 failed");
		}
	}

	/*
	 * @testName: queryTest33
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352.1; PERSISTENCE:JAVADOC:787
	 * 
	 * @test_Strategy: Execute a query using the comparison operator NOT MEMBER in a
	 * collection member expression with input parameter omitting the optional use
	 * of 'OF'. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest33() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		LineItem liDvc;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find orders whose orders are do NOT contain the specified line items");
			liDvc = getEntityManager().find(LineItem.class, "30");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.isNotMember(cbuilder.parameter(LineItem.class, "liDvc"),
					order.<Collection<LineItem>>get("lineItemsCollection")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("liDvc", liDvc);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[19];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
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

			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest33 failed");
		}
	}

	/*
	 * @testName: queryTest34
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363.1
	 * 
	 * @test_Strategy: Execute a query using the comparison operator MEMBER OF in a
	 * collection member expression using single_valued_association_path_expression.
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest34() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find orders who have Samples in their orders");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.isMember(order.<LineItem>get("sampleLineItem"),
					order.<Collection<LineItem>>get("lineItemsCollection")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "1";
			expectedPKs[1] = "6";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest34 failed");
		}
	}

	/*
	 * @testName: queryTest35
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352
	 * 
	 * @test_Strategy: Execute a query using comparison operator NOT IN in a
	 * comparison expression within the WHERE clause where the value for the
	 * state_field_path_expression contains numeric values. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest35() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders which contain lineitems not of quantities 1 or 5");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			Join<Order, LineItem> l = order.join("lineItemsCollection");
			cquery.where(l.get("quantity").in(1, 5).not());
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[9];
			expectedPKs[0] = "10";
			expectedPKs[1] = "12";
			expectedPKs[2] = "14";
			expectedPKs[3] = "15";
			expectedPKs[4] = "16";
			expectedPKs[5] = "17";
			expectedPKs[6] = "18";
			expectedPKs[7] = "19";
			expectedPKs[8] = "20";
			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 9 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest35 failed");
		}
	}

	/*
	 * @testName: queryTest39
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.6; PERSISTENCE:SPEC:814;
	 * PERSISTENCE:SPEC:816
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function SQRT
	 * in a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest39() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final double dbl = 50;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find customers with specific credit card balance");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, CreditCard> b = customer.join("creditCards");
			cquery.where(
					cbuilder.equal(cbuilder.sqrt(b.<Double>get("balance")), cbuilder.parameter(Double.class, "dbl")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("dbl", 50D);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest39 failed");
		}
	}

	/*
	 * @testName: queryTest42
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:423
	 * 
	 * @test_Strategy: This tests that nulls are eliminated using a
	 * single-valued_association_field with IS NOT NULL. Verify results are
	 * accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest42() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where related customer name is not null");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.isNotNull(order.get("customer").get("name")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> olist = tquery.getResultList();

			expectedPKs = new String[19];
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
			expectedPKs[17] = "19";
			expectedPKs[18] = "20";

			if (!checkEntityPK(olist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + olist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest42 failed");
		}
	}

	/*
	 * @testName: queryTest48
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:329; PERSISTENCE:SPEC:348.1;
	 * PERSISTENCE:SPEC:399.1;
	 * 
	 * @test_Strategy: This query, which includes a null non-terminal
	 * association-field, verifies the null is not included in the result set.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest48() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		final Double[] expectedBalances = new Double[] { 400D, 500D, 750D, 1000D, 1400D, 1500D, 2000D, 2500D, 4400D,
				5000D, 5500D, 7000D, 7400D, 8000D, 9500D, 13000D, 15000D, 23000D };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all credit card balances");
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order.get("creditCard").<Double>get("balance")).distinct(true);
			cquery.orderBy(cbuilder.asc(order.get("creditCard").get("balance")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			List<Double> olist = tquery.getResultList();

			Double[] result = (Double[]) (olist.toArray(new Double[olist.size()]));
			for (Double d : result) {

				logger.log(Logger.Level.TRACE, "query results returned:  " + d);
			}
			logger.log(Logger.Level.TRACE, "Compare expected results to query results");
			pass = Arrays.equals(expectedBalances, result);

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest48 failed");
		}
	}

	/*
	 * @testName: queryTest60
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:775; PERSISTENCE:SPEC:773
	 * 
	 * @test_Strategy: This query contains an identification variable defined in a
	 * collection member declaration which is not used in the rest of the query
	 * however, a JOIN operation needs to be performed for the correct result set.
	 * Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest60() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with an Order");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join("orders");
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest60 failed");
		}
	}

	/*
	 *
	 * @testName: queryTest62
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.8
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function SIZE
	 * in a functional expression within the WHERE clause. The SIZE function returns
	 * an integer value the number of elements of the Collection. Verify the results
	 * were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest62() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.ge(cbuilder.size(customer.<Collection>get("orders")), 2));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "14";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest62 failed");
		}
	}

	/*
	 * @testName: queryTest63
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.8
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function SIZE
	 * in a functional expression within the WHERE clause. The SIZE function returns
	 * an integer value the number of elements of the Collection.
	 *
	 * If the Collection is empty, the SIZE function evaluates to zero.
	 *
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest63() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.gt(cbuilder.size(customer.<Collection>get("orders")), 100));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[0];

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("queryTest63 failed");
		}
	}

	/*
	 * @testName: queryTest65
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:381; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function MIN.
	 * Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest65() throws Exception {
		boolean pass = false;
		final String s1 = "4";
		String s2;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find MINIMUM order id for Robert E. Bissett");
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.equal(order.get("customer").get("name"), "Robert E. Bissett"))
					.select(cbuilder.least(order.<String>get("id")));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			s2 = (String) tquery.getSingleResult();

			if (s2.equals(s1)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest65 returned " + s2 + "expected: " + s1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("queryTest65 failed");
		}
	}

	/*
	 * @testName: queryTest66
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:382; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function MAX.
	 * Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest66() throws Exception {
		boolean pass = false;
		final Integer i1 = Integer.valueOf(8);
		Integer i2;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find MAXIMUM number of lineItem quantities available an order may have");
			CriteriaQuery<Integer> cquery = cbuilder.createQuery(Integer.class);
			Root<LineItem> l = cquery.from(LineItem.class);
			cquery.select(cbuilder.max(l.<Integer>get("quantity")));
			TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
			i2 = (Integer) tquery.getSingleResult();

			if (i2.equals(i1)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest66 returned:" + i2 + "expected: " + i1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("queryTest66 failed");
		}
	}

	/*
	 * @testName: queryTest67
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:380; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:826; PERSISTENCE:SPEC:821; PERSISTENCE:SPEC:814;
	 * PERSISTENCE:SPEC:818
	 * 
	 * @test_Strategy: Execute a query using the aggregate function AVG. Verify the
	 * results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest67() throws Exception {
		boolean pass = false;
		final Double d1 = 1487.29;
		final Double d2 = 1487.30;
		Double d3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find AVERAGE price of all orders");
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(cbuilder.avg(order.<Double>get("totalPrice")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			d3 = (Double) tquery.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "queryTest67 returned expected results: " + d1);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest67 returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("queryTest67 failed");
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
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_leftouterjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join("creditCards", JoinType.LEFT);
			cquery.where(cbuilder.like(customer.<String>get("name"), "%Caruso")).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_leftouterjoin_1xM failed");
		}
	}

	/*
	 * @testName: test_leftouterjoin_Mx1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:399.1;
	 * PERSISTENCE:SPEC:399
	 * 
	 * @test_Strategy: Left Outer Join for M-1 relationship. Retrieve customer
	 * information from Order.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_leftouterjoin_Mx1() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		final Object[][] expectedResultSet = new Object[][] { new Object[] { "15", "14" },
				new Object[] { "16", "14" } };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Order> order = cquery.from(Order.class);
			Join<Order, Customer> c = order.join("customer", JoinType.LEFT);
			// criteria queries don't support positional parameters, using "one"
			// as parameter name instead
			cquery.where(cbuilder.equal(c.get("name"), cbuilder.parameter(String.class, "one")));
			cquery.multiselect(order.get("id"), c.get("id"));
			cquery.orderBy(cbuilder.asc(order.get("id")));
			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("one", "Kellie A. Sanborn");
			List<Tuple> q = tquery.getResultList();

			if (q.size() != 2) {
				logger.log(Logger.Level.TRACE,
						"test_leftouterjoin_Mx1:  Did not get expected results. " + "Expected 2,  got: " + q.size());
			} else {
				pass1 = true;

				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				// each element of the list q should be a size-2 array
				for (int i = 0; i < q.size(); i++) {
					Object obj = q.get(i);
					Object[] orderAndCustomerExpected = expectedResultSet[i];
					Tuple orderAndCustomerTuple = null;
					Object[] orderAndCustomer = null;
					if (obj instanceof Tuple) {
						logger.log(Logger.Level.TRACE,
								"The element in the result list is of type Object[], continue . . .");
						orderAndCustomerTuple = (Tuple) obj;
						orderAndCustomer = orderAndCustomerTuple.toArray();
						if (!Arrays.equals(orderAndCustomerExpected, orderAndCustomer)) {
							logger.log(Logger.Level.ERROR,
									"Expecting element value: " + Arrays.asList(orderAndCustomerExpected)
											+ ", actual element value: " + Arrays.asList(orderAndCustomer));
							pass2 = false;
							break;
						}
					} else {
						logger.log(Logger.Level.ERROR, "The element in the result list is not of type Object[]:" + obj);
						break;
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("test_leftouterjoin_Mx1 failed");
		}
	}

	/*
	 * @testName: test_groupBy_1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:810
	 * 
	 * @test_Strategy: Test for Only Group By in a simple select statement without
	 * using an Embeddable Entity in the query.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_groupBy_1() throws Exception {
		boolean pass = false;
		final String expectedTypes[] = new String[] { "AXP", "MCARD", "VISA" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<CreditCard> cc = cquery.from(CreditCard.class);
			cquery.select(cc.<String>get("type")).groupBy(cc.get("type"));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));
			Arrays.sort(output);

			pass = Arrays.equals(expectedTypes, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 3 Credit Card Types: "
						+ "AXP, MCARD, VISA. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.ERROR, " Credit Card Type: " + s);
				}

			}
			// verify this
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_groupBy_1 failed");
		}
	}

	/*
	 * @testName: test_innerjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779
	 * 
	 * @test_Strategy: Inner Join for 1-M relationship. Retrieve credit card
	 * information for all customers.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_innerjoin_1xM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, CreditCard> cc = customer.join("creditCards");
			cquery.where(cbuilder.equal(cc.get("type"), "VISA")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[8];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "6";
			expectedPKs[4] = "7";
			expectedPKs[5] = "10";
			expectedPKs[6] = "14";
			expectedPKs[7] = "17";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 8 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_innerjoin_1xM failed");
		}
	}

	/*
	 * @testName: test_innerjoin_Mx1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779; PERSISTENCE:SPEC:373
	 * 
	 * @test_Strategy: Inner Join for M-1 relationship. Retrieve customer
	 * information from Order. customer name = Kellie A. Sanborn
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_innerjoin_Mx1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			Join<Order, Customer> c = order.join("customer");
			// note: uses named parameter, not positional one
			cquery.where(cbuilder.equal(c.get("name"), cbuilder.parameter(String.class, "one")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("one", "Kellie A. Sanborn");
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_innerjoin_Mx1 failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_Mx1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781; PERSISTENCE:SPEC:654
	 * 
	 * @test_Strategy: Retrieve customer information from Order.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_fetchjoin_Mx1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.equal(order.get("customer").get("home").get("city"), "Lawrence")).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 8 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_fetchjoin_Mx1 failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_Mx1_1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781
	 * 
	 * @test_Strategy: Retrieve customer information from Order.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_fetchjoin_Mx1_1() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.like(order.get("customer").<String>get("name"), "%Caruso")).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_fetchjoin_Mx1_1 failed");
		}
	}

	/*
	 * @testName: test_notBetweenArithmetic
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349
	 * 
	 * @test_Strategy: Execute a query containing using the operator BETWEEN and NOT
	 * BETWEEN. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_notBetweenArithmetic() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.not(cbuilder.between(order.<Double>get("totalPrice"), 1000D, 1200D)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[15];
			expectedPKs[0] = "2";
			expectedPKs[1] = "4";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";
			expectedPKs[4] = "9";
			expectedPKs[5] = "10";
			expectedPKs[6] = "11";
			expectedPKs[7] = "12";
			expectedPKs[8] = "13";
			expectedPKs[9] = "15";
			expectedPKs[10] = "16";
			expectedPKs[11] = "17";
			expectedPKs[12] = "18";
			expectedPKs[13] = "19";
			expectedPKs[14] = "20";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_notBetweenArithmetic failed");
		}
	}

	/*
	 * @testName: test_ANDconditionTT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424;
	 * 
	 * @test_Strategy: Both the conditions in the WHERE Clause are True and hence
	 * the result is also TRUE Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ANDconditionTT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get("customer").get("name"), "Karen R. Tegan"),
					cbuilder.gt(order.<Double>get("totalPrice"), 500)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "6";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ANDconditionTT failed");
		}
	}

	/*
	 * @testName: test_ANDconditionTF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is True and Second is False and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ANDconditionTF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get("customer").get("name"), "Karen R. Tegan"),
					cbuilder.gt(order.<Double>get("totalPrice"), 10000)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ANDconditionTF failed");
		}
	}

	/*
	 * @testName: test_ANDconditionFT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is FALSE and Second is TRUE and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ANDconditionFT() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.equal(order.get("customer").get("id"), "1001"),
					cbuilder.lt(order.<Double>get("totalPrice"), 1000)).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ANDconditionFT failed");
		}
	}

	/*
	 * @testName: test_ANDconditionFF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:424
	 * 
	 * @test_Strategy: First condition is FALSE and Second is FALSE and hence the
	 * result is also False
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ANDconditionFF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.and(cbuilder.equal(order.get("customer").get("id"), "1001"),
					cbuilder.gt(order.<Double>get("totalPrice"), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ANDconditionFF failed");
		}
	}

	/*
	 * @testName: test_ORconditionTT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is TRUE OR Second is TRUE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ORconditionTT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get("customer").get("name"), "Karen R. Tegan"),
					cbuilder.gt(order.<Double>get("totalPrice"), 5000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "6";
			expectedPKs[1] = "11";
			expectedPKs[2] = "16";

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

		if (!pass) {
			throw new Exception("test_ORconditionTT failed");
		}
	}

	/*
	 * @testName: test_ORconditionTF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is TRUE OR Second is FALSE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ORconditionTF() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get("customer").get("name"), "Karen R. Tegan"),
					cbuilder.gt(order.<Double>get("totalPrice"), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "6";

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

		if (!pass) {
			throw new Exception("test_ORconditionTF failed");
		}
	}

	/*
	 * @testName: test_ORconditionFT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is FALSE OR Second is TRUE and hence the
	 * result is also TRUE
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ORconditionFT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get("customer").get("id"), "1001"),
					cbuilder.lt(order.<Double>get("totalPrice"), 1000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "9";
			expectedPKs[1] = "10";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "15";
			expectedPKs[5] = "19";
			expectedPKs[6] = "20";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 7 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ORconditionFT failed");
		}
	}

	/*
	 * @testName: test_ORconditionFF
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: First condition is FALSE OR Second is FALSE and hence the
	 * result is also FALSE
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ORconditionFF() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.or(cbuilder.equal(order.get("customer").get("id"), "1001"),
					cbuilder.gt(order.<Double>get("totalPrice"), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ORconditionFF failed");
		}
	}

	/*
	 * @testName: test_groupByWhereClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:808
	 * 
	 * @test_Strategy: Test for Group By within a WHERE clause
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_groupByWhereClause() throws Exception {
		boolean pass = false;
		final String[] expectedCusts = new String[] { "Jonathan K. Smith", "Kellie A. Sanborn", "Robert E. Bissett" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			cquery.where(cbuilder.between(o.<Double>get("totalPrice"), 90D, 160D)).groupBy(customer.get("name"))
					.select(customer.<String>get("name"));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			List<String> result = tquery.getResultList();

			String[] output = (String[]) (result.toArray(new String[result.size()]));

			Arrays.sort(output);
			pass = Arrays.equals(expectedCusts, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 3 Customers : "
						+ "Jonathan K. Smith, Kellie A. Sanborn and Robert E. Bissett. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.TRACE, " Customer: " + s);
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_groupByWhereClause failed");
		}
	}

	/*
	 * @testName: test_ABSHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.5
	 * 
	 * @test_Strategy: Test for ABS expression in the Having Clause
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_ABSHavingClause() throws Exception {
		boolean pass = false;
		Object result;
		final Double expectedPrice = 10191.90D;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(cbuilder.sum(order.<Double>get("totalPrice"))).groupBy(order.get("totalPrice"))
					.having(cbuilder.equal(cbuilder.abs(order.<Double>get("totalPrice")),
							cbuilder.parameter(Double.class, "doubleValue")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 5095.95D);
			result = (Double) tquery.getSingleResult();

			if (expectedPrice.equals(result)) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Expected results received");
			} else {
				logger.log(Logger.Level.ERROR, "test_ABSHavingClause:  Did not get expected results." + "Expected:"
						+ expectedPrice + ", got: " + result);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_ABSHavingClause failed");
		}
	}

	/*
	 * @testName: test_SQRTWhereClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.6
	 * 
	 * @test_Strategy: Test for SQRT expression in the WHERE Clause
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_SQRTWhereClause() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "SQRT: Executing Query");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.where(cbuilder.gt(cbuilder.sqrt(order.<Double>get("totalPrice")),
					cbuilder.parameter(Double.class, "doubleValue"))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 70D);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "11";
			expectedPKs[1] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_SQRTWhereClause:  Did not get expected results."
						+ "  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_SQRTWhereClause failed");
		}

	}

	/*
	 * @testName: test_subquery_exists_01
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792
	 * 
	 * @test_Strategy: Test NOT EXISTS in the Where Clause for a correlated query.
	 * Select the customers without orders.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_exists_01() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<Order> sq = cquery.subquery(Order.class);
			// correlate subquery root to root of main query:
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Order> sqo = sqc.join("orders");
			sq.select(sqo);
			cquery.where(cbuilder.not(cbuilder.exists(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "19";
			expectedPKs[1] = "20";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_exists_01:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_exists_01 failed");
		}
	}

	/*
	 * @testName: test_subquery_exists_02
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792
	 * 
	 * @test_Strategy: Test for EXISTS in the Where Clause for a correlated query.
	 * Select the customers with orders where total order > 1500.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_exists_02() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			// create correlated subquery
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Order> sqo = sqc.join("orders");
			sq.where(cbuilder.gt(sqo.<Double>get("totalPrice"), 1500D));
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "5";
			expectedPKs[1] = "10";
			expectedPKs[2] = "14";
			expectedPKs[3] = "15";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_exists_02:  Did not get expected results. "
						+ " Expected 4 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_exists_02 failed");
		}
	}

	/*
	 * @testName: test_subquery_like
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792;
	 * PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801; PERSISTENCE:SPEC:802
	 * 
	 * @test_Strategy: Use LIKE expression in a sub query. Select the customers with
	 * name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_like() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);
			// create correlated subquery
			Subquery<Customer> sq = cquery.subquery(Customer.class);
			Root<Order> sqo = sq.correlate(order);
			Join<Order, Customer> sqc = sqo.join("customer");
			sq.where(cbuilder.like(sqc.<String>get("name"), "%Caruso")).select(sqc);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_like:  Did not get expected "
						+ " results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_like failed");
		}
	}

	/*
	 * @testName: test_subquery_between
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802
	 * 
	 * @test_Strategy: Use BETWEEN expression in a sub query. Select the customers
	 * whose orders total price is between 1000 and 2000.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_between() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute query for test_subquery_between");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Customer> sqc = sq.correlate(customer);
			Join<Customer, Order> sqo = sqc.join("orders");
			sq.where(cbuilder.between(sqo.<Double>get("totalPrice"), 1000D, 1200D));
			cquery.where(cbuilder.exists(sq));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "7";
			expectedPKs[3] = "8";
			expectedPKs[4] = "13";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_between:  Did not get expected "
						+ " results.  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_between failed");
		}

	}

	/*
	 * @testName: test_subquery_join
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:765
	 * 
	 * @test_Strategy: Use JOIN in a sub query. Select the customers whose orders
	 * have line items of quantity > 2.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_join() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.where(cbuilder.gt(sql.<Integer>get("quantity"), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "16";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_join:  Did not get expected results."
						+ "  Expected 5 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_join failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_GT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:766; PERSISTENCE:SPEC:793; PERSISTENCE:SPEC:799
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator ">".
	 * Select all customers where total price of orders is greater than ALL the
	 * values in the result set.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_GT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> l = sqo.join("lineItemsCollection");
			sq.where(cbuilder.gt(l.<Integer>get("quantity"), 3));
			sq.select(sqo.<Double>get("totalPrice"));
			cquery.select(customer);
			cquery.where(cbuilder.gt(o.<Double>get("totalPrice"), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "5";
			expectedPKs[1] = "10";
			expectedPKs[2] = "14";
			expectedPKs[3] = "15";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_GT:  Did not get expected results. "
						+ " Expected 4 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_ALL_GT failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_LT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<".
	 * Select all customers where total price of orders is less than ALL the values
	 * in the result set.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_LT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.where(cbuilder.gt(sql.<Integer>get("quantity"), 3));
			sq.select(sqo.<Double>get("totalPrice"));
			cquery.where(cbuilder.lt(o.<Double>get("totalPrice"), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_LT:  Did not get expected results."
						+ "  Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_LT failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_EQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "=".
	 * Select all customers where total price of orders is = ALL the values in the
	 * result set. The result set contains the min of total price of orders.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_EQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.<Double>get("totalPrice")));
			cquery.where(cbuilder.equal(o.get("totalPrice"), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_EQ:  Did not get expected results. "
						+ " Expected 1 reference, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_EQ failed");
		}

	}

	/*
	 * @testName: test_subquery_ALL_LTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<=".
	 * Select all customers where total price of orders is <= ALL the values in the
	 * result set. The result set contains the total price of orders where count of
	 * lineItems > 3.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_LTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.gt(sql.<Integer>get("quantity"), 3));
			cquery.where(cbuilder.le(o.<Double>get("totalPrice"), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "9";
			expectedPKs[1] = "12";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_LTEQ:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_LTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_GTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator ">=".
	 * Select all customers where total price of orders is >= ALL the values in the
	 * result set.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_GTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.ge(sql.<Integer>get("quantity"), 3));
			cquery.where(cbuilder.ge(o.<Double>get("totalPrice"), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "14";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_GTEQ:  Did not get expected results. "
						+ " Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_GTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ALL_NOTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ALL in a subquery with the relational operator "<>".
	 * Select all customers where total price of orders is <> ALL the values in the
	 * result set.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ALL_NOTEQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.<Double>get("totalPrice")));
			cquery.where(cbuilder.notEqual(o.get("totalPrice"), cbuilder.all(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

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
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ALL_NOTEQ:  Did not get expected results."
						+ "  Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ALL_NOTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_GT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator ">".
	 * Select all customers where total price of orders is greater than ANY of the
	 * values in the result. The result set contains the total price of orders where
	 * count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ANY_GT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.equal(sql.<Integer>get("quantity"), 3));
			cquery.where(cbuilder.gt(o.<Double>get("totalPrice"), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[16];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "3";
			expectedPKs[3] = "4";
			expectedPKs[4] = "5";
			expectedPKs[5] = "6";
			expectedPKs[6] = "7";
			expectedPKs[7] = "8";
			expectedPKs[8] = "10";
			expectedPKs[9] = "11";
			expectedPKs[10] = "13";
			expectedPKs[11] = "14";
			expectedPKs[12] = "15";
			expectedPKs[13] = "16";
			expectedPKs[14] = "17";
			expectedPKs[15] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_GT:  Did not get expected results. "
						+ "  Expected 16 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("test_subquery_ANY_GT failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_LT
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator "<".
	 * Select all customers where total price of orders is less than ANY of the
	 * values in the result set. The result set contains the total price of orders
	 * where count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ANY_LT() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.equal(sql.<Integer>get("quantity"), 3));
			cquery.where(cbuilder.lt(o.<Double>get("totalPrice"), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

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
			expectedPKs[9] = "11";
			expectedPKs[10] = "12";
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_LT:  Did not get expected results.  "
						+ "Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ANY_LT failed");
		}
	}

	/*
	 * @testName: test_subquery_ANY_EQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:797;
	 * PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for ANY in a subquery with the relational operator "=".
	 * Select all customers where total price of orders is = ANY the values in the
	 * result set. The result set contains the min and avg of total price of orders.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_ANY_EQ() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.max(sqo.<Double>get("totalPrice")));
			cquery.where(cbuilder.equal(o.get("totalPrice"), cbuilder.any(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "14";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_ANY_EQ:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_ANY_EQ failed");
		}
	}

	/*
	 * @testName: test_subquery_SOME_LTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
	 * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: SOME with less than or equal to The result set contains the
	 * total price of orders where count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_SOME_LTEQ() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.equal(sql.get("quantity"), 3));
			cquery.where(cbuilder.le(o.<Double>get("totalPrice"), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

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

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_SOME_LTEQ:  Did not get expected results. "
						+ " Expected 18 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception(" test_subquery_SOME_LTEQ failed");
		}
	}

	/*
	 * @testName: test_subquery_SOME_GTEQ
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:794; PERSISTENCE:SPEC:795;
	 * PERSISTENCE:SPEC:797; PERSISTENCE:SPEC:798
	 * 
	 * @test_Strategy: Test for SOME in a subquery with the relational operator
	 * ">=". Select all customers where total price of orders is >= SOME the values
	 * in the result set. The result set contains the total price of orders where
	 * count of lineItems = 3.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_SOME_GTEQ() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join("orders");
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join("lineItemsCollection");
			sq.select(sqo.<Double>get("totalPrice"));
			sq.where(cbuilder.equal(sql.get("quantity"), 3));
			cquery.where(cbuilder.ge(o.<Double>get("totalPrice"), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

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
			expectedPKs[11] = "13";
			expectedPKs[12] = "14";
			expectedPKs[13] = "15";
			expectedPKs[14] = "16";
			expectedPKs[15] = "17";
			expectedPKs[16] = "18";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_SOME_GTEQ:  Did not get expected results. "
						+ " Expected 17 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("test_subquery_SOME_GTEQ failed");
		}
	}
}
