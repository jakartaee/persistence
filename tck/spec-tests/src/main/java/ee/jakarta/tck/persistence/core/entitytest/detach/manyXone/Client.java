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

package ee.jakarta.tck.persistence.core.entitytest.detach.manyXone;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityExistsException;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "B" };
		return createDeploymentJar("jpa_core_entitytest_detach_manyXone.jar", pkgNameWithoutSuffix, classes);

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
	 * BEGIN Test Cases
	 */

	/*
	 * @testName: detachMX1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:742;
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * If X is a detached object and the persist method is invoked on it, an
	 * IllegalArgumentException is thrown or the commit() will fail. Check for an
	 * IllegalArgumentException, if not thrown, be sure the persist method was not
	 * successful by invoking find(). Invoke persist on a detached entity.
	 *
	 */
	@Test
	public void detachMX1Test1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin detachMX1Test1");
		boolean pass = false;
		final A aRef = new A("3", "a3", 3);

		try {
			logger.log(Logger.Level.TRACE, "Persist Instance");
			createA(aRef);
			logger.log(Logger.Level.TRACE, "Call EntityManager.clear()");
			clearCache();

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Call getEntityManager.contains to determine if the instance is detached");

			if (getEntityManager().contains(aRef)) {
				logger.log(Logger.Level.TRACE, "entity is not detached, unexpected cannot proceed with test.");
			} else {

				try {
					logger.log(Logger.Level.TRACE, "Status is false as expected, try perist()");
					getEntityManager().persist(aRef);
					logger.log(Logger.Level.INFO, "Did not throw IllegalArgumentException");
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.INFO,
							"received expected IllegalArgumentException when trying to persist a detached entity");
					pass = true;
				} catch (EntityExistsException eee) {
					logger.log(Logger.Level.INFO, "EntityExistsException thrown trying to persist an existing entity");
					pass = true;
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "or, Transaction commit will fail.  " + " Test the commit failed by testing"
					+ " the transaction is marked for rollback");

			if ((!pass) && (e instanceof jakarta.transaction.TransactionRolledbackException
					|| e instanceof jakarta.persistence.PersistenceException)) {
				logger.log(Logger.Level.INFO,
						"Received exception TransactionRolledbackException or PersistenceException");
				pass = true;
			}

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}

		if (!pass)
			throw new Exception("detachMX1Test1 failed");
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
	 */

	private void createA(final A a) {
		logger.log(Logger.Level.TRACE, "Entered createA method");
		getEntityTransaction().begin();
		getEntityManager().persist(a);
		getEntityTransaction().commit();
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
			getEntityManager().createNativeQuery("DELETE FROM BEJB_MX1_UNI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM AEJB_MX1_UNI_BTOB").executeUpdate();
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
