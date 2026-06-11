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

package ee.jakarta.tck.persistence.jpa40.jpql;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".JpqlBook", packageName + ".JpqlBookSummary"};
        return createDeploymentJar("jpa_jpa40_jpql.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 JPQL constructor result shorthand. The test
     * verifies a multi-item select list may be mapped to the explicit result
     * class constructor without using {@code SELECT NEW}.
     */
    @Test
    public void constructorResultWithoutSelectNewTest() {
        List<JpqlBookSummary> summaries = getEntityManager()
                .createQuery("SELECT b.id, b.title FROM Jpa40JpqlBook b ORDER BY b.id",
                        JpqlBookSummary.class)
                .getResultList();

        assertEquals(List.of(
                new JpqlBookSummary(1, "Alpha"),
                new JpqlBookSummary(2, "Beta"),
                new JpqlBookSummary(3, "Gamma")), summaries);
    }

    /**
     * Tests Jakarta Persistence 4.0 optional {@code ELSE} branches in JPQL
     * {@code CASE} expressions. The test verifies both searched and simple
     * case expressions evaluate to {@code NULL} when no branch matches.
     */
    @Test
    public void caseExpressionWithoutElseTest() {
        List<String> searchedCase = getEntityManager()
                .createQuery("SELECT COALESCE(CASE WHEN b.quantity > 10 THEN b.title END, 'none') "
                        + "FROM Jpa40JpqlBook b ORDER BY b.id", String.class)
                .getResultList();

        assertEquals(List.of("none", "Beta", "Gamma"), searchedCase);

        List<String> simpleCase = getEntityManager()
                .createQuery("SELECT COALESCE(CASE b.category WHEN 'fiction' THEN b.title END, 'other') "
                        + "FROM Jpa40JpqlBook b ORDER BY b.id", String.class)
                .getResultList();

        assertEquals(List.of("Alpha", "other", "Gamma"), simpleCase);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new JpqlBook(1, "Alpha", "fiction", 10));
        getEntityManager().persist(new JpqlBook(2, "Beta", "reference", 20));
        getEntityManager().persist(new JpqlBook(3, "Gamma", "fiction", 30));
        transaction.commit();
        getEntityManager().clear();
    }
}
