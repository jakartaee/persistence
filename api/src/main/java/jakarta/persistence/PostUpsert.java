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

package jakarta.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a callback method for the corresponding lifecycle event.
 * This annotation may be applied to methods of an entity class, a
 * mapped superclass, or a callback listener class.
 *
 * <p>The {@code PostUpsert} callback occurs after any database upsert
 * operation to entity data, whenever entity state is modified via
 * {@link EntityManager} or {@link EntityAgent}. When such a modification
 * is made via an {@link EntityManager}, the database operation might
 * occur at the time the entity state is modified, or it may occur when
 * modifications are flushed to the database.
 *
 * <p>The following rules apply to lifecycle callback methods:
 * <ul>
 * <li>Lifecycle callback methods may throw unchecked/runtime exceptions.
 *     A runtime exception thrown by a callback method that executes within
 *     a transaction causes that transaction to be marked for rollback if
 *     context is joined to the transaction.
 * <li>Lifecycle callbacks can invoke JNDI, JDBC, JMS, and enterprise beans.
 * <li>A lifecycle callback method may modify the non-relationship state of
 *     the entity on which it is invoked.
 * <li>In general, the lifecycle method of a portable application should not
 *     invoke {@link EntityManager} or query operations, access other entity
 *     instances, or modify relationships within the same persistence context
 * </ul>
 *
 * <p>It is implementation-dependent whether callback methods are invoked
 * before or after the cascading of the lifecycle events to related entities.
 *
 * @since 4.0
 */
@Target({METHOD}) 
@Retention(RUNTIME)
public @interface PostUpsert {}