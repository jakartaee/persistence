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

package ee.jakarta.tck.persistence.core.entitytest.detach.manyXmany;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Iterator;

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
		return createDeploymentJar("jpa_core_entitytest_detach_manyXmany.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: detachMXMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:659; PERSISTENCE:SPEC:662;
	 * 
	 * @test_Strategy: The merge operation allows for the propagation of state from
	 * detached entities onto persistence entities managed by the EntityManager. The
	 * semantics of the merge operation applied to entity X are as follows:
	 *
	 * If X is a detached entity, the state of X is copied onto a pre-existing
	 * managed entity instance X1 of the same identity or a new managed copy of X1
	 * is created.
	 *
	 * If X is a managed entity, it is ignored by the merge operation however, the
	 * merge operation is cascaded to entities referenced by relationships from X if
	 * these relationships have been annotated with the cascade element value
	 *
	 */
	@Test
	public void detachMXMTest1() throws Exception {

		final A aRef = new A("1", "a1", 1);
		final B b1 = new B("1", "b1", 1);
		final B b2 = new B("2", "b2", 2);
		int foundB = 0;
		final String[] expectedResults = new String[] { "1", "2" };
		boolean pass1 = true;
		boolean pass2 = false;

		try {

			logger.log(Logger.Level.TRACE, "Begin detachMXMTest1");
			createA(aRef);

			logger.log(Logger.Level.TRACE, "Call clean to detach");
			clearCache();

			getEntityTransaction().begin();

			if (!getEntityManager().contains(aRef)) {
				logger.log(Logger.Level.TRACE, "Status is false as expected, try merge");
				getEntityManager().merge(aRef);
				aRef.getBCol().add(b1);
				aRef.getBCol().add(b2);
				getEntityManager().merge(aRef);

				logger.log(Logger.Level.TRACE, "findA and getBCol");
				A a1 = getEntityManager().find(A.class, "1");
				Collection newCol = a1.getBCol();

				if (newCol.size() != 2) {
					logger.log(Logger.Level.ERROR, "detachMXMTest1: Did not get expected results."
							+ "Expected Collection Size of 2 B entities, got: " + newCol.size());
					pass1 = false;
				} else if (pass1) {

					Iterator i1 = newCol.iterator();
					while (i1.hasNext()) {
						logger.log(Logger.Level.TRACE, "Check Collection B entities");
						B c1 = (B) i1.next();

						for (int l = 0; l < 2; l++) {
							if (expectedResults[l].equals((String) c1.getBId())) {
								logger.log(Logger.Level.TRACE, "Found B Entity : " + (String) c1.getBName());
								foundB++;
								break;
							}
						}
					}
				}

			} else {
				logger.log(Logger.Level.TRACE, "entity is not detached, cannot proceed with test.");
				pass1 = false;
				pass2 = false;
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception caught during commit:", e);
			pass1 = false;
			pass2 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logger.log(Logger.Level.ERROR, "Unexpected exception rolling back TX:", fe);
			}
		}

		if (foundB != 2) {
			logger.log(Logger.Level.ERROR, "detachMXMTest1: Did not get expected results");
			pass2 = false;
		} else {
			logger.log(Logger.Level.TRACE, "Expected results received");
			pass2 = true;
		}

		if (!pass1 || !pass2)
			throw new Exception("detachMXMTest1 failed");
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
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
			getEntityManager().createNativeQuery("DELETE FROM FKEYS_MXM_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM AEJB_MXM_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BEJB_MXM_BI_BTOB").executeUpdate();
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
