/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkey;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Client1 extends Client {

	public Client1() {
	}

	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Employee", pkgName + "Employee2", pkgName + "Employee3",
				pkgName + "Employee4" };
		return createDeploymentJar("jpa_core_annotations_mapkey1.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setupCreateTestData() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: annotationMapKeyTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:1980;
	 * 
	 * @test_Strategy: The MapKey annotation is used to specify the map key for
	 * associations of type java.util.Map.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 * 
	 * Execute a query returning Employees objects.
	 * 
	 */
	@Test
	public void annotationMapKeyTest1() throws Exception {

		boolean pass = true;

		try {
			List<Integer> expected = new ArrayList<Integer>();
			List<Integer> actual = new ArrayList<Integer>();

			expected.add(empRef[0].getId());
			expected.add(empRef[2].getId());
			expected.add(empRef[4].getId());

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "find Employees belonging to Department: Marketing");
			List l = getEntityManager().createQuery("Select e from Employee e where e.department.name = 'Marketing'")
					.getResultList();

			for (Object o : l) {
				Employee e = (Employee) o;
				actual.add(e.getId());
			}

			Collections.sort(actual);
			if (expected.equals(actual)) {
				logger.log(Logger.Level.TRACE, "Received expected employees");
			} else {
				logger.log(Logger.Level.ERROR, "Expected id values were:");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "id: " + i);
				}
				logger.log(Logger.Level.ERROR, "actual id values were:");
				Collections.sort(actual);
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "id: " + i);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
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
			throw new Exception("annotationMapKeyTest1 failed");
	}

	/*
	 * @testName: annotationMapKeyTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101
	 * 
	 * @test_Strategy: The MapKey annotation is used to specify the map key for
	 * associations of type java.util.Map.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 *
	 * Execute a query returning Employee IDs.
	 */
	@Test
	public void annotationMapKeyTest2() throws Exception {

		boolean pass = true;

		try {
			List<Integer> expected = new ArrayList<Integer>();
			List<Integer> actual = new ArrayList<Integer>();

			expected.add(empRef[1].getId());
			expected.add(empRef[3].getId());

			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "find Employees belonging to Department: Marketing");
			List l = getEntityManager()
					.createQuery(
							"Select e.id from Employee e where e.department.name = 'Administration' ORDER BY e.id DESC")
					.getResultList();

			for (Object o : l) {
				Integer i = (Integer) o;
				actual.add(i);
			}

			Collections.sort(actual);
			if (expected.equals(actual)) {
				logger.log(Logger.Level.TRACE, "Received expected employees");
			} else {
				logger.log(Logger.Level.ERROR, "Expected id values were:");
				for (Integer i : expected) {
					logger.log(Logger.Level.ERROR, "id: " + i);
				}
				logger.log(Logger.Level.ERROR, "actual id values were:");
				Collections.sort(actual);
				for (Integer i : actual) {
					logger.log(Logger.Level.ERROR, "id: " + i);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
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
			throw new Exception("annotationMapKeyTest2 failed");
	}
}
