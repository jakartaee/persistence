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

import static jakarta.persistence.sql.ResultSetMapping.column;
import static jakarta.persistence.sql.ResultSetMapping.compound;
import static jakarta.persistence.sql.ResultSetMapping.constructor;
import static jakarta.persistence.sql.ResultSetMapping.entity;
import static jakarta.persistence.sql.ResultSetMapping.field;
import static jakarta.persistence.sql.ResultSetMapping.tuple;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SqlMappingBook(1, "Alpha"));
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
