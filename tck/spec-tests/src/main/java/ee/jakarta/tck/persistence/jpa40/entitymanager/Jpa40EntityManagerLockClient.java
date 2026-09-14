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

package ee.jakarta.tck.persistence.jpa40.entitymanager;

import ee.jakarta.tck.persistence.common.PMClientBase;
import ee.jakarta.tck.persistence.jpa40.entityagent.AgentBook;
import ee.jakarta.tck.persistence.jpa40.entityagent.AgentPublisher;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jakarta.persistence.LockModeType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Lock Modes and Queries and the Lock Mode sections of chapter 3,
 * including EntityHandler retrieval options and locks on entities already
 * in the persistence context. Uses the same entities as the EntityAgent tests.
 */
public class Jpa40EntityManagerLockClient extends PMClientBase {

    private static final List<LockModeType> FORCE_INCREMENT_MODES =
            List.of(WRITE, OPTIMISTIC_FORCE_INCREMENT, PESSIMISTIC_FORCE_INCREMENT);
    private static final List<LockModeType> PESSIMISTIC_MODES =
            List.of(PESSIMISTIC_READ, PESSIMISTIC_WRITE, PESSIMISTIC_FORCE_INCREMENT);

    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa40EntityManagerLockClient.class.getPackageName();
        String[] classes = {AgentBook.class.getName(), AgentPublisher.class.getName()};
        return createDeploymentJar("jpa_jpa40_entitymanager_lock.jar", packageName, classes);
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
    public void findWithPropertiesLockModesTest() {
        assertLockModes(LockOperation.FIND_PROPERTIES);
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
    public void refreshWithPropertiesLockModesTest() {
        assertLockModes(LockOperation.REFRESH_PROPERTIES);
    }

    @Test
    public void lockModesTest() {
        assertLockModes(LockOperation.LOCK);
    }

    @Test
    public void lockWithPropertiesLockModesTest() {
        assertLockModes(LockOperation.LOCK_PROPERTIES);
    }

    @Test
    public void lockWithOptionsLockModesTest() {
        assertLockModes(LockOperation.LOCK_OPTIONS);
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

    /** Every non-NONE mode requires a transaction associated with the manager. */
    @Test
    public void lockModesRequireTransactionTest() {
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : LockOperation.values()) {
            for (LockModeType mode : LockModeType.values()) {
                if (mode != NONE) {
                    checks.add(() -> {
                        try (EntityManager manager = newManager()) {
                            assertThrows(TransactionRequiredException.class,
                                    () -> operation.read(manager, mode), operation + " " + mode);
                        }
                    });
                }
            }
        }
        assertAll(checks);
    }

    /** Retrieval and refresh with NONE work on an application-managed manager without a transaction. */
    @Test
    public void noneDoesNotRequireTransactionTest() {
        for (LockOperation operation : LockOperation.values()) {
            if (operation.isLock()) {
                continue;
            }
            try (EntityManager manager = newManager()) {
                assertBooks(operation.read(manager, NONE), operation);
            }
        }
    }

    /** Unlike retrieval with NONE, an explicit lock call always requires a transaction. */
    @Test
    public void lockNoneRequiresTransactionTest() {
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : List.of(LockOperation.LOCK,
                LockOperation.LOCK_PROPERTIES, LockOperation.LOCK_OPTIONS)) {
            checks.add(() -> {
                try (EntityManager manager = newManager()) {
                    assertThrows(TransactionRequiredException.class,
                            () -> operation.read(manager, NONE), operation.toString());
                }
            });
        }
        assertAll(checks);
    }

    /** Lock and refresh must reject detached instances even with an active transaction. */
    @Test
    public void lockAndRefreshRequireManagedEntityTest() {
        List<Executable> checks = new ArrayList<>();
        for (LockModeType mode : LockModeType.values()) {
            checks.add(() -> {
                AgentBook detached = readBooks().get(0);
                try (EntityManager manager = newManager()) {
                    EntityTransaction transaction = manager.getTransaction();
                    transaction.begin();
                    try {
                        assertFalse(manager.contains(detached));
                        assertAll(mode.toString(),
                                () -> assertThrows(IllegalArgumentException.class, () -> manager.lock(detached, mode)),
                                () -> assertThrows(IllegalArgumentException.class, () -> manager.lock(detached, mode, Map.of())),
                                () -> assertThrows(IllegalArgumentException.class,
                                        () -> manager.lock(detached, mode, PessimisticLockScope.NORMAL)),
                                () -> assertThrows(IllegalArgumentException.class, () -> manager.refresh(detached, mode)),
                                () -> assertThrows(IllegalArgumentException.class, () -> manager.refresh(detached, mode, Map.of())));
                    } finally {
                        rollback(transaction);
                    }
                }
            });
        }
        assertAll(checks);
    }

