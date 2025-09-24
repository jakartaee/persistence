/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.entityManagerFactory;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.metamodel.Metamodel;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	Properties props = null;

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Member_", pkgName + "Member", pkgName + "Order_", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManagerFactory2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setupNoData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupNoData");
		try {
			super.setup();
			createDeployment();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupNoData() throws Exception {
		try {
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: autoCloseableTest
	 *
	 * @assertion_ids: PERSISTENCE:SPEC:2517;
	 *
	 * @test_Strategy: Create EntityManagerFactory in try with resources block and
	 * verify whether it's open inside and outside of the try block.
	 */
	@Test
	public void autoCloseableTest() throws Exception {
		EntityManagerFactory emf = null;
		try (final EntityManagerFactory emfLocal = Persistence.createEntityManagerFactory(getPersistenceUnitName(),
				getPersistenceUnitProperties())) {
			emf = emfLocal;
			if (emf == null) {
				throw new Exception("autoCloseableTest failed: createEntityManagerFactory(String) returned null");
			}
			if (!emf.isOpen()) {
				throw new Exception(
						"autoCloseableTest failed: EntityManagerFactory isOpen() returned false in try block");
			}
		} catch (Exception f) {
			throw f;
		} catch (Throwable t) {
			throw new Exception("autoCloseableTest failed with Exception", t);
		} finally {
			if (emf != null && emf.isOpen()) {
				throw new Exception(
						"autoCloseableTest failed: EntityManagerFactory isOpen() returned true outside try block");
			}
		}
	}

	/*
	 * @testName: getMetamodelTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:340;
	 *
	 * @test_Strategy: Get a MetaModel Object from the EntityManagerFactory and make
	 * sure it is not null
	 */
	@Test
	public void getMetamodelTest() throws Exception {
		boolean pass = false;
		try {
			Metamodel mm = getEntityManager().getEntityManagerFactory().getMetamodel();
			if (mm == null) {
				logger.log(Logger.Level.ERROR, "getMetamodel() returned a null result");
			} else {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getMetamodelTest failed");
		}
	}

	/*
	 * @testName: getPersistenceUnitUtil
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:341;
	 *
	 * @test_Strategy: Get a PersistenceUnitUtil Object from the
	 * EntityManagerFactory an make sure it is not null
	 */
	@Test
	public void getPersistenceUnitUtil() throws Exception {
		boolean pass = false;
		try {
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			if (puu == null) {
				logger.log(Logger.Level.ERROR, "getPersistenceUnitUtil() returned a null result");
			} else {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getPersistenceUnitUtil failed");
		}
	}

	/*
	 * @testName: getCriteriaBuilderTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:339; PERSISTENCE:SPEC:1702;
	 *
	 * @test_Strategy: access EntityManagerFactory.getCriteriaBuilder and verify it
	 * can be used to create a query
	 *
	 */
	@Test
	public void getCriteriaBuilderTest() throws Exception {
		boolean pass = false;
		try {
			CriteriaBuilder cbuilder = getEntityManager().getEntityManagerFactory().getCriteriaBuilder();
			if (cbuilder != null) {
				getEntityTransaction().begin();
				CriteriaQuery<Object> cquery = cbuilder.createQuery();
				if (cquery != null) {
					logger.log(Logger.Level.TRACE, "Obtained Non-null Criteria Query");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Failed to get Non-null Criteria Query");
				}

				getEntityTransaction().commit();
			} else {
				logger.log(Logger.Level.ERROR, "getCriteriaBuilder() returned null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getCriteriaBuilderTest failed");
		}
	}
}
