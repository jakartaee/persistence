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
 *  $Id$
 */

package ee.jakarta.tck.persistence.core.entitytest.detach.basic;

import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A" };
		return createDeploymentJar("jpa_core_entitytest_detach_basic.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * BEGIN Test Cases
	 */

	/*
	 * @testName: detachBasicTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:635
	 * 
	 * @test_Strategy: If X is a detached entity, invoking the remove method on it
	 * will cause an IllegalArgumentException to be thrown or the transaction commit
	 * will fail. Invoke remove on a detached entity.
	 *
	 */
	@Test
	public void detachBasicTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin detachBasicTest1");
		boolean pass = false;
		final A aRef = new A("1", "a1", 1);

		try {

			logger.log(Logger.Level.TRACE, "Persist Instance");
			createA(aRef);

			clearCache();

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "tx started, see if entity is detached");
			if (getEntityManager().contains(aRef)) {
				logger.log(Logger.Level.ERROR,
						"contains method returned true; expected false" + " (detached), test fails.");
				pass = false;
			} else {

				try {
					logger.log(Logger.Level.TRACE, "try remove");
					getEntityManager().remove(aRef);
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "IllegalArgumentException caught as expected", iae);
					pass = true;
				}

			}

			logger.log(Logger.Level.TRACE, "tx commit");
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "or, Transaction commit will fail. " + " Test the commit failed by testing"
					+ " the transaction is marked for rollback");
			if (!pass) {
				if (e instanceof jakarta.transaction.TransactionRolledbackException
						|| e instanceof jakarta.persistence.PersistenceException) {
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR,
							"Not TransactionRolledbackException nor PersistenceException, totally unexpected:", e);
				}
			}

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("detachBasicTest1 failed");
	}

	/*
	 * @testName: detachBasicTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:323; PERSISTENCE:SPEC:649;
	 * PERSISTENCE:SPEC:650;
	 * 
	 * @test_Strategy: Do a find of an entity, detached it, then modify it. Do
	 * another find and verify the changes were not persisted.
	 *
	 */
	@Test
	public void detachBasicTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin detachBasicTest2");
		boolean pass = false;
		final A expected = new A("1", "a1", 1);

		try {

			logger.log(Logger.Level.TRACE, "Persist Instance");
			createA(new A("1", "a1", 1));

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Executing find");
			A newA = getEntityManager().find(A.class, "1");
			logger.log(Logger.Level.TRACE, "newA:" + newA.toString());

			logger.log(Logger.Level.TRACE, "changing name");
			newA.setAName("foobar");
			logger.log(Logger.Level.TRACE, "newA:" + newA.toString());
			logger.log(Logger.Level.TRACE, "executing detach");
			getEntityManager().detach(newA);
			logger.log(Logger.Level.TRACE, "newA:" + newA.toString());

			logger.log(Logger.Level.TRACE, "tx commit");
			getEntityTransaction().commit();
			A newAA = getEntityManager().find(A.class, "1");
			logger.log(Logger.Level.TRACE, "newAA:" + newAA.toString());

			if (expected.equals(newAA)) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR,
						"Changes made to entity were persisted even though it was detached without a flush");
				logger.log(Logger.Level.ERROR, "expected A:" + expected.toString() + ", actual A:" + newAA.toString());

			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass)
			throw new Exception("detachBasicTest2 failed");
	}

	/*
	 * Business Methods for Test Cases
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
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB").executeUpdate();
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
