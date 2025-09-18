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
//     Gavin King      - 4.0

package jakarta.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

/**
 * Declares operations common to {@link EntityManager}
 * and {@link EntityAgent}.
 *
 * @since 4.0
 */
public interface EntityHandler extends AutoCloseable {

    /**
     * Set the default {@linkplain CacheRetrieveMode cache retrieval
     * mode} for this {@code EntityHandler}.
     * @param cacheRetrieveMode cache retrieval mode
     * @since 3.2
     */
    void setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the default {@linkplain CacheStoreMode cache storage mode}
     * for this {@code EntityHandler}.
     * @param cacheStoreMode cache storage mode
     * @since 3.2
     */
    void setCacheStoreMode(CacheStoreMode cacheStoreMode);

    /**
     * The cache retrieval mode for this {@code EntityHandler}.
     * @since 3.2
     */
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The cache storage mode for this {@code EntityHandler}.
     * @since 3.2
     */
    CacheStoreMode getCacheStoreMode();

    /**
     * Set an {@code EntityHandler}-scoped property or hint.
     * If a vendor-specific property or hint is not recognized, it is
     * silently ignored.
     * @param propertyName name of the property or hint
     * @param value  value for the property or hint
     * @throws IllegalArgumentException if the property or hint name
     *         is recognized by the implementation, but the second
     *         argument is not valid value
     * @since 2.0
     */
    void setProperty(String propertyName, Object value);

    /**
     * The properties and hints and their associated values which
     * are in effect for this {@code EntityHandler}. Modifying the
     * contents of the returned map does not change the configuration
     * in effect.
     * @return a map of properties and hints currently in effect
     * @since 2.0
     */
    Map<String, Object> getProperties();

    /**
     * Create an instance of {@link Query} for executing a
     * Jakarta Persistence query language statement.
     * @param qlString a Jakarta Persistence query string
     * @return the new query instance
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid
     */
    Query createQuery(String qlString);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * criteria query.
     * @param criteriaQuery  a criteria query object
     * @return the new query instance
     * @throws IllegalArgumentException if the criteria query is
     *         found to be invalid
     * @since 2.0
     */
    <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * criteria query, which may be a union or intersection of
     * top-level queries.
     * @param selectQuery  a criteria query object
     * @return the new query instance
     * @throws IllegalArgumentException if the criteria query is
     *         found to be invalid
     * @since 3.2
     */
    <T> TypedQuery<T> createQuery(CriteriaSelect<T> selectQuery);

    /**
     * Create an instance of {@link Query} for executing a criteria
     * update query.
     * @param updateQuery  a criteria update query object
     * @return the new query instance
     * @throws IllegalArgumentException if the update query is
     *         found to be invalid
     * @since 2.1
     */
    Query createQuery(CriteriaUpdate<?> updateQuery);

    /**
     * Create an instance of {@link Query} for executing a criteria
     * delete query.
     * @param deleteQuery  a criteria delete query object
     * @return the new query instance
     * @throws IllegalArgumentException if the delete query is
     *         found to be invalid
     * @since 2.1
     */
    Query createQuery(CriteriaDelete<?> deleteQuery);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * Jakarta Persistence query language statement.
     * The select list of the query must contain only a single
     * item, which must be assignable to the type specified by
     * the {@code resultClass} argument.
     * @param qlString a Jakarta Persistence query string
     * @param resultClass the type of the query result
     * @return the new query instance
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid or if the query result is
     *         found to not be assignable to the specified type
     * @since 2.0
     */
    <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

    /**
     * Create an instance of {@link Query} for executing a named
     * query written in the Jakarta Persistence query language or
     * in native SQL.
     * @param name the name of a query defined in metadata
     * @return the new query instance
     * @throws IllegalArgumentException if a query has not been
     *         defined with the given name or if the query string is
     *         found to be invalid
     * @see NamedQuery
     * @see NamedNativeQuery
     */
    Query createNamedQuery(String name);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * Jakarta Persistence query language named query.
     * The select list of the query must contain only a single
     * item, which must be assignable to the type specified by
     * the {@code resultClass} argument.
     * @param name the name of a query defined in metadata
     * @param resultClass the type of the query result
     * @return the new query instance
     * @throws IllegalArgumentException if a query has not been
     *         defined with the given name or if the query string is
     *         found to be invalid or if the query result is found to
     *         not be assignable to the specified type
     * @since 2.0
     */
    <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * named query written in the Jakarta Persistence query
     * language or in native SQL.
     * @param reference a reference to the query defined in metadata
     * @return the new query instance
     * @throws IllegalArgumentException if a query has not been
     *         defined, or if the query string is found to be
     *         invalid, or if the query result is found to not be
     *         assignable to the specified type
     * @see EntityManagerFactory#getNamedQueries(Class)
     * @see NamedQuery
     * @see NamedNativeQuery
     */
    <T> TypedQuery<T> createQuery(TypedQueryReference<T> reference);

