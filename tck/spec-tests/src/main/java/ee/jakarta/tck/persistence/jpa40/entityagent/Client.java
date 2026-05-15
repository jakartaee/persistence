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
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".AgentBook",
                packageName + ".AgentPublisher"
        };
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

    /**
     * Verifies the bulk EntityAgent operations perform the corresponding
     * database changes for every entity in the supplied list.
     */
    @Test
    public void entityAgentBulkOperationsTest() {
        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent ->
                agent.insertMultiple(List.of(
                        new AgentBook(1, "Alpha"),
                        new AgentBook(2, "Beta"))));

        assertEquals(2L, countBooks());
        assertEquals(List.of("Alpha", "Beta"), titlesById(1, 2));

        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            List<AgentBook> books = agent.getMultiple(AgentBook.class, List.of(1, 2));
            books.get(0).setTitle("Alpha updated");
            books.get(1).setTitle("Beta updated");
            agent.updateMultiple(books);
        });

        assertEquals(2L, countBooks());
        assertEquals(List.of("Alpha updated", "Beta updated"), titlesById(1, 2));

        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            AgentBook existing = agent.get(AgentBook.class, 2);
            existing.setTitle("Beta upserted");
            agent.upsertMultiple(List.of(
                    existing,
                    new AgentBook(3, "Gamma upserted")));
        });

        assertEquals(3L, countBooks());
        assertEquals(List.of("Alpha updated", "Beta upserted", "Gamma upserted"), titlesById(1, 2, 3));

        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent ->
                agent.deleteMultiple(agent.getMultiple(AgentBook.class, List.of(1, 3))));

        assertEquals(1L, countBooks());
        assertNull(findBook(1));
        assertEquals("Beta upserted", titleById(2));
        assertNull(findBook(3));
    }

    /**
     * Verifies instances returned by an EntityAgent are detached, are not
     * reused as an identity map, and may be refreshed from the database.
     */
    @Test
    public void entityAgentDetachedAndRefreshOperationsTest() {
        createBooks(
                new AgentBook(1, "Alpha"),
                new AgentBook(2, "Beta"));

        try (EntityAgent agent = getEntityManagerFactory().createEntityAgent()) {
            AgentBook first = agent.get(AgentBook.class, 1);
            AgentBook second = agent.get(AgentBook.class, 1);
            assertNotSame(first, second);

            first.setTitle("changed only in memory");
            assertEquals("Alpha", titleById(1));

            updateTitle(1, "Alpha refreshed");
            agent.refresh(first);
            assertEquals("Alpha refreshed", first.getTitle());

            updateTitle(1, "Alpha lock refreshed");
            agent.refresh(first, LockModeType.NONE);
            assertEquals("Alpha lock refreshed", first.getTitle());

            AgentBook beta = agent.get(AgentBook.class, 2);
            first.setTitle("stale Alpha");
            beta.setTitle("stale Beta");
            updateTitle(1, "Alpha refreshed multiple");
            updateTitle(2, "Beta refreshed multiple");

            agent.refreshMultiple(List.of(first, beta));

            assertEquals("Alpha refreshed multiple", first.getTitle());
            assertEquals("Beta refreshed multiple", beta.getTitle());
        }
    }

    /**
     * Verifies refresh operations report the specified failure when the target
     * database record no longer exists.
     */
    @Test
    public void entityAgentRefreshFailureExceptionTypesTest() {
        createBooks(
                new AgentBook(1, "Alpha"),
                new AgentBook(2, "Beta"),
                new AgentBook(3, "Gamma"));

        try (var agent = getEntityManagerFactory().createEntityAgent()) {
            AgentBook first = agent.get(AgentBook.class, 1);
            deleteBook(1);
            assertThrows(EntityNotFoundException.class, () -> agent.refresh(first));

            AgentBook beta = agent.get(AgentBook.class, 2);
            AgentBook gamma = agent.get(AgentBook.class, 3);
            deleteBook(3);
            assertThrows(EntityNotFoundException.class, () -> agent.refreshMultiple(List.of(beta, gamma)));
        }
    }

    /**
     * Verifies EntityAgent.fetch() initializes a lazy association belonging to
     * a detached entity returned by the agent.
     */
    @Test
    public void entityAgentFetchLazyAssociationTest() {
        createPublisherTestData();

        try (EntityAgent agent = getEntityManagerFactory().createEntityAgent()) {
            AgentPublisher publisher = agent.get(AgentPublisher.class, 10);
            List<AgentBook> books = publisher.getBooks();

            assertSame(books, agent.fetch(books));
            assertEquals(List.of("Alpha", "Beta"), books.stream()
                    .map(AgentBook::getTitle)
                    .toList());
        }
    }

    /**
     * Verifies EntityAgent options can be supplied at creation time and changed
     * through the type-safe option APIs.
     */
    @Test
    public void entityAgentOptionsTest() {
        try (EntityAgent agent = getEntityManagerFactory().createEntityAgent(
                CacheRetrieveMode.BYPASS,
                CacheStoreMode.BYPASS)) {
            assertEquals(CacheRetrieveMode.BYPASS, agent.getCacheRetrieveMode());
            assertEquals(CacheStoreMode.BYPASS, agent.getCacheStoreMode());
            assertTrue(agent.getOptions().contains(CacheRetrieveMode.BYPASS));
            assertTrue(agent.getOptions().contains(CacheStoreMode.BYPASS));

            Set<EntityAgent.Option> options = agent.getOptions();
            options.clear();
            assertTrue(agent.getOptions().contains(CacheRetrieveMode.BYPASS));
            assertTrue(agent.getOptions().contains(CacheStoreMode.BYPASS));

            agent.addOption(CacheRetrieveMode.USE);
            assertEquals(CacheRetrieveMode.USE, agent.getCacheRetrieveMode());
            assertTrue(agent.getOptions().contains(CacheRetrieveMode.USE));

            agent.setCacheStoreMode(CacheStoreMode.REFRESH);
            assertEquals(CacheStoreMode.REFRESH, agent.getCacheStoreMode());
            assertTrue(agent.getOptions().contains(CacheStoreMode.REFRESH));
        }

        try (EntityAgent agent = getEntityManagerFactory().createEntityAgent(Map.of(
                "jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS,
                "jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS))) {
            assertEquals(CacheRetrieveMode.BYPASS, agent.getCacheRetrieveMode());
            assertEquals(CacheStoreMode.BYPASS, agent.getCacheStoreMode());
        }
    }

    /**
     * Verifies rollback leaves no database changes, and that EntityAgent
     * reports the specified failures for invalid or conflicting operations.
     */
    @Test
    public void entityAgentFailureAndRollbackTest() {
        createBooks(new AgentBook(1, "initial"));

        EntityAgent agent = getEntityManagerFactory().createEntityAgent();
        EntityTransaction transaction = agent.getTransaction();
        try {
            transaction.begin();
            agent.insert(new AgentBook(2, "rolled back"));
            transaction.rollback();
            assertNull(findBook(2));

            transaction.begin();
            assertThrows(EntityExistsException.class, () -> agent.insert(new AgentBook(1, "duplicate")));
            rollbackIfActive(transaction);

            AgentBook stale = agent.get(AgentBook.class, 1);
            updateTitle(1, "updated elsewhere");
            stale.setTitle("stale update");
            transaction.begin();
            assertThrows(OptimisticLockException.class, () -> agent.update(stale));
            rollbackIfActive(transaction);
            assertEquals("updated elsewhere", titleById(1));

            transaction.begin();
            assertThrows(OptimisticLockException.class, () -> agent.update(new AgentBook(99, "missing update")));
            rollbackIfActive(transaction);

            transaction.begin();
            assertThrows(OptimisticLockException.class, () -> agent.delete(new AgentBook(99, "missing delete")));
            rollbackIfActive(transaction);

            transaction.begin();
            assertThrows(IllegalArgumentException.class, () -> agent.upsert(new AgentBook(null, "missing id")));
            rollbackIfActive(transaction);
        } finally {
            rollbackIfActive(transaction);
            agent.close();
        }

        assertEquals(1L, countBooks());
        assertEquals("updated elsewhere", titleById(1));
    }

    /**
     * Verifies bulk write operations and stale upsert operations report the
     * specified exception types for invalid, missing, or stale records.
     */
    @Test
    public void entityAgentBulkFailureExceptionTypesTest() {
        createBooks(
                new AgentBook(1, "initial"),
                new AgentBook(2, "second"));

        var agent = getEntityManagerFactory().createEntityAgent();
        var transaction = agent.getTransaction();
        try {
            transaction.begin();
            assertThrows(PersistenceException.class,
                    () -> agent.insertMultiple(List.of(new AgentBook(1, "duplicate bulk"))));
            rollbackIfActive(transaction);

            transaction.begin();
            assertThrows(OptimisticLockException.class,
                    () -> agent.updateMultiple(List.of(new AgentBook(99, "missing update"))));
            rollbackIfActive(transaction);

            AgentBook staleUpdate = findBook(1);
            updateTitle(1, "updated before bulk update");
            staleUpdate.setTitle("stale bulk update");
            transaction.begin();
            assertThrows(OptimisticLockException.class,
                    () -> agent.updateMultiple(List.of(staleUpdate)));
            rollbackIfActive(transaction);
            assertEquals("updated before bulk update", titleById(1));

            transaction.begin();
            assertThrows(OptimisticLockException.class,
                    () -> agent.deleteMultiple(List.of(new AgentBook(99, "missing delete"))));
            rollbackIfActive(transaction);

            AgentBook staleDelete = findBook(1);
            updateTitle(1, "updated before bulk delete");
            transaction.begin();
            assertThrows(OptimisticLockException.class,
                    () -> agent.deleteMultiple(List.of(staleDelete)));
            rollbackIfActive(transaction);
            assertEquals("updated before bulk delete", titleById(1));

            transaction.begin();
            assertThrows(IllegalArgumentException.class,
                    () -> agent.upsertMultiple(List.of(new AgentBook(null, "missing id"))));
            rollbackIfActive(transaction);

            AgentBook staleUpsert = findBook(1);
            updateTitle(1, "updated before upsert");
            staleUpsert.setTitle("stale upsert");
            transaction.begin();
            assertThrows(OptimisticLockException.class, () -> agent.upsert(staleUpsert));
            rollbackIfActive(transaction);
            assertEquals("updated before upsert", titleById(1));

            AgentBook staleBulkUpsert = findBook(1);
            updateTitle(1, "updated before bulk upsert");
            staleBulkUpsert.setTitle("stale bulk upsert");
            transaction.begin();
            assertThrows(OptimisticLockException.class,
                    () -> agent.upsertMultiple(List.of(staleBulkUpsert)));
            rollbackIfActive(transaction);
            assertEquals("updated before bulk upsert", titleById(1));
        } finally {
            rollbackIfActive(transaction);
            agent.close();
        }

        assertEquals(2L, countBooks());
        assertEquals(List.of("updated before bulk upsert", "second"), titlesById(1, 2));
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

    private void createBooks(AgentBook... books) {
        getEntityManagerFactory().runInTransaction(entityManager -> {
            for (AgentBook book : books) {
                entityManager.persist(book);
            }
        });
    }

    private void createPublisherTestData() {
        getEntityManagerFactory().runInTransaction(entityManager -> {
            AgentPublisher publisher = new AgentPublisher(10, "Agent Publisher");
            entityManager.persist(publisher);
            entityManager.persist(new AgentBook(1, "Alpha", publisher));
            entityManager.persist(new AgentBook(2, "Beta", publisher));
        });
    }

    private AgentBook findBook(int id) {
        return getEntityManagerFactory().callInTransaction(entityManager ->
                entityManager.find(AgentBook.class, id));
    }

    private String titleById(int id) {
        AgentBook book = findBook(id);
        return book == null ? null : book.getTitle();
    }

    private List<String> titlesById(Integer... ids) {
        return getEntityManagerFactory().callInTransaction(entityManager -> entityManager
                .createQuery("SELECT b.title FROM Jpa40AgentBook b WHERE b.id IN :ids ORDER BY b.id", String.class)
                .setParameter("ids", List.of(ids))
                .getResultList());
    }

    private void updateTitle(int id, String title) {
        getEntityManagerFactory().runInTransaction(entityManager ->
                entityManager.find(AgentBook.class, id).setTitle(title));
    }

    private void deleteBook(int id) {
        getEntityManagerFactory().runInTransaction(entityManager -> {
            AgentBook book = entityManager.find(AgentBook.class, id);
            if (book != null) {
                entityManager.remove(book);
            }
        });
    }

    private void rollbackIfActive(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

}
