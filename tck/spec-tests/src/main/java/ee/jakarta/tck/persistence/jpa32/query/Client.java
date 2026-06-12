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

package ee.jakarta.tck.persistence.jpa32.query;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".QueryBook"};
        return createDeploymentJar("jpa_jpa32_query.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests the Jakarta Persistence 3.2 JPQL grammar change that makes
     * identification variable declarations optional for simple queries. The test
     * verifies that an entity name may be used directly in the {@code FROM}
     * clause and filtered without an explicit alias.
     */
    @Test
    public void jpqlOptionalClausesTest() {
        List<QueryBook> books = getEntityManager()
                .createQuery("FROM Jpa32QueryBook WHERE title = :title", QueryBook.class)
                .setParameter("title", "Alpha")
                .getResultList();
        assertEquals(1, books.size());
        assertEquals(1, books.get(0).getId());
    }

    /**
     * Tests the Jakarta Persistence 3.2 JPQL relaxation allowing scalar
     * expressions in {@code ORDER BY}. The test verifies ordering by an
     * arithmetic expression rather than a selected state field alone.
     */
    @Test
    public void jpqlOrderByScalarExpressionTest() {
        List<String> orderedTitles = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32QueryBook b ORDER BY b.quantity + 1 DESC", String.class)
                .getResultList();
        assertEquals("Gamma", orderedTitles.get(0));
    }

    /**
     * Tests Jakarta Persistence 3.2 JPQL numeric literal suffixes. The test
     * verifies that {@code bi} and {@code bd} literals are parsed as
     * {@link BigInteger} and {@link BigDecimal} values in query predicates.
     */
    @Test
    public void jpqlNumericLiteralSuffixesTest() {
        String title = getEntityManager()
                .createQuery("SELECT b.title FROM Jpa32QueryBook b "
                        + "WHERE b.bigIntegerValue = 10bi AND b.bigDecimalValue = 2.50bd", String.class)
                .getSingleResult();
        assertEquals("Alpha", title);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new QueryBook(1, "Alpha", 10, BigInteger.TEN, new BigDecimal("2.50")));
        getEntityManager().persist(new QueryBook(2, "Beta", 20, BigInteger.valueOf(11), new BigDecimal("3.00")));
        getEntityManager().persist(new QueryBook(3, "Gamma", 30, BigInteger.valueOf(12), new BigDecimal("4.00")));
        transaction.commit();
        getEntityManager().clear();
    }
}
