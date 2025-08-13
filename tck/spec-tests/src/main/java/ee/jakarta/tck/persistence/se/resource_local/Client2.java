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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.se.resource_local;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.SynchronizationType;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	private EntityManager entityManager;

	private EntityTransaction entityTransaction;

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "B" };
		return createDeploymentJar("jpa_resource_local2.jar", pkgNameWithoutSuffix, (String[]) classes);

	}

	@BeforeEach
	public void setupOnly() throws Exception {
		try {
			super.setup();
			createDeployment();
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * @testName: createEntityManagerSynchronizationTypeIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3323;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void createEntityManagerSynchronizationTypeIllegalStateExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Properties p = getPersistenceUnitProperties();
		logger.log(Logger.Level.INFO, "Testing for resource-local entity managers");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), p);

		displayMap(emf.getProperties());
		try {
			emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Caught Expected IllegalStateException");
			pass1 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		logger.log(Logger.Level.INFO, "Testing when EMF is closed");
		emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), p);
		displayMap(emf.getProperties());
		try {
			emf.close();
			emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED);
			logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
		} catch (IllegalStateException iae) {
			logger.log(Logger.Level.TRACE, "Caught Expected IllegalStateException");
			pass2 = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("createEntityManagerSynchronizationTypeIllegalStateExceptionTest failed");
		}
	}

	@AfterEach
	public void cleanupOnly() throws Exception {
		logger.log(Logger.Level.TRACE, "cleanupOnly");
		super.cleanup();
		removeTestJarFromCP();
	}

}
