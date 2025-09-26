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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.orderColumn;

import java.io.File;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Client extends PMClientBase {
	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	String schemaGenerationDir = null;

	Employee expectedEmployee = null;

	String sTestCase = "jpa_se_schemaGeneration_annotations_orderColumn";

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Department", pkgName + "Employee" };
		return createDeploymentJar("jpa_se_schemaGeneration_annotations_orderColumn.jar", pkgNameWithoutSuffix,
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
	 * @testName: orderColumnTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2118.14; PERSISTENCE:SPEC:2118.9;
	 * 
	 * @test_Strategy: Test the @OrderColumn annotation
	 */
	@Test
	public void orderColumnTest() throws Exception {
		boolean pass1a = false;
		boolean pass1b = false;
		boolean pass1c = false;
		boolean pass2a = false;
		boolean pass2b = false;
		boolean pass2c = false;
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
		expected.add("CREATE TABLE SCHEMAGENEMP");
		expected.add("EMPID");
		expected.add("FK_DEPT");
		expected.add("THEORDERCOLUMN");
		expected.add("PRIMARY KEY (EMPID)");
		pass1a = findDataInFile(f1, expected);

		expected.clear();
		expected.add("CREATE TABLE SCHEMAGENDEPT");
		expected.add("DEPTID");
		expected.add("PRIMARY KEY (DEPTID)");
		pass1b = findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENEMP ADD CONSTRAINT MYCONSTRANT FOREIGN KEY (FK_DEPT) REFERENCES SCHEMAGENDEPT (DEPTID)");
		pass1c = findDataInFile(f1, expected);

		/*
		 * CREATE TABLE SCHEMAGENDEPT (DEPTID INTEGER NOT NULL, PRIMARY KEY (DEPTID))
		 * CREATE TABLE SCHEMAGENEMP (EMPID INTEGER NOT NULL, FK_DEPT INTEGER,
		 * THEORDERCOLUMN INTEGER, PRIMARY KEY (EMPID)) ALTER TABLE SCHEMAGENEMP ADD
		 * CONSTRAINT MYCONSTRANT FOREIGN KEY (FK_DEPT) REFERENCES SCHEMAGENDEPT
		 * (DEPTID)
		 */

		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENEMP");
		pass2a = findDataInFile(f2, expected);
		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENDEPT");
		pass2b = findDataInFile(f2, expected);
		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENEMP DROP");
		pass2c = findDataInFile(f2, expected);
		pass2c = pass2c || findDataInFile(f2, List.of("DROP TABLE", "SCHEMAGENEMP", "CASCADE CONSTRAINTS"));

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

			Department d1 = new Department(50);
			getEntityManager().persist(d1);

			expectedEmployee = new Employee(20, d1);
			final Employee e2 = new Employee(40, d1);
			final Employee e3 = new Employee(60, d1);
			getEntityManager().persist(expectedEmployee);
			getEntityManager().persist(e2);
			getEntityManager().persist(e3);

			List<Employee> expectedEmployees = new ArrayList<Employee>();
			expectedEmployees.add(e3);
			expectedEmployees.add(expectedEmployee);
			expectedEmployees.add(e2);

			d1.setEmployees(expectedEmployees);
			getEntityManager().merge(d1);

			logger.log(Logger.Level.TRACE, "persisted Entity Data");
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction(true).begin();
			TypedQuery<Employee> q = getEntityManager().createQuery(
					"SELECT e FROM Department d JOIN d.employees e WHERE d.deptId = 50 AND INDEX(e) = 1",
					Employee.class);
			Employee e = q.getSingleResult();
			if (e != null) {
				if (e.equals(expectedEmployee)) {
					logger.log(Logger.Level.TRACE, "Received expected:" + expectedEmployee);
					pass3 = true;
				} else {
					logger.log(Logger.Level.ERROR, "expected:" + expectedEmployee + ", actual:" + e);
				}
			} else {
				logger.log(Logger.Level.ERROR, "Query of INDEX(1) returned null result");
			}
			getEntityTransaction().commit();
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
			Employee e2 = new Employee(2);
			getEntityManager().persist(e2);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Receive expected exception");
			pass4 = true;
		}
		try {
			getEntityTransaction(true).begin();
			Department d = new Department(2);
			getEntityManager().persist(d);
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
		logger.log(Logger.Level.TRACE, "pass2a:" + pass2a);
		logger.log(Logger.Level.TRACE, "pass2b:" + pass2b);
		logger.log(Logger.Level.TRACE, "pass2c:" + pass2c);
		logger.log(Logger.Level.TRACE, "pass3:" + pass3);
		logger.log(Logger.Level.TRACE, "pass4:" + pass4);
		logger.log(Logger.Level.TRACE, "pass5:" + pass5);
		if (!pass1a || !pass1b || !pass1c || !pass2a || !pass2b || !pass2c || !pass3 || !pass4 || !pass5) {
			throw new Exception("orderColumnTest failed");
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
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENEMP").executeUpdate();
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENDEPT").executeUpdate();
			getEntityTransaction().commit();
		} catch (Throwable t) {
			logger.log(Logger.Level.INFO,
					"AN EXCEPTION WAS THROWN DURING DROP TABLE, IT MAY OR MAY NOT BE A PROBLEM, " + t.getMessage());
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
