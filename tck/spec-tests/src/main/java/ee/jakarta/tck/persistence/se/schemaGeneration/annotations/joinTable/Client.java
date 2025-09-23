/*
 * Copyright (c) 2013, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.joinTable;

import java.io.File;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	String schemaGenerationDir = null;

	String sTestCase = "jpa_se_schemaGeneration_annotations_joinTable";

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Course", pkgName + "Student" };
		return createDeploymentJar("jpa_se_schemaGeneration_annotations_joinTable.jar", pkgNameWithoutSuffix,
				(String[]) classes);

	}

	public Client() {
	}

	@BeforeEach
	public void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");
		try {
			super.setup();
			createDeployment();

			schemaGenerationDir = System.getProperty("user.dir");
			if (!schemaGenerationDir.endsWith(File.separator)) {
				schemaGenerationDir += File.separator;
			}
			schemaGenerationDir += "schemaGeneration";
			logger.log(Logger.Level.INFO, "schemaGenerationDir=" + this.schemaGenerationDir);

			File f = new File(schemaGenerationDir);
			logger.log(Logger.Level.INFO, "Delete existing directory ");
			deleteItem(f);
			logger.log(Logger.Level.INFO, "Create new directory ");
			if (!f.mkdir()) {
				String msg = "Could not mkdir:" + f.getAbsolutePath();
				throw new Exception(msg);
			}
			removeTestData();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: joinTableTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2118.10; PERSISTENCE:SPEC:2118.9;
	 * 
	 * @test_Strategy: Test the @JoinTable annotation
	 */
	@Test
	public void joinTableTest() throws Exception {
		boolean pass1a = false;
		boolean pass1b = false;
		boolean pass1c = false;
		boolean pass1d = false;
		boolean pass1e = false;

		boolean pass2a = false;
		boolean pass2b = false;
		boolean pass2c = false;
		boolean pass2d = false;
		boolean pass2e = false;
		boolean pass3 = false;
		boolean pass4 = false;
		boolean pass5 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");
		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = new File(CREATEFILENAME);
		logger.log(Logger.Level.TRACE, "Deleting previous create script");
		deleteItem(f1);
		File f2 = new File(DROPFILENAME);
		logger.log(Logger.Level.TRACE, "Deleting previous drop script");
		deleteItem(f2);

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		logger.log(Logger.Level.INFO, "Check script(s) content");
		List<String> expected = new ArrayList<String>();
		expected.add("CREATE TABLE SCHEMAGENCOURSE");
		expected.add("COURSEID");
		expected.add("COURSENAME");
		expected.add("PRIMARY KEY (COURSEID)");
		pass1a = findDataInFile(f1, expected);

		expected.clear();
		expected.add("CREATE TABLE SCHEMAGENSTUDENT");
		expected.add("STUDENTID");
		expected.add("STUDENTNAME");
		expected.add("PRIMARY KEY (STUDENTID)");
		pass1b = findDataInFile(f1, expected);

		expected.clear();
		expected.add("CREATE TABLE SCHEMAGEN_COURSE_STUDENT");
		expected.add("COURSE_ID");
		expected.add("STUDENT_ID");
		// bug id 27382206
		// Based on JPA 2.2 specification section 2.10.4, the join table is not
		// required to have a primary key.
		// expected.add("PRIMARY KEY (COURSE_ID, STUDENT_ID)");
		pass1c = findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGEN_COURSE_STUDENT ADD");
		expected.add("CONSTRAINT STUDENTIDCONSTRAINT");
		expected.add("SCHEMAGENSTUDENT");
		pass1d = findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGEN_COURSE_STUDENT ADD");
		expected.add("CONSTRAINT COURSEIDCONSTRAINT");
		expected.add("SCHEMAGENCOURSE");
		pass1e = findDataInFile(f1, expected);

		/*
		 * CREATE TABLE SCHEMAGENCOURSE (COURSEID INTEGER NOT NULL, COURSENAME
		 * VARCHAR(255), PRIMARY KEY (COURSEID)) CREATE TABLE SCHEMAGENSTUDENT
		 * (STUDENTID INTEGER NOT NULL, STUDENTNAME VARCHAR(255), PRIMARY KEY
		 * (STUDENTID)) CREATE TABLE COURSE_STUDENT (COURSE_ID INTEGER NOT NULL,
		 * STUDENT_ID INTEGER NOT NULL, PRIMARY KEY (COURSE_ID, STUDENT_ID)) ALTER TABLE
		 * COURSE_STUDENT ADD CONSTRAINT STUDENTIDCONSTRAINT FOREIGN KEY (STUDENT_ID)
		 * REFERENCES SCHEMAGENSTUDENT (STUDENTID) ALTER TABLE COURSE_STUDENT ADD
		 * CONSTRAINT COURSEIDCONSTRAINT FOREIGN KEY (COURSE_ID) REFERENCES
		 * SCHEMAGENCOURSE (COURSEID)
		 */

		pass2a = findDataInFile(f2, List.of("ALTER TABLE", "SCHEMAGEN_COURSE_STUDENT DROP", "STUDENTIDCONSTRAINT"));
		pass2a = pass2a || findDataInFile(f2, List.of("DROP TABLE", "SCHEMAGEN_COURSE_STUDENT", "CASCADE CONSTRAINTS"));
		pass2b = findDataInFile(f2, List.of("ALTER TABLE", "SCHEMAGEN_COURSE_STUDENT DROP", "COURSEIDCONSTRAINT"));
		pass2b = pass2b || findDataInFile(f2, List.of("DROP TABLE", "SCHEMAGEN_COURSE_STUDENT", "CASCADE CONSTRAINTS"));
		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGEN_COURSE_STUDENT");
		pass2c = findDataInFile(f2, expected);

		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENCOURSE");
		pass2d = findDataInFile(f2, expected);

		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENSTUDENT");
		pass2e = findDataInFile(f2, expected);

		logger.log(Logger.Level.TRACE, "Execute the create script");
		props = getPersistenceUnitProperties();

		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.create-script-source", convertToURI(CREATEFILENAME));
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		clearEMAndEMF();
		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();

			Student expectedStudent = new Student(1, "Neo");

			// Create 12 Courses;
			Course appliedMath = new Course(101, "AppliedMathematics");
			Course physics = new Course(102, "Physics");

			// Set enrolled students for each course
			List<Student> studentList = new ArrayList<Student>();

			studentList.add(expectedStudent);

			appliedMath.setStudents(studentList);
			physics.setStudents(studentList);

			// Set Courses for first semester
			List<Course> firstSemCourses = new ArrayList<Course>();
			firstSemCourses.add(appliedMath);
			firstSemCourses.add(physics);

			// Set Courses for each student
			expectedStudent.setCourses(firstSemCourses);

			// persist student
			getEntityManager().persist(expectedStudent);

			// persist courses
			getEntityManager().persist(appliedMath);
			getEntityManager().persist(physics);

			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();

			Course course = getEntityManager().find(Course.class, 101);
			if (course != null) {
				if (course.courseName.equals(appliedMath.courseName)) {
					if (course.getStudents().size() == 1) {
						Student s1 = course.getStudents().get(0);
						if (expectedStudent.equals(s1)) {
							logger.log(Logger.Level.TRACE, "Received expected result:" + s1);
							pass3 = true;
						} else {
							logger.log(Logger.Level.ERROR, "Expected:" + expectedStudent.toString());
							logger.log(Logger.Level.ERROR, "Actual:" + s1.toString());
						}
					} else {
						logger.log(Logger.Level.ERROR,
								"Did not get list of students of size 1, actual:" + course.getStudents().size());
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected Course:" + appliedMath.courseName);
					logger.log(Logger.Level.ERROR, "Actual Course:" + course.courseName);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Received null result from find");
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		clearEMAndEMF();

		logger.log(Logger.Level.TRACE, "Execute the drop script");
		props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "drop");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.drop-script-source", convertToURI(DROPFILENAME));
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		clearEMAndEMF();

		logger.log(Logger.Level.INFO, "Try to persist an entity, it should fail");
		try {
			getEntityTransaction(true).begin();
			Student joe = new Student(2, "Joe");
			getEntityManager().persist(joe);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Receive expected exception");
			pass4 = true;
		}
		try {
			getEntityTransaction(true).begin();
			Course accounting = new Course(303, "Accounting");
			getEntityManager().persist(accounting);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Receive expected exception");
			pass5 = true;
		}

		logger.log(Logger.Level.TRACE, "pass1a:" + pass1a);
		logger.log(Logger.Level.TRACE, "pass1b:" + pass1b);
		logger.log(Logger.Level.TRACE, "pass1c:" + pass1c);
		logger.log(Logger.Level.TRACE, "pass1d:" + pass1d);
		logger.log(Logger.Level.TRACE, "pass1e:" + pass1e);
		logger.log(Logger.Level.TRACE, "pass2a:" + pass2a);
		logger.log(Logger.Level.TRACE, "pass2b:" + pass2b);
		logger.log(Logger.Level.TRACE, "pass2c:" + pass2c);
		logger.log(Logger.Level.TRACE, "pass2d:" + pass2d);
		logger.log(Logger.Level.TRACE, "pass2e:" + pass2e);
		logger.log(Logger.Level.TRACE, "pass3:" + pass3);
		logger.log(Logger.Level.TRACE, "pass4:" + pass4);
		logger.log(Logger.Level.TRACE, "pass5:" + pass5);
		if (!pass1a || !pass1b || !pass1c || !pass1d || !pass1e || !pass2a || !pass2b || !pass2c || !pass2d || !pass2e
				|| !pass3 || !pass4 || !pass5) {
			throw new Exception("joinTableTest failed");
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
			logger.log(Logger.Level.INFO, "Try to drop table SCHEMAGENSIMPLE");
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENCOURSE").executeUpdate();
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSTUDENT").executeUpdate();
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGEN_COURSE_STUDENT").executeUpdate();
			getEntityTransaction().commit();
		} catch (Throwable t) {
			logger.log(Logger.Level.INFO,
					"AN EXCEPTION WAS THROWN DURING DROPS, IT MAY OR MAY NOT BE A PROBLEM, " + t.getMessage());
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
				clearEntityTransaction();

				// ensure that we close the EM and EMF before proceeding.
				clearEMAndEMF();
			} catch (Exception re) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
