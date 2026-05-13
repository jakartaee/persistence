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

package ee.jakarta.tck.persistence.jpa40.criteria.specialized;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.BooleanExpression;
import jakarta.persistence.criteria.ComparableExpression;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.LocalDateField;
import jakarta.persistence.criteria.Nulls;
import jakarta.persistence.criteria.NumericExpression;
import jakarta.persistence.criteria.PluralExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.TemporalExpression;
import jakarta.persistence.criteria.TextExpression;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SpecializedBook", packageName + ".SpecializedBook_"};
        return createDeploymentJar("jpa_jpa40_criteria_specialized.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 specialized criteria and metamodel types.
     * The test verifies that specialized metamodel attributes produce matching
     * specialized criteria expressions for text, numeric, temporal, boolean,
     * and comparable attributes and that those expressions may be used in a
     * criteria query.
     */
    @Test
    public void specializedCriteriaExpressionsAndAttributesTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SpecializedBook> criteria = builder.createQuery(SpecializedBook.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);

        TextExpression title = book.get(SpecializedBook_.title);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);
        TemporalExpression<LocalDate> publishedOn = book.get(SpecializedBook_.publishedOn);
        BooleanExpression available = book.get(SpecializedBook_.available);
        ComparableExpression<String> category = book.get(SpecializedBook_.category);

        criteria.select(book).where(
                title.startsWith("Al"),
                quantity.plus(5).gt(14),
                publishedOn.extract(LocalDateField.YEAR).equalTo(2026),
                publishedOn.lessThan(builder.temporalLiteral(LocalDate.of(2027, 1, 1))),
                available.and(builder.booleanLiteral(true)),
                category.greaterThanOrEqualTo("fiction"));

        SpecializedBook result = getEntityManager().createQuery(criteria).getSingleResult();
        assertEquals("Alpha", result.getTitle());
    }

    /**
     * Tests Jakarta Persistence 4.0 fluent operations on specialized criteria
     * expressions. The predicates and selected expression are evaluated by the
     * database so the test verifies that the methods are not just exposed by the
     * API, but also affect query execution.
     */
    @Test
    public void specializedCriteriaFluentExpressionOperationsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);

        TextExpression title = book.get(SpecializedBook_.title);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);
        BooleanExpression available = book.get(SpecializedBook_.available);
        ComparableExpression<String> category = book.get(SpecializedBook_.category);

        criteria.select(title.upper()
                        .append(builder.stringLiteral("-"))
                        .append(category.coalesce("uncategorized")))
                .where(
                        title.contains("ph"),
                        title.notContains("zz"),
                        title.notStartsWith("Be"),
                        title.endsWith("ha"),
                        title.substring(1, 3).equalTo("Alp"),
                        title.locate("ph").equalTo(3),
                        quantity.times(builder.numericLiteral(2)).minus(5).equalTo(15),
                        quantity.negated().lt(0),
                        quantity.between(9, 11),
                        available.and(builder.booleanLiteral(true)),
                        category.coalesce("uncategorized").equalTo("fiction"));

        assertEquals("ALPHA-fiction", getEntityManager().createQuery(criteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 4.0 null precedence ordering through the
     * specialized comparable expression ordering methods.
     */
    @Test
    public void specializedCriteriaNullPrecedenceOrderingTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Integer> nullsLast = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = nullsLast.from(SpecializedBook.class);
        ComparableExpression<String> category = book.get(SpecializedBook_.category);
        nullsLast.select(book.get("id"))
                .orderBy(category.asc(Nulls.LAST), builder.asc(book.get("id")));

        assertEquals(Nulls.LAST, nullsLast.getOrderList().get(0).getNullPrecedence());
        assertEquals(List.of(1, 2, 3), getEntityManager().createQuery(nullsLast).getResultList());

        CriteriaQuery<Integer> nullsFirst = builder.createQuery(Integer.class);
        book = nullsFirst.from(SpecializedBook.class);
        category = book.get(SpecializedBook_.category);
        nullsFirst.select(book.get("id"))
                .orderBy(category.desc(Nulls.FIRST), builder.asc(book.get("id")));

        assertEquals(Nulls.FIRST, nullsFirst.getOrderList().get(0).getNullPrecedence());
        assertEquals(List.of(3, 2, 1), getEntityManager().createQuery(nullsFirst).getResultList());
    }

    /**
     * Tests Jakarta Persistence 4.0 shortcuts for subquery predicates and the
     * new expression overload accepting a subquery directly.
     */
    @Test
    public void specializedCriteriaSubqueryExistsAndInShortcutsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);

        Subquery<Integer> alphaQuantity = criteria.subquery(Integer.class);
        Root<SpecializedBook> alphaBook = alphaQuantity.from(SpecializedBook.class);
        alphaQuantity.select(alphaBook.get(SpecializedBook_.quantity))
                .where(alphaBook.get(SpecializedBook_.title).equalTo("Alpha"));

        Subquery<Integer> lowerQuantity = criteria.subquery(Integer.class);
        Root<SpecializedBook> lowerBook = lowerQuantity.from(SpecializedBook.class);
        lowerQuantity.select(lowerBook.get("id"))
                .where(lowerBook.get(SpecializedBook_.quantity).lt(quantity));

        criteria.select(book.get("id"))
                .where(quantity.in(alphaQuantity), lowerQuantity.exists());

        assertEquals(List.of(1), getEntityManager().createQuery(criteria).getResultList());
    }

    /**
     * Tests Jakarta Persistence 4.0 subquery quantifier shortcuts.
     */
    @Test
    public void specializedCriteriaSubqueryQuantifierShortcutsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);

        criteria.select(book.get("id"))
                .where(
                        quantity.gt(quantitySubquery(criteria, "Beta").all()),
                        quantity.lt(quantitySubquery(criteria, "Alpha").some()),
                        quantity.lt(quantitySubquery(criteria, "Alpha").any()))
                .orderBy(builder.asc(book.get("id")));

        assertEquals(List.of(3), getEntityManager().createQuery(criteria).getResultList());
    }

    /**
     * Tests Jakarta Persistence 4.0 plural criteria expressions for
     * collection-valued paths.
     */
    @Test
    public void specializedCriteriaPluralExpressionOperationsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Integer> tagged = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = tagged.from(SpecializedBook.class);
        PluralExpression<Set<String>, String> tags = book.get(SpecializedBook_.tags);
        tagged.select(book.get("id"))
                .where(
                        tags.isNotEmpty(),
                        tags.contains("fiction"),
                        tags.notContains("archived"),
                        tags.size().ge(2))
                .orderBy(builder.asc(book.get("id")));

        assertEquals(List.of(1), getEntityManager().createQuery(tagged).getResultList());

        CriteriaQuery<Integer> untagged = builder.createQuery(Integer.class);
        book = untagged.from(SpecializedBook.class);
        tags = book.get(SpecializedBook_.tags);
        untagged.select(book.get("id"))
                .where(tags.isEmpty());

        assertEquals(List.of(3), getEntityManager().createQuery(untagged).getResultList());
    }

    /**
     * Tests Jakarta Persistence 4.0 fluent expression case, nullif, coalesce,
     * and aggregate shortcuts.
     */
    @Test
    public void specializedCriteriaExpressionCaseAndAggregateShortcutsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> label = builder.createQuery(String.class);
        Root<SpecializedBook> book = label.from(SpecializedBook.class);
        TextExpression title = book.get(SpecializedBook_.title);
        label.select(title.selectCase(String.class)
                        .when("Alpha", title.nullif("Alpha").coalesce("first"))
                        .otherwise("other"))
                .where(title.equalTo("Alpha"));

        assertEquals("first", getEntityManager().createQuery(label).getSingleResult());

        CriteriaQuery<Long> count = builder.createQuery(Long.class);
        book = count.from(SpecializedBook.class);
        count.select(book.get(SpecializedBook_.category)
                .coalesce("uncategorized")
                .countDistinct());

        assertEquals(3L, getEntityManager().createQuery(count).getSingleResult());
    }

    private Subquery<Integer> quantitySubquery(CriteriaQuery<?> criteria, String title) {
        Subquery<Integer> subquery = criteria.subquery(Integer.class);
        Root<SpecializedBook> book = subquery.from(SpecializedBook.class);
        subquery.select(book.get(SpecializedBook_.quantity))
                .where(book.get(SpecializedBook_.title).equalTo(title));
        return subquery;
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SpecializedBook(
                1, "Alpha", 10, LocalDate.of(2026, 5, 10), true, "fiction",
                Set.of("fiction", "popular")));
        getEntityManager().persist(new SpecializedBook(
                2, "Beta", 3, LocalDate.of(2025, 1, 1), false, "reference",
                Set.of("reference")));
        getEntityManager().persist(new SpecializedBook(
                3, "NoCategory", 7, LocalDate.of(2026, 6, 1), true, null,
                Set.of()));
        transaction.commit();
        getEntityManager().clear();
    }

    private void removeTestData() {
        EntityTransaction transaction = getEntityTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        getEntityManagerFactory().getSchemaManager().truncate();
        getEntityManager().clear();
    }
}
