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

package ee.jakarta.tck.persistence.se.schemaGeneration.annotations.index;

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

public class Client extends PMClientBase {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	private static final long serialVersionUID = 22L;

	String schemaGenerationDir = null;

	String sTestCase = "jpa_se_schemaGeneration_annotations_index";

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Simple" };
		return createDeploymentJar("jpa_se_schemaGeneration_annotations_index.jar", pkgNameWithoutSuffix,
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
	 * @testName: indexTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2118.7
	 * 
	 * @test_Strategy: Test the @Index annotation
	 */
	@Test
	public void indexTest() throws Exception {
		boolean pass1a = false;
		boolean pass1b = false;
		boolean pass1c = false;
		boolean pass1d = false;

		boolean pass2a = false;
		// boolean pass2b = false;
		// boolean pass2c = false;
		// boolean pass2d = false;

		boolean pass3 = false;
		boolean pass4 = false;

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
		expected.add("CREATE TABLE SCHEMAGENSIMPLE");
		expected.add("SVALUE VARCHAR");
		expected.add("SVALUE2 VARCHAR");
		expected.add("SVALUE3 VARCHAR");
		expected.add("PRIMARY KEY (ID)");
		pass1a = findDataInFile(f1, expected);

		/*
		 * Bug 27422087 Index can be created using ALTER TABLE [table_name] ADD INDEX
		 * [index_name] pass1b = findDataInFile(f1,
		 * "CREATE INDEX SCHEMAGENSIMPLE_SVALUE_ASC ON SCHEMAGENSIMPLE (SVALUE)");
		 * pass1c = findDataInFile(f1,
		 * "CREATE INDEX SCHEMAGENSIMPLE_SVALUE2_DESC ON SCHEMAGENSIMPLE (SVALUE2 DESC)"
		 * ); pass1d = findDataInFile(f1,
		 * "CREATE UNIQUE INDEX SCHEMAGENSIMPLE_SVALUE3 ON SCHEMAGENSIMPLE (SVALUE3)" );
		 * 
		 * CREATE TABLE SCHEMAGENSIMPLE (ID INTEGER NOT NULL, SVALUE VARCHAR(255),
		 * SVALUE2 VARCHAR(255), SVALUE3 VARCHAR(255), PRIMARY KEY (ID)) CREATE INDEX
		 * SCHEMAGENSIMPLE_SVALUE_ASC ON SCHEMAGENSIMPLE (SVALUE) CREATE INDEX
		 * SCHEMAGENSIMPLE_SVALUE2_DESC ON SCHEMAGENSIMPLE (SVALUE2 DESC) CREATE UNIQUE
		 * INDEX SCHEMAGENSIMPLE_SVALUE3 ON SCHEMAGENSIMPLE (SVALUE3)
		 */
		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENSIMPLE");
		expected.add("ADD");
		expected.add("INDEX SCHEMAGENSIMPLE_SVALUE_ASC");

		pass1b = findDataInFile(f1, "CREATE INDEX SCHEMAGENSIMPLE_SVALUE_ASC ON SCHEMAGENSIMPLE (SVALUE)");
		pass1b = pass1b || findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENSIMPLE");
		expected.add("ADD");
		expected.add("INDEX SCHEMAGENSIMPLE_SVALUE2_DESC");

		pass1c = findDataInFile(f1, "CREATE INDEX SCHEMAGENSIMPLE_SVALUE2_DESC ON SCHEMAGENSIMPLE (SVALUE2 DESC)");
		pass1c = pass1c || findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENSIMPLE");
		expected.add("ADD");
		expected.add("UNIQUE");
		expected.add("INDEX SCHEMAGENSIMPLE_SVALUE3");

		pass1d = findDataInFile(f1, "CREATE UNIQUE INDEX SCHEMAGENSIMPLE_SVALUE3 ON SCHEMAGENSIMPLE (SVALUE3)");
		pass1d = pass1d || findDataInFile(f1, expected);

		expected.clear();
		expected.add("ALTER TABLE");
		expected.add("SCHEMAGENSIMPLE");
		expected.add("ADD");
		expected.add("CONSTRAINT");
		expected.add("SCHEMAGENSIMPLE_SVALUE3");
		expected.add("UNIQUE");

		pass1d = pass1d || findDataInFile(f1, "CONSTRAINT SCHEMAGENSIMPLE_SVALUE3 UNIQUE (SVALUE3)");
		pass1d = pass1d || findDataInFile(f1, expected);

		expected.clear();
		expected.add("DROP TABLE");
		expected.add("SCHEMAGENSIMPLE");
		pass2a = findDataInFile(f2, expected);
		/*
		 * Index can be dropped using ALTER TABLE AS WELL Bug 27422087: Some databases
		 * do drop things such as indexes and constraints associated with a table when
		 * the table is dropped.
		 *
		 * pass2b = findDataInFile(f2, "DROP INDEX SCHEMAGENSIMPLE_SVALUE_ASC"); pass2c
		 * = findDataInFile(f2, "DROP INDEX SCHEMAGENSIMPLE_SVALUE2_DESC"); pass2d =
		 * findDataInFile(f2, "DROP INDEX SCHEMAGENSIMPLE_SVALUE3");
		 */

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
			Simple s = new Simple(1, "1", "1", "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass3 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
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
			Simple s3 = new Simple(2, "2", "2", "2");
			getEntityManager().persist(s3);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Receive expected exception");
			pass4 = true;
		}
		logger.log(Logger.Level.INFO, "pass1a:" + pass1a);
		logger.log(Logger.Level.INFO, "pass1b:" + pass1b);
		logger.log(Logger.Level.INFO, "pass1c:" + pass1c);
		logger.log(Logger.Level.INFO, "pass1d:" + pass1d);
		logger.log(Logger.Level.INFO, "pass2a:" + pass1a);
		// logger.log(Logger.Level.INFO,"pass2b:" + pass1b);
		// logger.log(Logger.Level.INFO,"pass2c:" + pass1c);
		// logger.log(Logger.Level.INFO,"pass2d:" + pass1d);
		logger.log(Logger.Level.INFO, "pass3:" + pass3);
		logger.log(Logger.Level.INFO, "pass4:" + pass4);

		if (!pass1a || !pass1b || !pass1c || !pass1d || !pass2a || // !pass2b ||
																	// !pass2c ||
																	// !pass2d ||
				!pass3 || !pass4) {
			throw new Exception("indexTest failed");
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
			getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSIMPLE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Throwable t) {
			logger.log(Logger.Level.INFO,
					"AN EXCEPTION WAS THROWN DURING DROP TABLE SCHEMAGENSIMPLE, IT MAY OR MAY NOT BE A PROBLEM, "
							+ t.getMessage());
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
