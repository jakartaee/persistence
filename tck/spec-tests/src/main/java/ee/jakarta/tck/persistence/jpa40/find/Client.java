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

package ee.jakarta.tck.persistence.jpa40.find;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import jakarta.persistence.TransactionRequiredException;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".AccessBook", packageName + ".AccessPublisher"};
        return createDeploymentJar("jpa_jpa40_find.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 entity retrieval additions. The test
     * verifies {@code get()} throws when no row exists and returns an entity
     * otherwise, and verifies ordered retrieval using {@code findMultiple()} and
     * {@code getMultiple()}.
     */
    @Test
    public void managerGetFindMultipleAndGetMultipleTest() {
        assertEquals("Alpha", getEntityManager().get(AccessBook.class, 1).getTitle());
        assertThrows(EntityNotFoundException.class,
                () -> getEntityManager().get(AccessBook.class, 99));
        assertNull(getEntityManager().find(AccessBook.class, 99));

        List<AccessBook> found = getEntityManager()
                .findMultiple(AccessBook.class, List.of(1, 2));
        assertEquals(List.of(1, 2), found.stream().map(AccessBook::getId).toList());

        List<AccessBook> got = getEntityManager()
                .getMultiple(AccessBook.class, List.of(2, 1));
        assertEquals(List.of(2, 1), got.stream().map(AccessBook::getId).toList());
    }

    /**
     * Tests Jakarta Persistence 4.0 entity retrieval additions. The test
     * verifies {@code get()} throws when no row exists and returns an entity
     * otherwise, and verifies ordered retrieval using {@code findMultiple()} and
     * {@code getMultiple()}.
     */
    @Test
    public void agentGetFindMultipleAndGetMultipleTest() {
        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            assertEquals("Alpha", agent.get(AccessBook.class, 1).getTitle());
            assertThrows(EntityNotFoundException.class, () -> agent.get(AccessBook.class, 99));
            assertNull(agent.find(AccessBook.class, 99));

            List<AccessBook> found = agent.findMultiple(AccessBook.class, List.of(1, 2));
            assertEquals(List.of(1, 2), found.stream().map(AccessBook::getId).toList());

            List<AccessBook> got = agent.getMultiple(AccessBook.class, List.of(2, 1));
            assertEquals(List.of(2, 1), got.stream().map(AccessBook::getId).toList());
        });
    }

    /**
     * Tests Jakarta Persistence 4.0 find/get overloads accepting
     * {@code FindOption} values. The test verifies cache and lock options are
     * accepted, and that {@code findMultiple()} preserves null result slots
     * while {@code getMultiple()} throws for a missing record.
     */
    @Test
    public void managerFindAndGetOptionOverloadsTest() {
        AccessBook found = getEntityManager().find(AccessBook.class, 1,
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertEquals("Alpha", found.getTitle());

        AccessBook got = getEntityManager().get(AccessBook.class, 2,
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertEquals("Beta", got.getTitle());

        List<AccessBook> foundMultiple = getEntityManager().findMultiple(AccessBook.class,
                List.of(1, 99, 2),
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertOrderedFindMultipleResults(foundMultiple);

        assertThrows(EntityNotFoundException.class,
                () -> getEntityManager().getMultiple(AccessBook.class, List.of(1, 99, 2),
                        CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
    }

    /**
     * Tests Jakarta Persistence 4.0 {@link EntityAgent} find/get overloads
     * accepting {@code FindOption} values. The test verifies cache and lock
     * options are accepted, and that {@code findMultiple()} preserves null
     * result slots while {@code getMultiple()} throws for a missing record.
     */
    @Test
    public void agentFindAndGetOptionOverloadsTest() {
        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            AccessBook found = agent.find(AccessBook.class, 1,
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertEquals("Alpha", found.getTitle());

            AccessBook got = agent.get(AccessBook.class, 2,
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertEquals("Beta", got.getTitle());

            List<AccessBook> foundMultiple = agent.findMultiple(AccessBook.class, List.of(1, 99, 2),
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertOrderedFindMultipleResults(foundMultiple);

            assertThrows(EntityNotFoundException.class,
                    () -> agent.getMultiple(AccessBook.class, List.of(1, 99, 2),
                            CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
        });
    }

    /**
     * Tests Jakarta Persistence 4.0 entity graph overloads for find/get. The
     * test verifies the graph is applied to single and multiple retrieval, and
     * verifies missing-record behavior for both {@code find()} and {@code get()}.
     */
    @Test
    public void managerGraphFindAndGetOverloadsTest() {
        EntityGraph<AccessBook> graph = getEntityManager().createEntityGraph(AccessBook.class);
        graph.addAttributeNode("publisher");

        AccessBook found = getEntityManager().find(graph, 1,
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertEquals("Alpha", found.getTitle());
        assertPublisherLoaded(found);

        getEntityManager().clear();

        AccessBook got = getEntityManager().get(graph, 2,
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertEquals("Beta", got.getTitle());
        assertPublisherLoaded(got);

        getEntityManager().clear();

        List<AccessBook> foundMultiple = getEntityManager().findMultiple(graph, List.of(1, 99, 2),
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
        assertOrderedFindMultipleResults(foundMultiple);
        assertPublisherLoaded(foundMultiple.get(0));
        assertPublisherLoaded(foundMultiple.get(2));

        assertNull(getEntityManager().find(graph, 99,
                CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
        assertThrows(EntityNotFoundException.class,
                () -> getEntityManager().get(graph, 99,
                        CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
        assertThrows(EntityNotFoundException.class,
                () -> getEntityManager().getMultiple(graph, List.of(1, 99, 2),
                        CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
    }

    /**
     * Tests Jakarta Persistence 4.0 {@link EntityAgent} graph overloads for
     * find/get. The test verifies graph fetching with detached results from the
     * agent and the same missing-record semantics as {@code EntityManager}.
     */
    @Test
    public void agentGraphFindAndGetOverloadsTest() {
        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            EntityGraph<AccessBook> graph = agent.createEntityGraph(AccessBook.class);
            graph.addAttributeNode("publisher");

            AccessBook found = agent.find(graph, 1,
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertEquals("Alpha", found.getTitle());
            assertPublisherLoaded(found);

            AccessBook got = agent.get(graph, 2,
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertEquals("Beta", got.getTitle());
            assertPublisherLoaded(got);

            List<AccessBook> foundMultiple = agent.findMultiple(graph, List.of(1, 99, 2),
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE);
            assertOrderedFindMultipleResults(foundMultiple);
            assertPublisherLoaded(foundMultiple.get(0));
            assertPublisherLoaded(foundMultiple.get(2));

            assertNull(agent.find(graph, 99,
                    CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
            assertThrows(EntityNotFoundException.class,
                    () -> agent.get(graph, 99,
                            CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
            assertThrows(EntityNotFoundException.class,
                    () -> agent.getMultiple(graph, List.of(1, 99, 2),
                            CacheRetrieveMode.BYPASS, CacheStoreMode.BYPASS, LockModeType.NONE));
        });
    }

    /**
     * Verifies EntityAgent find/get overloads reject lock modes other than
     * NONE when no transaction is active.
     */
    @Test
    public void agentFindAndGetLockModeRequiresTransactionTest() {
        try (var agent = getEntityManagerFactory().createEntityAgent()) {
            assertFalse(agent.getTransaction().isActive());
            EntityGraph<AccessBook> graph = agent.createEntityGraph(AccessBook.class);

            assertAll(
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.find(AccessBook.class, 1, LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.get(AccessBook.class, 1, LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.findMultiple(AccessBook.class, List.of(1, 2), LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.getMultiple(AccessBook.class, List.of(1, 2), LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.find(graph, 1, LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.get(graph, 1, LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.findMultiple(graph, List.of(1, 2), LockModeType.PESSIMISTIC_READ)),
                    () -> assertThrows(TransactionRequiredException.class,
                            () -> agent.getMultiple(graph, List.of(1, 2), LockModeType.PESSIMISTIC_READ)));
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 find/get validation. The test verifies
     * invalid entity classes and invalid identifiers fail for class and graph
     * overloads, including multiple retrieval lists.
     */
    @Test
    public void findAndGetInvalidArgumentsTest() {
        EntityGraph<AccessBook> graph = getEntityManager().createEntityGraph(AccessBook.class);

        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().find(String.class, "not-an-entity"));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().find(AccessBook.class, null));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().get(AccessBook.class, "wrong-id-type"));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().findMultiple(AccessBook.class, Arrays.asList(1, null)));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().getMultiple(AccessBook.class, List.of(1, "wrong-id-type")));

        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().find(graph, null));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().get(graph, "wrong-id-type"));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().findMultiple(graph, Arrays.asList(1, null)));
        assertThrows(IllegalArgumentException.class,
                () -> getEntityManager().getMultiple(graph, List.of(1, "wrong-id-type")));

        getEntityManagerFactory().runInTransaction(EntityAgent.class, agent -> {
            EntityGraph<AccessBook> agentGraph = agent.createEntityGraph(AccessBook.class);

            assertThrows(IllegalArgumentException.class,
                    () -> agent.find(String.class, "not-an-entity"));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.find(AccessBook.class, null));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.get(AccessBook.class, "wrong-id-type"));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.findMultiple(AccessBook.class, Arrays.asList(1, null)));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.getMultiple(AccessBook.class, List.of(1, "wrong-id-type")));

            assertThrows(IllegalArgumentException.class,
                    () -> agent.find(agentGraph, null));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.get(agentGraph, "wrong-id-type"));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.findMultiple(agentGraph, Arrays.asList(1, null)));
            assertThrows(IllegalArgumentException.class,
                    () -> agent.getMultiple(agentGraph, List.of(1, "wrong-id-type")));
        });
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        AccessPublisher publisher = new AccessPublisher(1, "Publisher");
        getEntityManager().persist(publisher);
        getEntityManager().persist(new AccessBook(1, "Alpha", publisher));
        getEntityManager().persist(new AccessBook(2, "Beta", publisher));
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

    private void assertOrderedFindMultipleResults(List<AccessBook> books) {
        assertEquals(3, books.size());
        assertEquals(1, books.get(0).getId());
        assertEquals("Alpha", books.get(0).getTitle());
        assertNull(books.get(1));
        assertEquals(2, books.get(2).getId());
        assertEquals("Beta", books.get(2).getTitle());
    }

    private void assertPublisherLoaded(AccessBook book) {
        assertNotNull(book);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(book, "publisher"));
        AccessPublisher publisher = book.getPublisher();
        assertNotNull(publisher);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(publisher));
        assertEquals("Publisher", publisher.getName());
    }
}
