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

package ee.jakarta.tck.persistence.jpa32.jpql;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".JpqlBook"};
        return createDeploymentJar("jpa_jpa32_jpql.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 JPQL set operations. The test verifies that
     * {@code UNION}, {@code INTERSECT}, and {@code EXCEPT} combine compatible
     * select query results with the expected set semantics.
     */
    @Test
    public void jpqlSetOperationsTest() {
        List<String> union = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32JpqlBook b WHERE b.quantity <= 20 "
                        + "UNION SELECT b.title FROM Jpa32JpqlBook b WHERE b.title IN ('Beta', 'Gamma')",
                        String.class)
                .getResultList();
        assertEquals(Set.of("Alpha", "Beta", "Gamma"), new HashSet<>(union));

        List<String> intersect = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32JpqlBook b WHERE b.quantity <= 20 "
                        + "INTERSECT SELECT b.title FROM Jpa32JpqlBook b WHERE b.title IN ('Beta', 'Gamma')",
                        String.class)
                .getResultList();
        assertEquals(Set.of("Beta"), new HashSet<>(intersect));

        List<String> except = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32JpqlBook b WHERE b.quantity <= 20 "
                        + "EXCEPT SELECT b.title FROM Jpa32JpqlBook b WHERE b.title IN ('Beta', 'Gamma')",
                        String.class)
                .getResultList();
        assertEquals(Set.of("Alpha"), new HashSet<>(except));
    }

    /**
     * Tests Jakarta Persistence 3.2 JPQL scalar expression additions. The test
     * verifies the string functions {@code LEFT}, {@code RIGHT}, and
     * {@code REPLACE}, the string concatenation operator {@code ||}, and
     * numeric conversion using {@code CAST}.
     */
    @Test
    public void jpqlCastStringFunctionsAndConcatOperatorTest() {
        String text = getEntityManager()
                .createQuery("SELECT LEFT(b.title, 2) || ':' || RIGHT(b.title, 2) || ':' "
                        + "|| REPLACE(b.title, 'Alpha', 'A') "
                        + "FROM Jpa32JpqlBook b WHERE b.id = 1", String.class)
                .getSingleResult();
        assertEquals("Al:ha:A", text);

        Number castValue = getEntityManager()
                .createQuery("SELECT CAST(b.code AS INTEGER) + 5 FROM Jpa32JpqlBook b WHERE b.id = 1",
                        Number.class)
                .getSingleResult();
        assertEquals(15, castValue.intValue());
    }

    /**
     * Tests Jakarta Persistence 3.2 JPQL entity helper functions. The test
     * verifies that {@code ID(entity)} resolves the entity identifier and
     * {@code VERSION(entity)} resolves the entity version value.
     */
    @Test
    public void jpqlIdAndVersionFunctionsTest() {
        Integer id = getEntityManager()
                .createQuery("SELECT ID(b) FROM Jpa32JpqlBook b WHERE b.id = 1", Integer.class)
                .getSingleResult();
        assertEquals(1, id);

        Integer version = getEntityManager()
                .createQuery("SELECT VERSION(b) FROM Jpa32JpqlBook b WHERE b.id = 1", Integer.class)
                .getSingleResult();
        assertNotNull(version);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new JpqlBook(1, "Alpha", "10", 10));
        getEntityManager().persist(new JpqlBook(2, "Beta", "20", 20));
        getEntityManager().persist(new JpqlBook(3, "Gamma", "30", 30));
        transaction.commit();
        getEntityManager().clear();
    }
}
