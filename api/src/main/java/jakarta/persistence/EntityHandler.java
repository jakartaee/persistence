/*
 * Copyright (c) 2008, 2025 Contributors to the Eclipse Foundation
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
import jakarta.persistence.sql.ResultSetMapping;

import java.util.List;
import java.util.Map;

/**
 * Declares operations common to {@link EntityManager} and
 * {@link EntityAgent}. The status of an entity instance
 * returned by an operation declared by this interface
 * depends on the semantics of the subtype.
 * <ul>
 * <li>For an {@link EntityAgent}, there is no associated
 *     persistence context, and so every entity instance
 *     returned by any operation is in the detached state.
 *     Operations on an entity agent never return the same
 *     entity instance twice.
 * <li>For an {@link EntityManager}, every entity instance
 *     returned by any operation declared by this interface
 *     is in the managed state and belongs to the persistence
 *     context associated with the entity manager. The
 *     instance remains in the managed state until that
 *     entity manager is closed and its persistence context
 *     is destroyed. An {@code EntityManager} is required
 *     to ensure that if an entity instance representing a
 *     given record already exists in its associated
 *     persistence context, then exactly that instance is
 *     returned by every operation that returns an entity
 *     representing the record.
 * </ul>
 * <p>Note that, in a Jakarta EE container environment, a
 * transaction-scoped entity manager which is accessed
 * outside the scope of a container transaction is destroyed
 * immediately after it is called. And so such an entity
 * manager may appear to return unmanaged instances to its
 * client. But this is merely a consequence of the extremely
 * short lifetime of its persistence context.
 *
 * <p>Operations which return entity instances are, by default,
 * permitted to retrieve state from the second-level cache, if
 * any, or, in the case of an {@code EntityManager}, directly
 * from the persistence context, without directly accessing
 * the database. However, any such operation must respect:
 * <ul>
 * <li>any {@linkplain LockModeType lock mode} requested as an
 *     argument to the operation or by calling
 *     {@link TypedQuery#setLockMode(LockModeType)}, as well as
 * <li>any {@linkplain CacheRetrieveMode cache mode} given
 *     as an {@linkplain FindOption optional argument} or by
 *     calling {@link #setCacheRetrieveMode} or
 *     {@link TypedQuery#setCacheRetrieveMode}.
 * </ul>
 * <p>Thus, access to the database might be required even when
 * the entity is available in the persistence context or in the
 * second-level cache.
 *
 * <p>Outside the Jakarta EE container environment, the safest
 * and simplest way to obtain an {@code EntityHandler} is by
 * calling {@link EntityManagerFactory#runInTransaction} or
 * {@link EntityManagerFactory#callInTransaction}.
 * {@snippet :
 * Book book =
 *         factory.callInTransaction(EntityAgent.class,
 *                 agent -> agent.get(Book.class, isbn))
 * }
 *
 * @since 4.0
 */
public interface EntityHandler extends AutoCloseable {

    /**
     * Retrieve an entity representing the record with the
     * given identifier.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The identifier of the entity to retrieve
     *
     * @return an entity instance with the given identifier
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if the given identifier is not a
     *         non-null instance of the identifier type of the
     *         given entity class
     * @throws EntityNotFoundException if no record with the
     *         given identifier exists in the database
     * @throws PersistenceException if the record could not be
     *         read from the database
     *
     * @apiNote Conceptually the same as {@linkplain #find}, except this
     * form throws {@linkplain EntityNotFoundException} rather than return null.
     *
     * @since 4.0
     */
    <T> T get(Class<T> entityClass, Object id);

    /**
     * Retrieve an entity representing the record with the
     * given identifier, using the specified
     * {@linkplain FindOption options}. If the given options
     * include a {@link LockModeType}, obtain the lock level
     * specified by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The identifier of the entity to retrieve
     * @param options Standard and vendor-specific options
     *
     * @return an entity instance with the given identifier
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if the given identifier is not a
     *         non-null instance of the identifier type of the
     *         given entity class
     * @throws EntityNotFoundException if no record with the
     *         given identifier exists in the database
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the
     *         record could not be read from the database
     *
     * @apiNote Conceptually the same as {@linkplain #find}, except this
     * form throws {@linkplain EntityNotFoundException} rather than return null.
     *
     * @since 4.0
     */
    <T> T get(Class<T> entityClass, Object id, FindOption... options);

