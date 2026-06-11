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

package ee.jakarta.tck.persistence.jpa40.resultcount;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".CountBook"};
        return createDeploymentJar("jpa_jpa40_resultcount.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 {@link TypedQuery#getResultCount()}. The
     * test verifies that a typed query can return the number of matching rows
     * independently of the current pagination settings.
     */
    @Test
    public void typedQueryGetResultCountTest() {
        TypedQuery<CountBook> query = getEntityManager()
                .createQuery("SELECT b FROM Jpa40CountBook b WHERE b.category = :category ORDER BY b.id",
                        CountBook.class)
                .setParameter("category", "fiction")
                .setMaxResults(1);

        assertEquals(2L, query.getResultCount());
        assertEquals(1, query.getResultList().size());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new CountBook(1, "Alpha", "fiction"));
        getEntityManager().persist(new CountBook(2, "Beta", "fiction"));
        getEntityManager().persist(new CountBook(3, "Gamma", "tech"));
        transaction.commit();
        getEntityManager().clear();
    }
}
