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

package ee.jakarta.tck.persistence.jpa32.version;

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
        String[] classes = {
                packageName + ".LocalDateTimeVersionEntity",
                packageName + ".InstantVersionEntity"
        };
        return createDeploymentJar("jpa_jpa32_version.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 version attribute type additions. The test
     * verifies that {@code LocalDateTime} and {@code Instant} version attributes
     * are accepted and populated for versioned entities.
     */
    @Test
    public void localDateTimeAndInstantVersionTypesTest() {
        LocalDateTimeVersionEntity localDateTimeVersionEntity =
                getEntityManager().find(LocalDateTimeVersionEntity.class, 1);
        assertNotNull(localDateTimeVersionEntity);
        assertNotNull(localDateTimeVersionEntity.getVersion());
        assertEquals(VersionTestData.FIXED_INSTANT, localDateTimeVersionEntity.getUpdatedAt());

        InstantVersionEntity instantVersionEntity =
                getEntityManager().find(InstantVersionEntity.class, 1);
        assertNotNull(instantVersionEntity);
        assertNotNull(instantVersionEntity.getVersion());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new LocalDateTimeVersionEntity(1, "local-date-time",
                VersionTestData.FIXED_INSTANT));
        getEntityManager().persist(new InstantVersionEntity(1, "instant"));
        transaction.commit();
        getEntityManager().clear();
    }
}
