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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Alias;
import ee.jakarta.tck.persistence.common.schema30.UtilAliasOnlyData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;

public class Client4 extends UtilAliasOnlyData {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client4.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder4.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: greaterThanExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:774
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder
					.greaterThan(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("greaterThanExpNumTest failed");

		}
	}

	/*
	 * @testName: greaterThanExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:773
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		;
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.greaterThan(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
							cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("greaterThanExpExpTest failed");

		}
	}

	/*
	 * @testName: greaterThanOrEqualToExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:795; PERSISTENCE:JAVADOC:776
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanOrEqualToExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;

		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.greaterThanOrEqualTo(
					cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("greaterThanOrEqualToExpNumTest failed");

		}
	}

	/*
	 * @testName: greaterThanOrEqualToExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:775
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void greaterThanOrEqualToExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];

		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.greaterThanOrEqualTo(
					cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();
			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("greaterThanOrEqualToExpExpTest failed");

		}
	}

	/*
	 * @testName: gtExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:779
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void gtExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.gt(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("gtExpNumTest failed");

		}
	}

	/*
	 * @testName: gtExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:778
	 *
	 * @test_Strategy: Select a From Alias a WHERE LENGTH(a.alias) > 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void gtExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[7];
		;
		expected[0] = aliasRef[7].getId();
		expected[1] = aliasRef[9].getId();
		expected[2] = aliasRef[12].getId();
		expected[3] = aliasRef[13].getId();
		expected[4] = aliasRef[17].getId();
		expected[5] = aliasRef[27].getId();
		expected[6] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.gt(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("gtExpExpTest failed");

		}
	}

	/*
	 * @testName: geExpNumTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:772
	 *
	 * @test_Strategy: Select Distinct a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void geExpNumTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;
		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(
					cbuilder.ge(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 4));
			cquery.distinct(true);

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("geExpNumTest failed");

		}
	}

	/*
	 * @testName: geExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:771
	 *
	 * @test_Strategy: Select Distinct a From Alias a WHERE LENGTH(a.alias) >= 4
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void geExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[13];
		;
		expected[0] = aliasRef[2].getId();
		expected[1] = aliasRef[3].getId();
		expected[2] = aliasRef[7].getId();
		expected[3] = aliasRef[9].getId();
		expected[4] = aliasRef[12].getId();
		expected[5] = aliasRef[13].getId();
		expected[6] = aliasRef[17].getId();
		expected[7] = aliasRef[19].getId();
		expected[8] = aliasRef[22].getId();
		expected[9] = aliasRef[23].getId();
		expected[10] = aliasRef[24].getId();
		expected[11] = aliasRef[27].getId();
		expected[12] = aliasRef[28].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.ge(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))),
					cbuilder.literal(4)));
			cquery.distinct(true);

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("geExpExpTest failed");

		}
	}

	/*
	 * @testName: substringExpIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:850
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), 1)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("substringExpIntTest failed");

		}
	}

	/*
	 * @testName: substringExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:849
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), cbuilder.literal(1))));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("substringExpExpTest failed");

		}
	}

	/*
	 * @testName: substringExpIntIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:852
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1, 4)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpIntIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.substring(cbuilder.parameter(String.class, "string1"), 1, 4)));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("substringExpIntIntTest failed");

		}
	}

	/*
	 * @testName: substringExpExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:851
	 *
	 * @test_Strategy: Select a From Alias a WHERE a.alias = SUBSTRING("iris", 1, 4)
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void substringExpExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[19].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.select(alias);
			cquery.where(cbuilder.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)), cbuilder
					.substring(cbuilder.parameter(String.class, "string1"), cbuilder.literal(1), cbuilder.literal(4))));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);
			tq.setParameter("string1", "iris");

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("substringExpExpExpTest failed");

		}
	}

	/*
	 * @testName: upper
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:874
	 *
	 * @test_Strategy: Select upper(a.alias) From Alias a WHERE a.alias = 'iris'
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void upper() throws Exception {
		final String expectedResult = "IRIS";
		boolean pass = false;

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();

			cquery.where(cb.equal(alias.get(Alias_.getSingularAttribute("alias", String.class)), cb.literal("iris")));
			cquery.select(cb.upper(alias.get(Alias_.getSingularAttribute("alias", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);
			String result = tq.getSingleResult();

			if (result != null) {
				if (result.equals(expectedResult)) {
					logger.log(Logger.Level.TRACE, "Successfully returned expected results");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Mismatch in received results - expected = " + expectedResult + " received = " + result);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Missing expected result");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("upper test failed");

		}
	}

	/*
	 * @testName: length
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:795
	 *
	 * @test_Strategy: Select a From Alias a WHERE length (a.alias) = 9
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void length() throws Exception {
		boolean pass = false;

		String[] expected = new String[1];
		expected[0] = aliasRef[27].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			cquery.where(
					cbuilder.equal(cbuilder.length(alias.get(Alias_.getSingularAttribute("alias", String.class))), 9));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("length test failed");

		}
	}

	/*
	 * @testName: locateExpStringTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:809
	 *
	 * @test_Strategy: Select a from Alias a where LOCATE('ev', a.alias) = 3
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpStringTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder
					.equal(cbuilder.locate(alias.get(Alias_.getSingularAttribute("alias", String.class)), "ev"), 3));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("locateExpStringTest failed");

		}
	}

	/*
	 * @testName: locateExpExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:808
	 *
	 * @test_Strategy: Select a from Alias a where LOCATE('ev', a.alias) = 3
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpExpTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null) {
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);
			cquery.where(cbuilder.equal(cbuilder.locate(alias.get(Alias_.getSingularAttribute("alias", String.class)),
					cbuilder.literal("ev")), 3));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("locateExpExpTest failed");

		}
	}

	/*
	 * @testName: locateExpressionExpressionExpressionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:810
	 *
	 * @test_Strategy: SELECT a FROM ALIAS a WHERE (LOCATE(ev, a.alias, 1) > 0)
	 *
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpressionExpressionExpressionTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null)

		{
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			Expression exp1 = alias.get(Alias_.getSingularAttribute("alias", String.class));
			Expression exp2 = cbuilder.literal("ev");
			Expression exp3 = cbuilder.toInteger(cbuilder.literal(1));

			cquery.where(cbuilder.gt(cbuilder.locate(exp1, exp2, exp3), 0));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("locateExpressionExpressionExpressionTest failed");

		}
	}

	/*
	 * @testName: locateExpressionStringIntTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:811
	 *
	 * @test_Strategy: SELECT a FROM ALIAS a WHERE (LOCATE(ev, a.alias, 1) > 0)
	 *
	 *
	 */
	@SetupMethod(name = "setupAliasOnlyData")
	@Test
	public void locateExpressionStringIntTest() throws Exception {
		boolean pass = false;

		String[] expected = new String[3];
		expected[0] = aliasRef[12].getId();
		expected[1] = aliasRef[13].getId();
		expected[2] = aliasRef[17].getId();

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		CriteriaQuery<Alias> cquery = cbuilder.createQuery(Alias.class);
		if (cquery != null)

		{
			Root<Alias> alias = cquery.from(Alias.class);

			// Get Metamodel from Root
			EntityType<Alias> Alias_ = alias.getModel();
			cquery.select(alias);

			Expression exp1 = alias.get(Alias_.getSingularAttribute("alias", String.class));

			cquery.where(cbuilder.gt(cbuilder.locate(exp1, "ev", 1), 0));

			TypedQuery<Alias> tq = getEntityManager().createQuery(cquery);

			List<Alias> result = tq.getResultList();

			List<Integer> actual = new ArrayList<Integer>();
			for (Alias a : result) {
				actual.add(Integer.parseInt(a.getId()));
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
			throw new Exception("locateExpressionExpressionExpressionTest failed");

		}
	}

}
