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

package ee.jakarta.tck.persistence.core.entitytest.cascadeall.oneXmany;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

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
		return createDeploymentJar("jpa_core_entitytest_cascadeall_oneXmany.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: cascadeAll1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
	 * PERSISTENCE:JAVADOC:129; PERSISTENCE:JAVADOC:132; PERSISTENCE:SPEC:566
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a OneToMany relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void cascadeAll1XMTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1XMTest1");
		boolean pass = false;
		A aRef;
		Collection newCol;

		try {
			logger.log(Logger.Level.TRACE, "New instances");
			final B b1 = new B("1", "b1", 1);
			final B b2 = new B("2", "b2", 1);
			final B b3 = new B("3", "b3", 1);
			final B b4 = new B("4", "b4", 1);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			aRef = new A("1", "bean1", 1, v1);
			createA(aRef);

			newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3) && newCol.contains(b4)) {
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Test failed");
				pass = false;
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
			throw new Exception("cascadeAll1XMTest1 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * For all entities Y referenced by a relationship from X, if the relationship
	 * to Y has been annotated with the cascade member value cascade=ALL, the
	 * persist operation is applied to Y.
	 *
	 * Invoke persist a relationship from X annotated where Y IS NOT annotated with
	 * cascade=ALL and ensure the persist operation is not cascaded. An
	 * IllegalStateException should be thrown.
	 *
	 */
	@Test
	public void cascadeAll1XMTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1XMTest2");
		boolean pass = false;
		B bRef;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("2", "a2", 2);
			bRef = new B("2", "bean2", 2, a1);
			getEntityManager().persist(bRef);
			getEntityManager().flush();
			getEntityTransaction().commit();

		} catch (IllegalStateException e) {
			logger.log(Logger.Level.TRACE,
					"IllegalStateException Caught as Expected.  Validate the entity did " + "not get persisted");
			pass = true;
			/*
			 * B newB = findB("2"); a2 = newB.getA1Info();
			 * logger.log(Logger.Level.TRACE,"A1Info is:" + a2.getAId() );
			 * 
			 * if (null == a2 ) { pass = true; }
			 */
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
			throw new Exception("cascadeAll1XMTest2 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest3
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
	public void cascadeAll1XMTest3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1XMTest3");
		boolean pass = false;
		A a1;
		Collection newCol;
		try {
			getEntityTransaction().begin();
			final B b1 = new B("1", "b1", 3);
			final B b2 = new B("2", "b2", 3);
			final B b3 = new B("3", "b3", 3);
			final B b4 = new B("4", "b4", 3);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);

			logger.log(Logger.Level.TRACE, "New A instance");
			a1 = new A("3", "bean3", 3, v1);
			getEntityManager().persist(a1);
			newCol = a1.getBCol();

			dumpCollectionDataB(newCol);

			if ((newCol.size() != 0) && (getEntityManager().contains(b1)) && (getEntityManager().contains(b2))
					&& (getEntityManager().contains(b3)) && (getEntityManager().contains(b4))) {
				pass = getInstanceStatus(a1);
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
			throw new Exception("cascadeAll1XMTest3 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false:
	 *
	 * If the entity instance is new and the persist operation has not been cascaded
	 * to it.
	 */
	@Test
	public void cascadeAll1XMTest4() throws Exception {
		boolean pass = false;

		A a1;
		B bRef;
		try {
			getEntityTransaction().begin();
			a1 = new A("4", "b4", 4);
			bRef = new B("4", "bean4", 4, a1);

			pass = ((!getEntityManager().contains(bRef)) && (!getEntityManager().contains(a1)));

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
			throw new Exception("cascadeAll1XMTest4 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a OneToMany relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 * If X is a pre-existing managed entity, it is ignored by the persist
	 * operation. However, the persist operation is cascaded to entities referenced
	 * by X, if the relationships from X to these other entities is annotated with
	 * cascade=ALL annotation member value.
	 *
	 */
	@Test
	public void cascadeAll1XMTest5() throws Exception {
		boolean pass = false;
		A aRef;
		A aRef1;
		B b1;

		try {
			getEntityTransaction().begin();
			b1 = new B("5", "b5", 5);
			Vector v1 = new Vector();
			v1.add(b1);
			aRef = new A("5", "bean5", 5);
			getEntityManager().persist(aRef);

			if (getEntityManager().contains(aRef)) {
				aRef1 = findA("5");
				aRef1.setBCol(v1);
				getEntityManager().persist(aRef1);
				logger.log(Logger.Level.TRACE, "try to find B");
				B b2 = findB("5");
				if (null != b2) {
					logger.log(Logger.Level.TRACE, "b2 is not null");
					pass = getEntityManager().contains(b2);
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
			throw new Exception("cascadeAll1XMTest5 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest6
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
	public void cascadeAll1XMTest6() throws Exception {
		boolean pass = false;
		A aRef;
		A a2;
		B b1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			b1 = new B("6", "b6", 6);
			Vector v1 = new Vector();
			v1.add(b1);
			aRef = new A("6", "bean6", 6, v1);
			getEntityManager().persist(aRef);

			a2 = findA("6");
			if ((null != a2) && (getEntityManager().contains(a2))) {
				Collection result = a2.getBInfoFromA();
				dumpCollectionDataB(result);
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
			throw new Exception("cascadeAll1XMTest6 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest7
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
	public void cascadeAll1XMTest7() throws Exception {
		boolean pass = false;

		A aRef;
		B b1;

		try {
			logger.log(Logger.Level.TRACE, "New instances");
			getEntityTransaction().begin();
			b1 = new B("7", "b7", 7);
			Vector v1 = new Vector();
			v1.add(b1);
			aRef = new A("7", "bean7", 7, v1);

			getEntityManager().persist(aRef);
			getEntityManager().flush();
			pass = getEntityManager().contains(b1);
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
			throw new Exception("cascadeAll1XMTest7 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:645
	 * 
	 * @test_Strategy: The flush method can be used for force synchronization. The
	 * semantics of the flush operation applied to an entity X is as follows:
	 *
	 * For any new entity Y referenced by a relationship from X, where the
	 * relationship to Y has not been annotated with the cascade member value
	 * cascade=ALL, an exception will be thrown by the container or the transaction
	 * commit will fail.
	 *
	 */
	@Test
	public void cascadeAll1XMTest8() throws Exception {
		boolean pass = false;
		B bRef;
		A a1;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("8", "a8", 8);
			bRef = new B("8", "bean8", 8, a1);

			getEntityManager().persist(bRef);
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "Exception Caught as Expected:" + e.getMessage());
			pass = true;
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
			throw new Exception("cascadeAll1XMTest8 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:647
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
	public void cascadeAll1XMTest9() throws Exception {
		A a1;
		B bRef;
		B b2;
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "New instances");
			a1 = new A("9", "a9", 9);
			getEntityManager().persist(a1);
			bRef = new B("9", "bean9", 9, a1);
			getEntityManager().persist(bRef);
			getEntityManager().flush();

			b2 = findB("9");
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
			throw new Exception("cascadeAll1XMTest9 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:634;
	 * PERSISTENCE:SPEC:575
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
	 * Invoke remove on a OneToMany relationship from X annotated with cascade=ALL
	 * and ensure the remove operation is cascaded.
	 *
	 */
	@Test
	public void cascadeAll1XMTest10() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1XMTest10");
		boolean pass = false;

		getEntityTransaction().begin();
		final B b1 = new B("1", "b1", 10);
		getEntityManager().persist(b1);
		final B b2 = new B("2", "b2", 10);
		getEntityManager().persist(b2);
		final B b3 = new B("3", "b3", 10);
		getEntityManager().persist(b3);
		final B b4 = new B("4", "b4", 10);
		getEntityManager().persist(b4);
		Vector v1 = new Vector();
		v1.add(b1);
		v1.add(b2);
		v1.add(b3);
		v1.add(b4);
		A aRef = new A("10", "bean10", 10, v1);
		getEntityManager().persist(aRef);

		Collection newCol = aRef.getBCol();

		dumpCollectionDataB(newCol);

		try {
			logger.log(Logger.Level.TRACE, "get Instance Status ");

			if ((getInstanceStatus(aRef)) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				try {
					logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
					getEntityManager().remove(findA("10"));
					logger.log(Logger.Level.TRACE, "Remove is immediately visible to the contains method");
					if ((!getEntityManager().contains(aRef)) && (!getEntityManager().contains(b1))
							&& (!getEntityManager().contains(b2)) && (!getEntityManager().contains(b3))
							&& (!getEntityManager().contains(b4))) {
						pass = true;
					}

				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
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

		if (!pass)
			throw new Exception("cascadeAll1XMTest10 failed");
	}

	/*
	 * @testName: cascadeAll1XMTest11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:674
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false: If the remove operation has
	 * been cascaded to it.
	 *
	 */
	@Test
	public void cascadeAll1XMTest11() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin cascadeAll1XMTest11");
		boolean pass = false;

		getEntityTransaction().begin();
		final B b1 = new B("1", "b1", 11);
		getEntityManager().persist(b1);
		final B b2 = new B("2", "b2", 11);
		getEntityManager().persist(b2);
		final B b3 = new B("3", "b3", 11);
		getEntityManager().persist(b3);
		final B b4 = new B("4", "b4", 11);
		getEntityManager().persist(b4);
		Vector v1 = new Vector();
		v1.add(b1);
		v1.add(b2);
		v1.add(b3);
		v1.add(b4);
		A aRef = new A("11", "bean11", 11, v1);
		getEntityManager().persist(aRef);

		Collection newCol = aRef.getBCol();

		dumpCollectionDataB(newCol);

		try {
			logger.log(Logger.Level.TRACE, "get Instance Status ");

			if ((getInstanceStatus(aRef)) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				try {
					logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
					getEntityManager().remove(findA("11"));
					if ((!getEntityManager().contains(aRef)) && (!getEntityManager().contains(b1))
							&& (!getEntityManager().contains(b2)) && (!getEntityManager().contains(b3))
							&& (!getEntityManager().contains(b4))) {
						pass = true;
					}
				} catch (Exception e) {
					logger.log(Logger.Level.TRACE, "Expected Exception :" + e);
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

		if (!pass)
			throw new Exception("cascadeAll1XMTest11 failed");
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

	private void dumpCollectionDataB(final Collection c) {
		logger.log(Logger.Level.TRACE, "collection Data");
		logger.log(Logger.Level.TRACE, "---------------");
		logger.log(Logger.Level.TRACE, "- size=" + c.size());
		Iterator i = c.iterator();
		int elem = 1;
		while (i.hasNext()) {
			B v = (B) i.next();
			logger.log(Logger.Level.TRACE, "- Element #" + elem++);
			logger.log(Logger.Level.TRACE,
					"  id=" + v.getBId() + ", name=" + v.getBName() + ", value=" + v.getBValue());
		}
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
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1XM_BI_BTOB").executeUpdate();
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
