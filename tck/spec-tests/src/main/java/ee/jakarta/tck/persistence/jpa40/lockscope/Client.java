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

package ee.jakarta.tck.persistence.jpa40.lockscope;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PessimisticLockScope;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".LockScopeBook", packageName + ".LockScopePublisher"};
        return createDeploymentJar("jpa_jpa40_lockscope.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 runtime query lock scope. The test verifies
     * {@code setLockScope(FETCHED)} is reflected by the query API and that a
     * pessimistic lock covers the join-fetched associated entity.
     */
    @Test
    public void typedQueryFetchedLockScopeTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        try {
            TypedQuery<LockScopeBook> query = getEntityManager()
                    .createQuery("SELECT b FROM Jpa40LockScopeBook b JOIN FETCH b.publisher WHERE b.id = :id",
                            LockScopeBook.class);

            assertSame(query, query.setLockScope(PessimisticLockScope.FETCHED));
            assertSame(query, query.setLockMode(LockModeType.PESSIMISTIC_WRITE));
            assertEquals(PessimisticLockScope.FETCHED, query.getLockScope());
            assertTrue(query.getOptions().contains(PessimisticLockScope.FETCHED));

            LockScopeBook book = query.setParameter("id", 1).getSingleResult();
            assertPessimisticWriteLocked(book, book.getPublisher());
        } finally {
            transaction.rollback();
            getEntityManager().clear();
        }
    }

    /**
     * Tests Jakarta Persistence 4.0 {@link jakarta.persistence.NamedQuery}
     * {@code lockScope}. The test verifies the named query exposes the declared
     * lock scope and applies it to join-fetched entities.
     */
    @Test
    public void namedQueryFetchedLockScopeTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        try {
            TypedQuery<LockScopeBook> query = getEntityManager()
                    .createNamedQuery(LockScopeBook.FETCHED_LOCK_QUERY, LockScopeBook.class);

            assertEquals(LockModeType.PESSIMISTIC_WRITE, query.getLockMode());
            assertEquals(PessimisticLockScope.FETCHED, query.getLockScope());

            LockScopeBook book = query.setParameter("id", 1).getSingleResult();
            assertPessimisticWriteLocked(book, book.getPublisher());
        } finally {
            transaction.rollback();
            getEntityManager().clear();
        }
    }

    /**
     * Tests that the Jakarta Persistence 4.0 {@code FETCHED} pessimistic lock
     * scope is accepted as a find, refresh, and lock option.
     */
    @Test
    public void fetchedLockScopeEntityManagerOptionTest() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        try {
            LockScopeBook book = getEntityManager().find(LockScopeBook.class, 1,
                    LockModeType.PESSIMISTIC_WRITE, PessimisticLockScope.FETCHED);
            assertEquals(LockModeType.PESSIMISTIC_WRITE, getEntityManager().getLockMode(book));

            getEntityManager().refresh(book, LockModeType.PESSIMISTIC_WRITE, PessimisticLockScope.FETCHED);
            assertEquals(LockModeType.PESSIMISTIC_WRITE, getEntityManager().getLockMode(book));

            getEntityManager().lock(book, LockModeType.PESSIMISTIC_WRITE, PessimisticLockScope.FETCHED);
            assertEquals(LockModeType.PESSIMISTIC_WRITE, getEntityManager().getLockMode(book));
        } finally {
            transaction.rollback();
            getEntityManager().clear();
        }
    }

    private void assertPessimisticWriteLocked(LockScopeBook book, LockScopePublisher publisher) {
        assertEquals(LockModeType.PESSIMISTIC_WRITE, getEntityManager().getLockMode(book));
        assertEquals(LockModeType.PESSIMISTIC_WRITE, getEntityManager().getLockMode(publisher));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        LockScopePublisher publisher = new LockScopePublisher(1, "Acme");
        getEntityManager().persist(publisher);
        getEntityManager().persist(new LockScopeBook(1, "Alpha", publisher));
        transaction.commit();
        getEntityManager().clear();
    }
}
