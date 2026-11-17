/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.common;

import jakarta.persistence.Persistence;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/** Tests the effects of generated scripts without depending on their SQL syntax. */
public abstract class SchemaGenerationClientBase extends PMClientBase {
    @TempDir
    protected Path schemaGenerationDirectory;

    public abstract JavaArchive createDeployment() throws Exception;

    @BeforeEach
    @Override
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        closePersistenceContext();
        // Each test starts with an empty schema, including tests which only write scripts.
        Persistence.generateSchema(getPersistenceUnitName(), schemaProperties("drop"));
    }

    @Override
    protected boolean dropCreateSchemaOnStartByDefault() {
        return false;
    }

    protected Path createScript() {
        return schemaGenerationDirectory.resolve("create.sql");
    }

    protected Path dropScript() {
        return schemaGenerationDirectory.resolve("drop.sql");
    }

    protected Properties schemaProperties(String databaseAction) {
        var properties = getPersistenceUnitProperties();
        properties.put("jakarta.persistence.schema-generation.database.action", databaseAction);
        properties.put("jakarta.persistence.schema-generation.scripts.action", "none");
        return properties;
    }

    protected void verifyGeneratedSchema(Executable verification, String... tables) throws Throwable {
        assertTables(false, tables);
        Files.deleteIfExists(createScript());
        Files.deleteIfExists(dropScript());
        var properties = schemaProperties("none");
        properties.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
        properties.put("jakarta.persistence.schema-generation.scripts.create-target", createScript().toUri().toString());
        properties.put("jakarta.persistence.schema-generation.scripts.drop-target", dropScript().toUri().toString());
        Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties).close();
        assertTables(false, tables);

        executeScript("create", createScript());
        assertTables(true, tables);
        try {
            verification.execute();
        } finally {
            closePersistenceContext();
        }
        executeScript("drop", dropScript());
        assertTables(false, tables);
    }

    protected void executeScript(String action, Path script) throws Exception {
        assertTrue(Files.isRegularFile(script), "Script was not generated: " + script);
        assertTrue(Files.size(script) > 0, "Generated script is empty: " + script);
        executeScript(action, script.toUri().toString());
    }

    protected void executeScript(String action, Object source) {
        var properties = schemaProperties(action);
        // Explicitly exclude metadata so it cannot conceal a missing or incomplete script.
        properties.put("jakarta.persistence.schema-generation." + action + "-source", "script");
        properties.put("jakarta.persistence.schema-generation." + action + "-script-source", source);
        Persistence.generateSchema(getPersistenceUnitName(), properties);
    }

    protected void inTransaction(Runnable work) {
        var transaction = getEntityTransaction(true);
        transaction.begin();
        try {
            work.run();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    protected void persist(Object... entities) {
        inTransaction(() -> {
            for (var entity : entities) {
                getEntityManager().persist(entity);
            }
        });
        clearPersistenceContext();
    }

    protected void clearPersistenceContext() {
        getEntityManager().clear();
        var cache = getEntityManagerFactory().getCache();
        if (cache != null) {
            cache.evictAll();
        }
    }

    /** Close without truncating, so the drop script is also tested against populated tables. */
    protected void closePersistenceContext() {
        var factory = getEntityManagerFactory();
        if (factory != null && factory.isOpen()) {
            var entityManager = getEntityManager();
            if (entityManager.isOpen()) {
                var transaction = entityManager.getTransaction();
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.close();
            }
            factory.close();
        }
        clearEntityManager();
        clearEntityManagerFactory();
        clearEntityTransaction();
    }

    protected Connection schemaConnection() throws Exception {
        return new DriverManagerConnection().getConnection(getPersistenceUnitProperties());
    }

    protected void executeSql(String sql) throws Exception {
        try (var connection = schemaConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    protected void assertSqlRejected(String sql) throws Exception {
        try (var connection = schemaConnection(); var statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            try {
                assertThrows(SQLException.class, () -> {
                    statement.executeUpdate(sql);
                    // A database may defer checking a constraint until commit.
                    connection.commit();
                }, sql);
            } finally {
                connection.rollback();
            }
        }
    }

    protected Object querySingleValue(String sql) throws Exception {
        try (var connection = schemaConnection(); var statement = connection.createStatement();
             var result = statement.executeQuery(sql)) {
            assertTrue(result.next(), "No row returned by: " + sql);
            var value = result.getObject(1);
            assertFalse(result.next(), "Multiple rows returned by: " + sql);
            return value;
        }
    }

    protected void assertTables(boolean present, String... names) throws Exception {
        try (var connection = schemaConnection()) {
            for (var name : names) {
                assertEquals(present, findTable(connection, name) != null,
                        "Table " + name + (present ? " was not created" : " still exists"));
            }
        }
    }

    private record Table(String catalog, String schema, String name) {}

    private Table findTable(Connection connection, String name) throws SQLException {
        String schema;
        try {
            schema = connection.getSchema();
        } catch (SQLFeatureNotSupportedException unsupported) {
            schema = null;
        }
        Table result = null;
        try (var tables = connection.getMetaData().getTables(connection.getCatalog(), schema, "%", new String[]{"TABLE"})) {
            while (tables.next()) {
                if (name.equalsIgnoreCase(tables.getString("TABLE_NAME"))
                        && (schema == null || schema.equals(tables.getString("TABLE_SCHEM")))) {
                    assertNull(result, "Ambiguous table " + name + " in JDBC metadata");
                    result = new Table(tables.getString("TABLE_CAT"), tables.getString("TABLE_SCHEM"),
                            tables.getString("TABLE_NAME"));
                }
            }
        }
        return result;
    }

    protected void assertForeignKey(String tableName, String constraint, String column,
                                    String referencedTable, String referencedColumn) throws Exception {
        try (var connection = schemaConnection()) {
            var table = findTable(connection, tableName);
            assertNotNull(table, "Missing table " + tableName);
            try (var keys = connection.getMetaData().getImportedKeys(table.catalog(), table.schema(), table.name())) {
                while (keys.next()) {
                    if (constraint.equalsIgnoreCase(keys.getString("FK_NAME"))) {
                        assertTrue(column.equalsIgnoreCase(keys.getString("FKCOLUMN_NAME")), constraint);
                        assertTrue(referencedTable.equalsIgnoreCase(keys.getString("PKTABLE_NAME")), constraint);
                        assertTrue(referencedColumn.equalsIgnoreCase(keys.getString("PKCOLUMN_NAME")), constraint);
                        return;
                    }
                }
            }
            fail("Foreign key " + constraint + " was not created on " + tableName);
        }
    }

    protected void assertIndex(String tableName, String index, String column, String direction) throws Exception {
        try (var connection = schemaConnection()) {
            var table = findTable(connection, tableName);
            assertNotNull(table, "Missing table " + tableName);
            try (var indexes = connection.getMetaData().getIndexInfo(table.catalog(), table.schema(), table.name(), false, false)) {
                while (indexes.next()) {
                    if (index.equalsIgnoreCase(indexes.getString("INDEX_NAME"))) {
                        assertTrue(column.equalsIgnoreCase(indexes.getString("COLUMN_NAME")), index);
                        // JDBC permits null when the driver does not expose the sort direction.
                        var actualDirection = indexes.getString("ASC_OR_DESC");
                        if (actualDirection != null) {
                            assertEquals(direction, actualDirection, index);
                        }
                        return;
                    }
                }
            }
            fail("Index " + index + " was not created on " + tableName);
        }
    }
}