    /** A resource-local transaction on a different manager does not suffice. */
    @Test
    public void lockRequiresThisManagersTransactionTest() {
        try (EntityManager other = newManager(); EntityManager manager = newManager()) {
            EntityTransaction transaction = other.getTransaction();
            transaction.begin();
            try {
                assertThrows(TransactionRequiredException.class,
                        () -> manager.find(AgentBook.class, 1, PESSIMISTIC_WRITE));
                assertThrows(TransactionRequiredException.class,
                        () -> manager.createQuery("SELECT b FROM Jpa40AgentBook b", AgentBook.class)
                                .setLockMode(OPTIMISTIC).getResultList());
            } finally {
                rollback(transaction);
            }
        }
    }

    /** The lock declared by NamedQuery also applies to an EntityManager query. */
    @Test
    public void namedQueryDeclaredLockModeTest() {
        try (EntityManager manager = newManager()) {
            var query = manager.createNamedQuery(AgentBook.LOCKED_QUERY, AgentBook.class);
            assertEquals(PESSIMISTIC_WRITE, query.getLockMode());
            assertThrows(TransactionRequiredException.class, query::getResultList);
            EntityTransaction transaction = manager.getTransaction();
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
            try (EntityManager manager = newManager()) {
                var query = manager.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class);
                assertSame(query, query.setLockMode(mode));
                EntityTransaction transaction = manager.getTransaction();
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
            try (EntityManager manager = newManager()) {
                EntityTransaction transaction = manager.getTransaction();
                transaction.begin();
                try {
                    AgentPublisher publisher = manager.find(AgentPublisher.class, 1, mode);
                    assertNotNull(publisher);
                    manager.refresh(publisher, mode);
                    assertEquals("Publisher", publisher.getName());
                    assertEquals(1, manager.createQuery("SELECT p FROM Jpa40AgentPublisher p", AgentPublisher.class)
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
                try (EntityManager manager = newManager()) {
                    EntityTransaction transaction = manager.getTransaction();
                    transaction.begin();
                    try {
                        operation.read(manager, mode);
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

    /** A lock must preserve normal dirty checking and version synchronization. */
    @Test
    public void lockedEntityRemainsManagedTest() {
        for (LockModeType mode : LockModeType.values()) {
            int before = readBooks().get(0).getVersion();
            String title = "Managed update " + mode;
            try (EntityManager manager = newManager()) {
                EntityTransaction transaction = manager.getTransaction();
                transaction.begin();
                try {
                    AgentBook book = manager.find(AgentBook.class, 1, mode);
                    assertTrue(manager.contains(book));
                    book.setTitle(title);
                    transaction.commit();
                    assertTrue(manager.contains(book));
                } finally {
                    rollback(transaction);
                }
            }
            AgentBook after = readBooks().get(0);
            assertEquals(title, after.getTitle());
            assertTrue(after.getVersion() > before, mode + " must version a managed update");
        }
    }

    /**
     * A pessimistic lock requested for an already-managed entity must check
     * its version. Load before starting the transaction so no database read
     * lock can prevent the other transaction from making the entity stale.
     */
    @Test
    public void pessimisticLockOfStaleManagedEntityTest() {
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : List.of(LockOperation.FIND, LockOperation.FIND_PROPERTIES,
                LockOperation.LOCK, LockOperation.LOCK_PROPERTIES, LockOperation.LOCK_OPTIONS)) {
            for (LockModeType mode : PESSIMISTIC_MODES) {
                checks.add(() -> {
                    String context = operation + " " + mode;
                    try (EntityManager manager = newManager()) {
                        AgentBook stale = manager.find(AgentBook.class, 1);
                        int staleVersion = stale.getVersion();
                        getEntityManagerFactory().runInTransaction(other ->
                                other.find(AgentBook.class, 1).setTitle("Stale " + context));
                        assertTrue(readBooks().get(0).getVersion() > staleVersion, context);
                        assertTrue(manager.contains(stale));
                        EntityTransaction transaction = manager.getTransaction();
                        transaction.begin();
                        try {
                            RuntimeException failure = assertThrows(RuntimeException.class, () -> {
                                operation.read(manager, mode);
                                manager.flush();
                                transaction.commit();
                            }, context + ": stale managed version was accepted");
                            assertTrue(hasCause(failure, OptimisticLockException.class),
                                    context + ": expected an optimistic lock failure, got " + failure);
                            if (transaction.isActive()) {
                                assertTrue(transaction.getRollbackOnly(), context + ": transaction must be rollback-only");
                            }
                        } finally {
                            rollback(transaction);
                        }
                    }
                });
            }
        }
        assertAll(checks);
    }

    @Test
    public void optimisticReadConflictTest() throws Exception {
        assertConflicts(List.of(READ, OPTIMISTIC), false, false, false);
    }

    @Test
    public void optimisticForceIncrementConflictTest() throws Exception {
        assertConflicts(List.of(WRITE, OPTIMISTIC_FORCE_INCREMENT), false, false, false);
    }

    @Test
    public void pessimisticLocksRetainedUntilCommitTest() throws Exception {
        assertConflicts(PESSIMISTIC_MODES, true, false, false);
    }

    @Test
    public void pessimisticLocksRetainedUntilRollbackTest() throws Exception {
        assertConflicts(List.of(PESSIMISTIC_WRITE), true, false, true);
    }

    /**
     * Prevent A5B write skew while locking entities already in the persistence
     * context. Each transaction changes one book's title only if the other
     * book still has its original title. At least one original title must
     * therefore remain after both transactions commit.
     */
    @Test
    public void optimisticLockOfAlreadyManagedEntityWriteSkewTest() {
        assertUncontendedUpdates();
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : LockOperation.values()) {
            for (int id : operation.ids) {
                checks.add(() -> assertWriteSkewPrevented(operation, id));
            }
        }
        assertAll(checks);
    }

    @Test
    public void pessimisticLockOfAlreadyManagedEntityRetainedTest() throws Exception {
        assertConflicts(List.of(PESSIMISTIC_WRITE), true, true, false);
    }

    private void assertLockModes(LockOperation operation) {
        List<Executable> checks = new ArrayList<>();
        for (LockModeType mode : LockModeType.values()) {
            checks.add(() -> assertLockMode(operation, mode, false));
            checks.add(() -> assertLockMode(operation, mode, true));
        }
        assertAll(operation.toString(), checks);
    }

    private void assertLockMode(LockOperation operation, LockModeType mode, boolean preload) {
        List<AgentBook> before = readBooks();
        List<AgentBook> locked;
        try (EntityManager manager = newManager()) {
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            try {
                List<AgentBook> managed = preload
                        ? List.of(manager.find(AgentBook.class, 1), manager.find(AgentBook.class, 2))
                        : List.of();
                locked = operation.read(manager, mode);
                if (preload) {
                    for (AgentBook book : locked) {
                        assertSame(managed.get(book.getId() - 1), book, operation + " " + mode);
                    }
                }
                assertBooks(locked, operation);
                for (AgentBook book : locked) {
                    assertTrue(manager.contains(book), operation + " must return a managed entity");
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
                        operation + " " + mode + " preload=" + preload
                                + " must increment version of book " + book.getId());
                assertEquals(after.get(index).getVersion(), book.getVersion(),
                        operation + " " + mode + " must synchronize the managed version");
            }
            // Other modes may be implemented with a stronger lock, including a forced increment.
        }
    }

    private void assertConflicts(List<LockModeType> modes, boolean pessimistic,
                                 boolean preload, boolean rollBackReader) throws Exception {
        assertUncontendedUpdates();
        List<Executable> checks = new ArrayList<>();
        for (LockOperation operation : LockOperation.values()) {
            for (LockModeType mode : modes) {
                // Check both returned entities for operations that return more than one.
                for (int id : operation.ids) {
                    checks.add(() -> assertConflict(operation, mode, id, pessimistic, preload, rollBackReader));
                }
            }
        }
        assertAll(checks);
    }

    /**
     * Attempt r1[x0], r2[y0], w2[x1], c2, w1[y1], c1. Both commits would
     * violate x = x0 or y = y0, which either serial order preserves.
     * Immediate locks may instead serialize the transactions or cause a
     * lock conflict. The observation timeout does not decide the outcome:
     * check the committed database state after both transactions finish.
     */
    private void assertWriteSkewPrevented(LockOperation operation, int id) throws Exception {
        int otherId = id == 1 ? 2 : 1;
        List<AgentBook> before = readBooks();
        String originalTitle = before.get(id - 1).getTitle();
        String otherOriginalTitle = before.get(otherId - 1).getTitle();
        String context = operation + " OPTIMISTIC book " + id;
        var executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "entity-manager-write-skew");
            thread.setDaemon(true);
            return thread;
        });
        Future<Boolean> writer = null;
        boolean committed = false;
        boolean writerCommitted = false;
        try (EntityManager reader = newManager()) {
            EntityTransaction transaction = reader.getTransaction();
            transaction.begin();
            try {
                List<AgentBook> managed = List.of(reader.find(AgentBook.class, 1), reader.find(AgentBook.class, 2));
                List<AgentBook> locked = operation.read(reader, OPTIMISTIC);
                AgentBook observed = locked.stream().filter(book -> book.getId() == id).findFirst().orElseThrow();
                assertSame(managed.get(id - 1), observed, context);
                boolean changeOther = originalTitle.equals(observed.getTitle());
                CountDownLatch started = new CountDownLatch(1);
                writer = executor.submit(() -> updateForWriteSkew(id, "Writer " + context,
                        otherId, otherOriginalTitle, started));
                assertTrue(started.await(10, TimeUnit.SECONDS), context + ": writer did not start");
                try {
                    writer.get(1, TimeUnit.SECONDS);
                } catch (TimeoutException expected) {
                    // Allow a writer blocked by an immediate lock to proceed when this transaction ends.
                }
                if (changeOther) {
                    managed.get(otherId - 1).setTitle("Reader " + context);
                }
                try {
                    transaction.commit();
                    committed = true;
                } catch (PersistenceException expected) {
                    // Contention may abort the commit without a specific lock exception.
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
        try (EntityManager writer = newManager()) {
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
     * Give a concurrent writer an opportunity to finish while the reader's
     * transaction remains open. An optimistic implementation may instead
     * acquire an immediate database lock, so blocking or a lock conflict is
     * also valid. No assertion depends on support for a lock timeout hint.
     */
    private void assertConflict(LockOperation operation, LockModeType mode, int id,
                                boolean pessimistic, boolean preload, boolean rollBackReader) throws Exception {
        String context = operation + " " + mode + " book " + id + " preload=" + preload + " rollback=" + rollBackReader;
        var executor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "entity-manager-lock-writer");
            thread.setDaemon(true);
            return thread;
        });
        Future<Boolean> writer = null;
        try (EntityManager reader = newManager()) {
            EntityTransaction transaction = reader.getTransaction();
            transaction.begin();
            try {
                if (preload) {
                    reader.find(AgentBook.class, 1);
                    reader.find(AgentBook.class, 2);
                }
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
                if (pessimistic) {
                    assertFalse(committedBeforeReader, context + ": writer committed while the lock was held");
                    if (rollBackReader) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } else if (committedBeforeReader) {
                    assertThrows(PersistenceException.class, transaction::commit,
                            context + ": optimistic validation accepted a conflicting committed update");
                } else {
                    try {
                        transaction.commit();
                    } catch (PersistenceException expected) {
                        // The writer may have committed after the observation interval, or
                        // completing optimistic validation may itself encounter a lock conflict.
                    }
                }
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
        try (EntityManager writer = newManager()) {
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
                try (EntityManager manager = newManager()) {
                    EntityTransaction transaction = manager.getTransaction();
                    transaction.begin();
                    try {
                        if (conditional) {
                            updateIfUnchanged(manager, id, title, otherId, otherTitle);
                        } else {
                            updateBook(manager, id, title);
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

    private static void updateIfUnchanged(EntityManager manager, int id, String title,
                                          int otherId, String otherOriginalTitle) {
        AgentBook other = manager.find(AgentBook.class, otherId, OPTIMISTIC);
        if (otherOriginalTitle.equals(other.getTitle())) {
            updateBook(manager, id, title);
        }
    }

    private static void updateBook(EntityManager manager, int id, String title) {
        AgentBook book = manager.get(AgentBook.class, id);
        book.setTitle(title);
    }

    private static boolean hasCause(Throwable failure, Class<? extends Throwable> type) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (type.isInstance(cause)) {
                return true;
            }
        }
        return false;
    }

    private EntityManager newManager() {
        EntityManager manager = getEntityManagerFactory().createEntityManager();
        manager.setCacheRetrieveMode(CacheRetrieveMode.BYPASS);
        manager.setCacheStoreMode(CacheStoreMode.BYPASS);
        return manager;
    }

    private List<AgentBook> readBooks() {
        try (EntityManager manager = newManager()) {
            return manager.getMultiple(AgentBook.class, List.of(1, 2));
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
        FIND(false), FIND_PROPERTIES(false), GET(false), FIND_GRAPH(false), GET_GRAPH(false),
        FIND_MULTIPLE(true), GET_MULTIPLE(true), FIND_MULTIPLE_GRAPH(true), GET_MULTIPLE_GRAPH(true),
        LOCK(false), LOCK_PROPERTIES(false), LOCK_OPTIONS(false),
        REFRESH(false), REFRESH_PROPERTIES(false), QUERY_LIST(true), QUERY_SINGLE(false), QUERY_SINGLE_OR_NULL(false),
        QUERY_STREAM(true), CRITERIA_QUERY(true), NAMED_QUERY(true);

        private final List<Integer> ids;

        LockOperation(boolean multiple) {
            ids = multiple ? List.of(1, 2) : List.of(1);
        }

        boolean isLock() {
            return this == LOCK || this == LOCK_PROPERTIES || this == LOCK_OPTIONS;
        }

        List<AgentBook> read(EntityManager manager, LockModeType mode) {
            return switch (this) {
                case FIND -> List.of(manager.find(AgentBook.class, 1, mode));
                case FIND_PROPERTIES -> List.of(manager.find(AgentBook.class, 1, mode, Map.of()));
                case GET -> List.of(manager.get(AgentBook.class, 1, mode));
                case FIND_GRAPH -> List.of(manager.find(manager.createEntityGraph(AgentBook.class), 1, mode));
                case GET_GRAPH -> List.of(manager.get(manager.createEntityGraph(AgentBook.class), 1, mode));
                case FIND_MULTIPLE -> manager.findMultiple(AgentBook.class, ids, mode);
                case GET_MULTIPLE -> manager.getMultiple(AgentBook.class, ids, mode);
                case FIND_MULTIPLE_GRAPH -> manager.findMultiple(manager.createEntityGraph(AgentBook.class), ids, mode);
                case GET_MULTIPLE_GRAPH -> manager.getMultiple(manager.createEntityGraph(AgentBook.class), ids, mode);
                case LOCK, LOCK_PROPERTIES, LOCK_OPTIONS -> {
                    AgentBook book = manager.get(AgentBook.class, 1);
                    switch (this) {
                        case LOCK -> manager.lock(book, mode);
                        case LOCK_PROPERTIES -> manager.lock(book, mode, Map.of());
                        case LOCK_OPTIONS -> manager.lock(book, mode, PessimisticLockScope.NORMAL);
                        default -> throw new AssertionError(this);
                    }
                    yield List.of(book);
                }
                case REFRESH, REFRESH_PROPERTIES -> {
                    AgentBook book = manager.get(AgentBook.class, 1);
                    book.setTitle("Discarded by refresh");
                    if (this == REFRESH_PROPERTIES) {
                        manager.refresh(book, mode, Map.of());
                    } else {
                        manager.refresh(book, mode);
                    }
                    yield List.of(book);
                }
                case QUERY_LIST -> manager.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class)
                        .setLockMode(mode).getResultList();
                case QUERY_SINGLE -> List.of(manager.createQuery("SELECT b FROM Jpa40AgentBook b WHERE b.id = 1", AgentBook.class)
                        .setLockMode(mode).getSingleResult());
                case QUERY_SINGLE_OR_NULL -> List.of(manager.createQuery("SELECT b FROM Jpa40AgentBook b WHERE b.id = 1", AgentBook.class)
                        .setLockMode(mode).getSingleResultOrNull());
                case QUERY_STREAM -> {
                    try (var stream = manager.createQuery("SELECT b FROM Jpa40AgentBook b ORDER BY b.id", AgentBook.class)
                            .setLockMode(mode).getResultStream()) {
                        yield stream.toList();
                    }
                }
                case CRITERIA_QUERY -> {
                    var builder = manager.getCriteriaBuilder();
                    var query = builder.createQuery(AgentBook.class);
                    var book = query.from(AgentBook.class);
                    query.select(book).orderBy(builder.asc(book.get("id")));
                    yield manager.createQuery(query).setLockMode(mode).getResultList();
                }
                case NAMED_QUERY -> manager.createNamedQuery(AgentBook.LOCKED_QUERY, AgentBook.class)
                        .setLockMode(mode).getResultList();
            };
        }
    }
}