    /**
     * Retrieve an entity representing the record with the
     * given identifier, fetching associations specified by
     * the given {@linkplain EntityGraph load graph}, and
     * using the specified {@linkplain FindOption options}.
     * If the given options include a {@link LockModeType},
     * obtain the lock level specified by the given lock
     * mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param graph The {@linkplain EntityGraph load graph}
     * @param id The identifier of the entity to retrieve
     * @param options Standard and vendor-specific options
     *
     * @return an entity instance with the given identifier
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if the given identifier is not a
     *         non-null instance of the identifier type of the
     *         given entity class
     * @throws EntityNotFoundException if no record with the
     *         given identifier exists in the database
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the
     *         record could not be read from the database
     *
     * @apiNote Conceptually the same as {@linkplain #find}, except this
     * form throws {@linkplain EntityNotFoundException} rather than return null.
     *
     * @since 4.0
     */
    <T> T get(EntityGraph<T> graph, Object id, FindOption... options);

    /**
     * Retrieve entity instances representing the records
     * with the given identifiers, using the specified
     * {@linkplain FindOption options}, and returning the
     * instances in a list where the position of an instance
     * in the list matches the position of its identifier in
     * the given array. If the given options include a
     * {@link LockModeType}, obtain the lock level specified
     * by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param entityClass The class of the entity to retrieve
     * @param ids The identifiers of the entities to retrieve
     * @param options Standard and vendor-specific options
     * @return an ordered list of entity instances
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if one of the given identifiers is not
     *         a non-null instance of the identifier type of
     *         the given entity class
     * @throws EntityNotFoundException if no record exists in
     *         the database for one of the given identifiers
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if a
     *         record could not be read from the database
     *
     * @apiNote Conceptually the same as {@linkplain #findMultiple}, except this
     * form throws {@linkplain EntityNotFoundException} rather than return nulls.
     *
     * @since 4.0
     */
    <T> List<T> getMultiple(Class<T> entityClass, List<?> ids,
                            FindOption... options);

    /**
     * Retrieve entity instances representing the records with
     * the given identifiers of the root entity of the given
     * {@linkplain EntityGraph load graph}, using the specified
     * {@linkplain FindOption options}, and returning the
     * instances in a list where the position of an instance
     * in the list matches the position of its identifier in
     * the given array, and fetching associations specified by
     * the load graph.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param graph The {@linkplain EntityGraph load graph}
     * @param ids The identifiers of the entities to retrieve
     * @param options Standard and vendor-specific options
     * @return an ordered list of entity instances
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if one of the given identifiers is not
     *         a non-null instance of the identifier type of
     *         the given entity class
     * @throws EntityNotFoundException if no record exists in
     *         the database for one of the given identifiers
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if a
     *         record could not be read from the database
     *
     * @apiNote Conceptually the same as {@linkplain #findMultiple}, except this
     * form throws {@linkplain EntityNotFoundException} rather than return nulls.
     *
     * @since 4.0
     */
    <T> List<T> getMultiple(EntityGraph<T> graph, List<?> ids,
                            FindOption... options);

    /**
     * Retrieve an entity representing the record with the
     * given identifier, or return {@code null} if there is
     * no such record in the database.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The identifier of the entity to retrieve
     * @return an entity instance with the given identifier,
     *         or {@code null} if there is no matching record
     *         in the database
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, or if the given identifier is not a
     *         non-null instance of the identifier type of the
     *         given entity class
     * @throws PersistenceException if the record could not be
     *         read from the database
     *
     * @since 1.0
     */
    <T> T find(Class<T> entityClass, Object id);

    /**
     * Retrieve an entity representing the record with the
     * given identifier, or return {@code null} if there is
     * no such record in the database, using the specified
     * {@linkplain FindOption options}. If the given options
     * include a {@link LockModeType}, obtain the lock level
     * specified by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The identifier of the entity to retrieve
     * @param options Standard and vendor-specific options
     * @return an entity instance with the given identifier,
     *         or {@code null} if there is no matching record
     *         in the database
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, if the given identifier is not a non-null
     *         instance of the identifier type of the given
     *         entity class, or if the given options are
     *         contradictory
     * @throws TransactionRequiredException if any lock mode
     *         other than {@link LockModeType#NONE NONE} is
     *         specified and there is no transaction
     *         associated with this handler
     * @throws OptimisticLockException if an optimistic version
     *         check fails
     * @throws PessimisticLockException if a pessimistic lock
     *         could not be obtained and the transaction is
     *         rolled back
     * @throws LockTimeoutException if a pessimistic lock
     *         could not be obtained and only the statement
     *         is rolled back
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the
     *         record could not be read from the database
     *
     * @since 3.2
     */
    <T> T find(Class<T> entityClass, Object id, FindOption... options);

