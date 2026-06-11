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

package ee.jakarta.tck.persistence.jpa32.graph;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Graph;
import jakarta.persistence.Subgraph;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".GraphBook", packageName + ".GraphPublisher"};
        return createDeploymentJar("jpa_jpa32_graph.jar", packageName, classes);
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
     * Tests Jakarta Persistence 3.2 common {@link Graph} operations. The test
     * verifies adding, checking, retrieving, and removing attribute nodes using
     * both attribute names and metamodel attributes, and adding a subgraph from a
     * metamodel attribute.
     */
    @Test
    public void graphCommonOperationsTest() {
        EntityManager em = getEntityManager();
        EntityGraph<GraphBook> graph = em.createEntityGraph(GraphBook.class);
        Graph<GraphBook> graphView = graph;
        EntityType<GraphBook> bookType = em.getMetamodel().entity(GraphBook.class);
        SingularAttribute<? super GraphBook, String> titleAttribute =
                bookType.getSingularAttribute("title", String.class);
        SingularAttribute<? super GraphBook, GraphPublisher> publisherAttribute =
                bookType.getSingularAttribute("publisher", GraphPublisher.class);

        graphView.addAttributeNode("title");
        assertTrue(graphView.hasAttributeNode("title"));
        graphView.removeAttributeNode("title");

        AttributeNode<String> titleNode = graphView.addAttributeNode(titleAttribute);
        assertEquals("title", titleNode.getAttributeName());
        assertTrue(graphView.hasAttributeNode(titleAttribute));
        assertEquals("title", graphView.getAttributeNode(titleAttribute).getAttributeName());

        Subgraph<GraphPublisher> publisherGraph = graphView.addSubgraph(publisherAttribute);
        publisherGraph.addAttributeNode("name");
        assertEquals(GraphPublisher.class, publisherGraph.getClassType());
        assertTrue(publisherGraph.hasAttributeNode("name"));
    }

    /**
     * Tests Jakarta Persistence 3.2 entity graph element subgraph creation. The
     * test verifies creating a subgraph for collection elements by attribute
     * name and element class.
     */
    @Test
    public void graphElementSubgraphTest() {
        EntityGraph<GraphPublisher> graph = getEntityManager().createEntityGraph(GraphPublisher.class);
        Subgraph<GraphBook> booksGraph = graph.addElementSubgraph("books", GraphBook.class);
        booksGraph.addAttributeNode("title");
        assertTrue(booksGraph.hasAttributeNode("title"));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        GraphPublisher publisher = new GraphPublisher(1, "Acme");
        GraphBook book = new GraphBook(1, "Alpha", publisher);
        publisher.getBooks().add(book);
        getEntityManager().persist(publisher);
        getEntityManager().persist(book);
        transaction.commit();
        getEntityManager().clear();
    }
}
