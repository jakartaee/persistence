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
import jakarta.persistence.Query;

public class Client1 extends PMClientBase {

	private List<Student> expectedResults;
	private static final Logger logger = (Logger) System.getLogger(Client1.class.getName());

	public Client1() {
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Course", pkgName + "Department", pkgName + "Department2", pkgName + "Employee",
				pkgName + "Employee2", pkgName + "Student" };
		return createDeploymentJar("jpa_core_annotations_ordercolumn1.jar", pkgNameWithoutSuffix, classes);
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();
			removeTestData();
			createStudentTestData();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: orderColumn
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846; PERSISTENCE:SPEC:1204; PERSISTENCE:JAVADOC:378;
	 * PERSISTENCE:JAVADOC:379; PERSISTENCE:JAVADOC:377; PERSISTENCE:JAVADOC:380;
	 *
	 * @test_Strategy: With basic entity requirements, persist/remove an entity.
	 */
	@Test
	public void orderColumn() throws Exception {
		boolean pass = false;
		final String expectedStudentName = "Joseph";
		final int expectedListSize = 4;
		final int courseNumber = 101;
		List<Student> students;
		int numStudents = 0;

		try {
			getEntityTransaction().begin();

			final Course course = getEntityManager().find(Course.class, courseNumber);

			if (course != null) {
				// force students to be read
				numStudents = course.getStudents().size();
			} else {
				logger.log(Logger.Level.ERROR, "course from find() is NULL!");
			}

			if (numStudents == expectedListSize) {
				students = course.getStudents();
				if (students.get(0).equals(expectedResults.get(0)) && students.get(1).equals(expectedResults.get(1))
						&& students.get(2).equals(expectedResults.get(2))
						&& students.get(3).equals(expectedResults.get(3))) {
					logger.log(Logger.Level.TRACE,
							"****Current order of students returned via " + "getStudents()\nNow checking via JPQL");

					Query q = getEntityManager().createQuery("SELECT s.studentName "
							+ "FROM Course c JOIN c.students s where c.courseName ='Physics' and INDEX(s) = 1");
					final String result = (String) q.getSingleResult();
					if (result.equals(expectedStudentName)) {
						logger.log(Logger.Level.TRACE, "+++Received expected Name via Query:" + expectedStudentName);
						pass = true;
					} else {
						logger.log(Logger.Level.ERROR, "Did NOT get expected name via Query: " + expectedStudentName
								+ ", received: " + result);
					}

				} else {
					logger.log(Logger.Level.ERROR,
							"Failed to return the correct order of " + "students via getStudents()");
				}

			} else {
				logger.log(Logger.Level.ERROR, "course.getStudents() returned wrong number!");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
		}

		if (!pass) {
			throw new Exception("orderColumn test failed");
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
			getEntityManager().createNativeQuery("Delete from COURSE_STUDENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from STUDENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from COURSE").executeUpdate();
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
