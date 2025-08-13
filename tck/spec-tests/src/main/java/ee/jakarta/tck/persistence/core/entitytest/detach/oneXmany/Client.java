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

package ee.jakarta.tck.persistence.core.entitytest.detach.oneXmany;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

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
	 * @testName: detach1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:742;
	 * PERSISTENCE:JAVADOC:31
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows:
	 *
	 * If X is a detached object and the persist method is invoked on it, an
	 * IllegalArgumentException is thrown or the commit() will fail. Check for an
	 * IllegalArgumentException, or an EntityExistsException. Invoke persist on a
	 * detached entity.
	 *
	 */
	@Test
	public void detach1XMTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin detach1XMTest1");
		boolean pass = false;
		final A aRef = new A("1", "a1", 1);

		try {
			createA(aRef);
			clearCache();

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Persist Instance");

			logger.log(Logger.Level.TRACE, "Call contains to determined if the instance is detached");

			if (getEntityManager().contains(aRef)) {
				logger.log(Logger.Level.TRACE, "entity is not detached, cannot proceed with test.");
				pass = false;
			} else {
				try {
					logger.log(Logger.Level.TRACE, "Status is false as expected, try perist()");
					getEntityManager().persist(aRef);
				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE,
							"IllegalArgumentException thrown trying to" + " persist a detached entity", iae);
					pass = true;
				} catch (EntityExistsException eee) {
					logger.log(Logger.Level.TRACE,
							"entityExistsException thrown trying to" + " persist a detached entity", eee);
					pass = true;
				}
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "or, Transaction commit will fail. " + " Test the commit failed by testing"
					+ " the transaction is marked for rollback");

			if ((!pass) && (e instanceof jakarta.transaction.TransactionRolledbackException
					|| e instanceof jakarta.persistence.PersistenceException)) {
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
			throw new Exception("detach1XMTest1 failed");
	}

	/*
	 * @testName: detach1XMTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:625; PERSISTENCE:SPEC:635
	 * 
	 * @test_Strategy: If X is a detached entity, invoking the remove method on it
	 * will cause an IllegalArgumentException to be thrown or the transaction commit
	 * will fail. Invoke remove on a detached entity.
	 *
	 */
	@Test
	public void detach1XMTest2() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin detach1XMTest2");
		boolean pass = false;

		try {
			B b1 = new B("1", "b1", 2);
			B b2 = new B("2", "b2", 2);
			B b3 = new B("3", "b3", 2);
			B b4 = new B("4", "b4", 2);
			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			A aRef = new A("2", "bean2", 2, v1);
			createA(aRef);

			Collection newCol = aRef.getBCol();
			dumpCollectionDataB(newCol);

			clearCache();

			logger.log(Logger.Level.TRACE, "Begin Transaction and make sure instance is detached prior to remove");
			getEntityTransaction().begin();

			if ((!getEntityManager().contains(aRef)) && (newCol.contains(b1)) && (newCol.contains(b2))
					&& (newCol.contains(b3)) && (newCol.contains(b4))) {

				try {
					logger.log(Logger.Level.TRACE, "aref is detached, Try remove");
					getEntityManager().remove(aRef);

				} catch (IllegalArgumentException iae) {
					logger.log(Logger.Level.TRACE, "IllegalArgumentException thrown trying to remove a detached entity",
							iae);
					pass = true;
				}
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.TRACE, "or, Transaction commit will fail.  Test the commit failed by testing"
					+ " the transaction is marked for rollback");

			if ((!pass) && (e instanceof jakarta.transaction.TransactionRolledbackException
					|| e instanceof jakarta.persistence.PersistenceException)) {
				pass = true;
			}

		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR,
						"Unexpected exception caught trying to " + " remove entity instance :" + fe);
			}
		}

		if (!pass)
			throw new Exception("detach1XMTest2 failed");

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

	private void dumpCollectionDataB(Collection c) {
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
