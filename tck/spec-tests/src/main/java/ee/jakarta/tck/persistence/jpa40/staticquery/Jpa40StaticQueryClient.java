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

package ee.jakarta.tck.persistence.jpa40.staticquery;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Jpa40StaticQueryClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String[] classes = {
                StaticQueryBook.class.getName(),
                BookSummary.class.getName(),
                BookQueries.class.getName()
        };
        return createDeploymentJar("jpa_jpa40_staticquery.jar", getClass().getPackageName(), classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new StaticQueryBook(1, "Alpha"));
        getEntityManager().persist(new StaticQueryBook(2, "Beta"));
        getEntityManager().persist(new StaticQueryBook(3, "Gamma"));
        transaction.commit();
        getEntityManager().clear();
    }

    /**
     * Verifies {@code @JakartaQuery} infers the result class from a list return type
     * and packages each row using the matching constructor.
     */
    @Test
    public void jakartaQueryAnnotationListConstructorResultTest() {
        List<?> summaries = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#bookSummaries()")
                .getResultList();

        assertEquals(List.of(
                new BookSummary(1, "Alpha"),
                new BookSummary(2, "Beta"),
                new BookSummary(3, "Gamma")), summaries);
    }

    /**
     * Verifies lookup by the annotated method's signature and result class
     * inference from a single record return type.
     */
    @Test
    public void jakartaQueryAnnotationSingleConstructorResultTest() {
        Object summary = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#bookSummary(java.lang.Integer)")
                .setParameter("id", 2)
                .getSingleResult();

        assertEquals(new BookSummary(2, "Beta"), summary);
    }

    /**
     * Verifies {@code @JakartaQuery} infers the result class from a stream return type.
     */
    @Test
    public void jakartaQueryAnnotationStreamConstructorResultTest() {
        try (var summaries = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#bookSummaryStream()")
                .getResultStream()) {
            assertEquals(List.of(
                    new BookSummary(1, "Alpha"),
                    new BookSummary(2, "Beta"),
                    new BookSummary(3, "Gamma")), summaries.toList());
        }
    }

    /**
     * Verifies {@code @NativeQuery} infers the record result class from a list return type.
     */
    @Test
    public void nativeQueryAnnotationListConstructorResultTest() {
        List<?> summaries = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#nativeBookSummaries()")
                .getResultList();

        assertEquals(List.of(
                new BookSummary(1, "Alpha"),
                new BookSummary(2, "Beta"),
                new BookSummary(3, "Gamma")), summaries);
    }

    /**
     * Verifies lookup of the native overload by its method signature and
     * execution with a positional parameter.
     */
    @Test
    public void nativeQueryAnnotationSingleConstructorResultTest() {
        Object summary = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#bookSummary(java.lang.String)")
                .setParameter(1, "Gamma")
                .getSingleResult();

        assertEquals(new BookSummary(3, "Gamma"), summary);
    }

    /**
     * Verifies that {@code @QueryOptions} configures a JPQL query's timeout
     * and lock mode, and the requested lock requires an active transaction.
     */
    @Test
    public void jakartaQueryOptionsLockModeAndTimeoutTest() {
        TypedQuery<StaticQueryBook> query = getEntityManager()
                .createNamedQuery(BookQueries.class.getName() + "#lockedBook(java.lang.Integer)",
                        StaticQueryBook.class)
                .setParameter("id", 1);

        assertEquals(4000, query.getTimeout());
        assertEquals(LockModeType.PESSIMISTIC_WRITE, query.getLockMode());
        assertThrows(TransactionRequiredException.class, query::getSingleResult);
    }

    /**
     * Verifies that {@code @QueryOptions} can request a flush before a native query.
     */
    @Test
    public void nativeQueryOptionsFlushTest() {
        var em = getEntityManager();
        em.setFlushMode(FlushModeType.EXPLICIT);

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        em.find(StaticQueryBook.class, 1).setTitle("Pending query title");
        String title = em.createNamedQuery(BookQueries.class.getName() + "#flushedBookTitle(java.lang.Integer)",
                        String.class)
                .setParameter(1, 1)
                .getSingleResult();
        transaction.rollback();

        assertEquals("Pending query title", title);
    }
}
