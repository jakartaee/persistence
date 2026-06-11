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

package ee.jakarta.tck.persistence.jpa40.queryflush;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.persistence.QueryFlushMode;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".FlushBook"};
        return createDeploymentJar("jpa_jpa40_queryflush.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
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
     * Tests Jakarta Persistence 4.0 named query flush modes. The test verifies
     * the provider honors {@code flush = NO_FLUSH} by not synchronizing a
     * pending change before query execution and honors {@code flush = FLUSH} by
     * synchronizing a pending change before query execution.
     */
    @Test
    public void namedQueryFlushModeExecutionTest() {
        getEntityManager().setFlushMode(FlushModeType.EXPLICIT);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().find(FlushBook.class, 1).setTitle("NoFlush");
        Long unflushed = getEntityManager()
                .createNamedQuery(FlushBook.NO_FLUSH_QUERY, Long.class)
                .getSingleResult();
        transaction.rollback();
        getEntityManager().clear();

        assertEquals(0L, unflushed);

        transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().find(FlushBook.class, 1).setTitle("Flush");
        Long flushed = getEntityManager()
                .createNamedQuery(FlushBook.FLUSH_QUERY, Long.class)
                .getSingleResult();
        transaction.rollback();
        getEntityManager().clear();

        assertEquals(1L, flushed);
    }

    /**
     * Tests Jakarta Persistence 4.0 named native query flush modes. The test
     * verifies a native named query with {@code flush = FLUSH} synchronizes a
     * pending managed-entity change before the native SQL query is executed.
     */
    @Test
    public void namedNativeQueryFlushModeExecutionTest() {
        getEntityManager().setFlushMode(FlushModeType.EXPLICIT);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().find(FlushBook.class, 1).setTitle("NativeFlush");
        Long flushed = getEntityManager()
                .createNamedQuery(FlushBook.NATIVE_FLUSH_QUERY, Long.class)
                .getSingleResult();
        transaction.rollback();
        getEntityManager().clear();

        assertEquals(1L, flushed);
    }

    /**
     * Tests Jakarta Persistence 4.0 runtime query flush modes. The test
     * verifies {@link Query#setQueryFlushMode(QueryFlushMode)} affects whether
     * pending managed-entity changes are synchronized before query execution
     * when the entity manager itself uses {@link FlushModeType#EXPLICIT}.
     */
    @Test
    public void queryFlushModeApiTest() {
        getEntityManager().setFlushMode(FlushModeType.EXPLICIT);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().find(FlushBook.class, 1).setTitle("QueryNoFlush");
        Long unflushed = getEntityManager()
                .createQuery("SELECT COUNT(b) FROM Jpa40FlushBook b WHERE b.title = 'QueryNoFlush'", Long.class)
                .setQueryFlushMode(QueryFlushMode.NO_FLUSH)
                .getSingleResult();
        transaction.rollback();
        getEntityManager().clear();

        assertEquals(0L, unflushed);

        transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().find(FlushBook.class, 1).setTitle("QueryFlush");
        Long flushed = getEntityManager()
                .createQuery("SELECT COUNT(b) FROM Jpa40FlushBook b WHERE b.title = 'QueryFlush'", Long.class)
                .setQueryFlushMode(QueryFlushMode.FLUSH)
                .getSingleResult();
        transaction.rollback();
        getEntityManager().clear();

        assertEquals(1L, flushed);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new FlushBook(1, "Initial"));
        transaction.commit();
        getEntityManager().clear();
    }
}
