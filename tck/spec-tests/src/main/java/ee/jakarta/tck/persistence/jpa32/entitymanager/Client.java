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

package ee.jakarta.tck.persistence.jpa32.entitymanager;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.Timeout;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".EntityManagerBook"};
        return createDeploymentJar("jpa_jpa32_entitymanager.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 timeout option support for entity manager
     * locking. The test verifies the {@code EntityManager.lock(entity, mode,
     * Timeout)} overload accepts a {@link Timeout} option.
     */
    @Test
    public void lockOptionOverloadTest() {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        EntityManagerBook book = em.find(EntityManagerBook.class, 1);
        em.lock(book, LockModeType.NONE, Timeout.seconds(1));
        transaction.commit();
    }

    /**
     * Tests Jakarta Persistence 3.2 cache mode convenience accessors. The test
     * verifies setting and reading cache retrieve/store modes directly on
     * {@link EntityManager} and {@link TypedQuery}.
     */
    @Test
    public void entityManagerAndQueryCacheModesTest() {
        EntityManager em = getEntityManager();
        em.setCacheRetrieveMode(CacheRetrieveMode.BYPASS);
        em.setCacheStoreMode(CacheStoreMode.BYPASS);
        assertEquals(CacheRetrieveMode.BYPASS, em.getCacheRetrieveMode());
        assertEquals(CacheStoreMode.BYPASS, em.getCacheStoreMode());

        TypedQuery<EntityManagerBook> typedQuery =
                em.createQuery("SELECT b FROM Jpa32EntityManagerBook b ORDER BY b.id", EntityManagerBook.class);
        typedQuery.setCacheRetrieveMode(CacheRetrieveMode.BYPASS);
        typedQuery.setCacheStoreMode(CacheStoreMode.USE);
        assertEquals(CacheRetrieveMode.BYPASS, typedQuery.getCacheRetrieveMode());
        assertEquals(CacheStoreMode.USE, typedQuery.getCacheStoreMode());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new EntityManagerBook(1, "Alpha"));
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
