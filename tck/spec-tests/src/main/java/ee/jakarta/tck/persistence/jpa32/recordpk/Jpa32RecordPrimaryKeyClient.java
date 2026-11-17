/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package ee.jakarta.tck.persistence.jpa32.recordpk;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Jpa32RecordPrimaryKeyClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa32RecordPrimaryKeyClient.class.getPackageName();
        String[] classes = {
                packageName + ".EmbeddedRecordId",
                packageName + ".EmbeddedRecordPkBook",
                packageName + ".IdClassRecordId",
                packageName + ".IdClassRecordPkBook"
        };
        return createDeploymentJar("jpa_jpa32_recordpk.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Verifies Java records are valid primary key classes for both
     * {@code @EmbeddedId} and {@code @IdClass}.
     */
    @Test
    public void recordPrimaryKeyClassesTest() {
        EmbeddedRecordPkBook embedded = getEntityManager()
                .find(EmbeddedRecordPkBook.class, new EmbeddedRecordId(1, "embedded"));
        assertNotNull(embedded);
        assertEquals("Embedded", embedded.getTitle());
        assertEquals(new EmbeddedRecordId(1, "embedded"), embedded.getId());

        IdClassRecordPkBook idClass = getEntityManager()
                .find(IdClassRecordPkBook.class, new IdClassRecordId(2, "idclass"));
        assertNotNull(idClass);
        assertEquals("IdClass", idClass.getTitle());
        assertEquals(2, idClass.getId());
        assertEquals("idclass", idClass.getCode());
    }

    /**
     * Tests querying entities having record primary keys (both @EmbeddedId and @IdClass) via JPQL.
     */
    @Test
    public void recordPrimaryKeyQueryTest() {
        EmbeddedRecordPkBook embedded = getEntityManager()
                .createQuery("SELECT b FROM Jpa32EmbeddedRecordPkBook b WHERE b.id.code = :code",
                        EmbeddedRecordPkBook.class)
                .setParameter("code", "embedded")
                .getSingleResult();
        assertNotNull(embedded);
        assertEquals("Embedded", embedded.getTitle());
        assertEquals(new EmbeddedRecordId(1, "embedded"), embedded.getId());

        IdClassRecordPkBook idClass = getEntityManager()
                .createQuery("SELECT b FROM Jpa32IdClassRecordPkBook b WHERE b.code = :code",
                        IdClassRecordPkBook.class)
                .setParameter("code", "idclass")
                .getSingleResult();
        assertNotNull(idClass);
        assertEquals("IdClass", idClass.getTitle());
        assertEquals(2, idClass.getId());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new EmbeddedRecordPkBook(
                new EmbeddedRecordId(1, "embedded"), "Embedded"));
        getEntityManager().persist(new IdClassRecordPkBook(2, "idclass", "IdClass"));
        transaction.commit();
        getEntityManager().clear();
    }
}
