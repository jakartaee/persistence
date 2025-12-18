/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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

import java.util.List;

/**
 * Provides entity operations that are performed independently
 * of a persistence context. This interface enables more direct
 * control over interaction with the database than what is
 * possible with {@link EntityManager}.
 *
 * <p>An instance of {@code EntityAgent} must be obtained from
 * an {@link EntityManagerFactory}, and is only able to manage
 * persistence of entities belonging to the associated persistence
 * unit. In the Jakarta EE environment, an entity agent with a
 * lifecycle managed by the container may be obtained by
 * dependency injection.
 *
 * <ul>
 * <li>An {@code EntityAgent} obtained directly from an
 *     {@code EntityManagerFactory} is never thread safe, and
 *     it is always wrong to share a reference to such an entity
 *     agent between multiple concurrently executing threads.
 * <li>On the other hand, in the Jakarta EE container environment,
 *     a reference to a container-managed, transaction-scoped
 *     entity agent obtained by injection is safe to invoke
 *     concurrently from distinct threads. The container
 *     redirects invocations to distinct instances of
 *     {@code EntityAgent} based on transaction affinity.
 * </ul>
 *
 * <p>An {@code EntityAgent} has no associated persistence context,
 * and works only with detached entity instances. When a method of
 * this interface is called, any necessary interaction with the
 * database happens immediately and synchronously. In particular,
 * {@linkplain #update update} is an explicit operation. Since
 * there is no {@linkplain EntityManager#flush flush operation},
 * and since the entities themselves are detached, modifications to
 * the entities are never automatically detected and made persistent.
 *
 * @since 4.0
 *
 * @see EntityManager
 */
