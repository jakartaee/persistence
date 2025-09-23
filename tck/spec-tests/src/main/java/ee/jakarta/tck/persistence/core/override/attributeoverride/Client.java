/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.attributeoverride;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final int ENTITY_ID = 3039;

	private static final String NAME = "Cheese";

	private static final String PUBLISHER = "Johnson";

	private static final int COST = 20;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "Book", pkgName + "LawBook" };
		return createDeploymentJar("jpa_core_override_attributeoverride.jar", pkgNameWithoutSuffix, classes, xmlFiles);

	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception:test failed ", e);
		}
	}

	/*
	 * @testName: testNoAttributeOverrideAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:594; PERSISTENCE:SPEC:596;
	 * PERSISTENCE:SPEC:597; PERSISTENCE:SPEC:598; PERSISTENCE:SPEC:599;
	 * PERSISTENCE:SPEC:600; PERSISTENCE:SPEC:601;
	 * 
	 * @test_Strategy: LawBook is an entity which extends a class Book. A column
	 * "name" is overriden in Orm.xml as "BOOK_NAME". The following test tests for
	 * the same.
	 */
	@Test
	public void testNoAttributeOverrideAnnotation() throws Exception {

		LawBook book = new LawBook();
		getEntityTransaction().begin();
		book.setCategory("Motivational");
		book.setId(ENTITY_ID);
		book.setName(NAME);
		book.setPublisher(PUBLISHER);
		book.setCost(COST);
		getEntityManager().persist(book);
		getEntityManager().flush();
		try {
			List result = getEntityManager().createQuery("SELECT b FROM LawBook b where b.name= " + ":name")
					.setParameter("name", NAME).getResultList();
			if (result.size() == 1) {
				logger.log(Logger.Level.TRACE, "test Overriding Attributes passed");
			} else {
				throw new Exception("Expected the size to be 1 " + " but it is -" + result.size());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoAttributeOverrideAnnotation" + e);
		} finally {
			getEntityManager().remove(book);
			getEntityTransaction().commit();
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
			getEntityManager().createNativeQuery("DELETE FROM LAWBOOK").executeUpdate();
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
