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

import jakarta.persistence.metamodel.Type;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Interface used to control the execution of typed queries. In
 * the Jakarta Persistence query language, only {@code SELECT}
 * queries are typed queries, since only a {@code SELECT} query
 * can return a result. A {@code DELETE} or {@code UPDATE} query
 * is not a typed query and is always represented by an untyped
 * instance of {@link Query}, usually, by a {@link Statement}.
 * On the other hand, a native SQL query is considered a typed
 * query if it returns a result set.
 *
 * @param <X> query result type
 *
 * @see Query
 * @see Parameter
 * @see Query#ofType(Class)
 *
 * @since 2.0
 */
public interface TypedQuery<X> extends Query {

    /**
     * <p>Determine the maximum number of results that could in
     * principle be returned by the query if no
     * {@linkplain #getFirstResult() offset} or
     * {@linkplain #getMaxResults() limit} were applied.</p>
     *
     * <p>The {@code getResultCount} method should not cause query
     * results to be fetched from the database.</p>
     *
     * @return the maximum number of results that could in principle
     *         be returned by the query if no offset or limit were
     *         applied
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws PersistenceException if the query execution exceeds
     *         the query timeout value set and the transaction
     *         is rolled back
     * @since 4.0
     */
    long getResultCount();

    /**
     * Execute the query and return the query results as a typed
     * {@link List List&lt;X&gt;}. If necessary, first synchronize
     * changes with the database by flushing the persistence context.
     *
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
    @Override @SuppressWarnings("removal")
    List<X> getResultList();

    /**
     * Execute the query, returning a single typed result.
     * If necessary, first synchronize changes with the database by
     * flushing the persistence context.
     *
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
    @Override @SuppressWarnings("removal")
    X getSingleResult();

    /**
     * Execute the query and return the query results as a typed
     * {@link Stream Stream&lt;X&gt;}. If necessary, first synchronize
     * changes with the database by flushing the persistence context.
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
    @Override @SuppressWarnings("removal")
    default Stream<X> getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute the query, returning a single typed result, or
     * {@code null} if the query has no results. If necessary, first
     * synchronize changes with the database by flushing the persistence
     * context.
     *
     * @return the result, of type {@link X}, or {@code null} if there
     *         is no result
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
    @Override @SuppressWarnings("removal")
    X getSingleResultOrNull();

    /**
     * Set the maximum number of results returned to the client.
     * If the query has more results than the given maximum,
     * results are excluded from the list returned by
     * {@link #getResultList()}, so that only the given number
     * of results is returned. If the query returns results
     * with a well-defined order, the excluded results must be
     * those which would otherwise occur later in the list.
     *
     * @param maxResult The maximum number of results
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override @SuppressWarnings("removal")
    TypedQuery<X> setMaxResults(int maxResult);

    /**
     * Set the position of the first query result returned to
     * the client. The given number of results is excluded
     * from the list returned by {@link #getResultList()}.
     * If the query returns results with a well-defined order,
     * the excluded results must be those which would otherwise
     * occur earlier in the list.
     *
     * @param startPosition The position of the first result,
     *                      numbered from {@code 0}
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override @SuppressWarnings("removal")
    TypedQuery<X> setFirstResult(int startPosition);

    /**
     * The maximum number of results returned to the client,
     * as specified by {@link #setMaxResults}, or
     * {@value Integer#MAX_VALUE} if {@link #setMaxResults}
     * was not called.
     *
     * @return the maximum number of results
     * @since 2.0
     */
    @Override @SuppressWarnings("removal")
    int getMaxResults();

    /**
     * The position of the first result returned to the client,
     * as specified by {@link #setFirstResult}, or {@code 0} if
     * {@link #setFirstResult} was not called.
     *
     * @return the position of the first result
     * @since 2.0
     */
    @Override @SuppressWarnings("removal")
    int getFirstResult();

    /**
     * Specify an {@link EntityGraph} to be applied to the entity
     * returned by the Jakarta Persistence query. This operation only
     * makes sense when the {@code SELECT} query returns a single entity.
     *
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
     *
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
     *
     * @param hintName The name of the property or hint
     * @param value The value for the property or hint
     * @return the same query instance
     * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    @Override
    TypedQuery<X> setHint(String hintName, Object value);

    /**
     * Bind an argument to a parameter of this query respresented as
     * a {@link Parameter} object.
     *
     * @param parameter The parameter object
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     */
    @Override
    <T> TypedQuery<X> setParameter(Parameter<T> parameter, T value);

