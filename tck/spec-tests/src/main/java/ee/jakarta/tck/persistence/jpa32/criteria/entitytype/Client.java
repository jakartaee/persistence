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

package ee.jakarta.tck.persistence.jpa32.criteria.entitytype;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.EntityType;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".EntityTypeBook", packageName + ".EntityTypePublisher"};
        return createDeploymentJar("jpa_jpa32_criteria_entitytype.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        removeTestData();
        createTestData();
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API overloads that accept
     * {@link EntityType} objects. The test verifies using an {@code EntityType}
     * to create roots for both a top-level query and a subquery.
     */
    @Test
    public void subqueryEntityTypeTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        EntityType<EntityTypeBook> bookType = em.getMetamodel().entity(EntityTypeBook.class);

        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<EntityTypeBook> book = criteria.from(bookType);
        Subquery<EntityTypeBook> subquery = criteria.subquery(bookType);
        Root<EntityTypeBook> subBook = subquery.from(bookType);
        subquery.select(subBook)
                .where(cb.and(List.of(
                        subBook.get("id").equalTo(book.get("id")),
                        subBook.get("category").equalTo("tech"))));
        criteria.select(book.get("title"))
                .where(cb.exists(subquery));

        assertEquals("Gamma", em.createQuery(criteria).getSingleResult());
    }

    /**
     * Tests Jakarta Persistence 3.2 Criteria API joins using metamodel
     * {@link EntityType} objects. The test verifies joining from an entity root
     * to another entity type and applying an {@code ON} predicate to that join.
     */
    @Test
    public void joinEntityTypeTest() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        EntityType<EntityTypeBook> bookType = em.getMetamodel().entity(EntityTypeBook.class);
        EntityType<EntityTypePublisher> publisherType = em.getMetamodel().entity(EntityTypePublisher.class);

        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<EntityTypeBook> book = criteria.from(bookType);
        Join<EntityTypeBook, EntityTypePublisher> publisher = book.join(publisherType);
        publisher.on(cb.equal(book.get("publisher"), publisher));
        criteria.select(book.get("title"))
                .where(publisher.get("name").equalTo("Acme"))
                .orderBy(cb.asc(book.get("id")));

        assertIterableEquals(List.of("Alpha", "Gamma"), em.createQuery(criteria).getResultList());
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        EntityTypePublisher acme = new EntityTypePublisher(1, "Acme");
        EntityTypePublisher orbit = new EntityTypePublisher(2, "Orbit");
        getEntityManager().persist(acme);
        getEntityManager().persist(orbit);
        getEntityManager().persist(new EntityTypeBook(1, "Alpha", "fiction", acme));
        getEntityManager().persist(new EntityTypeBook(2, "Beta", "fiction", orbit));
        getEntityManager().persist(new EntityTypeBook(3, "Gamma", "tech", acme));
        transaction.commit();
        getEntityManager().clear();
    }
}
