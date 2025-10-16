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

package ee.jakarta.tck.persistence.se.entityManagerFactory;

import java.lang.System.Logger;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;

public class Client1 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	Properties props = null;

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Member", pkgName + "Member_", pkgName + "Order", pkgName + "Order_" };
		return createDeploymentJar("jpa_se_entityManagerFactory.jar", pkgNameWithoutSuffix, (String[]) classes);

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
	 * @testName: getMetamodelIllegalStateExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:536;
	 * 
	 * @test_Strategy: Close the EntityManagerFactory, then call emf.getMetaModel()
	 */
	@Test
	public void getMetamodelIllegalStateExceptionTest() throws Exception {
		boolean pass = false;
		try {
			EntityManagerFactory emf = getEntityManager().getEntityManagerFactory();
			emf.close();
			try {
				emf.getMetamodel();
				logger.log(Logger.Level.ERROR, "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Received expected IllegalStateException");
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getMetamodelIllegalStateExceptionTest failed");
		}
	}
}
