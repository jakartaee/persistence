/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkeyclass;

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

	private Map<Course, Semester> student2EnrollmentMap;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Course", pkgName + "Semester", pkgName + "Student" };
		return createDeploymentJar("jpa_core_annotations_mapkeyclass.jar", pkgNameWithoutSuffix, classes);

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
	 * @testName: mapKeyClass
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
	 * PERSISTENCE:SPEC:846; PERSISTENCE:SPEC:1204; PERSISTENCE:JAVADOC:350;
	 * PERSISTENCE:JAVADOC:370;
	 *
	 * @test_Strategy: With basic entity requirements, persist/remove an entity.
	 */
	@Test
	public void mapKeyClass() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			createEntities();
			getEntityManager().flush();
			clearCache();
			clearCache();

			final Student student = getEntityManager().find(Student.class, 2);
			final Set<Course> courses = student.getCourses();
			if (courses != null) {
				if (courses.containsAll(student2EnrollmentMap.keySet())
						&& student2EnrollmentMap.keySet().containsAll(courses)
						&& courses.size() == student2EnrollmentMap.keySet().size())
					pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "getCourses() returned null value");
			}
			clearCache();
			getEntityTransaction().commit();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:", e);
		}

		if (!pass) {
			throw new Exception("mapKeyClass Failed");
		}

	}

	public void createEntities() {
		// Create 8 students;
		final Student student1 = new Student(1, "Neo");
		final Student student2 = new Student(2, "Vivek");

		// Create 4 Semesters;
		final Semester semester1 = new Semester(1);
		final Semester semester2 = new Semester(2);
		final Semester semester3 = new Semester(3);

		// Create 12 Courses;
		final Course appliedMath = new Course(101, "AppliedMathematics");
		final Course physics = new Course(102, "Physics");
		final Course operationResearch = new Course(103, "OperationResearch");
		final Course statistics = new Course(201, "Statistics");

		// Create Enrollment map for Student1
		Map<Course, Semester> student1EnrollmentMap = new Hashtable();
		student1EnrollmentMap.put(appliedMath, semester1);
		student1EnrollmentMap.put(physics, semester1);
		student1EnrollmentMap.put(statistics, semester2);
		// Set Enrollment map for Student1
		student1.setEnrollment(student1EnrollmentMap);

		// Create Enrollment map for Student2
		student2EnrollmentMap = new Hashtable();
		student2EnrollmentMap.put(appliedMath, semester1);
		student2EnrollmentMap.put(physics, semester1);
		student2EnrollmentMap.put(operationResearch, semester3);
		student2EnrollmentMap.put(statistics, semester3);
		// Set Enrollment map for Student2
		student2.setEnrollment(student2EnrollmentMap);

		EntityManager entityManager = getEntityManager();

		// persist 8 students
		entityManager.persist(student1);
		entityManager.persist(student2);
		logger.log(Logger.Level.TRACE, "persisted 2 students");

		// persist 4 semesters
		entityManager.persist(semester1);
		entityManager.persist(semester2);
		entityManager.persist(semester3);
		logger.log(Logger.Level.TRACE, "persisted 4 semesters");

		// persist 12 courses
		entityManager.persist(appliedMath);
		entityManager.persist(physics);
		entityManager.persist(operationResearch);
		entityManager.persist(statistics);
		logger.log(Logger.Level.TRACE, "persisted 4 Courses");

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
				logger.log(Logger.Level.ERROR, "Unexpected Exception in RemoveSchemaData:", re);
			}
		}
	}
}
