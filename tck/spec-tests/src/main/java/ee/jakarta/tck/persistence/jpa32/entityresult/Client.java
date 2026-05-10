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

package ee.jakarta.tck.persistence.jpa32.entityresult;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityResult;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.SqlResultSetMapping;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".EntityResultBook"};
        return createDeploymentJar("jpa_jpa32_entityresult.jar", packageName, classes);
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
     * Tests the Jakarta Persistence 3.2 {@link EntityResult#lockMode()}
     * annotation element. The test verifies that the mapping exposes the
     * configured optimistic lock mode and that a native query using the
     * {@link SqlResultSetMapping} can materialize the mapped entity.
     */
    @Test
    public void entityResultLockModeTest() {
        SqlResultSetMapping mapping = EntityResultBook.class.getAnnotation(SqlResultSetMapping.class);
        EntityResult entityResult = mapping.entities()[0];
        assertEquals(LockModeType.OPTIMISTIC, entityResult.lockMode());

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        EntityResultBook book = getEntityManager()
                .createNamedQuery(EntityResultBook.QUERY_WITH_LOCK_MODE, EntityResultBook.class)
                .getSingleResult();
        assertEquals("locked", book.getTitle());
        transaction.commit();
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new EntityResultBook(1, "locked"));
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
