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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0


package jakarta.persistence;

import java.util.List;

/**
 * Thrown by the persistence provider when:
 * <ul>
 * <li>{@link EntityHandler#get} cannot return an instance of
 *     the requested entity type because there is no matching
 *     record in the database,
 * <li>an entity reference obtained by calling
 *     {@link EntityManager#getReference} is accessed but the
 *     reference has no corresponding record in the database,
 * <li>an entity is passed to {@link EntityManager#refresh}
 *     or {@link EntityAgent#refresh} and its record no longer
 *     exists in the database,
 * <li>an entity is passed to {@link EntityManager#lock},
 *     a pessimistic lock mode is requested, and the record
 *     no longer exists in the database.
 * </ul>
 *
 * <p> If an {@code EntityNotFoundException} is thrown by an
 * {@link EntityManager} with a persistence context joined to
 * an active transaction, the transaction is automatically
 * marked for rollback when the exception is thrown.
 * 
 * @see EntityHandler#get(Class, Object)
 * @see EntityHandler#get(Class, Object, FindOption...)
 * @see EntityHandler#get(EntityGraph, Object, FindOption...)
 * @see EntityHandler#getMultiple(Class, List, FindOption...)
 * @see EntityHandler#getMultiple(EntityGraph, List, FindOption...)
 * @see EntityManager#getReference(Class,Object)
 * @see EntityManager#getReference(Object)
 * @see EntityManager#refresh(Object)
 * @see EntityManager#refresh(Object, RefreshOption...)
 * @see EntityManager#refresh(Object, java.util.Map)
 * @see EntityManager#refresh(Object, LockModeType, java.util.Map)
 * @see EntityManager#lock(Object, LockModeType)
 * @see EntityManager#lock(Object, LockModeType, LockOption...)
 * @see EntityManager#lock(Object, LockModeType, java.util.Map)
 * @see EntityAgent#refresh(Object)
 * @see EntityAgent#refresh(Object, LockModeType)
 * @see EntityAgent#refreshMultiple(List)
 * 
 * @since 1.0
 */
public class EntityNotFoundException extends PersistenceException {

	/**
	 * Constructs a new {@code EntityNotFoundException} exception with
	 * {@code null} as its detail message.
	 */
	public EntityNotFoundException() {
		super();
	}

	/**
	 * Constructs a new {@code EntityNotFoundException} exception with
	 * {@code null} as its detail message.
	 */
	public EntityNotFoundException(Exception cause) {
		super(cause);
	}

	/**
	 * Constructs a new {@code EntityNotFoundException} exception with the
	 * specified detail message.
	 * 
	 * @param message the detail message.
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new {@code EntityNotFoundException} exception with the
	 * specified detail message.
	 *
	 * @param message the detail message.
	 */
	public EntityNotFoundException(String message, Exception cause) {
		super(message, cause);
	}

}
