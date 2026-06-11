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

package ee.jakarta.tck.persistence.jpa40.namedqueryregistration;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Statement;
import jakarta.persistence.StatementReference;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {
    private static final String REGISTERED_QUERY = "Jpa40RegistrationBook.byCategory";
    private static final String REGISTERED_STATEMENT = "Jpa40RegistrationBook.rename";
    private static final String AUGMENTED_QUERY = "Jpa40RegistrationBook.augmentByCategory";
    private static final String AUGMENTED_STATEMENT = "Jpa40RegistrationBook.augmentRename";

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".RegistrationBook"};
        return createDeploymentJar("jpa_jpa40_namedqueryregistration.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 runtime named query and statement
     * registration. The test verifies {@code addNamedQuery(String, TypedQuery)}
     * returns a typed query reference usable with {@code createQuery()}, and
     * {@code addNamedStatement()} returns a statement reference usable with
     * {@code createStatement()}.
     */
    @Test
    public void typedNamedQueryAndNamedStatementRegistrationTest() {
        var query = getEntityManager()
                .createQuery("SELECT b FROM Jpa40RegistrationBook b WHERE b.category = :category",
                        RegistrationBook.class);
        var queryReference = getEntityManagerFactory().addNamedQuery(REGISTERED_QUERY, query);

        List<RegistrationBook> books = getEntityManager()
                .createQuery(queryReference)
                .setParameter("category", "fiction")
                .getResultList();
        assertEquals(1, books.size());
        assertEquals("Alpha", books.get(0).getTitle());

        var statement = getEntityManager()
                .createStatement("UPDATE Jpa40RegistrationBook b SET b.title = :title WHERE b.id = :id");
        var statementReference =
                getEntityManagerFactory().addNamedStatement(REGISTERED_STATEMENT, statement);
        assertTrue(getEntityManagerFactory().getNamedStatements().containsKey(REGISTERED_STATEMENT));

        var transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager()
                .createStatement(statementReference)
                .setParameter("title", "Registered")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updated);
        assertEquals("Registered", getEntityManager().find(RegistrationBook.class, 1).getTitle());
    }

    /**
     * Tests {@link CriteriaBuilder#augment(TypedQueryReference, java.util.function.Consumer)}
     * and {@link CriteriaBuilder#augment(StatementReference, java.util.function.Consumer)}
     * for named Jakarta Persistence Query Language references.
     */
    @Test
    public void criteriaBuilderAugmentNamedQueryAndStatementTest() {
        var query = getEntityManager()
                .createQuery("SELECT b FROM Jpa40RegistrationBook b ORDER BY b.id",
                        RegistrationBook.class);
        var queryReference = getEntityManagerFactory().addNamedQuery(AUGMENTED_QUERY, query);

        var builder = getEntityManager().getCriteriaBuilder();
        var augmentedQuery =
                builder.augment(queryReference, criteria -> {
                    Root<?> book = criteria.getRoots().iterator().next();
                    criteria.where(builder.equal(book.get("category"), "fiction"));
                });

        List<RegistrationBook> books = getEntityManager()
                .createQuery(augmentedQuery)
                .getResultList();
        assertEquals(1, books.size());
        assertEquals("Alpha", books.get(0).getTitle());

        var statement = getEntityManager()
                .createStatement("UPDATE Jpa40RegistrationBook b SET b.title = :title");
        var statementReference =
                getEntityManagerFactory().addNamedStatement(AUGMENTED_STATEMENT, statement);
        var augmentedStatement =
                builder.augment(statementReference, criteriaStatement -> {
                    var update = (CriteriaUpdate<?>) criteriaStatement;
                    Root<?> book = update.getRoot();
                    update.where(builder.equal(book.get("id"), 2));
                });

        var transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager()
                .createStatement(augmentedStatement)
                .setParameter("title", "Augmented")
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updated);
        assertEquals("Augmented", getEntityManager().find(RegistrationBook.class, 2).getTitle());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new RegistrationBook(1, "Alpha", "fiction"));
        getEntityManager().persist(new RegistrationBook(2, "Beta", "reference"));
        transaction.commit();
        getEntityManager().clear();
    }
}