    /**
     * Bind an instance of {@link Calendar} to a {@link Parameter} object.
     * 
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(Parameter<Calendar> param, 
                               Calendar value,
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a {@link Parameter} object.
     *
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(Parameter<Date> param, Date value,
                               TemporalType temporalType);

    /**
     * Bind an argument value to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does 
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     */
    @Override
    TypedQuery<X> setParameter(String name, Object value);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}.
     * {@snippet :
     * var books =
     *     session.createNativeQuery("select * from books where :limit is null or pub_date > :limit",
     *                               Book.class)
     *         .setParameter("limit", optionalDateLimit, LocalDate.class)
     *         .getResultList();
     * }
     * 
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param type A class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setParameter(String name, P value, Class<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < :amount", Book.class)
     *         .setParameter("amount", amount, Book_.price.getType())
     *         .getResultList();
     * }
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param type The {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setParameter(String name, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < :amount", Book.class)
     *         .setConvertedParameter("amount", amount,
     *                 MonetaryAmountConverter.class)
     *         .getResultList();
     *}
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param converter The class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setConvertedParameter(String name, P value,
                                            Class<? extends AttributeConverter<P, ?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(String name, Calendar value, 
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a named parameter.
     *
     * @param name The name of the parameter
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query, or if
     *         the value argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(String name, Date value, 
                               TemporalType temporalType);

    /**
     * Bind an argument value to a positional parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     */
    @Override
    TypedQuery<X> setParameter(int position, Object value);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}.
     * {@snippet :
     * var books =
     *     session.createNativeQuery("select * from books where ?1 is null or pub_date > ?1",
     *                               Book.class)
     *         .setParameter(1, optionalDateLimit, LocalDate.class)
     *         .getResultList();
     * }
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param type A class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setParameter(int position, P value, Class<P> type);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < ?1", Book.class)
     *         .setParameter(1, amount, Book_.price.getType())
     *         .getResultList();
     * }
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param type The {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setParameter(int position, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < ?1", Book.class)
     *         .setConvertedParameter(1, amount,
     *                 MonetaryAmountConverter.class)
     *         .getResultList();
     *}
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param converter The class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
    @Override
    <P> TypedQuery<X> setConvertedParameter(int position, P value,
                                            Class<? extends AttributeConverter<P, ?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a positional
     * parameter.
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(int position, Calendar value,  
                               TemporalType temporalType);

    /**
     * Bind an instance of {@link Date} to a positional
     * parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a positional parameter of the query,
     *         or if the argument is of incompatible type
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    TypedQuery<X> setParameter(int position, Date value,  
                               TemporalType temporalType);

    /**
     * Set the {@linkplain QueryFlushMode query flush mode} to be
     * used when the query is executed. This flush mode overrides
     * the {@linkplain EntityManager#getFlushMode flush mode type
     * of the entity manager}.
     *
     * @param flushMode The new flush mode
     * @return the same query instance
     *
     * @since 4.0
     */
    @Override
    TypedQuery<X> setQueryFlushMode(QueryFlushMode flushMode);

    /**
      * Set the {@linkplain FlushModeType flush mode type} to be
      * used when the query is executed. This flush mode overrides
      * the {@linkplain EntityManager#getFlushMode flush mode type
      * of the entity manager}.
      *
      * @param flushMode The new flush mode
      * @return the same query instance
     *
     * @deprecated Use {@link #setQueryFlushMode(QueryFlushMode)}.
      */
     @Override @Deprecated
     TypedQuery<X> setFlushMode(FlushModeType flushMode);

     /**
      * Set the {@linkplain LockModeType lock mode type} to use
      * when the query is executed.
      *
      * @param lockMode The new lock mode
      * @return the same query instance
      * @throws IllegalStateException if the query is not a Jakarta
      *         Persistence query language {@code SELECT} query or a
      *         {@link jakarta.persistence.criteria.CriteriaQuery}
      * @see #getLockMode
      * @since 2.0
      */
     @Override @SuppressWarnings("removal")
     TypedQuery<X> setLockMode(LockModeType lockMode);

    /**
     * Set the {@linkplain PessimisticLockScope pessimistic lock scope}
     * to use when the query is executed if a pessimistic lock mode
     * is specified via {@link #setLockMode}.
     * If the query is executed without a pessimistic lock mode,
     * the pessimistic lock scope has no effect.
     *
     * @since 4.0
     * @param lockScope The scope of the pessimistic lock
     * @return the same query instance
     * @throws IllegalStateException if the query is not a Jakarta
     *         Persistence query language {@code SELECT} query or a
     *         {@link jakarta.persistence.criteria.CriteriaQuery}
     */
    TypedQuery<X> setLockScope(PessimisticLockScope lockScope);

    /**
     * The current {@linkplain LockModeType lock mode} for the
     * query or {@code null} if a lock mode has not been set.
     * <p>The lock mode affects every entity occurring as an
     * item in the SELECT clause, including entities occurring
     * as arguments to constructors. The effect on association
     * join tables, collection tables, and primary and secondary
     * tables of join fetched entities is determined by the
     * specified {@linkplain #getLockScope lock scope}. If no
     * lock scope was explicitly specified, the lock scope
     * defaults to {@link PessimisticLockScope#NORMAL NORMAL}.
     * <p>If the given lock mode is
     * {@link LockModeType#PESSIMISTIC_READ PESSIMISTIC_READ},
     * {@link LockModeType#PESSIMISTIC_WRITE PESSIMISTIC_WRITE},
     * or {@link LockModeType#PESSIMISTIC_FORCE_INCREMENT
     * PESSIMISTIC_FORCE_INCREMENT}, the lock also affects every
     * entity with an attribute reference occurring in the SELECT
     * clause, except when the attribute reference occurs as an
     * argument to an aggregate function.
     *
     * @return the current lock mode
     * @throws IllegalStateException if the query is not a Jakarta
     *         Persistence query language {@code SELECT} query or a
     *         {@link jakarta.persistence.criteria.CriteriaQuery}
     * @see #getLockScope
     * @since 2.0
     */
    @Override @SuppressWarnings("removal")
    LockModeType getLockMode();

    /**
     * The current {@linkplain PessimisticLockScope pessimistic
     * lock scope} for the query or {@code null} if a scope has
     * not been set.
     * <p>The lock scope determines the effect of
     * {@linkplain #getLockMode locking} on association join
     * tables, collection tables, and primary and secondary tables
     * of join fetched entities. If no lock scope was explicitly
     * specified, locking behaves as if the lock scope were set
     * to {@link PessimisticLockScope#NORMAL NORMAL}.
     * <p>The pessimistic lock scope has no effect if the lock
     * mode is {@code null} or {@link LockModeType#NONE NONE}.
     *
     * @return the current pessimistic lock scope
     * @throws IllegalStateException if the query is not a Jakarta
     *         Persistence query language {@code SELECT} query or a
     *         {@link jakarta.persistence.criteria.CriteriaQuery}
     * @since 4.0
     */
    PessimisticLockScope getLockScope();

    /**
     * Set the {@linkplain CacheRetrieveMode cache retrieval mode}
     * in effect during query execution. This cache retrieval mode
     * overrides the {@linkplain EntityManager#getCacheRetrieveMode
     * cache retrieve mode of the entity manager}.
     *
     * @param cacheRetrieveMode The new cache retrieval mode
     * @return the same query instance
     * @since 3.2
     */
    @Override @SuppressWarnings("removal")
    TypedQuery<X> setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the {@linkplain CacheStoreMode cache storage mode} in
     * effect during query execution. This cache storage mode
     * overrides the {@linkplain EntityManager#getCacheStoreMode
     * cache storage mode of the entity manager}.
     *
     * @param cacheStoreMode The new cache storage mode
     * @return the same query instance
     * @since 3.2
     */
    @Override @SuppressWarnings("removal")
    TypedQuery<X> setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * The {@linkplain CacheRetrieveMode cache retrieval mode} in
     * effect during query execution.
     *
     * @return The current cache retrieval mode set by calling
     *         {@link #setCacheRetrieveMode} or the cache retrieval
     *         mode of the persistence context if no cache retrieval
     *         mode has been explicitly specified for this query.
     * @since 3.2
     */
    @Override @SuppressWarnings("removal")
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The {@linkplain CacheStoreMode cache storage mode} in effect
     * during query execution.
     *
     * @return The current cache storage mode set by calling
     *         {@link #setCacheStoreMode} or the cache storage
     *         mode of the persistence context if no cache storage
     *         mode has been explicitly specified for this query.
     * @since 3.2
     */
    @Override @SuppressWarnings("removal")
    CacheStoreMode getCacheStoreMode();

    /**
     * Set the query timeout, in milliseconds. This is a hint,
     * and is an alternative to {@linkplain #setHint setting
     * the hint} {@code jakarta.persistence.query.timeout}.
     *
     * @param timeout the timeout, in milliseconds, or null to
     *                indicate no timeout
     * @return the same query instance
     * @since 3.2
     */
    @Override
    TypedQuery<X> setTimeout(Integer timeout);

    /**
     * Set the query timeout. This is a hint.
     *
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same query instance
     * @since 4.0
     */
    @Override
    TypedQuery<X> setTimeout(Timeout timeout);

    /**
     * @deprecated
     * This operation should never be called on a {@code TypedQuery}.
     * Any {@code DELETE} or {@code UPDATE} query should be represented
     * by an untyped instance of {@link Query} or, preferably, by an
     * instance of {@link Statement}.
     */
    @Deprecated(since = "4.0", forRemoval = true)
    @Override @SuppressWarnings("removal")
    int executeUpdate();
}
