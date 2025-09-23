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

package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;
import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Alias;
import ee.jakarta.tck.persistence.common.schema30.Alias_;
import ee.jakarta.tck.persistence.common.schema30.UtilAliasOnlyData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class Client5 extends UtilAliasOnlyData {

	private static final Logger logger = (Logger) System.getLogger(Client5.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client5.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery5.jar", pkgNameWithoutSuffix, classes);
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
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void queryTest24() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases who have match: stevie");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			cquery.where(cbuilder.equal(alias.get(Alias_.alias), cbuilder.concat(cbuilder.literal("ste"), "vie")));
			cquery.select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(aliasRef.length);
			List<Alias> alist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "14";
			if (!checkEntityPK(alist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + alist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest24 failed");
		}
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
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void queryTest25() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases containing the substring: iris");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			cquery.where(cbuilder.equal(alias.get(Alias_.alias),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"),
							cbuilder.parameter(Integer.class, "int2"), cbuilder.parameter(Integer.class, "int3"))));
			cquery.select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setParameter("string1", "iris");
			tquery.setParameter("int2", Integer.valueOf(1));
			tquery.setParameter("int3", Integer.valueOf(4));
			tquery.setMaxResults(aliasRef.length);
			List<Alias> alist = tquery.getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "20";
			if (!checkEntityPK(alist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 1 reference, got: " + alist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest25 failed");
		}
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
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void queryTest26() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find aliases whose alias name is greater than 4 characters");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			cquery.where(cbuilder.gt(cbuilder.length(alias.get(Alias_.alias)), 4));
			cquery.select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(aliasRef.length);
			List<Alias> alist = tquery.getResultList();

			expectedPKs = new String[7];
			expectedPKs[0] = "8";
			expectedPKs[1] = "10";
			expectedPKs[2] = "13";
			expectedPKs[3] = "14";
			expectedPKs[4] = "18";
			expectedPKs[5] = "28";
			expectedPKs[6] = "29";
			if (!checkEntityPK(alist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 7 references, got: " + alist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest26 failed");
		}
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
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void queryTest28() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find all aliases who contain the string: ev in their alias name");
			CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
			Root<Alias> alias = cquery.from(Alias.class);
			cquery.where(cbuilder.equal(cbuilder.locate(alias.get(Alias_.alias), "ev"), 3));
			cquery.select(alias);
			TypedQuery<Alias> tquery = getEntityManager().createQuery(cquery);
			tquery.setMaxResults(aliasRef.length);
			List<Alias> alist = tquery.getResultList();

			expectedPKs = new String[3];
			expectedPKs[0] = "13";
			expectedPKs[1] = "14";
			expectedPKs[2] = "18";
			if (!checkEntityPK(alist, expectedPKs)) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 3 references, got: " + alist.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception("queryTest28 failed");
		}
	}

}
