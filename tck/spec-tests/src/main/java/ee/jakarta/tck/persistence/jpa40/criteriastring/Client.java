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

package ee.jakarta.tck.persistence.jpa40.criteriastring;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.CriteriaStatement;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".CriteriaStringBook"};
        return createDeploymentJar("jpa_jpa40_criteriastring.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 4.0 creation of a {@link CriteriaQuery} from a
     * JPQL select string. The test verifies the returned criteria object exposes
     * the expected result type, root, restriction, parameter, and ordering, and
     * that executing the criteria object produces the same ordered result as
     * executing the original JPQL query string.
     */
    @Test
    public void criteriaBuilderJpqlSelectStringEquivalenceTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        String jpql = "SELECT b FROM Jpa40CriteriaStringBook b WHERE b.id > :minId ORDER BY b.title DESC";
        CriteriaQuery<CriteriaStringBook> select = builder.createQuery(CriteriaStringBook.class, jpql);

        assertEquals(CriteriaStringBook.class, select.getResultType());
        assertEquals(1, select.getRoots().size());
        assertEquals(CriteriaStringBook.class, select.getRoots().iterator().next().getModel().getJavaType());
        assertEquals(CriteriaStringBook.class, select.getSelection().getJavaType());
        assertNotNull(select.getRestriction());
        assertEquals(1, select.getParameters().size());
        assertTrue(select.getParameters().stream().anyMatch(parameter -> "minId".equals(parameter.getName())));
        assertFalse(select.getOrderList().isEmpty());

        List<Integer> jpqlResult = getEntityManager()
                .createQuery(jpql, CriteriaStringBook.class)
                .setParameter("minId", 0)
                .getResultList()
                .stream()
                .map(CriteriaStringBook::getId)
                .toList();
        List<Integer> criteriaResult = getEntityManager()
                .createQuery(select)
                .setParameter("minId", 0)
                .getResultList()
                .stream()
                .map(CriteriaStringBook::getId)
                .toList();

        assertEquals(jpqlResult, criteriaResult);
        assertEquals(List.of(3, 2, 1), criteriaResult);
    }

    /**
     * Tests Jakarta Persistence 4.0 creation of a {@link CriteriaUpdate} from a
     * JPQL update string. The test verifies the returned criteria statement has
     * the same root, restriction, and parameters as the JPQL update, and that
     * executing it produces the same row count and database state as executing
     * the original JPQL update string.
     */
    @Test
    public void criteriaBuilderJpqlUpdateStringEquivalenceTest() {
        String jpql = "UPDATE Jpa40CriteriaStringBook b SET b.title = :title WHERE b.id = :id";

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int jpqlUpdated = getEntityManager()
                .createQuery(jpql)
                .asStatement()
                .setParameter("title", "Updated")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();
        List<String> jpqlTitles = titlesById();

        resetTestData();

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<CriteriaStringBook> update = builder.createCriteriaUpdate(CriteriaStringBook.class, jpql);
        assertCriteriaStatementRepresentsParsedJpql(update, 2);

        transaction = getEntityTransaction();
        transaction.begin();
        int criteriaUpdated = getEntityManager()
                .createStatement(update)
                .setParameter("title", "Updated")
                .setParameter("id", 1)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(jpqlUpdated, criteriaUpdated);
        assertEquals(jpqlTitles, titlesById());
    }

    /**
     * Tests Jakarta Persistence 4.0 creation of a {@link CriteriaDelete} from a
     * JPQL delete string. The test verifies the returned criteria statement has
     * the same root, restriction, and parameter as the JPQL delete, and that
     * executing it produces the same row count and remaining database rows as
     * executing the original JPQL delete string.
     */
    @Test
    public void criteriaBuilderJpqlDeleteStringEquivalenceTest() {
        String jpql = "DELETE FROM Jpa40CriteriaStringBook b WHERE b.id <= :maxId";

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int jpqlDeleted = getEntityManager()
                .createQuery(jpql)
                .asStatement()
                .setParameter("maxId", 2)
                .execute();
        transaction.commit();
        getEntityManager().clear();
        List<Integer> jpqlRemainingIds = idsById();

        resetTestData();

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<CriteriaStringBook> delete = builder.createCriteriaDelete(CriteriaStringBook.class, jpql);
        assertCriteriaStatementRepresentsParsedJpql(delete, 1);

        transaction = getEntityTransaction();
        transaction.begin();
        int criteriaDeleted = getEntityManager()
                .createStatement(delete)
                .setParameter("maxId", 2)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(jpqlDeleted, criteriaDeleted);
        assertEquals(jpqlRemainingIds, idsById());
        assertEquals(List.of(3), idsById());
    }

    /**
     * Tests Jakarta Persistence 4.0 untyped JPQL parsing overloads for select,
     * update, and delete criteria objects.
     */
    @Test
    public void criteriaBuilderUntypedJpqlStringOverloadsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<?> select = builder.createQuery(
                "SELECT b.title FROM Jpa40CriteriaStringBook b WHERE b.id = :id");
        assertEquals(String.class, select.getSelection().getJavaType());
        assertEquals(1, select.getParameters().size());
        Object title = getEntityManager()
                .createQuery(select)
                .setParameter("id", 1)
                .getSingleResult();
        assertEquals("Alpha", title);

        CriteriaUpdate<?> update = builder.createCriteriaUpdate(
                "UPDATE Jpa40CriteriaStringBook b SET b.title = :title WHERE b.id = :id");
        assertCriteriaStatementRepresentsParsedJpql(update, 2);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager()
                .createStatement(update)
                .setParameter("title", "Untyped")
                .setParameter("id", 2)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(1, updated);
        assertEquals(List.of("Alpha", "Untyped", "Gamma"), titlesById());

        resetTestData();

        CriteriaDelete<?> delete = builder.createCriteriaDelete(
                "DELETE FROM Jpa40CriteriaStringBook b WHERE b.id < :id");
        assertCriteriaStatementRepresentsParsedJpql(delete, 1);

        transaction = getEntityTransaction();
        transaction.begin();
        int deleted = getEntityManager()
                .createStatement(delete)
                .setParameter("id", 3)
                .execute();
        transaction.commit();
        getEntityManager().clear();

        assertEquals(2, deleted);
        assertEquals(List.of(3), idsById());
    }

    /**
     * Tests criteria set operation selects using the {@link CriteriaSelect}
     * execution path.
     */
    @Test
    public void criteriaBuilderSetOperationSelectsTest() {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        assertEquals(List.of(1, 2, 3),
                sortedIds(builder.union(idLessThanOrEqualTo(builder, 2), idGreaterThanOrEqualTo(builder, 2))));
        assertEquals(List.of(1, 2, 2, 3),
                sortedIds(builder.unionAll(idLessThanOrEqualTo(builder, 2), idGreaterThanOrEqualTo(builder, 2))));
        assertEquals(List.of(2),
                sortedIds(builder.intersect(idLessThanOrEqualTo(builder, 2), idGreaterThanOrEqualTo(builder, 2))));
        assertEquals(List.of(1),
                sortedIds(builder.except(idLessThanOrEqualTo(builder, 2), idGreaterThanOrEqualTo(builder, 2))));
    }

    private void assertCriteriaStatementRepresentsParsedJpql(CriteriaStatement<?> statement,
                                                             int expectedParameterCount) {
        assertEquals(CriteriaStringBook.class, statement.getRoot().getModel().getJavaType());
        assertNotNull(statement.getRestriction());
        assertEquals(expectedParameterCount, statement.getParameters().size());
    }

    private CriteriaQuery<Integer> idLessThanOrEqualTo(CriteriaBuilder builder, int id) {
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<CriteriaStringBook> book = criteria.from(CriteriaStringBook.class);
        criteria.select(book.get("id"))
                .where(builder.le(book.get("id"), id));
        return criteria;
    }

    private CriteriaQuery<Integer> idGreaterThanOrEqualTo(CriteriaBuilder builder, int id) {
        CriteriaQuery<Integer> criteria = builder.createQuery(Integer.class);
        Root<CriteriaStringBook> book = criteria.from(CriteriaStringBook.class);
        criteria.select(book.get("id"))
                .where(builder.ge(book.get("id"), id));
        return criteria;
    }

    private List<Integer> sortedIds(CriteriaSelect<Integer> select) {
        return getEntityManager()
                .createQuery(select)
                .getResultList()
                .stream()
                .sorted()
                .toList();
    }

    private void resetTestData() {
        removeTestData();
        createTestData();
    }

    private List<Integer> idsById() {
        return getEntityManager()
                .createQuery("SELECT b FROM Jpa40CriteriaStringBook b ORDER BY b.id", CriteriaStringBook.class)
                .getResultList()
                .stream()
                .map(CriteriaStringBook::getId)
                .toList();
    }

    private List<String> titlesById() {
        return getEntityManager()
                .createQuery("SELECT b FROM Jpa40CriteriaStringBook b ORDER BY b.id", CriteriaStringBook.class)
                .getResultList()
                .stream()
                .map(CriteriaStringBook::getTitle)
                .toList();
    }

    private void createTestData() {
        getEntityManager().clear();
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new CriteriaStringBook(1, "Alpha"));
        getEntityManager().persist(new CriteriaStringBook(2, "Beta"));
        getEntityManager().persist(new CriteriaStringBook(3, "Gamma"));
        transaction.commit();
        getEntityManager().clear();
    }
}
