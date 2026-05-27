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

package ee.jakarta.tck.persistence.jpa40.statement;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Statement;
import jakarta.persistence.StatementOrTypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jakarta.persistence.sql.ResultSetMapping.column;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".StatementBook"};
        return createDeploymentJar("jpa_jpa40_statement.jar", packageName, classes);
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
     * Tests the Jakarta Persistence 4.0 {@link StatementOrTypedQuery} API.
     * The test verifies that {@code createQuery(String)} can be refined into a
     * typed query using {@code ofType()} and can also be refined with an entity
     * graph using {@code withEntityGraph()}.
     */
    @Test
    public void statementOrTypedQueryRefinementTest() {
        StatementBook typed = getEntityManager()
                .createQuery("SELECT b FROM Jpa40StatementBook b WHERE b.id = 1")
                .ofType(StatementBook.class)
                .getSingleResult();
        assertEquals("Alpha", typed.getTitle());

        EntityGraph<StatementBook> graph = getEntityManager().createEntityGraph(StatementBook.class);
        graph.addAttributeNode("title");
        StatementBook graphed = getEntityManager()
                .createQuery("SELECT b FROM Jpa40StatementBook b WHERE b.id = 1")
                .withEntityGraph(graph)
                .getSingleResult();
        assertEquals("Alpha", graphed.getTitle());
    }

    /**
     * Tests the exception types thrown by the Jakarta Persistence 4.0
     * {@link StatementOrTypedQuery} refinement operations when the underlying
     * query is not a kind accepted by the requested operation.
     */
    @Test
    public void statementOrTypedQueryWrongQueryTypeExceptionsTest() {
        EntityGraph<StatementBook> graph = getEntityManager().createEntityGraph(StatementBook.class);

        assertThrows(IllegalStateException.class, () -> getEntityManager()
                .createQuery("SELECT b FROM Jpa40StatementBook b")
                .asStatement());

        assertThrows(IllegalStateException.class, () -> getEntityManager()
                .createQuery("UPDATE Jpa40StatementBook b SET b.title = 'Updated'")
                .ofType(StatementBook.class));

        assertThrows(IllegalStateException.class, () -> getEntityManager()
                .createQuery("DELETE FROM Jpa40StatementBook b WHERE b.id = 1")
                .withEntityGraph(graph));

        assertThrows(IllegalStateException.class, () -> getEntityManager()
                .createQuery("SELECT b FROM Jpa40StatementBook b")
                .withResultSetMapping(column("TITLE", String.class)));
    }

    /**
     * Tests the exception type thrown by {@link StatementOrTypedQuery#ofType(Class)}
     * when the requested type is not a supertype of the query result type.
     */
    @Test
    public void statementOrTypedQueryOfTypeWrongResultTypeExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> getEntityManager()
                .createQuery("SELECT b FROM Jpa40StatementBook b")
                .ofType(String.class));
    }

    /**
     * Tests the Jakarta Persistence 4.0 {@link Statement} interface and
     * {@code asStatement()} conversion. The test verifies that an update
     * statement is explicitly executed with {@code Statement.execute()} rather
     * than as a result-returning query.
     */
    @Test
    public void statementExecutionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int count = getEntityManager()
                .createQuery("UPDATE Jpa40StatementBook b SET b.title = :title WHERE b.id = :id")
                .asStatement()
                .setParameter("title", "Updated")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, count);
        assertEquals("Updated", getEntityManager().find(StatementBook.class, 1).getTitle());
    }

    /**
     * Tests Jakarta Persistence 4.0 named statement annotations. The test
     * verifies that {@code @NamedStatement} and {@code @NamedNativeStatement}
     * define executable statements obtained through {@code createNamedStatement()}.
     */
    @Test
    public void namedAndNativeStatementExecutionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int namedCount = getEntityManager()
                .createNamedStatement(StatementBook.UPDATE_TITLE)
                .setParameter("title", "Named")
                .setParameter("id", 1)
                .execute();
        int nativeCount = getEntityManager()
                .createNamedStatement(StatementBook.NATIVE_UPDATE_TITLE)
                .setParameter(1, "Native")
                .setParameter(2, 2)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, namedCount);
        assertEquals(1, nativeCount);
        assertEquals("Named", getEntityManager().find(StatementBook.class, 1).getTitle());
        assertEquals("Native", getEntityManager().find(StatementBook.class, 2).getTitle());
    }

    /**
     * Tests Jakarta Persistence 4.0 {@code CriteriaStatement} execution. The
     * test verifies that a criteria update is converted to a {@link Statement}
     * using {@code createStatement(CriteriaStatement)} and executed explicitly.
     */
    @Test
    public void criteriaStatementExecutionTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<StatementBook> update = builder.createCriteriaUpdate(StatementBook.class);
        Root<StatementBook> root = update.from(StatementBook.class);
        update.set("title", "Criteria");
        update.where(builder.equal(root.get("id"), 1));

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int count = getEntityManager().createStatement(update).execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, count);
        assertEquals("Criteria", getEntityManager().find(StatementBook.class, 1).getTitle());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new StatementBook(1, "Alpha"));
        getEntityManager().persist(new StatementBook(2, "Beta"));
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
