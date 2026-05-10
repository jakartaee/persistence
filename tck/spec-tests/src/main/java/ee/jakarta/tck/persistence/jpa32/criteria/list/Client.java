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

package ee.jakarta.tck.persistence.jpa32.criteria.list;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Nulls;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".CriteriaListBook"};
        return createDeploymentJar("jpa_jpa32_criteria_list.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 Criteria API list-based predicate overloads.
     * The test verifies {@code where(List)}, {@code having(List)},
     * {@code and(List)}, and {@code or(List)} when building query restrictions.
     */
    @Test
    public void criteriaWhereHavingAndOrListOverloadsTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<String> whereCriteria = cb.createQuery(String.class);
        Root<CriteriaListBook> whereBook = whereCriteria.from(CriteriaListBook.class);
        Predicate fiction = whereBook.get("category").equalTo("fiction");
        Predicate beta = whereBook.get("title").equalTo("Beta");
        Predicate quantityTwenty = whereBook.get("quantity").equalTo(20);
        Predicate quantityImpossible = whereBook.get("quantity").equalTo(999);
        whereCriteria.select(whereBook.get("title"))
                .where(List.of(
                        cb.and(List.of(fiction, beta)),
                        cb.or(List.of(quantityTwenty, quantityImpossible))));
        assertEquals("Beta", em.createQuery(whereCriteria).getSingleResult());

        CriteriaQuery<String> havingCriteria = cb.createQuery(String.class);
        Root<CriteriaListBook> havingBook = havingCriteria.from(CriteriaListBook.class);
        Expression<String> category = havingBook.get("category");
        havingCriteria.select(category)
                .groupBy(List.of(category))
                .having(List.of(cb.gt(cb.count(havingBook), 1L)));
        assertEquals("fiction", em.createQuery(havingCriteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API list-based selection and
     * expression overloads. The test verifies {@code array(List)},
     * {@code tuple(List)}, and {@code concat(List)} query construction.
     */
    @Test
    public void criteriaArrayTupleAndConcatListOverloadsTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Object[]> arrayCriteria = cb.createQuery(Object[].class);
        Root<CriteriaListBook> arrayBook = arrayCriteria.from(CriteriaListBook.class);
        List<Selection<?>> arraySelections =
                List.of(arrayBook.get("title"), arrayBook.get("quantity"));
        arrayCriteria.select(cb.array(arraySelections))
                .where(arrayBook.get("id").equalTo(1));
        Object[] arrayResult = em.createQuery(arrayCriteria).getSingleResult();
        assertEquals("Alpha", arrayResult[0]);
        assertEquals(10, arrayResult[1]);

        CriteriaQuery<Tuple> tupleCriteria = cb.createTupleQuery();
        Root<CriteriaListBook> tupleBook = tupleCriteria.from(CriteriaListBook.class);
        List<Selection<?>> tupleSelections =
                List.of(tupleBook.get("title").alias("title"),
                        tupleBook.get("quantity").alias("quantity"));
        tupleCriteria.select(cb.tuple(tupleSelections))
                .where(tupleBook.get("id").equalTo(1));
        Tuple tuple = em.createQuery(tupleCriteria).getSingleResult();
        assertEquals("Alpha", tuple.get("title", String.class));
        assertEquals(10, tuple.get("quantity", Integer.class));

        CriteriaQuery<String> concatCriteria = cb.createQuery(String.class);
        Root<CriteriaListBook> concatBook = concatCriteria.from(CriteriaListBook.class);
        concatCriteria.select(cb.concat(List.<Expression<String>>of(
                        concatBook.get("title"),
                        cb.literal(":"),
                        concatBook.get("category"))))
                .where(concatBook.get("id").equalTo(1));
        assertEquals("Alpha:fiction", em.createQuery(concatCriteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API null ordering support. The test
     * verifies that {@link Nulls#FIRST} and {@link Nulls#LAST} can be supplied
     * when building order expressions.
     */
    @Test
    public void criteriaNullPrecedenceTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Integer> nullsFirstCriteria = cb.createQuery(Integer.class);
        Root<CriteriaListBook> nullsFirstBook = nullsFirstCriteria.from(CriteriaListBook.class);
        nullsFirstCriteria.select(nullsFirstBook.get("id"))
                .orderBy(List.of(
                        cb.asc(nullsFirstBook.get("nullableLabel"), Nulls.FIRST),
                        cb.asc(nullsFirstBook.get("id"))));
        List<Integer> nullsFirst = em.createQuery(nullsFirstCriteria).getResultList();
        assertEquals(1, nullsFirst.get(0));

        CriteriaQuery<Integer> nullsLastCriteria = cb.createQuery(Integer.class);
        Root<CriteriaListBook> nullsLastBook = nullsLastCriteria.from(CriteriaListBook.class);
        nullsLastCriteria.select(nullsLastBook.get("id"))
                .orderBy(List.of(
                        cb.asc(nullsLastBook.get("nullableLabel"), Nulls.LAST),
                        cb.asc(nullsLastBook.get("id"))));
        List<Integer> nullsLast = em.createQuery(nullsLastCriteria).getResultList();
        assertEquals(1, nullsLast.get(nullsLast.size() - 1));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new CriteriaListBook(1, "Alpha", "fiction", null, 10));
        getEntityManager().persist(new CriteriaListBook(2, "Beta", "fiction", "bravo", 20));
        getEntityManager().persist(new CriteriaListBook(3, "Gamma", "tech", "charlie", 30));
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
