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

package ee.jakarta.tck.persistence.jpa32.criteria.functions;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.LocalDateField;
import jakarta.persistence.criteria.LocalDateTimeField;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".CriteriaFunctionBook"};
        return createDeploymentJar("jpa_jpa32_criteria_functions.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API expression additions. The test
     * verifies extracting a {@link LocalDateField} value from a date expression,
     * casting an expression to another Java type, and using the new
     * expression-level {@code notEqualTo} predicate builder.
     */
    @Test
    public void criteriaExtractCastAndNotEqualToTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<CriteriaFunctionBook> book = criteria.from(CriteriaFunctionBook.class);

        Expression<Integer> publishedYear = cb.extract(LocalDateField.YEAR, book.get("publishedOn"));
        Expression<Integer> code = book.<String>get("code").cast(Integer.class);
        criteria.select(book.get("title"))
                .where(List.of(
                        publishedYear.equalTo(2024),
                        book.get("title").notEqualTo("Alpha"),
                        cb.gt(code, 10)));

        assertEquals("Beta", em.createQuery(criteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API string function additions.
     * The test verifies criteria construction for {@code left}, {@code right},
     * {@code replace}, and list-based string concatenation.
     */
    @Test
    public void criteriaStringFunctionsTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<CriteriaFunctionBook> book = criteria.from(CriteriaFunctionBook.class);

        criteria.select(cb.concat(List.of(
                        cb.left(book.get("title"), 2),
                        cb.literal(":"),
                        cb.right(book.get("title"), 2),
                        cb.literal(":"),
                        cb.replace(book.get("title"), "Alpha", "A"))))
                .where(book.get("id").equalTo(1));

        assertEquals("Al:ha:A", em.createQuery(criteria).getSingleResult());
    }

    /**
     * Tests extracting a {@link LocalDateTimeField} value from a local date-time
     * expression.
     */
    @Test
    public void criteriaExtractLocalDateTimeFieldTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<CriteriaFunctionBook> book = criteria.from(CriteriaFunctionBook.class);

        Expression<Integer> hour = cb.extract(LocalDateTimeField.HOUR, book.get("publishedAt"));
        Expression<Integer> minute = cb.extract(LocalDateTimeField.MINUTE, book.get("publishedAt"));
        criteria.select(book.get("title"))
                .where(hour.equalTo(14), minute.equalTo(5));

        assertEquals("Beta", em.createQuery(criteria).getSingleResult());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new CriteriaFunctionBook(1, "Alpha", "10",
                LocalDate.of(2024, 1, 15), LocalDateTime.of(2024, 1, 15, 8, 30, 45)));
        getEntityManager().persist(new CriteriaFunctionBook(2, "Beta", "20",
                LocalDate.of(2024, 6, 1), LocalDateTime.of(2024, 6, 1, 14, 5, 10)));
        getEntityManager().persist(new CriteriaFunctionBook(3, "Gamma", "30",
                LocalDate.of(2025, 3, 20), LocalDateTime.of(2025, 3, 20, 21, 15, 20)));
        transaction.commit();
        getEntityManager().clear();
    }
}
