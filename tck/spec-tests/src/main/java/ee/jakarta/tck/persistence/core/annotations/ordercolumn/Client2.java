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

package ee.jakarta.tck.persistence.core.annotations.ordercolumn;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class Client2 extends PMClientBase {

	private List<Student> expectedResults;

	private List<Employee> expectedEmployees;

	private List<Employee2> expectedEmployees2;

	private static final Logger logger = (Logger) System.getLogger(Client2.class.getName());

	public Client2() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Course", pkgName + "Department", pkgName + "Department2", pkgName + "Employee",
				pkgName + "Employee2", pkgName + "Student" };
		return createDeploymentJar("jpa_core_annotations_ordercolumn2.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setupEmployee() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {

			super.setup();
			createDeployment();
			removeEmployeeTestData();
			createEmployeeTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: propertyAccessWithNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2097; PERSISTENCE:SPEC:2104;
	 * PERSISTENCE:SPEC:2102;
	 *
	 * @test_Strategy: name is specified while using property access.
	 */
	@Test
	public void propertyAccessWithNameTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			Department d = getEntityManager().find(Department.class, 50);
			logger.log(Logger.Level.INFO, "Display find data");

			for (Employee e : d.getEmployees()) {
				logger.log(Logger.Level.INFO, "Employee:" + e);
			}

			List<Employee> actual = new ArrayList<Employee>();
			TypedQuery<Employee> q = getEntityManager().createQuery(
					"SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 0", Employee.class);
			Employee emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(0) returned null result");
			}
			q = getEntityManager().createQuery(
					"SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 1", Employee.class);
			emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(1) returned null result");
			}
			q = getEntityManager().createQuery(
					"SELECT e FROM Department d JOIN d.employees e WHERE d.id = 50 AND INDEX(e) = 2", Employee.class);
			emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(2) returned null result");
			}

			if (actual.size() == expectedEmployees.size()) {
				int count = 0;
				for (int i = 0; i < expectedEmployees.size(); i++) {
					logger.log(Logger.Level.TRACE,
							"Testing - expected[" + expectedEmployees.get(i) + "], actual[" + actual.get(i) + "]");

					if (expectedEmployees.get(i).equals(actual.get(i))) {
						count++;
					}
				}

				if (count == expectedEmployees.size()) {
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE, "count=" + count + ", expected size:" + expectedEmployees.size());
					for (Employee e : expectedEmployees) {
						logger.log(Logger.Level.ERROR, "expected:" + e);
					}
					logger.log(Logger.Level.ERROR, "------------");
					for (Employee e : actual) {
						logger.log(Logger.Level.ERROR, "actual:" + e);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected list size:" + expectedEmployees.size() + ", actual size:" + actual.size());
				for (Employee e : expectedEmployees) {
					logger.log(Logger.Level.ERROR, "expected:" + e);
				}
				logger.log(Logger.Level.ERROR, "------------");
				for (Employee e : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + e);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
		}

		if (!pass) {
			throw new Exception("propertyAccessWithNameTest test failed");
		}

	}

	/*
	 * @testName: fieldAccessWithNameTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2097; PERSISTENCE:SPEC:2104;
	 * PERSISTENCE:SPEC:2102; PERSISTENCE:SPEC:2101;
	 *
	 * @test_Strategy: name is specified while using property access.
	 */
	@Test
	public void fieldAccessWithNameTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			clearCache();
			Department2 d = getEntityManager().find(Department2.class, 55);
			logger.log(Logger.Level.INFO, "Display find data");

			for (Employee2 e : d.getEmployees()) {
				logger.log(Logger.Level.INFO, "Employee2:" + e);
			}

			List<Employee2> actual = new ArrayList<Employee2>();
			TypedQuery<Employee2> q = getEntityManager().createQuery(
					"SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 0", Employee2.class);
			Employee2 emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(0) returned null result");
			}
			q = getEntityManager().createQuery(
					"SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 1", Employee2.class);
			emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(1) returned null result");
			}
			q = getEntityManager().createQuery(
					"SELECT e FROM Department2 d JOIN d.employees e WHERE d.id = 55 AND INDEX(e) = 2", Employee2.class);
			emp = q.getSingleResult();
			if (emp != null) {
				actual.add(emp);
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(2) returned null result");
			}

			if (actual.size() == expectedEmployees2.size()) {
				int count = 0;
				for (int i = 0; i < expectedEmployees2.size(); i++) {
					logger.log(Logger.Level.TRACE,
							"Testing - expected[" + expectedEmployees2.get(i) + "], actual[" + actual.get(i) + "]");

					if (expectedEmployees2.get(i).equals(actual.get(i))) {
						count++;
					}
				}

				if (count == expectedEmployees.size()) {
					pass = true;
				} else {
					logger.log(Logger.Level.TRACE, "count=" + count + ", expected size:" + expectedEmployees2.size());
					for (Employee2 e : expectedEmployees2) {
						logger.log(Logger.Level.ERROR, "expected:" + e);
					}
					logger.log(Logger.Level.ERROR, "------------");
					for (Employee2 e : actual) {
						logger.log(Logger.Level.ERROR, "actual:" + e);
					}
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Expected list size:" + expectedEmployees2.size() + ", actual size:" + actual.size());
				for (Employee2 e : expectedEmployees2) {
					logger.log(Logger.Level.ERROR, "expected:" + e);
				}
				logger.log(Logger.Level.ERROR, "------------");
				for (Employee2 e : actual) {
					logger.log(Logger.Level.ERROR, "actual:" + e);
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
		}

		if (!pass) {
			throw new Exception("fieldAccessWithNameTest test failed");
		}

	}

	public void createStudentTestData() {
		try {
			logger.log(Logger.Level.TRACE, "createTestData");
			getEntityTransaction().begin();

			// Create 8 students;
			Student student1 = new Student(1, "Neo");
			Student student2 = new Student(2, "Vivek");
			Student student3 = new Student(3, "Arun");
			Student student4 = new Student(4, "Ganesh");
			Student student5 = new Student(5, "Ram");
			Student student6 = new Student(6, "Rahim");
			Student student7 = new Student(7, "Joseph");
			Student student8 = new Student(8, "Krishna");

			// Create 12 Courses;
			Course appliedMath = new Course(101, "AppliedMathematics");
			Course physics = new Course(102, "Physics");
			Course operationResearch = new Course(103, "OperationResearch");
			Course statistics = new Course(201, "Statistics");
			Course operatingSystem = new Course(202, "OperatingSystem");

			// Set enrolled students for each course
			List<Student> studentList1 = new ArrayList<Student>();
			studentList1.add(student1);
			studentList1.add(student7);
			studentList1.add(student2);
			studentList1.add(student8);

			expectedResults = new ArrayList<Student>();
			expectedResults.addAll(studentList1);

			// Set enrolled students for each course
			List<Student> studentList2 = new ArrayList<Student>();
			studentList2.add(student3);
			studentList2.add(student4);

			// Set enrolled students for each course
			List<Student> studentList3 = new ArrayList<Student>();
			studentList3.add(student5);
			studentList3.add(student6);

			// Set enrolled students for each course
			List<Student> studentList4 = new ArrayList<Student>();
			studentList4.add(student7);
			studentList4.add(student8);

			appliedMath.setStudents(studentList1);
			physics.setStudents(studentList1);
			operationResearch.setStudents(studentList1);

			statistics.setStudents(studentList2);
			operatingSystem.setStudents(studentList2);

			// Set Courses for first semester
			List<Course> firstSemCourses = new ArrayList<Course>();
			firstSemCourses.add(appliedMath);
			firstSemCourses.add(physics);
			firstSemCourses.add(operationResearch);

			// Set Courses for Second semester
			List<Course> secondSemCourses = new ArrayList<Course>();
			secondSemCourses.add(statistics);
			secondSemCourses.add(operatingSystem);

			// Set Courses for each student
			student1.setCourses(firstSemCourses);
			student2.setCourses(firstSemCourses);
			student3.setCourses(secondSemCourses);
			student4.setCourses(secondSemCourses);
			student5.setCourses(secondSemCourses);
			student6.setCourses(secondSemCourses);
			student7.setCourses(firstSemCourses);
			student8.setCourses(firstSemCourses);

			EntityManager entityManager = getEntityManager();

			// persist 8 students

			entityManager.persist(student1);
			entityManager.persist(student2);
			entityManager.persist(student3);
			entityManager.persist(student4);
			entityManager.persist(student5);
			entityManager.persist(student6);
			entityManager.persist(student7);
			entityManager.persist(student8);
			logger.log(Logger.Level.TRACE, "persisted 8 students");

			// persist courses
			entityManager.persist(appliedMath);
			entityManager.persist(physics);
			entityManager.persist(operationResearch);
			entityManager.persist(statistics);
			entityManager.persist(operatingSystem);
			logger.log(Logger.Level.TRACE, "persisted 5 Courses");

			logger.log(Logger.Level.TRACE, "persisted Entity Data");
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

	private void createEmployeeTestData() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "createEmployeeTestData");
			getEntityTransaction().begin();

			Department d1 = new Department(50, "Dept1");
			Department2 d2 = new Department2(55, "Dept2");
			getEntityManager().persist(d1);

			final Employee e1 = new Employee(20, "Jie", "Leng", 0.0F, d1);
			final Employee e2 = new Employee(40, "Zoe", "Leng", 0.0F, d1);
			final Employee e3 = new Employee(60, "John", "Smith", 0.0F, d1);
			final Employee2 e4 = new Employee2(80, "Song", "Leng", 0.0F, d2);
			final Employee2 e5 = new Employee2(100, "May", "Leng", 0.0F, d2);
			final Employee2 e6 = new Employee2(120, "Donny", "Oz", 0.0F, d2);
			getEntityManager().persist(e1);
			getEntityManager().persist(e2);
			getEntityManager().persist(e3);
			getEntityManager().persist(e4);
			getEntityManager().persist(e5);
			getEntityManager().persist(e6);

			expectedEmployees = new ArrayList<Employee>();
			expectedEmployees.add(e3);
			expectedEmployees.add(e1);
			expectedEmployees.add(e2);

			d1.setEmployees(expectedEmployees);
			getEntityManager().merge(d1);

			expectedEmployees2 = new ArrayList<Employee2>();
			expectedEmployees2.add(e6);
			expectedEmployees2.add(e4);
			expectedEmployees2.add(e5);

			d2.setEmployees(expectedEmployees2);
			getEntityManager().merge(d2);

			logger.log(Logger.Level.TRACE, "persisted Entity Data");
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
	public void cleanupEmployee() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "cleanupEmployee");
			removeEmployeeTestData();
			logger.log(Logger.Level.TRACE, "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {
			removeTestJarFromCP();
		}
	}

	private void removeEmployeeTestData() {
		logger.log(Logger.Level.TRACE, "removeEmployeeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
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
