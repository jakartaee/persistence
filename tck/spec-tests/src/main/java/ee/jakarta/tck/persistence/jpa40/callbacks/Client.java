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

package ee.jakarta.tck.persistence.jpa40.callbacks;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityAgent;
import jakarta.persistence.EntityTransaction;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".CallbackEntity",
                packageName + ".CallbackEventLog",
                packageName + ".AnnotatedCallbackListener",
                packageName + ".PackageCallbackListener",
                packageName + ".package-info"};
        return createDeploymentJar("jpa_jpa40_callbacks.jar", packageName, classes);
    }

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        createDeployment();
        getEntityManager();
        removeTestData();
        CallbackEventLog.reset();
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
     * Tests Jakarta Persistence 4.0 lifecycle callbacks. The test verifies
     * {@code @PreMerge}, {@code @PreInsert}, {@code @PostInsert},
     * {@code @PreUpsert}, {@code @PostUpsert}, {@code @PreDelete}, and
     * {@code @PostDelete} callbacks using entity manager and entity agent
     * operations.
     */
    @Test
    public void newLifecycleCallbacksTest() {
        EntityAgent agent = getEntityManagerFactory().createEntityAgent();
        EntityTransaction agentTransaction = agent.getTransaction();
        try {
            agentTransaction.begin();
            agent.insert(new CallbackEntity(1, "inserted"));
            agentTransaction.commit();

            agentTransaction.begin();
            agent.upsert(new CallbackEntity(1, "upserted"));
            agentTransaction.commit();

            agentTransaction.begin();
            agent.delete(agent.get(CallbackEntity.class, 1));
            agentTransaction.commit();
        } finally {
            if (agentTransaction.isActive()) {
                agentTransaction.rollback();
            }
            agent.close();
        }

        EntityTransaction transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().merge(new CallbackEntity(2, "merged"));
        transaction.commit();

        List<String> events = CallbackEventLog.events();
        assertTrue(events.contains("entity-pre-insert"));
        assertTrue(events.contains("entity-post-insert"));
        assertTrue(events.contains("entity-pre-upsert"));
        assertTrue(events.contains("entity-post-upsert"));
        assertTrue(events.contains("entity-pre-delete"));
        assertTrue(events.contains("entity-post-delete"));
        assertTrue(events.contains("entity-pre-merge"));
    }

    /**
     * Tests Jakarta Persistence 4.0 listener invocation from annotations. The
     * test verifies callbacks from a package-declared listener and from an
     * {@code @EntityListener}-annotated listener class are invoked for a
     * database insert, including multiple listener callback methods of the same
     * lifecycle type with different parameter types.
     */
    @Test
    public void entityListenerInvocationTest() {
        EntityAgent agent = getEntityManagerFactory().createEntityAgent();
        EntityTransaction transaction = agent.getTransaction();
        try {
            transaction.begin();
            agent.insert(new CallbackEntity(3, "listener"));
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            agent.close();
        }

        List<String> events = CallbackEventLog.events();
        assertTrue(events.contains("package-pre-insert"));
        assertTrue(events.contains("annotated-post-insert-callback-entity"));
        assertTrue(events.contains("annotated-post-insert-object"));
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