    /**
     * Retrieve an instance of the root entity of the given
     * {@link EntityGraph} representing the record with the
     * given identifier, or return {@code null} if there is
     * no such record in the database, using the specified
     * {@linkplain FindOption options} and interpreting the
     * {@code EntityGraph} as a load graph. If the given
     * options include a {@link LockModeType}, obtain the
     * lock level specified by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param graph The {@linkplain EntityGraph load graph}
     * @param id The identifier of the entity to retrieve
     * @param options Standard and vendor-specific options
     * @return an entity instance with the given identifier,
     *         or {@code null} if there is no matching record
     *         in the database
     *
     * @throws IllegalArgumentException if the root entity
     *         of the given graph is not an entity class
     *         belonging to the persistence unit, if the
     *         given identifier is not a non-null instance of
     *         the identifier type of the given entity class,
     *         or if the given options are contradictory
     * @throws TransactionRequiredException if any lock mode
     *         other than {@link LockModeType#NONE NONE} is
     *         specified and there is no transaction
     *         associated with this handler
     * @throws OptimisticLockException if an optimistic version
     *         check fails
     * @throws PessimisticLockException if a pessimistic lock
     *         could not be obtained and the transaction is
     *         rolled back
     * @throws LockTimeoutException if a pessimistic lock
     *         could not be obtained and only the statement
     *         is rolled back
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the root entity of the given graph
     *         or if the record could not be read from the database
     *
     * @since 3.2
     */
    <T> T find(EntityGraph<T> graph, Object id, FindOption... options);

    /**
     * Retrieve entity instances representing the records
     * with the given identifiers, using the specified
     * {@linkplain FindOption options}, returning the
     * instances in a list where the position of an instance
     * in the list matches the position of its identifier in
     * the given array, and the list contains a null value
     * if there is no record matching a given identifier.
     * If the given options include a {@link LockModeType},
     * obtain the lock level specified by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param entityClass The class of the entity to retrieve
     * @param ids The identifiers of the entities to retrieve
     * @param options Standard and vendor-specific options
     * @return an ordered list of entity instances with the
     *         given identifiers, with {@code null} in
     *         positions where there is no matching record
     *         in the database
     *
     * @throws IllegalArgumentException if the given class is
     *         not an entity class belonging to the persistence
     *         unit, if one of the given identifiers is not a
     *         non-null instance of the identifier type of the
     *         given entity class, or if the given options are
     *         contradictory
     * @throws TransactionRequiredException if any lock mode
     *         other than {@link LockModeType#NONE NONE} is
     *         specified and there is no transaction
     *         associated with this handler
     * @throws OptimisticLockException if an optimistic version
     *         check fails
     * @throws PessimisticLockException if a pessimistic lock
     *         could not be obtained and the transaction is
     *         rolled back
     * @throws LockTimeoutException if a pessimistic lock
     *         could not be obtained and only the statement
     *         is rolled back
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if a
     *         record could not be read from the database
     *
     * @since 4.0
     */
    <T> List<T> findMultiple(Class<T> entityClass, List<?> ids,
                             FindOption... options);

    /**
     * Retrieve entity instances representing the records with
     * the given identifiers of the root entity of the given
     * {@link EntityGraph}, using the specified
     * {@linkplain FindOption options} and interpreting the
     * {@code EntityGraph} as a load graph, returning entity
     * instances in a list where the position of an instance
     * in the list matches the position of its identifier in
     * the given array, and the list contains a null value
     * if there is no record matching a given identifier.
     * If the given options include a {@link LockModeType},
     * obtain the lock level specified by the given lock mode.
     * <p>If an implementation does not recognize a given
     * vendor-specific {@linkplain FindOption option}, it
     * must silently ignore the option.
     * <p>Portable applications should not rely on the
     * standard {@linkplain Timeout timeout option}.
     * Depending on the database in use and the locking
     * mechanisms used by the provider, this option may or
     * may not be observed.
     *
     * @param graph The {@linkplain EntityGraph load graph}
     * @param ids The identifiers of the entities to retrieve
     * @param options Standard and vendor-specific options
     * @return an ordered list of entity instances with the
     *         given identifiers, with {@code null} in
     *         positions where there is no matching record
     *         in the database
     *
     * @throws IllegalArgumentException if the root entity
     *         of the given graph is not an entity class
     *         belonging to the persistence unit, if one of
     *         the given identifiers is not a non-null
     *         instance of the identifier type of the given
     *         entity class, or if the given options are
     *         contradictory
     * @throws TransactionRequiredException if any lock mode
     *         other than {@link LockModeType#NONE NONE} is
     *         specified and there is no transaction
     *         associated with this handler
     * @throws OptimisticLockException if an optimistic version
     *         check fails
     * @throws PessimisticLockException if a pessimistic lock
     *         could not be obtained and the transaction is
     *         rolled back
     * @throws LockTimeoutException if a pessimistic lock
     *         could not be obtained and only the statement
     *         is rolled back
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the root entity of the given graph
     *         or if a record could not be read from the database
     *
     * @since 4.0
     */
    <T> List<T> findMultiple(EntityGraph<T> graph, List<?> ids,
                             FindOption... options);