    /**
     * Create an instance of {@link Query} for executing a native
     * SQL statement, e.g., for update or delete.
     *
     * <p>If the query is not an update or delete query, query
     * execution will result in each row of the SQL result being
     * returned as a result of type {@code Object[]} (or a result
     * of type {@code Object} if there is only one column in the
     * select list.) Column values are returned in the order of
     * their occurrence in the select list and default JDBC type
     * mappings are applied.
     * @param sqlString a native SQL query string
     * @return the new query instance
     */
    Query createNativeQuery(String sqlString);

    /**
     * Create an instance of {@link Query} for executing a native
     * SQL query.
     *
     * <p><em>In the next release of this API, the return type of this
     * method will change to {@code TypedQuery<T>}.</em>
     * @param sqlString a native SQL query string
     * @param resultClass the type of the query result
     * @return the new query instance
     */
    <T> Query createNativeQuery(String sqlString, Class<T> resultClass);

    /**
     * Create an instance of {@link Query} for executing
     * a native SQL query.
     * @param sqlString a native SQL query string
     * @param resultSetMapping the name of the result set mapping
     * @return the new query instance
     */
    Query createNativeQuery(String sqlString, String resultSetMapping);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>If the stored procedure returns one or more result sets, any
     * result set is returned as a list of type {@code Object[]}.
     * @param name name assigned to the stored procedure query in
     *        metadata
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if no query has been defined
     *         with the given name
     * @since 2.1
     */
    StoredProcedureQuery createNamedStoredProcedureQuery(String name);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing a
     * stored procedure in the database.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>If the stored procedure returns one or more result sets, any
     * result set is returned as a list of type {@code Object[]}.
     * @param procedureName name of the stored procedure in the database
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure of the
     *         given name does not exist (or if query execution will
     *         fail)
     * @since 2.1
     */
    StoredProcedureQuery createStoredProcedureQuery(String procedureName);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>The {@code resultClass} arguments must be specified in the
     * order in which the result sets is returned by the stored procedure
     * invocation.
     * @param procedureName name of the stored procedure in the database
     * @param resultClasses classes to which the result sets
     *        produced by the stored procedure are to be mapped
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure of the
     *         given name does not exist (or if query execution will
     *         fail)
     * @since 2.1
     */
    StoredProcedureQuery createStoredProcedureQuery(
            String procedureName, Class<?>... resultClasses);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>The {@code resultSetMapping} arguments must be specified in
     * the order in which the result sets is returned by the stored
     * procedure invocation.
     * @param procedureName name of the stored procedure in the
     *        database
     * @param resultSetMappings the names of the result set mappings
     *        to be used in mapping result sets
     *        returned by the stored procedure
     * @return the new stored procedure query instance
     * @throws IllegalArgumentException if a stored procedure or
     *         result set mapping of the given name does not exist
     *         (or the query execution will fail)
     */
    StoredProcedureQuery createStoredProcedureQuery(
            String procedureName, String... resultSetMappings);

    /**
     * Indicate to the entity manager that a JTA transaction is
     * active and join the persistence context to it.
     * <p>This method should be called on a JTA application
     * managed entity manager that was created outside the scope
     * of the active transaction or on an entity manager of type
     * {@link SynchronizationType#UNSYNCHRONIZED} to associate
     * it with the current JTA transaction.
     * @throws TransactionRequiredException if there is no active
     *         transaction
     */
    void joinTransaction();

    /**
     * Determine whether the entity manager is joined to the
     * current transaction. Returns false if the entity manager
     * is not joined to the current transaction or if no
     * transaction is active.
     * @return boolean
     * @since 2.1
     */
    boolean isJoinedToTransaction();

    /**
     * Return an object of the specified type to allow access to
     * a provider-specific API. If the provider implementation
     * of {@code EntityHandler} does not support the given type,
     * the {@link PersistenceException} is thrown.
     * @param cls  the class of the object to be returned.
     *             This is usually either the underlying class
     *             implementing {@code EntityHandler} or an
     *             interface it implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not
     *         support the given type
     * @since 2.0
     */
    <T> T unwrap(Class<T> cls);

    /**
     * Return the underlying provider object for the
     * {@link EntityHandler}, if available. The result of this
     * method is implementation-specific.
     * <p>The {@code unwrap} method is to be preferred for new
     * applications.
     * @return the underlying provider object
     */
    Object getDelegate();

