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

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.schema30.Customer;
import ee.jakarta.tck.persistence.common.schema30.Order;
import ee.jakarta.tck.persistence.common.schema30.UtilSetup;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Bindable;

public class Client4 extends UtilSetup {

	private static final Logger logger = (Logger) System.getLogger(Client4.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client4.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		classes = concat(getSchema30classes(), classes);
		return createDeploymentJar("jpa_core_criteriaapi_CriteriaQuery4.jar", pkgNameWithoutSuffix, classes);
	}

	/*
	 * @testName: fromGetStringIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1030;
	 * 
	 * @test_Strategy: Verify get(String) returns an exception for a basic type
	 */
	@Test
	public void fromGetStringIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
			try {
				cquery.from(A.class).get("value").get("value2");
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("fromGetStringIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: pathGetStringIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1100;
	 * 
	 * @test_Strategy: Verify get(String) returns an exception for a basic type
	 */
	@Test
	public void pathGetStringIllegalStateExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
			Path path = cquery.from(A.class).get("value");
			try {
				path.get("value2");
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("pathGetStringIllegalStateExceptionTest failed");
		}
	}

	/*
	 * @testName: fromGetStringIllegalArgumentExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1031;
	 * 
	 * @test_Strategy: Verify get(String) returns an exception
	 *
	 */
	@Test
	public void fromGetStringIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		try {
			getEntityTransaction().begin();
			CriteriaQuery<A> cquery = cbuilder.createQuery(A.class);
			try {
				cquery.from(A.class).get("doesnotexist");
				logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
			} catch (IllegalArgumentException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
				pass = true;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("fromGetStringIllegalArgumentExceptionTest failed");
		}
	}

	/*
	 * @testName: fromGetModelTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1032;
	 * 
	 * @test_Strategy: Verify getModel() returns correct result.
	 *
	 */
	@Test
	public void fromGetModelTest() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

		String expected = "ee.jakarta.tck.persistence.common.schema30.Customer";
		try {
			getEntityTransaction().begin();
			CriteriaQuery<Customer> cquery = cbuilder.createQuery(Customer.class);
			Bindable bind = cquery.from(Customer.class).getModel();
			String name = bind.getBindableJavaType().getName();
			if (name.equals(expected)) {
				logger.log(Logger.Level.TRACE, "Received expected name:" + name);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + expected + ", actual:" + name);
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("fromGetModelTest failed");
		}
	}

	/*
	 * @testName: pathGetIllegalArgumentException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:1101
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void pathGetIllegalArgumentException() throws Exception {
		boolean pass = false;

		CriteriaBuilder cbuilder = getEntityManagerFactory().getCriteriaBuilder();
		try {
			CriteriaQuery<String> cquery = cbuilder.createQuery(String.class);
			Root<Customer> customer = cquery.from(Customer.class);
			customer.get("doesnotexist");
			logger.log(Logger.Level.ERROR, "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException iae) {
			logger.log(Logger.Level.TRACE, "Received expected IllegalArgumentException");
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: ", e);
		}

		if (!pass) {
			throw new Exception("pathGetIllegalArgumentException failed");
		}
	}

	/*
	 * @testName: executeUpdateIllegalStateException1Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:551
	 * 
	 * @test_Strategy: Use the
	 * CriteriaBuilder.createQuery(CriteriaQuery).executeUpdate() for a CriteriaAPI
	 * and verify a IllegalStateException is thrown
	 *
	 */
	@Test
	public void executeUpdateIllegalStateException1Test() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();

			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

			CriteriaQuery<Order> cquery = cb.createQuery(Order.class);
			Root<Order> order = cquery.from(Order.class);
			cquery.select(order);
			cquery.where(cb.equal(order.get("id"), "1"));
			getEntityManager().createQuery(cquery).executeUpdate();
			logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");

		} catch (IllegalStateException ise) {
			pass = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("executeUpdateIllegalStateException1Test failed");
	}
}
