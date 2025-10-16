/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.override.entity;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private final static Long ID = 9l;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] xmlFiles = { ORM_XML };
		String[] classes = { pkgName + "NameOnlyInAnnotation", pkgName + "NameOnlyInXML", pkgName + "NameOverride",
				pkgName + "NoEntityAnnotation" };
		return createDeploymentJar("jpa_core_override_entity.jar", pkgNameWithoutSuffix, classes, xmlFiles);

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
	 * @testName: testNameOnlyInXML
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:502; PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504;
	 * PERSISTENCE:SPEC:505; PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507;
	 * PERSISTENCE:SPEC:508; PERSISTENCE:SPEC:509; PERSISTENCE:SPEC:510;
	 * PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:513;
	 * PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515; PERSISTENCE:SPEC:516;
	 * PERSISTENCE:SPEC:517; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519;
	 * PERSISTENCE:SPEC:520; PERSISTENCE:SPEC:521; PERSISTENCE:SPEC:522;
	 * PERSISTENCE:SPEC:523; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:526; PERSISTENCE:SPEC:527; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:529; PERSISTENCE:SPEC:530; PERSISTENCE:SPEC:531;
	 * PERSISTENCE:SPEC:532; PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534;
	 * PERSISTENCE:SPEC:1004;
	 * 
	 * @test_Strategy: Table name is specified only in orm.xml and the test is
	 * performed by retrieving data from that table.
	 */
	@Test
	public void testNameOnlyInXML() throws Exception {

		getEntityTransaction().begin();
		NameOnlyInXML entity = new NameOnlyInXML();
		entity.setId(ID);
		getEntityManager().persist(entity);
		getEntityManager().flush();
		try {
			List result = getEntityManager().createNamedQuery("findAll").getResultList();
			logger.log(Logger.Level.TRACE, "Result of the entity is " + result.size());
			if (result.size() == 1) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected the size to be 1 " + " but it is -" + result.size());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNameOnlyInXML" + e);
		} finally {
			getEntityManager().remove(entity);
			getEntityTransaction().commit();
		}
	}

	/*
	 * @testName: testNameOnlyInAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:502; PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504;
	 * PERSISTENCE:SPEC:505; PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507;
	 * PERSISTENCE:SPEC:508; PERSISTENCE:SPEC:509; PERSISTENCE:SPEC:510;
	 * PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:513;
	 * PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515; PERSISTENCE:SPEC:516;
	 * PERSISTENCE:SPEC:517; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519;
	 * PERSISTENCE:SPEC:520; PERSISTENCE:SPEC:521; PERSISTENCE:SPEC:522;
	 * PERSISTENCE:SPEC:523; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:526; PERSISTENCE:SPEC:527; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:529; PERSISTENCE:SPEC:530; PERSISTENCE:SPEC:531;
	 * PERSISTENCE:SPEC:532; PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534;
	 * PERSISTENCE:SPEC:1004;
	 * 
	 * @test_Strategy: Entity name is specified in the entity using annotation. Test
	 * is executed by retrieving data from the table.
	 */
	@Test
	public void testNameOnlyInAnnotation() throws Exception {

		List result = getEntityManager().createQuery("SELECT m FROM NAMEONLYINANNOTATION" + " m").getResultList();
		if (result.size() == 0) {
			logger.log(Logger.Level.TRACE, "Test Passed");
		} else {
			throw new Exception("Expected the size to be 0 " + " but it is -" + result.size());
		}
	}

	/*
	 * @testName: testNameOverride
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:502; PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504;
	 * PERSISTENCE:SPEC:505; PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507;
	 * PERSISTENCE:SPEC:508; PERSISTENCE:SPEC:509; PERSISTENCE:SPEC:510;
	 * PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:513;
	 * PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515; PERSISTENCE:SPEC:516;
	 * PERSISTENCE:SPEC:517; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519;
	 * PERSISTENCE:SPEC:520; PERSISTENCE:SPEC:521; PERSISTENCE:SPEC:522;
	 * PERSISTENCE:SPEC:523; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:526; PERSISTENCE:SPEC:527; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:529; PERSISTENCE:SPEC:530; PERSISTENCE:SPEC:531;
	 * PERSISTENCE:SPEC:532; PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534;
	 * PERSISTENCE:SPEC:1004;
	 * 
	 * @test_Strategy: Entity is given a name using annotation. But also is
	 * overriden by another name in orm.xml. Test is executed by retrieving data
	 * from the overriden table name.
	 */
	@Test
	public void testNameOverride() throws Exception {

		List result = getEntityManager().createQuery("SELECT n FROM NAMEOVERRIDE" + " n").getResultList();
		if (result.size() == 0) {
			logger.log(Logger.Level.TRACE, "Test Passed");
		} else {
			throw new Exception("Expected the size to be 0 " + " but it is -" + result.size());
		}
	}

	/*
	 * @testName: testNoEntityAnnotation
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:502; PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504;
	 * PERSISTENCE:SPEC:505; PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507;
	 * PERSISTENCE:SPEC:508; PERSISTENCE:SPEC:509; PERSISTENCE:SPEC:510;
	 * PERSISTENCE:SPEC:511; PERSISTENCE:SPEC:512; PERSISTENCE:SPEC:513;
	 * PERSISTENCE:SPEC:514; PERSISTENCE:SPEC:515; PERSISTENCE:SPEC:516;
	 * PERSISTENCE:SPEC:517; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:519;
	 * PERSISTENCE:SPEC:520; PERSISTENCE:SPEC:521; PERSISTENCE:SPEC:522;
	 * PERSISTENCE:SPEC:523; PERSISTENCE:SPEC:524; PERSISTENCE:SPEC:525;
	 * PERSISTENCE:SPEC:526; PERSISTENCE:SPEC:527; PERSISTENCE:SPEC:528;
	 * PERSISTENCE:SPEC:529; PERSISTENCE:SPEC:530; PERSISTENCE:SPEC:531;
	 * PERSISTENCE:SPEC:532; PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:534;
	 * PERSISTENCE:SPEC:1004;
	 * 
	 * @test_Strategy: Query name and the query is specified in an entity which is
	 * declared as entity only in orm.xml without actually specifying it using
	 * annotation. Test is performed by select from the entity name that is
	 * specified in the orm.xml.
	 */
	@Test
	public void testNoEntityAnnotation() throws Exception {

		getEntityTransaction().begin();
		NoEntityAnnotation entity = new NoEntityAnnotation();
		entity.setId(ID);
		getEntityManager().persist(entity);
		getEntityManager().flush();
		try {
			List result = getEntityManager().createNamedQuery("findAllNoEntityAnnotation").getResultList();
			logger.log(Logger.Level.TRACE, "Result of the entity is " + result.size());
			if (result.size() == 1) {
				logger.log(Logger.Level.TRACE, "Test Passed");
			} else {
				throw new Exception("Expected the size to be 1 " + " but it is -" + result.size());
			}
		} catch (Exception e) {
			throw new Exception("Exception thrown while testing testNoEntityAnnotation" + e);
		} finally {
			getEntityManager().remove(entity);
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
			getEntityManager().createNativeQuery("DELETE FROM NAMEONLYINXML").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM NOENTITYANNOTATION").executeUpdate();
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
