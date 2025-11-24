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
 * Specifies how the {@link EntityManager} interacts with the
 * second-level cache when data is read from the database and
 * when data is written to the database.
 * <ul>
 * <li>{@link #USE} indicates that data may be written to the
 *     second-level cache.
 * <li>{@link #BYPASS} indicates that data may not be written
 *     to the second-level cache.
 * <li>{@link #REFRESH} indicates that data must be written
 *     to the second-level cache, even when the data is already
 *     cached.
 * </ul>
 *
 * <p>Enumerates legal values of the property
 * {@code jakarta.persistence.cache.storeMode}.
 *
 * @see EntityManager#setCacheStoreMode(CacheStoreMode)
 * @see Query#setCacheStoreMode(CacheStoreMode)
 *
 * @since 2.0
 */
public enum CacheStoreMode implements FindOption, RefreshOption {

    /**
     * Specifies that entity data may be inserted into the
     * second-level cache when read from the database, and inserted
     * or updated in the second-level cache when written to the
     * database. The persistence provider is not required to refresh
     * already-cached items when reading from the database.
     * <p>
     * This is the default cache storage mode for a persistence
     * context.
     *
     * @see EntityManager#getCacheStoreMode()
     */
    USE,

    /**
     * Specifies that entity data may not be inserted into the
     * second-level cache when read from the database, nor when
     * written to the database.
     */
    BYPASS,

    /**
     * Specifies that entity data must be inserted or updates in
     * the second-level cache when read from the database or when
     * written to the database. The persistence provider is
     * required to refresh already-cached items when reading from
     * the database.
     */
    REFRESH,

    /**
     * Specifies that the cache storage mode is determined by
     * the {@linkplain EntityManager#getCacheStoreMode
     * cache storage mode of the persistence context}.
     * <p>
     * This is the default cache storage mode for a query.
     * <p>
     * If this cache retrieval mode is passed to
     * {@link EntityManager#setCacheStoreMode},
     * the behavior is undefined.
     *
     * @see Query#getCacheStoreMode()
     *
     * @since 4.0
     */
    DEFAULT
}
