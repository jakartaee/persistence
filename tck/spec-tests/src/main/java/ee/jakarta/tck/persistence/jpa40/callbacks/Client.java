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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Client extends PMClientBase {

    public JavaArchive createDeployment() throws Exception {
        String packageName = Client.class.getPackageName();
        String[] classes = {
                packageName + ".CallbackEntity",
                packageName + ".CallbackDerivedEntity",
                packageName + ".CallbackEventLog",
                packageName + ".CallbackMappedSuperclass",
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

    /**
     * Tests Jakarta Persistence 4.0 lifecycle callbacks. The test verifies
     * {@code @PreMerge}, {@code @PreInsert}, {@code @PostInsert},
     * {@code @PreUpsert}, {@code @PostUpsert}, {@code @PreDelete}, and
     * {@code @PostDelete} callbacks using entity manager and entity agent
     * operations.
     */
    @Test
    public void newLifecycleCallbacksTest() {
        var agent = getEntityManagerFactory().createEntityAgent();
        var agentTransaction = agent.getTransaction();
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

        var transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().merge(new CallbackEntity(2, "merged"));
        transaction.commit();

        var events = CallbackEventLog.events();
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
        var agent = getEntityManagerFactory().createEntityAgent();
        var transaction = agent.getTransaction();
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

        var events = CallbackEventLog.events();
        assertTrue(events.contains("package-pre-insert"));
        assertTrue(events.contains("annotated-pre-insert-callback-entity"));
        assertTrue(events.contains("annotated-post-insert-callback-entity"));
        assertTrue(events.contains("annotated-post-insert-object"));
        assertEventOrder(events, "annotated-pre-insert-callback-entity", "package-pre-insert");
    }

    /**
     * Verifies the Jakarta Persistence 4.0 database update callbacks are
     * invoked for updates performed through an {@link EntityAgent}.
     */
    @Test
    public void entityAgentUpdateLifecycleCallbacksTest() {
        createCallbackEntity(new CallbackEntity(4, "original"));
        CallbackEventLog.reset();

        var agent = getEntityManagerFactory().createEntityAgent();
        var transaction = agent.getTransaction();
        try {
            var entity = agent.get(CallbackEntity.class, 4);
            entity.setTitle("updated");

            transaction.begin();
            agent.update(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            agent.close();
        }

        var events = CallbackEventLog.events();
        assertTrue(events.contains("entity-pre-update"));
        assertTrue(events.contains("entity-post-update"));
        assertTrue(events.contains("annotated-pre-update-object"));
        assertTrue(events.contains("annotated-post-update-callback-entity"));
        assertTrue(events.contains("package-post-update"));
        assertEquals("updated", titleById(4));
    }

    /**
     * Verifies lifecycle callback methods may modify non-relationship state and
     * that the changed state is written to the database.
     */
    @Test
    public void lifecycleCallbackStateChangesArePersistedTest() {
        createCallbackEntity(new CallbackEntity(5, "change-on-pre-insert"));
        assertEquals("changed-by-pre-insert", titleById(5));

        CallbackEventLog.reset();

        var agent = getEntityManagerFactory().createEntityAgent();
        var transaction = agent.getTransaction();
        try {
            var entity = agent.get(CallbackEntity.class, 5);
            entity.setTitle("changed-on-pre-update");

            transaction.begin();
            agent.update(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            agent.close();
        }

        assertEquals("changed-on-pre-update", titleById(5));
        assertTrue(CallbackEventLog.events().contains("entity-pre-update"));
        assertTrue(CallbackEventLog.events().contains("entity-post-update"));
    }

    /**
     * Verifies lifecycle callback methods declared by a mapped superclass are
     * invoked for a concrete entity subclass.
     */
    @Test
    public void mappedSuperclassLifecycleCallbacksTest() {
        var transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(new CallbackDerivedEntity(6, "derived"));
        transaction.commit();

        List<String> events = CallbackEventLog.events();
        assertTrue(events.contains("mapped-superclass-pre-insert"));
        assertTrue(events.contains("mapped-superclass-post-insert"));
        assertTrue(events.contains("derived-entity-pre-insert"));
        assertTrue(events.contains("derived-entity-post-insert"));
    }

    /**
     * Verifies bulk update and delete statements do not trigger entity
     * lifecycle callbacks.
     */
    @Test
    public void statementOperationsDoNotInvokeEntityLifecycleCallbacksTest() {
        createCallbackEntity(new CallbackEntity(7, "statement"));
        CallbackEventLog.reset();

        var transaction = getEntityTransaction();
        transaction.begin();
        int updated = getEntityManager()
                .createStatement("UPDATE Jpa40CallbackEntity e SET e.title = 'bulk-updated' WHERE e.id = 7")
                .execute();
        transaction.commit();

        assertEquals(1, updated);
        assertEquals("bulk-updated", titleById(7));
        assertFalse(CallbackEventLog.events().contains("entity-pre-update"));
        assertFalse(CallbackEventLog.events().contains("entity-post-update"));

        CallbackEventLog.reset();

        transaction = getEntityTransaction();
        transaction.begin();
        int deleted = getEntityManager()
                .createStatement("DELETE FROM Jpa40CallbackEntity e WHERE e.id = 7")
                .execute();
        transaction.commit();

        assertEquals(1, deleted);
        assertEquals(0L, countCallbackEntities());
        assertFalse(CallbackEventLog.events().contains("entity-pre-delete"));
        assertFalse(CallbackEventLog.events().contains("entity-post-delete"));
    }

    /**
     * Verifies a runtime exception from a lifecycle callback prevents the
     * database operation from completing and the post callback from firing.
     */
    @Test
    public void lifecycleCallbackExceptionPreventsInsertTest() {
        var transaction = getEntityTransaction();
        try {
            transaction.begin();
            assertThrows(RuntimeException.class, () -> {
                getEntityManager().persist(new CallbackEntity(8, "throw-on-pre-insert"));
                getEntityManager().flush();
            });
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            getEntityManager().clear();
        }

        var events = CallbackEventLog.events();
        assertTrue(events.contains("entity-pre-insert"));
        assertTrue(events.contains("entity-pre-insert-throwing"));
        assertFalse(events.contains("entity-post-insert"));
        assertEquals(0L, countCallbackEntities());
    }

    private void createCallbackEntity(CallbackEntity entity) {
        var transaction = getEntityTransaction();
        transaction.begin();
        getEntityManager().persist(entity);
        transaction.commit();
        getEntityManager().clear();
    }

    private String titleById(int id) {
        return getEntityManagerFactory().callInTransaction(entityManager ->
                entityManager.find(CallbackEntity.class, id).getTitle());
    }

    private long countCallbackEntities() {
        return getEntityManagerFactory().callInTransaction(entityManager -> entityManager
                .createQuery("SELECT COUNT(e) FROM Jpa40CallbackEntity e", Long.class)
                .getSingleResult());
    }

    private void assertEventOrder(List<String> events, String first, String second) {
        assertTrue(events.indexOf(first) >= 0);
        assertTrue(events.indexOf(second) >= 0);
        assertTrue(events.indexOf(first) < events.indexOf(second));
    }
}
