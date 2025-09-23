/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import ee.jakarta.tck.persistence.core.versioning.Member;
import jakarta.persistence.PersistenceUnitUtil;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.UtilAliasData;
import jakarta.persistence.Query;

public class Client3 extends UtilAliasData {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		classes = Arrays.copyOf(classes, classes.length + 1);
		classes[classes.length - 1] = Member.class.getName();
		return createDeploymentJar("jpa_core_query_language3.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */

	/*
	 * @testName: queryTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:321; PERSISTENCE:SPEC:317.2;
	 * PERSISTENCE:SPEC:332; PERSISTENCE:SPEC:323; PERSISTENCE:SPEC:517;
	 * PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519; PERSISTENCE:JAVADOC:93;
	 * PERSISTENCE:JAVADOC:94
	 * 
	 * @test_Strategy: This query is defined on a many-many relationship. Verify the
	 * results were accurately returned.
	 */

	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest3() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers with Alias: imc");
			c = getEntityManager()
					.createQuery("Select Distinct Object(c) FrOm Customer c, In(c.aliases) a WHERE a.alias = :aName")
					.setParameter("aName", "imc").getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "8";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest3 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who do not have aliases");
			c = getEntityManager().createQuery("Select Distinct Object(c) FROM Customer c WHERE c.aliases IS EMPTY")
					.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "6";
			expectedPKs[1] = "15";
			expectedPKs[2] = "16";
			expectedPKs[3] = "17";
			expectedPKs[4] = "18";
			expectedPKs[5] = "19";
			expectedPKs[6] = "20";

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
			throw new Exception("queryTest20 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers who have aliases");
			c = getEntityManager().createQuery("Select Distinct Object(c) FROM Customer c WHERE c.aliases IS NOT EMPTY")
					.getResultList();

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

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 15 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest21 failed");
	}

