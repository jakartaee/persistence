/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.cache.xml.enableselective;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = { ORM_XML };
		String[] classes = { pkgName + "Customer", pkgName + "Department", pkgName + "Order" };
		return createDeploymentJar("jpa_se_cache_xml_enableselective.jar", pkgNameWithoutSuffix, (String[]) classes,
				PERSISTENCE_XML, xmlFile);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: containsTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1498; PERSISTENCE:SPEC:1498.1;
	 * PERSISTENCE:SPEC:1498.2; PERSISTENCE:SPEC:1866; PERSISTENCE:SPEC:1978;
	 * 
	 * @test_Strategy: Using the xml shared-cache-mode element with a value of
	 * ENABLE_SELECTIVE persist some entities with various Cachable values and
	 * verify the behavior of whether or not each is retained or not in the cache.
	 */
	@Test
	public void containsTest() throws Exception {
		Cache cache;
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		if (cachingSupported) {
			try {

				EntityManager em2 = getEntityManager();
				EntityTransaction et = getEntityTransaction();

				et.begin();

				Order order = new Order(1, 101);
				em2.persist(order);
				logger.log(Logger.Level.TRACE, "persisted Order " + order);

				Customer cust = new Customer("1", "one");
				em2.persist(cust);
				logger.log(Logger.Level.TRACE, "persisted Customer " + cust);

				Department dept = new Department(1, "one");
				em2.persist(dept);
				logger.log(Logger.Level.TRACE, "persisted Department " + dept);

				em2.flush();
				et.commit();

				EntityManagerFactory emf = getEntityManagerFactory();
				cache = emf.getCache();

				if (cache != null) {
					boolean b1 = cache.contains(Order.class, 1);
					if (b1) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b1 + ", therefore cache does contain order " + order);
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b1 + ", therefore cache does not contain order " + order);
					}
					boolean b2 = cache.contains(Customer.class, "1");
					if (!b2) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b2 + ", therefore cache does not contain Customer " + cust);
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b2 + ", therefore cache does contain Customer " + cust);
					}
					boolean b3 = cache.contains(Department.class, 1);
					if (!b3) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b3 + ", therefore cache does not contain Department " + dept);
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b3 + ", therefore cache does contain Department " + dept);
					}
				} else {
					logger.log(Logger.Level.ERROR, "Cache returned was null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.INFO, "Cache not supported, bypassing test");
			pass1 = true;
			pass2 = true;
			pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("containsTest failed");
		}

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
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUSTOMER_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
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
