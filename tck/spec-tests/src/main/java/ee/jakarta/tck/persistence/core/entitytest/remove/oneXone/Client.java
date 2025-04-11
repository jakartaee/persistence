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

package ee.jakarta.tck.persistence.core.entitytest.remove.oneXone;

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
		String[] classes = { pkgName + "A", pkgName + "B" };
		return createDeploymentJar("jpa_core_entitytest_remove_oneXone.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: remove1X1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:629
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * If X is a new entity, it is ignored by the remove operation. Invoke remove on
	 * a new entity.
	 */
	@Test
	public void remove1X1Test1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test1");
		boolean pass = false;
		final A a1 = new A("1", "a1", 1);
		final B bRef = new B("1", "b1", 1, a1);

		try {

			getEntityTransaction().begin();
			boolean result = getInstanceStatus(bRef);

			if (!result) {
				logger.log(Logger.Level.TRACE,
						"Instance state is not managed as expected. " + "Try invoking remove on it.");
				getEntityManager().remove(bRef);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Instance state is managed. " + " Unexpected as this is NEW instance.");
				pass = false;
			}
			getEntityTransaction().commit();
		} catch (Exception fe) {
			logger.log(Logger.Level.ERROR, "Unexpected exception during remove operation. Should have been ignored.",
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
			throw new Exception("remove1X1Test1 failed");
	}

	/*
	 * @testName: remove1X1Test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:632
	 * 
	 * @test_Strategy: If X is a managed entity, the remove operation causes it to
	 * transition to the removed state. Invoke remove on a managed entity.
	 *
	 */
	@Test
	public void remove1X1Test2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test2");
		boolean pass = true;
		final A a1 = new A("2", "a2", 2);
		final B bRef = new B("2", "b2", 2, a1);

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance Status ");
			if (getInstanceStatus(bRef)) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(bRef);

				logger.log(Logger.Level.TRACE, "Call contains after remove()");
				pass = getEntityManager().contains(bRef);
			} else {
				logger.log(Logger.Level.ERROR, "Instance is not managed, cannot proceed with test");
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
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (pass)
			throw new Exception("remove1X1Test2 failed");
	}

	/*
	 * @testName: remove1X1Test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:633
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * The remove operation is cascaded to entities referenced by X, if the
	 * relationship from X to these other entities is annotated with cascade=REMOVE
	 * annotation member.
	 *
	 * The cascade=REMOVE specification should only be applied to associations that
	 * are specified as OneToOne or OneToMany.
	 *
	 * Invoke remove on a OneToOne relationship from X annotated with cascade=REMOVE
	 * and ensure the remove operation is cascaded.
	 *
	 */
	@Test
	public void remove1X1Test3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test3");
		boolean pass = false;
		boolean status = false;

		try {

			getEntityTransaction().begin();
			final A a1 = new A("3", "a3", 3);
			getEntityManager().persist(a1);

			final B bRef = new B("3", "a3", 3, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance Status ");
			final A a2 = bRef.getA1();
			status = getInstanceStatus(bRef);

			if ((status) && (a2 == a1)) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(bRef);
				logger.log(Logger.Level.TRACE, "Remove is immediately visible to the contains method");
				if ((!getEntityManager().contains(a2)) && (!getEntityManager().contains(bRef))) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "Expected Exception :", e);
			pass = false;
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
			throw new Exception("remove1X1Test3 failed");
	}

	/*
	 * @testName: remove1X1Test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:636
	 * 
	 * @test_Strategy: If X is a removed entity, invoking the remove method on it
	 * will be ignored. Invoke remove on a removed entity.
	 *
	 */
	@Test
	public void remove1X1Test4() throws Exception {
		boolean pass = false;
		boolean status = false;
		final A a1 = new A("4", "a4", 4);
		final B bRef = new B("4", "b4", 4, a1);

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(a1);
			getEntityManager().persist(bRef);
			logger.log(Logger.Level.TRACE, "get Instance Status ");
			status = getEntityManager().contains(bRef);

			if (status) {
				logger.log(Logger.Level.TRACE, "entity is managed, remove");
				getEntityManager().remove(bRef);
				getEntityManager().flush();

				B stillExists = findB("4");
				if (stillExists == null) {
					getEntityManager().remove(bRef);
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "entity not managed, unexpected, test fails.");
				pass = false;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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
			throw new Exception("remove1X1Test4 failed");
	}

	/*
	 * @testName: remove1X1Test5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:637; PERSISTENCE:SPEC:648
	 * 
	 * @test_Strategy: A removed entity will be removed from the database at or
	 * before transaction commit or as a result of a flush operation. Accessing an
	 * entity in the removed state is undefined.
	 *
	 * Remove an entity. Verify the entity is removed from the database at
	 * transaction commit.
	 *
	 */
	@Test
	public void remove1X1Test5() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test5");
		boolean pass = false;
		boolean status = false;
		final A a1 = new A("5", "a5", 5);
		final B bRef = new B("5", "a5", 5, a1);

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance ");
			final A a2 = bRef.getA1();
			logger.log(Logger.Level.TRACE, "get Instance Status ");
			status = getInstanceStatus(bRef);

			if ((status) && (a2 == a1)) {
				getEntityManager().remove(bRef);
				getEntityManager().flush();

				final B newB = findB("5");
				if ((newB == null) && (!getEntityManager().contains(bRef)) && (!getEntityManager().contains(a1))) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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
			throw new Exception("remove1X1Test5 failed");
	}

	/*
	 * @testName: remove1X1Test6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:648
	 * 
	 * @test_Strategy: A removed entity will be removed from the database at or
	 * before transaction commit or as a result of a flush operation. Accessing an
	 * entity in the removed state is undefined.
	 *
	 * Remove an entity and force the removal using flush(). Verify the entity is
	 * removed from the database.
	 *
	 * The flush method can be used for force synchronization. The semantics of the
	 * flush operation applied to an entity X is as follows:
	 *
	 * If X is a removed entity, it is removed from the database.
	 *
	 */
	@Test
	public void remove1X1Test6() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test6");
		boolean pass = false;
		boolean status = false;

		getEntityTransaction().begin();
		final A a1 = new A("6", "a6", 6);
		getEntityManager().persist(a1);
		final B bRef = new B("6", "a6", 6, a1);
		getEntityManager().persist(bRef);

		logger.log(Logger.Level.TRACE, "get Instance ");
		final A a2 = bRef.getA1();
		logger.log(Logger.Level.TRACE, "get Instance Status ");
		status = getInstanceStatus(bRef);

		if ((status) && (a2 == a1)) {
			try {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(findB("6"));
				getEntityManager().flush();
				logger.log(Logger.Level.TRACE,
						"A removed entity is removed from the database " + "as a result of the flush operation");
				final B newB = findB("6");
				if ((newB == null) && (!getEntityManager().contains(bRef)) && (!getEntityManager().contains(a1))) {
					pass = true;
				}
				getEntityTransaction().commit();

			} catch (Exception onfe) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception :" + onfe);
			}
		} else {
			logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
			pass = false;
		}

		if (!pass)
			throw new Exception("remove1X1Test6 failed");
	}

	/*
	 * @testName: remove1X1Test7
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
	public void remove1X1Test7() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test7");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final A a1 = new A("7", "a7", 7);
			getEntityManager().persist(a1);
			final B bRef = new B("7", "a7", 7, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance ");
			final A a2 = bRef.getA1();
			logger.log(Logger.Level.TRACE, "get Instance Status ");
			status = getEntityManager().contains(bRef);

			if ((status) && (a2 == a1)) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(bRef);

				if (!getEntityManager().contains(bRef)) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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
			throw new Exception("remove1X1Test7 failed");
	}

	/*
	 * @testName: remove1X1Test8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:674
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false:
	 *
	 * If the remove operation has been cascaded to it.
	 *
	 */
	@Test
	public void remove1X1Test8() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test8");
		boolean pass = false;
		boolean status = false;
		try {
			getEntityTransaction().begin();
			final A a1 = new A("8", "a8", 8);
			getEntityManager().persist(a1);
			final B bRef = new B("8", "a8", 8, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance ");
			final A a2 = bRef.getA1();
			logger.log(Logger.Level.TRACE, "get Instance Status ");
			status = getInstanceStatus(bRef);

			if ((status) && (a2 == a1)) {
				getEntityManager().remove(bRef);

				if (!getEntityManager().contains(a2)) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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
			throw new Exception("remove1X1Test8 failed");
	}

	/*
	 * @testName: remove1X1Test9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:630
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * If X is a new entity, it is ignored by the remove operation. However, the
	 * remove operation is cascaded to entities referenced by X, if the
	 * relationships from X to these other entities is annotated with cascade=REMOVE
	 * annotation member value.
	 *
	 */
	@Test
	public void remove1X1Test9() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1X1Test9");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final A a1 = new A("9", "a9", 9);
			getEntityManager().persist(a1);
			final B bRef = new B("9", "b9", 9, a1);

			logger.log(Logger.Level.TRACE, "get Instance Status ");
			status = getEntityManager().contains(a1);

			if (status) {
				logger.log(Logger.Level.TRACE,
						"A Entity is persisted, bRef is new, remove should be ignored on bRef, a1 should be removed");
				getEntityManager().remove(bRef);
				getEntityManager().flush();
				logger.log(Logger.Level.TRACE, "Call contains after remove()");
				status = getEntityManager().contains(a1);

				logger.log(Logger.Level.TRACE, "Call contains after remove()");
				final A stillExists = findA("9");
				if ((!status) && (stillExists == null)) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}

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
			throw new Exception("remove1X1Test9 failed");
	}

	/*
	 * Business Methods for Test Cases
	 */

	private A findA(final String id) {
		logger.log(Logger.Level.TRACE, "Entered findA method");
		return getEntityManager().find(A.class, id);
	}

	private B findB(final String id) {
		logger.log(Logger.Level.TRACE, "Entered findB method");
		return getEntityManager().find(B.class, id);
	}

	private boolean getInstanceStatus(final Object o) {
		logger.log(Logger.Level.TRACE, "Entered getInstanceStatus method");
		return getEntityManager().contains(o);
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
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
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
