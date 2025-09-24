/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Address_;
import ee.jakarta.tck.persistence.common.schema30.CreditCard;
import ee.jakarta.tck.persistence.common.schema30.CreditCard_;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.LineItem;
import ee.jakarta.tck.persistence.common.schema30.LineItem_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.Order_;
import ee.jakarta.tck.persistence.common.schema30.Product_;
import ee.jakarta.tck.persistence.common.schema30.UtilOrderData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SetAttribute;

public class Client2 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery2.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */
	/*
	 * @testName: queryTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:312; PERSISTENCE:SPEC:322;
	 * PERSISTENCE:SPEC:602; PERSISTENCE:SPEC:603; PERSISTENCE:JAVADOC:91;
	 * PERSISTENCE:SPEC:785; PERSISTENCE:SPEC:1686; PERSISTENCE:SPEC:1687;
	 * PERSISTENCE:SPEC:1688; PERSISTENCE:SPEC:1689; PERSISTENCE:SPEC:1691;
	 * PERSISTENCE:SPEC:1692; PERSISTENCE:SPEC:1692.1; PERSISTENCE:SPEC:1692.2;
	 * PERSISTENCE:SPEC:1692.3; PERSISTENCE:SPEC:1693; PERSISTENCE:SPEC:1693.1;
	 * PERSISTENCE:SPEC:1693.2; PERSISTENCE:SPEC:1693.3; PERSISTENCE:SPEC:1693.4;
	 * PERSISTENCE:SPEC:1695; PERSISTENCE:SPEC:1696;
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

			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.name),
					cbuilder.parameter(String.class, "name"))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("name", "Robert E. Bissett");
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest1 failed");
		}
	}

	/*
	 * @testName: queryTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:760;
	 * PERSISTENCE:SPEC:761; PERSISTENCE:JAVADOC:989
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
			Join<Customer, CreditCard> a = customer.join(Customer_.creditCards);
			cquery.where(cbuilder.equal(a.get(CreditCard_.type), cbuilder.parameter(String.class, "ccard")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("ccard", "AXP");
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.lt(order.get(Order_.totalPrice), 4500).not());
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.lt(order.get(Order_.totalPrice), 100)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
			cquery.where(
					cbuilder.or(
							cbuilder.and(cbuilder.lt(l.get(LineItem_.quantity), 2),
									cbuilder.lt(order.get(Order_.totalPrice),
											cbuilder.sum(cbuilder.sum(cbuilder.literal(3),
													cbuilder.prod(cbuilder.literal(54), 2)), -8))),
							cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Robert E. Bissett")));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest10 failed");
		}
	}

	/*
	 * @testName: queryTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:338;
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
			Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
			cquery.where(cbuilder.and(cbuilder.lt(l.get(LineItem_.quantity), 2),
					cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Robert E. Bissett")));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest11 failed");
		}
	}

	/*
	 * @testName: queryTest12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:349; PERSISTENCE:SPEC:348.3;
	 * PERSISTENCE:JAVADOC:744
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
			cquery.where(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D)).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D).not()).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.isFalse(order.get(Order_.creditCard).get(CreditCard_.approved)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(
					cbuilder.lt(cbuilder.parameter(Double.class, "dbl"), cbuilder.abs(order.get(Order_.totalPrice))));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("dbl", 1180D);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.TRACE, "Caught unexpected exception:", e);

		}
	}

	/*
	 * @testName: queryTest29
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363.1; PERSISTENCE:SPEC:365
	 * 
	 * @test_Strategy: Execute a query using the comparison operator MEMBER OF in a
	 * collection member expression. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest29() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "finding a line item from order 1");

			LineItem li = getEntityManager().find(LineItem.class, "1");
			logger.log(Logger.Level.TRACE, "LineItem found was:" + li.toString());

			logger.log(Logger.Level.TRACE, "find line items who are members of line item found");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);

			cquery.where(cbuilder.isMember(li, order.get(Order_.lineItemsCollection)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> actual = tquery.getResultList();

			if (TestUtil.traceflag) {
				for (Order o : actual) {
					logger.log(Logger.Level.TRACE, "actual:" + o.toString());
					for (LineItem li2 : o.getLineItemsCollection()) {
						logger.log(Logger.Level.TRACE, "   LineItems:" + li2.toString());

					}
				}
			}
			expectedPKs = new String[1];
			expectedPKs[0] = orderRef[0].getId();
			if (!checkEntityPK(actual, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest29 failed");
		}
	}

	/*
	 * @testName: queryTest30
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363; PERSISTENCE:SPEC:365
	 * 
	 * @test_Strategy: Execute a query using the comparison operator NOT MEMBER in a
	 * collection member expression. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest30() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "finding a line item from order 11");

			LineItem li = getEntityManager().find(LineItem.class, "11");
			logger.log(Logger.Level.TRACE, "LineItem found was:" + li.toString());

			logger.log(Logger.Level.TRACE,
					"find orders who have line items that are not members of the line item found");
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);

			cquery.where(cbuilder.isNotMember(li, order.get(Order_.lineItemsCollection)));
			cquery.select(order);
			cquery.distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> actual = tquery.getResultList();

			Collections.sort(actual);

			expectedPKs = new String[19];
			expectedPKs[0] = orderRef[0].getId();
			expectedPKs[1] = orderRef[1].getId();
			expectedPKs[2] = orderRef[2].getId();
			expectedPKs[3] = orderRef[4].getId();
			expectedPKs[4] = orderRef[5].getId();
			expectedPKs[5] = orderRef[6].getId();
			expectedPKs[6] = orderRef[7].getId();
			expectedPKs[7] = orderRef[8].getId();
			expectedPKs[8] = orderRef[9].getId();
			expectedPKs[9] = orderRef[10].getId();
			expectedPKs[10] = orderRef[11].getId();
			expectedPKs[11] = orderRef[12].getId();
			expectedPKs[12] = orderRef[13].getId();
			expectedPKs[13] = orderRef[14].getId();
			expectedPKs[14] = orderRef[15].getId();
			expectedPKs[15] = orderRef[16].getId();
			expectedPKs[16] = orderRef[17].getId();
			expectedPKs[17] = orderRef[18].getId();
			expectedPKs[18] = orderRef[19].getId();

			if (!checkEntityPK(actual, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected:" + expectedPKs.length
						+ " references, got: " + actual.size());
				if (TestUtil.traceflag) {
					for (Order o : actual) {
						logger.log(Logger.Level.TRACE, "actual:" + o.toString());
						for (LineItem li2 : o.getLineItemsCollection()) {
							logger.log(Logger.Level.TRACE, "   LineItems:" + li2.toString());
						}
					}
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest30 failed");
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
			cquery.where(cbuilder.isMember(l, order.get(Order_.lineItemsCollection)));
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest32 failed");
		}
	}

	/*
	 * @testName: queryTest33
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:352.1
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
					order.get(Order_.lineItemsCollection)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("liDvc", liDvc);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest33 failed");
		}
	}

	/*
	 * @testName: queryTest34
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363.1; PERSISTENCE:JAVADOC:783
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
			cquery.where(cbuilder.isMember(order.get(Order_.sampleLineItem), order.get(Order_.lineItemsCollection)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Order, LineItem> l = order.join(Order_.lineItemsCollection);
			cquery.where(l.get(LineItem_.quantity).in(1, 5).not());
			cquery.select(order).distinct(true);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find customers with specific credit card balance");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, CreditCard> b = customer.join(Customer_.creditCards);
			cquery.where(
					cbuilder.equal(cbuilder.sqrt(b.get(CreditCard_.balance)), cbuilder.parameter(Double.class, "dbl")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("dbl", 50D);
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
			cquery.where(cbuilder.isNotNull(order.get(Order_.customer).get(Customer_.name)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.select(order.get(Order_.creditCard).get(CreditCard_.balance)).distinct(true);
			cquery.orderBy(cbuilder.asc(order.get(Order_.creditCard).get(CreditCard_.balance)));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			Collection<Double> olist = tquery.getResultList();

			Double[] result = (olist.toArray(new Double[olist.size()]));

			if (TestUtil.traceflag) {
				for (Double d : olist) {
					logger.log(Logger.Level.TRACE, "query results returned:  " + d);
				}
			}
			logger.log(Logger.Level.TRACE, "Compare expected results to query results");
			pass = Arrays.equals(expectedBalances, result);

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
		String[] expectedPKs = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
				"17", "18" };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with an Order");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.join(Customer_.orders);
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
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
			throw new Exception("queryTest60 failed");
		}
	}

	/*
	 *
	 * @testName: queryTest62
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.8;PERSISTENCE:JAVADOC:1027;
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
			From<Customer, Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.ge(cbuilder.size(customer.get(Customer_.orders)), 2));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.gt(cbuilder.size(customer.get(Customer_.orders)), 100));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Robert E. Bissett"))
					.select(cbuilder.least(order.get(Order_.id)));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			s2 = tquery.getSingleResult();

			if (s2.equals(s1)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest65 returned " + s2 + "expected: " + s1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.select(cbuilder.max(l.get(LineItem_.quantity)));
			TypedQuery<Integer> tquery = getEntityManager().createQuery(cquery);
			i2 = tquery.getSingleResult();

			if (i2.equals(i1)) {
				logger.log(Logger.Level.TRACE, "Successfully returned expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest66 returned:" + i2 + "expected: " + i1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
		final Double d1 = 1487.29D;
		final Double d2 = 1487.30D;
		Double d3;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			logger.log(Logger.Level.TRACE, "find AVERAGE price of all orders");
			CriteriaQuery<Double> cquery = cbuilder.createQuery(Double.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(cbuilder.avg(order.get(Order_.totalPrice)));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			d3 = tquery.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "queryTest67 returned expected results: " + d1);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "queryTest67 returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}
		if (!pass) {
			throw new Exception("queryTest67 failed");
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
			Join<Order, Customer> c = order.join(Order_.customer, JoinType.LEFT);
			// criteria queries don't support positional parameters, using "one"
			// as parameter name instead
			cquery.where(cbuilder.equal(c.get(Customer_.name), cbuilder.parameter(String.class, "one")));
			cquery.multiselect(order.get(Order_.id), c.get(Customer_.id));
			cquery.orderBy(cbuilder.asc(order.get(Order_.id)));
			TypedQuery tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("one", "Kellie A. Sanborn");
			tquery.setMaxResults(orderRef.length);
			List<Tuple> q = tquery.getResultList();

			if (q.size() != 2) {
				logger.log(Logger.Level.TRACE,
						"test_leftouterjoin_Mx1:  Did not get expected results. " + "Expected 2,  got: " + q.size());
			} else {

				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				// each element of the list q should be a size-2 array
				for (int i = 0; i < q.size(); i++) {
					pass1 = true;
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
						pass2 = false;
						break;
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cc.join(CreditCard_.customer);
			cquery.select(cc.get(CreditCard_.type)).groupBy(cc.get(CreditCard_.type));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(creditCard.length);
			List<String> result = tquery.getResultList();

			String[] output = result.toArray(new String[result.size()]);
			Arrays.sort(output);

			pass = Arrays.equals(expectedTypes, output);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 3 Credit Card Types: "
						+ "AXP, MCARD, VISA. Received: " + result.size());
				for (String s : result) {
					logger.log(Logger.Level.TRACE, " Credit Card Type: " + s);
				}

			}
			// verify this
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, CreditCard> cc = customer.join(Customer_.creditCards);
			cquery.where(cbuilder.equal(cc.get(CreditCard_.type), "VISA")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			Join<Order, Customer> c = order.join(Order_.customer);
			// note: uses named parameter, not positional one
			cquery.where(cbuilder.equal(c.get(Customer_.name), cbuilder.parameter(String.class, "one")));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("one", "Kellie A. Sanborn");
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "16";

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
			throw new Exception("test_innerjoin_Mx1 failed");
		}
	}

	/*
	 * @testName: fetchFetchPluralAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:967
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchPluralAttributeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.orders2);

			SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
			PluralAttribute pa = o.getModel();

			Fetch f1 = f.fetch(pa);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.orders2.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.orders2.getName() + ", actual:" + attr.getName());
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
			throw new Exception("fetchFetchPluralAttributeTest failed");
		}
	}

	/*
	 * @testName: fetchFetchPluralAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:968
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchPluralAttributeJoinTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);

			Root<Customer> customer = cquery.from(Customer.class);
			Fetch f = customer.fetch(Customer_.orders2);

			SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
			PluralAttribute pa = o.getModel();

			Fetch f1 = f.fetch(pa, JoinType.INNER);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals(Customer_.orders2.getName())) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.orders2.getName() + ", actual:" + attr.getName());
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
			throw new Exception("fetchFetchPluralAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fetchFetchStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:969
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchStringTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			Fetch f = customer.fetch("orders");
			Fetch f1 = f.fetch("creditCard");
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals("creditCard")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected attribute:creditCard, actual:" + attr.getName());
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
			throw new Exception("fetchFetchStringTest failed");
		}
	}

	/*
	 * @testName: fetchFetchStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:971; PERSISTENCE:SPEC:1698;
	 * PERSISTENCE:SPEC:1786; PERSISTENCE:SPEC:1786.2;
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchStringJoinTypeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			Fetch f = customer.fetch("orders");
			Fetch f1 = f.fetch("creditCard", JoinType.INNER);
			Attribute attr = f1.getAttribute();
			if (attr.getName().equals("creditCard")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + attr.getName());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected attribute:" + Customer_.orders.getName() + ", actual:" + attr.getName());
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
			throw new Exception("fetchFetchStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: fetchFetchStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:970
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			Fetch f = customer.fetch("orders");
			try {
				Fetch f1 = f.fetch("doesnotexist");
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchFetchStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: fetchFetchStringJoinTypeIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:972
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchFetchStringJoinTypeIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);

			Fetch f = customer.fetch("orders");
			try {
				Fetch f1 = f.fetch("doesnotexist", JoinType.INNER);
				logger.log(Logger.Level.ERROR, "Did not throw IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchFetchStringJoinTypeIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: fetchPluralAttribute1xMTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:976; PERSISTENCE:JAVADOC:1019;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchPluralAttribute1xMTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchPluralAttribute1xMTest failed");
		}
	}

	/*
	 * @testName: fetchPluralAttributeJoinType1xMTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1020;
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void fetchPluralAttributeJoinType1xMTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			From<Customer, Customer> customer = cquery.from(Customer.class);
			customer.fetch(Customer_.orders, JoinType.INNER);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass) {
			throw new Exception("fetchPluralAttributeJoinType1xMTest failed");
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
			order.fetch(Order_.customer, JoinType.LEFT);
			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.home).get(Address_.city), "Lawrence"))
					.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "15";
			expectedPKs[1] = "16";

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
			order.fetch(Order_.customer, JoinType.LEFT);
			cquery.where(cbuilder.like(order.get(Order_.customer).get(Customer_.name), "%Caruso")).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

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
			cquery.where(cbuilder.not(cbuilder.between(order.get(Order_.totalPrice), 1000D, 1200D)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 500)));
			cquery.select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000)));
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.lt(order.get(Order_.totalPrice), 1000)).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.and(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 5000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "6";
			expectedPKs[1] = "11";
			expectedPKs[2] = "16";

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
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.name), "Karen R. Tegan"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "6";

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
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.lt(order.get(Order_.totalPrice), 1000))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			cquery.where(cbuilder.or(cbuilder.equal(order.get(Order_.customer).get(Customer_.id), "1001"),
					cbuilder.gt(order.get(Order_.totalPrice), 10000))).select(order);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.where(cbuilder.between(o.get(Order_.totalPrice), 90D, 160D)).groupBy(customer.get(Customer_.name))
					.select(customer.get(Customer_.name));
			TypedQuery<String> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.select(cbuilder.sum(order.get(Order_.totalPrice))).groupBy(order.get(Order_.totalPrice))
					.having(cbuilder.equal(cbuilder.abs(order.get(Order_.totalPrice)),
							cbuilder.parameter(Double.class, "doubleValue")));
			TypedQuery<Double> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 5095.95D);
			result = tquery.getSingleResult();

			if (expectedPrice.equals(result)) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Expected results received");
			} else {
				logger.log(Logger.Level.ERROR, "test_ABSHavingClause:  Did not get expected results."
						+ "Expected 10190, got: " + (Double) result);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			cquery.where(cbuilder.gt(cbuilder.sqrt(order.get(Order_.totalPrice)),
					cbuilder.parameter(Double.class, "doubleValue"))).select(order);
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("doubleValue", 70D);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_SQRTWhereClause failed");
		}

	}

	/*
	 * @testName: subQueryGetSelectionGetParentTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1180; PERSISTENCE:JAVADOC:1181
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryGetSelectionGetParentTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			AbstractQuery<?> parent = sq.getParent();
			if (parent != null) {
				logger.log(Logger.Level.TRACE, "Received non-null parent query");
				if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"getParent() did not return a query of type CriteriaQuery or Subquery:" + parent);
				}
			} else {
				logger.log(Logger.Level.ERROR, "getParent() returned null");

			}

			Expression selExpression1 = order2.get(Order_.id);
			sq.select(selExpression1);
			Expression selExpression2 = sq.getSelection();
			if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
				logger.log(Logger.Level.TRACE, "Received expected selection expression" + selExpression1.getJavaType());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Receive unexpected expression - Expected:"
						+ selExpression1.getJavaType() + ", actual" + selExpression2.getJavaType());

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("subQueryGetSelectionGetParentTest failed");
		}
	}

	/*
	 * @testName: subQueryHavingExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1184; PERSISTENCE:JAVADOC:1180;
	 * PERSISTENCE:JAVADOC:1200; PERSISTENCE:JAVADOC:1193;
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryHavingExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o.ID FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID < '2' ) ) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			AbstractQuery<?> parent = sq.getParent();
			if (parent != null) {
				logger.log(Logger.Level.TRACE, "Received non-null parent query");
				if (parent instanceof CriteriaQuery || parent instanceof Subquery) {
					pass1 = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"getParent() did not return a query of type CriteriaQuery or Subquery:" + parent);
				}
			} else {
				logger.log(Logger.Level.ERROR, "SubQuery.getParent returned null");
			}

			Expression selExpression1 = order2.get(Order_.id);
			sq.select(selExpression1);
			Expression selExpression2 = sq.getSelection();
			if (selExpression1.getJavaType().equals(selExpression2.getJavaType())) {
				logger.log(Logger.Level.TRACE, "Received expected selection expression");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "SubQuery.getParent returned null");

			}

			sq.groupBy(order2.get(Order_.id));
			Predicate pred = sq.getGroupRestriction();
			if (pred == null) {
				logger.log(Logger.Level.TRACE,
						"Received null result from subquery.getGroupRestriction() when no restrictions exist");
				Expression exp = cbuilder.lessThan(order2.get(Order_.id), "2");
				sq.having(exp);

				// this having overrides the previous
				Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id), "1");
				sq.having(exp2);
				pred = sq.getGroupRestriction();
				if (pred != null) {
					logger.log(Logger.Level.TRACE,
							"Received non-null result from subquery.getGroupRestriction() when restrictions do exist");

					cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
					TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
					List<Order> result = tquery.getResultList();
					if (TestUtil.traceflag) {
						for (Order o : result) {
							logger.log(Logger.Level.TRACE, "id:" + o.getId());
						}
					}

					String[] expectedPKs = new String[1];
					expectedPKs[0] = "1";

					if (!checkEntityPK(result, expectedPKs)) {
						logger.log(Logger.Level.ERROR, "Did not get expected results.");

					} else {
						logger.log(Logger.Level.TRACE, "Expected results received");
						pass3 = true;
					}
				} else {
					logger.log(Logger.Level.ERROR,
							"Received null result from subquery.getGroupRestriction() when restrictions do exist");

				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected null result from subquery.getGroupRestriction() when no restrictions exist:"
								+ pred.toString());

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2 | !pass3) {
			throw new Exception("subQueryHavingExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryHavingPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1185; PERSISTENCE:JAVADOC:1201;
	 * 
	 * @test_Strategy: Test SubQuery with a Having statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryHavingPredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 GROUP BY o2.ID HAVING (o2.ID > '1' AND o2.ID < '2' ) ) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));
			sq.groupBy(order2.get(Order_.id));
			Predicate[] predArray1 = { cbuilder.greaterThan(order2.get(Order_.id), "3"),
					cbuilder.lessThan(order2.get(Order_.id), "4") };
			sq.having(predArray1);

			// this having overrides the previous
			Predicate[] predArray2 = { cbuilder.greaterThan(order2.get(Order_.id), "1"),
					cbuilder.lessThan(order2.get(Order_.id), "2") };
			sq.having(predArray2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();
			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
				}
			}
			String[] expectedPKs = new String[10];
			expectedPKs[0] = "10";
			expectedPKs[1] = "11";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "16";
			expectedPKs[7] = "17";
			expectedPKs[8] = "18";
			expectedPKs[9] = "19";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryHavingPredicateArrayTest failed");
		}
	}

	/*
	 * @testName: subQuerySelectExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1186
	 * 
	 * @test_Strategy: Test SubQuery with a Select (Expression) statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQuerySelectExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.TOTALPRICE IN (SELECT MAX(o2.TOTALPRICE) FROM ORDER_TABLE o2) "
			 * ).getResultList(); for (Order o : result) {
			 * logger.log(Logger.Level.TRACE,"id:"+o.getId()+", totalprice:"+o.getTotalPrice
			 * ()); }
			 */
			String[] expectedPKs = new String[2];
			expectedPKs[0] = "11";
			expectedPKs[1] = "16";

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> order2 = sq.from(Order.class);
			sq.select(cbuilder.min(order2.get(Order_.totalPrice)));
			// this select overrides the previous
			sq.select(cbuilder.max(order2.get(Order_.totalPrice)));
			cquery.where(cbuilder.in(order.get(Order_.totalPrice)).value(sq));

			TypedQuery<Order> tq = getEntityManager().createQuery(cquery);

			List<Order> result = tq.getResultList();
			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId() + ", totalprice:" + o.getTotalPrice());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQuerySelectExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryWhereExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1187; PERSISTENCE:JAVADOC:1204
	 * 
	 * @test_Strategy: Test SubQuery with a Where Expression statement. SELECT o
	 * FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE
	 * (o2.ID <= 1 )
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryWhereExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);

			logger.log(Logger.Level.TRACE, "Received null from subquery.getRestriction() when no restrictions exist");

			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));

			Expression exp1 = cbuilder.lessThan(order2.get(Order_.id), "2");
			sq.where(exp1);
			Expression exp2 = cbuilder.lessThanOrEqualTo(order2.get(Order_.id), "1");
			sq.where(exp2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			String[] expectedPKs = new String[1];
			expectedPKs[0] = "1";

			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryWhereExpressionTest failed");
		}
	}

	/*
	 * @testName: subQueryWherePredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1188; PERSISTENCE:JAVADOC:1205;
	 * 
	 * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryWherePredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();

			/*
			 * List q = getEntityManager().
			 * createNativeQuery("SELECT o FROM ORDER_TABLE o WHERE o.ID IN (SELECT o2.ID FROM ORDER_TABLE o2 WHERE ((o2.ID > 1) AND (o2.ID < 2)) "
			 * ).getResultList(); for (Object obj : q) { String o = (String) obj;
			 * logger.log(Logger.Level.TRACE,"id:" + o); }
			 */

			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);

			Subquery<String> sq = cquery.subquery(String.class);
			Root<Order> order2 = sq.from(Order.class);

			sq.select(order2.get(Order_.id));

			Predicate[] predArray1 = { cbuilder.greaterThan(order2.get(Order_.id), "3"),
					cbuilder.lessThan(order2.get(Order_.id), "4") };
			sq.where(predArray1);

			// this having overrides the previous
			Predicate[] predArray2 = { cbuilder.greaterThan(order2.get(Order_.id), "1"),
					cbuilder.lessThan(order2.get(Order_.id), "2") };
			sq.where(predArray2);

			cquery.where(cbuilder.in(order.get(Order_.id)).value(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			String[] expectedPKs = new String[10];
			expectedPKs[0] = "10";
			expectedPKs[1] = "11";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			expectedPKs[4] = "14";
			expectedPKs[5] = "15";
			expectedPKs[6] = "16";
			expectedPKs[7] = "17";
			expectedPKs[8] = "18";
			expectedPKs[9] = "19";

			if (TestUtil.traceflag) {
				for (Order o : result) {
					logger.log(Logger.Level.TRACE, "id:" + o.getId());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subQueryWherePredicateArrayTest failed");
		}
	}

	/*
	 * @testName: subQueryDistinctTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1178; PERSISTENCE:JAVADOC:1195;
	 * PERSISTENCE:JAVADOC:1196; PERSISTENCE:JAVADOC:1202;
	 * 
	 * @test_Strategy: Test SubQuery with a Where Predicate[] statement.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subQueryDistinctTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass4 = false;
		boolean pass5 = true;
		boolean pass6 = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			/*
			 * List<Object[]> q = getEntityManager().
			 * createQuery("SELECT l.id, l.product.id, l.product.price FROM LineItem l WHERE l.product.id = (SELECT DISTINCT l.product.id FROM LineItem l WHERE l.product.price > 5000 )"
			 * ).getResultList(); for (Object[] o : q) {
			 * logger.log(Logger.Level.TRACE,"lineitem id:" + o[0] + ", prod id:" + o[1]+
			 * ", prod price:" + o[2]); }
			 */
			CriteriaQuery<LineItem> cquery = cbuilder.createQuery(LineItem.class);
			Root<LineItem> lineItem = cquery.from(LineItem.class);
			cquery.select(lineItem);

			Subquery<String> sq = cquery.subquery(String.class);
			String name = sq.getResultType().getName();
			if (name.equals("java.lang.String")) {
				logger.log(Logger.Level.TRACE, "Received expected type:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected type: java.lang.String, actual: " + name);

			}

			Root<LineItem> lineItem2 = sq.from(LineItem.class);
			sq.where(cbuilder.greaterThan(lineItem2.get(LineItem_.product).get(Product_.price), 5000D))
					.select(lineItem2.get(LineItem_.product).get(Product_.id));
			sq.distinct(true);
			boolean b = sq.isDistinct();
			if (b) {
				logger.log(Logger.Level.TRACE, "Received expected isDistinct result:" + b);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected isDistinct: true, actual:" + b);

			}

			cquery.where(cbuilder.equal(lineItem.get(LineItem_.product).get(Product_.id), sq));

			Set<Root<?>> s = sq.getRoots();
			if (s.size() == 1) {
				logger.log(Logger.Level.TRACE, "Received expected size:" + s.size());
				for (Root r : s) {
					pass4 = true;
					name = r.getJavaType().getName();
					if (name.equals("ee.jakarta.tck.persistence.common.schema30.LineItem")) {
						logger.log(Logger.Level.TRACE, "Received expected root:" + name);
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected Root: ee.jakarta.tck.persistence.common.schema30.LineItem, actual:" + name);
						pass5 = false;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected size: 1, actual:" + s.size());

			}

			TypedQuery<LineItem> tquery = getEntityManager().createQuery(cquery);
			List<LineItem> result = tquery.getResultList();

			String[] expectedPKs = new String[2];
			expectedPKs[0] = "33";
			expectedPKs[1] = "44";

			if (TestUtil.traceflag) {
				for (LineItem l : result) {
					logger.log(Logger.Level.TRACE, " lineitem id:" + l.getId() + ", prod id:" + l.getProduct().getId()
							+ ", prod price:" + l.getProduct().getPrice());
				}
			}

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.");

			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass6 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2 || !pass4 || !pass5 || !pass6) {
			throw new Exception("subQueryDistinctTest failed");
		}
	}

	/*
	 * @testName: test_subquery_exists_02
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;
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
			Join<Customer, Order> sqo = sqc.join(Customer_.orders);
			sq.where(cbuilder.gt(sqo.get(Order_.totalPrice), 1500D));
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Order, Customer> sqc = sqo.join(Order_.customer);
			sq.where(cbuilder.like(sqc.get(Customer_.name), "%Caruso")).select(sqc);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(orderRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> sqo = sqc.join(Customer_.orders);
			sq.where(cbuilder.between(sqo.get(Order_.totalPrice), 1000D, 1200D));
			cquery.where(cbuilder.exists(sq));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" test_subquery_between failed");
		}

	}

	/*
	 * @testName: test_subquery_join
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:800;PERSISTENCE:SPEC:801;
	 * PERSISTENCE:SPEC:802; PERSISTENCE:SPEC:765; PERSISTENCE:JAVADOC:1173;
	 * PERSISTENCE:JAVADOC:1038;
	 * 
	 * @test_Strategy: Use JOIN in a sub query. Select the customers whose orders
	 * have line items of quantity > 2.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_join() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			From f = sqo.getCorrelationParent();
			String name = f.getModel().getBindableJavaType().getSimpleName();
			if (name.equals("Order")) {
				logger.log(Logger.Level.TRACE, "Received expected CorrelationParent:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected CorrelationParent: Order, actual:" + name);

			}
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception(" test_subquery_join failed");
		}
	}

	/*
	 * @testName: getCorrelationParentIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1039; PERSISTENCE:JAVADOC:1041;
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void getCorrelationParentIllegalStateExceptionTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Root<Order> order = sq.from(Order.class);
			Join<Order, LineItem> sql = order.join(Order_.lineItemsCollection);
			boolean b = sql.isCorrelated();
			if (!b) {
				logger.log(Logger.Level.TRACE, "Received expected result from isCorrelated(): false");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected false from isCorrelated(), actual:" + b);

			}
			try {
				From f = sql.getCorrelationParent();

				logger.log(Logger.Level.ERROR, "Did not throw IllegalStateException");
				logger.log(Logger.Level.ERROR, "getCorrelationParent() returned:" + f);
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass2 = true;
			} catch (Exception ex) {

				logger.log(Logger.Level.ERROR, "Received unexpected exception", ex);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("getCorrelationParentIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: subquerySetJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1170; PERSISTENCE:JAVADOC:1175;
	 * PERSISTENCE:JAVADOC:1137; PERSISTENCE:JAVADOC:1171;
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subquerySetJoinTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			SetJoin<Customer, Order> o = customer.join(Customer_.orders2);
			PluralAttribute pa = o.getModel();
			String paName = pa.getCollectionType().name();
			if (paName.equals("SET")) {
				logger.log(Logger.Level.TRACE, "Received expected PluralAttribute:" + paName);
				pass1 = true;
			} else {
				logger.log(Logger.Level.TRACE, "Expected PluralAttribute: SET, actual: " + paName);

			}

			SetAttribute sa = o.getModel();
			String saName = sa.getName();
			if (saName.equals("orders2")) {
				logger.log(Logger.Level.TRACE, "Received expected SetAttribute:" + saName);
			} else {
				logger.log(Logger.Level.TRACE, "Expected SetAttribute: orders2, actual: " + saName);

			}
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("subquerySetJoinTest failed");
		}
	}

	/*
	 * @testName: subqueryListJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1176; PERSISTENCE:JAVADOC:1138;
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryListJoinTest() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			ListJoin<Customer, Order> o = customer.join(Customer_.orders3);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subqueryListJoinTest failed");
		}
	}

	/*
	 * @testName: subqueryCollectionJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1174
	 * 
	 * @test_Strategy: Use JOIN in a sub query.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryCollectionJoinTest() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Join<Customer, Order> sqo = sq.correlate(o);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3)).select(sqo);
			cquery.select(customer);
			cquery.where(cbuilder.exists(sq));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("subqueryCollectionJoinTest failed");
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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> l = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(l.get(LineItem_.quantity), 3));
			sq.select(sqo.get(Order_.totalPrice));
			cquery.select(customer);
			cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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

			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
			sq.select(sqo.get(Order_.totalPrice));
			cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			cquery.select(customer);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.equal(o.get("totalPrice"), cbuilder.all(sq)));
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.gt(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.ge(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.min(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.notEqual(o.get(Order_.totalPrice), cbuilder.all(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.gt(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.lt(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			sq.select(cbuilder.max(sqo.get(Order_.totalPrice)));
			cquery.where(cbuilder.equal(o.get(Order_.totalPrice), cbuilder.any(sq))).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.le(o.get(Order_.totalPrice), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

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
			Join<Customer, Order> o = customer.join(Customer_.orders);
			Subquery<Double> sq = cquery.subquery(Double.class);
			Root<Order> sqo = sq.from(Order.class);
			Join<Order, LineItem> sql = sqo.join(Order_.lineItemsCollection);
			sq.select(sqo.get(Order_.totalPrice));
			sq.where(cbuilder.equal(sql.get(LineItem_.quantity), 3));
			cquery.where(cbuilder.ge(o.get(Order_.totalPrice), cbuilder.some(sq))).select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_subquery_SOME_GTEQ failed");
		}
	}

}
