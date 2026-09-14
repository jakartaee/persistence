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

package ee.jakarta.tck.persistence.jpa40.entityagent;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jakarta.persistence.LockModeType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests EntityAgent lock modes as required by the EntityAgent, Lock Modes,
 * and Queries and the Lock Mode sections of chapter 3, and EntityHandler's
 * retrieval contracts. Locked results remain detached.
 */
public class Jpa40EntityAgentLockClient extends PMClientBase {

    private static final List<LockModeType> FORCE_INCREMENT_MODES =
            List.of(WRITE, OPTIMISTIC_FORCE_INCREMENT, PESSIMISTIC_FORCE_INCREMENT);
    private static final List<LockModeType> PESSIMISTIC_MODES =
            List.of(PESSIMISTIC_READ, PESSIMISTIC_WRITE, PESSIMISTIC_FORCE_INCREMENT);

    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa40EntityAgentLockClient.class.getPackageName();
        String[] classes = {packageName + ".AgentBook", packageName + ".AgentPublisher"};
        return createDeploymentJar("jpa_jpa40_entityagent_lock.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        getEntityManagerFactory().runInTransaction(em -> {
            em.persist(new AgentBook(1, "Alpha"));
            em.persist(new AgentBook(2, "Beta"));
            em.persist(new AgentPublisher(1, "Publisher"));
        });
    }

    @Test
    public void findLockModesTest() {
        assertLockModes(LockOperation.FIND);
    }

    @Test
    public void getLockModesTest() {
        assertLockModes(LockOperation.GET);
    }

    @Test
    public void findWithGraphLockModesTest() {
        assertLockModes(LockOperation.FIND_GRAPH);
    }

    @Test
    public void getWithGraphLockModesTest() {
        assertLockModes(LockOperation.GET_GRAPH);
    }

    @Test
    public void findMultipleLockModesTest() {
        assertLockModes(LockOperation.FIND_MULTIPLE);
    }

    @Test
    public void getMultipleLockModesTest() {
        assertLockModes(LockOperation.GET_MULTIPLE);
    }

    @Test
    public void findMultipleWithGraphLockModesTest() {
        assertLockModes(LockOperation.FIND_MULTIPLE_GRAPH);
    }

    @Test
    public void getMultipleWithGraphLockModesTest() {
        assertLockModes(LockOperation.GET_MULTIPLE_GRAPH);
    }

    @Test
    public void refreshLockModesTest() {
        assertLockModes(LockOperation.REFRESH);
    }

    @Test
    public void queryResultListLockModesTest() {
        assertLockModes(LockOperation.QUERY_LIST);
    }

    @Test
    public void querySingleResultLockModesTest() {
        assertLockModes(LockOperation.QUERY_SINGLE);
    }

    @Test
    public void querySingleResultOrNullLockModesTest() {
        assertLockModes(LockOperation.QUERY_SINGLE_OR_NULL);
    }

    @Test
    public void queryResultStreamLockModesTest() {
        assertLockModes(LockOperation.QUERY_STREAM);
    }

    @Test
    public void criteriaQueryLockModesTest() {
        assertLockModes(LockOperation.CRITERIA_QUERY);
    }

    @Test
    public void namedQueryLockModesTest() {
        assertLockModes(LockOperation.NAMED_QUERY);
    }

    /** Every non-NONE mode requires a transaction associated with the agent. */
    @Test
    public void lockModesRequireTransactionTest() {
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : LockOperation.values()) {
            for (LockModeType mode : LockModeType.values()) {
                if (mode != NONE) {
                    checks.add(() -> {
                        try (EntityAgent agent = newAgent()) {
                            assertThrows(TransactionRequiredException.class,
                                    () -> operation.read(agent, mode), operation + " " + mode);
                        }
                    });
                }
            }
        }
        assertAll(checks);
    }

    /** Explicit NONE remains usable without a transaction, including refresh. */
    @Test
    public void noneDoesNotRequireTransactionTest() {
        for (LockOperation operation : LockOperation.values()) {
            try (EntityAgent agent = newAgent()) {
                assertBooks(operation.read(agent, NONE), operation);
            }
        }
    }

    /** A resource-local transaction on a different agent does not suffice. */
    @Test
    public void lockRequiresThisAgentsTransactionTest() {
        try (EntityAgent other = newAgent(); EntityAgent agent = newAgent()) {
            EntityTransaction transaction = other.getTransaction();
            transaction.begin();
            try {
                assertThrows(TransactionRequiredException.class,
                        () -> agent.find(AgentBook.class, 1, PESSIMISTIC_WRITE));
                assertThrows(TransactionRequiredException.class,
                        () -> agent.createQuery("SELECT b FROM Jpa40AgentBook b", AgentBook.class)
                                .setLockMode(OPTIMISTIC).getResultList());
            } finally {
                rollback(transaction);
            }
        }
    }

    /** The lock declared by NamedQuery also applies to an EntityAgent query. */
    @Test
    public void namedQueryDeclaredLockModeTest() {
        try (EntityAgent agent = newAgent()) {
            var query = agent.createNamedQuery(AgentBook.LOCKED_QUERY, AgentBook.class);
            assertEquals(PESSIMISTIC_WRITE, query.getLockMode());
            assertThrows(TransactionRequiredException.class, query::getResultList);
            EntityTransaction transaction = agent.getTransaction();
            transaction.begin();
            try {
                assertBooks(query.getResultList(), LockOperation.NAMED_QUERY);
                transaction.commit();
            } finally {
                rollback(transaction);
            }
        }
    }

    /** Setting the query's lock mode does not itself require a transaction. */
    @Test
    public void configureQueryLockBeforeTransactionTest() {
        for (LockModeType mode : LockModeType.values()) {
            try (EntityAgent agent = newAgent()) {
                var query = agent.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class);
                assertSame(query, query.setLockMode(mode));
                EntityTransaction transaction = agent.getTransaction();
                transaction.begin();
                try {
                    assertBooks(query.getResultList(), LockOperation.QUERY_LIST);
                    transaction.commit();
                } finally {
                    rollback(transaction);
                }
            }
        }
    }

    /** Pessimistic read and write locks must also work without a version. */
    @Test
    public void pessimisticLocksOnNonVersionedEntityTest() {
        for (LockModeType mode : List.of(PESSIMISTIC_READ, PESSIMISTIC_WRITE)) {
            try (EntityAgent agent = newAgent()) {
                EntityTransaction transaction = agent.getTransaction();
                transaction.begin();
                try {
                    AgentPublisher publisher = agent.find(AgentPublisher.class, 1, mode);
                    assertNotNull(publisher);
                    agent.refresh(publisher, mode);
                    assertEquals("Publisher", publisher.getName());
                    assertEquals(1, agent.createQuery("SELECT p FROM Jpa40AgentPublisher p", AgentPublisher.class)
                            .setLockMode(mode).getResultList().size());
                    transaction.commit();
                } finally {
                    rollback(transaction);
                }
            }
        }
    }

    /** A forced version increment must be undone when its transaction rolls back. */
    @Test
    public void forceIncrementRollbackTest() {
        for (LockOperation operation : LockOperation.values()) {
            for (LockModeType mode : FORCE_INCREMENT_MODES) {
                List<AgentBook> before = readBooks();
                try (EntityAgent agent = newAgent()) {
                    EntityTransaction transaction = agent.getTransaction();
                    transaction.begin();
                    try {
                        operation.read(agent, mode);
                    } finally {
                        rollback(transaction);
                    }
                }
                List<AgentBook> after = readBooks();
                for (int i = 0; i < before.size(); i++) {
                    assertEquals(before.get(i).getVersion(), after.get(i).getVersion(),
                            operation + " " + mode + " must roll back the version increment");
                }
            }
        }
    }

    /** READ and OPTIMISTIC must prevent A5B write skew between two agents. */
    @Test
    public void optimisticReadWriteSkewTest() {
        assertConflicts(List.of(READ, OPTIMISTIC), false);
    }

    /** Forced optimistic locks must also prevent A5B write skew between two agents. */
    @Test
    public void optimisticForceIncrementWriteSkewTest() {
        assertConflicts(List.of(WRITE, OPTIMISTIC_FORCE_INCREMENT), false);
    }

    @Test
    public void pessimisticLocksRetainedUntilCommitTest() throws Exception {
        assertConflicts(PESSIMISTIC_MODES, true);
    }

    private void assertLockModes(LockOperation operation) {
        List<Executable> checks = new ArrayList<>();
        for (LockModeType mode : LockModeType.values()) {
            checks.add(() -> assertLockMode(operation, mode));
        }
        assertAll(operation.toString(), checks);
    }

    private void assertLockMode(LockOperation operation, LockModeType mode) {
        List<AgentBook> before = readBooks();
        List<AgentBook> locked;
        try (EntityAgent agent = newAgent()) {
            EntityTransaction transaction = agent.getTransaction();
            transaction.begin();
            try {
                locked = operation.read(agent, mode);
                assertBooks(locked, operation);
                for (AgentBook book : locked) {
                    book.setTitle("Changed only in memory " + book.getId());
                }
                transaction.commit();
            } finally {
                rollback(transaction);
            }
        }
        List<AgentBook> after = readBooks();
        assertBooks(after, LockOperation.QUERY_LIST);
        for (AgentBook book : locked) {
            int index = book.getId() - 1;
            if (FORCE_INCREMENT_MODES.contains(mode)) {
                assertTrue(after.get(index).getVersion() > before.get(index).getVersion(),
                        operation + " " + mode + " must increment version of book " + book.getId());
            }
            // Other modes may be implemented with a stronger lock, including a forced increment.
        }
    }

    private void assertConflicts(List<LockModeType> modes, boolean pessimistic) {
        assertUncontendedUpdates();
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : LockOperation.values()) {
            for (LockModeType mode : modes) {
                // Check both returned entities for operations that return more than one.
                for (int id : operation.ids) {
                    checks.add(() -> {
                        if (pessimistic) {
                            assertPessimisticLockRetained(operation, mode, id);
                        } else {
                            assertWriteSkewPrevented(operation, mode, id);
                        }
                    });
                }
            }
        }
        assertAll(checks);
    }

    /**
     * Attempt r1[x0], r2[y0], w2[x1], c2, w1[y1], c1. Each transaction
     * changes one title only if the other still has its original value, so
     * either serial order preserves x = x0 or y = y0. Check this invariant
     * after both commits instead of rejecting a read followed by a write.
     * Immediate locks may serialize the transactions or cause a lock conflict.
     */
    private void assertWriteSkewPrevented(LockOperation operation, LockModeType mode, int id) throws Exception {
        int otherId = id == 1 ? 2 : 1;
        List<AgentBook> before = readBooks();
        String originalTitle = before.get(id - 1).getTitle();
        String otherOriginalTitle = before.get(otherId - 1).getTitle();
        String context = operation + " " + mode + " book " + id;
        var executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "entity-agent-write-skew");
            thread.setDaemon(true);
            return thread;
        });
        Future<Boolean> writer = null;
        boolean committed = false;
        boolean writerCommitted = false;
        try (EntityAgent reader = newAgent()) {
            EntityTransaction transaction = reader.getTransaction();
            transaction.begin();
            try {
                List<AgentBook> locked = operation.read(reader, mode);
                AgentBook observed = locked.stream().filter(book -> book.getId() == id).findFirst().orElseThrow();
                boolean changeOther = originalTitle.equals(observed.getTitle());
                // Read after requesting the lock, which may already have incremented this book's version.
                AgentBook other = reader.get(AgentBook.class, otherId);
                CountDownLatch started = new CountDownLatch(1);
                writer = executor.submit(() -> updateForWriteSkew(id, "Writer " + context,
                        otherId, otherOriginalTitle, started));
                assertTrue(started.await(10, TimeUnit.SECONDS), context + ": writer did not start");
                try {
                    writer.get(1, TimeUnit.SECONDS);
                } catch (TimeoutException expected) {
                    // Completing this transaction lets a writer blocked by an immediate lock proceed.
                }
                try {
                    if (changeOther) {
                        other.setTitle("Reader " + context);
                        reader.update(other);
                    }
                    transaction.commit();
                    committed = true;
                } catch (PersistenceException expected) {
                    // Contention may abort an update or commit without a specific lock exception.
                }
            } finally {
                rollback(transaction);
                if (writer != null) {
                    writerCommitted = writer.get(30, TimeUnit.SECONDS);
                }
            }
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS), context + ": writer did not terminate");
        }
        if (committed && writerCommitted) {
            List<AgentBook> after = readBooks();
            assertTrue(originalTitle.equals(after.get(id - 1).getTitle())
                            || otherOriginalTitle.equals(after.get(otherId - 1).getTitle()),
                    context + ": both transactions committed A5B write skew, changing both original titles");
        }
    }

    private boolean updateForWriteSkew(int id, String title, int otherId,
                                      String otherOriginalTitle, CountDownLatch started) {
        try (EntityAgent writer = newAgent()) {
            EntityTransaction transaction = writer.getTransaction();
            transaction.begin();
            try {
                started.countDown();
                updateIfUnchanged(writer, id, title, otherId, otherOriginalTitle);
                transaction.commit();
                return true;
            } catch (PersistenceException expected) {
                // The finally block rolls back any transaction still active after this failed attempt.
                return false;
            } finally {
                rollback(transaction);
            }
        }
    }

    /**
     * A pessimistic lock must exclude a concurrent writer until commit.
     * No assertion depends on support for a lock timeout hint.
     */
    private void assertPessimisticLockRetained(LockOperation operation, LockModeType mode, int id) throws Exception {
        String context = operation + " " + mode + " book " + id;
        var executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "entity-agent-lock-writer");
            thread.setDaemon(true);
            return thread;
        });
        Future<Boolean> writer = null;
        try (EntityAgent reader = newAgent()) {
            EntityTransaction transaction = reader.getTransaction();
            transaction.begin();
            try {
                operation.read(reader, mode);
                CountDownLatch started = new CountDownLatch(1);
                writer = executor.submit(() -> updateConcurrently(id, "Concurrent " + context, started));
                assertTrue(started.await(10, TimeUnit.SECONDS), context + ": writer did not start");
                boolean committedBeforeReader = false;
                try {
                    committedBeforeReader = writer.get(1, TimeUnit.SECONDS);
                } catch (TimeoutException expected) {
                    // Release any immediate lock by completing the reader transaction below.
                }
                assertFalse(committedBeforeReader, context + ": writer committed while the lock was held");
                transaction.commit();
            } finally {
                rollback(transaction);
                if (writer != null) {
                    writer.get(30, TimeUnit.SECONDS);
                }
            }
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS), context + ": writer did not terminate");
        }
    }

    private boolean updateConcurrently(int id, String title, CountDownLatch started) {
        try (EntityAgent writer = newAgent()) {
            EntityTransaction transaction = writer.getTransaction();
            transaction.begin();
            try {
                started.countDown();
                updateBook(writer, id, title);
                transaction.commit();
                return true;
            } catch (PersistenceException expected) {
                // A plain update need not report a specific lock exception when it is blocked.
                return false;
            } finally {
                rollback(transaction);
            }
        }
    }

    /** A broken update must fail the control instead of making a contention test pass. */
    private void assertUncontendedUpdates() {
        for (int id : List.of(1, 2)) {
            for (boolean conditional : List.of(false, true)) {
                int otherId = id == 1 ? 2 : 1;
                String otherTitle = readBooks().get(otherId - 1).getTitle();
                String title = "Uncontended " + id + " conditional=" + conditional;
                try (EntityAgent agent = newAgent()) {
                    EntityTransaction transaction = agent.getTransaction();
                    transaction.begin();
                    try {
                        if (conditional) {
                            updateIfUnchanged(agent, id, title, otherId, otherTitle);
                        } else {
                            updateBook(agent, id, title);
                        }
                        transaction.commit();
                    } finally {
                        rollback(transaction);
                    }
                }
                assertEquals(title, readBooks().get(id - 1).getTitle(),
                        "Update must succeed without contention");
            }
        }
    }

    private static void updateIfUnchanged(EntityAgent agent, int id, String title,
                                          int otherId, String otherOriginalTitle) {
        AgentBook other = agent.find(AgentBook.class, otherId, OPTIMISTIC);
        if (otherOriginalTitle.equals(other.getTitle())) {
            updateBook(agent, id, title);
        }
    }

    private static void updateBook(EntityAgent agent, int id, String title) {
        AgentBook book = agent.get(AgentBook.class, id);
        book.setTitle(title);
        agent.update(book);
    }

    private EntityAgent newAgent() {
        EntityAgent agent = getEntityManagerFactory().createEntityAgent();
        agent.setCacheRetrieveMode(CacheRetrieveMode.BYPASS);
        agent.setCacheStoreMode(CacheStoreMode.BYPASS);
        return agent;
    }

    private List<AgentBook> readBooks() {
        try (EntityAgent agent = newAgent()) {
            return agent.getMultiple(AgentBook.class, List.of(1, 2));
        }
    }

    private static void rollback(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

    private static void assertBooks(List<AgentBook> books, LockOperation operation) {
        assertEquals(operation.ids, books.stream().map(AgentBook::getId).toList(), operation.toString());
        for (AgentBook book : books) {
            assertEquals(book.getId() == 1 ? "Alpha" : "Beta", book.getTitle(), operation.toString());
        }
    }

    private enum LockOperation {
        FIND(false), GET(false), FIND_GRAPH(false), GET_GRAPH(false),
        FIND_MULTIPLE(true), GET_MULTIPLE(true), FIND_MULTIPLE_GRAPH(true), GET_MULTIPLE_GRAPH(true),
        REFRESH(false), QUERY_LIST(true), QUERY_SINGLE(false), QUERY_SINGLE_OR_NULL(false),
        QUERY_STREAM(true), CRITERIA_QUERY(true), NAMED_QUERY(true);

        private final List<Integer> ids;

        LockOperation(boolean multiple) {
            ids = multiple ? List.of(1, 2) : List.of(1);
        }

        List<AgentBook> read(EntityAgent agent, LockModeType mode) {
            return switch (this) {
                case FIND -> List.of(agent.find(AgentBook.class, 1, mode));
                case GET -> List.of(agent.get(AgentBook.class, 1, mode));
                case FIND_GRAPH -> List.of(agent.find(agent.createEntityGraph(AgentBook.class), 1, mode));
                case GET_GRAPH -> List.of(agent.get(agent.createEntityGraph(AgentBook.class), 1, mode));
                case FIND_MULTIPLE -> agent.findMultiple(AgentBook.class, ids, mode);
                case GET_MULTIPLE -> agent.getMultiple(AgentBook.class, ids, mode);
                case FIND_MULTIPLE_GRAPH -> agent.findMultiple(agent.createEntityGraph(AgentBook.class), ids, mode);
                case GET_MULTIPLE_GRAPH -> agent.getMultiple(agent.createEntityGraph(AgentBook.class), ids, mode);
                case REFRESH -> {
                    AgentBook book = agent.get(AgentBook.class, 1);
                    book.setTitle("Discarded by refresh");
                    agent.refresh(book, mode);
                    yield List.of(book);
                }
                case QUERY_LIST -> agent.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class)
                        .setLockMode(mode).getResultList();
                case QUERY_SINGLE -> List.of(agent.createQuery("SELECT b FROM Jpa40AgentBook b WHERE b.id = 1", AgentBook.class)
                        .setLockMode(mode).getSingleResult());
                case QUERY_SINGLE_OR_NULL -> List.of(agent.createQuery("SELECT b FROM Jpa40AgentBook b WHERE b.id = 1", AgentBook.class)
                        .setLockMode(mode).getSingleResultOrNull());
                case QUERY_STREAM -> {
                    try (var stream = agent.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class)
                            .setLockMode(mode).getResultStream()) {
                        yield stream.toList();
                    }
                }
                case CRITERIA_QUERY -> {
                    var builder = agent.getCriteriaBuilder();
                    var query = builder.createQuery(AgentBook.class);
                    var book = query.from(AgentBook.class);
                    query.select(book).orderBy(builder.asc(book.get("id")));
                    yield agent.createQuery(query).setLockMode(mode).getResultList();
                }
                case NAMED_QUERY -> agent.createNamedQuery(AgentBook.LOCKED_QUERY, AgentBook.class)
                        .setLockMode(mode).getResultList();
            };
        }
    }
}
