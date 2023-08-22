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
//     Gavin King      - 3.2
//     Lukas Jungmann  - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Interface used to control query execution.
 *
 * @see TypedQuery
 * @see StoredProcedureQuery
 * @see Parameter
 *
 * @since 1.0
 */
public interface Query {

    /**
     * Execute a SELECT query and return the query results
     * as an untyped List.
     * @return a list of the results
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         <code>NONE</code> has been set and there is no transaction
     *         or the persistence context has not been joined to the transaction
     * @throws PessimisticLockException if pessimistic locking
     *         fails and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking
     *         fails and only the statement is rolled back
     * @throws PersistenceException if the query execution exceeds 
     *         the query timeout value set and the transaction 
     *         is rolled back 
     */
    List getResultList();

    /**
     * Execute a SELECT query and return the query results
     * as an untyped <code>java.util.stream.Stream</code>.
     * By default this method delegates to <code>getResultList().stream()</code>,
     * however persistence provider may choose to override this method
     * to provide additional capabilities.
     *
     * @return a stream of the results
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         <code>NONE</code> has been set and there is no transaction
     *         or the persistence context has not been joined to the transaction
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
    default Stream getResultStream() {
        return getResultList().stream();
    }

    /**
     * Execute a SELECT query that returns a single untyped result.
     * @return the result
     * @throws NoResultException if there is no result
     * @throws NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         <code>NONE</code> has been set and there is no transaction
     *         or the persistence context has not been joined to the transaction
     * @throws PessimisticLockException if pessimistic locking
     *         fails and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking
     *         fails and only the statement is rolled back
     * @throws PersistenceException if the query execution exceeds 
     *         the query timeout value set and the transaction 
     *         is rolled back 
     */
    Object getSingleResult();

    /**
     * Execute a SELECT query that returns a single untyped result.
     * @return the result, or null if there is no result
     * @throws NonUniqueResultException if more than one result
     * @throws IllegalStateException if called for a Jakarta
     *         Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException if the query execution exceeds
     *         the query timeout value set and only the statement is
     *         rolled back
     * @throws TransactionRequiredException if a lock mode other than
     *         <code>NONE</code> has been set and there is no transaction
     *         or the persistence context has not been joined to the transaction
     * @throws PessimisticLockException if pessimistic locking
     *         fails and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking
     *         fails and only the statement is rolled back
     * @throws PersistenceException if the query execution exceeds
     *         the query timeout value set and the transaction
     *         is rolled back
     */
    Object getSingleResultOrNull();

    /**
     * Execute an update or delete statement.
     * @return the number of entities updated or deleted
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
     */
    int executeUpdate();

    /**
     * Set the maximum number of results to retrieve.
     * @param maxResult  maximum number of results to retrieve
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    Query setMaxResults(int maxResult);

    /**
     * The maximum number of results the query object was set to
     * retrieve. Returns <code>Integer.MAX_VALUE</code> if <code>setMaxResults</code> was not
     * applied to the query object.
     * @return maximum number of results
     * @since 2.0
     */
    int getMaxResults();

    /**
     * Set the position of the first result to retrieve.
     * @param startPosition position of the first result, 
     * numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if the argument is negative
     */
    Query setFirstResult(int startPosition);

    /**
     * The position of the first result the query object was set to
     * retrieve. Returns 0 if <code>setFirstResult</code> was not applied to the
     * query object.
     * @return position of the first result
     * @since 2.0
     */
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
     * @param hintName  name of the property or hint
     * @param value  value for the property or hint
     * @return the same query instance
     * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    Query setHint(String hintName, Object value);

    /**
     * Get the properties and hints and associated values that are 
     * in effect for the query instance.
     * @return query properties and hints
     * @since 2.0
     */
    Map<String, Object> getHints();

    /**
     * Bind the value of a <code>Parameter</code> object.
     * @param param  parameter object
     * @param value  parameter value
     * @return the same query instance
     * @throws IllegalArgumentException if the parameter
     *         does not correspond to a parameter of the
     *         query
     * @since 2.0
     */
    <T> Query setParameter(Parameter<T> param, T value);

    /**
     * Bind an instance of <code>java.util.Calendar</code> to a <code>Parameter</code> object.
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
    @Deprecated(since = "3.2")
    Query setParameter(Parameter<Calendar> param, Calendar value, 
                       TemporalType temporalType);

    /**
     * Bind an instance of <code>java.util.Date</code> to a <code>Parameter</code> object.
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
    @Deprecated(since = "3.2")
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
    Query setParameter(String name, Object value);

    /**
     * Bind an instance of <code>java.util.Calendar</code> to a named parameter.
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
    Query setParameter(String name, Calendar value, 
                       TemporalType temporalType);

    /**
     * Bind an instance of <code>java.util.Date</code> to a named parameter.
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
    Query setParameter(int position, Object value);

    /**
     * Bind an instance of <code>java.util.Calendar</code> to a positional
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
    @Deprecated(since = "3.2")
    Query setParameter(int position, Calendar value,  
                       TemporalType temporalType);

    /**
     * Bind an instance of <code>java.util.Date</code> to a positional parameter.
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
    @Deprecated(since = "3.2")
    Query setParameter(int position, Date value,  
                       TemporalType temporalType);

    /**
     * Get the parameter objects corresponding to the declared
     * parameters of the query.
     * Returns empty set if the query has no parameters.
     * This method is not required to be supported for native
     * queries.
     * @return set of the parameter objects
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support 
     *         this use
     * @since 2.0
     */
    Set<Parameter<?>> getParameters();

