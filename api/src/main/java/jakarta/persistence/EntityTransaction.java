/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import jakarta.annotation.Nullable;

/**
 * Interface used to control transactions involving resource-local entity
 * managers and entity agents. An instance of {@code EntityTransaction}
 * may be obtained by calling {@link EntityHandler#getTransaction()}.
 *
 * @since 1.0
 */
public interface EntityTransaction {

     /**
      * Start a resource transaction. 
      * @throws IllegalStateException if {@link #isActive()} is true
      */
     void begin();

     /**
      * Commit the current resource transaction, writing any unflushed
      * changes to the database.
      * @throws IllegalStateException if {@link #isActive()} is false
      * @throws RollbackException if the commit fails
      */
     void commit();

     /**
      * Roll back the current resource transaction.
      * <p> Transaction rollback typically results in a loss of consistency
      * between records in the database and any persistence context joined
      * to the transaction. The persistence provider is not required to
      * resynchronize the state of the persistence context with state held
      * in the database after a transaction rolls back; nor is it required
      * to refresh the entity instances belonging to the persistence context;
      * nor is it required to revert the values of generated primary keys or
      * of updated versions. It is therefore strongly recommended that the
      * application discard references to entities modified or made persistent
      * in a transaction when the transaction rolls back.
      * @throws IllegalStateException if {@link #isActive()} is false
      * @throws PersistenceException if an unexpected error 
      *         condition is encountered
      */
     void rollback();

     /**
      * Mark the current resource transaction so that the only possible
      * outcome of the transaction is for the transaction to be rolled
      * back.
      * @throws IllegalStateException if {@link #isActive()} is false
      */
     void setRollbackOnly();

     /**
      * Determine whether the current resource transaction has been 
      * marked for rollback.
      * @return boolean indicating whether the transaction has been
      *         marked for rollback
      * @throws IllegalStateException if {@link #isActive()} is false
      */
     boolean getRollbackOnly();

     /**
      * Indicate whether a resource transaction is in progress.
      * @return boolean indicating whether transaction is in progress
      * @throws PersistenceException if an unexpected error 
      *         condition is encountered
      */
     boolean isActive();

     /**
      * Set the transaction timeout, in seconds. This is a hint.
      * @param timeout the timeout, in seconds, or null to indicate
      *                that the database server should set the timeout
      * @since 3.2
      */
     void setTimeout(@Nullable Integer timeout);

     /**
      * The transaction timeout.
      * @since 3.2
      */
     @Nullable Integer getTimeout();
}
