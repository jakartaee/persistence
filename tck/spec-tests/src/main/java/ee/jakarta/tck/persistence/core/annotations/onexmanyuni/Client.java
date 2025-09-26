/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.onexmanyuni;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	final private static long ORDER1_ID = 786l;

	final private static long ORDER2_ID = 787l;

	final private static long ORDER3_ID = 788l;

	final private static long ORDER4_ID = 789l;

	final private static double COST1 = 53;

	final private static double COST2 = 540;

	final private static double COST3 = 155;

	final private static double COST4 = 256;

	final private static long CUST1_ID = 2l;

	final private static long CUST2_ID = 4l;

	final private static String CUST1_NAME = "Ross";

	final private static String CUST2_NAME = "Joey";

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Customer1", pkgName + "RetailOrder2" };
		return createDeploymentJar("jpa_core_annotations_onexmanyuni.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: oneXmanyUniJoinColumn
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039;
	 * PERSISTENCE:SPEC:1040; PERSISTENCE:SPEC:1041; PERSISTENCE:SPEC:1042;
	 * PERSISTENCE:SPEC:1046; PERSISTENCE:SPEC:1048; PERSISTENCE:SPEC:1097;
	 * PERSISTENCE:SPEC:1214; PERSISTENCE:SPEC:1243; PERSISTENCE:JAVADOC:374;
	 * 
	 * @test_Strategy: The two entities "Customer1" and "RetailOrder2" have
	 * One-to-Many relationship.
	 * 
	 */
	@Test
	public void oneXmanyUniJoinColumn() throws Exception {

		EntityManager em = getEntityManager();
		EntityTransaction tx = getEntityTransaction();
		Customer1 customer1 = createCustomer(CUST1_ID, CUST1_NAME);
		Customer1 customer2 = createCustomer(CUST2_ID, CUST2_NAME);

		final RetailOrder2 order1 = createOrder(ORDER1_ID, COST1);
		final RetailOrder2 order2 = createOrder(ORDER2_ID, COST2);
		final RetailOrder2 order3 = createOrder(ORDER3_ID, COST3);
		final RetailOrder2 order4 = createOrder(ORDER4_ID, COST4);

		try {
			tx.begin();
			em.persist(customer1);
			em.persist(customer2);
			em.flush();
			em.persist(order1);
			em.persist(order2);
			em.persist(order3);
			em.persist(order4);
			customer1.addOrder(order1);
			customer1.addOrder(order2);
			customer2.addOrder(order3);
			customer2.addOrder(order4);
			em.flush();
			logger.log(Logger.Level.TRACE, "Test Passed");
		} catch (Exception e) {

			throw new Exception("Test failed" + e);
		} finally {
			em.remove(order1);
			em.remove(order2);
			em.remove(order3);
			em.remove(order4);
			em.remove(customer1);
			em.remove(customer2);
			tx.commit();
		}

	}

	private RetailOrder2 createOrder(final long id, final double cost) {
		RetailOrder2 order = new RetailOrder2();
		order.setId(id);
		order.setCost(cost);
		return order;
	}

	private Customer1 createCustomer(final long id, final String name) {
		Customer1 customer = new Customer1();
		customer.setId(id);
		customer.setName(name);
		return customer;
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from CUSTOMER1").executeUpdate();
			getEntityManager().createNativeQuery("Delete from RETAILORDER2").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