    /**
     * Set the default {@linkplain CacheRetrieveMode cache retrieval
     * mode} for this {@code EntityHandler}.
     * @param cacheRetrieveMode The new default cache retrieval mode
     * @since 3.2
     */
    void setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode);

    /**
     * Set the default {@linkplain CacheStoreMode cache storage mode}
     * for this {@code EntityHandler}.
     * @param cacheStoreMode The new default cache storage mode
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
     * @param propertyName The name of the property or hint
     * @param value The value for the property or hint
     * @throws IllegalArgumentException if the property or hint name
     *         is recognized by the implementation, but the second
     *         argument is not a valid value for that hint
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
     * Jakarta Persistence query language statement, usually
     * an {@code UPDATE} or {@code DELETE} statement.
     * <p>If the given query is a {@code SELECT} statement,
     * the query results might be packaged as arrays:
     * <ul>
     * <li>if the query contains a single item in its select
     *     list, each query result is the value of that item,
     *     or
     * <li>otherwise, if the query contains multiple items in
     *     its select list, each query result is packaged in
     *     an array of type {@code Object[]}, with the array
     *     elements corresponding by position with the items
     *     of the select list.
     * </ul>
     * @param qlString A Jakarta Persistence query string
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid
     * @apiNote This method should be used to execute an
     * {@code UPDATE} or {@code DELETE} statement. For a
     * {@code SELECT} statement, an overload which specifies a
     * {@linkplain #createQuery(String, Class) result class}
     * or {@linkplain #createQuery(String, EntityGraph)
     * entity graph} is usually more appropriate.
     */
    Query createQuery(String qlString);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * criteria query.
     * @param criteriaQuery A criteria query object
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the criteria query is
     *         found to be invalid
     * @since 2.0
     */
    <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * criteria query, which may be a union or intersection of
     * top-level queries.
     * @param selectQuery A criteria query object
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the criteria query is
     *         found to be invalid
     * @since 3.2
     */
    <T> TypedQuery<T> createQuery(CriteriaSelect<T> selectQuery);

    /**
     * Create an instance of {@link Query} for executing a criteria
     * update query.
     * @param updateQuery A criteria update query object
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the update query is
     *         found to be invalid
     * @since 2.1
     */
    Query createQuery(CriteriaUpdate<?> updateQuery);

    /**
     * Create an instance of {@link Query} for executing a criteria
     * delete query.
     * @param deleteQuery A criteria delete query object
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the delete query is
     *         found to be invalid
     * @since 2.1
     */
    Query createQuery(CriteriaDelete<?> deleteQuery);

    /**
     * Create an instance of {@link TypedQuery} for executing
     * a Jakarta Persistence query language {@code SELECT}
     * statement and returning instances of the given result
     * class. Either:
     * <ol>
     * <li>the select list of the query contains only a single
     *     item, which must be assignable to the result class,
     * <li>the result class is {@code Object[].class}, or
     * <li>the result class is a non-abstract class or record
     *     type with a constructor with the same number of
     *     parameters as the query has items in its select list,
     *     and the constructor parameter types exactly match the
     *     types of the corresponding items in the select list.
     * </ol>
     * <p>In the first case, each query result is returned
     * directly to the caller. In the second case, each query
     * result is packaged in an array with the array elements
     * corresponding by position with the items of the query
     * select list. In the third case, each query result is
     * automatically packaged in a new instance of the result
     * class by calling the matching constructor.
     * @param qlString A Jakarta Persistence query string
     * @param resultClass The result class
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid, or if the query result is
     *         found to not be assignable to the specified type
     *         and the specified type does not have a suitable
     *         constructor
     * @since 2.0
     */
    <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

    /**
     * Create an instance of {@link TypedQuery} for executing
     * a Jakarta Persistence query language {@code SELECT}
     * statement, specifying an {@link EntityGraph} which is
     * interpreted as a load graph. The select list of the
     * query must contain only a single item, which must be
     * assignable to the root type of the given entity graph.
     * @param qlString A Jakarta Persistence query string
     * @param resultGraph The {@linkplain EntityGraph load graph}
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if the query string is
     *         found to be invalid, or if the query result is
     *         found to not be assignable to the root type of
     *         the entity graph
     * @since 4.0
     */
    <T> TypedQuery<T> createQuery(String qlString, EntityGraph<T> resultGraph);

    /**
     * Create an instance of {@link Query} for executing a named
     * query written in the Jakarta Persistence query language,
     * usually an {@code UPDATE} or {@code DELETE} statement, or
     * in native SQL, usually an {@code INSERT}, {@code UPDATE},
     * {@code MERGE}, or {@code DELETE} statement.
     * <ul>
     * <li>If the named query is a {@code SELECT} statement
     *     written in the Jakarta Persistence query language,
     *     the query results are packaged according to the
     *     {@linkplain NamedQuery#resultClass result class}
     *     specified by the {@link NamedQuery} annotation, or
     * <li>If the named query is written in native SQL and
     *     returns a result set, the query results are
     *     interpreted and packaged according to the
     *     {@linkplain NamedNativeQuery#resultClass result class}
     *     and {@linkplain NamedNativeQuery#resultSetMapping
     *     result set mapping} specified by the
     *     {@link NamedNativeQuery} annotation.
     * </ul>
     * @param name The name of a query defined in metadata
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if a query has not been
     *         defined with the given name, or if the query string is
     *         found to be invalid
     * @see NamedQuery
     * @see NamedNativeQuery
     * @since 1.0
     */
    Query createNamedQuery(String name);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * named query written in the Jakarta Persistence query language
     * or in native SQL, returning instances of the given result class.
     * <ul>
     * <li>If the named query is written in the Jakarta Persistence
     *     query language, the result class is interpreted as if it
     *     were an argument of {@link #createQuery(String, Class)}.
     * <li>If the named query is written in native SQL, the result
     *     class is interpreted as if it were an argument of
     *     {@link #createNativeQuery(String, Class)}.
     * </ul>
     * <p>The given result class overrides any result class specified
     * by the {@link NamedQuery} annotation or {@link NamedNativeQuery}
     * annotation which declares the named query.
     * @param name The name of a query defined in metadata
     * @param resultClass The type of the query result
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if a query has not been
     *         defined with the given name, if the query string is
     *         found to be invalid, or if the query result is found to
     *         not be assignable to the specified type
     * @since 2.0
     */
    <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass);

    /**
     * Create an instance of {@link Query} for executing a named
     * query written in the Jakarta Persistence query language or
     * in native SQL.
     * @param reference a reference to the query defined in metadata
     * @return the new query instance
     * @throws IllegalArgumentException if a query has not been
     *         defined, or if the query string is found to be
     *         invalid, or if the query result is found to not be
     *         assignable to the specified type
     * @see EntityManagerFactory#getNamedQueries()
     * @see NamedQuery
     * @see NamedNativeQuery
     * @since 4.0
     */
    Query createQuery(QueryReference reference);

    /**
     * Create an instance of {@link TypedQuery} for executing a
     * named query written in the Jakarta Persistence query language
     * or in native SQL.
     * <ul>
     * <li>If the named query is a {@code SELECT} statement
     *     written in the Jakarta Persistence query language,
     *     the query results are packaged according to the
     *     {@linkplain NamedQuery#resultClass result class}
     *     specified by the {@link NamedQuery} annotation, or
     * <li>If the named query is written in native SQL and
     *     returns a result set, the query results are
     *     interpreted and packaged according to the
     *     {@linkplain NamedNativeQuery#resultClass result class}
     *     and {@linkplain NamedNativeQuery#resultSetMapping
     *     result set mapping} specified by the
     *     {@link NamedNativeQuery} annotation.
     * </ul>
     * @param reference A reference to the query defined in metadata
     * @return An instance of {@link TypedQuery} which may be used
     *         to execute the given query
     * @throws IllegalArgumentException if a query has not been
     *         defined, if the query string is found to be
     *         invalid, or if the query result is found to not be
     *         assignable to the specified type
     * @see EntityManagerFactory#getNamedQueries(Class)
     * @see NamedQuery
     * @see NamedNativeQuery
     * @since 3.2
     */
    <T> TypedQuery<T> createQuery(TypedQueryReference<T> reference);

    /**
     * Create an instance of {@link Query} for executing a
     * native SQL statement, usually an {@code INSERT},
     * {@code UPDATE}, {@code MERGE}, or {@code DELETE}
     * statement.
     * <p>If the given query produces a result set, the query
     * results might be packaged as arrays:
     * <ul>
     * <li>if the query contains a single column in its result
     *     set, each query result is the value of that column,
     *     or
     * <li>otherwise, if the query contains multiple columns
     *     in its result set, each query result is packaged in
     *     an array of type {@code Object[]}, with the array
     *     elements corresponding by position with the columns
     *     of the select list.
     * </ul>
     * <p>Column values are obtained according to the default
     * type mappings defined by the JDBC specification.
     * @param sqlString A native SQL query string
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @since 1.0
     * @apiNote This overload is most appropriate when used to
     * execute a statement which returns a row count. For a
     * {@code SELECT} statement, an overload which specifies a
     * {@linkplain #createNativeQuery(String, Class) result class}
     * or {@linkplain #createNativeQuery(String, ResultSetMapping)
     * result set mapping} is usually more appropriate.
     */
    Query createNativeQuery(String sqlString);

    /**
     * Create an instance of {@link TypedQuery} for executing a native
     * SQL query which produces a result set, returning instances of the
     * given result class. Either:
     * <ul>
     * <li>the result class is an entity class and is interpreted as a
     *     managed {@linkplain EntityResult entity result} with implicit
     *     field mappings determined by the names of the columns in the
     *     result set and the object/relational mapping of the entity,
     * <li>the result class is the class of a {@linkplain Basic basic}
     *     type and the result set must have a single column which is
     *     interpreted as a {@linkplain ColumnResult scalar result},
     * <li>the result class is a non-{@code abstract} class or record
     *     type with a constructor with the same number of parameters
     *     as the result set has columns, and is interpreted as a
     *     {@linkplain ConstructorResult constructor result} including
     *     all the columns of the result set, or
     * <li>the result class is {@code Object[].class} and each query
     *     result is packaged in an array of type {@code Object[]},
     *     with the array elements corresponding by position with the
     *     columns of the select list and column values obtained
     *     according to the default type mappings defined by the JDBC
     *     specification.
     * </ul>
     * @param sqlString A native SQL query string
     * @param resultClass The type of the query result
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @since 1.0
     */
    <T> TypedQuery<T> createNativeQuery(String sqlString, Class<T> resultClass);

    /**
     * Create an instance of {@link Query} for executing a native SQL
     * query, using the {@linkplain SqlResultSetMapping mapping} with
     * the given {@linkplain SqlResultSetMapping#name name} to interpret
     * the JDBC result set.
     * @param sqlString A native SQL query string
     * @param resultSetMapping The name of the result set mapping
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @see SqlResultSetMapping
     * @since 1.0
     * @apiNote Use of this overloaded form of the method results in a
     * type cast of each query result in client code. As an alternative,
     * obtain a typed reference to the mapping from the static metamodel
     * and use {@link #createNativeQuery(String, ResultSetMapping)}.
     */
    Query createNativeQuery(String sqlString, String resultSetMapping);

    /**
     * Create an instance of {@link TypedQuery} for executing a native
     * SQL query, using the given {@link ResultSetMapping} to interpret
     * the JDBC result set.
     * @param sqlString A native SQL query string
     * @param resultSetMapping The result set mapping
     * @return An instance of {@link Query} which may be used
     *         to execute the given query
     * @since 4.0
     */
    <T> TypedQuery<T> createNativeQuery(String sqlString,
                                        ResultSetMapping<T> resultSetMapping);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>If the stored procedure returns one or more result sets, any
     * result set is returned as a list of type {@code Object[]}.
     * @param name The name assigned to the stored procedure query in
     *             metadata
     * @return An instance of {@link StoredProcedureQuery} which may be
     *         used to execute the stored procedure
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
     * @param procedureName The name of the stored procedure in the
     *                      database
     * @return An instance of {@link StoredProcedureQuery} which may be
     *         used to execute the stored procedure
     * @throws IllegalArgumentException if a stored procedure of the
     *         given name does not exist (or if query execution will
     *         fail)
     * @since 2.1
     */
    StoredProcedureQuery createStoredProcedureQuery(String procedureName);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database, explicitly specifying the
     * result class for every result set returned by the stored procedure.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>The given result classes must be specified in the order in which
     * the corresponding result sets are returned by the stored procedure
     * invocation. For each given result class, either:
     * <ul>
     * <li>the result class is an entity class and is interpreted as a
     *     managed {@linkplain EntityResult entity result} with implicit
     *     field mappings determined by the names of the columns in the
     *     result set and the object/relational mapping of the entity,
     * <li>the result class is the class of a {@linkplain Basic basic}
     *     type and the result set must have a single column which is
     *     interpreted as a {@linkplain ColumnResult scalar result}, or
     * <li>the result class must be a non-abstract class or record type
     *     with a constructor with the same number of parameters as the
     *     result set has columns, and is interpreted as a
     *     {@linkplain ConstructorResult constructor result} including
     *     all the columns of the result set.
     * </ul>
     * @param procedureName The name of the stored procedure in the
     *                      database
     * @param resultClasses The classes to which the result sets
     *                      produced by the stored procedure are mapped
     * @return An instance of {@link StoredProcedureQuery} which may be
     *         used to execute the stored procedure
     * @throws IllegalArgumentException if a stored procedure of the
     *         given name does not exist (or if query execution will
     *         fail)
     * @since 2.1
     * @apiNote Use of this overloaded form of the method results
     * in a redundant type cast of each query result in client code.
     * Instead, use {@link #createStoredProcedureQuery(String)} and
     * pass each result class individually as an argument to
     * {@link StoredProcedureQuery#getResultList(Class)},
     * {@link StoredProcedureQuery#getSingleResult(Class)}, or
     * {@link StoredProcedureQuery#getSingleResultOrNull(Class)}.
     */
    StoredProcedureQuery createStoredProcedureQuery(
            String procedureName, Class<?>... resultClasses);

    /**
     * Create an instance of {@link StoredProcedureQuery} for executing
     * a stored procedure in the database, explicitly specifying the
     * {@linkplain SqlResultSetMapping#name name} of a result set
     * {@linkplain SqlResultSetMapping mapping} for every result set
     * returned by the stored procedure.
     * <p>Parameters must be registered before the stored procedure can
     * be executed.
     * <p>The given result set mappings must be specified in the order
     * in which the corresponding result sets are returned by the stored
     * procedure invocation.
     * @param procedureName The name of the stored procedure in the
     *                      database
     * @param resultSetMappings The names of the result set mappings
     *                          to be used to map result sets returned
     *                          by the stored procedure
     * @return An instance of {@link StoredProcedureQuery} which may be
     *         used to execute the stored procedure
     * @throws IllegalArgumentException if a stored procedure or
     *         result set mapping of the given name does not exist
     *         (or the query execution will fail)
     * @see SqlResultSetMapping
     * @since 2.1
     * @apiNote Use of this overloaded form of the method results
     * in a type cast of each query result in client code. As an
     * alternative, use {@link #createStoredProcedureQuery(String)}
     * and pass each result mapping individually as an argument to
     * {@link StoredProcedureQuery#getResultList(ResultSetMapping)},
     * {@link StoredProcedureQuery#getSingleResult(ResultSetMapping)}, or
     * {@link StoredProcedureQuery#getSingleResultOrNull(ResultSetMapping)}.
     */
    StoredProcedureQuery createStoredProcedureQuery(
            String procedureName, String... resultSetMappings);

    /**
     * Return an object of the specified type to allow access to
     * a provider-specific API. If the provider implementation
     * of {@code EntityHandler} does not support the given type,
     * the {@link PersistenceException} is thrown.
     * @param type The class of the object to be returned.
     *             This is usually either the underlying class
     *             implementing {@code EntityHandler} or an
     *             interface it implements
     * @return An instance of the specified class
     * @throws PersistenceException if the provider does not
     *         support the given type
     * @since 2.0
     */
    <T> T unwrap(Class<T> type);

    /**
     * Close an application-managed {@code EntityHandler}.
     * <p>After invocation of {@code close()}, every method of
     * the {@code EntityHandler} instance and of any instance
     * of {@link Query}, {@link TypedQuery}, or
     * {@link StoredProcedureQuery} obtained from it throws
     * the {@link IllegalStateException}, except for
     * {@link #getProperties()}, {@link #getTransaction()},
     * and {@link #isOpen()} (which returns false).
     * <p>If this method is called when the {@code EntityHandler}
     * is joined to an active transaction, any managed entities
     * remain managed until the transaction completes.
     * @throws IllegalStateException if the {@code EntityHandler}
     *         is container-managed
     * @since 1.0
     */
    void close();

    /**
     * Determine whether the {@code EntityHandler} is open.
     * @return true until the {@code EntityHandler} has been closed
     * @since 1.0
     */
    boolean isOpen();

    /**
     * Return the resource-level {@link EntityTransaction} object.
     * The {@code EntityTransaction} instance may be used serially
     * to begin and commit multiple transactions.
     * @return An instance of {@link EntityTransaction}
     * @throws IllegalStateException if invoked on a
     *         {@linkplain PersistenceUnitTransactionType#JTA JTA}
     *         entity manager or entity agent
     * @since 1.0
     */
    EntityTransaction getTransaction();

    /**
     * The {@linkplain EntityManagerFactory entity manager factory}
     * which created this {@code EntityHandler}.
     * @return The {@link EntityManagerFactory}
     * @throws IllegalStateException if the {@code EntityHandler}
     *         has been closed
     * @since 2.0
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * Obtain an instance of {@link CriteriaBuilder} which may be
     * used to construct {@link CriteriaQuery} objects.
     * @return an instance of {@link CriteriaBuilder}
     * @throws IllegalStateException if the {@code EntityHandler}
     *         has been closed
     * @see EntityManagerFactory#getCriteriaBuilder()
     * @since 2.0
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * Obtain an instance of the {@link Metamodel} interface which
     * provides access to metamodel objects describing the managed
     * types belonging to the persistence unit.
     * @return An instance of {@link Metamodel}
     * @throws IllegalStateException if the {@code EntityHandler}
     *         has been closed
     * @since 2.0
     */
    Metamodel getMetamodel();

    /**
     * Create a new mutable {@link EntityGraph}, allowing programmatic
     * definition of the graph.
     * @param rootType the root entity type of the new graph
     * @return a trivial entity graph with only a root node
     * @see jakarta.persistence.metamodel.EntityType#createEntityGraph()
     * @since 2.1
     */
    <T> EntityGraph<T> createEntityGraph(Class<T> rootType);

    /**
     * Obtain a mutable copy of the named {@link EntityGraph}.
     * @param graphName the name of an existing entity graph
     * @return the entity graph with the given name
     * @throws IllegalArgumentException if there is no entity
     *         graph with the given name
     * @since 2.1
     */
    EntityGraph<?> getEntityGraph(String graphName);

    /**
     * Obtain a mutable copy of the named {@link EntityGraph}
     * whose root type is exactly the given entity type.
     * @param rootType the root entity type of the graph
     * @param graphName the name of an existing entity graph
     * @return the entity graph with the given name
     * @throws IllegalArgumentException if there is no entity
     *         graph with the given name, or if the entity
     *         graph with the given name does not have exactly
     *         the given root entity type
     * @since 4.0
     */
    <T> EntityGraph<T> getEntityGraph(Class<T> rootType, String graphName);

    /**
     * Return all named {@link EntityGraph}s that are defined for
     * the given entity class type.
     * @param entityClass An entity class
     * @return A list of all entity graphs whose root entity type
     *         is a supertype of the given entity class
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
     * transaction. The given action should close any resources it creates but should
     * not close the connection itself, nor commit or roll back the transaction. If
     * the given action throws an exception, the persistence provider must mark the
     * transaction for rollback.
     * {@snippet :
     * entityManager.runWithConnection((Connection connection) -> {
     *     try (var statement = connection.createStatement()) {
     *         statement.execute("set constraints all deferred");
     *     }
     * });
     * }
     * @param action The action to execute
     * @param <C> The connection type, usually {@code java.sql.Connection}
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
     * any resources it creates but should not close the connection itself, nor
     * commit or roll back the transaction. If the given action throws an exception,
     * the persistence provider must mark the transaction for rollback.
     * {@snippet :
     * LocalTime currentTimeOnServer =
     *         entityManager.callWithConnection((Connection connection) -> {
     *             try (var statement = connection.createStatement()) {
     *                 try (var resultSet = statement.executeQuery("select current_time")) {
     *                     resultSet.next();
     *                     return resultSet.getObject(1, LocalTime.class);
     *                 }
     *             }
     *         });
     * }
     * @param function The function to call
     * @param <C> The connection type, usually {@code java.sql.Connection}
     * @param <T> The type of result returned by the function
     * @return The value returned by {@link ConnectionFunction#apply}.
     * @throws PersistenceException wrapping the checked {@link Exception} thrown by
     *         {@link ConnectionFunction#apply}, if any
     * @since 3.2
     */
    <C,T> T callWithConnection(ConnectionFunction<C, T> function);

}