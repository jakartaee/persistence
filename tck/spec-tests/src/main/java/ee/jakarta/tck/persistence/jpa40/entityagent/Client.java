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

package ee.jakarta.tck.persistence.jpa40.entityagent;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".AgentBook"};
        return createDeploymentJar("jpa_jpa40_entityagent.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
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
     * Tests the Jakarta Persistence 4.0 {@link EntityAgent} API. The test
     * verifies creation of an application-managed agent, direct insert, get,
     * update, upsert, and delete operations.
     */
    @Test
    public void entityAgentDirectOperationsTest() {
        EntityAgent agent = getEntityManagerFactory().createEntityAgent();
        EntityTransaction transaction = agent.getTransaction();
        try {
            assertEquals(0L, countBooks());

            transaction.begin();
            agent.insert(new AgentBook(1, "inserted"));
            transaction.commit();

            assertEquals(1L, countBooks());

            AgentBook inserted = agent.get(AgentBook.class, 1);
            assertEquals("inserted", inserted.getTitle());

            transaction.begin();
            inserted.setTitle("updated");
            agent.update(inserted);
            transaction.commit();

            assertEquals(1L, countBooks());
            assertEquals("updated", agent.get(AgentBook.class, 1).getTitle());

            transaction.begin();
            agent.upsert(new AgentBook(2, "upserted new"));
            transaction.commit();

            transaction.begin();
            inserted.setTitle("upserted old");
            agent.upsert(inserted);
            transaction.commit();

            assertEquals(2L, countBooks());
            assertEquals("upserted old", agent.get(AgentBook.class, 1).getTitle());
            assertEquals("upserted new", agent.get(AgentBook.class, 2).getTitle());

            transaction.begin();
            agent.delete(inserted);
            transaction.commit();

            assertEquals(1L, countBooks());
            assertNull(agent.find(AgentBook.class, 1));
            assertNotNull(agent.find(AgentBook.class, 2));
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            agent.close();
        }
        assertFalse(agent.isOpen());
    }

    private void removeTestData() {
        EntityTransaction transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        getEntityManagerFactory().getSchemaManager().truncate();
        getEntityManager().clear();
    }

    private long countBooks() {
        return getEntityManagerFactory().callInTransaction(entityManager -> entityManager
                .createQuery("SELECT COUNT(b) FROM Jpa40AgentBook b", Long.class)
                .getSingleResult());
    }

}
