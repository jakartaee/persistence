/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa40.persistenceconfiguration;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.SchemaManagementAction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static jakarta.persistence.Persistence.SchemaManagementProperties.SCHEMAGEN_CREATE_TARGET;
import static jakarta.persistence.Persistence.SchemaManagementProperties.SCHEMAGEN_DROP_TARGET;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    private Path schemaDirectory;
    private Path createScript;
    private Path dropScript;

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".ExportSchemaBook"};
        return createDeploymentJar("jpa_jpa40_persistenceconfiguration.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        schemaDirectory = Files.createTempDirectory("jpa40-export-schema");
        createScript = schemaDirectory.resolve("create.sql");
        dropScript = schemaDirectory.resolve("drop.sql");
    }

    @Override
    protected boolean dropCreateSchemaOnStartByDefault() {
        return false;
    }

    /**
     * Tests Jakarta Persistence 4.0
     * {@link PersistenceConfiguration#exportSchema()}. The test configures
     * schema script generation programmatically and verifies
     * {@code exportSchema()} generates DDL scripts without creating an
     * {@code EntityManagerFactory}.
     */
    @Test
    public void persistenceConfigurationExportSchemaGeneratesScriptsTest() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        getPersistenceUnitProperties().forEach((key, value) -> properties.put((String) key, value));

        new PersistenceConfiguration("JPATCK-JPA40-EXPORT-SCHEMA")
                .provider((String) properties.get(JAKARTA_PERSISTENCE_PROVIDER))
                .managedClass(ExportSchemaBook.class)
                .schemaManagementDatabaseAction(SchemaManagementAction.NONE)
                .schemaManagementScriptsAction(SchemaManagementAction.DROP_AND_CREATE)
                .properties(properties)
                .property(SCHEMAGEN_CREATE_TARGET, createScript.toUri().toString())
                .property(SCHEMAGEN_DROP_TARGET, dropScript.toUri().toString())
                .exportSchema();

        assertScriptContains(createScript, "CREATE", "JPA40_EXPORT_SCHEMA_BOOK", "TITLE");
        assertScriptContains(dropScript, "DROP", "JPA40_EXPORT_SCHEMA_BOOK");
    }

    private void assertScriptContains(Path script, String... expected) throws Exception {
        assertTrue(Files.exists(script), "Expected schema script to exist: " + script);
        assertTrue(Files.size(script) > 0, "Expected schema script to be non-empty: " + script);
        String contents = Files.readString(script).toUpperCase(Locale.ROOT);
        for (String value : expected) {
            assertTrue(contents.contains(value), "Expected " + script + " to contain: " + value);
        }
    }

    private void deleteSchemaScripts() throws Exception {
        if (createScript != null) {
            Files.deleteIfExists(createScript);
        }
        if (dropScript != null) {
            Files.deleteIfExists(dropScript);
        }
        if (schemaDirectory != null) {
            Files.deleteIfExists(schemaDirectory);
        }
    }
}
