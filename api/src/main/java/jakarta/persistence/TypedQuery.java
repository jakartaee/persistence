/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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
//     Lukas Jungmann  - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Interface used to control the execution of typed queries.
 * Only SELECT queries are typed queries, since only a SELECT
 * query can return a result. A DELETE or UPDATE query is not
 * a typed query, and is always represented by an untyped
 * instance of {@link Query}.
 *
 * @param <X> query result type
 *
 * @see Query
 * @see Parameter
 *
 * @since 2.0
 */
public interface TypedQuery<X> extends Query {
	
    /**
     * Execute a SELECT query and return the query results as a typed
     * {@link List List&lt;X&gt;}.
     * @return a list of the results, each of type {@link X}, or an
     *         empty list if there are no results
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         {@code NONE} has been set and there is no transaction
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
    @Override
    List<X> getResultList();

    /**
     * Execute a SELECT query and return the query result as a typed
     * {@link java.util.stream.Stream Stream&lt;X&gt;}.
     *
     * <p>By default, this method delegates to {@link List#stream()
     * getResultList().stream()}, however, persistence provider may
     * choose to override this method to provide additional capabilities.
     *
     * @return a stream of the results, each of type {@link X}, or an
     *         empty stream if there are no results
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         {@code NONE} has been set and there is no transaction
     *         or the persistence context has not been joined to the
     *         transaction
     * @throws PessimisticLockException if pessimistic locking
     *         fails and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking
     *         fails and only the statement is rolled back
     * @throws PersistenceException if the query execution exceeds
     *         the query timeout value set and the transaction
     *         is rolled back
     * @see Stream
     * @see #getResultList()
     * @since 2.2
     */
    @Override
    default Stream<X> getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute a SELECT query that returns a single result.
     * @return the result, of type {@link X}
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         {@code NONE} has been set and there is no transaction
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
    @Override
    X getSingleResult();

    /**
     * Execute a SELECT query that returns a single untyped result.
     * @return the result, of type {@link X}, or null if there is no
     *         result
     * @throws NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         {@code NONE} has been set and there is no transaction
     *         or the persistence context has not been joined to the
     *         transaction
     * @throws PessimisticLockException if pessimistic locking
     *         fails and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking
     *         fails and only the statement is rolled back
     * @throws PersistenceException if the query execution exceeds
     *         the query timeout value set and the transaction
     *         is rolled back
     *
     * @since 3.2
     */
    @Override
    X getSingleResultOrNull();

    /**
     * Set the maximum number of results to retrieve.
     * @param maxResult  maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override
    TypedQuery<X> setMaxResults(int maxResult);

    /**
     * The maximum number of results the query object was set to retrieve.
     * Returns {@link Integer#MAX_VALUE} if {@link #setMaxResults} was not
     * applied to the query object.
     * @return maximum number of results
     * @since 2.0
     */
    @Override
    int getMaxResults();

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, 
     *        numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override
    TypedQuery<X> setFirstResult(int startPosition);

    /**
     * The position of the first result the query object was set to
     * retrieve. Returns {@code 0} if {@code setFirstResult} was not
     * applied to the query object.
     * @return position of the first result
     * @since 2.0
     */
    @Override
    int getFirstResult();

    /**
     * Set a query property or hint. The hints elements may be used 
     * to specify query properties and hints. Properties defined by
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
    @Override
    TypedQuery<X> setHint(String hintName, Object value);

    /**
     * Bind the value of a {@code Parameter} object.
     * @param param  parameter object
     * @param value  parameter value
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     */
    @Override
    <T> TypedQuery<X> setParameter(Parameter<T> param, T value);

    /**
     * Bind an instance of {@link java.util.Calendar} to a {@link Parameter} object.
     * @param param  parameter object
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(Parameter<Calendar> param, 
                               Calendar value,  
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a {@link Parameter} object.
     * @param param  parameter object
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(Parameter<Date> param, Date value,  
                               TemporalType temporalType);

    /**
     * Bind an argument value to a named parameter.
     * @param name  parameter name
     * @param value  parameter value
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does 
     *         not correspond to a parameter of the query or if
     *         the argument is of incorrect type
     */
    @Override
    TypedQuery<X> setParameter(String name, Object value);

