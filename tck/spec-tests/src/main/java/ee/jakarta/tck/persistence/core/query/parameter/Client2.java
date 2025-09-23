/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.query.parameter;

import java.lang.System.Logger;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Query;

public class Client2 extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	protected final Employee empRef[] = new Employee[5];

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client2.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Employee" };
		return createDeploymentJar("jpa_core_relationship_annotations2.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeEach
	public void setupEmployee() throws Exception {
		logger.log(Logger.Level.TRACE, "setupEmployee");
		try {
			super.setup();
			createDeployment();
			createEmployeeData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: parameterPositionalTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
	 * PERSISTENCE:SPEC:1640;
	 * 
	 * @test_Strategy: Create a query with a having clause with a positional
	 * parameter and retrieve information about the parameter.
	 */
	@Test
	public void parameterPositionalTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT e.id FROM Employee e WHERE e.id > ?2 GROUP BY e.id HAVING e.id <=?1")
					.setParameter(2, 2).setParameter(1, 4).getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "3";
			expectedPKs[1] = "4";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Expected 1 result, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterPositionalTest failed");
	}

	/*
	 * @testName: parameterUpdateTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1636; PERSISTENCE:SPEC:1638;
	 * PERSISTENCE:SPEC:1639;
	 * 
	 * @test_Strategy: Create an update query with a positional parameter and
	 * retrieve information about the parameter.
	 */
	@Test
	public void parameterUpdateTest() throws Exception {
		boolean pass = false;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			Query q = getEntityManager().createQuery("UPDATE Employee e SET e.firstName=?1, e.lastName=?1 WHERE e.id=1")
					.setParameter(1, "foo");

			int result_size = q.executeUpdate();
			if (result_size == 1) {
				logger.log(Logger.Level.TRACE, "Updated 1 rows");
			}

			doFlush();
			clearCache();
			Employee emp = getEntityManager().find(Employee.class, 1);

			if (emp.getFirstName().equals("foo") && emp.getLastName().equals("foo")) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + emp.toString());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected: firstName=foo, lastName=foo, actual: firstName="
						+ emp.getFirstName() + ", lastName=" + emp.getLastName());
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterUpdateTest failed");
	}

	/*
	 * @testName: parameterCaseTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1641;
	 * 
	 * @test_Strategy: Create a query with a name parameter using different cases
	 * and retrieve information about the parameter.
	 */
	@Test
	public void parameterCaseTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			result = getEntityManager()
					.createQuery("SELECT e.id FROM Employee e WHERE e.id > :Id GROUP BY e.id HAVING e.id <=:iD")
					.setParameter("Id", 2).setParameter("iD", 4).getResultList();

			expectedPKs = new String[2];
			expectedPKs[0] = "3";
			expectedPKs[1] = "4";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Expected 1 result, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterCaseTest failed");
	}

	/*
	 * @testName: parameterNamedParameterTwiceTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1642;
	 * 
	 * @test_Strategy: Create a query using a name parameter twice and retrieve
	 * information about the parameter.
	 */
	@Test
	public void parameterNamedParameterTwiceTest() throws Exception {
		boolean pass = false;
		List result;
		String expectedPKs[];

		try {
			getEntityTransaction().begin();
			Query q = getEntityManager().createQuery("SELECT e FROM Employee e WHERE e.id >=:ID AND e.id <=:ID");
			result = q.setParameter("ID", 2).getResultList();

			expectedPKs = new String[1];
			expectedPKs[0] = "2";

			if (!checkEntityPK(result, expectedPKs)) {
				logger.log(Logger.Level.ERROR, "Expected 1 result, got: " + result.size());
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
				pass = true;
			}
			getEntityTransaction().commit();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception:", e);
		}
		if (!pass)
			throw new Exception("parameterNamedParameterTwiceTest failed");
	}

	public void createEmployeeData() throws Exception {
		logger.log(Logger.Level.TRACE, "createDepartmentEmployeeData");
		getEntityTransaction().begin();

		try {
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[1] = new Employee(2, "Arthur", "Frechette");
			empRef[2] = new Employee(3, "Shelly", "McGowan");
			empRef[3] = new Employee(4, "Robert", "Bissett");
			empRef[4] = new Employee(5, "Stephen", "DMilla");
			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted employee " + emp.getId());
				}
			}
			doFlush();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("createDepartmentEmployeeData failed:", e);
		}
	}

	@AfterEach
	public void cleanupEmployee() throws Exception {
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
