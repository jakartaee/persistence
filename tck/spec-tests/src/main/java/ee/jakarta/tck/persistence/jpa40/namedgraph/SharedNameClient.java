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
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Subgraph;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SharedNameClient extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = SharedNameClient.class.getPackageName();
        String[] classes = {
                packageName + ".SameNameGraphRoot",
                packageName + ".SameNameGraphChild",
                packageName + ".SameNameGraphDetail"};
        return createDeploymentJar("jpa_jpa40_namedgraph_sharedname.jar", packageName, classes);
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
     * Tests Jakarta Persistence 4.0 distributed named entity graph annotations
     * where the root graph and its subgraph contributions share the same name.
     */
    @Test
    public void namedEntityGraphSameNamedSubgraphAnnotationsTest() {
        EntityGraph<SameNameGraphRoot> graph =
                getEntityManager().getEntityGraph(SameNameGraphRoot.class, SameNameGraphRoot.GRAPH);

        assertEquals(SameNameGraphRoot.class, graph.getGraphedType().getJavaType());
        assertTrue(graph.hasAttributeNode("child"));
        assertTrue(graph.hasAttributeNode("name"));
        assertEquals("child", graph.getAttributeNode("child").getAttribute().getName());
        assertEquals(1, graph.getAttributeNode("child").getSubgraphs().size());

        Subgraph<?> subgraph = graph.getAttributeNode("child").getSubgraphs().get(SameNameGraphChild.class);
        assertNotNull(subgraph);
        assertEquals(SameNameGraphChild.class, subgraph.getGraphedType().getJavaType());
        assertTrue(subgraph.hasAttributeNode("graphDetail"));
        assertEquals("graphDetail", subgraph.getAttributeNode("graphDetail").getAttribute().getName());
        assertFalse(subgraph.hasAttributeNode("detail"));
    }

    /**
     * Tests Jakarta Persistence 4.0 distributed named entity graph annotations
     * where the root graph and its subgraph contributions share the same name by
     * applying the graph to a find operation.
     */
    @Test
    public void namedEntityGraphSameNamedSubgraphAnnotationsExecutionTest() {
        SameNameGraphRoot plain = getEntityManager().find(SameNameGraphRoot.class, 1);
        assertFalse(Persistence.getPersistenceUtil().isLoaded(plain, "child")
                && Persistence.getPersistenceUtil().isLoaded(plain.getChild()));

        getEntityManager().clear();
        EntityGraph<SameNameGraphRoot> graph =
                getEntityManager().getEntityGraph(SameNameGraphRoot.class, SameNameGraphRoot.GRAPH);
        SameNameGraphRoot graphed = getEntityManager().find(graph, 1);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed, "child"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild()));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild(), "graphDetail"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(graphed.getChild().getGraphDetail()));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        SameNameGraphDetail detail = new SameNameGraphDetail(1, "detail");
        SameNameGraphChild child = new SameNameGraphChild(1, "child", detail);
        getEntityManager().persist(detail);
        getEntityManager().persist(child);
        getEntityManager().persist(new SameNameGraphRoot(1, "root", child));
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
