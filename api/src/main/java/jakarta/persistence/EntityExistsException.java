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
 * <li>an instance of an entity type with an identifier assigned by the
 *     application is passed to {@link EntityAgent#insert(Object)}, and
 *     a record with the assigned identifier already exists in the database.
 * </ul>
 *
 * <p>If a detached instance is passed to the persist operation, an
 * {@code EntityExistsException} may be immediately thrown by
 * {@code persist()}, or the {@code EntityExistsException} or another
 * {@link PersistenceException} may be thrown at flush or commit time.
 *
 * <p> If the persistence context is joined to an active transaction,
 * the transaction is automatically marked for rollback when this
 * exception is thrown.
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
