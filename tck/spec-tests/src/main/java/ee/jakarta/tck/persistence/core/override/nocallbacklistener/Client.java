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

package ee.jakarta.tck.persistence.core.override.nocallbacklistener;

import java.lang.System.Logger;
import java.util.List;

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
		String[] classes = { pkgName + "NoCallBackListener" };
		return createDeploymentJar("jpa_core_override_nocallbacklistener.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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
	 * @testName: testNoCallBackListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:719; PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:722;
	 * PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested without using callback method's
	 * annotations.
	 * 
	 */
	@Test
	public void testNoCallBackListener() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CallBackCounts.clearCountsMap();
		NoCallBackListener entity = new NoCallBackListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		logger.log(Logger.Level.TRACE, "persisted entity" + entity);
		getEntityManager().flush();
		getEntityManager().refresh(entity);

		// perform query to induce postLoad() method
		//
		// The PostLoad method for an entity is invoked after the entity has
		// been loaded into the current persistence context from the database
		// or after the refresh operation has been applied to it. The PostLoad
		// method is invoked before a query result is returned or accessed or
		// before an association is traversed.
		//
		NoCallBackListener newEntity = null;
		List result = getEntityManager().createQuery("SELECT n FROM NoCallBackListener n").getResultList();
		if (result != null && result.size() > 0) {
			newEntity = (NoCallBackListener) result.get(0);
			if (newEntity.getId() == 1) {
				logger.log(Logger.Level.TRACE, "Retrieved persisted entity");
			}
			getEntityManager().remove(entity);
			getEntityManager().flush();
			getEntityTransaction().commit();
			try {
				pass1 = checkPersistCallBacks();
				logger.log(Logger.Level.TRACE, "pass1 = " + pass1);

				pass2 = checkRemoveCallBacks();
				logger.log(Logger.Level.TRACE, "pass2 = " + pass2);

				pass3 = checkLoadCallBacks();
				logger.log(Logger.Level.TRACE, "pass3 = " + pass3);
				if (pass1 && pass2 && pass3) {
					logger.log(Logger.Level.TRACE, "testOverrideCallBackMethods Passed");
				} else {
					throw new Exception("Test failed while testing prePersist" + ", postPersist, preremove and "
							+ "postremove methods and also the post Load method");
				}
			} catch (Exception e) {
				throw new Exception("Exception thrown while testing testNoCallBackListener" + e);
			}
		} else {
			throw new Exception("getResultList returned a null list");
		}
	}

	private boolean checkPersistCallBacks() throws Exception {
		boolean result = false;
		if (test("prePersistFromXML", 1) && (test("postPersist", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkRemoveCallBacks() throws Exception {
		boolean result = false;
		if (test("preRemoveFromXML", 1) && (test("postRemoveFromXML", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkLoadCallBacks() throws Exception {
		boolean result = false;
		if (test("postLoadFromXML", 1)) {
			return true;
		}
		return result;
	}

	private boolean test(final String callBackName, final int expectedCount) throws Exception {

		boolean pass = false;

		int count = CallBackCounts.getCount(callBackName);
		if (count == expectedCount) {
			logger.log(Logger.Level.TRACE, "test passed in test method" + callBackName);
			pass = true;
		} else {
			logger.log(Logger.Level.TRACE,
					"test not passed as the count and the" + " expected count are not same" + callBackName);
		}

		return pass;
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
