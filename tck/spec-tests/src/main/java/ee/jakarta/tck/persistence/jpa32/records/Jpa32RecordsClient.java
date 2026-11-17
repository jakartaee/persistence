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

package ee.jakarta.tck.persistence.jpa32.records;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Jpa32RecordsClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa32RecordsClient.class.getPackageName();
        String[] classes = {packageName + ".RecordBook", packageName + ".BookLocation"};
        return createDeploymentJar("jpa_jpa32_records.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 3.2 support for record classes as embeddables.
     * The test verifies that an entity can persist and retrieve an embedded Java
     * record value.
     */
    @Test
    public void recordEmbeddableTest() {
        RecordBook book = getEntityManager().find(RecordBook.class, 1);
        assertNotNull(book);
        assertEquals(new BookLocation("Madrid", "A1"), book.getLocation());
    }

    /**
     * Tests querying entities with embedded records via JPQL, including navigation
     * through record component path expressions, selecting the embedded record itself,
     * projecting individual record components, and parameter binding with record values.
     */
    @Test
    public void recordEmbeddableJpqlQueryTest() {
        // Query by navigating through embedded record component
        RecordBook book = getEntityManager()
                .createQuery("SELECT b FROM Jpa32RecordBook b WHERE b.location.city = :city", RecordBook.class)
                .setParameter("city", "Madrid")
                .getSingleResult();
        assertNotNull(book);
        assertEquals(1, book.getId());
        assertEquals("Alpha", book.getTitle());

        // Select the embedded record itself
        BookLocation location = getEntityManager()
                .createQuery("SELECT b.location FROM Jpa32RecordBook b WHERE b.id = :id", BookLocation.class)
                .setParameter("id", 1)
                .getSingleResult();
        assertNotNull(location);
        assertEquals(new BookLocation("Madrid", "A1"), location);
        assertEquals("Madrid", location.city());
        assertEquals("A1", location.shelfCode());

        // Project individual record components
        String city = getEntityManager()
                .createQuery("SELECT b.location.city FROM Jpa32RecordBook b WHERE b.id = 1", String.class)
                .getSingleResult();
        assertEquals("Madrid", city);

        // Parameter binding using record instance
        List<RecordBook> books = getEntityManager()
                .createQuery("SELECT b FROM Jpa32RecordBook b WHERE b.location = :loc", RecordBook.class)
                .setParameter("loc", new BookLocation("Madrid", "A1"))
                .getResultList();
        assertEquals(1, books.size());
        assertEquals(1, books.get(0).getId());
    }

    /**
     * Tests updating an entity's embedded record by assigning a new record instance,
     * respecting Java record immutability.
     */
    @Test
    public void recordEmbeddableUpdateTest() {
        EntityTransaction tx = getEntityTransaction();
        tx.begin();
        RecordBook book = getEntityManager().find(RecordBook.class, 1);
        assertNotNull(book);
        book.setLocation(new BookLocation("Barcelona", "B2"));
        tx.commit();
        getEntityManager().clear();

        RecordBook updated = getEntityManager().find(RecordBook.class, 1);
        assertNotNull(updated);
        assertEquals(new BookLocation("Barcelona", "B2"), updated.getLocation());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new RecordBook(1, "Alpha", new BookLocation("Madrid", "A1")));
        transaction.commit();
        getEntityManager().clear();
    }
}
