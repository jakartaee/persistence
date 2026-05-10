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

package ee.jakarta.tck.persistence.jpa40.versioning;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".VersionedBook"};
        return createDeploymentJar("jpa_jpa40_versioning.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 {@code @ExcludedFromVersioning}. The test
     * verifies that changing only an excluded attribute does not increment the
     * version while changing a regular persistent attribute still does.
     */
    @Test
    public void excludedFromVersioningTest() {
        VersionedBook book = getEntityManager().find(VersionedBook.class, 1);
        int initialVersion = book.getVersion();

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        book.setAuditNote("audit-only change");
        transaction.commit();
        getEntityManager().clear();

        VersionedBook unchangedVersion = getEntityManager().find(VersionedBook.class, 1);
        assertEquals(initialVersion, unchangedVersion.getVersion());

        transaction = getEntityTransaction();
        transaction.begin();
        unchangedVersion.setTitle("content change");
        transaction.commit();
        getEntityManager().clear();

        VersionedBook changedVersion = getEntityManager().find(VersionedBook.class, 1);
        assertTrue(changedVersion.getVersion() > initialVersion);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new VersionedBook(1, "Alpha", "initial"));
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
