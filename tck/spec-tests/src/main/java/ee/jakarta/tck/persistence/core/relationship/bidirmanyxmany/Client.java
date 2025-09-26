/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.relationship.bidirmanyxmany;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Vector;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "BiDirMXMPerson", pkgName + "BiDirMXMProject" };
		return createDeploymentJar("jpa_core_relationship_bidirmanyxmany.jar", pkgNameWithoutSuffix, classes);

	}

	public Client() {
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
	 * @testName: biDirMXMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 *
	 * @test_Strategy: Bi-Directional RelationShip OneToMany Mapping
	 *
	 */
	@Test
	public void biDirMXMTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin biDirMXMTest1");
		boolean pass = false;
		try {

			getEntityTransaction().begin();

			BiDirMXMProject project1 = new BiDirMXMProject(1L, "JavaEE", 500.0F);
			BiDirMXMProject project2 = new BiDirMXMProject(2L, "Identity", 300F);

			BiDirMXMPerson person1 = new BiDirMXMPerson(1L, "Duke");
			BiDirMXMPerson person2 = new BiDirMXMPerson(2L, "Foo");

			getEntityManager().persist(project1);
			getEntityManager().persist(project2);
			getEntityManager().persist(person1);
			getEntityManager().persist(person2);

			Vector<BiDirMXMProject> projects = new Vector<BiDirMXMProject>();
			projects.add(project1);
			projects.add(project2);

			person1.setProjects(projects);

			Vector<BiDirMXMProject> projects2 = new Vector<BiDirMXMProject>();
			projects2.add(project2);

			person2.setProjects(projects2);

			getEntityManager().merge(person1);
			getEntityManager().merge(person2);

			getEntityManager().flush();
			getEntityTransaction().commit();
			logger.log(Logger.Level.TRACE, "persisted Persons and Project");

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "persisted Persons and Project");

			boolean pass1 = false;
			boolean pass2 = false;

			BiDirMXMPerson newPerson = getEntityManager().find(BiDirMXMPerson.class, 1L);

			if (newPerson != null) {

				Collection<BiDirMXMProject> newProjects = newPerson.getProjects();
				for (BiDirMXMProject prj : newProjects) {
					if (prj.getName().equals("Identity")) {
						logger.log(Logger.Level.TRACE, "Found Searched Project Identity");
						pass1 = true;
					} else if (prj.getName().equals("JavaEE")) {
						logger.log(Logger.Level.TRACE, "Found Searched Project JavaEE");
						pass2 = true;
					}
				}

			}

			boolean pass3 = false;
			BiDirMXMPerson newPerson2 = getEntityManager().find(BiDirMXMPerson.class, 2L);

			if (newPerson2 != null) {

				Collection<BiDirMXMProject> newProjects2 = newPerson2.getProjects();
				for (BiDirMXMProject prj : newProjects2) {
					if (prj.getName().equals("Identity")) {
						logger.log(Logger.Level.TRACE, "Found Searched Project for Person id=2");
						pass3 = true;
					}
				}

			}

			if (pass1 && pass2 && pass3) {
				logger.log(Logger.Level.TRACE, "biDirMXMTest1: Expected results received");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Unexpected results received");
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
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass) {
			throw new Exception("biDirMXMTest1 failed");
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
			getEntityManager().createNativeQuery("DELETE FROM BIDIRMXMPERSON_BIDIRMXMPROJECT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BIDIRMXMPERSON").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BIDIRMXMPROJECT").executeUpdate();
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