public non-sealed interface EntityAgent extends EntityHandler {

    /**
     * Insert a record.
     * <p>
     * If the entity {@link Id} field is declared to be generated,
     * for example, if it is annotated {@link GeneratedValue}, the
     * id is generated and assigned to the given instance.
     * <p>
     * <ul>
     * <li>The {@link jakarta.persistence.PreInsert} callback is
     *     triggered.
     * <li>The {@link jakarta.persistence.PostInsert} callback is
     *     triggered if the operation is successful.
     * </ul>
     *
     * @param entity a new or removed entity instance
     *
     * @throws IllegalArgumentException if the given instance is
     *         determined to not be new or removed
     * @throws EntityExistsException if the given entity has an
     *         identifier assigned by the application, and a
     *         record with the assigned identifier already exists
     *         in the database
     * @throws PersistenceException if a record could not be
     *         inserted in the database
     */
    void insert(Object entity);

    /**
     * Insert every record in the given list. The records are
     * inserted in the order in which they occur in the given list
     * and according to the requirements of the {@link #insert(Object)}
     * method.
     *
     * @param entities The entities to be inserted.
     *
     * @throws IllegalArgumentException if one of the given
     *         instances is determined to not be new or removed
     * @throws EntityExistsException if one of the given entities
     *         has an identifier assigned by the application, and
     *         a record with the assigned identifier already exists
     *         in the database
     * @throws PersistenceException if a record could not be
     *         inserted in the database
     *
     * @see #insert(Object)
     */
    void insertMultiple(List<?> entities);

    /**
     * Update a record.
     * <p>
     * <ul>
     * <li>The {@link jakarta.persistence.PreUpdate} callback is
     *     triggered.
     * <li>The {@link jakarta.persistence.PostUpdate} callback is
     *     triggered if the operation is successful.
     * </ul>
     *
     * @param entity a detached entity instance
     *
     * @throws IllegalArgumentException if the given instance is
     *         determined to not be detached
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if no row matching
     *         the identifier of the given entity exists in the
     *         database or if an optimistic version check fails
     * @throws PersistenceException if a record could not be
     *         updated in the database
     */
    void update(Object entity);

    /**
     * Update every record in the given list. The records are
     * updated in the order in which they occur in the given list
     * and according to the requirements of the {@link #update(Object)}
     * method.
     *
     * @param entities The entities to be updated.
     *
     * @throws IllegalArgumentException if the one of the given
     *         instances is determined to not be detached
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if no row matching
     *         the identifier of one of the given entities exists
     *         in the database or if an optimistic version check
     *         fails
     * @throws PersistenceException if a record could not be
     *         updated in the database
     *
     * @see #update(Object)
     */
    void updateMultiple(List<?> entities);

    /**
     * Delete a record.
     * <p>
     * <ul>
     * <li>The {@link jakarta.persistence.PreDelete} callback is
     *     triggered.
     * <li>The {@link jakarta.persistence.PostDelete} callback is
     *     triggered if the operation is successful.
     * </ul>
     *
     * @param entity a detached entity instance
     *
     * @throws IllegalArgumentException if the given instance is
     *         determined to not be detached
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if no row matching
     *         the identifier of the given entity exists in the
     *         database or if an optimistic version check fails
     * @throws PersistenceException if a record could not be
     *         deleted from the database
     */
    void delete(Object entity);

    /**
     * Delete every record in the given list. The records are
     * deleted in the order in which they occur in the given list
     * and according to the requirements of the {@link #delete(Object)}
     * method.
     *
     * @param entities The entities to be deleted.
     *
     * @throws IllegalArgumentException if the one of the given
     *         instances is determined to not be detached
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if no row matching
     *         the identifier of one of the given entities exists
     *         in the database or if an optimistic version check
     *         fails
     * @throws PersistenceException if a record could not be
     *         deleted from the database
     *
     * @see #delete(Object)
     */
    void deleteMultiple(List<?> entities);

    /**
     * Perform an upsert, that is, insert the record if it does not
     * exist, or update it if it already exists.
     * <p>
     * This method never performs id generation and does not accept
     * an entity instance with a null identifier. When id generation
     * is required, use {@link #insert(Object)}.
     * <p>
     * On the other hand, {@code upsert()} does accept an entity
     * instance with an assigned identifier value, even if the entity
     * {@link Id} field is declared to be generated, for example, if
     * it is annotated {@link GeneratedValue}. Thus, this method may
     * be used to import data from an external source.
     * <p>
     * <ul>
     * <li>The {@link jakarta.persistence.PreUpsert} callback is
     *     triggered.
     * <li>The {@link jakarta.persistence.PostUpsert} callback is
     *     triggered if the operation is successful.
     * </ul>
     *
     * @param entity a detached entity instance, or a new instance
     *               with an assigned identifier
     *
     * @throws IllegalArgumentException if the given entity has a
     *         null identifier value
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if an optimistic
     *         version check fails
     * @throws PersistenceException if a record could not be
     *         upserted in the database
     */
    void upsert(Object entity);

    /**
     * Upsert every record in the given list. The records are
     * upserted in the order in which they occur in the given list
     * and according to the requirements of the {@link #upsert(Object)}
     * method.
     *
     * @param entities The entities to be inserted or updated.
     *
     * @throws IllegalArgumentException if one of the given
     *         entities has a null identifier value
     * @throws OptimisticLockException if an optimistic locking
     *         conflict is detected, that is, if an optimistic
     *         version check fails
     * @throws PersistenceException if a record could not be
     *         upserted in the database
     *
     * @see #upsert(Object)
     */
    void upsertMultiple(List<?> entities);

    /**
     * Refresh the entity instance state from the database.
     *
     * @param entity The entity to be refreshed.
     *
     * @throws EntityNotFoundException if the given entity no
     *         longer exists in the database
     * @throws PersistenceException if a record could not be
     *         read from the database
     */
    void refresh(Object entity);

    /**
     * Refresh multiple entities.
     *
     * @param entities The entities to be refreshed.
     *
     * @see #refresh(Object)
     *
     * @throws EntityNotFoundException if one of the given
     *         entities no longer exists in the database
     * @throws PersistenceException if a record could not be
     *         read from the database
     */
    void refreshMultiple(List<?> entities);

    /**
     * Refresh the entity instance state from the database.
     *
     * @param entity The entity to be refreshed.
     * @param lockMode The LockMode to be applied.
     *
     * @throws EntityNotFoundException if the given entity no
     *         longer exists in the database
     * @throws PersistenceException if a record could not be
     *         read from the database
     */
    void refresh(Object entity, LockModeType lockMode);

    /**
     * Fetch an association or collection that was configured
     * for lazy loading if it has not yet been fetched from
     * the database. If the association or collection is
     * already fetched, this operation has no effect. For
     * convenience, this method always returns its argument.
     * {@snippet :
     * Book book = agent.get(Book.class, isbn);  // book is immediately detached
     * agent.fetch(book.getAuthors())            // fetch the associated authors
     *      .forEach(author -> ... );            // iterate the collection
     * }
     *
     * @param association A {@linkplain FetchType#LAZY lazy}
     *                    association
     * @param <T> The type of the entity attribute type,
     *            either an entity type or a collection type
     *
     * @return The association with its state fetched.
     * @throws PersistenceException if a record could not be
     *         read from the database
     */
    <T> T fetch(T association);
}