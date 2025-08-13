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

package ee.jakarta.tck.persistence.core.relationship.unionexmany;

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

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Uni1XMPerson", pkgName + "Uni1XMProject" };
		return createDeploymentJar("jpa_core_relationship_unionexmany.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: uni1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1094; PERSISTENCE:JAVADOC:135;
	 * PERSISTENCE:JAVADOC:91; PERSISTENCE:SPEC:561; PERSISTENCE:SPEC:562;
	 * PERSISTENCE:SPEC:567; PERSISTENCE:SPEC:570; PERSISTENCE:SPEC:571;
	 * PERSISTENCE:SPEC:573; PERSISTENCE:SPEC:961; PERSISTENCE:SPEC:1028;
	 * PERSISTENCE:SPEC:1037; PERSISTENCE:SPEC:1038; PERSISTENCE:SPEC:1039
	 * 
	 * @test_Strategy: RelationShip OneToMany Mapping
	 *
	 */
	@Test
	public void uni1XMTest1() throws Exception {
		logger.log(Logger.Level.TRACE, "Begin uni1X1Test1");
		boolean pass = false;
		try {
			getEntityTransaction().begin();

			Uni1XMProject project1 = new Uni1XMProject(1L, "JavaEE", 500.0F);
			Uni1XMProject project2 = new Uni1XMProject(2L, "Identity", 500.0F);
			Uni1XMPerson person = new Uni1XMPerson(1L, "Duke");

			getEntityManager().persist(project1);
			getEntityManager().persist(project2);
			getEntityManager().persist(person);
			logger.log(Logger.Level.TRACE, "persisted Person and Projects");

			Vector<Uni1XMProject> projects = new Vector<Uni1XMProject>();
			projects.add(project1);
			projects.add(project2);

			person.setProjects(projects);
			getEntityManager().merge(person);
			logger.log(Logger.Level.TRACE, "merged Contents of Person Entity");

			Uni1XMPerson newPerson = getEntityManager().find(Uni1XMPerson.class, 1L);
			boolean pass1 = false;
			boolean pass2 = false;

			if (newPerson != null) {

				Collection<Uni1XMProject> newProjects = newPerson.getProjects();
				for (Uni1XMProject prj : newProjects) {
					if (prj.getName().equals("Identity")) {
						pass1 = true;
					} else if (prj.getName().equals("JavaEE")) {
						pass2 = true;
					}
				}
			}

			if (pass1 && pass2) {
				logger.log(Logger.Level.TRACE, "Expected results received");
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
			throw new Exception("uni1XMTest1 failed");
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
			getEntityManager().createNativeQuery("DELETE FROM UNI1XMPERSON_UNI1XMPROJECT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM UNI1XMPROJECT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM UNI1XMPERSON").executeUpdate();
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
