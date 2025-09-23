/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.se.repeatable.secondarytable;

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

	private static final long serialVersionUID = 22L;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFile = {};
		String[] classes = { pkgName + "HardwareProduct", pkgName + "Product", pkgName + "SoftwareProduct" };
		return createDeploymentJar("jpa_se_repeatable_secondarytable.jar", pkgNameWithoutSuffix, (String[]) classes,
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
	 * @testName: subClassInheritsCacheableTrue
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:189; PERSISTENCE:JAVADOC:190;
	 * PERSISTENCE:SPEC:1979; PERSISTENCE:SPEC:1980;
	 * 
	 * @test_Strategy: follow se/cache/inherit but without @SecondaryTables
	 */
	@Test
	public void subClassInheritsCacheableTrue() throws Exception {
		Cache cache;
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		if (cachingSupported) {
			try {

				EntityManager em2 = getEntityManager();
				EntityTransaction et = getEntityTransaction();

				et.begin();

				Product product = new Product("1", 101);
				em2.persist(product);
				logger.log(Logger.Level.TRACE, "persisted Product " + product);

				SoftwareProduct sp = new SoftwareProduct();
				sp.setId("2");
				sp.setRevisionNumber(1D);
				sp.setQuantity(202);
				em2.persist(sp);
				logger.log(Logger.Level.TRACE, "persisted SoftwareProduct " + sp);

				HardwareProduct hp = new HardwareProduct();
				hp.setId("3");
				hp.setModelNumber(3);
				hp.setQuantity(303);
				em2.persist(hp);
				logger.log(Logger.Level.TRACE, "persisted HardwareProduct " + hp);

				em2.flush();
				et.commit();

				EntityManagerFactory emf = getEntityManagerFactory();
				cache = emf.getCache();

				if (cache != null) {
					boolean b1 = cache.contains(Product.class, "1");
					if (b1) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b1 + ", therefore cache does contain Product " + product);
						pass1 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b1 + ", therefore cache does not contain Product " + product);
					}
					boolean b2 = cache.contains(SoftwareProduct.class, "2");
					if (!b2) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b2 + ", therefore cache does not contain SoftwareProduct " + sp);
						pass2 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b2 + ", therefore cache does contain SoftwareProduct " + sp);
					}
					boolean b3 = cache.contains(HardwareProduct.class, "3");
					if (b3) {
						logger.log(Logger.Level.TRACE,
								"Cache returned: " + b3 + ", therefore cache does contain HardwareProduct " + hp);
						pass3 = true;
					} else {
						logger.log(Logger.Level.ERROR,
								"Cache returned: " + b3 + ", therefore cache does not contain HardwareProduct " + hp);
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
			throw new Exception("subClassInheritsCacheableTrue failed");
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
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_DETAILS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
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
