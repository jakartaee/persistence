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
package ee.jakarta.tck.persistence.core.entitytest.cascadeall.oneXone;

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
		return createDeploymentJar("jpa_core_entitytest_cascadeall_oneXone.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "Entering Setup");
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
	 * @testName: cascadeAll1X1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
	 * PERSISTENCE:JAVADOC:133; PERSISTENCE:JAVADOC:137
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a OneToOne relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded. Entity B is annotated with ALL
	 * so call persist from there.
	 *
	 */
	@Test
	public void cascadeAll1X1Test1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test1");
		boolean pass = false;
		A a1;
		A a2;
		B bRef;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("1", "a1", 1);
			bRef = new B("1", "bean1", 1, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "getA1");
			a2 = bRef.getA1();

			if ((a1 == a2) && (getEntityManager().contains(bRef))) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Unexpected results - test fails.");
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test1 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:624;
	 * PERSISTENCE:JAVADOC:375
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * If X is a removed entity, it becomes managed.
	 *
	 * Create an entity, persist it, remove it, and invoke persist again. Check that
	 * is is managed and is accessible.
	 *
	 */
	@Test
	public void cascadeAll1X1Test2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test2");
		B bRef;
		A a1;

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			a1 = new A("2", "a2", 2);
			bRef = new B("2", "b2", 2, a1);
			logger.log(Logger.Level.TRACE, "Persist Instance");
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance Status ");
			if (getInstanceStatus(findB("2"))) {
				try {
					logger.log(Logger.Level.TRACE, "entity is managed, remove it ");
					getEntityManager().remove(findB("2"));
					getEntityManager().flush();

					logger.log(Logger.Level.TRACE, "Persist a removed entity");
					getEntityManager().persist(bRef);
					pass = getInstanceStatus(bRef);
				} catch (Exception ee) {
					logger.log(Logger.Level.ERROR, "Unexpected exception trying to persist a removed entity", ee);
					pass = false;
				}

			} else {
				logger.log(Logger.Level.TRACE, "Instance is not already persisted. Test Fails.");
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test2 failed");
		}
	}

	/*
	 *
	 * @testName: cascadeAll1X1Test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:627
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * For all entities Y referenced by a relationship from X, if the relationship
	 * to Y has been annotated with the cascade member value cascade=ALL, the
	 * persist operation is applied to Y.
	 *
	 * Invoke persist on a OneToOne relationship from X where Y is annotated with
	 * cascade=ALL and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void cascadeAll1X1Test3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test3");
		boolean pass = false;
		B bRef;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("3", "a3", 3);
			bRef = new B("3", "bean3", 3, a1);
			getEntityManager().persist(bRef);

			A a2 = bRef.getA1();

			if ((a1 == a2) && (getEntityManager().contains(a2))) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Unexpected results received - test failed");
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test3 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:670
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns true: If the entity instance is new
	 * and the persist operation has been cascaded to it.
	 *
	 */
	@Test
	public void cascadeAll1X1Test4() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test4");
		boolean pass = false;
		B bRef;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("4", "a4", 4);
			bRef = new B("4", "bean4", 4, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "bref created, try find");
			A newA = findA("4");
			if (null != newA) {
				pass = getEntityManager().contains(newA);
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test4 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false: If the entity instance is
	 * new and the persist operation has not been cascaded to it.
	 *
	 */
	@Test
	public void cascadeAll1X1Test5() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test5");
		boolean pass1 = true;
		boolean pass2 = true;
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			B b1 = new B("5", "b5", 5);
			A aRef = new A("5", "bean5", 5, b1);

			pass1 = getEntityManager().contains(b1);
			pass2 = getEntityManager().contains(aRef);

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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

		if (pass1 || pass2 || !pass) {
			logger.log(Logger.Level.ERROR, "pass=" + pass + ", pass1=" + pass1 + ", pass2=" + pass2);
			throw new Exception("cascadeAll1X1Test5 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:627
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * If X is a pre-existing managed entity, it is ignored by the persist
	 * operation. However, the persist operation is cascaded to entities referenced
	 * by X, if the relationships from X to these other entities is annotated with
	 * cascade=ALL annotation member value.
	 *
	 * The flush method can be used for force synchronization. The semantics of the
	 * flush operation applied to an entity X is as follows:
	 *
	 * For all entities Y referenced by a relationship from X, if the relationship
	 * to Y has been annotated with the cascade member value cascade=ALL the persist
	 * operation is applied to Y.
	 *
	 */
	@Test
	public void cascadeAll1X1Test6() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test6");
		boolean pass = false;

		B bRef;
		B bRef1;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("6", "a6", 6);
			bRef = new B("6", "bean6", 6);
			getEntityManager().persist(bRef);

			if (getEntityManager().contains(bRef)) {
				bRef1 = findB("6");
				bRef1.setA1(a1);
				getEntityManager().persist(bRef1);
				getEntityManager().flush();

				logger.log(Logger.Level.TRACE, "try to find A");
				A a2 = findA("6");

				if (null != a2) {
					logger.log(Logger.Level.TRACE, "A2 is not null");
					pass = getEntityManager().contains(a2);
				}

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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test6 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:647
	 * 
	 * @test_Strategy: The flush method can be used for force synchronization. The
	 * semantics of the flush operation applied to an entity X is as follows:
	 *
	 * For any entity Y referenced by a relationship from X, where the relationship
	 * to Y has not been annotated with the cascade element value cascade=ALL.
	 *
	 * If Y is new or removed, an IllegalStateException will be thrown by the flush
	 * operation or the transaction commit will fail.
	 *
	 */
	@Test
	public void cascadeAll1X1Test7() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test7");
		boolean pass = false;

		A aRef;
		B b1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			b1 = new B("7", "b7", 7);
			aRef = new A("7", "bean7", 7, b1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "IllegalArgumentException caught as expected:" + e);
			pass = true;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (IllegalStateException fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass) {
			throw new Exception("cascadeAll1X1Test7 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:631
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * The remove operation is cascaded to entities referenced by X, if the
	 * relationship from X to these other entities is annotated with cascade=ALL
	 * annotation member.
	 *
	 * The cascade=ALL specification should only be applied to associations that are
	 * specified as OneToOne or OneToMany.
	 *
	 * Invoke remove on a OneToOne relationship from X annotated with cascade=ALL
	 * and ensure the remove operation is cascaded.
	 *
	 */
	@Test
	public void cascadeAll1X1Test8() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test8");
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final A a1 = new A("8", "a8", 8);
			final B bRef = new B("8", "a8", 8, a1);
			getEntityManager().persist(bRef);

			A a2 = bRef.getA1();

			if ((getInstanceStatus(bRef)) && (a2 == a1)) {
				try {
					logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
					getEntityManager().remove(findB("8"));
					logger.log(Logger.Level.TRACE, "Remove is immediately visible to the contains method");
					if ((!getEntityManager().contains(a2)) && (!getEntityManager().contains(bRef))) {
						getEntityManager().flush();
					}

					A stillExists = findA("8");
					if (stillExists == null) {
						pass = true;
					}

				} catch (Exception onfe) {
					logger.log(Logger.Level.TRACE, "Expected Exception :" + onfe);
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test8 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test9
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
	public void cascadeAll1X1Test9() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test9");
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final A a1 = new A("9", "a9", 9);
			final B bRef = new B("9", "a9", 9, a1);
			getEntityManager().persist(bRef);

			logger.log(Logger.Level.TRACE, "get Instance ");
			A a2 = bRef.getA1();

			if ((getInstanceStatus(bRef)) && (a2 == a1)) {
				try {
					logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
					B newB = findB("9");
					getEntityManager().remove(newB);

					if (!getEntityManager().contains(a2)) {
						pass = true;
					}
				} catch (Exception onfe) {
					logger.log(Logger.Level.ERROR, "Unexpected Exception :" + onfe);
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test9 failed");
		}
	}

	/*
	 * @testName: cascadeAll1X1Test10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:631
	 * 
	 * @test_Strategy: A managed entity instance becomes removed by invoking the
	 * remove method on it or by cascading the remove operation. The semantics of
	 * the remove operation, applied to an entity X are as follows:
	 *
	 * If X is a new entity, it is ignored by the remove operation. However, the
	 * remove operation is cascaded to entities referenced by X, if the
	 * relationships from X to these other entities is annotated with cascade=ALL
	 * annotation member value.
	 *
	 */
	@Test
	public void cascadeAll1X1Test10() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1X1Test10");
		boolean pass = false;
		boolean status = true;

		try {
			getEntityTransaction().begin();
			final A a1 = new A("10", "a10", 10);
			getEntityManager().persist(a1);

			final B bRef = new B("10", "b10", 10, a1);

			if (getInstanceStatus(a1)) {
				try {
					logger.log(Logger.Level.TRACE, "entity is new, remove ignored on bRef, a1 should be removed");
					getEntityManager().remove(bRef);
					status = getEntityManager().contains(a1);
					getEntityManager().flush();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not managed- Unexpected");
				pass = false;
			}

			A stillExists = findA("10");
			if ((!status) && (stillExists == null)) {
				pass = true;
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

		if (!pass) {
			throw new Exception("cascadeAll1X1Test10 failed");
		}
	}

	/*
	 * Business Methods to set up data for Test Cases
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
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB").executeUpdate();
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
