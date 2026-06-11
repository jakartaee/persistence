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

package ee.jakarta.tck.persistence.jpa40.namedgraph;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Subgraph;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".GraphRoot",
                packageName + ".GraphChild",
                packageName + ".SpecialGraphChild",
                packageName + ".GraphDetail"};
        return createDeploymentJar("jpa_jpa40_namedgraph.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 distributed named entity graph annotations.
     * The test verifies the provider builds a usable named entity graph from
     * {@code @NamedEntityGraph.AttributeNode} and
     * {@code @NamedEntityGraph.Subgraph} declarations by applying that graph to
     * a find operation and checking the graph-selected associations are loaded.
     */
    @Test
    public void namedEntityGraphAttributeNodeAndSubgraphAnnotationsTest() {
        EntityGraph<GraphRoot> graph = getEntityManager().getEntityGraph(GraphRoot.class, GraphRoot.GRAPH);
        assertTrue(graph.hasAttributeNode("child"));
        assertTrue(graph.hasAttributeNode("name"));
        assertEquals(GraphRoot.class, graph.getGraphedType().getJavaType());
        assertEquals("child", graph.getAttributeNode("child").getAttribute().getName());
        assertEquals(1, graph.getAttributeNode("child").getSubgraphs().size());
        Subgraph<?> subgraph = graph.getAttributeNode("child").getSubgraphs().get(GraphChild.class);
        assertNotNull(subgraph);
        assertEquals(GraphChild.class, subgraph.getGraphedType().getJavaType());
        assertTrue(subgraph.hasAttributeNode("graphDetail"));
        assertEquals("graphDetail", subgraph.getAttributeNode("graphDetail").getAttribute().getName());
        assertFalse(subgraph.hasAttributeNode("detail"));

        EntityGraph<GraphChild> childGraph = getEntityManager().getEntityGraph(GraphChild.class, GraphChild.GRAPH);
        assertEquals(GraphChild.class, childGraph.getGraphedType().getJavaType());
        assertTrue(childGraph.hasAttributeNode("graphDetail"));
        assertFalse(childGraph.hasAttributeNode("detail"));
    }

    /**
     * Tests Jakarta Persistence 4.0 distributed named entity graph annotations.
     * The test verifies the provider builds a usable named entity graph from
     * {@code @NamedEntityGraph.AttributeNode} and
     * {@code @NamedEntityGraph.Subgraph} declarations by applying that graph to
     * a find operation and checking the graph-selected associations are loaded.
     */
    @Test
    public void namedEntityGraphAttributeNodeAndSubgraphAnnotationsExecutionTest() {
        GraphRoot plain = getEntityManager().find(GraphRoot.class, 1);
        assertFalse(Persistence.getPersistenceUtil().isLoaded(plain, "child")
                && Persistence.getPersistenceUtil().isLoaded(plain.getChild()));

        getEntityManager().clear();
        EntityGraph<GraphRoot> graph = getEntityManager().getEntityGraph(GraphRoot.class, GraphRoot.GRAPH);
        GraphRoot graphed = getEntityManager().find(graph, 1);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed, "child"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild()));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild(), "graphDetail"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild().getGraphDetail()));
    }

    /**
     * Tests Jakarta Persistence 4.0 graph metadata and runtime subgraph
     * mutation. The test verifies {@link EntityGraph#getGraphedType()},
     * {@link AttributeNode#getAttribute()}, {@link AttributeNode#addSubgraph()},
     * and {@link AttributeNode#addTreatedSubgraph(Class)}.
     */
    @Test
    public void runtimeAttributeNodeSubgraphMetadataAndExecutionTest() {
        EntityGraph<GraphRoot> graph = getEntityManager().createEntityGraph(GraphRoot.class);
        assertEquals(GraphRoot.class, graph.getGraphedType().getJavaType());

        AttributeNode<GraphChild> childNode = graph.addAttributeNode("child");
        assertEquals("child", childNode.getAttributeName());
        assertEquals("child", childNode.getAttribute().getName());

        Subgraph<GraphChild> childSubgraph = childNode.addSubgraph();
        assertEquals(GraphChild.class, childSubgraph.getClassType());
        assertEquals(GraphChild.class, childSubgraph.getGraphedType().getJavaType());

        AttributeNode<GraphDetail> detailNode = childSubgraph.addAttributeNode("graphDetail");
        assertEquals("graphDetail", detailNode.getAttributeName());
        assertEquals("graphDetail", detailNode.getAttribute().getName());
        assertTrue(childNode.getSubgraphs().containsKey(GraphChild.class));

        Subgraph<SpecialGraphChild> treatedSubgraph = childNode.addTreatedSubgraph(SpecialGraphChild.class);
        treatedSubgraph.addAttributeNode("specialDetail");
        assertEquals(SpecialGraphChild.class, treatedSubgraph.getClassType());
        assertEquals(SpecialGraphChild.class, treatedSubgraph.getGraphedType().getJavaType());
        assertTrue(treatedSubgraph.hasAttributeNode("specialDetail"));
        assertTrue(childNode.getSubgraphs().containsKey(SpecialGraphChild.class));

        GraphRoot root = getEntityManager().find(graph, 1);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(root, "child"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(root.getChild()));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(root.getChild(), "graphDetail"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(root.getChild().getGraphDetail()));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        GraphDetail detail = new GraphDetail(1, "detail");
        GraphChild child = new GraphChild(1, "child", detail);
        getEntityManager().persist(detail);
        getEntityManager().persist(child);
        getEntityManager().persist(new GraphRoot(1, "root", child));
        transaction.commit();
        getEntityManager().clear();
    }
}
