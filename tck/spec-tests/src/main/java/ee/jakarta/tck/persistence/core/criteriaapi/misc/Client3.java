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
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilOrderData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;

public class Client3 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_misc3.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: compoundSelectionGetCompoundSelectionItemsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:731; PERSISTENCE:JAVADOC:733;
	 * PERSISTENCE:JAVADOC:773
	 *
	 * @test_Strategy:
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void compoundSelectionGetCompoundSelectionItemsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		getEntityTransaction().begin();
		Expression exp1 = cbuilder.literal("1");
		Expression exp2 = cbuilder.literal("2");
		CompoundSelection cs = cbuilder.tuple(exp1, exp2);
		boolean bActual = cs.isCompoundSelection();
		if (bActual == true) {
			List<Selection<?>> lSel = cs.getCompoundSelectionItems();
			if (lSel.size() == 2) {

				CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();

				Root<Order> order = cquery.from(Order.class);
				cquery.select(cs);
				cquery.where(cbuilder.equal(order.get("id"), "1"));

				TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
				Collection<Tuple> result = tquery.getResultList();
				if (result.size() == 1) {
					int i = 0;
					for (Tuple actual : result) {
						pass1 = true;
						logger.log(Logger.Level.TRACE, "first=" + actual.get(0) + ", second=" + actual.get(1));
						if (!actual.get(0).equals("1")) {
							logger.log(Logger.Level.ERROR, "Expected: 1, actual:" + actual.get(0));
							pass2 = false;
						}
						if (!actual.get(1).equals("2")) {
							logger.log(Logger.Level.ERROR, "Expected: 2, actual:" + actual.get(1));
							pass2 = false;
						}
						i++;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 1 tuple, actual:" + result.size());
					for (Tuple actual : result) {
						logger.log(Logger.Level.ERROR, "first=" + actual.get(0) + ", second=" + actual.get(1));
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 2 compound selection item, actual:" + lSel.size());
				for (Selection s : lSel) {
					logger.log(Logger.Level.ERROR, "selection:" + s.toString());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Expected isCompoundSelection() to return: true, actual:" + bActual);
		}
		getEntityTransaction().commit();

		if (!pass1 || !pass2) {
			throw new Exception("compoundSelectionGetCompoundSelectionItemsTest failed");
		}
	}

	/*
	 * @testName: selectionGetCompoundSelectionItemsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1165;
	 *
	 * @test_Strategy:
	 *
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void selectionGetCompoundSelectionItemsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		Expression exp1 = cbuilder.literal("1");
		Expression exp2 = cbuilder.literal("2");
		Selection sel = cbuilder.tuple(exp1, exp2);
		boolean bActual = sel.isCompoundSelection();
		if (bActual == true) {
			List<Selection<?>> lSel = sel.getCompoundSelectionItems();
			if (lSel.size() == 2) {

				CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();

				Root<Order> order = cquery.from(Order.class);
				cquery.select(sel);
				cquery.where(cbuilder.equal(order.get("id"), "1"));

				TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
				Collection<Tuple> result = tquery.getResultList();
				if (result.size() == 1) {
					for (Tuple actual : result) {
						pass1 = true;
						logger.log(Logger.Level.TRACE, "first=" + actual.get(0) + ", second=" + actual.get(1));
						if (!actual.get(0).equals("1")) {
							logger.log(Logger.Level.ERROR, "Expected: 1, actual:" + actual.get(0));
							pass2 = false;
						}
						if (!actual.get(1).equals("2")) {
							logger.log(Logger.Level.ERROR, "Expected: 2, actual:" + actual.get(1));
							pass2 = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected: 1 tuple, actual:" + result.size());
					for (Tuple actual : result) {
						logger.log(Logger.Level.ERROR, "first=" + actual.get(0) + ", second=" + actual.get(1));
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected: 2 compound selection item, actual:" + lSel.size());
				for (Selection s : lSel) {
					logger.log(Logger.Level.ERROR, "selection:" + s.toString());
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "Expected isCompoundSelection() to return: true, actual:" + bActual);
		}

		if (!pass1 || !pass2) {
			throw new Exception("selectionGetCompoundSelectionItemsTest failed");
		}
	}

	/*
	 * @testName: pathGetPluralAttributeTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1097;
	 * 
	 * @test_Strategy:
	 *
	 * SELECT c FROM Customer c WHERE ((SELECT COUNT(o.id) FROM Order o WHERE
	 * (o.customer.id = c.ID)) > 1)
	 * 
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void pathGetPluralAttributeTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[2];
		expected[0] = customerRef[3].getId();
		expected[1] = customerRef[13].getId();

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Path<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.gt(cbuilder.size(customer.get(Customer_.orders)), 1)).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> clist = tquery.getResultList();

			if (!checkEntityPK(clist, expected)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expected.length
						+ " references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);

		}

		if (!pass) {
			throw new Exception("pathGetPluralAttributeTest failed");
		}
	}

	/*
	 * @testName: subquery
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:801; PERSISTENCE:JAVADOC:1172;
	 *
	 * @test_Strategy: Use LIKE expression in a sub query. Select the customers with
	 * name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subquery() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);
			// create correlated subquery
			Subquery<Customer> sq = cquery.subquery(Customer.class);
			Root<Order> sqo = sq.correlate(order);
			Join<Order, Customer> sqc = sqo.join("customer");
			sq.where(qbuilder.like(sqc.<String>get("name"), "%Caruso")).select(sqc);
			cquery.where(qbuilder.exists(sq));
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
			logger.log(Logger.Level.ERROR, "Caught exception subquery: ", e);
		}

		if (!pass) {
			throw new Exception("subquery failed");
		}
	}

	/*
	 * @testName: subqueryGroupByExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1182; PERSISTENCE:JAVADOC:1192;
	 *
	 * @test_Strategy: Use LIKE expression in a sub query. Select the customers with
	 * name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryGroupByExpressionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;
		String expectedPKs[];

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);

			Subquery<String> subquery = cquery.subquery(String.class);
			List<Expression<?>> gList = subquery.getGroupList();
			if (gList.size() == 0) {
				logger.log(Logger.Level.TRACE,
						"Received expected empty list from getGroupList() when there is no groupBy expressions");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not received empty list from getGroupList() when there is no groupBy expressions");
				for (Expression e : gList) {
					logger.log(Logger.Level.ERROR, "Item:" + e.toString());
				}
			}
			Expression sel = subquery.getSelection();
			if (sel == null) {
				logger.log(Logger.Level.TRACE,
						"Received expected null from getSelection() when there is no selection specified");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not received null from getSelection() when there is no selection specified:"
								+ sel.toString());
			}
			Root<Customer> customer = subquery.from(Customer.class);
			EntityType<Customer> Customer_ = customer.getModel();
			subquery.select(customer.get(Customer_.getSingularAttribute("name", String.class)));
			sel = subquery.getSelection();
			if (sel != null) {
				logger.log(Logger.Level.TRACE, "Received non-result from getSelection()");
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Received null from getSelection() when there is a selection specified");
			}
			subquery.where(
					qbuilder.like(customer.get(Customer_.getSingularAttribute("name", String.class)), "%Caruso"));
			Expression exp = customer.get(Customer_.getSingularAttribute("name", String.class));

			subquery.groupBy(exp);
			gList = subquery.getGroupList();
			if (gList != null) {
				logger.log(Logger.Level.TRACE,
						"Received non-null from getGroupList() when there is groupBy expressions");
				if (gList.size() == 1) {
					logger.log(Logger.Level.TRACE, "Received one groupBy expression");
					pass4 = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected one groupBy expression, actual:" + gList.size());

					for (Expression e : gList) {
						logger.log(Logger.Level.ERROR, "Did not get expected result:" + e);
					}
				}
			} else {

				logger.log(Logger.Level.ERROR, "Received null from getGroupList() when there is groupBy expressions");
			}
			cquery.where(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("name", String.class)).in(subquery));

			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected " + " results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass5 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);

		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("subqueryGroupByExpressionTest failed");
		}
	}

	/*
	 * @testName: subqueryGroupByExpressionArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1198;
	 *
	 * @test_Strategy: Use groupBy expression in a sub query. Select the customers
	 * with name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryGroupByExpressionArrayTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);

			Subquery<String> subquery = cquery.subquery(String.class);
			Root<Customer> customer = subquery.from(Customer.class);
			EntityType<Customer> Customer_ = customer.getModel();

			subquery.select(customer.get(Customer_.getSingularAttribute("id", String.class)));
			subquery.where(
					qbuilder.like(customer.get(Customer_.getSingularAttribute("name", String.class)), "%Caruso"));
			Expression[] exp = { customer.get(Customer_.getSingularAttribute("id", String.class)) };

			subquery.groupBy(exp);

			cquery.where(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("id", String.class)).in(subquery));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected " + " results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception subquery: ", e);
		}

		if (!pass) {
			throw new Exception("subqueryGroupByExpressionArrayTest failed");
		}
	}

	/*
	 * @testName: subqueryGroupByListTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1183; PERSISTENCE:JAVADOC:1199;
	 *
	 * @test_Strategy: Use groupBy expression in a sub query. Select the customers
	 * with name like Caruso. The name Caruso is derived in the subquery.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryGroupByListTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder qbuilder = getEntityManagerFactory().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Order> cquery = qbuilder.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			EntityType<Order> Order_ = order.getModel();

			cquery.select(order);

			Subquery<String> subquery = cquery.subquery(String.class);
			Root<Customer> customer = subquery.from(Customer.class);
			EntityType<Customer> Customer_ = customer.getModel();

			subquery.select(customer.get(Customer_.getSingularAttribute("id", String.class)));
			subquery.where(
					qbuilder.like(customer.get(Customer_.getSingularAttribute("name", String.class)), "%Caruso"));
			List list = new ArrayList();
			list.add(customer.get(Customer_.getSingularAttribute("id", String.class)));

			subquery.groupBy(list);

			cquery.where(order.get(Order_.getSingularAttribute("customer", Customer.class))
					.get(Customer_.getSingularAttribute("id", String.class)).in(subquery));
			TypedQuery<Order> tquery = getEntityManager().createQuery(cquery);
			List<Order> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "7";
			expectedPKs[1] = "8";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected " + " results.  Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception subquery: ", e);
		}

		if (!pass) {
			throw new Exception("subqueryGroupByListTest failed");
		}
	}
}
