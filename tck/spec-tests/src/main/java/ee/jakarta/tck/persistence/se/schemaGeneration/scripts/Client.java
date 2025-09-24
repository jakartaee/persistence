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

/*
    NOTES:

    Since the JPA Alternate Provider is enabled at all times and returns
    false when Persistence.generateSchema(...) is called, the assertion
    PERSISTENCE:SPEC:2480 is covered.
 */
package ee.jakarta.tck.persistence.se.schemaGeneration.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
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

	String schemaGenerationDir = null;
	String sTestCase = "jpa_se_schemaGeneration_scripts";

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Simple" };
		return createDeploymentJar("jpa_se_schemaGeneration_scripts.jar", pkgNameWithoutSuffix, (String[]) classes);

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
	 * @testName: scriptsURLTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
	 * PERSISTENCE:SPEC:1893.2; PERSISTENCE:SPEC:1898.3; PERSISTENCE:SPEC:1898.4;
	 * PERSISTENCE:SPEC:1898.8; PERSISTENCE:SPEC:1898.9;
	 * 
	 * @test_Strategy: create scripts via createEntityManagerFactory using a URL
	 * location
	 */
	@Test
	public void scriptsURLTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

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

		pass1 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass2 = findDataInFile(f2, expected);
		if (!pass1 || !pass2) {
			throw new Exception("scriptsURLTest failed");
		}
	}

	/*
	 * @testName: scriptsPrintWriterTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
	 * PERSISTENCE:SPEC:1898.8; PERSISTENCE:SPEC:1898.9;
	 * 
	 * @test_Strategy: create scripts via createEntityManagerFactory using a
	 * PrintWriter
	 */
	@Test
	public void scriptsPrintWriterTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = null;
		PrintWriter pw1 = null;
		try {
			f1 = new File(CREATEFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous create script");
			deleteItem(f1);
			pw1 = new PrintWriter(new File(CREATEFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}
		File f2 = null;
		PrintWriter pw2 = null;
		try {
			f2 = new File(DROPFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous drop script");
			deleteItem(f2);

			pw2 = new PrintWriter(new File(DROPFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		if (pw1 != null)
			pw1.close();
		if (pw2 != null)
			pw2.close();

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass1 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass2 = findDataInFile(f2, expected);
		if (!pass1 || !pass2) {
			throw new Exception("scriptsPrintWriterTest failed");
		}
	}

	/*
	 * @testName: scriptsURL2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:JAVADOC:3335;
	 * PERSISTENCE:SPEC:1899.1; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
	 * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create scripts via Persistence.generateSchema using a URL
	 * location
	 */
	@Test
	public void scriptsURL2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

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

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass1 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass2 = findDataInFile(f2, expected);
		if (!pass1 || !pass2) {
			throw new Exception("scriptsURL2Test failed");
		}
	}

	/*
	 * @testName: scriptsPrintWriter2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:JAVADOC:3335;
	 * PERSISTENCE:SPEC:1899.1; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
	 * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create scripts via Persistence.generateSchema using a
	 * PrintWriter
	 */
	@Test
	public void scriptsPrintWriter2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = null;
		PrintWriter pw1 = null;
		try {
			f1 = new File(CREATEFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous create script");
			deleteItem(f1);
			pw1 = new PrintWriter(new File(CREATEFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		File f2 = null;
		PrintWriter pw2 = null;
		try {
			f2 = new File(DROPFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous drop script");
			deleteItem(f2);
			pw2 = new PrintWriter(new File(DROPFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		if (pw1 != null)
			pw1.close();
		if (pw2 != null)
			pw2.close();

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass1 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass2 = findDataInFile(f2, expected);
		if (!pass1 || !pass2) {
			throw new Exception("scriptsPrintWriter2Test failed");
		}
	}

	/*
	 * @testName: databaseAndScriptsURLTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
	 * PERSISTENCE:SPEC:1898.3
	 * 
	 * @test_Strategy: create scripts and generate the schema via
	 * createEntityManagerFactory using a URL location
	 */
	@Test
	public void databaseAndScriptsURLTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

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
		props.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass2 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass3 = findDataInFile(f2, expected);
		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("databaseAndScriptsURLTest failed");
		}
	}

	/*
	 * @testName: databaseAndScriptsPrintWriterTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.2;
	 * 
	 * @test_Strategy: create scripts and generate the schema via
	 * createEntityManagerFactory using a PrintWriter
	 */
	@Test
	public void databaseAndScriptsPrintWriterTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = null;
		PrintWriter pw1 = null;
		try {
			f1 = new File(CREATEFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous create script");
			deleteItem(f1);
			pw1 = new PrintWriter(new File(CREATEFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}
		File f2 = null;
		PrintWriter pw2 = null;
		try {
			f2 = new File(DROPFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous drop script");
			deleteItem(f2);
			pw2 = new PrintWriter(new File(DROPFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;
		if (pw1 != null)
			pw1.close();
		if (pw2 != null)
			pw2.close();

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass2 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass3 = findDataInFile(f2, expected);
		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("databaseAndScriptsPrintWriterTest failed");
		}
	}

	/*
	 * @testName: databaseAndScriptsURL2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create scripts and generate the schema via
	 * Persistence.generateSchema using a URL location
	 */
	@Test
	public void databaseAndScriptsURL2Test() throws Exception {
		boolean pass = false;

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
		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		if (!pass) {
			throw new Exception("databaseAndScriptsURL2Test failed");
		}
	}

	/*
	 * @testName: databaseAndScriptsPrintWriter2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1893; PERSISTENCE:SPEC:1893.1; PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create scripts and generate the schema via
	 * Persistence.generateSchema using a PrintWriter
	 */
	@Test
	public void databaseAndScriptsPrintWriter2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f1 = null;
		PrintWriter pw1 = null;
		try {
			f1 = new File(CREATEFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous create script");
			deleteItem(f1);
			pw1 = new PrintWriter(new File(CREATEFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}
		File f2 = null;
		PrintWriter pw2 = null;
		try {
			f2 = new File(DROPFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous drop script");
			deleteItem(f2);
			pw2 = new PrintWriter(new File(DROPFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		if (pw1 != null)
			pw1.close();
		if (pw2 != null)
			pw2.close();

		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass1 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass2 = findDataInFile(f1, "create table schemaGenSimple");
		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass3 = findDataInFile(f2, expected);
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("databaseAndScriptsPrintWriter2Test failed");
		}
	}

	/*
	 * @testName: databaseCreateTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1898.3; PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create scripts and generate the schema via
	 * Persistence.generateSchema using a PrintWriter
	 */
	@Test
	public void databaseCreateTest() throws Exception {
		boolean pass = false;

		Properties props = getPersistenceUnitProperties();

		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}

		if (!pass) {
			throw new Exception("databaseCreateTest failed");
		}
	}

	/*
	 * @testName: executeCreateScriptURLTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1898.12; PERSISTENCE:SPEC:1898.14;
	 * PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create a create script then use a URL location to execute it
	 */
	@Test
	public void executeCreateScriptURLTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");
		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";

		Properties props = getPersistenceUnitProperties();

		File f1 = new File(CREATEFILENAME);
		logger.log(Logger.Level.TRACE, "Deleting previous create script");
		deleteItem(f1);

		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass1 = findDataInFile(f1, "create table schemaGenSimple");

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
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		if (!pass1 || !pass2) {
			throw new Exception("executeCreateScriptURLTest failed");
		}
	}

	/*
	 * @testName: executeCreateScriptReaderTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1870.1; PERSISTENCE:SPEC:1898.14; PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create a create script then use a Reader to load and execute
	 * it
	 */
	@Test
	public void executeCreateScriptReaderTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";

		File f1 = null;
		PrintWriter pw1 = null;
		try {
			f1 = new File(CREATEFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous create script");
			deleteItem(f1);
			pw1 = new PrintWriter(new File(CREATEFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "create");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
		props.put("jakarta.persistence.schema-generation.scripts.create-target", pw1);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		logger.log(Logger.Level.INFO, "Check script(s) content");

		pass1 = findDataInFile(f1, "create table schemaGenSimple");

		logger.log(Logger.Level.INFO, "Execute the script");
		props = getPersistenceUnitProperties();

		Reader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f1));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		props.put("jakarta.persistence.schema-generation.create-script-source", reader);
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		if (pw1 != null)
			pw1.close();
		if (reader != null)
			reader.close();

		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		if (!pass1 || !pass2) {
			throw new Exception("executeCreateScriptReaderTest failed");
		}
	}

	/*
	 * @testName: executeDropScriptURLTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:1899.2; PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475;
	 * PERSISTENCE:SPEC:2476; PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357;
	 * PERSISTENCE:SPEC:2480; PERSISTENCE:SPEC:1870.2; PERSISTENCE:SPEC:1898.3;
	 * PERSISTENCE:SPEC:1898.4; PERSISTENCE:SPEC:1898.12; PERSISTENCE:SPEC:1898.15;
	 * PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create drop script using a URL location and execute it
	 */
	@Test
	public void executeDropScriptURLTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");
		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f2 = new File(DROPFILENAME);
		logger.log(Logger.Level.TRACE, "Deleting previous drop script");
		deleteItem(f2);
		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop");
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.createEntityManagerFactory(...)");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
		emf.close();
		emf = null;

		logger.log(Logger.Level.INFO, "Check script(s) content");

		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass1 = findDataInFile(f2, expected);

		logger.log(Logger.Level.TRACE, "Generate schema");
		props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		clearEMAndEMF();

		try {
			logger.log(Logger.Level.INFO, "Persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass2 = true;
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
			Simple s3 = new Simple(2, "2");
			getEntityManager().persist(s3);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop had occurred successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Received expected exception");
			pass3 = true;
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("executeDropScriptURLTest failed");
		}
	}

	/*
	 * @testName: executeDropScriptReaderTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1899; PERSISTENCE:SPEC:1899.1;
	 * PERSISTENCE:SPEC:2474; PERSISTENCE:SPEC:2475; PERSISTENCE:SPEC:2476;
	 * PERSISTENCE:SPEC:2478; PERSISTENCE:JAVADOC:3357; PERSISTENCE:SPEC:2480;
	 * PERSISTENCE:SPEC:1898.15; PERSISTENCE:SPEC:1915;
	 * 
	 * @test_Strategy: create drop script then use a Reader to load and execute it.
	 */
	@Test
	public void executeDropScriptReaderTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		logger.log(Logger.Level.INFO, "Create the script(s)");

		final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

		File f2 = null;
		PrintWriter pw2 = null;
		try {
			f2 = new File(DROPFILENAME);
			logger.log(Logger.Level.TRACE, "Deleting previous drop script");
			deleteItem(f2);
			pw2 = new PrintWriter(new File(DROPFILENAME));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}

		Properties props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "none");
		props.put("jakarta.persistence.schema-generation.scripts.action", "drop");
		props.put("jakarta.persistence.schema-generation.scripts.drop-target", pw2);

		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		if (pw2 != null)
			pw2.close();

		logger.log(Logger.Level.INFO, "Check script(s) content");

		List<String> expected = new ArrayList<String>();
		expected.add("drop table");
		expected.add("schemaGenSimple");
		pass1 = findDataInFile(f2, expected);

		logger.log(Logger.Level.TRACE, "Generate schema");
		props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "create");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);

		clearEMAndEMF();

		try {
			logger.log(Logger.Level.TRACE, "Try to persist some data");
			getEntityTransaction(true).begin();
			Simple s = new Simple(1, "1");
			getEntityManager().persist(s);
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Simple s2 = getEntityManager().find(Simple.class, 1);
			getEntityTransaction().commit();
			if (s.equals(s2)) {
				logger.log(Logger.Level.TRACE, "Received expected result:" + s.toString());
				pass2 = true;
			} else {
				logger.log(Logger.Level.ERROR, "Expected:" + s.toString());
				logger.log(Logger.Level.ERROR, "Actual:" + s2.toString());
			}
		} catch (Throwable t) {
			logger.log(Logger.Level.ERROR, "Received unexpected exception", t);
		}
		clearEMAndEMF();
		logger.log(Logger.Level.TRACE, "Execute the drop script");

		Reader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f2));
		} catch (Exception ex) {
			logger.log(Logger.Level.ERROR, "Unexpected exception occurred:" + ex);
		}
		props = getPersistenceUnitProperties();
		props.put("jakarta.persistence.schema-generation.database.action", "drop");
		props.put("jakarta.persistence.schema-generation.scripts.action", "none");
		props.put("jakarta.persistence.schema-generation.drop-script-source", reader);
		displayProperties(props);

		logger.log(Logger.Level.INFO, "Executing Persistence.generateSchema(...)");
		Persistence.generateSchema(getPersistenceUnitName(), props);
		clearEMAndEMF();

		if (reader != null)
			reader.close();

		logger.log(Logger.Level.INFO, "Try to persist an entity, it should fail because table should not exist");
		try {
			getEntityTransaction(true).begin();
			Simple s3 = new Simple(2, "2");
			getEntityManager().persist(s3);
			getEntityTransaction().commit();
			logger.log(Logger.Level.ERROR, "An exception should have been thrown if drop script executed successfully");
		} catch (Exception ex) {
			logger.log(Logger.Level.TRACE, "Received expected exception");
			pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("executeDropScriptReaderTest failed");
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
