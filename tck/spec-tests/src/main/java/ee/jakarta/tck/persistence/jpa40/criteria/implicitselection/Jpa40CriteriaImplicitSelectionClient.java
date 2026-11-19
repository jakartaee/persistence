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

package ee.jakarta.tck.persistence.jpa40.criteria.implicitselection;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests implicit selection of the single root entity when neither
 * {@code select()} nor {@code multiselect()} is called (issue #1228).
 */
public class Jpa40CriteriaImplicitSelectionClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Jpa40CriteriaImplicitSelectionClient.class.getPackageName();
        String[] classes = {packageName + ".ImplicitSelectionBook"};
        return createDeploymentJar("jpa_jpa40_criteria_implicitselection.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    @Test
    public void entityResultWithoutSelectTest() {
        assertImplicitSelection(getEntityManager().getCriteriaBuilder()
                .createQuery(ImplicitSelectionBook.class));
    }

    @Test
    public void objectResultWithoutSelectTest() {
        assertImplicitSelection(getEntityManager().getCriteriaBuilder()
                .createQuery(Object.class));
    }

    @Test
    public void untypedQueryWithoutSelectTest() {
        assertImplicitSelection(getEntityManager().getCriteriaBuilder().createQuery());
    }

    private void assertImplicitSelection(CriteriaQuery<?> criteria) {
        var em = getEntityManager();
        var cb = em.getCriteriaBuilder();
        var book = criteria.from(ImplicitSelectionBook.class);
        criteria.orderBy(cb.asc(book.get("id")));

        var results = em.createQuery(criteria).getResultList();
        assertEquals(List.of(1, 2, 3), results.stream()
                .map(result -> assertInstanceOf(ImplicitSelectionBook.class, result).getId())
                .toList());

        criteria.where(cb.equal(book.get("category"), "fiction"))
                .orderBy(cb.desc(book.get("id")));

        var filteredResults = em.createQuery(criteria).getResultList();
        assertEquals(List.of(3, 1), filteredResults.stream()
                .map(result -> assertInstanceOf(ImplicitSelectionBook.class, result).getId())
                .toList());
    }

    private void createTestData() {
        var transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new ImplicitSelectionBook(1, "fiction"));
        getEntityManager().persist(new ImplicitSelectionBook(2, "reference"));
        getEntityManager().persist(new ImplicitSelectionBook(3, "fiction"));
        transaction.commit();
        getEntityManager().clear();
    }
}
