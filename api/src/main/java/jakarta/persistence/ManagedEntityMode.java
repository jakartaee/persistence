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

/**
 * A {@link FindOption} which specifies whether entities are
 * to be loaded in {@linkplain #READ_ONLY read only} mode or
 * in regular {@linkplain #READ_WRITE modifiable} mode.
 * <p>
 * By default, an entity is loaded in modifiable mode, and
 * its state is synchronized to the database when a flush
 * operation occurs.
 * <p>
 * An entity may be loaded in read-only mode by explicitly
 * passing {@link #READ_ONLY} as an option to any method of
 * {@link EntityManager} which accepts a {@link FindOption}
 * or to {@link Query#setManagedEntityMode}. Furthermore,
 * the current mode for a given managed entity may be changed
 * by calling {@link EntityManager#setManagedEntityMode}.
 * <p>
 * Changes made to an entity instance currently loaded in
 * read-only mode are not synchronized to the database and
 * do not become persistent. The provider is not required
 * to detect modifications to entities in read-only mode.
 * <p>
 * When {@link #READ_ONLY} is passed as an option, or when
 * a query is executed in {@link #READ_ONLY} mode, as
 * determined by the return value of the method
 * {@link Query#setManagedEntityMode}, every entity loaded
 * into the persistence context during the invocation is
 * loaded in read-only mode, including any eagerly fetched
 * associated entities. Entities which are already loaded
 * remain in their predetermined {@link ManagedEntityMode}.
 * <p>
 * On the other hand, when the {@code ManagedEntityMode} of
 * an entity is changed via invocation of
 * {@link EntityManager#setManagedEntityMode}, the new mode
 * applies only the given managed entity instance, and does
 * not affect associated entities.
 * <p>
 * A managed entity in read-only mode has its mode
 * automatically reset to {@link #READ_WRITE} when:
 * <ul>
 * <li>it is directly returned by a call to a method of
 *     {@link EntityManager}, and {@link #READ_ONLY} was
 *     not specified as an argument to the method,
 * <li>it is referenced by the entity returned by a call
 *     to a method of {@link EntityManager} via an
 *     association path marked for eager fetching within
 *     the fetch graph in effect during execution of the
 *     method, and {@link #READ_ONLY} was not specified as
 *     an argument to the method,
 * <li>it occurs directly in the result of a query which
 *     was executed in {@link #READ_WRITE} mode, as
 *     determined by the return value of the method
 *     {@link Query#getManagedEntityMode},
 * <li>it is referenced via an association path marked
 *     for eager fetching by an entity occurring directly
 *     in the result of a query which was executed in
 *     {@link #READ_WRITE} mode, or
 * <li>it is passed as an argument to
 *     {@link EntityManager#refresh refresh()},
 * 	   {@link EntityManager#lock lock()},
 * 	   {@link EntityManager#remove persist()}, or
 * 	   {@link EntityManager#remove remove()}.
 * </ul>
 * <p>If an entity in read-only mode is modified, and then the
 * entity is reset to modifiable mode, either automatically or
 * explicitly, the behavior is undefined. Such modifications
 * might be lost; they might be made persistent; or the provider
 * might throw an exception. Portable applications should not
 * depend on such behavior.
 *
 * @since 4.0
 */
public enum ManagedEntityMode implements FindOption {
	/**
	 * Specifies that an entity is loaded with the intention that
	 * its state is used only for reading, and not for modification,
	 * enabling the persistence provider to optimize performance.
	 * <p>
	 * If a read-only entity is modified, the modifications are not
	 * synchronized with the database. The provider is not required
	 * to detect modifications to entities in read-only mode.
	 */
	READ_ONLY,
	/**
	 * Specifies that an entity should be loaded in the default
	 * modifiable mode.
	 * <p>
	 * Changes made to modifiable entities are synchronized with
	 * the database when the persistence context is flushed.
	 */
	READ_WRITE
}
