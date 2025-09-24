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

package ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXone;

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
		return createDeploymentJar("jpa_core_entitytest_cascadeall_manyXone.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: cascadeAllMX1Test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
	 * PERSISTENCE:JAVADOC:112; PERSISTENCE:JAVADOC:113
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a ManyToOne relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void cascadeAllMX1Test1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAllMX1Test1");
		boolean pass = false;
		A aRef;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			aRef = new A("1", "bean1", 1);
			final B b1 = new B("1", "b1", 1, aRef);
			getEntityManager().persist(b1);
			final B b2 = new B("2", "b2", 1, aRef);
			getEntityManager().persist(b2);
			final B b3 = new B("3", "b3", 1, aRef);
			getEntityManager().persist(b3);
			final B b4 = new B("4", "b4", 1, aRef);
			getEntityManager().persist(b4);
			getEntityManager().flush();

			final A newA1 = b1.getA1Info();
			final A newA2 = b2.getA1Info();
			final A newA3 = b3.getA1Info();
			final A newA4 = b4.getA1Info();

			if ((newA1 != null) && (newA2 != null) && (newA3 != null) && (newA4 != null)) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Wrong results received");
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
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}

		}

		if (!pass)
			throw new Exception("cascadeAllMX1Test1 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:624;
	 * PERSISTENCE:JAVADOC:111
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * If X is a removed entity, it becomes managed.
	 *
	 * Create an entity, persist it, remove it, and invoke persist again. Check that
	 * it is managed and is accessible.
	 *
	 */
	@Test
	public void cascadeAllMX1Test2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAllMX1Test2");
		boolean pass = false;
		A aRef;

		try {
			getEntityTransaction().begin();
			aRef = new A("2", "bean2", 2);
			final B b1 = new B("2", "b2", 2, aRef);
			getEntityManager().persist(b1);
			getEntityManager().flush();

			final A newA1 = b1.getA1Info();

			if (newA1 != null) {
				try {
					logger.log(Logger.Level.TRACE, "Remove b1 ");
					getEntityManager().remove(newA1);
					getEntityManager().remove(b1);
					getEntityManager().flush();

					logger.log(Logger.Level.TRACE, "Persist a removed entity ");
					final B newB = findB("2");
					if (null == newB) {
						getEntityManager().persist(b1);
						getEntityManager().flush();
						pass = ((getEntityManager().contains(b1)) && (b1.getA1() != null));
					} else {
						logger.log(Logger.Level.TRACE, "entity B not removed");
					}
					getEntityTransaction().commit();
				} catch (Exception ee) {
					logger.log(Logger.Level.ERROR, "Unexpected exception trying to persist a removed entity", ee);
				}
			} else {
				logger.log(Logger.Level.TRACE, "Instance is not already persisted. Test Fails.");
			}

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

		if (!pass)
			throw new Exception("cascadeAllMX1Test2 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:668
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns true:
	 *
	 * If the entity has been retrieved from the database and has not been removed
	 * or detached.
	 */
	@Test
	public void cascadeAllMX1Test3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAllMX1Test3");
		boolean pass = false;
		A aRef;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			aRef = new A("4", "bean4", 4);
			getEntityManager().persist(aRef);
			final B b1 = new B("1", "b1", 4);
			getEntityManager().persist(b1);
			final B b2 = new B("2", "b2", 4);
			getEntityManager().persist(b2);
			final B b3 = new B("3", "b3", 4);
			getEntityManager().persist(b3);
			final B b4 = new B("4", "b4", 4);
			getEntityManager().persist(b4);

			b1.setA1(aRef);
			b2.setA1(aRef);
			b3.setA1(aRef);
			b4.setA1(aRef);

			getEntityManager().flush();

			final A newA1 = b1.getA1Info();
			final A newA2 = b2.getA1Info();
			final A newA3 = b3.getA1Info();
			final A newA4 = b4.getA1Info();

			if (((newA1 != null) && (getEntityManager().contains(newA1)))
					&& ((newA2 != null) && (getEntityManager().contains(newA2)))
					&& ((newA3 != null) && (getEntityManager().contains(newA3)))
					&& ((newA4 != null) && (getEntityManager().contains(newA4)))) {
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Unexpected exception caught: " + e);
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
			throw new Exception("cascadeAllMX1Test3 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669;
	 * PERSISTENCE:SPEC:677
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns true: If the entity instance is new
	 * and the persist method has been called on the entity. The effect of cascading
	 * persist is immediately visible visible to the contains method.
	 */
	@Test
	public void cascadeAllMX1Test4() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAllMX1Test4");
		boolean pass = false;
		A aRef;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			aRef = new A("5", "bean5", 5);
			final B b1 = new B("1", "b1", 5, aRef);
			getEntityManager().persist(b1);
			final B b2 = new B("2", "b2", 5, aRef);
			getEntityManager().persist(b2);
			final B b3 = new B("3", "b3", 5, aRef);
			getEntityManager().persist(b3);
			final B b4 = new B("4", "b4", 5, aRef);
			getEntityManager().persist(b4);

			pass = getInstanceStatus(aRef);

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

		if (!pass)
			throw new Exception("cascadeAllMX1Test4 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false:
	 *
	 * If the entity instance is new and the persist operation has not been cascaded
	 * to it.
	 *
	 */
	@Test
	public void cascadeAllMX1Test5() throws Exception {
		boolean pass = false;
		A aRef;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			aRef = new A("6", "bean6", 6);
			final B b1 = new B("1", "b1", 6, aRef);
			final B b2 = new B("2", "b2", 6, aRef);
			final B b3 = new B("3", "b3", 6, aRef);
			final B b4 = new B("4", "b4", 6, aRef);

			pass = ((!getInstanceStatus(aRef)) && (!getInstanceStatus(b1)) && (!getInstanceStatus(b2))
					&& (!getInstanceStatus(b3)) && (!getInstanceStatus(b4)));

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("cascadeAllMX1Test5 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:675
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false:
	 *
	 * If the entity instance is new and the persist operation not been called on
	 * it.
	 */
	@Test
	public void cascadeAllMX1Test6() throws Exception {
		boolean pass = false;

		A a1;
		B bRef;
		try {
			getEntityTransaction().begin();
			a1 = new A("7", "b7", 7);
			bRef = new B("7", "bean7", 7, a1);

			pass = ((!getEntityManager().contains(bRef)) && (!getEntityManager().contains(a1)));

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass)
			throw new Exception("cascadeAllMX1Test6 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a ManyToOne relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 * If X is a pre-existing managed entity, it is ignored by the persist
	 * operation. However, the persist operation is cascaded to entities referenced
	 * by X, if the relationships from X to these other entities is annotated with
	 * cascade=ALL annotation member value.
	 *
	 */
	@Test
	public void cascadeAllMX1Test7() throws Exception {
		boolean pass = false;
		A a1;
		A a2;
		B bRef;
		B bRef1;

		try {
			getEntityTransaction().begin();
			a1 = new A("8", "a8", 8);
			bRef = new B("8", "bean8", 8);
			getEntityManager().persist(bRef);

			if (getEntityManager().contains(bRef)) {
				bRef1 = findB("8");
				bRef1.setA1(a1);
				getEntityManager().persist(bRef1);
				pass = getEntityManager().contains(a1);
				logger.log(Logger.Level.TRACE, "try to find A");
				a2 = findA("8");
				if (null != a2) {
					logger.log(Logger.Level.TRACE, "b2 is not null");
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

		if (!pass)
			throw new Exception("cascadeAllMX1Test7 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:642
	 * 
	 * @test_Strategy: The flush method can be used for force synchronization. The
	 * semantics of the flush operation applied to an entity X is as follows:
	 *
	 * If X is a managed entity, it is synchronized to the database.
	 *
	 */
	@Test
	public void cascadeAllMX1Test8() throws Exception {
		boolean pass = false;
		B bRef;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("9", "A9", 9);
			bRef = new B("9", "bean9", 9);
			getEntityManager().persist(bRef);

			if (null == bRef.getA1()) {
				bRef.setA1(a1);
				getEntityManager().flush();
			}

			if ((null != a1) && (getEntityManager().contains(a1))) {
				A result = bRef.getA1Info();
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Entity not managed - test fails.");
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

		if (!pass)
			throw new Exception("cascadeAllMX1Test8 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
	 * 
	 * @test_Strategy: The flush method can be used for force synchronization. The
	 * semantics of the flush operation applied to an entity X is as follows:
	 *
	 * For all entities Y referenced by a relationship from X, if the relationship
	 * to Y has been annotated with the cascade member value cascade=ALL, the
	 * persist operation is applied to Y.
	 *
	 */
	@Test
	public void cascadeAllMX1Test9() throws Exception {
		boolean pass = false;

		B bRef;
		A a1;

		try {
			logger.log(Logger.Level.TRACE, "New instances");
			getEntityTransaction().begin();
			a1 = new A("10", "a10", 10);
			bRef = new B("10", "bean10", 10, a1);

			getEntityManager().persist(bRef);
			getEntityManager().flush();
			pass = getEntityManager().contains(a1);
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
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
			throw new Exception("cascadeAllMX1Test9 failed");
	}

	/*
	 * @testName: cascadeAllMX1Test10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:646
	 * 
	 * @test_Strategy: The flush method can be used for force synchronization. The
	 * semantics of the flush operation applied to an entity X is as follows:
	 *
	 * For any detached entity Y referenced by a relationship from X, where the
	 * relationship to Y has not been annotated with the cascade member value
	 * cascade=ALL the semantics depend upon the ownership of the relationship. If X
	 * owns the relationship, any changes to the relationship are synchronized with
	 * the database, otherwise, if Y owns the relationship, the behavior is
	 * undefined.
	 *
	 */
	@Test
	public void cascadeAllMX1Test10() throws Exception {
		A a1;
		B bRef;
		B b2;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("11", "a11", 11);
			bRef = new B("11", "bean11", 11, a1);
			getEntityManager().persist(bRef);
			getEntityTransaction().commit();

			getEntityTransaction().begin();
			b2 = findB("11");
			A newA = b2.getA1();
			newA.setAName("newA");
			getEntityManager().flush();
			if ((b2.isA()) && (newA.getAName().equals("newA"))) {
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

		if (!pass)
			throw new Exception("cascadeAllMX1Test10 failed");
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
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
