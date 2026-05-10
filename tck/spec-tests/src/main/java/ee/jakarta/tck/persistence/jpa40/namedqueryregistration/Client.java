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
        TypedQuery<RegistrationBook> query = getEntityManager()
                .createQuery("SELECT b FROM Jpa40RegistrationBook b WHERE b.category = :category",
                        RegistrationBook.class);
        TypedQueryReference<RegistrationBook> queryReference =
                getEntityManagerFactory().addNamedQuery(REGISTERED_QUERY, query);

        List<RegistrationBook> books = getEntityManager()
                .createQuery(queryReference)
                .setParameter("category", "fiction")
                .getResultList();
        assertEquals(1, books.size());
        assertEquals("Alpha", books.get(0).getTitle());

        Statement statement = getEntityManager()
                .createStatement("UPDATE Jpa40RegistrationBook b SET b.title = :title WHERE b.id = :id");
        StatementReference statementReference =
                getEntityManagerFactory().addNamedStatement(REGISTERED_STATEMENT, statement);
        assertTrue(getEntityManagerFactory().getNamedStatements().containsKey(REGISTERED_STATEMENT));

        EntityTransaction transaction = getEntityTransaction();
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

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new RegistrationBook(1, "Alpha", "fiction"));
        getEntityManager().persist(new RegistrationBook(2, "Beta", "reference"));
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
