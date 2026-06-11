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

package ee.jakarta.tck.persistence.jpa40.fetchoption;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.BatchSize;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FetchOption;
import jakarta.persistence.FetchType;
import jakarta.persistence.Persistence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static jakarta.persistence.Persistence.getPersistenceUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {packageName + ".FetchOptionBook", packageName + ".FetchOptionPublisher"};
        return createDeploymentJar("jpa_jpa40_fetchoption.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
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
     * Tests Jakarta Persistence 4.0 fetch options on entity graph attribute
     * nodes. The test verifies {@link AttributeNode#addOption(FetchOption)}
     * affects provider fetch behavior by forcing an eager to-one mapping to be
     * treated as lazy for one find operation and then explicitly eager for a
     * second find operation.
     */
    @Test
    public void attributeNodeFetchOptionTest() {
        EntityGraph<FetchOptionBook> lazyGraph = getEntityManager().createEntityGraph(FetchOptionBook.class);
        lazyGraph.addAttributeNode("publisher").addOption(FetchType.LAZY);

        FetchOptionBook lazyBook = getEntityManager().find(lazyGraph, 1);
        assertFalse(getPersistenceUtil().isLoaded(lazyBook, "publisher"));

        getEntityManager().clear();

        EntityGraph<FetchOptionBook> eagerGraph = getEntityManager().createEntityGraph(FetchOptionBook.class);
        eagerGraph.addAttributeNode("publisher").addOption(FetchType.EAGER);

        FetchOptionBook eagerBook = getEntityManager().find(eagerGraph, 1);
        assertTrue(getPersistenceUtil().isLoaded(eagerBook, "publisher"));
        assertTrue(getPersistenceUtil().isLoaded(eagerBook.getPublisher()));
    }

    /**
     * Tests Jakarta Persistence 4.0 fetch options declared directly on a
     * mapped attribute. The test verifies an unqualified {@code @Fetch}
     * annotation applies to a plain find operation without an entity graph.
     */
    @Test
    public void fetchAnnotationWithoutGraphTest() {
        FetchOptionBook book = getEntityManager().find(FetchOptionBook.class, 1);

        assertTrue(getPersistenceUtil().isLoaded(book, "defaultFetchPublisher"));
        assertTrue(getPersistenceUtil().isLoaded(book.getDefaultFetchPublisher()));
    }

    /**
     * Tests Jakarta Persistence 4.0 fetch options declared directly on a
     * mapped attribute. The test verifies an unqualified {@code @Fetch}
     * annotation overrides the mapping fetch type when no entity graph is used.
     */
    @Test
    public void fetchAnnotationLazyWithoutGraphTest() {
        FetchOptionBook book = getEntityManager().find(FetchOptionBook.class, 1);

        assertFalse(getPersistenceUtil().isLoaded(book, "lazyFetchPublisher")
                && getPersistenceUtil().isLoaded(book.getLazyFetchPublisher()));
    }

    /**
     * Tests Jakarta Persistence 4.0 fetch option metadata on entity graph
     * attribute nodes. The test verifies default options for added and
     * removed nodes, option replacement, {@link BatchSize}, and defensive
     * option set semantics. Note that {@code addOption()} overwrites
     * existing options of the same type.
     */
    @Test
    public void attributeNodeFetchOptionMetadataTest() {
        EntityGraph<FetchOptionBook> graph = getEntityManager().createEntityGraph(FetchOptionBook.class);
        AttributeNode<FetchOptionPublisher> publisher = graph.addAttributeNode("publisher");

        assertEquals("publisher", publisher.getAttributeName());
        assertEquals("publisher", publisher.getAttribute().getName());
        assertTrue(publisher.getOptions().contains(FetchType.EAGER));

        BatchSize batchSize = new BatchSize(5);
        publisher.addOption(batchSize);
        publisher.addOption(FetchType.LAZY); // overwrite

        Set<FetchOption> options = publisher.getOptions();
        assertTrue(options.contains(batchSize));
        assertTrue(options.contains(FetchType.LAZY));
        assertFalse(options.contains(FetchType.EAGER));

        // mutation of the Set should not affect the original options
        options.clear();
        Set<FetchOption> fetchOptions = publisher.getOptions();
        assertTrue(fetchOptions.contains(batchSize));
        assertTrue(fetchOptions.contains(FetchType.LAZY));
        assertFalse(fetchOptions.contains(FetchType.EAGER));

        publisher.addOption(new BatchSize(10));
        assertTrue(publisher.getOptions().contains(new BatchSize(10)));
        assertFalse(publisher.getOptions().contains(new BatchSize(5)));
    }

    /**
     * Tests that removing a node (that was not added) from an entity graph
     * is the same as setting the option {@code FetchType.LAZY}.
     */
    @Test
    public void attributeNodeRemoveTest() {
        EntityGraph<FetchOptionBook> removedGraph =
                getEntityManager().createEntityGraph(FetchOptionBook.class);
        removedGraph.removeAttributeNode("publisher");
        AttributeNode<?> removedPublisher = removedGraph.getAttributeNode("publisher");
        assertEquals("publisher", removedPublisher.getAttribute().getName());
        assertTrue(removedPublisher.getOptions().contains(FetchType.LAZY));
    }

    /**
     * Tests Jakarta Persistence 4.0 removed graph nodes. The test verifies
     * removing an eager to-one attribute node suppresses its default eager
     * fetch when the graph is used as a load graph.
     */
    @Test
    public void removedAttributeNodeSuppressesEagerFetchTest() {
        EntityGraph<FetchOptionBook> graph =
                getEntityManager().createEntityGraph(FetchOptionBook.class);
        graph.removeAttributeNode("publisher");
        FetchOptionBook book = getEntityManager().find(graph, 1);
        assertFalse(getPersistenceUtil().isLoaded(book, "publisher")
                && getPersistenceUtil().isLoaded(book.getPublisher()));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        FetchOptionPublisher publisher = new FetchOptionPublisher(1, "Publisher");
        FetchOptionPublisher defaultFetchPublisher = new FetchOptionPublisher(2, "Default Fetch Publisher");
        FetchOptionPublisher lazyFetchPublisher = new FetchOptionPublisher(3, "Lazy Fetch Publisher");
        getEntityManager().persist(publisher);
        getEntityManager().persist(defaultFetchPublisher);
        getEntityManager().persist(lazyFetchPublisher);
        getEntityManager().persist(new FetchOptionBook(1, "Alpha", publisher,
                defaultFetchPublisher, lazyFetchPublisher));
        transaction.commit();
        getEntityManager().clear();
    }
}
