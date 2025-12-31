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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Interface used to control execution of a query when either the
 * query does not return a result or when the type of the result
 * was not explicitly specified and is not known at compile time.
 *
 * <p>If an instance of this interface represents a {@code SELECT}
 * query, then a {@link TypedQuery} representing the same query may
 * be obtained by calling {@link #ofType(Class)}, passing the result
 * type of the query.
 * {@snippet :
 * List<Book> books =
 *         em.createQuery("from Book where extract(year from publicationDate) > :year")
 *                 .ofType(Book.class)
 *                 .setParameter("year", Year.of(2000))
 *                 .setMaxResults(10)
 *                 .setCacheRetrieveMode(CacheRetrieveMode.BYPASS)
 *                 .getResults();
 * }
 *
 * @see TypedQuery
 * @see StoredProcedureQuery
 * @see Parameter
 *
 * @since 1.0
 */
public interface Query extends TypedQuery<Object> {

    /**
     * Obtain a {@link TypedQuery} with the given query result type,
     * which must be a supertype of the result type of this query.
     * This query must be a Jakarta Persistence {@code SELECT} query
     * or a native SQL query which returns a result set.
     * @param resultType The Java class of the query result type
     * @param <R> The query result type
     * @throws IllegalArgumentException if the given result type is
     *         not a supertype of the result type of this query
     * @throws IllegalStateException if this query is a Jakarta
     *         Persistence {@code UPDATE} or {@code DELETE}
     *         statement
     * @since 4.0
     */
    <R> TypedQuery<R> ofType(Class<R> resultType);

    /**
     * Obtain a {@link TypedQuery} with the given entity graph,
     * which must be rooted at a supertype of the result type of
     * this query. This query must be a Jakarta Persistence
     * {@code SELECT} query which returns a single entity type.
     * @param graph The entity graph, interpreted as a load graph
     * @param <R> The query result type
     * @throws IllegalArgumentException if the given graph type is
     *         not rooted at a supertype of the result type of this
     *         query
     * @throws IllegalStateException if this query is a Jakarta
     *         Persistence {@code UPDATE} or {@code DELETE}
     *         statement
     * @since 4.0
     */
    <R> TypedQuery<R> withEntityGraph(EntityGraph<R> graph);

    /**
     * Execute a SELECT query and return the query results as an untyped
     * {@link List}. If necessary, first synchronize changes with the
     * database by flushing the persistence context.
     * @return a list of the results, or an empty list if there are
     *         no results
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
     * @deprecated This method returns a raw {@code List}. Use
     * {@link #ofType ofType(X.class).getResultList()} instead.
     * This override might be removed in the next major release.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Deprecated(since = "4.0") @Override
    List getResultList();

    /**
     * Execute a SELECT query and return the query results as an untyped
     * {@link java.util.stream.Stream}. If necessary, first synchronize
     * changes with the database by flushing the persistence context.
     *
     * <p>By default, this method delegates to {@code getResultList().stream()},
     * however persistence provider may choose to override this method
     * to provide additional capabilities.
     *
     * @return a stream of the results, or an empty stream if there
     *         are no results
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
     * @deprecated This method returns a raw {@code Stream}. Use
     * {@link #ofType ofType(X.class).getResultStream()} instead.
     * This override might be removed in the next major release.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Deprecated(since = "4.0") @Override
    default Stream getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute a Jakarta Persistence UPDATE or DELETE statement,
     * or a native SQL statement that returns a row count.
     * <p>
     * After execution of a bulk update or delete operation, the
     * persistence provider is not required to resynchronize state
     * held in memory with the effects of the operation on data
     * held in the database. However, when this method is called
     * within a transaction, the persistence context is joined to
     * the transaction, and {@link FlushModeType#AUTO} is in effect,
     * the persistence provider must ensure that every modification
     * to the state of every entity associated with the persistence
     * context which could possibly alter the effects of the bulk
     * update or delete operation is visible to the processing of
     * the operation.
     *
     * @return the number of entities updated or deleted, or the
     *         row count of the native SQL statement
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language SELECT statement or for
     *         a criteria query
     * @throws TransactionRequiredException if there is 
     *         no transaction or the persistence context has not
     *         been joined to the transaction
     * @throws QueryTimeoutException if the statement execution 
     *         exceeds the query timeout value set and only
     *         the statement is rolled back
     * @throws PersistenceException if the query execution exceeds 
     *         the query timeout value set and the transaction
     *         is rolled back
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected during the flush
     */
    int executeUpdate();

    /**
     * Set the maximum number of results to retrieve.
     * @param maxResult  maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override
    Query setMaxResults(int maxResult);

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    @Override
    Query setFirstResult(int startPosition);

    /**
     * Set a query property or hint. The hints elements may be used 
     * to specify query properties and hints. Properties defined by
     * this specification must be observed by the provider. 
     * Vendor-specific hints that are not recognized by a provider
     * must be silently ignored. Portable applications should not
     * rely on the standard timeout hint. Depending on the database
     * in use and the locking mechanisms used by the provider,
     * this hint may or may not be observed.
     * @param hintName  name of the property or hint
     * @param value  value for the property or hint
     * @return the same query instance
     * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    @Override
    Query setHint(String hintName, Object value);

    /**
     * Bind the value of a {@code Parameter} object.
     * @param param  parameter object
     * @param value  parameter value
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     * @since 2.0
     */
    @Override
    <T> Query setParameter(Parameter<T> param, T value);

    /**
     * Bind an instance of {@link java.util.Calendar} to a {@link Parameter} object.
     * @param param parameter object
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Query setParameter(Parameter<Calendar> param, Calendar value, 
                       TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a {@link Parameter} object.
     * @param param parameter object
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Query setParameter(Parameter<Date> param, Date value, 
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
    Query setParameter(String name, Object value);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be null, especially in the case of
     * a native query.
     * {@snippet :
     * em.createNativeQuery("update books set pub_date = :date where isbn = :ISBN")
     *     .setParameter("date", optionalPublicationDate, LocalDate.class)
     *     .setParameter("ISBN", isbn)
     *     .executeUpdate();
     * }
     * {@snippet :
     * var books =
     *     session.createNativeQuery("select * from books where :limit is null or pub_date > :limit",
     *                               Book.class)
     *         .setParameter("limit", optionalDateLimit, LocalDate.class)
     *         .getResultList();
     * }
     * @param name  parameter name
     * @param value  parameter value
     * @param type  a class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setParameter(String name, P value, Class<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < :amount")
     *         .setParameter("amount", amount, Book_.price.getType())
     *         .getResultList();
     * }
     * @param name  parameter name
     * @param value  parameter value
     * @param type  the {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setParameter(String name, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < :amount")
     *         .setConvertedParameter("amount", amount,
     *                 MonetaryAmountConverter.class)
     *         .getResultList();
     * }
     * @param name  parameter name
     * @param value  parameter value
     * @param converter  class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setConvertedParameter(String name, P value,
                                    Class<? extends AttributeConverter<P,?>> converter);

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
    @Deprecated(since = "3.2") @Override
    Query setParameter(String name, Calendar value, 
                       TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a named parameter.
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
    @Deprecated(since = "3.2") @Override
    Query setParameter(String name, Date value, 
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
    Query setParameter(int position, Object value);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be null, especially in the case of
     * a native SQL query.
     * {@snippet :
     * em.createNativeQuery("update books set pub_date = ?1 where isbn = ?2")
     *     .setParameter(1, optionalPublicationDate, LocalDate.class)
     *     .setParameter(2, isbn)
     *     .executeUpdate();
     * }
     * {@snippet :
     * var books =
     *     session.createNativeQuery("select * from books where ?1 is null or pub_date > ?1",
     *                               Book.class)
     *         .setParameter(1, optionalDateLimit, LocalDate.class)
     *         .getResultList();
     * }
     * @param position  position
     * @param value  parameter value
     * @param type  a class object representing the parameter type
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the
     *         query or if the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setParameter(int position, P value, Class<P> type);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the binding is affected by an attribute converter.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < ?1")
     *         .setParameter(1, amount, Book_.price.getType())
     *         .getResultList();
     * }
     * @param position  position
     * @param value  parameter value
     * @param type  the {@link Type} of the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the
     *         query or if the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setParameter(int position, P value, Type<P> type);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying an {@linkplain AttributeConverter attribute
     * converter} to use.
     * {@snippet :
     * var amount = MonetaryAmount.of(priceLimit, currency);
     * var affordableBooks =
     *     em.createQuery("from Book where price < ?1")
     *         .setConvertedParameter(1, amount,
     *                 MonetaryAmountConverter.class)
     *         .getResultList();
     * }
     * @param position  position
     * @param value  parameter value
     * @param converter  class of the attribute converter
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter name does
     *         not correspond to a parameter of the query or if
     *         the argument is of incorrect type
     * @since 4.0
     */
    @Override
    <P> Query setConvertedParameter(int position, P value,
                                    Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link java.util.Calendar} to a positional
     * parameter.
     * @param position  position
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the query or
     *         if the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Query setParameter(int position, Calendar value,  
                       TemporalType temporalType);

    /**
     * Bind an instance of {@link java.util.Date} to a positional
     * parameter.
     * @param position  position
     * @param value  parameter value
     * @param temporalType  temporal type
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *         correspond to a positional parameter of the query or
     *         if the value argument is of incorrect type
     * @deprecated Newly-written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2") @Override
    Query setParameter(int position, Date value,  
                       TemporalType temporalType);

    /**
     * Set the flush mode type to be used for the query execution.
     * The flush mode type applies to the query regardless of the
     * flush mode type in use for the entity manager.
     * @param flushMode  flush mode
     * @return the same query instance
     */
    @Override
    Query setFlushMode(FlushModeType flushMode);

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
    @Override
    Query setLockMode(LockModeType lockMode);

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
    @Override
    Query setLockScope(PessimisticLockScope lockScope);

    /**
     * Set the cache retrieval mode that is in effect during query
     * execution. This cache retrieval mode overrides the cache
     * retrieve mode in use by the entity manager.
     * @param cacheRetrieveMode cache retrieval mode
     * @return the same query instance
     * @since 3.2
     */
    @Override
    Query setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the cache storage mode that is in effect during query
     * execution. This cache storage mode overrides the cache
     * storage mode in use by the entity manager.
     * @param cacheStoreMode cache storage mode
     * @return the same query instance
     * @since 3.2
     */
    @Override
    Query setCacheStoreMode(CacheStoreMode cacheStoreMode);

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
    Query setTimeout(Integer timeout);

    /**
     * Set the query timeout. This is a hint.
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same query instance
     * @since 4.0
     */
    @Override
    Query setTimeout(Timeout timeout);

}
