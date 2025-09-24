/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.entitylistener;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.core.override.util.CallBackCounts;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final Long ID = 1L;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "ListenerA", pkgName + "ListenerB", pkgName + "ListenerC", pkgName + "ListenerD",
				pkgName + "NoEntityListener", pkgName + "NoListener", pkgName + "OverridenListener" };
		return createDeploymentJar("jpa_core_override_entitylistener.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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
	 * @testName: testOverrideEntityListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested by overriding entity listener in
	 * XML file.
	 */
	@Test
	public void testOverrideEntityListener() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		CallBackCounts.clearCountsMap();
		OverridenListener entity = new OverridenListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logger.log(Logger.Level.TRACE, "persisted entity" + entity);
		getEntityManager().remove(entity);
		getEntityManager().flush();
		logger.log(Logger.Level.TRACE, "Removed entity" + entity);
		getEntityTransaction().commit();
		try {
			pass1 = checkPersistCallBacks();
			pass2 = checkRemoveCallBacks();
			if ((pass1 && pass2) == true) {
				logger.log(Logger.Level.TRACE, "testOverrideEntityListener Passed");
			} else if (pass1 == true) {
				throw new Exception("Test failed while testing preremove and "
						+ "postremove methods in testOverrideEntityListener ");
			} else if (pass2 == true) {
				throw new Exception("Test failed while testing prepersist and "
						+ "postpersist methods in testOverrideEntityListener ");
			}

		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testOverrideEntityListener" + e);
		}

	}

	/*
	 * @testName: testEntityListenerXML
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested by using entitylistener with
	 * empty xml tag.
	 */
	@Test
	public void testEntityListenerXML() throws Exception {

		boolean pass = false;
		CallBackCounts.clearCountsMap();
		NoEntityListener entity = new NoEntityListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logger.log(Logger.Level.TRACE, "persisted entity" + entity);
		try {
			pass = checkPersistCallBacks();
			if (pass == true) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("TestEntityListenerXML method failed");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testEntityListenerXML" + e);
		} finally {
			getEntityManager().remove(entity);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testNoEntityListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:722; PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested without using entitylistener.
	 */
	@Test
	public void testNoEntityListener() throws Exception {

		boolean pass = false;
		CallBackCounts.clearCountsMap();
		NoListener entity = new NoListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		getEntityManager().flush();
		logger.log(Logger.Level.TRACE, "persisted entity" + entity);
		try {
			pass = checkPersistCallBacks();
			if (pass == true) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("TestNoEntityListener method failed");
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoEntityListener" + e);
		} finally {
			getEntityManager().remove(entity);
			getEntityTransaction().commit();
		}
	}

	private boolean checkPersistCallBacks() throws Exception {
		boolean result = false;
		if (test("prePersist", 1) && (test("postPersist", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkRemoveCallBacks() throws Exception {
		boolean result = false;
		if (test("preRemove", 1) && (test("preRemove", 1))) {
			result = true;
		}
		return result;

	}

	private boolean test(final String callBackName, final int expectedCount) throws Exception {
		int count = CallBackCounts.getCount(callBackName);
		boolean result = false;
		if (count == expectedCount) {
			logger.log(Logger.Level.TRACE, "test passed" + callBackName);
			result = true;
		} else {
			logger.log(Logger.Level.ERROR, "test failed" + callBackName);
		}
		return result;
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM NOENTITYLISTENER_TABLE").executeUpdate();
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
