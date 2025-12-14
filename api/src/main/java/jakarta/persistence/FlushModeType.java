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
 * <p>When a query is executed within a transaction:
 * <ul>
 * <li>If {@link #AUTO} is selected via the {@link Query Query} or
 *     {@link TypedQuery} object, or if the flush mode setting for
 *     the persistence context is {@code AUTO} (the default) and a
 *     flush mode setting has not been specified for the {@code Query}
 *     or {@code TypedQuery} object, the persistence provider must
 *     ensure that every modification to the state of every entity
 *     associated with the persistence context which could possibly
 *     affect the result of the query is visible to the processing
 *     of the query. The persistence provider implementation might
 *     guarantee this by flushing pending updates to modified
 *     entities to the database before executing the query.
 * <li>On the other hand, if {@link #COMMIT} or {@link #EXPLICIT}
 *     is selected, the effect on query results of modifications
 *     made to entities held in the persistence context is
 *     unspecified.
 * </ul>
 *
 * <p>If there is no transaction active or if the persistence context
 * is not joined to the current transaction, the persistence provider
 * must not flush to the database, regardless of flush mode.
 *
 * @see EntityManager#setFlushMode(FlushModeType)
 * @see Query#setFlushMode(FlushModeType)
 *
 * @since 1.0
 */
public enum FlushModeType {
    /**
     * Every flush is an explicit operation requested by the application
     * program. The session is never automatically flushed. Modifications
     * to entities held in the persistence context might not be visible
     * to the processing of queries and might never be made persistent.
     *
     * @since 4.0
     */
    EXPLICIT,

    /**
     * The persistence context is flushed automatically before any attempt
     * to commit the transaction with which it is associated. The provider
     * may flush at other times, but is not required to. Modifications to
     * entities held in the persistence context might not be visible to
     * the processing of queries.
     */
    COMMIT,

    /**
     * A flush might occur before execution of any query, especially if
     * the flush is necessary to ensure consistency between the results
     * of the query and modifications held in memory. The provider is not
     * required to flush before execution of the query, but is required to
     * ensure that the effect of every modification to the state of every
     * entity associated with the persistence context is visible to the
     * processing of the query.
     * <p>
     * This is the default flush mode for a newly created entity manager.
     */
    AUTO
}
