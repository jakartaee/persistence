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

package ee.jakarta.tck.persistence.core.annotations.entity;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static Coffee cRef[] = new Coffee[5];

	public Client() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Coffee" };
		return createDeploymentJar("jpa_core_annotations_entity.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();
			removeTestData();
			logger.log(Logger.Level.TRACE, "Create Test data");
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: annotationEntityTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
	 * PERSISTENCE:JAVADOC:29; PERSISTENCE:SPEC:762; PERSISTENCE:SPEC:402;
	 * PERSISTENCE:SPEC:404;
	 * 
	 * @test_Strategy: The name annotation element defaults to the unqualified name
	 * of the entity class. This name is used to refer to the entities in queries.
	 * 
	 * Name the entity using a lower case name and ensure the query can be executed
	 * with the lower case entity name as the abstract schema name.
	 * 
	 */
	@Test
	public void annotationEntityTest1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationEntityTest1");
		boolean pass = true;
		List c = null;

		try {
			getEntityTransaction().begin();
			final String[] expectedBrands = new String[] { "vanilla creme", "mocha", "hazelnut", "decaf",
					"breakfast blend" };

			logger.log(Logger.Level.TRACE, "find coffees by brand name");
			c = getEntityManager().createQuery("Select c.brandName from cof c ORDER BY c.brandName DESC")
					.setMaxResults(10).getResultList();

			final String[] result = (String[]) (c.toArray(new String[c.size()]));
			logger.log(Logger.Level.TRACE, "Compare results of Coffee Brand Names");
			pass = Arrays.equals(expectedBrands, result);

			if (!pass) {
				logger.log(Logger.Level.ERROR, "Did not get expected results.  Expected 5 Coffees : "
						+ "vanilla creme, mocha, hazelnut, decaf, breakfast blend. " + " Received: " + c.size());
				Iterator it = c.iterator();
				while (it.hasNext()) {
					logger.log(Logger.Level.TRACE, " Coffee Brand Name: " + it.next());
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
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
			throw new Exception("annotationEntityTest1 failed");
	}

	/*
	 * @testName: annotationEntityTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:993; PERSISTENCE:SPEC:995;
	 * PERSISTENCE:JAVADOC:29
	 * 
	 * @test_Strategy: The name annotation element defaults to the unqualified name
	 * of the entity class. This name is used to refer to the entities in queries.
	 * 
	 * Name the entity using a different name than the entity class name and ensure
	 * the query can be executed with the lower case entity name as the abstract
	 * schema name selecting teh
	 * 
	 */
	@Test
	public void annotationEntityTest2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin annotationEntityTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		List c = null;

		try {
			getEntityTransaction().begin();
			final Integer[] expectedPKs = new Integer[] { 21, 22, 23, 24, 25 };

			logger.log(Logger.Level.TRACE, "find all coffees");
			c = getEntityManager().createQuery("Select c from cof c").setMaxResults(10).getResultList();

			if (c.size() != 5) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 5 references, got: " + c.size());
				pass1 = false;
			} else if (pass1) {
				logger.log(Logger.Level.TRACE, "Expected size received, verify contents . . . ");
				Iterator i = c.iterator();
				int foundCof = 0;
				while (i.hasNext()) {
					logger.log(Logger.Level.TRACE, "Check List for expected coffees");
					Coffee o = (Coffee) i.next();
					for (int l = 0; l < 5; l++) {
						if (expectedPKs[l].equals(o.getId())) {
							logger.log(Logger.Level.TRACE, "Found coffee with PK: " + o.getId());
							foundCof++;
							break;
						}
					}
				}
				if (foundCof != 5) {
					logger.log(Logger.Level.ERROR, "anotationEntityTest2: Did not get expected results");
					pass2 = false;
				} else {
					logger.log(Logger.Level.TRACE, "Expected results received");
					pass2 = true;
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
			pass2 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("annotationEntityTest1 failed");
	}

	/*
	 * 
	 * Business Methods to set up data for Test Cases
	 */

	private void createTestData() throws Exception {
		try {

			logger.log(Logger.Level.TRACE, "createTestData");

			getEntityTransaction().begin();
			logger.log(Logger.Level.TRACE, "Create 5 Coffees");
			cRef[0] = new Coffee(21, "hazelnut", 1.0F);
			cRef[1] = new Coffee(22, "vanilla creme", 2.0F);
			cRef[2] = new Coffee(23, "decaf", 3.0F);
			cRef[3] = new Coffee(24, "breakfast blend", 4.0F);
			cRef[4] = new Coffee(25, "mocha", 5.0F);

			logger.log(Logger.Level.TRACE, "Start to persist coffees ");
			for (Coffee coffee : cRef) {
				getEntityManager().persist(coffee);
				logger.log(Logger.Level.TRACE, "persisted coffee " + coffee);
			}
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanup");
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
			getEntityManager().createNativeQuery("DELETE FROM COFFEE").executeUpdate();
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
