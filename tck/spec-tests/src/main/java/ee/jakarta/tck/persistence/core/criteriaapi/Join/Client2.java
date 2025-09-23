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

package ee.jakarta.tck.persistence.core.criteriaapi.Join;

import java.lang.System.Logger;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.CreditCard;
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
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.PluralJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.metamodel.PluralAttribute;

public class Client2 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_join.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: joinStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1052; PERSISTENCE:JAVADOC:1040;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinStringTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = true;
		boolean pass4 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);

			Set sJoins = customer.getJoins();
			if (!sJoins.isEmpty()) {
				logger.log(Logger.Level.ERROR, "Expected getJoins() to return empty set instead got:");
				for (Iterator<Join<Order, ?>> i = sJoins.iterator(); i.hasNext();) {
					Join j = i.next();
					logger.log(Logger.Level.ERROR, "join:" + j.toString());
				}
			} else {
				logger.log(Logger.Level.TRACE, "getJoin() returned empty set as expected");
				pass1 = true;
			}

			Join<Customer, Order> order = customer.join(Customer_.orders);

			Set<Join<Customer, ?>> s = customer.getJoins();
			if (s.isEmpty()) {
				logger.log(Logger.Level.ERROR, "Expected getJoins() to return non empty set");
			} else {

				if (s.size() == 1) {
					logger.log(Logger.Level.TRACE, "getJoins returned:");
					for (Iterator<Join<Customer, ?>> i = s.iterator(); i.hasNext();) {
						pass2 = true;
						Join j = i.next();
						logger.log(Logger.Level.TRACE, "join:" + j.toString());
						String name = j.getAttribute().getName();
						if (name.equals("orders")) {
							logger.log(Logger.Level.TRACE, "Received expected attribute: orders");
						} else {
							logger.log(Logger.Level.ERROR, "Expected attribute: orders, actual:" + name);
							pass3 = false;
						}
					}
				} else {
					pass3 = false;
					logger.log(Logger.Level.ERROR, "Expected getJoins to return 1 join, actual:" + s.size());
					logger.log(Logger.Level.ERROR, "getJoins returned:");
					for (Iterator<Join<Customer, ?>> i = s.iterator(); i.hasNext();) {
						Join j = i.next();
						logger.log(Logger.Level.ERROR, "join:" + j.toString());
					}
				}

			}
			Join<Order, LineItem> lineItem = order.join("lineItemsCollection");

			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass4 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("joinStringTest failed");
		}
	}

	/*
	 * @testName: joinStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1054;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where (l.id
	 * = 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			Join<Order, LineItem> lineItem = order.join("lineItemsCollection", JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinSingularAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1042;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSingularAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			Join<Order, CreditCard> creditCard = order.join("creditCard");
			Expression e = cbuilder.literal("4");
			cquery.where(creditCard.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSingularAttributeTest failed");
		}
	}

	/*
	 * @testName: joinSingularAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1043; PERSISTENCE:SPEC:1698;
	 * PERSISTENCE:SPEC:1786; PERSISTENCE:SPEC:1786.1;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * SELECT c FROM Customer c INNER JOIN c.work o WHERE (o.id in (4))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSingularAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			Join<Order, CreditCard> creditCard = order.join("creditCard", JoinType.INNER);
			Expression e = cbuilder.literal("4");
			cquery.where(creditCard.get("id").in(e)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSingularAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinCollectionAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1044; PERSISTENCE:JAVADOC:729
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id = 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionAttributeTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection);
			PluralAttribute pa = lineItem.getModel();
			String name = pa.getName();
			if (name.equals("lineItemsCollection")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "getModel - Expected: lineItemsCollection, actual:" + name);
			}

			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinCollectionAttributeTest failed");
		}
	}

	/*
	 * @testName: joinCollectionAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1048;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where (l.id
	 * = 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsCollection, JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);

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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinCollectionStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1056; PERSISTENCE:JAVADOC:728;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id = 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.joinCollection("lineItemsCollection");
			String name = lineItem.getModel().getName();
			if (name.equals("lineItemsCollection")) {
				logger.log(Logger.Level.TRACE, "Received expected CollectionAttribute:" + name);
			} else {
				logger.log(Logger.Level.ERROR, "Expected CollectionAttribute:lineItems, actual:" + name);
			}
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionStringTest failed");
		}
	}

	/*
	 * @testName: joinCollectionStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1058;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o INNER JOIN o.lineItems l where (l.id
	 * = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinCollectionStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			CollectionJoin<Order, LineItem> lineItem = order.joinCollection("lineItemsCollection", JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinCollectionStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinSetAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1045
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders2 o JOIN o.lineItemsSet l where (l.id =
	 * 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			SetJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsSet);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetAttributeTest failed");
		}
	}

	/*
	 * @testName: joinSetAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1049
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders2 o INNER JOIN o.lineItemsSet l where
	 * (l.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			SetJoin<Order, LineItem> lineItem = order.join(Order_.lineItemsSet, JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinSetStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1068;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders2 o JOIN o.lineItemsSet l where (l.id =
	 * 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetStringTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet");
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetStringTest failed");
		}
	}

	/*
	 * @testName: joinSetStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1070;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders2 o INNER JOIN o.lineItemsSet l where
	 * (l.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinSetStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet", JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinSetStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinListAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1046; PERSISTENCE:JAVADOC:1076;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders3 o JOIN o.lineItemsList l where (l.id
	 * = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListAttributeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList");
			PluralAttribute pa = lineItem.getModel();
			String name = pa.getName();
			if (name.equals("lineItemsList")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + name);
			} else {
				logger.log(Logger.Level.ERROR, "getModel - Expected: lineItemsList, actual:" + name);
			}
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListAttributeTest failed");
		}
	}

	/*
	 * @testName: joinListAttributeJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1050;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders3 o INNER JOIN o.lineItemsList l where
	 * (l.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListAttributeJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders2);
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList", JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListAttributeJoinTypeTest failed");
		}
	}

	/*
	 * @testName: joinListStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1060; PERSISTENCE:JAVADOC:1074;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders3 o JOIN o.lineItemsList l where (l.id
	 * = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListStringTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.joinList("orders3");
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList");
			String name = lineItem.getModel().getName();
			if (name.equals("lineItemsList")) {
				logger.log(Logger.Level.TRACE, "Received expected ListAttribute:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected ListAttribute:lineItemsList, actual:" + name);
			}
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("joinListStringTest failed");
		}
	}

	/*
	 * @testName: joinListStringJoinTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1062;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders3 o INNER JOIN o.lineItemsList l where
	 * (l.id = 1)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinListStringJoinTypeTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.joinList("orders3");
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList", JoinType.INNER);
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
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
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("joinListStringJoinTypeTest failed");
		}
	}

	/*
	 * @testName: pluralJoinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1113; PERSISTENCE:JAVADOC:1114;
	 * PERSISTENCE:JAVADOC:1115; PERSISTENCE:JAVADOC:1112;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 *
	 * select c FROM Customer c JOIN c.orders o JOIN o.lineItems l where (l.id = 1)
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void pluralJoinTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Order> order = customer.join(Customer_.orders);
			PluralJoin lineItem = order.joinCollection("lineItemsCollection");
			String name = lineItem.getModel().getName();
			if (name.equals("lineItemsCollection")) {
				logger.log(Logger.Level.TRACE, "Received expected PluralAttribute:" + name);
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected PluralAttribute:lineItems, actual:" + name);
			}
			name = lineItem.getAttribute().getName();
			if (name.equals("lineItemsCollection")) {
				logger.log(Logger.Level.TRACE, "Received expected attribute:" + name);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected attribute name: lineItems, actual:" + name);
			}
			JoinType type = lineItem.getJoinType();
			if (type.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected JoinType:" + type);
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected JoinType : " + JoinType.INNER.toString() + ", actual:" + type);
			}
			From from = lineItem.getParent();
			String parent = from.getJavaType().getName();
			if (parent.equals("ee.jakarta.tck.persistence.common.schema30.Order")) {
				logger.log(Logger.Level.TRACE, "Received expected parent:" + parent);
				pass4 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected parent: ee.jakarta.tck.persistence.common.schema30.Order, actual:" + parent);
			}
			cquery.where(cbuilder.equal(lineItem.get("id"), "1")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "1";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass5 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("pluralJoinTest failed");
		}
	}

	/*
	 * @testName: pluralJoinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1723; PERSISTENCE:JAVADOC:1722;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void pluralJoinOnExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			PluralJoin order = customer.join(Customer_.orders);
			if (order.getOn() == null) {
				logger.log(Logger.Level.TRACE, "Received expected null from getOn()");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected null from getOn(), actual:" + order.toString());
			}

			Join join = order.on(cbuilder.equal(order.get("id"), "1"));
			Predicate pred = join.getOn();
			if (pred != null) {
				logger.log(Logger.Level.TRACE, "Received expected non-null from getOn()");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received unexpected null from getOn()");
			}
			cquery.select(cbuilder.count(join));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			Long actual = tquery.getSingleResult();
			if (actual == 1) {
				logger.log(Logger.Level.TRACE, "Received expected number: " + actual);
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 1, actual:" + actual);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("pluralJoinOnExpressionTest failed");
		}
	}

	/*
	 * @testName: pluralJoinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1724; PERSISTENCE:JAVADOC:1722;
	 * 
	 * @test_Strategy: This query is defined on a one-many relationship. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void pluralJoinOnPredicateArrayTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			PluralJoin order = customer.join(Customer_.orders);

			Predicate pred1 = cbuilder.equal(customer.get("id"), "1");
			Predicate pred2 = cbuilder.equal(customer.get("country").get("code"), "USA");
			Predicate[] pred = { pred1, pred2 };
			Join join = order.on(pred);

			Predicate pred3 = join.getOn();
			if (pred3.getExpressions().size() == 2) {
				logger.log(Logger.Level.TRACE, "Received expected number of predicates");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 2 predicates, actual:" + pred3.getExpressions().size());

			}

			cquery.select(cbuilder.count(join));
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			Long actual = tquery.getSingleResult();
			if (actual == 1) {
				logger.log(Logger.Level.TRACE, "Received expected number: " + actual);
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 1, actual:" + actual);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass1 || !pass2) {
			throw new Exception("pluralJoinOnPredicateArrayTest failed");
		}
	}

	/*
	 * @testName: collectionJoinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1668;
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where (l.QUANTITY > 5))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void collectionJoinOnExpressionTest() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			CollectionJoin<Order, LineItem> lineItem = order.joinCollection("lineItemsCollection", JoinType.INNER);
			Expression exp = cbuilder.gt(lineItem.<Number>get("quantity"), 5);
			lineItem.on(exp);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("collectionJoinOnExpressionTest failed");
	}

	/*
	 * @testName: collectionJoinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1669
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void collectionJoinOnPredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			CollectionJoin<Order, LineItem> lineItem = order.joinCollection("lineItemsCollection", JoinType.INNER);
			Predicate[] pred = { cbuilder.gt(lineItem.<Number>get("quantity"), 5),
					cbuilder.lt(lineItem.<Number>get("quantity"), 8) };
			lineItem.on(pred);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("collectionJoinOnPredicateArrayTest failed");
	}

	/*
	 * @testName: listJoinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1718
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where (l.QUANTITY > 5)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void listJoinOnExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList", JoinType.INNER);
			Expression exp = cbuilder.gt(lineItem.<Number>get("quantity"), 5);
			lineItem.on(exp);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("listJoinOnExpressionTest failed");
	}

	/*
	 * @testName: listJoinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1719
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void listJoinOnPredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			ListJoin<Order, LineItem> lineItem = order.joinList("lineItemsList", JoinType.INNER);
			Predicate[] pred = { cbuilder.gt(lineItem.<Number>get("quantity"), 5),
					cbuilder.lt(lineItem.<Number>get("quantity"), 8) };
			lineItem.on(pred);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("listJoinOnPredicateArrayTest failed");
	}

	/*
	 * @testName: setJoinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1725
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where (l.QUANTITY > 5)
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void setJoinOnExpressionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet", JoinType.INNER);
			Expression exp = cbuilder.gt(lineItem.<Number>get("quantity"), 5);
			lineItem.on(exp);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("setJoinOnExpressionTest failed");
	}

	/*
	 * @testName: setJoinOnPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1726
	 * 
	 * @test_Strategy: SELECT o FROM LineItem l INNER JOIN Order o ON (l.id = o.ID)
	 * where ((l.QUANTITY > 5) AND (l.QUANTITY < 8))
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void setJoinOnPredicateArrayTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		try {
			CriteriaQuery<Order> cquery = cbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			SetJoin<Order, LineItem> lineItem = order.joinSet("lineItemsSet", JoinType.INNER);
			Predicate[] pred = { cbuilder.gt(lineItem.<Number>get("quantity"), 5),
					cbuilder.lt(lineItem.<Number>get("quantity"), 8) };
			lineItem.on(pred);
			cquery.select(order);

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> oList = tquery.getResultList();

			String expectedPKs[] = new String[1];
			expectedPKs[0] = "12";

			if (!checkEntityPK(oList, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected: " + expectedPKs.length
						+ "  reference, got: " + oList.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("setJoinOnPredicateArrayTest failed");
	}

}
