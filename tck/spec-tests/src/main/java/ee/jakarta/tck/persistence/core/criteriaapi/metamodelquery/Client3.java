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
import java.util.List;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Alias;
import ee.jakarta.tck.persistence.common.schema30.Alias_;
import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.UtilAliasData;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Client3 extends UtilAliasData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery3.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */
	/*
	 * @testName: queryTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:321; PERSISTENCE:SPEC:317.2;
	 * PERSISTENCE:SPEC:332; PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:517;
	 * PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519; PERSISTENCE:JAVADOC:93;
	 * PERSISTENCE:JAVADOC:94; PERSISTENCE:JAVADOC:985; PERSISTENCE:JAVADOC:1132;
	 * 
	 * @test_Strategy: This query is defined on a many-many relationship. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest3() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = true;
		boolean pass4 = false;
		String expectedPKs[];
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers with Alias: imc");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Set set = customer.getJoins();
			if (set.size() != 0) {
				logger.log(Logger.Level.ERROR, "Expected getJoins to return 0, actual:" + set.size());
			} else {
				pass1 = true;
			}
			Join<Customer, Alias> alias = customer.join(Customer_.aliases);
			set = customer.getJoins();
			if (set.size() > 0) {
				for (Object o : set) {
					pass2 = true;
					Join j = (Join) o;
					String s = j.getAttribute().getName();
					if (s.equals("aliases")) {
						logger.log(Logger.Level.TRACE, "Received expected join:aliases");
					} else {
						pass3 = false;
						logger.log(Logger.Level.ERROR, "Expected getJoins: value, actual:" + s);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Expected 1 join, actual:" + set.size());
			}
			cquery.where(cbuilder.equal(alias.get(Alias_.alias), cbuilder.parameter(String.class, "aName")))
					.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("aName", "imc");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "8";
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass4 = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass1 || !pass2 || !pass3 || !pass4) {
			throw new Exception("queryTest3 failed");
		}
	}

	/*
	 * @testName: queryTest20
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:361; PERSISTENCE:SPEC:348.3;
	 * 
	 * @test_Strategy: Execute a query using the comparison operator IS EMPTY.
	 * Verify the results were accurately returned.
	 *
	 * 
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest20() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who do not have aliases");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isEmpty(customer.get(Customer_.aliases)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "6";
			expectedPKs[1] = "15";
			expectedPKs[2] = "16";
			expectedPKs[3] = "17";
			expectedPKs[4] = "18";
			expectedPKs[5] = "19";
			expectedPKs[6] = "20";

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
			throw new Exception("queryTest20 failed");
		}
	}

	/*
	 * @testName: queryTest21
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:361; PERSISTENCE:SPEC:348.3
	 * 
	 * @test_Strategy: Execute a query using the comparison operator IS NOT EMPTY.
	 * Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest21() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who have aliases");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.isNotEmpty(customer.get(Customer_.aliases)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[13];
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

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 reference, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest21 failed");
		}
	}

	/*
	 * @testName: queryTest31
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358; PERSISTENCE:JAVADOC:805
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The optional ESCAPE syntax is
	 * used to escape the underscore. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest31() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias LIKE: sh_ll");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.like(a.get(Alias_.alias), "sh\\_ll", '\\'));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			throw new Exception("queryTest31 failed");
		}
	}

	/*
	 * @testName: likeExpExpCharTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:803
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The optional ESCAPE syntax is
	 * used to escape the underscore. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void likeExpExpCharTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias LIKE: sh_ll");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.like(a.get(Alias_.alias), cbuilder.literal("sh\\_ll"), '\\'));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			throw new Exception("likeExpExpCharTest failed");
		}
	}

	/*
	 * @testName: likeExpExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:802
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The optional ESCAPE syntax is
	 * used to escape the underscore. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void likeExpExpExpTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias LIKE: sh_ll");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.like(a.get(Alias_.alias), cbuilder.literal("sh\\_ll"), cbuilder.literal('\\')));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			throw new Exception("likeExpExpExpTest failed");
		}
	}

	/*
	 * @testName: likeExpStringExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:804
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause. The optional ESCAPE syntax is
	 * used to escape the underscore. Verify the results were accurately returned.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void likeExpStringExpTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias LIKE: sh_ll");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.like(a.get(Alias_.alias), "sh\\_ll", cbuilder.literal('\\')));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			throw new Exception("likeExpStringExpTest failed");
		}
	}

	/*
	 * @testName: queryTest45
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:361
	 * 
	 * @test_Strategy: Execute a query using IS NOT EMPTY in a
	 * collection_valued_association_field where the field is EMPTY.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest45() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE,
					"find customers whose id is greater than 1 " + "OR where the relationship is NOT EMPTY");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.where(cbuilder.or(cbuilder.isNotEmpty(customer.get(Customer_.aliasesNoop)),
					cbuilder.notEqual(customer.get(Customer_.id), "1")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

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
			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 19 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest45 failed");
		}
	}

	/*
	 * @testName: queryTest49
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:359
	 * 
	 * @test_Strategy: Use the operator IS NULL in a null comparison expression
	 * using a single_valued_path_expression. Verify the results were accurately
	 * returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest49() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who have a null relationship");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.isNull(a.get(Alias_.customerNoop)));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[13];
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

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 13 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest49 failed");
		}
	}

	/*
	 * @testName: queryTest50
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358
	 * 
	 * @test_Strategy: Execute a query using the comparison operator LIKE in a
	 * comparison expression within the WHERE clause using percent (%) to wild card
	 * any expression including the optional ESCAPE syntax. Verify the results were
	 * accurately returned.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest50() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias that contains an underscore");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.like(a.get(Alias_.alias), "%\\_%", '\\'));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
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
			throw new Exception("queryTest50 failed");
		}
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
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest51() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who do not have null relationship");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.isNotNull(a.get(Alias_.customerNoop)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			if (clist.size() != 0) {
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
			throw new Exception("queryTest51 failed");
		}
	}

	/*
	 * @testName: queryTest53
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:425
	 * 
	 * @test_Strategy: Define a query using Boolean operator OR in a conditional
	 * test (True OR True = True) where the second condition is NULL. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest53() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine if customer has a NULL relationship");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(
					cbuilder.or(cbuilder.equal(customer.get(Customer_.name), cbuilder.parameter(String.class, "cName")),
							cbuilder.isNull(a.get(Alias_.customerNoop))));
			cquery.select(customer).distinct(true);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("cName", "Arthur D. Frechette");
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			expectedPKs = new String[13];
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

			if (!checkEntityPK(clist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 13 references, got: " + clist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest53 failed");
		}
	}

	/*
	 * @testName: queryTest54
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:426
	 * 
	 * @test_Strategy: Define a query using Boolean operator NOT in a conditional
	 * test (NOT True = False) where the relationship is NOT NULL. Verify the
	 * results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest54() throws Exception {
		boolean pass = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine if customers have a NULL relationship");
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.isNotNull(a.get(Alias_.customerNoop)));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Customer> clist = tquery.getResultList();

			if (clist.size() != 0) {
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
			throw new Exception("queryTest54 failed");
		}
	}

	/*
	 * @testName: test_leftouterjoin_MxM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:317;
	 * PERSISTENCE:SPEC:317.3; PERSISTENCE:SPEC:320; PERSISTENCE:SPEC:811;
	 * PERSISTENCE:JAVADOC:993
	 * 
	 * @test_Strategy: Left Outer Join for M-M relationship. Retrieve all aliases
	 * where customer name like Ste.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_leftouterjoin_MxM() throws Exception {

		boolean pass1 = false;
		boolean pass2 = true;
		final Object[][] expectedResultSet = new Object[][] { new Object[] { "7", "sjc" }, new Object[] { "5", "ssd" },
				new Object[] { "7", "stevec" }, new Object[] { "5", "steved" }, new Object[] { "5", "stevie" },
				new Object[] { "7", "stevie" } };

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases, JoinType.LEFT);
			cquery.where(cbuilder.like(customer.get(Customer_.name), "Ste%"));
			cquery.multiselect(customer.get(Customer_.id), a.get(Alias_.alias))
					.orderBy(cbuilder.asc(a.get(Alias_.alias)), cbuilder.asc(customer.get(Customer_.id)));
			TypedQuery<Tuple> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(customerRef.length);
			List<Tuple> q = tquery.getResultList();

			if (q.size() != 6) {
				logger.log(Logger.Level.TRACE,
						"test_leftouterjoin_MxM:  Did not get expected results. " + "Expected 6,  got: " + q.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				// each element of the list q should be a size-2 array
				for (int i = 0; i < q.size(); i++) {
					pass1 = true;
					Object obj = q.get(i);
					Object[] customerAndAliasExpected = expectedResultSet[i];
					Tuple customerAndAliasTuple = null;
					Object[] customerAndAlias = null;
					if (obj instanceof Tuple) {
						logger.log(Logger.Level.TRACE,
								"The element in the result list is of type Object[], continue . . .");
						customerAndAliasTuple = (Tuple) obj;
						customerAndAlias = customerAndAliasTuple.toArray();
						if (!Arrays.equals(customerAndAliasExpected, customerAndAlias)) {
							logger.log(Logger.Level.ERROR,
									"Expecting element value: " + Arrays.asList(customerAndAliasExpected)
											+ ", actual element value: " + Arrays.asList(customerAndAlias));
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
			throw new Exception("test_leftouterjoin_MxM failed");
		}
	}

	/*
	 * @testName: test_upperStringExpression
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.11
	 * 
	 * @test_Strategy: Test for Upper expression in the Where Clause Select the
	 * customer with alias name = UPPER(SJC)
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_upperStringExpression() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.equal(cbuilder.upper(a.get(Alias_.alias)), "SJC"));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "7";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected " + expectedPKs.length
						+ " references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("test_upperStringExpression failed");
		}
	}

	/*
	 * @testName: test_lowerStringExpression
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.10
	 * 
	 * @test_Strategy: Test for Lower expression in the Where Clause Select the
	 * customer with alias name = LOWER(sjc)
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_lowerStringExpression() throws Exception {

		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.equal(cbuilder.lower(a.get(Alias_.alias)), "sjc")).select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "7";

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
			throw new Exception("test_lowerStringExpression failed");
		}
	}

	/*
	 * @testName: test_innerjoin_MxM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:779
	 * 
	 * @test_Strategy: Inner Join for M-M relationship. Retrieve aliases for alias
	 * name=fish.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_innerjoin_MxM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			cquery.where(cbuilder.equal(a.get(Alias_.alias), cbuilder.parameter(String.class, "aName")));
			cquery.select(customer);
			TypedQuery<Customer> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("aName", "fish");
			tquery.setMaxResults(customerRef.length);
			List<Customer> result = tquery.getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";

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
			throw new Exception("test_innerjoin_MxM failed");
		}
	}

	/*
	 * @testName: test_fetchjoin_MxM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:781
	 * 
	 * @test_Strategy: Left Join Fetch for M-M relationship. Retrieve customers with
	 * orders that live in NH.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_fetchjoin_MxM() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FETCHJOIN-MXM Executing Query");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			alias.fetch(Alias_.customers, JoinType.LEFT);
			cquery.where(cbuilder.like(alias.get(Alias_.alias), "a%")).select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(aliasRef.length);
			List<Alias> result = tquery.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";

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
			throw new Exception("test_fetchjoin_MxM failed");
		}
	}

	/*
	 * @testName: substringHavingExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807; PERSISTENCE:JAVADOC:922;
	 * 
	 * @test_Strategy:Test for Functional Expression: substring in Having Clause
	 * Select all customers with alias = fish
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void substringHavingExpressionTest() throws Exception {
		boolean pass = false;
		Object result;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing Query");
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			Expression exp = cbuilder.equal(a.get(Alias_.alias),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2")));
			cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias)).having(exp);
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("string1", "fish");
			tquery.setParameter("int1", Integer.valueOf(1));
			tquery.setParameter("int2", Integer.valueOf(4));
			result = tquery.getSingleResult();

			logger.log(Logger.Level.TRACE, "Check results received .  .  .");
			if (expectedCount.equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected Count of 2, got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" substringHavingExpressionTest failed");
		}
	}

	/*
	 * @testName: substringHavingExpressionPredicateArrayTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:923; PERSISTENCE:JAVADOC:946;
	 * 
	 * @test_Strategy:Test for Functional Expression: substring in Having Clause
	 * Select all customers with alias = fish
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void substringHavingExpressionPredicateArrayTest() throws Exception {
		boolean pass = false;
		Object result;
		final Long expectedCount = Long.valueOf(2);

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing Query");
			CriteriaQuery<Long> cquery = cbuilder.createQuery(Long.class);
			Root<Customer> customer = cquery.from(Customer.class);
			Join<Customer, Alias> a = customer.join(Customer_.aliases);
			Predicate[] pred = {
					cbuilder.equal(a.get(Alias_.alias), cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2"))),
					cbuilder.equal(a.get(Alias_.alias), cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int1"), cbuilder.parameter(Integer.class, "int2"))) };

			cquery.select(cbuilder.count(customer)).groupBy(a.get(Alias_.alias)).having(pred);
			TypedQuery<Long> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("string1", "fish");
			tquery.setParameter("int1", Integer.valueOf(1));
			tquery.setParameter("int2", Integer.valueOf(4));
			result = tquery.getSingleResult();

			logger.log(Logger.Level.TRACE, "Check results received .  .  .");
			if (expectedCount.equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected Count of 2, got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("substringHavingExpressionPredicateArrayTest failed");
		}
	}

}
