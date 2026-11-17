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

import ee.jakarta.tck.persistence.common.SchemaGenerationClientBase;
import jakarta.persistence.Persistence;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SeSchemaGenerationScriptsClient extends SchemaGenerationClientBase {
    private static final String TABLE = "SCHEMAGENSIMPLE";

    @Override
    public JavaArchive createDeployment() throws Exception {
        var packageName = SeSchemaGenerationScriptsClient.class.getPackageName();
        return createDeploymentJar("jpa_se_schemaGeneration_scripts.jar", packageName,
                new String[] {packageName + ".Simple"});
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
        verifyScripts("none", false, true);
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
        verifyScripts("none", true, true);
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
        verifyScripts("none", false, false);
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
        verifyScripts("none", true, false);
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
        verifyScripts("drop-and-create", false, true);
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
        verifyScripts("drop-and-create", true, true);
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
        verifyScripts("create", false, false);
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
        verifyScripts("drop-and-create", true, false);
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
        Persistence.generateSchema(getPersistenceUnitName(), schemaProperties("create"));
        verifyCreatedSchema();
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
        verifyCreateScript(false, true);
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
        verifyCreateScript(true, false);
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
        verifyDropScript(false, true);
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
        verifyDropScript(true, false);
    }

    private void generateScripts(String databaseAction, String scriptAction, boolean writers,
                                 boolean factory) throws Exception {
        Files.deleteIfExists(createScript());
        Files.deleteIfExists(dropScript());
        var properties = schemaProperties(databaseAction);
        properties.put("jakarta.persistence.schema-generation.scripts.action", scriptAction);
        if (writers) {
            try (var createWriter = new PrintWriter(Files.newBufferedWriter(createScript()));
                 var dropWriter = new PrintWriter(Files.newBufferedWriter(dropScript()))) {
                properties.put("jakarta.persistence.schema-generation.scripts.create-target", createWriter);
                properties.put("jakarta.persistence.schema-generation.scripts.drop-target", dropWriter);
                generate(properties, factory);
                assertFalse(createWriter.checkError(), "Could not write the create script");
                assertFalse(dropWriter.checkError(), "Could not write the drop script");
            }
        } else {
            properties.put("jakarta.persistence.schema-generation.scripts.create-target", createScript().toUri().toString());
            properties.put("jakarta.persistence.schema-generation.scripts.drop-target", dropScript().toUri().toString());
            generate(properties, factory);
        }
    }

    private void generate(Properties properties, boolean factory) {
        if (factory) {
            Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties).close();
        } else {
            Persistence.generateSchema(getPersistenceUnitName(), properties);
        }
    }

    private void verifyScripts(String databaseAction, boolean writers, boolean factory) throws Exception {
        assertTables(false, TABLE);
        if ("drop-and-create".equals(databaseAction)) {
            Persistence.generateSchema(getPersistenceUnitName(), schemaProperties("create"));
            verifyCreatedSchema();
            closePersistenceContext();
        }
        generateScripts(databaseAction, "drop-and-create", writers, factory);
        if ("none".equals(databaseAction)) {
            assertTables(false, TABLE);
        } else {
            // Reusing the same identifier also checks that drop-and-create removed the old row.
            verifyCreatedSchema();
            closePersistenceContext();
            executeScript("drop", dropScript());
            assertTables(false, TABLE);
        }
        executeScript("create", createScript());
        verifyCreatedSchema();
        closePersistenceContext();
        executeScript("drop", dropScript());
        assertTables(false, TABLE);
    }

    private void verifyCreateScript(boolean reader, boolean factory) throws Exception {
        assertTables(false, TABLE);
        generateScripts("none", "create", reader, factory);
        assertTables(false, TABLE);
        executeScript("create", createScript(), reader);
        verifyCreatedSchema();
    }

    private void verifyDropScript(boolean reader, boolean factory) throws Exception {
        generateScripts("none", "drop", reader, factory);
        assertTables(false, TABLE);
        Persistence.generateSchema(getPersistenceUnitName(), schemaProperties("create"));
        verifyCreatedSchema();
        closePersistenceContext();
        executeScript("drop", dropScript(), reader);
        assertTables(false, TABLE);
    }

    private void executeScript(String action, Path script, boolean reader) throws Exception {
        if (reader) {
            try (var source = Files.newBufferedReader(script)) {
                executeScript(action, source);
            }
        } else {
            executeScript(action, script);
        }
    }

    private void verifyCreatedSchema() throws Exception {
        assertTables(true, TABLE);
        var entity = new Simple(1, "script value");
        persist(entity);
        assertEquals(entity, getEntityManager().find(Simple.class, 1));
    }
}
