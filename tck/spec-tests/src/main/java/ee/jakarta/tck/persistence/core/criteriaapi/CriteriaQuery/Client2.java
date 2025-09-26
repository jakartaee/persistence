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

package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaQuery;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.SetupMethod;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.UtilAliasData;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

public class Client2 extends UtilAliasData {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		classes = concat(getSchema30classes(), classes);
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery2.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: selectIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:931
	 *
	 * @test_Strategy:
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void selectIllegalArgumentException() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		CriteriaQuery cquery = cbuilder.createQuery();
		if (cquery != null) {
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Creating select using selection items with the same alias");
			try {
				CompoundSelection<java.lang.Object[]> c = cbuilder.array(customer.get("id").alias("SAMEALIAS"),
						customer.get("name").alias("SAMEALIAS"));

				cquery.select(c);

				logger.log(Logger.Level.ERROR, "Did not thrown IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}

		if (!pass) {
			throw new Exception("selectIllegalArgumentException failed");

		}
	}

	/*
	 * @testName: multiselectIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:925; PERSISTENCE:JAVADOC:927
	 *
	 * @test_Strategy: Create a multiselect using selection items with the same
	 * alias and verify exception is thrown
	 *
	 */
	@SetupMethod(name = "setupAliasData")
	@Test
	public void multiselectIllegalArgumentExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();

		logger.log(Logger.Level.INFO, "Testing multiselect invalid item");
		CriteriaQuery<Tuple> cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Creating multiselect using selection array of items that do not exist");
			try {
				cquery.multiselect(customer.get("doesnotexist").alias("ALIAS1"),
						customer.get("doesnotexist2").alias("ALIAS2"));
				logger.log(Logger.Level.ERROR, "Did not thrown IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass1 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}

		logger.log(Logger.Level.INFO, "Testing multiselect selection[]");
		cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Creating multiselect using selection array of items with the same alias");
			Selection[] selection = { customer.get("id").alias("SAMEALIAS"), customer.get("name").alias("SAMEALIAS") };

			try {
				cquery.multiselect(selection);
				logger.log(Logger.Level.ERROR, "Did not thrown IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "received expected IllegalArgumentException");
				pass2 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}
		logger.log(Logger.Level.INFO, "Testing multiselect List");
		cquery = cbuilder.createTupleQuery();
		if (cquery != null) {
			logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
			Root<Customer> customer = cquery.from(Customer.class);

			logger.log(Logger.Level.TRACE, "Creating multiselect using selection items with the same alias");
			try {
				List list = new ArrayList();
				list.add(customer.get("id").alias("SAMEALIAS"));
				list.add(customer.get("name").alias("SAMEALIAS"));

				cquery.multiselect(list);
				logger.log(Logger.Level.ERROR, "Did not thrown IllegalArgumentException");
			} catch (IllegalArgumentException iae) {
				logger.log(Logger.Level.TRACE, "received expected IllegalArgumentException");
				pass3 = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
			}
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("multiselectIllegalArgumentExceptionTest failed");
		}
	}
}
