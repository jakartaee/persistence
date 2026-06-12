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
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.TemporalExpression;
import jakarta.persistence.criteria.TextExpression;
import jakarta.persistence.metamodel.BooleanAttribute;
import jakarta.persistence.metamodel.ComparableAttribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.NumericAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.TemporalAttribute;
import jakarta.persistence.metamodel.TextAttribute;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
     * Tests Jakarta Persistence 4.0 specialized runtime metamodel attribute
     * types. This verifies that provider-created metamodel attributes expose
     * the specialized interfaces, not only the injected static metamodel fields.
     */
    @Test
    public void specializedCriteriaRuntimeMetamodelAttributeTypesTest() {
        EntityType<SpecializedBook> bookType = getEntityManager().getMetamodel().entity(SpecializedBook.class);

        assertInstanceOf(TextAttribute.class, bookType.getSingularAttribute("title"));
        assertInstanceOf(NumericAttribute.class, bookType.getSingularAttribute("quantity"));
        assertInstanceOf(NumericAttribute.class, bookType.getSingularAttribute("price"));
        assertInstanceOf(TemporalAttribute.class, bookType.getSingularAttribute("publishedOn"));
        assertInstanceOf(BooleanAttribute.class, bookType.getSingularAttribute("available"));
        assertInstanceOf(ComparableAttribute.class, bookType.getSingularAttribute("category"));
        assertInstanceOf(SetAttribute.class, bookType.getSet("tags", String.class));
        assertInstanceOf(MapAttribute.class, bookType.getMap("tagScores", String.class, Integer.class));
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
     * Tests additional Jakarta Persistence 4.0 text expression shortcuts and
     * overloads.
     */
    @Test
    public void specializedCriteriaTextExpressionOperationsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);

        TextExpression title = book.get(SpecializedBook_.title);

        criteria.select(title.lower()
                        .replace("alpha", "omega")
                        .prepend("first-"))
                .where(
                        title.like(builder.stringLiteral("A%")),
                        title.notLike("B%"),
                        title.notEndsWith("zz"),
                        title.trim(CriteriaBuilder.Trimspec.BOTH).equalTo("Alpha"),
                        title.length().equalTo(5),
                        title.left(2).equalTo("Al"),
                        title.right(builder.numericLiteral(2)).equalTo("ha"),
                        title.replace(builder.stringLiteral("Al"), "Br").equalTo("Brpha"),
                        title.substring(builder.numericLiteral(2), builder.numericLiteral(2)).equalTo("lp"),
                        title.locate(builder.stringLiteral("ph"), builder.numericLiteral(1)).equalTo(3));

        assertEquals("first-omega", getEntityManager().createQuery(criteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 4.0 numeric expression shortcuts including
     * unary, arithmetic, floating-point, type conversion, and null handling
     * operations.
     */
    @Test
    public void specializedCriteriaNumericExpressionOperationsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);

        TextExpression title = book.get(SpecializedBook_.title);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);
        NumericExpression<Double> price = book.get(SpecializedBook_.price);

        criteria.select(book.get("id"))
                .where(
                        title.equalTo("Alpha"),
                        price.sign().equalTo(1),
                        price.negated().abs().equalTo(4.0),
                        price.ceiling().equalTo(4.0),
                        price.floor().equalTo(4.0),
                        price.dividedBy(2.0).equalTo(2.0),
                        price.subtractedFrom(10.0).equalTo(6.0),
                        price.dividedInto(20.0).equalTo(5.0),
                        price.sqrt().equalTo(2.0),
                        price.exp().between(54.0, 55.0),
                        price.ln().between(1.3, 1.4),
                        price.power(2).equalTo(16.0),
                        price.round(0).equalTo(4.0),
                        quantity.toLong().equalTo(10L),
                        quantity.toInteger().equalTo(10),
                        quantity.toFloat().between(9.9F, 10.1F),
                        quantity.toDouble().between(9.9D, 10.1D),
                        quantity.toBigDecimal().equalTo(BigDecimal.TEN),
                        quantity.toBigInteger().equalTo(BigInteger.TEN),
                        quantity.coalesce(0).equalTo(10),
                        quantity.nullif(0).equalTo(10));

        assertEquals(List.of(1), getEntityManager().createQuery(criteria).getResultList());
    }

    /**
     * Tests Jakarta Persistence 4.0 numeric aggregate expression shortcuts.
     */
    @Test
    public void specializedCriteriaNumericAggregateShortcutsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);

        criteria.multiselect(
                quantity.avg(),
                quantity.sumAsLong(),
                quantity.max(),
                quantity.min(),
                quantity.toFloat().sumAsDouble());

        Object[] result = getEntityManager().createQuery(criteria).getSingleResult();
        assertEquals(20.0 / 3.0, ((Number) result[0]).doubleValue(), 0.01);
        assertEquals(20L, ((Number) result[1]).longValue());
        assertEquals(10, ((Number) result[2]).intValue());
        assertEquals(3, ((Number) result[3]).intValue());
        assertEquals(20.0, ((Number) result[4]).doubleValue(), 0.01);
    }

    /**
     * Tests the Jakarta Persistence 4.0 value-first CriteriaBuilder.between()
     * overload.
     */
    @Test
    public void specializedCriteriaValueFirstBetweenOverloadTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);
        NumericExpression<Integer> quantity = book.get(SpecializedBook_.quantity);

        criteria.select(book.get("id"))
                .where(builder.between(10, quantity.minus(1), quantity.plus(1)));

        assertEquals(List.of(1), getEntityManager().createQuery(criteria).getResultList());
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
     * Tests Jakarta Persistence 4.0 boolean expression operations and boolean
     * expression varargs accepted by joins and having clauses.
     */
    @Test
    public void specializedCriteriaBooleanExpressionOperationsAndRestrictionsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<SpecializedBook> book = criteria.from(SpecializedBook.class);
        SetJoin<SpecializedBook, String> tag = book.join(SpecializedBook_.tags);
        BooleanExpression available = book.get(SpecializedBook_.available);

        tag.on(
                tag.equalTo("fiction").or(tag.equalTo("popular")),
                tag.notEqualTo("archived"));

        BooleanExpression availableOrNot = builder.or(new BooleanExpression[] {
                available.and(builder.booleanLiteral(true)),
                available.not()
        });

        criteria.select(book.get("id"))
                .distinct(true)
                .where(
                        availableOrNot,
                        available.coalesce(false).equalTo(true),
                        available.nullif(false).isNotNull());

        assertEquals(List.of(1), getEntityManager().createQuery(criteria).getResultList());

        CriteriaQuery<Boolean> groups = builder.createQuery(Boolean.class);
        Root<SpecializedBook> groupedBook = groups.from(SpecializedBook.class);
        BooleanExpression groupedAvailable = groupedBook.get(SpecializedBook_.available);
        groups.select(groupedAvailable)
                .groupBy(groupedAvailable)
                .having(
                        groupedAvailable.or(builder.booleanLiteral(false)),
                        groupedAvailable.isNotNull());

        assertEquals(Boolean.TRUE, getEntityManager().createQuery(groups).getSingleResult());
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

        CriteriaQuery<Integer> scored = builder.createQuery(Integer.class);
        book = scored.from(SpecializedBook.class);
        PluralExpression<Map<String, Integer>, Integer> tagScores = book.get(SpecializedBook_.tagScores);
        scored.select(book.get("id"))
                .where(
                        tagScores.isNotEmpty(),
                        tagScores.contains(builder.numericLiteral(10)),
                        tagScores.notContains(99),
                        tagScores.size().equalTo(2));

        assertEquals(List.of(1), getEntityManager().createQuery(scored).getResultList());
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
                1, "Alpha", 10, 4.0, LocalDate.of(2026, 5, 10), true, "fiction",
                Set.of("fiction", "popular"), Map.of("fiction", 10, "popular", 5)));
        getEntityManager().persist(new SpecializedBook(
                2, "Beta", 3, 1.5, LocalDate.of(2025, 1, 1), false, "reference",
                Set.of("reference"), Map.of("reference", 3)));
        getEntityManager().persist(new SpecializedBook(
                3, "NoCategory", 7, 3.0, LocalDate.of(2026, 6, 1), true, null,
                Set.of(), Map.of()));
        transaction.commit();
        getEntityManager().clear();
    }
}
