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

package ee.jakarta.tck.persistence.core.annotations.orderby;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client3 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client3.class.getName());

	List<Address> addrRef;

	Address addr1 = null;

	Address addr2 = null;

	Address addr3 = null;

	List<Address2> addrRef2;

	Address2 addr11 = null;

	Address2 addr12 = null;

	Address2 addr13 = null;

	public Client3() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client3.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "A2", pkgName + "Address", pkgName + "Address2",
				pkgName + "Customer", pkgName + "Customer2", pkgName + "Department", pkgName + "Employee",
				pkgName + "Insurance", pkgName + "ZipCode", pkgName + "ZipCode2" };
		return createDeploymentJar("jpa_core_annotations_orderby3.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupCust() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

			removeCustTestData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: propertyElementCollectionBasicType
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2090
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
	@Test
	public void propertyElementCollectionBasicType() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer expected = new Customer("1");
			List<String> expectedphones = new ArrayList<String>();
			expectedphones.add("781-442-2010");
			expectedphones.add("781-442-2011");
			expectedphones.add("781-442-2012");

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
					for (String s : cust.getPhones()) {
						logger.log(Logger.Level.TRACE, "phone:" + s);
					}
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					for (String s : expectedphones) {
						logger.log(Logger.Level.ERROR, "expected:" + s);
					}
					logger.log(Logger.Level.ERROR, "actual:");
					for (String s : cust.getPhones()) {
						logger.log(Logger.Level.ERROR, "actual:" + s);
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
			throw new Exception("propertyElementCollectionBasicType failed");
		}
	}

	/*
	 * @testName: fieldElementCollectionBasicType
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2090
	 * 
	 * @test_Strategy: ElementCollection of a basic type
	 */
	@Test
	public void fieldElementCollectionBasicType() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer2 expected = new Customer2("2");
			List<String> expectedphones = new ArrayList<String>();
			expectedphones.add("781-442-2010");
			expectedphones.add("781-442-2011");
			expectedphones.add("781-442-2012");

			expected.setPhones(expectedphones);
			logger.log(Logger.Level.TRACE, "Persisting Customer2:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Customer2 cust = getEntityManager().find(Customer2.class, expected.getId());
			if (cust != null) {
				logger.log(Logger.Level.TRACE, "Found Customer2: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					logger.log(Logger.Level.TRACE, "Received expected Phones:");
					for (String s : cust.getPhones()) {
						logger.log(Logger.Level.TRACE, "phone:" + s);
					}
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get expected results.");
					for (String s : expectedphones) {
						logger.log(Logger.Level.ERROR, "expected:" + s);
					}
					logger.log(Logger.Level.ERROR, "actual:");
					for (String s : cust.getPhones()) {
						logger.log(Logger.Level.ERROR, "actual:" + s);
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
			throw new Exception("fieldElementCollectionBasicType failed");
		}
	}

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
