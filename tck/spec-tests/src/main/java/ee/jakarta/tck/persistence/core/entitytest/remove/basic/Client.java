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

package ee.jakarta.tck.persistence.core.entitytest.remove.basic;

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
		return createDeploymentJar("jpa_core_entitytest_remove_basic.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: removeBasicTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:628;
	 * PERSISTENCE:SPEC:629
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * If X is a new entity, it is ignored by the remove operation.
	 *
	 * Invoke remove on a new entity.
	 *
	 */
	@Test
	public void removeBasicTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin removeBasicTest1");
		boolean pass = false;
		final A a1 = new A("1", "a1", 1);

		try {
			getEntityTransaction().begin();
			getEntityManager().remove(a1);
			pass = true;
			getEntityTransaction().commit();
		} catch (Exception fe) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception during remove operation.  Should have been ignored.",
					fe);
			pass = false;
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
			throw new Exception("removeBasicTest1 failed");
	}

	/*
	 * @testName: removeBasicTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:632
	 * 
	 * @test_Strategy: If X is a managed entity, the remove operation causes it to
	 * transition to the removed state. Invoke remove on a managed entity.
	 *
	 */
	@Test
	public void removeBasicTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin removeBasicTest2");
		boolean pass = false;
		final A a1 = new A("2", "a2", 2);
		createA(a1);

		getEntityTransaction().begin();
		try {
			final A newA = findA("2");
			getEntityManager().remove(newA);
			logger.log(Logger.Level.TRACE, "Call contains after remove()");
			pass = (!getEntityManager().contains(newA));
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("removeBasicTest2 failed");
	}

	/*
	 * @testName: removeBasicTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:636
	 * 
	 * @test_Strategy: If X is a removed entity, invoking the remove method on it
	 * will be ignored.
	 *
	 * Invoke remove on a removed entity.
	 *
	 */
	@Test
	public void removeBasicTest3() throws Exception {
		final A a1 = new A("4", "a4", 4);
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Persist Instance");
			getEntityManager().persist(a1);

			if (getEntityManager().contains(a1)) {
				try {
					getEntityManager().remove(a1);
					getEntityManager().flush();

					final A stillExists = findA("4");

					if (stillExists == null) {
						getEntityManager().remove(a1);
						pass = true;
					}
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Unexpected exception caught trying to remove"
							+ " a removed entity, should have been ignored", e);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.TRACE, "entity not managed, unexpected, test fails.");
				pass = false;
			}
			getEntityTransaction().commit();

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
			throw new Exception("removeBasicTest3 failed");
	}

	/*
	 * @testName: removeBasicTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:617; PERSISTENCE:SPEC:637;
	 * PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:648
	 * 
	 * @test_Strategy: A removed entity will be removed from the database at or
	 * before transaction commit or as a result of a flush operation.
	 *
	 * Remove an entity. Verify the entity is removed from the database at as a
	 * result of the flush operation.
	 *
	 * The flush method can be used for force synchronization. The semantics of the
	 * flush operation applied to an entity X is as follows:
	 *
	 * If X is a removed entity, it is removed from the database.
	 *
	 */
	@Test
	public void removeBasicTest4() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin removeBasicTest4");
		boolean pass = false;
		final A a1 = new A("5", "a5", 5);
		getEntityTransaction().begin();
		getEntityManager().persist(a1);

		try {
			A newA = findA("5");
			if (null != newA) {
				logger.log(Logger.Level.TRACE, "Found newA, try Remove");
				getEntityManager().remove(newA);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "Removed, try to find and verify the entity has been removed");
				newA = findA("5");
				if (null == newA) {
					logger.log(Logger.Level.TRACE, "NewA is Null as expected");
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Could not find persisted entity.");
				pass = false;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception attempting to find removed entity:" + e);
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
			throw new Exception("removeBasicTest4 failed");
	}

	/*
	 * @testName: removeBasicTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:673
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false:
	 *
	 * If the remove method has been called on the entity.
	 *
	 */
	@Test
	public void removeBasicTest5() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin removeBasicTest5");
		boolean pass = false;
		final A a1 = new A("6", "a6", 6);
		createA(a1);

		try {
			getEntityTransaction().begin();

			final A a2 = findA("6");
			getEntityManager().remove(a2);

			if (!getEntityManager().contains(a2)) {
				pass = true;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught:" + e);
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
			throw new Exception("removeBasicTest5 failed");
	}

	/*
	 * @testName: removeMergeBasicTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:661
	 * 
	 * @test_Strategy: To merge a removed entity will result in
	 * IllegalArgumentException, or commit will fail.
	 */
	@Test
	public void removeMergeBasicTest() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin removeMergeBasicTest");
		boolean pass = false;
		String reason = null;
		final String aId = "7";
		final A a1 = new A(aId, "a7", 7);
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(a1);
			getEntityManager().flush();
			getEntityManager().remove(a1);

			try {
				getEntityManager().merge(a1);
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.TRACE, "Got expected exception when merging a removed entity:" + e);
				pass = true;
			}
			if (!pass) {
				getEntityTransaction().commit();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE,
					"This exception may be expected, but we need to check" + " if the commit really failed:" + e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
		if (!pass) {
			A aFound = findA(aId);
			if (aFound == null) {
				pass = true;
				logger.log(Logger.Level.TRACE,
						"No entity with id " + aId + " was found.  The commit " + "must have failed, as expected.");
			} else {
				reason = "Entity with id " + aId + " was found: " + aFound
						+ ".  It means the previous commit unexpectedly succeeded.";
				logger.log(Logger.Level.TRACE, reason);
			}
		}
		if (!pass)
			throw new Exception("removeMergeBasicTest failed, reason: " + reason);
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */

	private void createA(final A a) {
		logger.log(Logger.Level.TRACE, "Entered createA method");
		getEntityTransaction().begin();
		getEntityManager().persist(a);
		getEntityTransaction().commit();

	}

	private A findA(final String id) {
		logger.log(Logger.Level.TRACE, "Entered findA method");
		return getEntityManager().find(A.class, id);
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
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB").executeUpdate();
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
