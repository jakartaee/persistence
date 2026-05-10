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
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FetchOption;
import jakarta.persistence.FetchType;
import jakarta.persistence.Persistence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertFalse(Persistence.getPersistenceUtil().isLoaded(lazyBook, "publisher"));

        getEntityManager().clear();

        EntityGraph<FetchOptionBook> eagerGraph = getEntityManager().createEntityGraph(FetchOptionBook.class);
        eagerGraph.addAttributeNode("publisher").addOption(FetchType.EAGER);

        FetchOptionBook eagerBook = getEntityManager().find(eagerGraph, 1);
        assertTrue(Persistence.getPersistenceUtil().isLoaded(eagerBook, "publisher"));
        assertTrue(Persistence.getPersistenceUtil().isLoaded(eagerBook.getPublisher()));
    }

    private void createTestData() {
        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        FetchOptionPublisher publisher = new FetchOptionPublisher(1, "Publisher");
        getEntityManager().persist(publisher);
        getEntityManager().persist(new FetchOptionBook(1, "Alpha", publisher));
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
