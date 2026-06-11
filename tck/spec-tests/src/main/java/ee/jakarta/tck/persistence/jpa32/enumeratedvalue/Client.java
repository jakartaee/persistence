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

package ee.jakarta.tck.persistence.jpa32.enumeratedvalue;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".EnumeratedValueBook", packageName + ".BookStatus"};
        return createDeploymentJar("jpa_jpa32_enumeratedvalue.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 {@code @EnumeratedValue} support. The test
     * verifies that an enum value is persisted using the enum field annotated as
     * the database value and that the entity attribute is restored as the enum
     * constant when read back.
     */
    @Test
    public void enumeratedValueTest() {
        EnumeratedValueBook book = getEntityManager().find(EnumeratedValueBook.class, 1);
        assertNotNull(book);
        assertEquals(BookStatus.PUBLISHED, book.getStatus());

        String storedStatus = getEntityManager()
                .createNamedQuery(EnumeratedValueBook.QUERY_STATUS_CODE, String.class)
                .getSingleResult();
        assertEquals(BookStatus.PUBLISHED.getCode(), storedStatus);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new EnumeratedValueBook(1, BookStatus.PUBLISHED));
        transaction.commit();
        getEntityManager().clear();
    }
}
