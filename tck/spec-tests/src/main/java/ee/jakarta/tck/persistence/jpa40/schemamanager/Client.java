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

package ee.jakarta.tck.persistence.jpa40.schemamanager;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.*;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static jakarta.persistence.Persistence.SchemaManagementProperties.SCHEMAGEN_LOAD_SCRIPT_SOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Client extends PMClientBase {

    private Path loadScript;

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SchemaPopulateBook"};
        return createDeploymentJar("jpa_jpa40_schemamanager.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        createLoadScript();
    }

    @AfterEach
    public void cleanup() throws Exception {
        try {
            removeTestJarFromCP();
        } finally {
            deleteLoadScript();
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 {@link SchemaManager#populate()}. The
     * test configures {@code SCHEMAGEN_LOAD_SCRIPT_SOURCE} with a SQL DML
     * script, clears the row loaded during schema creation, invokes
     * {@code SchemaManager.populate()} without running DDL, and verifies
     * that the provider imports the row from the configured script.
     */
    @Test
    public void schemaManagerPopulateImportsLoadScriptTest() {
        Map<String, Object> properties = new HashMap<>();
        getPersistenceUnitProperties().forEach((key, value) -> properties.put((String) key, value));

        PersistenceConfiguration configuration =
                new PersistenceConfiguration("JPATCK-JPA40-SCHEMAMANAGER")
                        .provider((String) properties.get(JAKARTA_PERSISTENCE_PROVIDER))
                        .managedClass(SchemaPopulateBook.class)
                        .schemaManagementDatabaseAction(SchemaManagementAction.DROP_AND_CREATE)
                        .properties(properties)
                        .property(SCHEMAGEN_LOAD_SCRIPT_SOURCE, loadScript.toUri().toString());

        try (EntityManagerFactory emf = configuration.createEntityManagerFactory()) {
            try {
                removeTestData();
                assertEquals(0L, countBooks(emf));

                emf.getSchemaManager().populate();

                assertEquals(1L, countBooks(emf));
                SchemaPopulateBook book =
                        emf.callInTransaction(entityManager -> entityManager.find(SchemaPopulateBook.class, 1));
                assertNotNull(book);
                assertEquals("Loaded by SchemaManager.populate", book.getTitle());
            } finally {
                emf.getSchemaManager().drop(false);
            }
        }
    }

    private void createLoadScript() throws Exception {
        loadScript = Files.createTempFile("jpa40-schemamanager-populate", ".sql");
        Files.writeString(loadScript,
                "INSERT INTO JPA40_SCHEMA_POPULATE_BOOK (ID, TITLE) "
                        + "VALUES (1, 'Loaded by SchemaManager.populate');",
                StandardCharsets.UTF_8);
    }

    private void deleteLoadScript() throws Exception {
        if (loadScript != null) {
            Files.deleteIfExists(loadScript);
        }
    }

    private void removeTestData() {
        EntityTransaction transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        getEntityManagerFactory().getSchemaManager().truncate();
        getEntityManager().clear();
    }

    private long countBooks(EntityManagerFactory emf) {
        return emf.callInTransaction(entityManager -> entityManager
                .createQuery("SELECT COUNT(b) FROM Jpa40SchemaPopulateBook b", Long.class)
                .getSingleResult());
    }
}
