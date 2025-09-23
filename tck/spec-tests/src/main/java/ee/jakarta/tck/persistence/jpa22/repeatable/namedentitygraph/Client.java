/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.repeatable.namedentitygraph;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	Employee3[] empRef = new Employee3[5];

	Department[] deptRef = new Department[2];

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Employee", pkgName + "Employee2",
				pkgName + "Employee3" };
		return createDeploymentJar("jpa_jpa22_repeatable_namedentitygraph.jar", pkgNameWithoutSuffix,
				(String[]) classes);

	}

	@BeforeEach
	public void setupEmployeeData() throws Exception {
		logger.log(Logger.Level.TRACE, "setupOrderData");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createEmployeeData();
			displayMap(new Properties());
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanupEmployeeData() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "Cleanup data");
			removeTestData();
			cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	/*
	 * @testName: entityGraphGetNameNoNameExistsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3417;
	 * 
	 * @test_Strategy: Use getName to get the name of the named entity graph in the
	 * Employee2 entity that has no name
	 */
	@Test
	public void entityGraphGetNameNoNameExistsTest() throws Exception {
		boolean pass = false;

		List<EntityGraph<? super Employee2>> egs = getEntityManager().getEntityGraphs(Employee2.class);
		if (egs.size() == 1) {
			EntityGraph<?> e = egs.get(0);
			if (e.getName().equals("Employee2")) {
				logger.log(Logger.Level.TRACE, "Received expected name:" + e.getName());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected name: Employee2, actual:" + e.getName());
			}
		} else {
			logger.log(Logger.Level.ERROR, "Expected 1 graph to be returned, instead got:" + egs.size());
		}

		if (!pass) {
			throw new Exception("entityGraphGetNameNoNameExistsTest failed");
		}
	}

	/*
	 * @testName: getNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3417;
	 * 
	 * @test_Strategy: Use getName to get the name of the entity graph
	 */
	@Test
	public void getNameTest() throws Exception {
		boolean pass = false;

		EntityGraph<Employee2> eg = getEntityManager().createEntityGraph(Employee2.class);

		if (eg.getName() == null) {
			logger.log(Logger.Level.TRACE, "Received expected null");
			pass = true;
		} else {
			logger.log(Logger.Level.ERROR, "Expected name: null, actual:" + eg.getName());
		}

		if (!pass) {
			throw new Exception("getNameTest failed");
		}
	}

	/*
	 * @testName: getEntityGraphsClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3417;
	 * 
	 * @test_Strategy: Use getEntityGraph to get the named entity graphs in the
	 * Employee entity
	 */
	@Test
	public void getEntityGraphsClassTest() throws Exception {
		boolean pass = false;
		List<String> expected = new ArrayList<String>();
		expected.add("first_last_graph");
		expected.add("last_salary_graph");
		expected.add("lastname_department_subgraphs");

		List<String> actual = new ArrayList<String>();

		List<EntityGraph<? super Employee3>> eg = getEntityManager().getEntityGraphs(Employee3.class);
		if (eg.size() > 0) {
			for (EntityGraph<?> e : eg) {
				actual.add(e.getName());
			}
			if (actual.containsAll(expected) && expected.containsAll(actual) && actual.size() == expected.size()) {
				logger.log(Logger.Level.TRACE, "Received expected results");
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected results");
				for (String s : expected) {
					logger.log(Logger.Level.ERROR, "expected:" + s);
				}
				logger.log(Logger.Level.ERROR, "Actual results");
				for (String s : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + s);
				}
			}
		} else {
			logger.log(Logger.Level.ERROR, "No named entity graphs were returned eventhough they exist in entity");
		}

		if (!pass) {
			throw new Exception("getEntityGraphsClassTest failed");
		}
	}

	private void createEmployeeData() {

		try {
			getEntityTransaction().begin();
			logger.log(Logger.Level.INFO, "Creating Employees");

			final Date d1 = getUtilDate("2000-02-14");
			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");
			for (Department d : deptRef) {
				getEntityManager().persist(d);
				logger.log(Logger.Level.TRACE, "persisted department:" + d);
			}

			empRef[0] = new Employee3(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef[0].setDepartment(deptRef[0]);
			empRef[1] = new Employee3(2, "Arthur", "Frechette", d2, (float) 35000.0);
			empRef[1].setDepartment(deptRef[0]);
			empRef[2] = new Employee3(3, "Shelly", "McGowan", d3, (float) 50000.0);
			empRef[2].setDepartment(deptRef[1]);
			empRef[3] = new Employee3(4, "Robert", "Bissett", d4, (float) 55000.0);
			empRef[3].setDepartment(deptRef[1]);
			empRef[4] = new Employee3(5, "Stephen", "DMilla", d5, (float) 25000.0);
			empRef[4].setDepartment(deptRef[1]);

			for (Employee3 e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logger.log(Logger.Level.TRACE, "persisted employee3:" + e);
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
	}

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
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
