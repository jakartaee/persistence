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

package ee.jakarta.tck.persistence.jpa40.sqlresultmapping;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Tuple;
import jakarta.persistence.sql.ColumnMapping;
import jakarta.persistence.sql.CompoundMapping;
import jakarta.persistence.sql.ConstructorMapping;
import jakarta.persistence.sql.EntityMapping;
import jakarta.persistence.sql.FieldMapping;
import jakarta.persistence.sql.ResultSetMapping;
import jakarta.persistence.sql.TupleMapping;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jakarta.persistence.sql.ResultSetMapping.column;
import static jakarta.persistence.sql.ResultSetMapping.compound;
import static jakarta.persistence.sql.ResultSetMapping.constructor;
import static jakarta.persistence.sql.ResultSetMapping.entity;
import static jakarta.persistence.sql.ResultSetMapping.field;
import static jakarta.persistence.sql.ResultSetMapping.tuple;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SqlMappingBook", packageName + ".SqlMappingDto"};
        return createDeploymentJar("jpa_jpa40_sqlresultmapping.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    @AfterEach
    public void cleanup() throws Exception {
        try {
            removeTestData();
        } finally {
            try {
                super.cleanup();
            } finally {
                removeTestJarFromCP();
            }
        }
    }

    /**
     * Tests the Jakarta Persistence 4.0 programmatic SQL result set mapping API.
     * The test verifies construction of scalar, constructor, entity, tuple, and
     * compound mappings and uses a constructor mapping to execute a typed native
     * query.
     */
    @Test
    public void programmaticSqlResultSetMappingsTest() {
        ColumnMapping<Integer> id = column("ID", Integer.class).withAlias("id");
        ColumnMapping<String> title = column("TITLE", String.class).withAlias("title");
        ConstructorMapping<SqlMappingDto> constructorMapping =
                constructor(SqlMappingDto.class, id, title).withAlias("bookDto");
        EntityMapping<SqlMappingBook> entityMapping = entity(SqlMappingBook.class,
                field(SqlMappingBook.class, Integer.class, "id", "ID"),
                field(SqlMappingBook.class, String.class, "title", "TITLE"));
        TupleMapping tupleMapping = tuple(id, title);
        CompoundMapping compoundMapping = compound(id, title);

        assertEquals(Integer.class, id.type());
        assertEquals("bookDto", constructorMapping.getAlias());
        assertEquals(SqlMappingBook.class, entityMapping.type());
        assertEquals(Tuple.class, tupleMapping.type());
        assertArrayEquals(new Object[]{id, title}, compoundMapping.elements());

        ResultSetMapping<SqlMappingDto> mapping = constructorMapping;
        SqlMappingDto dto = getEntityManager()
                .createNativeQuery("SELECT ID, TITLE FROM JPA40_SQL_BOOK WHERE ID = 1", mapping)
                .getSingleResult();
        assertEquals(new SqlMappingDto(1, "Alpha"), dto);
    }

    /**
     * Verifies a programmatic scalar result set mapping returns one scalar value
     * for each JDBC result row.
     */
    @Test
    public void programmaticScalarSqlResultSetMappingTest() {
        ColumnMapping<Integer> id = column("BOOK_ID", Integer.class).withAlias("bookId");

        List<Integer> ids = getEntityManager()
                .createNativeQuery("SELECT ID AS BOOK_ID FROM JPA40_SQL_BOOK ORDER BY ID", id)
                .getResultList();

        assertEquals(List.of(1, 2), ids);
    }

    /**
     * Verifies a programmatic constructor result set mapping returns one
     * constructed value for each JDBC result row.
     */
    @Test
    public void programmaticConstructorSqlResultSetMappingListTest() {
        ConstructorMapping<SqlMappingDto> mapping = constructor(SqlMappingDto.class,
                column("BOOK_ID", Integer.class),
                column("BOOK_TITLE", String.class));

        List<SqlMappingDto> dtos = getEntityManager()
                .createNativeQuery(
                        "SELECT ID AS BOOK_ID, TITLE AS BOOK_TITLE FROM JPA40_SQL_BOOK ORDER BY ID",
                        mapping)
                .getResultList();

        assertEquals(List.of(new SqlMappingDto(1, "Alpha"), new SqlMappingDto(2, "Beta")), dtos);
    }

    /**
     * Verifies a programmatic entity result set mapping can map aliased result
     * columns to entity fields.
     */
    @Test
    public void programmaticEntitySqlResultSetMappingTest() {
        EntityMapping<SqlMappingBook> mapping = entity(SqlMappingBook.class,
                field(SqlMappingBook.class, Integer.class, "id", "BOOK_ID"),
                field(SqlMappingBook.class, String.class, "title", "BOOK_TITLE"));

        SqlMappingBook book = getEntityManager()
                .createNativeQuery(
                        "SELECT ID AS BOOK_ID, TITLE AS BOOK_TITLE FROM JPA40_SQL_BOOK WHERE ID = 1",
                        mapping)
                .getSingleResult();

        assertEquals(1, book.getId());
        assertEquals("Alpha", book.getTitle());
    }

    /**
     * Verifies a programmatic tuple result set mapping exposes values by tuple
     * element, alias, index, and array form.
     */
    @Test
    public void programmaticTupleSqlResultSetMappingTest() {
        ColumnMapping<Integer> id = column("BOOK_ID", Integer.class).withAlias("bookId");
        ColumnMapping<String> title = column("BOOK_TITLE", String.class).withAlias("bookTitle");
        TupleMapping mapping = tuple(id, title);

        Tuple result = getEntityManager()
                .createNativeQuery(
                        "SELECT ID AS BOOK_ID, TITLE AS BOOK_TITLE FROM JPA40_SQL_BOOK WHERE ID = 1",
                        mapping)
                .getSingleResult();

        assertEquals(1, result.get(id));
        assertEquals("Alpha", result.get(title));
        assertEquals(1, result.get("bookId", Integer.class));
        assertEquals("Alpha", result.get("bookTitle", String.class));
        assertEquals(1, result.get(0, Integer.class));
        assertEquals("Alpha", result.get(1, String.class));
        assertArrayEquals(new Object[]{1, "Alpha"}, result.toArray());
    }

    /**
     * Verifies a programmatic compound result set mapping can return an object
     * array containing an entity, a constructed value, and a scalar value.
     */
    @Test
    public void programmaticCompoundSqlResultSetMappingTest() {
        ColumnMapping<Integer> id = column("BOOK_ID", Integer.class).withAlias("bookId");
        ColumnMapping<String> title = column("BOOK_TITLE", String.class).withAlias("bookTitle");
        EntityMapping<SqlMappingBook> bookMapping = entity(SqlMappingBook.class,
                field(SqlMappingBook.class, Integer.class, "id", "BOOK_ID"),
                field(SqlMappingBook.class, String.class, "title", "BOOK_TITLE"))
                .withAlias("book");
        ConstructorMapping<SqlMappingDto> dtoMapping = constructor(SqlMappingDto.class, id, title)
                .withAlias("dto");
        CompoundMapping mapping = compound(bookMapping, dtoMapping, title);

        Object[] result = getEntityManager()
                .createNativeQuery(
                        "SELECT ID AS BOOK_ID, TITLE AS BOOK_TITLE FROM JPA40_SQL_BOOK WHERE ID = 1",
                        mapping)
                .getSingleResult();

        assertEquals(3, result.length);
        assertTrue(result[0] instanceof SqlMappingBook);
        SqlMappingBook book = (SqlMappingBook) result[0];
        assertEquals(1, book.getId());
        assertEquals("Alpha", book.getTitle());
        assertEquals(new SqlMappingDto(1, "Alpha"), result[1]);
        assertEquals("Alpha", result[2]);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SqlMappingBook(1, "Alpha"));
        getEntityManager().persist(new SqlMappingBook(2, "Beta"));
        transaction.commit();
        getEntityManager().clear();
    }

    private void removeTestData() {
        EntityTransaction transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        getEntityManagerFactory().getSchemaManager().truncate();
        getEntityManager().clear();
    }
}