	/*
	 * @testName: queryTest24
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.1
	 * 
	 * @test_Strategy: Execute a query which includes the string function CONCAT in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest24() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases who have match: stevie");
			a = getEntityManager()
					.createQuery("Select Distinct Object(a) From Alias a WHERE a.alias = CONCAT('ste', 'vie')")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "14";
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest24 failed");
	}

	/*
	 * @testName: queryTest25
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.2
	 * 
	 * @test_Strategy: Execute a query which includes the string function SUBSTRING
	 * in a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest25() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases containing the substring: iris");
			a = getEntityManager()
					.createQuery(
							"Select Distinct Object(a) From Alias a WHERE a.alias = SUBSTRING(:string1, :int2, :int3)")
					.setParameter("string1", "iris").setParameter("int2", 1).setParameter("int3", 4).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "20";
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest25 failed");
	}

	/*
	 * @testName: queryTest26
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.4
	 * 
	 * @test_Strategy: Execute a query which includes the string function LENGTH in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest26() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find aliases whose alias name is greater than 4 characters");
			a = getEntityManager().createQuery("Select Distinct OBjeCt(a) From Alias a WHERE LENGTH(a.alias) > 4")
					.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "8";
			expectedPKs[1] = "10";
			expectedPKs[2] = "13";
			expectedPKs[3] = "14";
			expectedPKs[4] = "18";
			expectedPKs[5] = "28";
			expectedPKs[6] = "29";
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 7 references, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest26 failed");
	}

	/*
	 * @testName: queryTest28
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:369.3
	 * 
	 * @test_Strategy: Execute a query which includes the string function LOCATE in
	 * a functional expression within the WHERE clause. Verify the results were
	 * accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest28() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases who contain the string: ev in their alias name");
			a = getEntityManager().createQuery("Select Distinct Object(a) from Alias a where LOCATE('ev', a.alias) = 3")
					.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "13";
			expectedPKs[1] = "14";
			expectedPKs[2] = "18";
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest28 failed");
	}

	/*
	 * @testName: queryTest29
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363.1; PERSISTENCE:SPEC:365
	 * 
	 * @test_Strategy: Execute a query using the comparison operator MEMBER OF in a
	 * collection member expression. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest29() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find aliases who are members of customersNoop");
			a = getEntityManager()
					.createQuery(
							"Select Distinct Object(a) FROM Alias a WHERE a.customerNoop MEMBER OF a.customersNoop")
					.getResultList();

			expectedPKs = new String[0];
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 0 references, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest29 failed");
	}

	/*
	 * @testName: queryTest30
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:363; PERSISTENCE:SPEC:365
	 * 
	 * @test_Strategy: Execute a query using the comparison operator NOT MEMBER in a
	 * collection member expression. Verify the results were accurately returned.
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest30() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List a;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find aliases who are NOT members of collection");
			a = getEntityManager()
					.createQuery(
							"Select Distinct Object(a) FROM Alias a WHERE a.customerNoop NOT MEMBER OF a.customersNoop")
					.getResultList();

			expectedPKs = new String[aliasRef.length];
			for (int i = 0; i < aliasRef.length; i++)
				expectedPKs[i] = Integer.toString(i + 1);
			if (!checkEntityPK(a, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected " + aliasRef.length + "references, got: " + a.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest30 failed");
	}

	/*
	 * @testName: queryTest31
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:358
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias LIKE: sh_ll");
			c = getEntityManager().createQuery(
					"select distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.alias LIKE 'sh\\_ll' escape '\\'")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest31 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find All Customers who have a null relationship");
			c = getEntityManager()
					.createQuery(
							"Select Distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.customerNoop IS NULL")
					.getResultList();

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

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 13 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest49 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all customers with an alias that contains an underscore");
			c = getEntityManager().createQuery(
					"select distinct Object(c) FROM Customer c, in(c.aliases) a WHERE a.alias LIKE '%\\_%' escape '\\'")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest50 failed");
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

	@SetupMethod(name = "setupAliasData")
	@Test
	public void queryTest52() throws Exception {
		boolean pass = false;
		String expectedPKs[];
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine if customer has a NULL relationship");
			c = getEntityManager().createQuery(
					"select Distinct Object(c) from Customer c, in(c.aliases) a where c.name = :cName AND a.customerNoop IS NULL")
					.setParameter("cName", "Shelly D. McGowan").getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "3";
			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 1 reference, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest52 failed");
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
		List c;
		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "determine if customer has a NULL relationship");
			c = getEntityManager().createQuery(
					"select distinct object(c) fRoM Customer c, IN(c.aliases) a where c.name = :cName OR a.customerNoop IS NULL")
					.setParameter("cName", "Arthur D. Frechette").getResultList();

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

			if (!checkEntityPK(c, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 13 references, got: " + c.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: ", e);
		}

		if (!pass)
			throw new Exception("queryTest53 failed");
	}

	/*
	 * @testName: test_leftouterjoin_MxM
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:780; PERSISTENCE:SPEC:317;
	 * PERSISTENCE:SPEC:317.3; PERSISTENCE:SPEC:320; PERSISTENCE:SPEC:811
	 * 
	 * @test_Strategy: Left Outer Join for M-M relationship. Retrieve all aliases
	 * where customer name like Ste.
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_leftouterjoin_MxM() throws Exception {

		List q;
		boolean pass1 = false;
		boolean pass2 = true;
		Object[][] expectedResultSet = new Object[][] { new Object[] { "7", "sjc" }, new Object[] { "5", "ssd" },
				new Object[] { "7", "stevec" }, new Object[] { "5", "steved" }, new Object[] { "5", "stevie" },
				new Object[] { "7", "stevie" } };
		try {
			getEntityTransaction().begin();
			q = getEntityManager().createQuery("SELECT c.id, a.alias from Customer c LEFT OUTER JOIN c.aliases a "
					+ "where c.name LIKE 'Ste%' ORDER BY a.alias, c.id").getResultList();

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
					Object[] customerAndAlias;
					if (obj instanceof Object[]) {
						logger.log(Logger.Level.TRACE,
								"The element in the result list is of type Object[], continue . . .");
						customerAndAlias = (Object[]) obj;
						if (!Arrays.equals(customerAndAliasExpected, customerAndAlias)) {
							logger.log(Logger.Level.ERROR,
									"Expecting element value: " + Arrays.asList(customerAndAliasExpected)
											+ ", actual element value: " + Arrays.asList(customerAndAlias));
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
			throw new Exception("test_leftouterjoin_MxM failed");
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

		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select Object(c) FROM Customer c JOIN c.aliases a where UPPER(a.alias)='SJC' ")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "7";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_upperStringExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_upperStringExpression failed");
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

		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select Object(c) FROM Customer c JOIN c.aliases a where LOWER(a.alias)='sjc' ")
					.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "7";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_lowerStringExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_lowerStringExpression failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT Object(c) from Customer c INNER JOIN c.aliases a where a.alias = :aName ")
					.setParameter("aName", "fish").getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";

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
			throw new Exception("test_innerjoin_MxM failed");
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
		List result;
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "FETCHJOIN-MXM Executing Query");
			result = getEntityManager()
					.createQuery("SELECT DISTINCT a from Alias a LEFT JOIN FETCH a.customers where a.alias LIKE 'a%' ")
					.getResultList();

			expectedPKs = new String[4];
			expectedPKs[0] = "1";
			expectedPKs[1] = "2";
			expectedPKs[2] = "5";
			expectedPKs[3] = "6";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 4 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_fetchjoin_MxM failed");
	}

	/*
	 * @testName: test_substringHavingClause
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:807
	 * 
	 * @test_Strategy:Test for Functional Expression: substring in Having Clause
	 * Select all customers with alias = fish
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_substringHavingClause() throws Exception {
		boolean pass = false;
		Query q;
		Object result;
		final Long expectedCount = 2L;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing Query");
			q = getEntityManager()
					.createQuery("select count(c) FROM Customer c JOIN c.aliases a GROUP BY a.alias "
							+ "HAVING a.alias = SUBSTRING(:string1, :int1, :int2)")
					.setParameter("string1", "fish").setParameter("int1", 1).setParameter("int2", 4);

			result = (Long) q.getSingleResult();

			logger.log(Logger.Level.TRACE, "Check results received .  .  .");
			if (expectedCount.equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not get expected results. Expected Count of 2, got: " + result);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception(" test_substringHavingClause failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_leftStringExpression() throws Exception {

		String result;
		boolean pass = false;
		//Irene M.
		String expectedName = customerRef[7].getName().substring(0, 8);

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select LEFT(c.name, 8) FROM Customer c WHERE LEFT(c.name, 5)='Irene' ", String.class)
					.getSingleResult();

			if (!result.equals(expectedName)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result);
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_leftStringExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_leftStringExpression failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_rightStringExpression() throws Exception {

		String result;
		boolean pass = false;
		//M. Caruso
		String expectedName = customerRef[7].getName().substring(6, 15);

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select RIGHT(c.name, 9) FROM Customer c WHERE RIGHT(c.name, 9)='M. Caruso' ", String.class)
					.getSingleResult();

			if (!result.equals(expectedName)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result);
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_rightStringExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_rightStringExpression failed");
	}

/*	Prepared bur DERBY doesn't support REPLACE
	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_replaceStringExpression() throws Exception {

		String result;
		boolean pass = false;
		//Irene M. Caruso -> Irene J. Caruso
		String expectedName = customerRef[7].getName().replace("M.", "J.");

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select REPLACE(c.name, 'M.', 'J.') FROM Customer c WHERE REPLACE(c.name, 'M.', 'A.') = 'Irene A. Caruso' ", String.class)
					.getSingleResult();

			if (!result.equals(expectedName)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result);
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_replaceStringExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_replaceStringExpression failed");
	}
*/


	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_concatStringOperator() throws Exception {

		String result;
		boolean pass = false;
		//8Irene M. CarusoUnited States
		String expectedResult = customerRef[7].getId() + customerRef[7].getName() + customerRef[7].getCountry().getCountry();

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select c.id || c.name || c.country.country FROM Customer c WHERE c.id || c.name = '8Irene M. Caruso' ", String.class)
					.getSingleResult();

			if (!result.equals(expectedResult)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result);
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_concatStringOperator: ", e);
		}

