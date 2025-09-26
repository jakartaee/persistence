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

package ee.jakarta.tck.persistence.jpa22.repeatable.mapkeyjoincolumn;

import java.lang.System.Logger;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	private Map<Course, Semester> student7EnrollmentMap;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Course", pkgName + "Semester", pkgName + "Student" };
		return createDeploymentJar("jpa_jpa22_repeatable_mapkeyjoincolumn.jar", pkgNameWithoutSuffix,
				(String[]) classes);

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
	 * @testName: mapKeyJoinColumnTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:364;
	 * 
	 * @test_Strategy: follow core/annotations/mapkeyclass test but
	 * without @mapkeyjoincolumns
	 */
	@Test
	public void mapKeyJoinColumnTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			createTestData();
			getEntityManager().flush();
			clearCache();
			clearCache();

			final Student student = getEntityManager().find(Student.class, 7);
			final Set<Course> courses = student.getCourses();
			if (courses.containsAll(student7EnrollmentMap.keySet())
					&& student7EnrollmentMap.keySet().containsAll(courses)
					&& courses.size() == student7EnrollmentMap.keySet().size())
				pass = true;
			clearCache();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("mapKeyJoinColumn Failed");
		}

	}

	public void createTestData() {
		// Create 8 students;
		final Student student1 = new Student(1, "Neo");
		final Student student2 = new Student(2, "Vivek");
		final Student student3 = new Student(3, "Arun");
		final Student student4 = new Student(4, "Ganesh");
		final Student student5 = new Student(5, "Ram");
		final Student student6 = new Student(6, "Rahim");
		final Student student7 = new Student(7, "Joseph");
		final Student student8 = new Student(8, "Krishna");

		// Create 4 Semesters;
		final Semester semester1 = new Semester(1);
		final Semester semester2 = new Semester(2);
		final Semester semester3 = new Semester(3);
		final Semester semester4 = new Semester(4);

		// Create 12 Courses;
		final Course appliedMath = new Course(101, "AppliedMathematics");
		final Course physics = new Course(102, "Physics");
		final Course operationResearch = new Course(103, "OperationResearch");
		final Course statistics = new Course(201, "Statistics");
		final Course operatingSystem = new Course(202, "OperatingSystem");
		final Course numericalMethods = new Course(203, "NumericalMethods");
		final Course graphics = new Course(301, "Graphics");
		final Course accountancy = new Course(302, "Accountancy");
		final Course mis = new Course(303, "ManagementInformationSystem");
		final Course cad = new Course(401, "ComputerAidedDesign");
		final Course compilerDesign = new Course(402, "CompilerDesign");
		final Course ood = new Course(403, "ObjectOrientedDesignAndAnalysis");

		// Create Enrollment map for Student1
		Map<Course, Semester> student1EnrollmentMap = new Hashtable<>();
		student1EnrollmentMap.put(appliedMath, semester1);
		student1EnrollmentMap.put(physics, semester1);
		student1EnrollmentMap.put(operationResearch, semester1);
		student1EnrollmentMap.put(statistics, semester2);
		student1EnrollmentMap.put(operatingSystem, semester2);
		student1EnrollmentMap.put(numericalMethods, semester2);
		// Set Enrollment map for Student1
		student1.setEnrollment(student1EnrollmentMap);

		// Create Enrollment map for Student2
		Map<Course, Semester> student2EnrollmentMap = new Hashtable<>();
		student2EnrollmentMap.put(appliedMath, semester1);
		student2EnrollmentMap.put(physics, semester1);
		student2EnrollmentMap.put(operationResearch, semester1);
		student2EnrollmentMap.put(graphics, semester3);
		student2EnrollmentMap.put(accountancy, semester3);
		student2EnrollmentMap.put(mis, semester3);
		// Set Enrollment map for Student2
		student2.setEnrollment(student2EnrollmentMap);

		// Create Enrollment map for Student3
		Map<Course, Semester> student3EnrollmentMap = new Hashtable<>();
		student3EnrollmentMap.put(statistics, semester2);
		student3EnrollmentMap.put(operatingSystem, semester2);
		student3EnrollmentMap.put(numericalMethods, semester2);
		student3EnrollmentMap.put(graphics, semester3);
		student3EnrollmentMap.put(accountancy, semester3);
		student3EnrollmentMap.put(mis, semester3);
		// Set Enrollment map for Student3
		student3.setEnrollment(student3EnrollmentMap);

		// Create Enrollment map for Student4
		Map<Course, Semester> student4EnrollmentMap = new Hashtable<>();
		student4EnrollmentMap.put(statistics, semester2);
		student4EnrollmentMap.put(operatingSystem, semester2);
		student4EnrollmentMap.put(numericalMethods, semester2);
		student4EnrollmentMap.put(cad, semester4);
		student4EnrollmentMap.put(compilerDesign, semester4);
		student4EnrollmentMap.put(ood, semester4);
		// Set Enrollment map for Student4
		student4.setEnrollment(student4EnrollmentMap);

		// Create Enrollment map for Student5
		Map<Course, Semester> student5EnrollmentMap = new Hashtable<>();
		student5EnrollmentMap.put(graphics, semester3);
		student5EnrollmentMap.put(accountancy, semester3);
		student5EnrollmentMap.put(mis, semester3);
		// Set Enrollment map for Student5
		student5.setEnrollment(student5EnrollmentMap);

		// Create Enrollment map for Student6
		Map<Course, Semester> student6EnrollmentMap = new Hashtable<>();
		student6EnrollmentMap.put(graphics, semester3);
		student6EnrollmentMap.put(accountancy, semester3);
		student6EnrollmentMap.put(mis, semester3);
		student6EnrollmentMap.put(cad, semester4);
		student6EnrollmentMap.put(compilerDesign, semester4);
		student6EnrollmentMap.put(ood, semester4);
		// Set Enrollment map for Student6
		student6.setEnrollment(student6EnrollmentMap);

		// Create Enrollment map for Student7
		student7EnrollmentMap = new Hashtable<>();
		student7EnrollmentMap.put(appliedMath, semester1);
		student7EnrollmentMap.put(physics, semester1);
		student7EnrollmentMap.put(operationResearch, semester1);
		student7EnrollmentMap.put(cad, semester4);
		student7EnrollmentMap.put(compilerDesign, semester4);
		student7EnrollmentMap.put(ood, semester4);
		// Set Enrollment map for Student7
		student7.setEnrollment(student7EnrollmentMap);

		// Create Enrollment map for Student8
		Map<Course, Semester> student8EnrollmentMap = new Hashtable<>();
		student8EnrollmentMap.put(appliedMath, semester2);
		student8EnrollmentMap.put(physics, semester2);
		student8EnrollmentMap.put(operationResearch, semester2);
		student8EnrollmentMap.put(cad, semester4);
		student8EnrollmentMap.put(compilerDesign, semester4);
		student8EnrollmentMap.put(ood, semester4);
		// Set Enrollment map for Student8
		student8.setEnrollment(student8EnrollmentMap);

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

		// persist 4 semesters
		entityManager.persist(semester1);
		entityManager.persist(semester2);
		entityManager.persist(semester3);
		entityManager.persist(semester4);
		logger.log(Logger.Level.TRACE, "persisted 4 semesters");

		// persist 12 courses
		entityManager.persist(appliedMath);
		entityManager.persist(physics);
		entityManager.persist(operationResearch);
		entityManager.persist(statistics);
		entityManager.persist(operatingSystem);
		entityManager.persist(numericalMethods);
		entityManager.persist(graphics);
		entityManager.persist(accountancy);
		entityManager.persist(mis);
		entityManager.persist(cad);
		entityManager.persist(compilerDesign);
		entityManager.persist(ood);
		logger.log(Logger.Level.TRACE, "persisted 12 Courses");

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
			getEntityManager().createNativeQuery("Delete from ENROLLMENTS").executeUpdate();
			getEntityManager().createNativeQuery("Delete from SEMESTER").executeUpdate();
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
