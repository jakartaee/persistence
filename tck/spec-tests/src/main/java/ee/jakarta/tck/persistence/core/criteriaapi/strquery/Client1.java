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

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.UtilSetup;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;

public class Client1 extends UtilSetup {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_strquery1.jar", pkgNameWithoutSuffix, classes);
	}

	/* Run test */
	/*
	 * @testName: joinTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1035; PERSISTENCE:JAVADOC:1036;
	 * PERSISTENCE:JAVADOC:1037
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void joinTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;

		try {
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			logger.log(Logger.Level.INFO, "Testing default getJoinType");
			JoinType jt = customer.join("aliases").getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected:" + jt.name());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + JoinType.INNER.name() + ", actual:" + jt);
			}
			cquery = null;
			logger.log(Logger.Level.INFO, "Testing INNER getJoinType");
			cquery = cbuilder.createQuery(Customer.class);
			customer = cquery.from(Customer.class);
			jt = customer.join("aliases", JoinType.INNER).getJoinType();
			if (jt.equals(JoinType.INNER)) {
				logger.log(Logger.Level.TRACE, "Received expected:" + jt.name());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + JoinType.INNER.name() + ", actual:" + jt);
			}
			cquery = null;
			logger.log(Logger.Level.INFO, "Testing LEFT getJoinType");
			cquery = cbuilder.createQuery(Customer.class);
			customer = cquery.from(Customer.class);
			jt = customer.join("aliases", JoinType.LEFT).getJoinType();
			if (jt.equals(JoinType.LEFT)) {
				logger.log(Logger.Level.TRACE, "Received expected:" + jt.name());
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + JoinType.LEFT.name() + ", actual:" + jt);
			}
			cquery = null;
			logger.log(Logger.Level.INFO, "Testing INNER getAttribute");
			cquery = cbuilder.createQuery(Customer.class);
			customer = cquery.from(Customer.class);
			Attribute attr = customer.join("aliases").getAttribute();
			if (attr.getName().equals("aliases")) {
				logger.log(Logger.Level.TRACE, "Received expected:" + attr.getName());
				pass4 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:aliases, actual:" + attr.getName());
			}
			cquery = null;
			logger.log(Logger.Level.INFO, "Testing getParent");
			cquery = cbuilder.createQuery(Customer.class);
			customer = cquery.from(Customer.class);
			From from = customer.join("aliases").getParent();
			if (from.getClass().getName().equals(customer.getClass().getName())) {
				logger.log(Logger.Level.TRACE, "Received expected:" + from.getClass().getName());
				pass5 = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected:" + customer.getClass().getName() + ", actual:" + from.getClass().getName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		if (!pass1 || !pass2 || !pass3 || !pass4 || !pass5) {
			throw new Exception("joinTest failed");
		}
	}

	/*
	 * @testName: fetchStringAndStringJoinTypeIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:979; PERSISTENCE:JAVADOC:1022;
	 * PERSISTENCE:JAVADOC:981; PERSISTENCE:JAVADOC:1024;
	 * 
	 * @test_Strategy: JOIN FETCH for 1-1 relationship. Prefetch an attribute that
	 * does not exist .
	 */
	@Test
	public void fetchStringAndStringJoinTypeIllegalArgumentException() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
		logger.log(Logger.Level.INFO, "Testing String");

		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.fetch("doesnotexist");
			logger.log(Logger.Level.ERROR, "did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception", e);
		}

		logger.log(Logger.Level.INFO, "Testing String, JoinType");

		try {
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.fetch("doesnotexist", JoinType.INNER);
			logger.log(Logger.Level.ERROR, "did not throw IllegalArgumentException");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception:", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("fetchStringAndStringJoinTypeIllegalArgumentException failed");
		}
	}

}
