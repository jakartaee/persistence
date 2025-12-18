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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0


package jakarta.persistence;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaQuery;

/**
 * Interface used to interact with the persistence context.
 *
 * <p>An instance of {@code EntityManager} must be obtained from
 * an {@link EntityManagerFactory}, and is only able to manage
 * persistence of entities belonging to the associated persistence
 * unit. In the Jakarta EE environment, an entity manager with a
 * lifecycle managed by the container may be obtained by dependency
 * injection.
 *
 * <ul>
 * <li>An {@code EntityManager} obtained directly from an
 *     {@code EntityManagerFactory} is never thread safe, and it is
 *     always wrong to share a reference to such an entity manager
 *     between multiple concurrently executing threads.
 * <li>On the other hand, in the Jakarta EE container environment,
 *     a reference to a container-managed, transaction-scoped
 *     entity manager obtained by injection is safe to invoke
 *     concurrently from distinct threads. The container redirects
 *     invocations to distinct instances of {@code EntityManager}
 *     based on transaction affinity. Even in this scenario, the
 *     underlying persistence context must never be shared between
 *     threads.
 * </ul>
 *
 * <p>An application-managed {@code EntityManager} may be created
 * via a call to {@link EntityManagerFactory#createEntityManager()}.
 * The {@code EntityManager} must be explicitly closed via a call
 * to {@link #close()}, to allow resources to be cleaned up by the
 * persistence provider. This approach places almost complete
 * responsibility for cleanup and exception management on the client,
 * and is thus considered quite error-prone. It is much safer to use
 * the methods {@link EntityManagerFactory#runInTransaction} and
 * {@link EntityManagerFactory#callInTransaction}.
 * {@snippet :
 * entityManagerFactory.runInTransaction(entityManager -> {
 *     // do work in a persistence context
 *     ...
 * });
 * }
 *
 * <p>In the Jakarta EE environment, a container-managed
 * {@link EntityManager} may be obtained by dependency injection,
 * using {@link PersistenceContext}.
 * {@snippet :
 * // inject the container-managed entity manager
 * @PersistenceContext(unitName="orderMgt")
 * EntityManager entityManager;
 * }
 *
 * <p>If the persistence unit has
 * {@linkplain PersistenceUnitTransactionType#RESOURCE_LOCAL
 * resource local} transaction management, transactions must
 * be managed using the {@link EntityTransaction} obtained by
 * calling {@link #getTransaction()}.
 *
 * <p>A complete idiom for custom application management of
 * the {@link EntityManager} and its associated resource-local
 * {@link EntityTransaction} is as follows:
 * {@snippet :
 * EntityManager entityManager = entityManagerFactory.createEntityManager();
 * EntityTransaction transaction = entityManager.getTransaction();
 * try {
 *     transaction.begin();
 *     // do work
 *     ...
 *     transaction.commit();
 * }
 * catch (Exception e) {
 *     if (transaction.isActive()) transaction.rollback();
 *     throw e;
 * }
 * finally {
 *     entityManager.close();
 * }
 * }
 *
 * <p>Each {@code EntityManager} instance is associated with a
 * distinct <em>persistence context</em>. A persistence context
 * is a set of entity instances in which for any given persistent
 * entity identity (defined by an entity type and primary key)
 * there is at most one entity instance. The entity instances
 * associated with a persistence context are considered managed
 * objects, with a well-defined lifecycle under the control of
 * the persistence provider.
 *
 * <p>Any entity instance can be characterized as being in one of
 * the following lifecycle states:
 * <ul>
 * <li>A <em>new</em> entity has no persistent identity, and is
 *     not yet associated with any persistence context.
 * <li>A <em>managed</em> entity is an instance with a persistent
 *     identity that is currently associated with a persistence
 *     context.
 * <li>A <em>detached</em> entity is an instance with a persistent
 *     identity that is not (or no longer) associated with any
 *     active persistence context.
 * <li>A <em>removed</em> entity is an instance with a persistent
 *     identity, and associated with a persistence context, that
 *     is scheduled for removal from the database upon transaction
 *     commit.
 * </ul>
 *
 * <p>The {@code EntityManager} API is used to perform operations
 * that affect the state of the persistence context, or that modify
 * the lifecycle state of individual entity instances. The client
 * may {@linkplain #persist} and {@linkplain #remove} instances,
 * {@linkplain #find(Class, Object) find} entities by their primary
 * key, and execute {@linkplain #createQuery(String) queries} which
 * range over entity types. A given entity may be disassociated
 * from the persistence context by calling {@link #detach}, and a
 * persistence context may be completely cleared, detaching all
 * its entities, by calling {@link #clear()}.
 *
 * <p>The client may also make changes to the state of an entity
 * instance by mutating the entity directly, or it may request
 * that the state of a detached instance be {@linkplain #merge
 * merged}, replacing the state of a managed instance with the
 * same persistent identity. Note that there is no explicit
 * "update" operation; since an entity is a managed object,
 * modifications to its persistent fields and properties are
 * automatically detected, as long as it is associated with an
 * active persistence context.
 *
 * <p>Modifications to the state of entities associated with a
 * persistence context are not immediately synchronized with the
 * database. Synchronization happens during a process called
 * <em>flush</em>. The timing of the flush process depends on the
 * {@linkplain FlushModeType flush mode}, which may be set
 * explicitly by calling {@link #setFlushMode(FlushModeType)}.
 * <ul>
 * <li>For {@link FlushModeType#COMMIT}, the persistence context
 *     is flushed before the transaction commits.
 * <li>For {@link FlushModeType#AUTO}, which is the default, the
 *     persistence context must also be flushed before execution
 *     of any query whose result set would be affected by
 *     unflushed modifications to entities associated with the
 *     persistence context.
 * </ul>
 * The client may force an immediate flush to occur by calling
 * {@link #flush()}.
 *
 * <p>At any given moment, a persistence context might hold an
 * optimistic or pessimistic <em>lock</em> on an entity instance.
 * The full range of possible lock types is enumerated by
 * {@link LockModeType}. Some operations of this interface,
 * including the methods {@link #lock(Object, LockModeType)},
 * {@link #refresh(Object, LockModeType)}, and
 * {@link #find(Class, Object, LockModeType)}, accept an explicit
 * {@link LockModeType}, allowing the client to request a specific
 * type of lock.
 *
 * <p>Interaction of the persistence context (or first-level cache)
 * with the second-level cache, if any, may be controlled by
 * calling {@link #setCacheRetrieveMode(CacheRetrieveMode)} and
 * {@link #setCacheStoreMode(CacheStoreMode)}.
 *
 * <p>Some operations accept one or more built-in and vendor-specific
 * options:
 * <ul>
 * <li>{@link #find(Class, Object, FindOption...)} and 
 *     {@link #find(EntityGraph, Object, FindOption...)} accept
 *     {@link FindOption}s,
 * <li>{@link #refresh(Object, RefreshOption...)} accepts
 *     {@link RefreshOption}s, and
 * <li>{@link #lock(Object, LockModeType, LockOption...)} accepts
 *     {@link LockOption}s.
 * </ul>
 *
 * @see Query
 * @see TypedQuery
 * @see CriteriaQuery
 * @see PersistenceContext
 * @see StoredProcedureQuery
 * @see EntityManagerFactory
 * 
 * @since 1.0
 */
