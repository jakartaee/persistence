/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
 * Specifies how the {@link EntityManager} interacts with the
 * second-level cache when data is read from the database via
 * the {@link EntityManager#find} methods and execution of
 * queries.
 * <ul>
 * <li>{@link #USE} indicates that data may be read from the
 *     second-level cache.
 * <li>{@link #BYPASS} indicates that data must not be read
 *     from the second-level cache.
 * </ul>
 *
 * <p>Enumerates legal values of the property
 * {@code jakarta.persistence.cache.retrieveMode}.
 *
 * @see EntityHandler#setCacheRetrieveMode(CacheRetrieveMode)
 * @see TypedQuery#setCacheRetrieveMode(CacheRetrieveMode)
 *
 * @since 2.0
 */
public enum CacheRetrieveMode implements FindOption, FetchOption {

    /**
     * Specifies that data may be read from the second-level cache
     * instead of from the database.
     * <p>
     * This is the default mode.
     */
    USE,

    /**
     * Specifies that data must not be read from the second-level
     * cache and must be read from the database.
     *
     * @apiNote When used as a {@link FetchOption}, this value is
     *          a hint to the persistence provider, and might be
     *          ignored.
     */
    BYPASS
}
