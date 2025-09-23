/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeycolumn;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Department2", pkgName + "Employee" };
		return createDeploymentJar("jpa_core_annotations_mapkeycolumn.jar", pkgNameWithoutSuffix, classes);

	}

	private static Employee empRef[] = new Employee[10];

	private static Department deptRef[] = new Department[5];

	private static Department2 deptRef2[] = new Department2[5];

	public Map<String, Employee> link = new HashMap<String, Employee>();

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createTestData();
			logger.log(Logger.Level.TRACE, "Done creating test data");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: annotationMapKeyColumnTest1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101; PERSISTENCE:SPEC:518; PERSISTENCE:SPEC:1202;
	 * PERSISTENCE:JAVADOC:90; PERSISTENCE:JAVADOC:92; PERSISTENCE:JAVADOC:96
	 *
	 * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
	 * for the key column of a map whose map key is a basic type.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 *
	 * Execute a query returning Employees objects.
	 *
	 */
	@Test
	public void annotationMapKeyColumnTest1() throws Exception {

		boolean pass = false;
		List e = null;

		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "find Employees belonging to Department: Marketing");
			e = getEntityManager().createQuery("Select e from Employee e where e.department.name = 'Marketing'")
					.setMaxResults(10).getResultList();

			if (e.size() != 3) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results" + "Expected 3 Employees, Received: " + e.size());
			} else {
				logger.log(Logger.Level.TRACE, "annotationMapKeyColumnTest1: Expected results received. "
						+ "Expected 3 Employees, Received: " + e.size());
				pass = true;
			}

			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
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
			throw new Exception("annotationMapKeyColumnTest1 failed");
		}
	}

	/*
	 * @testName: annotationMapKeyColumnTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:114; PERSISTENCE:SPEC:1100;
	 * PERSISTENCE:SPEC:1101; PERSISTENCE:JAVADOC:90; PERSISTENCE:JAVADOC:92;
	 * PERSISTENCE:JAVADOC:96
	 * 
	 * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
	 * for the key column of a map whose map key is a basic type.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 *
	 * Execute a query returning Employee IDs.
	 */
	@Test
	public void annotationMapKeyColumnTest2() throws Exception {

		boolean pass = false;
		List e = null;

		try {
			getEntityTransaction().begin();
			final Integer[] expectedEmps = new Integer[] { 4, 2 };

			logger.log(Logger.Level.TRACE, "find Employees belonging to Department: Marketing");
			e = getEntityManager()
					.createQuery(
							"Select e.id from Employee e where e.department.name = 'Administration' ORDER BY e.id DESC")
					.setMaxResults(10).getResultList();

			final Integer[] result = (Integer[]) (e.toArray(new Integer[e.size()]));
			logger.log(Logger.Level.TRACE, "Compare results of Employee Ids ");
			pass = Arrays.equals(expectedEmps, result);

			if (!pass) {
				logger.log(Logger.Level.ERROR,
						"Did not get expected results.  Expected 2 Employees : " + " Received: " + e.size());
				Iterator it = e.iterator();
				while (it.hasNext()) {
					logger.log(Logger.Level.TRACE, " Employee PK : " + it.next());
				}
			} else {
				logger.log(Logger.Level.TRACE, "Expected results received");
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

		if (!pass) {
			throw new Exception("annotationMapKeyColumnTest2 failed");
		}
	}

	/*
	 * @testName: annotationMapKeyColumnTest3
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:352; PERSISTENCE:JAVADOC:353;
	 * PERSISTENCE:JAVADOC:354; PERSISTENCE:JAVADOC:355; PERSISTENCE:JAVADOC:359;
	 *
	 * @test_Strategy: The MapKeyColumn annotation is used to specify the mapping
	 * for the key column of a map whose map key is a basic type.
	 *
	 * The name element designates the name of the persistence property or field of
	 * the associated entity that is used as the map key.
	 *
	 * Execute a query returning Employees objects.
	 */
	@Test
	public void annotationMapKeyColumnTest3() throws Exception {
		boolean pass = false;

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(0);
		expected.add(2);
		expected.add(4);

		List<Integer> actual = new ArrayList<Integer>();
		try {
			getEntityTransaction().begin();
			Employee emp = getEntityManager().find(Employee.class, 1);
			logger.log(Logger.Level.TRACE, "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department dept = emp.getDepartment();
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			Map<String, Employee> emps = dept.getLastNameEmployees();
			if (emps.size() == 3) {
				logger.log(Logger.Level.TRACE, "number of employees=" + emps.size());
				for (Map.Entry<String, Employee> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "id=" + entry.getValue().getId() + ", Name="
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
					actual.add(entry.getValue().getId() - 1);
				}

				Collections.sort(actual);
				if (expected.equals(actual)) {
					logger.log(Logger.Level.TRACE, "Received expected employees");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Did not get correct employees");
					logger.log(Logger.Level.ERROR, "Expected:");
					for (Integer i : expected) {
						logger.log(Logger.Level.TRACE, "id=" + empRef[i].getId() + ", Name=" + empRef[i].getFirstName()
								+ " " + empRef[i].getLastName());
					}
					logger.log(Logger.Level.ERROR, "Actual:");
					for (Integer i : actual) {
						logger.log(Logger.Level.TRACE, "id=" + empRef[i].getId() + ", Name=" + empRef[i].getFirstName()
								+ " " + empRef[i].getLastName());
					}
				}

			} else {
				logger.log(Logger.Level.ERROR, "Expected 3 employees, actual:" + emps.size());
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("annotationMapKeyColumnTest3 Failed");
		}

	}

	/*
	 * @testName: mapKeyColumnInsertableFalseTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:352
	 *
	 * @test_Strategy: The MapKeyColumn annotation with an attribute of
	 * insertable=false is used to specify the mapping for the fk column to a second
	 * entity Execute a query returning Employees objects.
	 */
	@Test
	public void mapKeyColumnInsertableFalseTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			clearCache();
			Department2 dept = getEntityManager().find(Department2.class, 3);
			if (dept != null) {
				logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
				Map<String, Employee> emps = dept.getLastNameEmployees();
				if (emps.size() == 0) {
					logger.log(Logger.Level.TRACE, "Received expected number of employees");
					pass = true;
				} else {
					logger.log(Logger.Level.ERROR, "Expected 0 employees, actual:" + emps.size());
					logger.log(Logger.Level.ERROR, "Actual:");
					for (Map.Entry<String, Employee> entry : emps.entrySet()) {
						logger.log(Logger.Level.ERROR, "id=" + entry.getValue().getId() + ", Name="
								+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "Department2 returned was null");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("mapKeyColumnInsertableFalseTest Failed");
		}

	}

	/*
	 * @testName: mapKeyColumnUpdatableFalseTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:360
	 *
	 * @test_Strategy: The MapKeyColumn annotation with an attribute of
	 * updatable=false is used to specify the mapping for the fk column to a second
	 * entity Execute a query returning Employees objects.
	 */
	@Test
	public void mapKeyColumnUpdatableFalseTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();

			// department 3
			logger.log(Logger.Level.TRACE, "find Department");
			Department2 dept = getEntityManager().find(Department2.class, deptRef2[0].getId());
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			link = new HashMap<String, Employee>();
			link.put("OFF-006", empRef[6]);
			logger.log(Logger.Level.TRACE, "set last names of employees and save");
			dept.setLastNameEmployees(link);
			getEntityManager().merge(dept);
			getEntityManager().flush();
			clearCache();
			logger.log(Logger.Level.TRACE, "find Department again");
			dept = getEntityManager().find(Department2.class, deptRef2[0].getId());
			Map<String, Employee> emps = dept.getLastNameEmployees();
			if (emps.size() == 0) {
				logger.log(Logger.Level.TRACE,
						"Received expected number of employees for department: " + deptRef2[0].getId());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected 0 employees, actual:" + emps.size());
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Map.Entry<String, Employee> entry : emps.entrySet()) {
					logger.log(Logger.Level.ERROR, "id=" + entry.getValue().getId() + ", Name="
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logger.log(Logger.Level.ERROR, "Received unexpected exception", e);
		}
		if (!pass) {
			throw new Exception("mapKeyColumnUpdatableFalseTest Failed");
		}

	}

	/*
	 * @testName: criteriaBuilderKeysValuesTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:791; PERSISTENCE:JAVADOC:875
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void criteriaBuilderKeysValuesTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<String> expected = new HashSet<String>();
		expected.add("OFF-000");
		expected.add("OFF-002");
		expected.add("OFF-004");

		try {
			getEntityTransaction().begin();
			Employee emp = getEntityManager().find(Employee.class, 1);
			System.out.println("Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department dept = emp.getDepartment();
			System.out.println("Dept=" + dept.getName());
			Map<String, Employee> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<String, Employee> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<String> keys = emps.keySet();
			for (String key : keys) {
				logger.log(Logger.Level.TRACE, "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logger.log(Logger.Level.TRACE, "Received expected keys");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected keys");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (String key : expected) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (String key : keys) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
			}

			Set<Employee> sExpected = new HashSet<Employee>();
			sExpected.add(empRef[0]);
			sExpected.add(empRef[2]);
			sExpected.add(empRef[4]);

			Collection<Employee> employees = emps.values();
			for (Employee e : employees) {
				logger.log(Logger.Level.TRACE, "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logger.log(Logger.Level.TRACE, "Received expected values");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected values");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Employee e : sExpected) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Employee e : employees) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("criteriaBuilderKeysValuesTest failed");
		}
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */
	private void createTestData() throws Exception {
		logger.log(Logger.Level.TRACE, "createTestData");
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create 2 Departments");
			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");

			logger.log(Logger.Level.TRACE, "Start to persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 2 Department2 ");
			deptRef2[0] = new Department2(3, "IT");

			logger.log(Logger.Level.TRACE, "Start to persist Department2 ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 5 employees");
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[0].setDepartment(deptRef[0]);

			empRef[1] = new Employee(2, "Arthur", "Frechette");
			empRef[1].setDepartment(deptRef[1]);

			empRef[2] = new Employee(3, "Shelly", "McGowan");
			empRef[2].setDepartment(deptRef[0]);

			empRef[3] = new Employee(4, "Robert", "Bissett");
			empRef[3].setDepartment(deptRef[1]);

			empRef[4] = new Employee(5, "Stephen", "DMilla");
			empRef[4].setDepartment(deptRef[0]);

			link.put("OFF-000", empRef[0]);
			link.put("OFF-002", empRef[2]);
			link.put("OFF-004", empRef[4]);
			deptRef[0].setLastNameEmployees(link);

			link = new HashMap<String, Employee>();
			link.put("OFF-001", empRef[1]);
			link.put("OFF-003", empRef[3]);
			deptRef[1].setLastNameEmployees(link);

			link = new HashMap<String, Employee>();
			link.put("OFF-005", empRef[5]);
			deptRef2[0].setLastNameEmployees(link);

			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted employee " + emp.getId());
				}
			}

			// Merge Department
			logger.log(Logger.Level.TRACE, "Start to Merge department ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged department " + dept.getName());

				}
			}

			// Merge Department2
			logger.log(Logger.Level.TRACE, "Start to Merge department ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged department " + dept.getName());

				}
			}

			logger.log(Logger.Level.TRACE, "Start to persist employees ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted employee " + emp.getId());
				}
			}

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
			getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
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
