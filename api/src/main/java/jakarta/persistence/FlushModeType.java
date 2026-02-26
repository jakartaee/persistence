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
//     Gavin King      - 4.0
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

/**
 * Enumerates flush modes recognized by the {@link EntityManager}.
 * The {@linkplain EntityManager#getFlushMode() flush mode of a
 * persistence context} controls synchronization of modifications
 * to managed entities with the database.
 *
 * <p>When a transaction is active and the persistence context is
 * joined to the transaction, the persistence provider runtime is
 * permitted to flush pending modifications to the database. The
 * application can influence the timing of synchronization via the
 * method {@link EntityManager#flush()}, by setting the
 * {@link FlushModeType}, or by specifying a {@link QueryFlushMode}.
 * <ul>
 * <li>When the {@code flush()} method is called, the provider must
 *     synchronize every pending modification made to every entity
 *     currently associated with the persistence context.
 * <li>The method {@link EntityManager#setFlushMode} can be used to
 *     control synchronization initiated automatically by the provider.
 *     Unless {@link #EXPLICIT} is specified, all pending modifications
 *     are synchronized with the database when the transaction commits.
 *     When {@link #AUTO} is specified, pending modifications might be
 *     automatically flushed to the database before execution of a
 *     query.
 * <li>The effect of the {@link FlushModeType} of the entity manager
 *     may be overridden during execution of a given query by
 *     explicitly specifying a {@link QueryFlushMode} via the method
 *     {@link Query#setQueryFlushMode}.</li>
 * </ul>
 *
 * <p>If there is no transaction active or if the persistence context
 * has not been joined to the current transaction, the persistence
 * provider must not flush modifications to the database.
 *
 * <p>An {@link EntityAgent} has no associated persistence context,
 * and so flush modes have no effect on an entity agent, nor on
 * {@code Query} objects created by an entity agent.
 *
 * @see EntityManager#setFlushMode(FlushModeType)
 * @see Query#setFlushMode(FlushModeType)
 * @see QueryFlushMode
 * @see SynchronizationType
 *
 * @since 1.0
 */
public enum FlushModeType {
    /**
     * Every flush is an explicit operation requested by the
     * application program by calling {@link EntityManager#flush()}.
     * The persistence context is never automatically flushed.
     * Pending modifications to entities held in the persistence
     * context might not be visible to the processing of queries
     * and might never be made persistent.
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
