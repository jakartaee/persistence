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
//     Gavin King  - 3.2

package jakarta.persistence;

import java.util.List;

/**
 * Interface used to control execution of queries by primary key.
 * @param <T> the entity type
 */
public interface Find<T> {

	/**
	 * Find by primary key, using any specified entity graph,
	 * cache modes, lock mode, hints, and properties, returning
	 * a single entity with the given identifier value.
	 * This operation differs from {@link #getSingleResult(Object)}
	 * in returning null if no entity is found.
	 * @param primaryKey primary key
	 * @return the found entity instance or null if the entity
	 *         does not exist
	 * @throws IllegalArgumentException if the argument is not
	 *         a valid type for the entity's primary key or
	 *         is null
	 * @throws QueryTimeoutException if the query execution exceeds
	 *         the query timeout value set and only the statement is
	 *         rolled back
	 * @throws TransactionRequiredException if a lock mode other than
	 *         <code>NONE</code> has been set and there is no transaction
	 *         or the persistence context has not been joined to the
	 *         transaction
	 * @throws PessimisticLockException if pessimistic locking
	 *         fails and the transaction is rolled back
	 * @throws LockTimeoutException if pessimistic locking
	 *         fails and only the statement is rolled back
	 * @throws PersistenceException if the query execution exceeds
	 *         the query timeout value set and the transaction
	 *         is rolled back
	 */
	T find(Object primaryKey);

	/**
	 * Find by primary key, using any specified entity graph,
	 * cache modes, lock mode, hints, and properties, returning
	 * a single entity with the given identifier value.
	 * This operation differs from {@link #find(Object)} in
	 * throwing a {@link NoResultException} if no entity is found.
	 * @param primaryKey primary key
	 * @return the found entity instance
	 * @throws NoResultException if there is no result
	 * @throws IllegalArgumentException if the argument is not
	 *         a valid type for the entity's primary key or
	 *         is null
	 * @throws QueryTimeoutException if the query execution exceeds
	 *         the query timeout value set and only the statement is
	 *         rolled back
	 * @throws TransactionRequiredException if a lock mode other than
	 *         <code>NONE</code> has been set and there is no transaction
	 *         or the persistence context has not been joined to the
	 *         transaction
	 * @throws PessimisticLockException if pessimistic locking
	 *         fails and the transaction is rolled back
	 * @throws LockTimeoutException if pessimistic locking
	 *         fails and only the statement is rolled back
	 * @throws PersistenceException if the query execution exceeds
	 *         the query timeout value set and the transaction
	 *         is rolled back
	 */
	T getSingleResult(Object primaryKey);

	/**
	 * Find by primary key, using any specified entity graph,
	 * cache modes, lock mode, hints, and properties, returning
	 * a list of entities with the given identifier values.
	 * @return a list of found entity instances
	 * @throws IllegalArgumentException if the argument is not
	 *         a valid type for the entity's primary key or
	 *         is null
	 * @throws QueryTimeoutException if the query execution exceeds
	 *         the query timeout value set and only the statement is
	 *         rolled back
	 * @throws TransactionRequiredException if a lock mode other than
	 *         <code>NONE</code> has been set and there is no transaction
	 *         or the persistence context has not been joined to the
	 *         transaction
	 * @throws PessimisticLockException if pessimistic locking
	 *         fails and the transaction is rolled back
	 * @throws LockTimeoutException if pessimistic locking
	 *         fails and only the statement is rolled back
	 * @throws PersistenceException if the query execution exceeds
	 *         the query timeout value set and the transaction
	 *         is rolled back
	 */
	List<T> getResultList(Object... primaryKeys);

	/**
	 * Find by primary key, using any specified entity graph,
	 * cache modes, lock mode, hints, and properties, returning
	 * a list of entities with the given identifier values.
	 * @return a list of found entity instances
	 * @throws IllegalArgumentException if the argument is not
	 *         a valid type for the entity's primary key or
	 *         is null
	 * @throws QueryTimeoutException if the query execution exceeds
	 *         the query timeout value set and only the statement is
	 *         rolled back
	 * @throws TransactionRequiredException if a lock mode other than
	 *         <code>NONE</code> has been set and there is no transaction
	 *         or the persistence context has not been joined to the
	 *         transaction
	 * @throws PessimisticLockException if pessimistic locking
	 *         fails and the transaction is rolled back
	 * @throws LockTimeoutException if pessimistic locking
	 *         fails and only the statement is rolled back
	 * @throws PersistenceException if the query execution exceeds
	 *         the query timeout value set and the transaction
	 *         is rolled back
	 */
	List<T> getResultList(List<?> primaryKeys);

	/**
	 * Set a property or hint. The hints elements may be used to
	 * specify query properties and hints. Properties defined by
	 * this specification must be observed by the provider.
	 * Vendor-specific hints that are not recognized by a provider
	 * must be silently ignored. Portable applications should not
	 * rely on the standard timeout hint. Depending on the database
	 * in use and the locking mechanisms used by the provider,
	 * this hint may or may not be observed.
	 * @param hintName  name of property or hint
	 * @param value  value for the property or hint
	 * @return the same query instance
	 * @throws IllegalArgumentException if the second argument is not
	 *         valid for the implementation
	 */
	Find<T> setHint(String hintName, Object value);

	/**
	 * Set a fetch graph to be applied to the entity returned by
	 * this query.
	 * @param fetchGraph an entity graph to be interpreted as a
	 *                   fetch graph
	 * @return the same query instance
	 */
	Find<T> setFetchGraph(EntityGraph<? super T> fetchGraph);

	/**
	 * Set a load graph to be applied to the entity returned by
	 * this query.
	 * @param loadGraph an entity graph to be interpreted as a
	 *                  load graph
	 * @return the same query instance
	 */
	Find<T> setLoadGraph(EntityGraph<? super T> loadGraph);

	/**
	 * Set the cache retrieval mode that is in effect during
	 * query execution. This cache retrieval mode overrides the
	 * cache retrieve mode in use by the entity manager.
	 * @param cacheRetrieveMode cache retrieval mode
	 * @return the same query instance
	 */
	Find<T> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

	/**
	 * Set the cache storage mode that is in effect during
	 * query execution. This cache storage mode overrides the
	 * cache storage mode in use by the entity manager.
	 * @param cacheStoreMode cache storage mode
	 * @return the same query instance
	 */
	Find<T> setCacheStoreMode(CacheStoreMode cacheStoreMode);

	/**
	 * Set the lock mode type to be used for the query execution.
	 * @param lockMode  lock mode
	 * @return the same query instance
	 * @throws IllegalStateException if the query is found not to
	 *         be a Jakarta Persistence query language SELECT query
	 *         or a CriteriaQuery query
	 */
	Find<T> setLockMode(LockModeType lockMode);
}
