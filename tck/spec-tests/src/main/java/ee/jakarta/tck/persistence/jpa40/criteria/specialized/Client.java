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
import jakarta.persistence.criteria.NumericExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.TemporalExpression;
import jakarta.persistence.criteria.TextExpression;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SpecializedBook(
                1, "Alpha", 10, LocalDate.of(2026, 5, 10), true, "fiction"));
        getEntityManager().persist(new SpecializedBook(
                2, "Beta", 3, LocalDate.of(2025, 1, 1), false, "reference"));
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
