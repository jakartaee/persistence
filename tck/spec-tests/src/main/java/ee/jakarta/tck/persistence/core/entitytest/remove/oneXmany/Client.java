/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.5 06/02/14
 */

package ee.jakarta.tck.persistence.core.entitytest.remove.oneXmany;

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
		return createDeploymentJar("jpa_core_entitytest_remove_oneXmany.jar", pkgNameWithoutSuffix, classes);

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
	 *
	 */

	/*
	 * @testName: remove1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:629
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
	public void remove1XMTest1() throws Exception {
		boolean pass = false;

		logger.log(Logger.Level.TRACE, "Begin remove1XMTest1");
		try {
			getEntityTransaction().begin();
			final B b1 = new B("11", "b1", 1);
			final B b2 = new B("12", "b2", 2);
			final B b3 = new B("13", "b3", 3);
			final B b4 = new B("14", "b4", 4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("1", "a1", 1, v1);

			boolean status = getInstanceStatus(aRef);
			if (!status) {
				logger.log(Logger.Level.TRACE,
						"Instance state is not managed as expected." + "Try invoking remove on it.");
				getEntityManager().remove(aRef);
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Instance state is managed." + "  Unexpected as this is NEW instance.");
				pass = false;
			}

			getEntityTransaction().commit();
		} catch (Exception fe) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception during remove operation. Should have been ignored.",
					fe);
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
			throw new Exception("remove1XMTest1 failed");
	}

	/*
	 * @testName: remove1XMTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:632
	 * 
	 * @test_Strategy: If X is a managed entity, the remove operation causes it to
	 * transition to the removed state. Invoke remove on a managed entity.
	 *
	 */
	@Test
	public void remove1XMTest2() throws Exception {
		boolean pass = false;
		boolean status = false;

		logger.log(Logger.Level.TRACE, "Begin remove1XMTest2");
		try {
			getEntityTransaction().begin();

			final B b1 = new B("21", "b1", 2);
			createB(b1);
			final B b2 = new B("22", "b2", 2);
			createB(b2);
			final B b3 = new B("23", "b3", 2);
			createB(b3);
			final B b4 = new B("24", "b4", 2);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("2", "bean2", 2, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getInstanceStatus(aRef);

			if ((status) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove");
				getEntityManager().remove(findA("2"));
				logger.log(Logger.Level.TRACE, "Call contains after remove");
				if (!getEntityManager().contains(aRef)) {
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
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass)
			throw new Exception("remove1XMTest2 failed");
	}

	/*
	 * @testName: remove1XMTest3
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
	 * Invoke remove on a OneToMany relationship from X annotated with
	 * cascade=REMOVE and ensure the remove operation is cascaded.
	 *
	 */
	@Test
	public void remove1XMTest3() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1XMTest3");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final B b1 = new B("31", "b1", 3);
			createB(b1);
			final B b2 = new B("32", "b2", 3);
			createB(b2);
			final B b3 = new B("33", "b3", 3);
			createB(b3);
			final B b4 = new B("34", "b4", 3);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("3", "bean3", 3, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getInstanceStatus(aRef);

			if ((status) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(findA("3"));
				logger.log(Logger.Level.TRACE, "Remove is immediately visible to the contains method");
				if ((!getEntityManager().contains(aRef)) && (!getEntityManager().contains(b1))
						&& (!getEntityManager().contains(b2)) && (!getEntityManager().contains(b3))
						&& (!getEntityManager().contains(b4))) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Instance is not managed- Unexpected");
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
			throw new Exception("remove1XMTest3 failed");
	}

	/*
	 * @testName: remove1XMTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:636
	 * 
	 * @test_Strategy: If X is a removed entity, it is ignored by the remove
	 * operation. Invoke remove on a removed entity.
	 *
	 */
	@Test
	public void remove1XMTest4() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1XMTest4");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final B b1 = new B("51", "b1", 5);
			createB(b1);
			final B b2 = new B("52", "b2", 5);
			createB(b2);
			final B b3 = new B("53", "b3", 5);
			createB(b3);
			final B b4 = new B("54", "b4", 5);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("5", "bean5", 5, v1);
			createA(aRef);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getEntityManager().contains(aRef);

			if (status) {
				logger.log(Logger.Level.TRACE, "Status is true, try remove");
				getEntityManager().remove(aRef);

				if (!getEntityManager().contains(aRef)) {
					getEntityManager().remove(aRef);
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "contains method returned true; unexpected, test fails.");
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
				logger.log(Logger.Level.ERROR, "Unexpected exception caught trying to remove entity instance :", fe);
			}
		}

		if (!pass)
			throw new Exception("remove1XMTest4 failed");
	}

	/*
	 * @testName: remove1XMTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:637; PERSISTENCE:SPEC:648
	 * 
	 * @test_Strategy: A removed entity will be removed from the database at or
	 * before transaction commit or as a result of a flush operation. Accessing an
	 * entity in the removed state is undefined.
	 *
	 * Remove an entity and force the removal using flush(). Verify the entity is
	 * removed.
	 *
	 * The flush method can be used for force synchronization. The semantics of the
	 * flush operation applied to an entity X is as follows:
	 *
	 * If X is a removed entity, it is removed from the database.
	 */
	@Test
	public void remove1XMTest5() throws Exception {
		boolean pass = false;
		boolean status = false;
		logger.log(Logger.Level.TRACE, "Begin remove1XMTest5");

		try {
			getEntityTransaction().begin();
			final B b1 = new B("61", "b1", 6);
			createB(b1);
			final B b2 = new B("62", "b2", 6);
			createB(b2);
			final B b3 = new B("63", "b3", 6);
			createB(b3);
			final B b4 = new B("64", "b4", 6);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("6", "bean6", 6, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getInstanceStatus(aRef);

			if ((status) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(findA("6"));
				getEntityManager().flush();
				logger.log(Logger.Level.TRACE,
						"A removed entity is removed from the " + " database as a result of the flush operation");
				A newA = findA("6");
				if (newA == null) {
					pass = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Instance is not managed- Unexpected");
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
				logger.log(Logger.Level.ERROR, "Unexpected exception caught trying to remove entity instance :", fe);
			}
		}
		if (!pass)
			throw new Exception("remove1XMTest5 failed");
	}

	/*
	 * @testName: remove1XMTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:673
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false: If the remove method has
	 * been called on the entity.
	 *
	 */
	@Test
	public void remove1XMTest6() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1XMTest6");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final B b1 = new B("71", "b1", 7);
			createB(b1);
			final B b2 = new B("72", "b2", 7);
			createB(b2);
			final B b3 = new B("73", "b3", 7);
			createB(b3);
			final B b4 = new B("74", "b4", 7);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("7", "bean7", 7, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getInstanceStatus(aRef);

			if ((status) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {
				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(findA("7"));
				if ((!getEntityManager().contains(aRef)) && (!getEntityManager().contains(b1))
						&& (!getEntityManager().contains(b2)) && (!getEntityManager().contains(b3))
						&& (!getEntityManager().contains(b4))) {
					pass = true;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Instance is not managed- Unexpected");
				pass = false;

			}

			getEntityTransaction().commit();
		} catch (Exception fe) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception during remove operation. Should have been ignored.",
					fe);
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
			throw new Exception("remove1XMTest6 failed");
	}

	/*
	 * @testName: remove1XMTest7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:674
	 * 
	 * @test_Strategy: The contains method [used to determine whether an entity
	 * instance is in the managed state] returns false: If the remove operation has
	 * been cascaded to it.
	 *
	 */
	@Test
	public void remove1XMTest7() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin remove1XMTest7");
		boolean pass = false;
		boolean status = false;

		try {
			getEntityTransaction().begin();
			final B b1 = new B("81", "b1", 8);
			createB(b1);
			final B b2 = new B("82", "b2", 8);
			createB(b2);
			final B b3 = new B("83", "b3", 8);
			createB(b3);
			final B b4 = new B("84", "b4", 8);
			createB(b4);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			final A aRef = new A("8", "bean8", 8, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			logger.log(Logger.Level.TRACE, "Get Instance Status ");
			status = getInstanceStatus(aRef);

			if ((status) && (newCol.contains(b1)) && (newCol.contains(b2)) && (newCol.contains(b3))
					&& (newCol.contains(b4))) {

				logger.log(Logger.Level.TRACE, "Status is true as expected, try remove()");
				getEntityManager().remove(findA("8"));
				if ((!getEntityManager().contains(aRef)) && (!getEntityManager().contains(b1))
						&& (!getEntityManager().contains(b2)) && (!getEntityManager().contains(b3))
						&& (!getEntityManager().contains(b4))) {
					pass = true;
				}

			} else {
				logger.log(Logger.Level.ERROR, "Instance is not managed- Unexpected");
				pass = false;

			}
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
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (!pass)
			throw new Exception("remove1XMTest7 failed");
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */

	private void createA(final A a) {
		logger.log(Logger.Level.TRACE, "Entered createA method");
		getEntityManager().persist(a);
		getEntityManager().flush();
	}

	private A findA(final String id) {
		logger.log(Logger.Level.TRACE, "Entered findA method");
		return getEntityManager().find(A.class, id);
	}

	private void createB(final B b) {
		logger.log(Logger.Level.TRACE, "Entered createB method");
		getEntityManager().persist(b);
		getEntityManager().flush();
	}

	private boolean getInstanceStatus(final Object o) {
		logger.log(Logger.Level.TRACE, "Entered getInstanceStatus method");
		return getEntityManager().contains(o);
	}

	private void dumpCollectionDataB(final Collection c) {
		logger.log(Logger.Level.TRACE, "Collection Data");
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
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BEJB_1XM_BI_BTOB").executeUpdate();
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
