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

package ee.jakarta.tck.persistence.jpa32.metamodel;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.StaticMetamodel;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Generated;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".MetaBook",
                packageName + ".MetaBook_",
                packageName + ".MetaPublisher"
        };
        return createDeploymentJar("jpa_jpa32_metamodel.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 metamodel lookup by entity name. The test
     * verifies that {@code Metamodel.entity(String)} resolves the entity type
     * associated with the supplied entity name.
     */
    @Test
    public void metamodelEntityNameTest() {
        EntityType<?> entityByName = getEntityManager().getMetamodel().entity("Jpa32MetaBook");
        assertEquals(MetaBook.class, entityByName.getJavaType());
    }

    /**
     * Tests Jakarta Persistence 3.2 entity manager factory metadata accessors.
     * The test verifies typed named query references from
     * {@code getNamedQueries(Class)} and named entity graph lookup from
     * {@code getNamedEntityGraphs(Class)}.
     */
    @Test
    public void factoryNamedQueriesAndGraphsTest() {
        EntityManager em = getEntityManager();
        EntityManagerFactory emf = em.getEntityManagerFactory();
        Map<String, TypedQueryReference<MetaBook>> namedQueries =
                emf.getNamedQueries(MetaBook.class);
        TypedQueryReference<MetaBook> queryReference =
                namedQueries.get(MetaBook.QUERY_BY_CATEGORY);
        assertNotNull(queryReference);
        List<Integer> fictionIds = em.createQuery(queryReference)
                .setParameter("category", "fiction")
                .getResultList()
                .stream()
                .map(MetaBook::getId)
                .collect(Collectors.toList());
        assertIterableEquals(List.of(1, 2), fictionIds);

        Map<String, EntityGraph<? extends MetaBook>> namedGraphs =
                emf.getNamedEntityGraphs(MetaBook.class);
        assertTrue(namedGraphs.containsKey(MetaBook.GRAPH_WITH_PUBLISHER));
    }

    /**
     * Tests Jakarta Persistence 3.2 static metamodel generation requirements.
     * The test verifies generated constants for attributes, named queries, named
     * graphs, result set mappings, and the presence of the generated class
     * annotation and metamodel fields.
     */
    @Test
    public void staticMetamodelConstantsAndGeneratedAnnotationTest() {
        StaticMetamodel staticMetamodel = MetaBook_.class.getAnnotation(StaticMetamodel.class);
        assertNotNull(staticMetamodel);
        assertEquals(MetaBook.class, staticMetamodel.value());
        assertNotNull(MetaBook_.class_);
        assertNotNull(MetaBook_.title);
        assertNotNull(MetaBook_.publisher);
        assertEquals("title", MetaBook_.TITLE);
        assertEquals("category", MetaBook_.CATEGORY);
        assertEquals(MetaBook.QUERY_BY_CATEGORY, MetaBook_.QUERY_JPA32_META_BOOK_FIND_BY_CATEGORY);
        assertEquals(MetaBook.GRAPH_WITH_PUBLISHER, MetaBook_.GRAPH_JPA32_META_BOOK_WITH_PUBLISHER);
        assertEquals(MetaBook.MAPPING_TITLE, MetaBook_.MAPPING_JPA32_META_BOOK_TITLE_MAPPING);
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        MetaPublisher acme = new MetaPublisher(1, "Acme");
        MetaPublisher orbit = new MetaPublisher(2, "Orbit");
        getEntityManager().persist(acme);
        getEntityManager().persist(orbit);
        getEntityManager().persist(new MetaBook(1, "Alpha", "fiction", acme));
        getEntityManager().persist(new MetaBook(2, "Beta", "fiction", orbit));
        getEntityManager().persist(new MetaBook(3, "Gamma", "tech", acme));
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