    /**
     * Close an application-managed {@code EntityHandler}.
     * <p>After invocation of {@code close()}, every method of
     * the {@code EntityHandler} instance and of any instance
     * of {@link Query}, {@link TypedQuery}, or
     * {@link StoredProcedureQuery} obtained from it throws
     * the {@link IllegalStateException}, except for
     * {@link #getProperties()}, {@link #getTransaction()},
     * and {@link #isOpen()} (which returns false).
     * <p>If this method is called when the entity manager is
     * joined to an active transaction, the persistence context
     * remains managed until the transaction completes.
     * @throws IllegalStateException if the entity manager is
     *         container-managed
     */
    void close();

    /**
     * Determine whether the {@code EntityHandler} is open.
     * @return true until the {@code EntityHandler} has been closed
     */
    boolean isOpen();

    /**
     * Return the resource-level {@link EntityTransaction} object.
     * The {@code EntityTransaction} instance may be used serially
     * to begin and commit multiple transactions.
     * @return EntityTransaction instance
     * @throws IllegalStateException if invoked on a JTA entity
     *         manager
     */
    EntityTransaction getTransaction();

    /**
     * The {@linkplain EntityManagerFactory entity manager factory}
     * which created this entity manager.
     * @return the {@link EntityManagerFactory}
     * @throws IllegalStateException if the entity manager has
     *         been closed
     * @since 2.0
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * Obtain an instance of {@link CriteriaBuilder} which may be
     * used to construct {@link CriteriaQuery} objects.
     * @return an instance of {@link CriteriaBuilder}
     * @throws IllegalStateException if the entity manager has
     *         been closed
     * @see EntityManagerFactory#getCriteriaBuilder()
     * @since 2.0
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * Obtain an instance of the {@link Metamodel} interface which
     * provides access to metamodel objects describing the managed
     * types belonging to the persistence unit.
     * @return an instance of {@link Metamodel}
     * @throws IllegalStateException if the entity manager has
     *         been closed
     * @since 2.0
     */
    Metamodel getMetamodel();

    /**
     * Create a new mutable {@link EntityGraph}, allowing dynamic
     * definition of an entity graph.
     * @param rootType class of entity graph
     * @return entity graph
     * @since 2.1
     */
    <T> EntityGraph<T> createEntityGraph(Class<T> rootType);

    /**
     * Obtain a mutable copy of a named {@link EntityGraph}, or
     * return null if there is no entity graph with the given
     * name.
     * @param graphName name of an entity graph
     * @return entity graph
     * @since 2.1
     */
    EntityGraph<?> createEntityGraph(String graphName);

    /**
     * Obtain a named {@link EntityGraph}. The returned instance
     * of {@code EntityGraph} should be considered immutable.
     * @param graphName  name of an existing entity graph
     * @return named entity graph
     * @throws IllegalArgumentException if there is no entity
     *         of graph with the given name
     * @since 2.1
     */
    EntityGraph<?> getEntityGraph(String graphName);

    /**
     * Return all named {@link EntityGraph}s that are defined for
     * the given entity class type.
     * @param entityClass  entity class
     * @return list of all entity graphs defined for the entity
     * @throws IllegalArgumentException if the class is not an entity
     * @since 2.1
     */
    <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass);

    /**
     * Execute the given action using the database connection underlying this
     * {@code EntityHandler}. Usually, the connection is a JDBC connection, but a
     * provider might support some other native connection type, and is not required
     * to support {@code java.sql.Connection}. If this {@code EntityHandler} is
     * associated with a transaction, the action is executed in the context of the
     * transaction. The given action should close any resources it creates, but should
     * not close the connection itself, nor commit or roll back the transaction. If
     * the given action throws an exception, the persistence provider must mark the
     * transaction for rollback.
     * @param action the action
     * @param <C> the connection type, usually {@code java.sql.Connection}
     * @throws PersistenceException wrapping the checked {@link Exception} thrown by
     *         {@link ConnectionConsumer#accept}, if any
     * @since 3.2
     */
    <C> void runWithConnection(ConnectionConsumer<C> action);

    /**
     * Call the given function and return its result using the database connection
     * underlying this {@code EntityHandler}. Usually, the connection is a JDBC
     * connection, but a provider might support some other native connection type,
     * and is not required to support {@code java.sql.Connection}. If this
     * {@code EntityHandler} is associated with a transaction, the function is
     * executed in the context of the transaction. The given function should close
     * any resources it creates, but should not close the connection itself, nor
     * commit or roll back the transaction. If the given action throws an exception,
     * the persistence provider must mark the transaction for rollback.
     * @param function the function
     * @param <C> the connection type, usually {@code java.sql.Connection}
     * @param <T> the type of result returned by the function
     * @return the value returned by {@link ConnectionFunction#apply}.
     * @throws PersistenceException wrapping the checked {@link Exception} thrown by
     *         {@link ConnectionFunction#apply}, if any
     * @since 3.2
     */
    <C,T> T callWithConnection(ConnectionFunction<C, T> function);
}