    /**
     * Get the parameter object corresponding to the declared
     * parameter of the given name.
     * This method is not required to be supported for native
     * queries.
     * @param name  parameter name
     * @return parameter object
     * @throws IllegalArgumentException if the parameter of the
     *         specified name does not exist
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support 
     *         this use
     * @since 2.0
     */
    Parameter<?> getParameter(String name);

    /**
     * Get the parameter object corresponding to the declared
     * parameter of the given name and type.
     * This method is required to be supported for criteria queries
     * only.
     * @param name  parameter name
     * @param type  type
     * @return parameter object
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
     * Get the parameter object corresponding to the declared
     * positional parameter with the given position.
     * This method is not required to be supported for native
     * queries.
     * @param position  position
     * @return parameter object
     * @throws IllegalArgumentException if the parameter with the
     *         specified position does not exist
     * @throws IllegalStateException if invoked on a native
     *         query when the implementation does not support 
     *         this use
     * @since 2.0
     */
    Parameter<?> getParameter(int position);

    /**
     * Get the parameter object corresponding to the declared
     * positional parameter with the given position and type.
     * This method is not required to be supported by the provider.
     * @param position  position
     * @param type  type
     * @return parameter object
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
     * Return a boolean indicating whether a value has been bound 
     * to the parameter.
     * @param param parameter object
     * @return boolean indicating whether parameter has been bound
     * @since 2.0
     */
    boolean isBound(Parameter<?> param);

    /**
     * Return the input value bound to the parameter.
     * (Note that OUT parameters are unbound.)
     * @param param parameter object
     * @return parameter value
     * @throws IllegalArgumentException if the parameter is not 
     *         a parameter of the query
     * @throws IllegalStateException if the parameter has not been
     *         been bound
     * @since 2.0
     */
    <T> T getParameterValue(Parameter<T> param);

    /**
     * Return the input value bound to the named parameter.
     * (Note that OUT parameters are unbound.)
     * @param name  parameter name
     * @return parameter value
     * @throws IllegalStateException if the parameter has not been
     *         been bound
     * @throws IllegalArgumentException if the parameter of the
     *         specified name does not exist
     * @since 2.0
     */
    Object getParameterValue(String name);

    /**
     * Return the input value bound to the positional parameter.
     * (Note that OUT parameters are unbound.)
     * @param position  position
     * @return parameter value
     * @throws IllegalStateException if the parameter has not been
     *         been bound
     * @throws IllegalArgumentException if the parameter with the
     *         specified position does not exist
     * @since 2.0
     */
    Object getParameterValue(int position);

    /**
     * Set the flush mode type to be used for the query execution.
     * The flush mode type applies to the query regardless of the
     * flush mode type in use for the entity manager.
     * @param flushMode  flush mode
     * @return the same query instance
     */
    Query setFlushMode(FlushModeType flushMode);

    /**
     * Get the flush mode in effect for the query execution. 
     * If a flush mode has not been set for the query object, 
     * returns the flush mode in effect for the entity manager.
     * @return flush mode
     * @since 2.0
     */
    FlushModeType getFlushMode();

    /**
     * Set the lock mode type to be used for the query execution.
     * @param lockMode  lock mode
     * @return the same query instance
     * @throws IllegalStateException if the query is found not to be 
     *         a Jakarta Persistence query language SELECT query
     *         or a CriteriaQuery query
     * @since 2.0
     */
    Query setLockMode(LockModeType lockMode);

    /**
     * Get the current lock mode for the query.  Returns null if a lock
     * mode has not been set on the query object.
     * @return lock mode
     * @throws IllegalStateException if the query is found not to be
     *         a Jakarta Persistence query language SELECT query or
     *         a Criteria API query
     * @since 2.0
     */
    LockModeType getLockMode();

    /**
     * Set the cache retrieval mode that is in effect during
     * query execution. This cache retrieval mode overrides the
     * cache retrieve mode in use by the entity manager.
     * @param cacheRetrieveMode cache retrieval mode
     * @return the same query instance
     * @since 3.2
     */
    Query setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the cache storage mode that is in effect during
     * query execution. This cache storage mode overrides the
     * cache storage mode in use by the entity manager.
     * @param cacheStoreMode cache storage mode
     * @return the same query instance
     * @since 3.2
     */
    Query setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * The cache retrieval mode that will be in effect during
     * query execution.
     * @since 3.2
     */
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The cache storage mode that will be in effect during
     * query execution.
     * @since 3.2
     */
    CacheStoreMode getCacheStoreMode();

    /**
     * Set the query timeout.
     * @param timeout the timeout, or null to indicate no timeout
     * @return the same query instance
     * @since 3.2
     */
    Query setTimeout(Integer timeout);

    /**
     * The query timeout.
     * @since 3.2
     */
    Integer getTimeout();

    /**
     * Return an object of the specified type to allow access to 
     * the provider-specific API.  If the provider's query 
     * implementation does not support the specified class, the 
     * <code>PersistenceException</code> is thrown.
     * @param cls  the class of the object to be returned.  This is
     *             normally either the underlying query 
     *             implementation class or an interface that it 
     *             implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not support
     *         the call
     * @since 2.0
     */
    <T> T unwrap(Class<T> cls);
}
