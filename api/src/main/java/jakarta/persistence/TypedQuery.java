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
//     Gavin King      - 4.0
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
     * {@link List List&lt;X&gt;}. If necessary, first synchronize
     * changes with the database by flushing the persistence context.
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
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     */
    List<X> getResultList();

    /**
     * Execute a SELECT query and return the query result as a typed
     * {@link java.util.stream.Stream Stream&lt;X&gt;}. If necessary,
     * first synchronize changes with the database by flushing the
     * persistence context.
     *
     * <p>By default, this method delegates to {@link List#stream()
     * getResultList().stream()}. The persistence provider may choose
     * to override this method to provide additional capabilities.
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
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     * @see Stream
     * @see #getResultList()
     * @since 2.2
     */
    default Stream<X> getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute a SELECT query that returns a single result.
     * If necessary, first synchronize changes with the database by
     * flushing the persistence context.
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
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     */
    X getSingleResult();

    /**
     * Execute a SELECT query that returns a single untyped result.
     * If necessary, first synchronize changes with the database by
     * flushing the persistence context.
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
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     *
     * @since 3.2
     */
    X getSingleResultOrNull();

    /**
     * Set the maximum number of results to retrieve.
     * @param maxResult  maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    TypedQuery<X> setMaxResults(int maxResult);

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, 
     *        numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    TypedQuery<X> setFirstResult(int startPosition);

    /**
     * Specify an {@link EntityGraph} to be applied to the entity
     * returned by the Jakarta Persistence query. This operation only
     * makes sense when the SELECT query returns a single entity.
     * @param entityGraph an entity graph interpreted as a load graph
     *                    applied to the entity returned by the query
     * @return the same query instance
     * @throws UnsupportedOperationException if this object represents
     *         a native query
     * @since 4.0
     */
    TypedQuery<X> setEntityGraph(EntityGraph<? super X> entityGraph);

    /**
     * Retrieve the {@link EntityGraph} to be applied to the entity
     * returned by the query, or {@code null} if no entity graph was
     * specified via {@link #setEntityGraph(EntityGraph)} or
     * {@link NamedQuery#entityGraph()}.
     * @return the entity graph or {@code null}
     * @since 4.0
     */
    EntityGraph<? super X> getEntityGraph();

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
     TypedQuery<X> setFlushMode(FlushModeType flushMode);

     /**
      * Set the lock mode type to be used for the query execution.
      * @param lockMode  lock mode
      * @return the same query instance
      * @throws IllegalStateException if the query is not a Jakarta
      *         Persistence query language SELECT query or a
      *         {@link jakarta.persistence.criteria.CriteriaQuery}
      * @see #getLockMode
      * @since 2.0
      */
     TypedQuery<X> setLockMode(LockModeType lockMode);

    /**
     * The pessimistic lock scope to use in query execution if a
     * pessimistic lock mode is specified via {@link #setLockMode}.
     * If the query is executed without a pessimistic lock mode,
     * the pessimistic lock scope has no effect.
     * @since 4.0
     * @param lockScope the scope of the pessimistic lock
     * @return the same query instance
     * @throws IllegalStateException if the query is not a Jakarta
     *         Persistence query language SELECT query or a
     *         {@link jakarta.persistence.criteria.CriteriaQuery}
     */
    TypedQuery<X> setLockScope(PessimisticLockScope lockScope);

    /**
     * Set the cache retrieval mode that is in effect during
     * query execution. This cache retrieval mode overrides the
     * cache retrieve mode in use by the entity manager.
     * @param cacheRetrieveMode cache retrieval mode
     * @return the same query instance
     * @since 3.2
     */
    TypedQuery<X> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the cache storage mode that is in effect during
     * query execution. This cache storage mode overrides the
     * cache storage mode in use by the entity manager.
     * @param cacheStoreMode cache storage mode
     * @return the same query instance
     * @since 3.2
     */
    TypedQuery<X> setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * Set the query timeout, in milliseconds. This is a hint,
     * and is an alternative to {@linkplain #setHint setting
     * the hint} {@code jakarta.persistence.query.timeout}.
     * @param timeout the timeout, in milliseconds, or null to
     *                indicate no timeout
     * @return the same query instance
     * @since 3.2
     */
    TypedQuery<X> setTimeout(Integer timeout);
}
