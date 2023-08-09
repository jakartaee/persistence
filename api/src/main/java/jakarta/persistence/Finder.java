/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2


package jakarta.persistence;

import java.util.List;

/**
 * Finds entities of a certain type by primary key, allowing
 * control over cache interaction, locking, and association
 * fetching.
 *
 * @param <T> the entity type
 *
 * @see EntityManager#find(Class)
 *
 * @since 3.2
 */
public interface Finder<T> {
    /**
     * Specify the {@link CacheStoreMode}.
     * @return the same finder instance
     */
    Finder<T> with(CacheStoreMode cacheStoreMode);

    /**
     * Specify the {@link CacheRetrieveMode}.
     * @return the same finder instance
     */
    Finder<T> with(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Specify the {@link LockModeType}.
     * @return the same finder instance
     */
    Finder<T> with(LockModeType lockModeType);

    /**
     * Specify a {@link PessimisticLockScope}.
     * @return the same finder instance
     */
    Finder<T> with(PessimisticLockScope pessimisticLockScope);

    /**
     * Specify a timeout.
     * @return the same finder instance
     */
    Finder<T> withTimeout(int timeout);

    /**
     * Specify an {@link EntityGraph} to be interpreted as
     * a "fetch graph".
     * @return the same finder instance
     */
    Finder<T> withFetchGraph(EntityGraph<T> fetchGraph);

    /**
     * Specify an {@link EntityGraph} to be interpreted as
     * a "load graph".
     * @return the same finder instance
     */
    Finder<T> withLoadGraph(EntityGraph<T> loadGraph);

    /**
     * Specify a hint.
     * @param hintName the name of the hint
     * @param value the value of the hint
     * @return the same finder instance
     */
    Finder<T> withHint(String hintName, Object value);

    /**
     * Find a single entity instance by its primary key.
     * @param primaryKey the primary key value
     * @return an instance of the entity type {@link T}
     * @throws NoResultException if there is no result
     * @throws IllegalArgumentException if the given primary
     *         key is not an instance of the entity's primary
     *         key type or is null
     */
    T getResult(Object primaryKey);

    /**
     * Find a single entity instance by its primary key, or
     * return null if there is no such instance.
     * @param primaryKey the primary key value
     * @return an instance of the entity type {@link T},
     * or null if there is no instance with the given
     * primary key
     * @throws IllegalArgumentException if the given primary
     *         key is not an instance of the entity's primary
     *         key type or is null
     */
    T getResultOrNull(Object primaryKey);

    /**
     * Find multiple entity instances by their primary keys.
     * @param primaryKeys the primary key values
     * @return an array of instances of the entity type {@link T}
     * matching the given primary keys, where an array element is
     * null if there is no instance with the given primary key
     * @throws IllegalArgumentException if one of the given primary
     *         keys is not an instance of the entity's primary
     *         key type or is null
     */
    T[] getResults(Object... primaryKeys);

    /**
     * Find multiple entity instances by their primary keys.
     * @param primaryKeys a list of primary key values
     * @return a list of instances of the entity type{@link T}
     * matching the given list of primary keys, where an array
     * element is null if there is no instance with the given
     * primary key
     * @throws IllegalArgumentException if an element of the
     *         given list is not an instance of the entity's
     *         primary key type or is null
     */
    List<T> getResultList(List<?> primaryKeys);
}
