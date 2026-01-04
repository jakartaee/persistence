/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

/**
 * Enumerates flush modes recognized by the {@link EntityManager}.
 *
 * <p>When a Jakarta Persistence or native SQL query is executed
 * within a transaction via a {@link Query}, {@link TypedQuery},
 * or {@link StoredProcedureQuery} object obtained from an
 * {@link EntityManager} joined to the transaction, the effective
 * flush mode is determined by the {@linkplain Query#getFlushMode
 * current flush mode of the query object}, which defaults to the
 * {@linkplain EntityManager#getFlushMode current flush mode of
 * the persistence context}.
 * <ul>
 * <li>If {@link #AUTO} is in effect, the persistence provider must
 *     ensure that every modification to the state of every entity
 *     associated with the persistence context which could possibly
 *     affect the result of the query is visible to the processing
 *     of the query. The persistence provider implementation might
 *     guarantee this by flushing pending updates to modified
 *     entities to the database before executing the query.
 * <li>Otherwise, if {@link #COMMIT} or {@link #EXPLICIT} is set,
 *     the effect on the results of the query of pending
 *     modifications to entities in the persistence context is
 *     unspecified.
 * </ul>
 *
 * <p>When there is no transaction active, or if the persistence
 * context has not been joined to the current transaction, the
 * persistence provider must not flush pending modifications to
 * the database, regardless of the flush mode.
 *
 * <p>An {@link EntityAgent} has no associated persistence context,
 * and so flush modes have no effect on an entity agent, nor on
 * {@code Query} objects created by an entity agent.
 *
 * @see EntityManager#setFlushMode(FlushModeType)
 * @see Query#setFlushMode(FlushModeType)
 * @see SynchronizationType
 *
 * @since 1.0
 */
public enum FlushModeType {
    /**
     * Every flush is an explicit operation requested by the
     * application program. The session is never automatically
     * flushed. Pending modifications to entities held in the
     * persistence context might not be visible to the processing
     * of queries and might never be made persistent.
     *
     * @since 4.0
     */
    EXPLICIT,

    /**
     * Pending modifications to entities associated with a
     * persistence context joined to the current transaction are
     * automatically flushed to the database when the transaction
     * commits. The provider is permitted to flush at other times,
     * but is not required to. Pending modifications to entities
     * associated with the persistence context might not be visible
     * to the processing of queries.
     */
    COMMIT,

    /**
     * Pending modifications to entities associated with a
     * persistence context joined to the current transaction are
     * automatically flushed to the database when the transaction
     * commits. In addition, the persistence provider must ensure
     * that every modification to the state of every entity
     * associated with the persistence context which could possibly
     * affect the result of a query is visible to the processing
     * of the query. The persistence provider implementation might
     * guarantee this by flushing pending modifications to the
     * database before executing the query. The provider is
     * permitted to flush at other times, but is not required to.
     * <p>
     * This is the default flush mode for a newly created
     * {@link EntityManager}.
     */
    AUTO
}