public non-sealed interface EntityManager extends EntityHandler {

    /**
     * Make a new entity instance managed and persistent, resulting in
     * its insertion in the database when the persistence context is
     * synchronized with the database, or make a removed entity managed,
     * undoing the effect of a previous call to {@link #remove(Object)}.
     * This operation cascades to every entity related by an association
     * marked {@link CascadeType#PERSIST cascade=PERSIST}. If the given
     * entity instance is already managed, that is, if it already belongs
     * to this persistence context and has not been marked for removal,
     * it is itself ignored, but the operation still cascades.
     * @param entity  a new, managed, or removed entity instance
     * @throws EntityExistsException if the given entity is detached
     * (if the entity is detached, the {@code EntityExistsException}
     * may be thrown when the persist operation is invoked, or the
     * {@code EntityExistsException} or another {@code PersistenceException}
     * may be thrown at flush or commit time)
     * @throws IllegalArgumentException if the given instance is not an
     *         entity
     * @throws TransactionRequiredException if there is no transaction
     *         when invoked on a container-managed entity manager that
     *         is of type {@link PersistenceContextType#TRANSACTION}
     * @throws PersistenceException if the entity class has a generated
     *         identifier and an identifier could not be generated
     */
    void persist(Object entity);
    
    /**
     * Merge the state of the given new or detached entity instance
     * into the current persistence context, resulting in, respectively,
     * an insert or possible update when the persistence context is
     * synchronized with the database. Return a managed instance with
     * the same persistent state as the given entity instance, but a
     * distinct Java object identity. If the given entity is detached,
     * the returned entity has the same persistent identity. This
     * operation cascades to every entity related by an association
     * marked {@link CascadeType#MERGE cascade=MERGE}. If the given
     * entity instance is managed, that is, if it belongs to this
     * persistence context and has not been marked for removal, it is
     * itself ignored, but the operation still cascades, and it is
     * returned directly.
     * @param entity  a new, managed, or detached entity instance
     * @return the managed instance that the state was merged to
     * @throws IllegalArgumentException if the instance is not an entity
     *         or is a removed entity
     * @throws TransactionRequiredException if there is no transaction
     *         when invoked on a container-managed entity manager of
     *         type {@link PersistenceContextType#TRANSACTION}
     * @throws OptimisticLockException if an optimistic locking conflict
     *         is detected (note that optimistic version checking might be
     *         deferred until changes are flushed to the database)
     * @throws PersistenceException if a record could not be read from
     *         the database
     *
     */
    <T> T merge(T entity);

    /**
     * Mark a managed entity instance as removed, resulting in its deletion
     * from the database when the persistence context is synchronized with
     * the database. This operation cascades to every entity related by an
     * association marked {@link CascadeType#REMOVE cascade=REMOVE}. If the
     * given entity instance is already removed, it is ignored. If the
     * given entity is new, it is itself ignored, but the operation still
     * cascades.
     * @param entity  a managed, new, or removed entity instance
     * @throws IllegalArgumentException if the instance is not an entity
     *         or is a detached entity
     * @throws TransactionRequiredException if invoked on a
     *         container-managed entity manager of type
     *         {@link PersistenceContextType#TRANSACTION} and there is
     *         no transaction
     * @throws OptimisticLockException if an optimistic locking conflict
     *         is detected (note that optimistic version checking might be
     *         deferred until changes are flushed to the database)
     */
    void remove(Object entity);

    /**
     * Find by primary key, using the specified properties.
     * Search for an entity of the specified class and primary key.
     * If the entity instance is contained in the persistence
     * context, it is returned from there.
     * If a vendor-specific property or hint is not recognized,
     * it is silently ignored.
     * @param entityClass  entity class
     * @param primaryKey   primary key
     * @param properties  standard and vendor-specific properties
     *        and hints
     * @return the found entity instance or null if the entity does
     *         not exist
     * @throws IllegalArgumentException if the first argument does
     *         not denote an entity type, or if the second argument
     *         is not a valid type for that entity's primary key or
     *         is null
     * @throws PersistenceException if the record could not be
     *         read from the database
     * @since 2.0
     */
    <T> T find(Class<T> entityClass, Object primaryKey,
               Map<String, Object> properties);

    /**
     * Find by primary key and lock the entity, using the specified
     * properties. Search for an entity of the specified class and
     * primary key and lock it with respect to the specified lock type.
     * If the entity instance is contained in the persistence context,
     * it is returned from there.  
     * <p> If the entity is found within the persistence context and
     * the lock mode type is pessimistic and the entity has a version
     * attribute, the persistence provider must perform optimistic
     * version checks when obtaining the database lock. If these checks
     * fail, the {@link OptimisticLockException} is thrown.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized, 
     * it is silently ignored.  
     * <p>Portable applications should not rely on the standard
     * timeout hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not be
     * observed.
     * @param entityClass  entity class
     * @param primaryKey  primary key
     * @param lockMode  lock mode
     * @param properties  standard and vendor-specific properties
     *        and hints
     * @return the found entity instance or null if the entity does
     *         not exist
     * @throws IllegalArgumentException if the first argument does
     *         not denote an entity type, or the second argument is
     *         not a valid type for that entity's primary key or
     *         is null
     * @throws TransactionRequiredException if there is no 
     *         transaction and a lock mode other than {@code NONE} is
     *         specified or if invoked on an entity manager which has
     *         not been joined to the current transaction and a lock
     *         mode other than {@code NONE} is specified
     * @throws OptimisticLockException if the optimistic version check
     *         fails
     * @throws PessimisticLockException if pessimistic locking fails
     *         and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *        only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the
     *         record could not be read from the database
     * @since 2.0
     */
    <T> T find(Class<T> entityClass, Object primaryKey,
               LockModeType lockMode,
               Map<String, Object> properties);

    /**
     * Obtain a reference to an instance of the given entity class
     * with the given primary key, whose state may be lazily fetched.
     * <p>If the requested instance does not exist in the database,
     * the {@link EntityNotFoundException} is thrown when the
     * instance state is first accessed.
     * (The persistence provider runtime is permitted but not
     * required to throw the {@code EntityNotFoundException} when
     * {@code getReference()} is called.)
     * <p>This operation allows the application to create an
     * association to an entity without loading its state from the
     * database.
     * <p>The application should not expect the instance state to
     * be available upon detachment unless it was accessed by the
     * application while the entity manager was open.
     * @param entityClass  entity class
     * @param primaryKey  primary key
     * @return a reference to the entity instance
     * @throws IllegalArgumentException if the first argument does
     *         not denote an entity type, or the second argument is
     *         not a valid type for that entity's primary key or
     *         is null
     * @throws EntityNotFoundException if the entity state cannot
     *         be accessed
     */
    <T> T getReference(Class<T> entityClass, Object primaryKey);

    /**
     * Obtain a reference to an instance of the entity class of the
     * given object, with the same primary key as the given object,
     * whose state may be lazily fetched. The given object may be
     * persistent or detached, but may be neither new nor removed.
     * <p>If the requested instance does not exist in the database,
     * the {@link EntityNotFoundException} is thrown when the
     * instance state is first accessed.
     * (The persistence provider runtime is permitted but not
     * required to throw the {@code EntityNotFoundException} when
     * {@code getReference()} is called.)
     * <p>This operation allows the application to create an
     * association to an entity without loading its state from the
     * database.
     * <p>The application should not expect the instance state to
     * be available upon detachment unless it was accessed by the
     * application while the entity manager was open.
     * @param entity  a persistent or detached entity instance
     * @return a reference to the entity instance
     * @throws IllegalArgumentException if the given object is not
     *         an entity, or if it is neither persistent nor detached
     * @throws EntityNotFoundException if the entity state cannot be
     *         accessed
     * @since 3.2
     */
    <T> T getReference(T entity);

    /**
     * Synchronize changes held in the persistence context to the
     * underlying database.
     * @throws TransactionRequiredException if there is
     *        no transaction or if the entity manager has not been
     *        joined to the current transaction
     * @throws PersistenceException if the flush fails
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected
     */
    void flush();

    /**
     * Set the {@linkplain FlushModeType flush mode} that applies to
     * all objects contained in the persistence context.
     * @param flushMode  flush mode
     */
    void setFlushMode(FlushModeType flushMode);

    /**
     * Get the {@linkplain FlushModeType flush mode} that applies to
     * all objects contained in the persistence context.
     * @return the current {@link FlushModeType}
     */
    FlushModeType getFlushMode();

    /**
     * Lock an entity instance belonging to the persistence context,
     * obtaining the specified {@linkplain LockModeType lock mode}.
     * <p>If a pessimistic lock mode type is specified and the entity
     * contains a version attribute, the persistence provider must 
     * also perform optimistic version checks when obtaining the 
     * database lock. If these checks fail, the
     * {@link OptimisticLockException} is thrown.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback
     * </ul>
     * @param entity  a managed entity instance
     * @param lockMode  lock mode
     * @throws IllegalArgumentException if the instance is not an
     *         entity or is a detached entity
     * @throws TransactionRequiredException if there is no 
     *         transaction or if invoked on an entity manager which
     *         has not been joined to the current transaction
     * @throws EntityNotFoundException if the entity does not exist 
     *         in the database when pessimistic locking is
     *         performed
     * @throws OptimisticLockException if the optimistic version check
     *         fails
     * @throws PessimisticLockException if pessimistic locking fails 
     *         and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *         only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class
     */
    void lock(Object entity, LockModeType lockMode);

    /**
     * Lock an entity instance belonging to the persistence context,
     * obtaining the specified {@linkplain LockModeType lock mode},
     * using the specified properties.
     * <p>If a pessimistic lock mode type is specified and the entity
     * contains a version attribute, the persistence provider must 
     * also perform optimistic version checks when obtaining the 
     * database lock. If these checks fail, the
     * {@link OptimisticLockException} is thrown.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} ia thrown if the database
     *     locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized, 
     * it is silently ignored.  
     * <p>Portable applications should not rely on the standard
     * timeout hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not be
     * observed.
     * @param entity  a managed entity instance
     * @param lockMode  lock mode
     * @param properties  standard and vendor-specific properties
     *        and hints
     * @throws IllegalArgumentException if the instance is not an
     *         entity or is a detached entity
     * @throws TransactionRequiredException if there is no 
     *         transaction or if invoked on an entity manager which
     *         has not been joined to the current transaction
     * @throws EntityNotFoundException if the entity does not exist 
     *         in the database when pessimistic locking is performed
     * @throws OptimisticLockException if the optimistic version check
     *         fails
     * @throws PessimisticLockException if pessimistic locking fails 
     *         and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *         only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class
     * @since 2.0
     */
    void lock(Object entity, LockModeType lockMode,
              Map<String, Object> properties);

    /**
     * Lock an entity instance belonging to the persistence context,
     * obtaining the specified {@linkplain LockModeType lock mode},
     * using the specified {@linkplain LockOption options}.
     * <p>If a pessimistic lock mode type is specified and the entity
     * contains a version attribute, the persistence provider must
     * also perform optimistic version checks when obtaining the
     * database lock. If these checks fail, the
     * {@link OptimisticLockException} is thrown.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback
     * </ul>
     * <p>If a vendor-specific {@link LockOption} is not recognized,
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard
     * {@linkplain Timeout timeout option}. Depending on the database
     * in use and the locking mechanisms used by the provider, the
     * option may or may not be observed.
     * @param entity  a managed entity instance
     * @param lockMode  lock mode
     * @param options  standard and vendor-specific options
     * @throws IllegalArgumentException if the instance is not an
     *         entity or is a detached entity
     * @throws TransactionRequiredException if there is no
     *         transaction or if invoked on an entity manager which
     *         has not been joined to the current transaction
     * @throws EntityNotFoundException if the entity does not exist
     *         in the database when pessimistic locking is
     *         performed
     * @throws OptimisticLockException if the optimistic version
     *         check fails
     * @throws PessimisticLockException if pessimistic locking fails
     *         and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *         only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class
     * @since 3.2
     */
    void lock(Object entity, LockModeType lockMode,
              LockOption... options);

    /**
     * Refresh the state of the given managed entity instance from
     * the database, overwriting unflushed changes made to the entity,
     * if any. This operation cascades to every entity related by an
     * association marked {@link CascadeType#REFRESH cascade=REFRESH}.
     * @param entity  a managed entity instance
     * @throws IllegalArgumentException if the instance is not
     *         an entity or the entity is not managed
     * @throws TransactionRequiredException if there is no
     *         transaction when invoked on a container-managed
     *         entity manager of type
     *         {@link PersistenceContextType#TRANSACTION}
     * @throws EntityNotFoundException if the entity no longer
     *         exists in the database
     * @throws PersistenceException if the record could not be read
     *         from the database
     */
    void refresh(Object entity);

    /**
     * Refresh the state of the given managed entity instance from
     * the database, using the specified properties, and overwriting
     * unflushed changes made to the entity, if any. This operation
     * cascades to every entity related by an association marked
     * {@link CascadeType#REFRESH cascade=REFRESH}.
     * <p>If a vendor-specific property or hint is not recognized,
     * it is silently ignored. 
     * @param entity  a managed entity instance
     * @param properties  standard and vendor-specific properties 
     *        and hints
     * @throws IllegalArgumentException if the instance is not 
     *         an entity or the entity is not managed
     * @throws TransactionRequiredException if there is no
     *         transaction when invoked on a container-managed
     *         entity manager of type
     *         {@link PersistenceContextType#TRANSACTION}
     * @throws EntityNotFoundException if the entity no longer 
     *         exists in the database
     * @throws PersistenceException if the record could not be read
     *         from the database
     * @since 2.0
     */
    void refresh(Object entity,
                 Map<String, Object> properties);

    /**
     * Refresh the state of the given managed entity instance from
     * the database, overwriting unflushed changes made to the entity,
     * if any, and obtain the given {@linkplain LockModeType lock mode}.
     * This operation cascades to every entity related by an association
     * marked {@link CascadeType#REFRESH cascade=REFRESH}.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback.
     * </ul>
     * @param entity  a managed entity instance
     * @param lockMode  lock mode
     * @throws IllegalArgumentException if the instance is not an entity
     *         or if the entity is not managed
     * @throws TransactionRequiredException if invoked on a 
     *         container-managed entity manager of type
     *         {@link PersistenceContextType#TRANSACTION} when there is
     *         no transaction; if invoked on an extended entity manager
     *         when there is no transaction and a lock mode other than
     *         {@link LockModeType#NONE} was specified; or if invoked
     *         on an extended entity manager that has not been joined
     *         to the current transaction and any lock mode other than
     *         {@code NONE} was specified
     * @throws EntityNotFoundException if the entity no longer exists
     *         in the database
     * @throws PessimisticLockException if pessimistic locking fails
     *         and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *         only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the record
     *         could not be read from the database
     * @since 2.0
     */
    void refresh(Object entity, LockModeType lockMode);

    /**
     * Refresh the state of the given managed entity instance from
     * the database, overwriting unflushed changes made to the entity,
     * if any, and obtain the given {@linkplain LockModeType lock mode},
     * using the specified properties. This operation cascades to every
     * entity related by an association marked {@link CascadeType#REFRESH
     * cascade=REFRESH}.
     * <p>If the lock mode type is pessimistic, and the entity instance
     * is found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback.
     * </ul>
     * <p>If a vendor-specific property or hint is not recognized, 
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard
     * timeout hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not be
     * observed.
     * @param entity  a managed entity instance
     * @param lockMode  lock mode
     * @param properties  standard and vendor-specific properties
     *        and hints
     * @throws IllegalArgumentException if the instance is not an
     *         entity or if the entity is not managed
     * @throws TransactionRequiredException if invoked on a 
     *         container-managed entity manager of type
     *         {@link PersistenceContextType#TRANSACTION} when there is
     *         no transaction; if invoked on an extended entity manager
     *         when there is no transaction and a lock mode other than
     *         {@link LockModeType#NONE} was specified; or if invoked
     *         on an extended entity manager that has not been joined
     *         to the current transaction and any lock mode other than
     *         {@code NONE} was specified
     * @throws EntityNotFoundException if the entity no longer exists
     *        in the database
     * @throws PessimisticLockException if pessimistic locking fails
     *        and the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and
     *        only the statement is rolled back
     * @throws PersistenceException if the given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the record
     *         could not be read from the database
     * @since 2.0
     */
    void refresh(Object entity, LockModeType lockMode,
                 Map<String, Object> properties);

    /**
     * Refresh the state of the given managed entity instance from the
     * database, using the specified {@linkplain RefreshOption options},
     * overwriting changes made to the entity, if any. If the supplied
     * options include a {@link LockModeType}, lock the given entity,
     * obtaining the given lock mode. This operation cascades to every
     * entity related by an association marked {@link CascadeType#REFRESH
     * cascade=REFRESH}.
     * <p>If the lock mode type is pessimistic, and the entity instance is
     * found but cannot be locked:
     * <ul>
     * <li>the {@link PessimisticLockException} is thrown if the
     *     database locking failure causes transaction-level rollback
     * <li>the {@link LockTimeoutException} is thrown if the database
     *     locking failure causes only statement-level rollback.
     * </ul>
     * <p>If a vendor-specific {@link RefreshOption} is not recognized,
     * it is silently ignored.
     * <p>Portable applications should not rely on the standard
     * {@linkplain Timeout timeout option}. Depending on the database in
     * use and the locking mechanisms used by the provider, the hint may
     * or may not be observed.
     * @param entity  a managed entity instance
     * @param options  standard and vendor-specific options
     * @throws IllegalArgumentException if the instance is not an entity
     *         or if the entity is not managed
     * @throws TransactionRequiredException if invoked on a
     *         container-managed entity manager of type
     *         {@link PersistenceContextType#TRANSACTION} when there is
     *         no transaction; if invoked on an extended entity manager
     *         when there is no transaction and a lock mode other than
     *         {@link LockModeType#NONE} was specified; or if invoked
     *         on an extended entity manager that has not been joined
     *         to the current transaction and any lock mode other than
     *         {@code NONE} was specified
     * @throws EntityNotFoundException if the entity no longer exists in
     *         the database
     * @throws PessimisticLockException if pessimistic locking fails and
     *         the transaction is rolled back
     * @throws LockTimeoutException if pessimistic locking fails and only
     *         the statement is rolled back
     * @throws PersistenceException if a given
     *         {@linkplain LockModeType lock mode type} is not
     *         supported for the given entity class or if the record
     *         could not be read from the database
     * @since 3.2
     */
    void refresh(Object entity,
                 RefreshOption... options);

    /**
     * Clear the persistence context, causing all managed entities to
     * become detached. Changes made to entities that have not already
     * been flushed to the database will never be made persistent.
     */
    void clear();

    /**
     * Evict the given managed or removed entity from the persistence
     * context, causing the entity to become immediately detached.
     * Unflushed changes made to the entity, if any, including deletion
     * of the entity, will never be synchronized to the database.
     * Managed entities which reference the given entity continue to
     * reference it. This operation cascades to every entity related by
     * an association marked {@link CascadeType#DETACH cascade=DETACH}.
     * If the given entity instance is new or detached, that is, if it
     * is not associated with this persistence context, it is ignored.
     * @param entity  a managed or removed entity instance
     * @throws IllegalArgumentException if the instance is not an 
     *         entity
     * @since 2.0
     */
    void detach(Object entity);

    /**
     * Determine if the given object is a managed entity instance
     * belonging to the current persistence context.
     * @param entity  entity instance
     * @return boolean value indicating if entity belongs to the
     *         persistence context
     * @throws IllegalArgumentException if not an entity
     */
    boolean contains(Object entity);

    /**
     * Get the current {@linkplain LockModeType lock mode} held by
     * this persistence context on the given managed entity instance.
     * @param entity  a managed entity instance
     * @return the lock mode currently held
     * @throws TransactionRequiredException if there is no active
     *         transaction, or if the entity manager has not been
     *         joined to the current transaction
     * @throws IllegalArgumentException if a transaction is active
     *         but the given instance is not a managed entity
     * @since 2.0
     */
    LockModeType getLockMode(Object entity);

    /**
     * Join the current active JTA transaction.
     * <p>This method should be called on a JTA application-managed
     * {@code EntityManager} that was created outside the scope of
     * the active transaction or on an {@code EntityManager} of
     * type {@link SynchronizationType#UNSYNCHRONIZED} to associate
     * it with the current JTA transaction.
     * @throws TransactionRequiredException if there is no active
     *         transaction
     * @since 1.0
     */
    void joinTransaction();

    /**
     * Determine whether the {@code EntityManager} is joined to the
     * current transaction. Returns false if the {@code EntityManager}
     * is not joined to the current transaction or if no
     * transaction is active.
     * @return True if the {@code EntityManager} is joined to the
     *         current transaction, or false otherwise
     * @since 2.1
     */
    boolean isJoinedToTransaction();

    /**
     * Obtain a mutable copy of a named {@link EntityGraph} or
     * return null if there is no entity graph with the given
     * name.
     * @param graphName The name of an entity graph
     * @return A mutable copy of the entity graph, or null
     * @since 2.1
     * @deprecated Use {@link #getEntityGraph(String)} instead.
     */
    @Deprecated(since = "4.0", forRemoval = true)
    EntityGraph<?> createEntityGraph(String graphName);

    /**
     * Return the underlying provider object for the
     * {@link EntityManager}, if available. The result of this
     * method is implementation-specific.
     * <p>The {@code unwrap} method is to be preferred for new
     * applications.
     * @return the underlying provider object
	 *
	 * @deprecated Use {@link #unwrap(Class)} instead.
     */
	@Deprecated(since = "4.0")
    Object getDelegate();

}
