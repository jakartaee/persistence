/*
 * Copyright (c) 2014, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyenumerated;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 20L;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Department2", pkgName + "Department3",
				pkgName + "Department4", pkgName + "EmbeddedEmployee", pkgName + "Employee", pkgName + "Employee2",
				pkgName + "Employee3", pkgName + "Employee4", pkgName + "Numbers", pkgName + "Offices" };
		return createDeploymentJar("jpa_core_annotations_mapkeyenumerated.jar", pkgNameWithoutSuffix, classes);

	}

	private static Employee empRef[] = new Employee[5];
	private static Employee2 empRef2[] = new Employee2[5];
	private static Employee3 empRef3[] = new Employee3[5];
	private static Employee4 empRef4[] = new Employee4[5];

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
	 * public void setupCust(String[] args, Properties p) throws Exception {
	 * logger.log(Logger.Level.TRACE,"setup"); try { super.setup();
	 * removeCustTestData(); } catch (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Exception: ", e); throw new
	 * Exception("Setup failed:", e);
	 * 
	 * } }
	 */

	/*
	 * @testName: mapKeyEnumeratedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2051; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2
	 *
	 * @test_Strategy:
	 *
	 */
	@Test
	public void mapKeyEnumeratedTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF000);
		expected.add(Offices.OFF002);
		expected.add(Offices.OFF004);

		try {
			getEntityTransaction().begin();
			Employee emp = getEntityManager().find(Employee.class, 1);
			logger.log(Logger.Level.TRACE, "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department dept = emp.getDepartment();
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			Map<Offices, Employee> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logger.log(Logger.Level.TRACE, "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logger.log(Logger.Level.TRACE, "Received expected keys");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected keys");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Offices key : expected) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Offices key : keys) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
			}

			Set<Employee> sExpected = new HashSet<Employee>();
			sExpected.add(empRef[0]);
			sExpected.add(empRef[1]);
			sExpected.add(empRef[2]);

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
			throw new Exception("mapKeyEnumeratedTest failed");
		}
	}

	/*
	 * @testName: mapKeyEnumeratedDefaultTypeTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2054; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void mapKeyEnumeratedDefaultTypeTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF001);
		expected.add(Offices.OFF003);

		try {
			getEntityTransaction().begin();
			Employee2 emp = getEntityManager().find(Employee2.class, 2);
			logger.log(Logger.Level.TRACE, "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department2 dept = emp.getDepartment();
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			Map<Offices, Employee2> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee2> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logger.log(Logger.Level.TRACE, "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logger.log(Logger.Level.TRACE, "Received expected keys");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected keys");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Offices key : expected) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Offices key : keys) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
			}

			Set<Employee2> sExpected = new HashSet<Employee2>();
			sExpected.add(empRef2[0]);
			sExpected.add(empRef2[1]);

			Collection<Employee2> employees = emps.values();
			for (Employee2 e : employees) {
				logger.log(Logger.Level.TRACE, "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logger.log(Logger.Level.TRACE, "Received expected values");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected values");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Employee2 e : sExpected) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Employee2 e : employees) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("mapKeyEnumeratedDefaultTypeTest failed");
		}
	}

	/*
	 * @testName: mapKeyEnumeratedWithMayKeyAnnotationTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2053; PERSISTENCE:SPEC:2052;
	 * PERSISTENCE:SPEC:2052.2;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void mapKeyEnumeratedWithMayKeyAnnotationTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Offices> expected = new HashSet<Offices>();
		expected.add(Offices.OFF001);
		expected.add(Offices.OFF003);

		try {
			getEntityTransaction().begin();
			Employee3 emp = getEntityManager().find(Employee3.class, 6);
			logger.log(Logger.Level.TRACE, "Name:" + emp.getFirstName() + " " + emp.getLastName());
			Department3 dept = emp.getDepartment();
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			@SuppressWarnings("unchecked")
			Map<Offices, Employee3> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Offices, Employee3> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "map:" + entry.getKey() + ", " + entry.getValue().getId() + " "
							+ entry.getValue().getFirstName() + " " + entry.getValue().getLastName());
				}
			}
			Set<Offices> keys = emps.keySet();
			for (Offices key : keys) {
				logger.log(Logger.Level.TRACE, "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logger.log(Logger.Level.TRACE, "Received expected keys");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected keys");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Offices key : expected) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Offices key : keys) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
			}

			Set<Employee3> sExpected = new HashSet<Employee3>();
			sExpected.add(empRef3[0]);
			sExpected.add(empRef3[1]);

			Collection<Employee3> employees = emps.values();
			for (Employee3 e : employees) {
				logger.log(Logger.Level.TRACE, "values:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logger.log(Logger.Level.TRACE, "Received expected values");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected values");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Employee3 e : sExpected) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Employee3 e : employees) {
					logger.log(Logger.Level.TRACE,
							"Employee:" + e.getId() + " " + e.getFirstName() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("mapKeyEnumeratedWithMayKeyAnnotationTest failed");
		}
	}

	/*
	 * @testName: elementCollectionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2052; PERSISTENCE:SPEC:2052.1;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void elementCollectionTest() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		Set<Numbers> expected = new HashSet<Numbers>();
		expected.add(Numbers.one);
		expected.add(Numbers.two);

		try {
			getEntityTransaction().begin();
			Employee4 emp = getEntityManager().find(Employee4.class, 8);
			logger.log(Logger.Level.TRACE, "Name:" + emp.getLastName());
			Department4 dept = emp.getDepartment();
			logger.log(Logger.Level.TRACE, "Dept=" + dept.getName());
			Map<Numbers, EmbeddedEmployee> emps = dept.getLastNameEmployees();
			if (TestUtil.traceflag) {
				for (Map.Entry<Numbers, EmbeddedEmployee> entry : emps.entrySet()) {
					logger.log(Logger.Level.TRACE, "map:" + entry.getKey() + ", " + entry.getValue().employeeId + " "
							+ entry.getValue().employeeName);
				}
			}
			Set<Numbers> keys = emps.keySet();
			for (Numbers key : keys) {
				logger.log(Logger.Level.TRACE, "key:" + key);
			}
			if (expected.containsAll(keys) && keys.containsAll(expected) && expected.size() == keys.size()) {
				logger.log(Logger.Level.TRACE, "Received expected keys");
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected keys");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Numbers key : expected) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Numbers key : keys) {
					logger.log(Logger.Level.TRACE, "key:" + key);
				}
			}

			Set<Employee4> sExpected = new HashSet<Employee4>();
			sExpected.add(empRef4[0]);
			sExpected.add(empRef4[1]);

			Collection<Employee4> employees = emps.values().stream()
					.flatMap(a -> Stream.of(new Employee4(a.employeeId, a.employeeName))).collect(Collectors.toList());
			for (Employee4 e : employees) {
				logger.log(Logger.Level.TRACE, "values:" + e.getId() + " " + e.getLastName());
			}
			if (sExpected.containsAll(employees) && employees.containsAll(sExpected)
					&& sExpected.size() == employees.size()) {
				logger.log(Logger.Level.TRACE, "Received expected values");
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Did not received expected values");
				logger.log(Logger.Level.ERROR, "Expected:");
				for (Employee4 e : sExpected) {
					logger.log(Logger.Level.TRACE, "Employee:" + e.getId() + " " + e.getLastName());
				}
				logger.log(Logger.Level.ERROR, "Actual:");
				for (Employee4 e : employees) {
					logger.log(Logger.Level.TRACE, "Employee:" + e.getId() + " " + e.getLastName());
				}
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", ex);
		}

		if (!pass1 || !pass2) {
			throw new Exception("elementCollectionTest failed");
		}
	}

	/*
	 * Business Methods to set up data for Test Cases
	 */
	private void createTestData() throws Exception {
		logger.log(Logger.Level.TRACE, "createTestData");
		try {
			getEntityTransaction().begin();

			logger.log(Logger.Level.TRACE, "Create 2 - Departments");
			Department deptRef[] = new Department[2];
			deptRef[0] = new Department(1, "Marketing");
			deptRef[1] = new Department(2, "Administration");

			logger.log(Logger.Level.TRACE, "Persist departments ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 2 - Department2");
			Department2 deptRef2[] = new Department2[2];
			deptRef2[0] = new Department2(3, "Development");
			deptRef2[1] = new Department2(4, "Release");

			logger.log(Logger.Level.TRACE, "Persist Department2s ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department2 " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 2 - Department3");
			Department3 deptRef3[] = new Department3[2];
			deptRef3[0] = new Department3(5, "Shipping");
			deptRef3[1] = new Department3(6, "Receiving");

			logger.log(Logger.Level.TRACE, "Persist Department2s ");
			for (Department3 dept : deptRef3) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department3 " + dept.getName());
				}
			}

			logger.log(Logger.Level.TRACE, "Create 2 - Department4");
			Department4 deptRef4[] = new Department4[2];
			deptRef4[0] = new Department4(7, "Accounts_Receivable");
			deptRef4[1] = new Department4(8, "Accounts_Payable");

			logger.log(Logger.Level.TRACE, "Persist Department4 ");
			for (Department4 dept : deptRef4) {
				if (dept != null) {
					getEntityManager().persist(dept);
					logger.log(Logger.Level.TRACE, "persisted department4 " + dept.getName());
				}
			}
			logger.log(Logger.Level.TRACE, "Create 5 - Employee");
			empRef[0] = new Employee(1, "Alan", "Frechette");
			empRef[0].setDepartment(deptRef[0]);

			empRef[1] = new Employee(3, "Shelly", "McGowan");
			empRef[1].setDepartment(deptRef[0]);

			empRef[2] = new Employee(5, "Stephen", "DMilla");
			empRef[2].setDepartment(deptRef[0]);

			empRef2[0] = new Employee2(2, "Arthur", "Frechette");
			empRef2[0].setDepartment(deptRef2[0]);

			empRef2[1] = new Employee2(4, "Robert", "Bissett");
			empRef2[1].setDepartment(deptRef2[0]);

			empRef3[0] = new Employee3(6, "Douglas", "Donahue");
			empRef3[0].setDepartment(deptRef3[0]);

			empRef3[1] = new Employee3(7, "Kellie", "Sanborn");
			empRef3[1].setDepartment(deptRef3[0]);

			empRef4[0] = new Employee4(8, "Grace");
			empRef4[0].setDepartment(deptRef4[0]);

			empRef4[1] = new Employee4(9, "Bender");
			empRef4[1].setDepartment(deptRef4[1]);

			Map<Offices, Employee> link = new HashMap<Offices, Employee>();

			link.put(Offices.OFF000, empRef[0]);
			link.put(Offices.OFF002, empRef[1]);
			link.put(Offices.OFF004, empRef[2]);
			deptRef[0].setLastNameEmployees(link);

			Map<Offices, Employee2> link2 = new HashMap<Offices, Employee2>();
			link2.put(Offices.OFF001, empRef2[0]);
			link2.put(Offices.OFF003, empRef2[1]);
			deptRef2[0].setLastNameEmployees(link2);

			Map<Offices, Employee3> link3 = new HashMap<Offices, Employee3>();
			link3.put(Offices.OFF001, empRef3[0]);
			link3.put(Offices.OFF003, empRef3[1]);
			deptRef3[0].setLastNameEmployees(link3);

			Map<Numbers, EmbeddedEmployee> link4 = new HashMap<Numbers, EmbeddedEmployee>();
			link4.put(Numbers.one, new EmbeddedEmployee(empRef4[0]));
			link4.put(Numbers.two, new EmbeddedEmployee(empRef4[1]));
			deptRef4[0].setLastNameEmployees(link4);

			logger.log(Logger.Level.TRACE, "Persist Employee ");
			for (Employee emp : empRef) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted Employee " + emp.getId());
				}
			}
			logger.log(Logger.Level.TRACE, "Persist Employee2 ");
			for (Employee2 emp : empRef2) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted Employee2 " + emp.getId());
				}
			}

			logger.log(Logger.Level.TRACE, "Persist Employee3 ");
			for (Employee3 emp : empRef3) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted Employee3 " + emp.getId());
				}
			}

			logger.log(Logger.Level.TRACE, "Persist Employee4 ");
			for (Employee4 emp : empRef4) {
				if (emp != null) {
					getEntityManager().persist(emp);
					logger.log(Logger.Level.TRACE, "persisted Employee4 " + emp.getId());
				}
			}
			// Merge Department
			logger.log(Logger.Level.TRACE, "Merge Department ");
			for (Department dept : deptRef) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged Department " + dept.getName());

				}
			}

			// Merge Department
			logger.log(Logger.Level.TRACE, "Merge Department2 ");
			for (Department2 dept : deptRef2) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged Department2 " + dept.getName());

				}
			}

			// Merge Department
			logger.log(Logger.Level.TRACE, "Merge Department3 ");
			for (Department3 dept : deptRef3) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged Department3 " + dept.getName());

				}
			}
			// Merge Department
			logger.log(Logger.Level.TRACE, "Merge Department4 ");
			for (Department4 dept : deptRef4) {
				if (dept != null) {
					getEntityManager().merge(dept);
					logger.log(Logger.Level.TRACE, "merged Department4 " + dept.getName());

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

	/*
	 * public void cleanupCust() throws Exception {
	 * logger.log(Logger.Level.TRACE,"cleanup"); removeCustTestData();
	 * logger.log(Logger.Level.TRACE,"cleanup complete, calling super.cleanup");
	 * super.cleanup(); }
	 */

	private void removeTestData() {
		logger.log(Logger.Level.TRACE, "removeTestData");

		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL").executeUpdate();
			getEntityManager().createNativeQuery("Delete from EMP_MAPKEYCOL2").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT2").executeUpdate();
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

	/*
	 * private void removeCustTestData() {
	 * logger.log(Logger.Level.TRACE,"removeCustTestData"); if
	 * (getEntityTransaction().isActive()) { getEntityTransaction().rollback(); }
	 * try { getEntityTransaction().begin();
	 * getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate(
	 * );
	 * getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
	 * getEntityTransaction().commit(); } catch (Exception e) {
	 * logger.log(Logger.Level.
	 * ERROR,"Exception encountered while removing entities:", e); } finally { try {
	 * if (getEntityTransaction().isActive()) { getEntityTransaction().rollback(); }
	 * } catch (Exception re) {
	 * logger.log(Logger.Level.ERROR,"Unexpected Exception in removeTestData:", re);
	 * } } }
	 */
}
