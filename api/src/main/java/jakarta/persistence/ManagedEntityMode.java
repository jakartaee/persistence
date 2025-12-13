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
 * do not become persistent.
 * <p>
 * When {@link #READ_ONLY} is passed as an option or to
 * {@link Query#setManagedEntityMode}, every entity loaded
 * into the persistence context during the invocation, or
 * during subsequent execution of the {@link Query}, is
 * loaded in read-only mode, including any eagerly-fetched
 * associated entities.
 * <p>
 * On the other hand, when the {@code ManagedEntityMode} of
 * an enity is changed via a call to
 * {@link EntityManager#setManagedEntityMode}, the new mode
 * applies only the given managed entity instance, and does
 * not affect associated entities.
 * <p>
 * If a managed entity loaded in read-only mode would be
 * returned by a method of {@link EntityManager} which does
 * not request {@code READ_ONLY} mode, or among the results
 * of a query which does not request {@code READ_ONLY} mode,
 * a {@link PersistenceException} is thrown.
 * <p>
 * If an entity loaded in read-only mode is passed to
 * {@link EntityManager#refresh}, its mode is automatically
 * reset to {@link #READ_WRITE}. If an entity loaded in
 * read-only mode is passed to {@link EntityManager#remove}
 * or {@link EntityManager#lock}, a {@link PersistenceException}
 * is thrown.
 *
 * @since 4.0
 */
public enum ManagedEntityMode implements FindOption {
	/**
	 * Specifies that an entity should be loaded in read-only mode.
	 * <p>
	 * Read-only entities can be modified, but changes are not
	 * synchronized with the database.
	 */
	READ_ONLY,
	/**
	 * Specifies that an entity should be loaded in the default
	 * modifiable mode.
	 * <p>
	 * Changes made to modifiable entities are synchronized to
	 * with the database when the persistence context is flushed.
	 */
	READ_WRITE
}
