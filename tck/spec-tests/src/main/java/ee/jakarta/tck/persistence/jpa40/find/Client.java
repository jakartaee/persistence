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
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".AccessBook"};
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

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new AccessBook(1, "Alpha"));
        getEntityManager().persist(new AccessBook(2, "Beta"));
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
