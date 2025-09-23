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

package ee.jakarta.tck.persistence.core.types.property;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.types.common.Grade;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { Grade.class.getCanonicalName(), pkgName + "Customer", pkgName + "DataTypes",
				pkgName + "DataTypes2" };
		return createDeploymentJar("jpa_core_types_property2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setupCust() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeCustTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: elementCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2017; PERSISTENCE:SPEC:2018;
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
	@Test
	public void elementCollectionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer expected = new Customer("1");
			// Since this column in db can take Strings just use
			// that for the test
			List<Grade> expectedphones = new ArrayList<Grade>();
			expectedphones.add(Grade.A);
			expectedphones.add(Grade.B);
			expectedphones.add(Grade.C);

			expected.setPhones(expectedphones);
			logger.log(Logger.Level.TRACE, "Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "find the previously persisted Customer and Country and verify them");
			Customer cust = getEntityManager().find(Customer.class, expected.getId());
			if (cust != null) {
				logger.log(Logger.Level.TRACE, "Found Customer: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					logger.log(Logger.Level.TRACE, "Received expected Phones:");
					for (Grade g : cust.getPhones()) {
						logger.log(Logger.Level.TRACE, "phone:" + g);
					}
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					for (Grade g : expectedphones) {
						logger.log(Logger.Level.ERROR, "expected:" + g);
					}
					logger.log(Logger.Level.ERROR, "actual:");
					for (Grade g : cust.getPhones()) {
						logger.log(Logger.Level.ERROR, "actual:" + g);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionTest failed");
		}
	}

	// Methods used for Tests

	@AfterEach
	public void cleanupCust() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
			removeCustTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeCustTestData() {
		logger.log(Logger.Level.TRACE, "removeCustTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
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
