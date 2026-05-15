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

/**
 * Thrown by the persistence provider when:
 * <ul>
 * <li>a detached instance of an entity type is passed to
 *     {@link EntityManager#persist(Object)}, or
 * <li>any kind of uniqueness constraint is violated when an
 *     entity is inserted in the database.
 * </ul>
 *
 * <p> The persistence provider is always permitted to postpone
 * an {@code EntityExistsException} until execution of the
 * resulting database insert operation. If a detached instance
 * is passed to the persist operation, either:
 * <ul>
 * <li>an {@code EntityExistsException} is immediately thrown
 *     by {@code persist()}, or
 * <li>an {@code EntityExistsException} or some other subtype
 *     of {@link PersistenceException} is thrown when the
 *     persistence context is flushed or when the transaction
 *     commits.
 * </ul>
 *
 * <p> If an {@code EntityExistsException} is thrown by an
 * {@link EntityManager} with a persistence context joined to
 * an active transaction, the transaction is automatically
 * marked for rollback when the exception is thrown.
 *
 * @see EntityManager#persist(Object)
 * 
 * @since 1.0
 */
public class EntityExistsException extends PersistenceException {

    /**
     * Constructs a new {@code EntityExistsException} exception with
     * {@code null} as its detail message.
     */
    public EntityExistsException() {
        super();
    }

    /**
     * Constructs a new {@code EntityExistsException} exception with the
     * specified detail message.
     * 
     * @param message the detail message.
     */
    public EntityExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code EntityExistsException} exception with the
     * specified detail message and cause.
     * 
     * @param message the detail message.
     * @param cause the cause.
     */
    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code EntityExistsException} exception with the
     * specified cause.
     * 
     * @param cause the cause.
     */
    public EntityExistsException(Throwable cause) {
        super(cause);
    }
}
