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

package ee.jakarta.tck.persistence.jpa32.schemamanager;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.SchemaManager;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SchemaManagedEntity"};
        return createDeploymentJar("jpa_jpa32_schemamanager.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
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
     * Tests the Jakarta Persistence 3.2 {@link SchemaManager} contract exposed by
     * {@link jakarta.persistence.EntityManagerFactory#getSchemaManager()}.
     * The test verifies that a provider can validate the mapped schema, truncate
     * all mapped tables, drop the mapped schema objects without script generation,
     * recreate them, and use the recreated schema for persistence operations.
     */
    @Test
    public void schemaManagerCreateValidateDropAndTruncateTest() throws Exception {
        SchemaManager schemaManager = getEntityManagerFactory().getSchemaManager();
        assertNotNull(schemaManager);

        schemaManager.validate();
        persistEntity(1, "before-truncate");
        assertEquals(1L, countEntities());

        schemaManager.truncate();
        getEntityManager().clear();
        assertEquals(0L, countEntities());

        schemaManager.drop(false);
        schemaManager.create(false);
        schemaManager.validate();

        persistEntity(2, "after-create");
        assertEquals(1L, countEntities());
    }

    private void persistEntity(Integer id, String name) {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SchemaManagedEntity(id, name));
        transaction.commit();
        getEntityManager().clear();
    }

    private long countEntities() {
        return getEntityManager()
                .createQuery("SELECT COUNT(e) FROM Jpa32SchemaManagedEntity e", Long.class)
                .getSingleResult();
    }
}
