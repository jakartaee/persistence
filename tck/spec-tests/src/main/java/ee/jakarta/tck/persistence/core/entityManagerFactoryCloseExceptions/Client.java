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

package ee.jakarta.tck.persistence.core.entityManagerFactoryCloseExceptions;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.CleanupMethod;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	Properties props = null;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = {};
		return createDeploymentJar("jpa_core_entityManagerFactoryCloseExceptions.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	public void nullCleanup() throws Exception {
	}

	/*
	 * @testName: exceptionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:536; PERSISTENCE:JAVADOC:538;
	 * PERSISTENCE:JAVADOC:537; PERSISTENCE:JAVADOC:531; PERSISTENCE:JAVADOC:532;
	 * PERSISTENCE:JAVADOC:533; PERSISTENCE:JAVADOC:534; PERSISTENCE:JAVADOC:535
	 * 
	 * @test_Strategy: Close the EntityManagerFactory, then call various methods
	 */
	@CleanupMethod(name = "nullCleanup")
	@Test
	public void exceptionsTest() throws Exception {
		int passCount = 0;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		EntityManagerFactory emf;
		logger.log(Logger.Level.INFO, "Getting EntityManagerFactory");
		if (isStandAloneMode()) {
			emf = getEntityManager().getEntityManagerFactory();
		} else {
			emf = getEntityManagerFactory();
		}
		if (emf != null) {
			if (emf.isOpen()) {
				logger.log(Logger.Level.INFO, "EMF is open, now closing it");
				emf.close();
			} else {
				logger.log(Logger.Level.INFO, "EMF is already closed");
			}

			logger.log(Logger.Level.INFO, "Testing getMetamodel() after close");
			try {
				emf.getMetamodel();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing emf.getProperties()");
			try {
				emf.getProperties();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing getPersistenceUnitUtil() after close");
			try {
				emf.getPersistenceUnitUtil();
				logger.log(Logger.Level.ERROR, "Did no throw IllegalStateException");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing close after close ");
			try {
				emf.close();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing createEntityManager() after close");
			try {
				emf.createEntityManager();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing createEntityManager(Map) after close");
			try {
				emf.createEntityManager(myMap);
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			logger.log(Logger.Level.INFO, "Testing getCache after close ");
			try {
				emf.getCache();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logger.log(Logger.Level.TRACE, "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}

			try {
				emf.getCriteriaBuilder();
				logger.log(Logger.Level.ERROR, "IllegalStateException was not thrown");
			} catch (IllegalStateException ise) {
				passCount++;
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			}
		} else {
			logger.log(Logger.Level.ERROR, "Could not obtain an EntityManagerFactory");
		}
		if (passCount != 8) {
			throw new Exception("exceptionsTest failed");
		}
	}

}
