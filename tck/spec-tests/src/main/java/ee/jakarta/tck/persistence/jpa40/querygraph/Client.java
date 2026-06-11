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

package ee.jakarta.tck.persistence.jpa40.querygraph;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".QueryGraphBook",
                packageName + ".QueryGraphPublisher",
                packageName + ".QueryGraphAuthor"};
        return createDeploymentJar("jpa_jpa40_querygraph.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 query/entity graph integration. The test
     * verifies provider execution of a named query with an entity graph
     * specified in the declaring annotation.
     */
    @Test
    public void namedQueryEntityGraphTest() {
        QueryGraphBook namedQuery = getEntityManager()
                .createNamedQuery(QueryGraphBook.QUERY, QueryGraphBook.class)
                .getSingleResult();
        assertTrue(Persistence.getPersistenceUtil().isLoaded(namedQuery, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(namedQuery.getPublisher()));
    }

    /**
     * Tests Jakarta Persistence 4.0 query/entity graph integration. The test
     * verifies typed {@code getEntityGraph(Class, String)}, mutability of
     * returned named graphs, and {@code createQuery(String, EntityGraph)}.
     */
    @Test
    public void namedEntityGraphNoMutationTest() {
        // get a copy of a named graph
        var throwawayGraph = getEntityManager().getEntityGraph(QueryGraphBook.class, QueryGraphBook.GRAPH);
        assertTrue(throwawayGraph.hasAttributeNode("publisher"));
        assertFalse(throwawayGraph.hasAttributeNode("title"));
        assertFalse(throwawayGraph.hasAttributeNode("authors"));

        // mutate the returned graph
        throwawayGraph.addAttributeNode("title");
        throwawayGraph.addAttributeNode("authors");
        assertTrue(throwawayGraph.hasAttributeNode("title"));
        assertTrue(throwawayGraph.hasAttributeNode("authors"));

        // check we did not mutate the underlying graph
        var graph = getEntityManager().getEntityGraph(QueryGraphBook.class, QueryGraphBook.GRAPH);
        assertTrue(graph.hasAttributeNode("publisher"));
        assertFalse(graph.hasAttributeNode("title"));
        assertFalse(graph.hasAttributeNode("authors"));

        var fromTypedQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40QueryGraphBook b WHERE b.id = 1", graph)
                .getSingleResult();
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "title"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery.getPublisher()));
        assertFalse(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "authors"));
        assertEquals(1, fromTypedQuery.getAuthors().size());
        assertEquals("Alpha", fromTypedQuery.getTitle());

        getEntityManager().clear();

        var fromQueryWithGraph = getEntityManager()
                .createQuery("SELECT b FROM Jpa40QueryGraphBook b WHERE b.id = 1")
                .withEntityGraph(graph)
                .getSingleResult();
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "title"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph.getPublisher()));
        assertFalse(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "authors"));
        assertEquals(1, fromQueryWithGraph.getAuthors().size());
        assertEquals("Alpha", fromQueryWithGraph.getTitle());
    }

    /**
     * Tests Jakarta Persistence 4.0 query/entity graph integration. The test
     * verifies typed {@code getEntityGraph(Class, String)}, mutability of
     * returned named graphs, and {@code createQuery(String, EntityGraph)}.
     */
    @Test
    public void namedEntityGraphMutationTest() {
        // get a copy of a named graph
        var graph = getEntityManager().getEntityGraph(QueryGraphBook.class, QueryGraphBook.GRAPH);
        assertTrue(graph.hasAttributeNode("publisher"));
        assertFalse(graph.hasAttributeNode("title"));
        assertFalse(graph.hasAttributeNode("authors"));

        // mutate the returned graph
        graph.addAttributeNode("title");
        graph.addAttributeNode("authors");
        assertTrue(graph.hasAttributeNode("title"));
        assertTrue(graph.hasAttributeNode("authors"));

        var fromTypedQuery = getEntityManager()
                .createQuery("SELECT b FROM Jpa40QueryGraphBook b WHERE b.id = 1", graph)
                .getSingleResult();
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "title"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery.getPublisher()));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromTypedQuery, "authors"));
        assertEquals(1, fromTypedQuery.getAuthors().size());
        assertEquals("Alpha", fromTypedQuery.getTitle());

        getEntityManager().clear();

        var fromQueryWithGraph = getEntityManager()
                .createQuery("SELECT b FROM Jpa40QueryGraphBook b WHERE b.id = 1")
                .withEntityGraph(graph)
                .getSingleResult();
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "title"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph.getPublisher()));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(fromQueryWithGraph, "authors"));
        assertEquals(1, fromQueryWithGraph.getAuthors().size());
        assertEquals("Alpha", fromQueryWithGraph.getTitle());
    }

    private void createTestData() {
        var transaction = getEntityTransaction();
        transaction.begin();
        var publisher = new QueryGraphPublisher(1, "Publisher");
        var author = new QueryGraphAuthor(1, "Author");
        var book = new QueryGraphBook(1, "Alpha", publisher);
        book.addAuthor(author);
        getEntityManager().persist(publisher);
        getEntityManager().persist(author);
        getEntityManager().persist(book);
        transaction.commit();
        getEntityManager().clear();
    }
}
