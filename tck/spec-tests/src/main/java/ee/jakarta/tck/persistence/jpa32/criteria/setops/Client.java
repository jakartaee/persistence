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

package ee.jakarta.tck.persistence.jpa32.criteria.setops;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.Root;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".SetOpsBook"};
        return createDeploymentJar("jpa_jpa32_criteria_setops.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API set operation support. The test
     * verifies that {@code union}, {@code intersect}, and {@code except} combine
     * compatible criteria select queries with the expected set semantics.
     */
    @Test
    public void criteriaSelectSetOperationsTest() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> first = cb.createQuery(String.class);
        Root<SetOpsBook> firstBook = first.from(SetOpsBook.class);
        first.select(firstBook.get("title"))
                .where(cb.le(firstBook.get("quantity"), 20));

        CriteriaQuery<String> second = cb.createQuery(String.class);
        Root<SetOpsBook> secondBook = second.from(SetOpsBook.class);
        second.select(secondBook.get("title"))
                .where(secondBook.get("title").in(List.of("Beta", "Gamma")));

        CriteriaSelect<String> union = cb.union(first, second);
        assertEquals(Set.of("Alpha", "Beta", "Gamma"),
                new HashSet<>(getEntityManager().createQuery(union).getResultList()));

        CriteriaSelect<String> intersect = cb.intersect(first, second);
        assertEquals(Set.of("Beta"),
                new HashSet<>(getEntityManager().createQuery(intersect).getResultList()));

        CriteriaSelect<String> except = cb.except(first, second);
        assertEquals(Set.of("Alpha"),
                new HashSet<>(getEntityManager().createQuery(except).getResultList()));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new SetOpsBook(1, "Alpha", 10));
        getEntityManager().persist(new SetOpsBook(2, "Beta", 20));
        getEntityManager().persist(new SetOpsBook(3, "Gamma", 30));
        transaction.commit();
        getEntityManager().clear();
    }
}
