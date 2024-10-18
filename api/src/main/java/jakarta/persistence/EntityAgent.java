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

import java.util.List;

/**
 * Interface enabling more direct control over interaction with the
 * database than what is possible with {@link EntityManager}.
 *
 * <p>An instance of {@code EntityAgent} must be obtained from
 * an {@link EntityManagerFactory}, and is able to manage
 * persistence of entities belonging to the associated persistence
 * unit.
 *
 * <p>An {@code EntityAgent} has no associated persistence context,
 * and works only with detached entity instances. When a method of
 * this interface is called, any necessary interaction with the
 * database happens immediately and synchronously. In particular,
 * {@linkplain #update update} is an explicit operation. Since
 * there's no {@linkplain EntityManager#flush flush operation},
 * and since the entities themselves are detached, modifications to
 * the entities are never automatically detected and made persistent.
 *
 * @since 4.0
 *
 * @see EntityManager
 */
public interface EntityAgent extends EntityHandler {

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
     * @return The identifier of the inserted entity
     *
     * @throws IllegalArgumentException is the given instance is
     *         determined to not be new or removed
     */
    Object insert(Object entity);

    /**
     * Insert every record in the given list.
     *
     * @param entities The entities to be inserted.
     *
     * @see #insert(Object)
     */
    void insertMultiple(List<Object> entities);

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
     */
    void update(Object entity);

    /**
     * Update every record in the given list.
     *
     * @param entities The entities to be updated.
     *
     * @see #update(Object)
     */
    void updateMultiple(List<Object> entities);

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
     */
    void delete(Object entity);

    /**
     * Delete every record in the given list.
     *
     * @param entities The entities to be deleted.
     *
     * @see #delete(Object)
     */
    void deleteMultiple(List<Object> entities);

    /**
     * Perform an upsert, that is, to insert the record if it does
     * not exist, or update it if it already exists.
     * <p>
     * This method never performs id generation, and does not accept
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
     * @throws IllegalArgumentException is the entity has a null id
     */
    void upsert(Object entity);

    /**
     * Upsert every record in the given list.
     *
     * @param entities The entities to be inserted or updated.
     *
     * @see #upsert(Object)
     */
    void upsertMultiple(List<Object> entities);

    /**
     * Retrieve a record.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The id of the entity to retrieve
     *
     * @return a detached entity instance
     */
    <T> T get(Class<T> entityClass, Object id);

    /**
     * Retrieve a record, obtaining the specified lock mode.
     *
     * @param entityClass The class of the entity to retrieve
     * @param id The id of the entity to retrieve
     * @param lockMode The lock mode to apply to the entity
     *
     * @return a detached entity instance
     */
    <T> T get(Class<T> entityClass, Object id, LockModeType lockMode);

    /**
     * Retrieve a record, fetching associations specified by the
     * given {@link EntityGraph}.
     *
     * @param graph The {@link EntityGraph}
     * @param graphSemantic a {@link GraphSemantic} specifying
     *                      how the graph should be interpreted
     * @param id The id of the entity to retrieve
     *
     * @return a detached entity instance
     *
     * @since 6.3
     */
    <T> T get(EntityGraph<T> graph, Object id);

    /**
     * Retrieve a record, fetching associations specified by the
     * given {@linkplain EntityGraph load graph}, and obtaining
     * the specified {@link LockModeType lock mode}.
     *
     * @param graph The {@linkplain EntityGraph load graph}
     * @param id The id of the entity to retrieve
     * @param lockMode The lock mode to apply to the entity
     *
     * @return a detached entity instance
     */
    <T> T get(EntityGraph<T> graph, Object id, LockModeType lockMode);

    /**
     * Retrieve multiple rows, returning entity instances in a
     * list where the position of an instance in the list matches
     * the position of its identifier in the given array, and the
     * list contains a null value if there is no persistent
     * instance matching a given identifier.
     *
     * @param entityClass The class of the entity to retrieve
     * @param ids         The ids of the entities to retrieve
     * @return an ordered list of detached entity instances, with
     *         null elements representing missing entities
     */
    <T> List<T> getMultiple(Class<T> entityClass, List<Object> ids);

    /**
     * Refresh the entity instance state from the database.
     *
     * @param entity The entity to be refreshed.
     */
    void refresh(Object entity);

    /**
     * Refresh multiple entities.
     *
     * @param entities The entities to be refreshed.
     *
     * @see #refresh(Object)
     */
    void refreshMultiple(List<Object> entities);

    /**
     * Refresh the entity instance state from the database.
     *
     * @param entity The entity to be refreshed.
     * @param lockMode The LockMode to be applied.
     */
    void refresh(Object entity, LockModeType lockMode);

    /**
     * Fetch an association or collection that's configured for lazy loading.
     * <pre>
     * Book book = agent.get(Book.class, isbn);  // book is immediately detached
     * agent.fetch(book.getAuthors());           // fetch the associated authors
     * book.getAuthors().forEach(author -> ... );  // iterate the collection
     * </pre>
     *
     * @param association a {@linkplain FetchType#LAZY lazy} association
     */
    void fetch(Object association);
}
