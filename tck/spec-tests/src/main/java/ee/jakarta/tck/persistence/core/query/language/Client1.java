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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.LineItem;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilOrderData;
import jakarta.persistence.Query;

public class Client1 extends UtilOrderData {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_query_language1.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */

	/*
	 * @testName: queryTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:312; PERSISTENCE:SPEC:322;
	 * PERSISTENCE:SPEC:602; PERSISTENCE:SPEC:603; PERSISTENCE:JAVADOC:91;
	 * PERSISTENCE:SPEC:785; PERSISTENCE:SPEC:786; PERSISTENCE:SPEC:1595;
	 * PERSISTENCE:SPEC:1600;
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Orders for Customer: Robert E. Bissett");
			o = getEntityManager().createQuery("Select Distinct o from Order AS o WHERE o.customer.name = :name")
					.setParameter("name", "Robert E. Bissett").getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest1 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all Customers with AXP Credit Cards");
			c = getEntityManager()
					.createQuery("Select Distinct Object(c) fRoM Customer c, IN(c.creditCards) a where a.type = :ccard")
					.setParameter("ccard", "AXP").getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "1";
			expectedPKs[1] = "4";
			expectedPKs[2] = "5";
			expectedPKs[3] = "8";
			expectedPKs[4] = "9";
			expectedPKs[5] = "12";
			expectedPKs[6] = "15";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 7 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest5 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where the total price is NOT less than $4500");
			o = getEntityManager().createQuery("Select Distinct Object(o) FROM Order o WHERE NOT o.totalPrice < 4500")
					.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "5";
			expectedPKs[1] = "11";
			expectedPKs[2] = "16";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest8 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where the customer name is Karen R. Tegan"
					+ " OR the total price is less than $100");
			o = getEntityManager().createQuery(
					"SeLeCt DiStInCt oBjEcT(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice < 100")
					.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "6";
			expectedPKs[1] = "9";
			expectedPKs[2] = "10";
			expectedPKs[3] = "12";
			expectedPKs[4] = "13";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest9 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where line item quantity is 1 AND the"
					+ " order total less than 100 or customer name is Robert E. Bissett");

			o = getEntityManager().createQuery(
					"select distinct Object(o) FROM Order AS o, in(o.lineItemsCollection) l WHERE (l.quantity < 2) AND ((o.totalPrice < (3 + 54 * 2 + -8)) OR (o.customer.name = 'Robert E. Bissett'))")
					.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			expectedPKs[2] = "12";
			expectedPKs[3] = "13";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest10 failed");
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
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"find all orders with line item quantity < 2" + " for customer Robert E. Bissett");
			o = getEntityManager().createQuery(
					"SELECT DISTINCT Object(o) FROM Order o, in(o.lineItemsCollection) l WHERE l.quantity < 2 AND o.customer.name = 'Robert E. Bissett'")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest11 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders with a total price BETWEEN $1000 and $1200");
			o = getEntityManager()
					.createQuery("SELECT DISTINCT Object(o) From Order o where o.totalPrice BETWEEN 1000 AND 1200")
					.getResultList();

			expectedPKs = new String[5];
			expectedPKs[0] = "1";
			expectedPKs[1] = "3";
			expectedPKs[2] = "7";
			expectedPKs[3] = "8";
			expectedPKs[4] = "14";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest12 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders with a total price NOT BETWEEN $1000 and $1200");
			o = getEntityManager()
					.createQuery("SELECT DISTINCT Object(o) From Order o where o.totalPrice NOT bETwEeN 1000 AND 1200")
					.getResultList();

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
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest13 failed");
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
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders that do not have approved Credit Cards");
			o = getEntityManager()
					.createQuery("select distinct Object(o) From Order o WHERE o.creditCard.approved = FALSE")
					.getResultList();

			expectedPKs = new String[6];
			expectedPKs[0] = "1";
			expectedPKs[1] = "7";
			expectedPKs[2] = "11";
			expectedPKs[3] = "13";
			expectedPKs[4] = "18";
			expectedPKs[5] = "20";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 6 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest14 failed");
	}

	/*
	 * @testName: queryTest27
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.5; PERSISTENCE:SPEC:368;
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function ABS in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest27() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing ABS with numeric Java object types");
			logger.log(Logger.Level.TRACE, "find all Orders with a total price greater than 1180");
			o = getEntityManager().createQuery("Select DISTINCT Object(o) From Order o WHERE :dbl < ABS(o.totalPrice)")
					.setParameter("dbl", 1180D).getResultList();

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
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 9 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}
			logger.log(Logger.Level.INFO, "Testing ABS with primitive numeric type");
			logger.log(Logger.Level.TRACE, "find all Orders with a total price greater than 1180");
			o = getEntityManager().createQuery("Select DISTINCT Object(o) From Order o WHERE o.totalPrice > ABS(:dbl)")
					.setParameter("dbl", 1180.55D).getResultList();

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
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 9 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest27 failed");
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
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where line items are members of the orders");
			o = getEntityManager()
					.createQuery(
							"Select Distinct Object(o) FROM Order o, LineItem l WHERE l MEMBER o.lineItemsCollection")
					.getResultList();

			expectedPKs = new String[orderRef.length];
			for (int i = 0; i < orderRef.length; i++)
				expectedPKs[i] = Integer.toString(i + 1);

			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected " + orderRef.length + "references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest32 failed");
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
		List o;
		LineItem liDvc;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find orders whose orders are do NOT contain the specified line items");
			liDvc = getEntityManager().find(LineItem.class, "30");
			o = getEntityManager()
					.createQuery("Select Distinct Object(o) FROM Order o WHERE :param NOT MEMBER o.lineItemsCollection")
					.setParameter("param", liDvc).getResultList();

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

			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest33 failed");
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
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find orders who have Samples in their orders");
			o = getEntityManager().createQuery(
					"Select Distinct Object(o) FROM Order o WHERE o.sampleLineItem MEMBER OF o.lineItemsCollection")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "1";
			expectedPKs[1] = "6";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest34 failed");
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
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders which contain lineitems not of quantities 1 or 5");
			o = getEntityManager().createQuery(
					"Select Distinct Object(o) from Order o, in(o.lineItemsCollection) l where l.quantity NOT IN (1, 5)")
					.getResultList();

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
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 9 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest35 failed");
	}

	/*
	 * @testName: queryTest39
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.6; PERSISTENCE:SPEC:814;
	 * PERSISTENCE:SPEC:816; PERSISTENCE:SPEC:368;
	 * 
	 * @test_Strategy: Execute a query which includes the arithmetic function SQRT
	 * in a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest39() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		String expectedPKs[];
		List c;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Testing SQRT with numeric Java object types");
			logger.log(Logger.Level.TRACE, "find customers with specific credit card balance");
			c = getEntityManager().createQuery(
					"Select Distinct OBJECT(c) from Customer c, IN(c.creditCards) b where SQRT(b.balance) = :dbl")
					.setParameter("dbl", 50D).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}

			logger.log(Logger.Level.INFO, "Testing SQRT with primitive numeric type");
			logger.log(Logger.Level.TRACE, "find customers with specific credit card balance");
			c = getEntityManager().createQuery(
					"Select Distinct OBJECT(c) from Customer c, IN(c.creditCards) b where SQRT(b.balance) = SQRT(:dbl)")
					.setParameter("dbl", 2500D).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("queryTest39 failed");
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
		List o;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all orders where related customer name is not null");
			o = getEntityManager()
					.createQuery("Select Distinct Object(o) from Order o where o.customer.name IS NOT NULL")
					.getResultList();

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

			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest42 failed");
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
		List o;
		final Double[] expectedBalances = new Double[] { 400D, 500D, 750D, 1000D, 1400D, 1500D, 2000D, 2500D, 4400D,
				5000D, 5500D, 7000D, 7400D, 8000D, 9500D, 13000D, 15000D, 23000D };
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all credit card balances");
			o = getEntityManager()
					.createQuery("Select Distinct o.creditCard.balance from Order o ORDER BY o.creditCard.balance ASC")
					.getResultList();

			Double[] result = (Double[]) (o.toArray(new Double[o.size()]));
			Iterator i = o.iterator();
			while (i.hasNext()) {
				logger.log(Logger.Level.TRACE, "query results returned:  " + (Double) i.next());
			}
			logger.log(Logger.Level.TRACE, "Compare expected results to query results");
			pass = Arrays.equals(expectedBalances, result);

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest48 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find Customers with an Order");
			c = getEntityManager().createQuery("SELECT DISTINCT c FROM Customer c, IN(c.orders) o").getResultList();

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
			throw new Exception("queryTest60 failed");
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
		List c;

		try {
			getEntityTransaction().begin();

			c = getEntityManager().createQuery("SELECT DISTINCT c FROM Customer c WHERE SIZE(c.orders) >= 2")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "14";

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest62 failed");
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
		List c;

		try {
			getEntityTransaction().begin();

			c = getEntityManager().createQuery("SELECT DISTINCT c FROM Customer c WHERE SIZE(c.orders) > 100")
					.getResultList();

			expectedPKs = new String[0];

			if (!checkEntityPK(c, expectedPKs)) {
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
			throw new Exception("queryTest63 failed");
	}

	/*
	 * @testName: queryTest65
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:381; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822; PERSISTENCE:SPEC:1674
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function MIN.
	 * Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest65() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 1;
		Query q, q1;
		List<Integer> i2;
		List<Integer> i3;

		try {
			logger.log(Logger.Level.TRACE, "select MINIMUM number of lineItem quantities available an order may have");
			q = getEntityManager().createQuery("SELECT DISTINCT MIN(l.quantity) FROM LineItem l");
			i2 = q.getResultList();

			q1 = getEntityManager().createQuery("SELECT MIN(l.quantity) FROM LineItem l");
			i3 = q1.getResultList();

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
			logger.log(Logger.Level.INFO, "Verify Select WITHOUT DISTINCT keyword");
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

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass1 || !pass2)
			throw new Exception("queryTest65 failed");
	}

	/*
	 * @testName: queryTest66
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:382; PERSISTENCE:SPEC:406;
	 * PERSISTENCE:SPEC:825; PERSISTENCE:SPEC:822; PERSISTENCE:SPEC:1674
	 * 
	 * @test_Strategy: Execute a query which contains the aggregate function MAX.
	 * Verify the results are accurately returned.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void queryTest66() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		final Integer i1 = 8;
		Query q, q1;
		List<Integer> i2;
		List<Integer> i3;

		try {
			logger.log(Logger.Level.TRACE, "find MAXIMUM number of lineItem quantities available an order may have");
			q = getEntityManager().createQuery("SELECT DISTINCT MAX(l.quantity) FROM LineItem l");
			i2 = q.getResultList();

			q1 = getEntityManager().createQuery("SELECT MAX(l.quantity) FROM LineItem l");
			i3 = q1.getResultList();

			logger.log(Logger.Level.INFO, "Testing select WITH DISTINCT keyword");
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
			logger.log(Logger.Level.INFO, "Testing Select WITHOUT DISTINCT keyword");
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

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass1 || !pass2)
			throw new Exception("queryTest66 failed");
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
		Double d1 = 1487.29;
		Double d2 = 1487.30;
		Double d3;
		Query q;

		try {
			logger.log(Logger.Level.TRACE, "find AVERAGE price of all orders");
			q = getEntityManager().createQuery("SELECT AVG(o.totalPrice) FROM Order o");

			d3 = (Double) q.getSingleResult();

			if (((d3 >= d1) && (d3 < d2))) {
				logger.log(Logger.Level.TRACE, "Returned expected results: " + d1);
				pass = true;
			} else {
				logger.log(Logger.Level.TRACE, "Returned " + d3 + "expected: " + d1);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}
		if (!pass)
			throw new Exception("queryTest67 failed");
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
		List q;
		boolean pass1 = false;
		boolean pass2 = true;
		Object[][] expectedResultSet = new Object[][] { new Object[] { "15", "14" }, new Object[] { "16", "14" } };

		try {
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("SELECT o.id, cust.id from Order o LEFT OUTER JOIN o.customer cust"
					+ " where cust.name=?1 ORDER BY o.id").setParameter(1, "Kellie A. Sanborn").getResultList();

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
					Object[] orderAndCustomer;
					if (obj instanceof Object[]) {
						logger.log(Logger.Level.TRACE,
								"The element in the result list is of type Object[], continue . . .");
						orderAndCustomer = (Object[]) obj;
						if (!Arrays.equals(orderAndCustomerExpected, orderAndCustomer)) {
							logger.log(Logger.Level.ERROR,
									"Expecting element value: " + Arrays.asList(orderAndCustomerExpected)
											+ ", actual element value: " + Arrays.asList(orderAndCustomer));
							pass2 = false;
							break;
						}
					} else {
						pass2 = false;
						logger.log(Logger.Level.ERROR, "The element in the result list is not of type Object[]:" + obj);
						break;
					}
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("test_leftouterjoin_Mx1 failed");
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
		List result;
		String expected[] = new String[] { "AXP", "MCARD", "VISA" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select cc.type FROM CreditCard cc JOIN cc.customer cust GROUP BY cc.type")
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

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_groupBy_1 failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();

			result = getEntityManager().createQuery(
					"SELECT DISTINCT object(c) from Customer c INNER JOIN c.creditCards cc where cc.type='VISA' ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_innerjoin_1xM failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];
		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT Object(o) from Order o INNER JOIN o.customer cust where cust.name = ?1")
					.setParameter(1, "Kellie A. Sanborn").getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_innerjoin_Mx1 failed");
	}

	/*
	 * @testName: test_fetchjoin_1xM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:782; PERSISTENCE:SPEC:374;
	 * PERSISTENCE:SPEC:777; PERSISTENCE:SPEC:783
	 * 
	 * @test_Strategy: Fetch Join for 1-M relationship. Retrieve customers from NY
	 * or RI who have orders.
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_fetchjoin_1xM() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT DISTINCT c from Customer c LEFT JOIN FETCH c.orders where c.home.state IN('NY','RI')")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "14";
			expectedPKs[1] = "17";

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
			throw new Exception("test_fetchjoin_1xM failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select o from Order o LEFT JOIN FETCH o.customer where o.customer.home.city='Lawrence'")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_fetchjoin_Mx1 failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select Object(o) from Order o LEFT JOIN FETCH o.customer where o.customer.name LIKE '%Caruso' ")
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
			throw new Exception("test_fetchjoin_Mx1_1 failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT DISTINCT o From Order o where o.totalPrice NOT BETWEEN 1000 AND 1200")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_notBetweenArithmetic failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' AND o.totalPrice > 500")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ANDconditionTT failed");
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
		List result;

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' AND o.totalPrice > 10000")
					.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ANDconditionTF failed");
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
		List result;

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select Object(o) FROM Order AS o WHERE o.customer.id = '1001' AND o.totalPrice < 1000 ")
					.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ANDconditionFT failed");
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
		List result;

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select Object(o) FROM Order AS o WHERE o.customer.id = '1001' AND o.totalPrice > 10000")
					.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ANDconditionFF failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select DISTINCT o FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice > 5000")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ORconditionTT failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select Object(o) FROM Order AS o WHERE o.customer.name = 'Karen R. Tegan' OR o.totalPrice > 10000")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ORconditionTF failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select Distinct Object(o) FROM Order AS o WHERE o.customer.id = '1001' OR o.totalPrice < 1000 ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ORconditionFT failed");
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
		List result;

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery(
							"select Object(o) FROM Order AS o WHERE o.customer.id = '1001' OR o.totalPrice > 10000")
					.getResultList();

			if (result.size() == 0) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + result.size());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ORconditionFF failed");
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
		List result;
		final String[] expected = new String[] { "Jonathan K. Smith", "Kellie A. Sanborn", "Robert E. Bissett" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"select c.name FROM Customer c JOIN c.orders o WHERE o.totalPrice BETWEEN 90 AND 160 GROUP BY c.name")
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
			throw new Exception("test_groupByWhereClause failed");
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
		Query q;
		Object result;
		final Double expectedPrice = 10191.90D;

		try {
			getEntityTransaction().begin();
			q = getEntityManager().createQuery(
					"select sum(o.totalPrice) FROM Order o GROUP BY o.totalPrice HAVING ABS(o.totalPrice) = :doubleValue ")
					.setParameter("doubleValue", 5095.95);

			result = (Double) q.getSingleResult();

			if (expectedPrice.equals(result)) {
				pass = true;
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "test_ABSHavingClause:  Did not get expected results."
						+ "Expected 10190, got: " + (Double) result);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_ABSHavingClause failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "SQRT: Executing Query");
			result = getEntityManager()
					.createQuery("select object(o) FROM Order o Where SQRT(o.totalPrice) > :doubleValue ")
					.setParameter("doubleValue", 70D).getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_SQRTWhereClause failed");

	}

	/*
	 * @testName: test_subquery_exists_01
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:791;PERSISTENCE:SPEC:792;
	 * PERSISTENCE:SPEC:1599;
	 * 
	 * @test_Strategy: Test NOT EXISTS in the Where Clause for a correlated query.
	 * Select the customers without orders.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void test_subquery_exists_01() throws Exception {
		boolean pass = false;
		List result;
		final String[] expectedPKs = { "19", "20" };

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT c FROM Customer c WHERE NOT EXISTS (SELECT o1 FROM c.orders o1) ")
					.getResultList();

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "test_subquery_exists_01:  Did not get expected results.  "
						+ "Expected 2 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_exists_01 failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT DISTINCT c FROM Customer c WHERE EXISTS (SELECT o FROM c.orders o where o.totalPrice > 1500 ) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_exists_02 failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("Select Object(o) from Order o WHERE EXISTS "
					+ "(Select c From o.customer c WHERE c.name LIKE '%Caruso') ").getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_like failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Execute query for test_subquery_between");
			result = getEntityManager().createQuery(
					"SELECT DISTINCT c FROM Customer c WHERE EXISTS (SELECT o FROM c.orders o where o.totalPrice BETWEEN 1000 AND 1200)")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_between failed");

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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery("SELECT DISTINCT c FROM Customer c JOIN c.orders o WHERE EXISTS "
					+ "(SELECT o FROM o.lineItemsCollection l where l.quantity > 3 ) ").getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_join failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice > "
							+ "ALL (Select o.totalPrice FROM Order o, in(o.lineItemsCollection) l WHERE l.quantity > 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_ALL_GT failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT distinct object(C) FROM Customer C, IN(C.orders) co WHERE co.totalPrice < "
							+ "ALL (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity > 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ALL_LT failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice = ALL"
							+ " (Select MIN(o.totalPrice) FROM Order o) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ALL_EQ failed");

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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= ALL"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity > 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ALL_LTEQ failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT DISTINCT object(c) FROM Customer C, IN(c.orders) co WHERE co.totalPrice >= ALL"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity >= 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ALL_GTEQ failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice <> "
							+ "ALL (Select MIN(o.totalPrice) FROM Order o) ")
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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ALL_NOTEQ failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT DISTINCT c FROM Customer c, IN(c.orders) co WHERE co.totalPrice > ANY"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("test_subquery_ANY_GT failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT Distinct Object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice < ANY"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3)")
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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ANY_LT failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice = ANY"
							+ " (Select MAX(o.totalPrice) FROM Order o) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_ANY_EQ failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT DISTINCT object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice <= SOME"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
					.getResultList();

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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_subquery_SOME_LTEQ failed");
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
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager().createQuery(
					"SELECT Distinct object(c) FROM Customer c, IN(c.orders) co WHERE co.totalPrice >= SOME"
							+ " (Select o.totalPrice FROM Order o, IN(o.lineItemsCollection) l WHERE l.quantity = 3) ")
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
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_subquery_SOME_GTEQ failed");
	}

	/*
	 * @testName: treatJoinClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1678
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void treatJoinClassTest() throws Exception {
		boolean pass = false;
		List<String> actual;
		List<String> expected = new ArrayList<String>();
		expected.add(softwareRef[0].getName());
		expected.add(softwareRef[1].getName());
		expected.add(softwareRef[2].getName());
		expected.add(softwareRef[3].getName());
		expected.add(softwareRef[4].getName());
		expected.add(softwareRef[5].getName());

		try {
			getEntityTransaction().begin();
			actual = getEntityManager()
					.createQuery("SELECT s.name FROM LineItem l JOIN TREAT(l.product AS SoftwareProduct) s")
					.getResultList();

			Collections.sort(actual);
			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "result:" + s);

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("treatJoinClassTest failed");
		}
	}

	/*
	 * @testName: treatInWhereClauseTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1620; PERSISTENCE:SPEC:1627;
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void treatInWhereClauseTest() throws Exception {
		boolean pass = false;
		List<String> actual;
		List<String> expected = new ArrayList<String>();
		expected.add(softwareRef[0].getName());
		expected.add(softwareRef[7].getName());

		try {
			getEntityTransaction().begin();
			actual = getEntityManager()
					.createQuery("SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1.0")
					.getResultList();

			Collections.sort(actual);
			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "result:" + s);

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("treatInWhereClauseTest failed");
		}
	}

	/*
	 * @testName: appropriateSuffixesTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1628;
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void appropriateSuffixesTest() throws Exception {
		boolean pass = false;
		List<String> actual;
		List<String> expected = new ArrayList<String>();
		expected.add(softwareRef[0].getName());
		expected.add(softwareRef[7].getName());

		try {
			getEntityTransaction().begin();
			actual = getEntityManager()
					.createQuery("SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1.0D")
					.getResultList();

			Collections.sort(actual);
			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "result:" + s);

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("appropriateSuffixesTest failed");
		}
	}

	/*
	 * @testName: sqlApproximateNumericLiteralTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1627;
	 * 
	 * @test_Strategy:
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void sqlApproximateNumericLiteralTest() throws Exception {
		boolean pass = false;
		List<String> actual;
		List<String> expected = new ArrayList<String>();
		expected.add(softwareRef[0].getName());
		expected.add(softwareRef[7].getName());

		try {
			getEntityTransaction().begin();
			actual = getEntityManager()
					.createQuery("SELECT p.name FROM Product p where TREAT(p AS SoftwareProduct).revisionNumber = 1E0")
					.getResultList();

			Collections.sort(actual);
			for (String s : actual) {
				logger.log(Logger.Level.TRACE, "result:" + s);

			}
			if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected result:");
				for (String s : expected) {
					logger.log(Logger.Level.TRACE, "expected:" + s);
				}
				for (String s : actual) {
					logger.log(Logger.Level.TRACE, "actual:" + s);
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("sqlApproximateNumericLiteralTest failed");
		}
	}

	/*
	 * @testName: joinOnExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1716
	 * 
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void joinOnExpressionTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List<Order> actual;
		try {
			getEntityTransaction().begin();
			actual = getEntityManager()
					.createQuery("select o FROM Order o INNER JOIN o.lineItemsCollection l ON (l.quantity > 5)")
					.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "10";
			expectedPKs[1] = "12";

			for (Order o : actual) {
				logger.log(Logger.Level.TRACE, "order:" + o.getId() + ":");
				Collection<LineItem> li = o.getLineItemsCollection();
				for (LineItem i : li) {
					logger.log(Logger.Level.TRACE, "   item:" + i.getId() + ", " + i.getQuantity());
				}
			}

			if (!checkEntityPK(actual, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + actual.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("joinOnExpressionTest failed");
	}

	/*
	 * @testName: subqueryVariableOverridesQueryVariableTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1599;
	 * 
	 * @test_Strategy: variable in a query is overridden in a subquery
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void subqueryVariableOverridesQueryVariableTest() throws Exception {

		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT c FROM Customer c WHERE c.id in (SELECT c.id FROM Order c where c.id='10' ) ")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "10";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Expected 1 result, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("subqueryVariableOverridesQueryVariableTest failed");
	}

	/*
	 * @testName: longIdentifierNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1586; PERSISTENCE:SPEC:1590
	 * 
	 * @test_Strategy: verify a long identifier name can be used and that the
	 * identifier evaluates to a value of the type of the expression
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void longIdentifierNameTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = true;
		String expectedPKs[];
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Orders for Customer: Robert E. Bissett");
			o = getEntityManager().createQuery(
					"Select Distinct variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 from Order variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789 WHERE variable01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789.customer.name = :name")
					.setParameter("name", "Robert E. Bissett").getResultList();
			if (o.size() > 0) {
				for (Object oo : o) {
					if (!(oo instanceof Order)) {
						logger.log(Logger.Level.ERROR,
								"Object returned was not of type Order:" + oo.getClass().getName());
						pass2 = false;
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Not results were returned");
				pass2 = false;
			}
			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass1 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("longIdentifierNameTest failed");
	}

	/*
	 * @testName: underscoreIdentifierNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1587; PERSISTENCE:SPEC:1588;
	 * 
	 * @test_Strategy: verify an identifier name can begin with an underscore.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void underscoreIdentifierNameTest() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		String expectedPKs[];
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Orders for Customer: Robert E. Bissett");
			String variable = "_01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
			if (checkIdentifierValues(variable)) {
				logger.log(Logger.Level.TRACE, "Identifier is valid");
				pass1 = true;
			} else {
				logger.log(Logger.Level.TRACE, "Identifier[" + variable + "] is invalid");
			}
			String sQuery = "Select Distinct " + variable + " from Order " + variable + " WHERE " + variable
					+ ".customer.name = :name";
			logger.log(Logger.Level.TRACE, "Query=" + sQuery);
			o = getEntityManager().createQuery(sQuery).setParameter("name", "Robert E. Bissett").getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("underscoreIdentifierNameTest failed");
	}

	/*
	 * @testName: dollarsignIdentifierNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1587; PERSISTENCE:SPEC:1588;
	 * 
	 * @test_Strategy: verify an identifier name can begin with a dollarsign.
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void dollarsignIdentifierNameTest() throws Exception {
		boolean pass1, pass2;
		pass1 = pass2 = false;
		String expectedPKs[];
		List o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Orders for Customer: Robert E. Bissett");

			String variable = "$01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
			if (checkIdentifierValues(variable)) {
				logger.log(Logger.Level.TRACE, "Identifier is valid");
				pass1 = true;
			} else {
				logger.log(Logger.Level.TRACE, "Identifier[" + variable + "] is invalid");
			}
			String sQuery = "Select Distinct " + variable + " from Order " + variable + " WHERE " + variable
					+ ".customer.name = :name";
			logger.log(Logger.Level.TRACE, "Query=" + sQuery);
			o = getEntityManager().createQuery(sQuery).setParameter("name", "Robert E. Bissett").getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "4";
			expectedPKs[1] = "9";
			if (!checkEntityPK(o, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass2 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass1 || !pass2)
			throw new Exception("dollarsignIdentifierNameTest failed");
	}

	/*
	 * @testName: distinctNotSpecifiedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1664;
	 * 
	 * @test_Strategy: Verify duplicates are returned when distinct is not specified
	 *
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void distinctNotSpecifiedTest() throws Exception {
		boolean pass = false;
		Integer expectedPKs[];
		List<String> o;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All customer ids from Orders");
			o = getEntityManager().createQuery("Select o.customer.id from Order AS o ").getResultList();

			expectedPKs = new Integer[20];
			expectedPKs[0] = Integer.parseInt("1");
			expectedPKs[1] = Integer.parseInt("2");
			expectedPKs[2] = Integer.parseInt("3");
			expectedPKs[3] = Integer.parseInt("4");
			expectedPKs[4] = Integer.parseInt("4");
			expectedPKs[5] = Integer.parseInt("5");
			expectedPKs[6] = Integer.parseInt("6");
			expectedPKs[7] = Integer.parseInt("7");
			expectedPKs[8] = Integer.parseInt("8");
			expectedPKs[9] = Integer.parseInt("9");
			expectedPKs[10] = Integer.parseInt("10");
			expectedPKs[11] = Integer.parseInt("11");
			expectedPKs[12] = Integer.parseInt("12");
			expectedPKs[13] = Integer.parseInt("13");
			expectedPKs[14] = Integer.parseInt("14");
			expectedPKs[15] = Integer.parseInt("14");
			expectedPKs[16] = Integer.parseInt("15");
			expectedPKs[17] = Integer.parseInt("16");
			expectedPKs[18] = Integer.parseInt("17");
			expectedPKs[19] = Integer.parseInt("18");

			if (!checkEntityPK(o, expectedPKs, true, true)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("distinctNotSpecifiedTest failed");
	}

	/*
	 * @testName: resultVariableTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1666;
	 * 
	 * @test_Strategy: A result variable may be used to name a select item in the
	 * query result
	 */
	@SetupMethod(name = "setupOrderData")
	@Test
	public void resultVariableTest() throws Exception {

		boolean pass = false;
		List<String> o;

		try {
			getEntityTransaction().begin();

			o = getEntityManager()
					.createQuery("Select o.id AS OID from Order o " + "WHERE (o.totalPrice < 100.0) ORDER BY OID")
					.getResultList();

			Integer expectedPKs[] = new Integer[4];
			expectedPKs[0] = Integer.parseInt("9");
			expectedPKs[1] = Integer.parseInt("10");
			expectedPKs[2] = Integer.parseInt("12");
			expectedPKs[3] = Integer.parseInt("13");

			if (!checkEntityPK(o, expectedPKs, true, true)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected " + expectedPKs.length
						+ " references, got: " + o.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("resultVariableTest failed");
	}

	private boolean checkIdentifierValues(String var) {
		boolean pass = true;
		logger.log(Logger.Level.INFO, "Testing that identifier is valid");
		char[] c = var.toCharArray();
		boolean first = true;
		for (char cc : c) {
			if (first) {
				if (!Character.isJavaIdentifierStart(cc)) {
					logger.log(Logger.Level.ERROR, "Value[" + cc + "is not a valid start character");
					pass = false;
				}
				// logger.log(Logger.Level.TRACE,"start["+cc+"]:"+Character.isJavaIdentifierStart(cc));
				first = false;
			} else {
				// logger.log(Logger.Level.TRACE,"part["+cc+"]:"+Character.isJavaIdentifierPart(cc));
				if (!Character.isJavaIdentifierPart(cc)) {
					logger.log(Logger.Level.ERROR, "Value[" + cc + "is not a valid part character");
					pass = false;
				}
			}
		}
		return pass;
	}

}
