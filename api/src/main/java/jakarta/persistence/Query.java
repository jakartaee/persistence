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
import java.util.Set;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Declares common operations for controlling the execution of
 * statements and queries written in the Jakarta Persistence
 * query language or in native SQL.
 * <ul>
 * <li>For a Jakarta Persistence {@code UPDATE} or {@code DELETE}
 *     statement, or for a native SQL statement that returns a row
 *     count, an instance of {@link Statement} should be used to
 *     execute the statement.
 * <li>For a Jakarta Persistence {@code SELECT} query or for any
 *     native SQL query that returns a result set, an instance of
 *     {@link TypedQuery} should be used.
 * <li>For a stored procedure call, a {@link StoredProcedureQuery}
 *     should be used.
 * </ul>
 *
 * @apiNote Every operation only relevant to {@code SELECT} queries,
 * for example, {@link #getResultList} and {@link #setMaxResults},
 * is now declared deprecated by this interface. Such operations
 * should be invoked via the {@link TypedQuery} interface. Similarly,
 * the operation {@link #executeUpdate}, which was only used to
 * execute statements, is declared as deprecated; the new operation
 * {@link Statement#execute} should be used instead.
 *
 * @see Statement
 * @see TypedQuery
 * @see StoredProcedureQuery
 * @see QueryOrStatement
 * @see Parameter
 *
 * @since 1.0
 */
public interface Query {

    /**
     * Execute a {@code SELECT} query or a native query that returns
     * a result set and return the query results as an untyped
     * {@link List}. If necessary, first synchronize changes with the
     * database by flushing the persistence context.
     *
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
     * @deprecated This method returns a raw type.
     *             Use {@link TypedQuery#getResultList} to execute
     *             queries.
     */
    @SuppressWarnings("rawtypes")
    @Deprecated(since = "4.0", forRemoval = true)
    List getResultList();

    /**
     * Execute a {@code SELECT} query or a native query that returns
     * a result set and return the query results as an untyped
     * {@link Stream}. If necessary, first synchronize changes with
     * the database by flushing the persistence context.
     * <p>By default, this method delegates to {@code getResultList().stream()},
     * The persistence provider may choose to override this method to
     * provide additional capabilities.
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
     * @deprecated This method returns a raw type.
     *             Use {@link TypedQuery#getResultStream} to execute
     *             queries
     */
    @SuppressWarnings("rawtypes")
    @Deprecated(since = "4.0", forRemoval = true)
    default Stream getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute a {@code SELECT} query or a native query that returns
     * a result set, returning a single untyped result. If necessary,
     * first synchronize changes with the database by flushing the
     * persistence context.
     *
     * @return the result
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
     * @deprecated Use {@link TypedQuery#getSingleResult}
     *             to execute queries
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Object getSingleResult();

    /**
     * Execute a {@code SELECT} query or a native query that returns
     * a result set, returning a single untyped result, or {@code null}
     * if the query has no results. If necessary, first synchronize
     * changes with the database by flushing the persistence context.
     *
     * @return the result, or {@code null} if there is no result
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
     * @deprecated Use {@link TypedQuery#getSingleResult}
     *             to execute queries
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Object getSingleResultOrNull();

    /**
     * Execute an {@code UPDATE} or {@code DELETE} statement or a
     * native SQL statement that returns a row count.
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
     * @deprecated Use {@link Statement#execute}.
     */
    @Deprecated(since = "4.0", forRemoval = true)
    int executeUpdate();

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
     * @deprecated Use {@link TypedQuery#setMaxResults}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Query setMaxResults(int maxResult);

    /**
     * The maximum number of results returned to the client,
     * as specified by {@link #setMaxResults}, or
     * {@value Integer#MAX_VALUE} if {@link #setMaxResults}
     * was not called.
     *
     * @return the maximum number of results
     * @since 2.0
     * @deprecated Use {@link TypedQuery#getMaxResults}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    int getMaxResults();

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
     * @deprecated Use {@link TypedQuery#setFirstResult}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Query setFirstResult(int startPosition);

    /**
     * The position of the first result returned to the client,
     * as specified by {@link #setFirstResult}, or {@code 0} if
     * {@link #setFirstResult} was not called.
     *
     * @return the position of the first result
     * @since 2.0
     * @deprecated Use {@link TypedQuery#getFirstResult}
     */
    @Deprecated(since = "4.0", forRemoval = true)
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
     *
     * @param hintName The name of the property or hint
     * @param value The value for the property or hint
     * @return the same query instance
     * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    Query setHint(String hintName, Object value);

    /**
     * Get the properties and hints and associated values that are in
     * effect for the query instance.
     *
     * @return query properties and hints
     * @since 2.0
     */
    Map<String, Object> getHints();

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
     * @since 2.0
     */
    <T> Query setParameter(Parameter<T> parameter, T value);

    /**
     * Bind an instance of {@link Calendar} to a {@link Parameter} object.
     *
     * @param param The parameter object
     * @param value The argument to the parameter
     * @param temporalType A {@linkplain TemporalType temporal type}
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter does not
     *         correspond to a parameter of the query
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2")
    Query setParameter(Parameter<Calendar> param, Calendar value, 
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
     * @since 2.0
     * @deprecated Newly written code should use the date/time types
     *             defined in {@link java.time}.
     */
    @Deprecated(since = "3.2")
    Query setParameter(Parameter<Date> param, Date value, 
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
    Query setParameter(String name, Object value);

    /**
     * Bind an argument value to a named parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}, especially in the case
     * of a native query.
     * {@snippet :
     * em.createNativeQuery("update books set pub_date = :date where isbn = :ISBN")
     *     .setParameter("date", optionalPublicationDate, LocalDate.class)
     *     .setParameter("ISBN", isbn)
     *     .executeUpdate();
     * }
     * {@snippet :
     * var books =
     *     em.createNativeQuery("select * from books where :limit is null or pub_date > :limit",
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
    <P> Query setConvertedParameter(String name, P value,
                                    Class<? extends AttributeConverter<P,?>> converter);

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
    @Deprecated(since = "3.2")
    Query setParameter(String name, Calendar value, 
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
    @Deprecated(since = "3.2")
    Query setParameter(String name, Date value, 
                       TemporalType temporalType);

    /**
     * Bind an argument value to a positional parameter.
     *
     * @param position The parameter position
     * @param value The argument to the parameter
     * @return the same query instance
     * @throws IllegalArgumentException if the given position does
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     */
    Query setParameter(int position, Object value);

    /**
     * Bind an argument value to a positional parameter, explicitly
     * specifying the parameter type. This is most useful when
     * the argument might be {@code null}, especially in the case
     * of a native SQL query.
     * {@snippet :
     * em.createNativeQuery("update books set pub_date = ?1 where isbn = ?2")
     *     .setParameter(1, optionalPublicationDate, LocalDate.class)
     *     .setParameter(2, isbn)
     *     .executeUpdate();
     * }
     * {@snippet :
     * var books =
     *     em.createNativeQuery("select * from books where ?1 is null or pub_date > ?1",
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
     *         not correspond to a parameter of the query, or if
     *         the argument is of incompatible type
     * @since 4.0
     */
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
    <P> Query setConvertedParameter(int position, P value,
                                    Class<? extends AttributeConverter<P,?>> converter);

    /**
     * Bind an instance of {@link Calendar} to a positional
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
    @Deprecated(since = "3.2")
    Query setParameter(int position, Calendar value,  
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
    @Deprecated(since = "3.2")
    Query setParameter(int position, Date value,  
                       TemporalType temporalType);

    /**
     * Get the {@link Parameter} objects representing the declared
     * parameters of the query or an empty set if the query has no
     * parameters.
     * This method is not required to be supported for native
     * queries.
     *
     * @return a set containing the parameter objects
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support
     *         this use
     * @since 2.0
     */
    Set<Parameter<?>> getParameters();

    /**
     * Get the {@link Parameter} object representing the declared
     * named parameter with the given name.
     * This method is not required to be supported for native
     * queries.
     *
     * @param name The name of the parameter
     * @return The parameter object representing the named
     *         parameter
     * @throws IllegalArgumentException if the parameter of the
     *         specified name does not exist
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support
     *         this use
     * @since 2.0
     */
    Parameter<?> getParameter(String name);

    /**
     * Get the {@link Parameter} object representing the declared
     * named parameter with the given name and type.
     * This method is required to be supported for criteria queries
     * only.
     *
     * @param name The name of the parameter
     * @param type A class object representing the parameter type
     * @return The parameter object representing the named
     *         parameter
     * @throws IllegalArgumentException if the parameter of the
     *         specified name does not exist or is not assignable
     *         to the type
     * @throws IllegalStateException if invoked on a native
     *         query or Jakarta Persistence query language query when
     *         the implementation does not support this use
     * @since 2.0
     */
    <T> Parameter<T> getParameter(String name, Class<T> type);

    /**
     * Get the {@link Parameter} object representing the declared
     * positional parameter with the given position.
     * This method is not required to be supported for native
     * queries.
     *
     * @param position The parameter position
     * @return The parameter object representing the positional
     *         parameter
     * @throws IllegalArgumentException if the parameter with the
     *         specified position does not exist
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support
     *         this use
     * @since 2.0
     */
    Parameter<?> getParameter(int position);

    /**
     * Get the {@link Parameter} object corresponding to the declared
     * positional parameter with the given position and type.
     * This method is not required to be supported by the provider.
     *
     * @param position The parameter position
     * @param type A class object representing the parameter type
     * @return The parameter object representing the positional
     *         parameter
     * @throws IllegalArgumentException if the parameter with the
     *         specified position does not exist or is not assignable
     *         to the type
     * @throws IllegalStateException if invoked on a native
     *         query or Jakarta Persistence query language query when
     *         the implementation does not support this use
     * @since 2.0
     */
    <T> Parameter<T> getParameter(int position, Class<T> type);

    /**
     * Return a boolean value indicating whether an argument has
     * been bound to the parameter represented by the given
     * parameter object.
     *
     * @param parameter The parameter object
     * @return {@code true} an argument has been bound, or
     *         {@code false} otherwise
     * @since 2.0
     */
    boolean isBound(Parameter<?> parameter);

    /**
     * Return the input value bound to the parameter.
     * (Note that OUT parameters are unbound.)
     *
     * @param parameter The parameter object
     * @return parameter value
     * @throws IllegalArgumentException if the parameter is not 
     *         a parameter of the query
     * @throws IllegalStateException if the parameter has not
     *         been bound
     * @since 2.0
     */
    <T> T getParameterValue(Parameter<T> parameter);

    /**
     * Return the input value bound to the named parameter.
     * (Note that OUT parameters are unbound.)
     *
     * @param name The name of the parameter
     * @return parameter value
     * @throws IllegalStateException if the parameter has not
     *         been bound
     * @throws IllegalArgumentException if the parameter of the
     *         specified name does not exist
     * @since 2.0
     */
    Object getParameterValue(String name);

    /**
     * Return the input value bound to the positional parameter.
     * (Note that OUT parameters are unbound.)
     *
     * @param position The parameter position
     * @return parameter value
     * @throws IllegalStateException if the parameter has not
     *          been bound
     * @throws IllegalArgumentException if the parameter with the
     *          specified position does not exist
     * @since 2.0
     */
    Object getParameterValue(int position);

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
    Query setQueryFlushMode(QueryFlushMode flushMode);

    /**
     * Get the flush mode which will be in effect when the query
     * is executed. If a flush mode has not been set for this query
     * object, return {@link QueryFlushMode#DEFAULT}.
     *
     * @return flush mode
     * @since 4.0
     */
    QueryFlushMode getQueryFlushMode();

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
    @Deprecated(since = "4.0")
    Query setFlushMode(FlushModeType flushMode);

    /**
     * Get the flush mode which will be in effect when the query
     * is executed. If a flush mode has not been set for this query
     * object, return the {@linkplain EntityManager#getFlushMode
     * current flush mode type of the entity manager}.
     *
     * @return flush mode
     * @since 2.0
     *
     * @deprecated Use {@link #getQueryFlushMode()}.
     */
    @Deprecated(since = "4.0")
    FlushModeType getFlushMode();

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
     * @deprecated Use {@link TypedQuery#setLockMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Query setLockMode(LockModeType lockMode);

    /**
     * The current {@linkplain LockModeType lock mode} for the
     * query or {@code null} if a lock mode has not been set.
     * <p>The lock mode affects every entity occurring as an
     * item in the SELECT clause, including entities occurring
     * as arguments to constructors. The effect on association
     * join tables, collection tables, and primary and secondary
     * tables of join fetched entities is determined by the
     * lock scope in effect. If no lock scope was explicitly
     * specified, the lock scope defaults to
     * {@link PessimisticLockScope#NORMAL NORMAL}.
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
     * @since 2.0
     * @deprecated Use {@link TypedQuery#getLockMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    LockModeType getLockMode();

    /**
     * Set the {@linkplain CacheRetrieveMode cache retrieval mode}
     * in effect during query execution. This cache retrieval mode
     * overrides the {@linkplain EntityManager#getCacheRetrieveMode
     * cache retrieve mode of the entity manager}.
     *
     * @param cacheRetrieveMode The new cache retrieval mode
     * @return the same query instance
     * @since 3.2
     * @deprecated Use {@link TypedQuery#setCacheRetrieveMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Query setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the {@linkplain CacheStoreMode cache storage mode} in
     * effect during query execution. This cache storage mode
     * overrides the {@linkplain EntityManager#getCacheStoreMode
     * cache storage mode of the entity manager}.
     *
     * @param cacheStoreMode The new cache storage mode
     * @return the same query instance
     * @since 3.2
     * @deprecated Use {@link TypedQuery#setCacheStoreMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    Query setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * The {@linkplain CacheRetrieveMode cache retrieval mode} in
     * effect during query execution.
     *
     * @return the current cache retrieval mode set by calling
     *         {@link #setCacheRetrieveMode} or the cache retrieval
     *         mode of the persistence context if no cache retrieval
     *         mode has been explicitly specified for this query.
     * @since 3.2
     * @deprecated Use {@link TypedQuery#getCacheRetrieveMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The {@linkplain CacheStoreMode cache storage mode} in effect
     * during query execution.
     *
     * @return the current cache storage mode set by calling
     *         {@link #setCacheStoreMode} or the cache storage
     *         mode of the persistence context if no cache storage
     *         mode has been explicitly specified for this query.
     * @since 3.2
     * @deprecated Use {@link TypedQuery#getCacheStoreMode}
     */
    @Deprecated(since = "4.0", forRemoval = true)
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
    Query setTimeout(Integer timeout);

    /**
     * Set the query timeout. This is a hint.
     *
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same query instance
     * @since 4.0
     */
    Query setTimeout(Timeout timeout);

    /**
     * The query timeout, in milliseconds, or null for no timeout.
     *
     * @since 3.2
     */
    Integer getTimeout();

    /**
     * Return an object of the specified type to allow access to 
     * a provider-specific API. If the provider implementation of
     * {@code Query} does not support the given type, the
     * {@link PersistenceException} is thrown.
     *
     * @param type The type of the object to be returned.
     *             This is usually either the underlying class
     *             implementing {@code Query} or an interface it
     *             implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not support
     *         the given type
     * @since 2.0
     */
    <T> T unwrap(Class<T> type);
}
