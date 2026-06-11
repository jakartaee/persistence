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

package ee.jakarta.tck.persistence.jpa40.entitytypegraph;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.metamodel.EntityType;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".EntityTypeGraphBook"};
        return createDeploymentJar("jpa_jpa40_entitytypegraph.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
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
     * Tests Jakarta Persistence 4.0 entity graph access from the metamodel.
     * The test verifies {@link EntityType#createEntityGraph()} creates a
     * mutable graph rooted at the entity type and
     * {@link EntityType#getNamedEntityGraphs()} exposes named graphs for that
     * entity type.
     */
    @Test
    public void entityTypeCreateAndListEntityGraphsTest() {
        EntityType<EntityTypeGraphBook> entityType =
                getEntityManager().getMetamodel().entity(EntityTypeGraphBook.class);

        EntityGraph<EntityTypeGraphBook> graph = entityType.createEntityGraph();
        graph.addAttributeNode("title");
        assertEquals(EntityTypeGraphBook.class, graph.getGraphedType().getJavaType());
        assertTrue(graph.hasAttributeNode("title"));
        assertEquals("title", graph.getAttributeNode("title").getAttribute().getName());

        Map<String, EntityGraph<EntityTypeGraphBook>> namedGraphs = entityType.getNamedEntityGraphs();
        assertTrue(namedGraphs.containsKey(EntityTypeGraphBook.GRAPH));
        assertEquals(EntityTypeGraphBook.class,
                namedGraphs.get(EntityTypeGraphBook.GRAPH).getGraphedType().getJavaType());
        assertTrue(namedGraphs.get(EntityTypeGraphBook.GRAPH).hasAttributeNode("title"));
    }
}
