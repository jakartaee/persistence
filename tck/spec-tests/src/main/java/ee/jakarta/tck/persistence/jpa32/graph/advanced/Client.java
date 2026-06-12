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

package ee.jakarta.tck.persistence.jpa32.graph.advanced;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".GraphAdvancedAuthor",
                packageName + ".GraphAdvancedBook",
                packageName + ".GraphAdvancedLibrary",
                packageName + ".GraphAdvancedPublication",
                packageName + ".GraphAdvancedStaffAuthor"
        };
        return createDeploymentJar("jpa_jpa32_graph_advanced.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
    }

    /**
     * Tests Jakarta Persistence 3.2 {@link EntityGraph} additions for building
     * treated subgraphs from metamodel attributes. The test verifies treated
     * subgraphs for a singular attribute, treated element subgraphs for a list
     * attribute, and treated map-key subgraphs for a map attribute.
     */
    @Test
    public void graphTreatedSubgraphsAndMapKeySubgraphTest() {
        EntityGraph<GraphAdvancedLibrary> graph =
                getEntityManager().createEntityGraph(GraphAdvancedLibrary.class);
        EntityType<GraphAdvancedLibrary> libraryType =
                getEntityManager().getMetamodel().entity(GraphAdvancedLibrary.class);

        SingularAttribute<? super GraphAdvancedLibrary, GraphAdvancedPublication> featuredPublication =
                libraryType.getSingularAttribute("featuredPublication", GraphAdvancedPublication.class);
        Subgraph<GraphAdvancedBook> featuredBookGraph =
                graph.addTreatedSubgraph(featuredPublication, GraphAdvancedBook.class);
        featuredBookGraph.addAttributeNode("isbn");
        assertEquals(GraphAdvancedBook.class, featuredBookGraph.getClassType());
        assertTrue(featuredBookGraph.hasAttributeNode("isbn"));

        ListAttribute<? super GraphAdvancedLibrary, GraphAdvancedPublication> publications =
                libraryType.getList("publications", GraphAdvancedPublication.class);
        Subgraph<GraphAdvancedBook> publicationBookGraph =
                graph.addTreatedElementSubgraph(publications, GraphAdvancedBook.class);
        publicationBookGraph.addAttributeNode("isbn");
        assertEquals(GraphAdvancedBook.class, publicationBookGraph.getClassType());
        assertTrue(publicationBookGraph.hasAttributeNode("isbn"));

        MapAttribute<? super GraphAdvancedLibrary, GraphAdvancedAuthor, GraphAdvancedPublication> selections =
                libraryType.getMap("selections", GraphAdvancedAuthor.class, GraphAdvancedPublication.class);
        Subgraph<GraphAdvancedStaffAuthor> keyGraph =
                graph.addTreatedMapKeySubgraph(selections, GraphAdvancedStaffAuthor.class);
        keyGraph.addAttributeNode("department");
        assertEquals(GraphAdvancedStaffAuthor.class, keyGraph.getClassType());
        assertTrue(keyGraph.hasAttributeNode("department"));
    }
}