    /**
     * Bind an instance of {@link java.util.Calendar} to a named parameter.
     * @param name  parameter name
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(String name, Calendar value,
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a named parameter.
     * @param name   parameter name
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(String name, Date value, 
                               TemporalType temporalType);

    /**
     * Bind an argument value to a positional parameter.
     * @param position  position
     * @param value  parameter value
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the
     *         query or if the argument is of incorrect type
     */
    @Override
    TypedQuery<X> setParameter(int position, Object value);

    /**
     * Bind an instance of {@link java.util.Calendar} to a positional
     * parameter.
     * @param position  position
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the query
     *         or if the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(int position, Calendar value,  
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a positional
     * parameter.
     * @param position  position
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
      *        correspond to a positional parameter of the query
     *         or if the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Override
    @Deprecated(since = "3.2")
    TypedQuery<X> setParameter(int position, Date value,  
                               TemporalType temporalType);

     /**
      * Set the flush mode type to be used for the query execution.
      * The flush mode type applies to the query regardless of the
      * flush mode type in use for the entity manager.
      * @param flushMode  flush mode
      * @return the same query instance
      */
     @Override
     TypedQuery<X> setFlushMode(FlushModeType flushMode);

     /**
      * Set the lock mode type to be used for the query execution.
      * @param lockMode  lock mode
      * @return the same query instance
      * @throws IllegalStateException if the query is found not to 
      *         be a Jakarta Persistence query language SELECT query
      *         or a {@link jakarta.persistence.criteria.CriteriaQuery}
      *         query
      */
     @Override
     TypedQuery<X> setLockMode(LockModeType lockMode);

    /**
     * Get the current lock mode for the query. Returns null if a
     * lock mode has not been set on the query object.
     * @return lock mode
     * @throws IllegalStateException if the query is found not to
     *          be a Jakarta Persistence query language SELECT query
     *          or a {@link jakarta.persistence.criteria.CriteriaQuery}
     *          query
     * @since 2.0
     */
    @Override
    LockModeType getLockMode();

    /**
     * Set the cache retrieval mode that is in effect during
     * query execution. This cache retrieval mode overrides the
     * cache retrieve mode in use by the entity manager.
     * @param cacheRetrieveMode cache retrieval mode
     * @return the same query instance
     * @since 3.2
     */
    @Override
    TypedQuery<X> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the cache storage mode that is in effect during
     * query execution. This cache storage mode overrides the
     * cache storage mode in use by the entity manager.
     * @param cacheStoreMode cache storage mode
     * @return the same query instance
     * @since 3.2
     */
    @Override
    TypedQuery<X> setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * The cache retrieval mode that will be in effect during query
     * execution.
     * @since 3.2
     */
    @Override
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The cache storage mode that will be in effect during query
     * execution.
     * @since 3.2
     */
    @Override
    CacheStoreMode getCacheStoreMode();

    /**
     * Set the query timeout, in milliseconds. This is a hint,
     * and is an alternative to {@linkplain #setHint setting
     * the hint} {@code jakarta.persistence.query.timeout}.
     * @param timeout the timeout, in milliseconds, or null to
     *                indicate no timeout
     * @return the same query instance
     * @since 3.2
     */
    @Override
    TypedQuery<X> setTimeout(Integer timeout);

    /**
     * @deprecated
     * This operation should never be called on a {@code TypedQuery}.
     * Any DELETE or UPDATE query should be represented by an
     * instance of {@link ExecutableQuery}.
     */
    @Override @Deprecated(since = "4.0")
    int executeUpdate();

    /**
     * @throws UnsupportedOperationException because this method
     * should not be called on a {@code TypedQuery}
     */
    @Override @Deprecated
    default ExecutableQuery forExecution() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException because this method
     * should not be called on a {@code TypedQuery}
     */
    @Override @Deprecated
    default <R> TypedQuery<R> forType(Class<R> resultType) {
        throw new UnsupportedOperationException();
    }
}
