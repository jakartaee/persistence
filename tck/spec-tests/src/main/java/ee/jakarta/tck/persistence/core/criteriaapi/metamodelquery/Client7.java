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
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Customer_;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilSetup;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;


public class Client7 extends UtilSetup {

	private static final Logger logger = (Logger) System.getLogger(Client7.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client7.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = getSchema30classes();
		return createDeploymentJar("jpa_core_criteriaapi_metamodelquery7.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: getCorrelatedJoinsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1179;
	 * 
	 * @test_Strategy: Test getting correlated joins from subquery.
	 */
	@Test
	public void getCorrelatedJoinsTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Root<Customer> customer = cquery.from(Customer.class);
			cquery.select(customer);
			Subquery<Order> sq = cquery.subquery(Order.class);
			Set cJoins = sq.getCorrelatedJoins();
			if (cJoins != null) {
				if (cJoins.size() == 0) {
					logger.log(Logger.Level.ERROR,
							"Received expected 0 correlated joins from subquery.getCorrelatedJoins() when none exist");

					// correlate subquery
					Join<Customer, Order> sqo = sq.correlate(customer.join(Customer_.orders));
					sq.select(sqo);
					cJoins = sq.getCorrelatedJoins();
					if (cJoins != null) {
						if (cJoins.size() == 1) {
							logger.log(Logger.Level.TRACE,
									"Received expected 1 correlated join from subquery.getCorrelatedJoins()");
							pass = true;
						} else {
							logger.log(Logger.Level.ERROR, "Received " + cJoins.size()
									+ " correlated joins from subquery.getCorrelatedJoins() when 1 exist");

						}
					} else {
						logger.log(Logger.Level.ERROR,
								"Received null from subquery.getCorrelatedJoins() when 1 correlated join exists");

					}
				} else {
					logger.log(Logger.Level.ERROR, "Received " + cJoins.size()
							+ " unexpected correlated joins from subquery.getCorrelatedJoins() when non exist");

				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Received null from subquery.getCorrelatedJoins() instead of empty set when non exist");

			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Caught unexpected exception:", e);

		}

		if (!pass) {
			throw new Exception(" getCorrelatedJoinsTest failed");
		}
	}

}
