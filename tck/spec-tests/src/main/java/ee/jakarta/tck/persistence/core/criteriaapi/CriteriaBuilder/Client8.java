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

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Trim;
import ee.jakarta.tck.persistence.common.schema30.UtilTrimData;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.Trimspec;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;

public class Client8 extends UtilTrimData {

	private static final Logger logger = (Logger) System.getLogger(Client8.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client8.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaBuilder8.jar", pkgNameWithoutSuffix, classes);

	}

	/*
	 * @testName: trimExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:866
	 *
	 * @test_Strategy: Select trim(both from t.name) from Trim t where
	 * trim(t.name)='David R. Vincent'
	 *
	 *
	 */
	@SetupMethod(name = "setupTrimData")
	@Test
	public void trimExpTest() throws Exception {
		boolean pass = false;
		final String expected = " David R. Vincent ";
		final String expected2 = "David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();
		/*
		 * Trim tTrim = getEntityManager().find(Trim.class, "19");
		 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
		 * (tTrim.getName().equals(expected)) {
		 * logger.log(Logger.Level.TRACE,"Received expected find result: " +
		 * tTrim.getName()); pass1 = true; } else {
		 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
		 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
		 * tTrim.getName() + "|"); }
		 * 
		 */
		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Trim> trim = cquery.from(Trim.class);

			// Get Metamodel from Root
			EntityType<Trim> trim_ = trim.getModel();

			cquery.where(cb.equal(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
					cb.literal(expected.trim())));
			cquery.select(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expected2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = |" + expected2 + "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimExpTest failed");

		}
	}

	/*
	 * testName: trimLeadingExpTest assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * test_Strategy: Select trim(leading from t.name) from Trim t where t.name= '
	 * David R. Vincent '
	 *
	 *
	 */
	/*
	 * @SetupMethod(name = "setupTrimData") // TODO - once TRIM issues are resolved,
	 * re-enable this test public void trimLeadingExpTest() throws Exception {
	 * boolean pass = false; final String expected = " David R. Vincent "; final
	 * String expected2 = "David R. Vincent             ";
	 * 
	 * CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
	 * 
	 * 
	 * getEntityTransaction().begin();
	 * 
	 * Trim tTrim = getEntityManager().find(Trim.class, "19");
	 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
	 * (!tTrim.getName().equals(expected)) {
	 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
	 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
	 * tTrim.getName() + "|"); }
	 * 
	 * 
	 * CriteriaQuery<String> cquery = cb.createQuery(String.class); if (cquery !=
	 * null) { Root<Trim> trim = cquery.from(Trim.class);
	 * 
	 * 
	 * //Get Metamodel from Root EntityType<Trim> trim_ = trim.getModel();
	 * 
	 * cquery.where(cb.equal( trim.get(trim_.getSingularAttribute("name",
	 * String.class)), cb.literal(expected)));
	 * cquery.select(cb.trim(Trimspec.LEADING,
	 * trim.get(trim_.getSingularAttribute("name", String.class))));
	 * 
	 * TypedQuery<String> tq = getEntityManager().createQuery(cquery);
	 * 
	 * String result = tq.getSingleResult();
	 * 
	 * if (result.equals(expected2)) {
	 * logger.log(Logger.Level.TRACE,"Received expected result:|" + result + "|");
	 * pass = true; } else {
	 * logger.log(Logger.Level.ERROR,"Mismatch in received results - expected = |" +
	 * expected2 + "|, received = |" + result + "|"); }
	 * 
	 * } else {
	 * logger.log(Logger.Level.ERROR,"Failed to get Non-null Criteria Query"); }
	 * 
	 * getEntityTransaction().commit();
	 * 
	 * if (!pass) { throw new Exception("trimLeadingExpTest failed");
	 * 
	 * } }
	 */

	/*
	 * @testName: trimTrailingCharExpTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:867
	 *
	 * @test_Strategy: Select trim(trailing from t.name) from Trim t where
	 * trim(t.name)= 'David R. Vincent'
	 *
	 */
	@SetupMethod(name = "setupTrimData")
	@Test
	public void trimTrailingCharExpTest() throws Exception {
		boolean pass = false;
		final String expected = " David R. Vincent ";
		final String expected2 = " David R. Vincent";

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		getEntityTransaction().begin();

		/*
		 * Trim tTrim = getEntityManager().find(Trim.class, "19");
		 * logger.log(Logger.Level.TRACE,"Trim(19):" + tTrim.toString()); if
		 * (tTrim.getName().equals(expected)) {
		 * logger.log(Logger.Level.TRACE,"Received expected find result: " +
		 * tTrim.getName()); pass1 = true; } else {
		 * logger.log(Logger.Level.ERROR,"Name returned by find does not match expected"
		 * ); logger.log(Logger.Level.ERROR,"Expected:|" + expected + "|, actual:|" +
		 * tTrim.getName() + "|"); }
		 */

		CriteriaQuery<String> cquery = cb.createQuery(String.class);
		if (cquery != null) {
			Root<Trim> trim = cquery.from(Trim.class);

			// Get Metamodel from Root
			EntityType<Trim> trim_ = trim.getModel();

			cquery.where(cb.equal(cb.trim(trim.get(trim_.getSingularAttribute("name", String.class))),
					cb.literal(expected.trim())));
			cquery.select(cb.trim(Trimspec.TRAILING, trim.get(trim_.getSingularAttribute("name", String.class))));

			TypedQuery<String> tq = getEntityManager().createQuery(cquery);

			String result = tq.getSingleResult();

			if (result.equals(expected2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:|" + result + "|");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Mismatch in received results - expected = |" + expected2 + "|, received = |" + result + "|");
			}

		} else {
			logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
		}

		getEntityTransaction().commit();

		if (!pass) {
			throw new Exception("trimTrailingCharExpTest failed");

		}
	}

}