		if (!pass)
			throw new Exception("test_concatStringOperator failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_castExpression() throws Exception {

		int result;
		boolean pass = false;
		//8
		int expectedId = Integer.valueOf(customerRef[7].getId());

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("select CAST(c.id AS INTEGER) FROM Customer c WHERE CAST(c.id AS INTEGER)=8 ", Integer.class)
					.getSingleResult();

			if (result !=expectedId) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 references, got: " + result);
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception caught exception in test_castExpression: ", e);
		}

		if (!pass)
			throw new Exception("test_castExpression failed");
	}


	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_unionOperator() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[] = new String[] {"1", "2", "3", "6", "7", "8"};

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "UNION Executing Query");
			result = getEntityManager()
					.createQuery("SELECT c from Customer c WHERE c.id IN ('1', '2', '3') UNION SELECT c from Customer c WHERE c.id IN ('6', '7', '8')")
					.getResultList();

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 6 references, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_unionOperator failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_intersectOperator() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[] = new String[] {"2", "3"};

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "UNION Executing Query");
			result = getEntityManager()
					.createQuery("SELECT c from Customer c WHERE c.id IN ('1', '2', '3') INTERSECT SELECT c from Customer c WHERE c.id IN ('2', '3', '4')")
					.getResultList();

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
			throw new Exception("test_intersectOperator failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_exceptOperator() throws Exception {
		List result;
		boolean pass = false;
		String expectedPKs[] = new String[] {"1", "2"};

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "UNION Executing Query");
			result = getEntityManager()
					.createQuery("SELECT c from Customer c WHERE c.id IN ('1', '2', '3', '4') EXCEPT SELECT c from Customer c WHERE c.id IN ('3', '4')")
					.getResultList();

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
			throw new Exception("test_exceptOperator failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_orderByNullsFirst() throws Exception {
		List result;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "UNION Executing Query");
			result = getEntityManager()
					.createQuery("SELECT c from Customer c ORDER BY c.home ASC NULLS FIRST")
					.getResultList();

			if (((Customer) result.get(0)).getHome() != null & ((Customer) result.get(1)).getHome() != null) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected first two references, got not null home address");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_orderByNullsFirst failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_orderByNullsLast() throws Exception {
		List result;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "UNION Executing Query");
			result = getEntityManager()
					.createQuery("SELECT c from Customer c ORDER BY c.home ASC NULLS LAST")
					.getResultList();

			if (((Customer) result.get(result.size() - 1)).getHome() != null & ((Customer) result.get(result.size() - 2)).getHome() != null) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected last two references, got not null home address");
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_orderByNullsLast failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_selectID() throws Exception {
		String result;
		boolean pass = false;

		try {
			logger.log(Logger.Level.TRACE, "SELECT ID() Executing Query");
			Query query = getEntityManager()
					.createQuery("SELECT ID(c) from Customer c WHERE c.id = :idParam");
			query.setParameter("idParam", customerRef[0].getId());
			result = (String) query.getSingleResult();

			if (customerRef[0].getId().equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,"Did not get expected results.  Expected Customer id |" + customerRef[0].getId() + "| but, got |" + result + "|");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_selectID failed");
	}

	@SetupMethod(name = "setupAliasData")
	@Test
	public void test_selectWhereID() throws Exception {
		String result;
		boolean pass = false;

		try {
			logger.log(Logger.Level.TRACE, "SELECT ID() Executing Query");
			Query query = getEntityManager()
					.createQuery("SELECT ID(c) from Customer c WHERE ID(c) = :idParam");
			query.setParameter("idParam", customerRef[0].getId());
			result = (String) query.getSingleResult();

			if (customerRef[0].getId().equals(result)) {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,"Did not get expected results.  Expected Customer id |" + customerRef[0].getId() + "| but, got |" + result + "|");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}

		if (!pass)
			throw new Exception("test_selectWhereID failed");
	}

	@Test
	public void test_selectVERSION() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		final int ID = 1;
		Member member = new Member(ID, "Member 1", true, BigInteger.valueOf(1000L));

		//Try cleanup first
		try {
			getEntityTransaction().begin();
			Member member1 = getEntityManager().find(Member.class, ID);
			if (member1 != null) {
				getEntityManager().remove(member1);
				getEntityTransaction().commit();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entity:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
		//Prepare test data and test created version after commit by JPQLs
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(member);

			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			getEntityTransaction().commit();

			//First test
			logger.log(Logger.Level.TRACE, "SELECT VERSION() Executing Query");
			Query query1 = getEntityManager()
					.createQuery("SELECT VERSION(m) from Member m WHERE m.memberId = :memberIdParam");
			query1.setParameter("memberIdParam", member.getMemberId());
			Integer version1 = (Integer)query1.getSingleResult();
			if (version1 != null && version1.equals(member.getVersion())) {
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR,"Did not get expected results.  Expected Member version |" + member.getVersion() + "| but, got |" + version1 + "|");
			}

			//Second test
			logger.log(Logger.Level.TRACE, "SELECT VERSION() WHERE version = VERSION() Executing Query");
			Query query2 = getEntityManager()
					.createQuery("SELECT VERSION(m) from Member m WHERE m.memberId = :memberIdParam AND m.version = :memberVersionParam");
			query2.setParameter("memberIdParam", member.getMemberId());
			query2.setParameter("memberVersionParam", member.getVersion());
			Integer version2 = (Integer)query2.getSingleResult();
			if (version2 != null && version2.equals(member.getVersion())) {
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR,"Did not get expected results.  Expected Member version |" + member.getVersion() + "| but, got |" + version2 + "|");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass1 = false;
			pass2 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}
		if (!pass1 || !pass2) {
			throw new Exception("getVersionTest failed");
		}
	}
}
