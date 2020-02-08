/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * Interface used to control transactions on resource-local entity
 * managers.  The {@link EntityManager#getTransaction
 * EntityManager.getTransaction()} method returns the
 * <code>EntityTransaction</code> interface.

 *
 * @since 1.0
 */
public interface EntityTransaction {

     /**
      * Start a resource transaction. 
      * @throws IllegalStateException if <code>isActive()</code> is true
      */
     public void begin();

     /**
      * Commit the current resource transaction, writing any 
      * unflushed changes to the database.  
      * @throws IllegalStateException if <code>isActive()</code> is false
      * @throws RollbackException if the commit fails
      */
     public void commit();

     /**
      * Roll back the current resource transaction. 
      * @throws IllegalStateException if <code>isActive()</code> is false
      * @throws PersistenceException if an unexpected error 
      *         condition is encountered
      */
     public void rollback();

     /**
      * Mark the current resource transaction so that the only 
      * possible outcome of the transaction is for the transaction 
      * to be rolled back. 
      * @throws IllegalStateException if <code>isActive()</code> is false
      */
     public void setRollbackOnly();

     /**
      * Determine whether the current resource transaction has been 
      * marked for rollback.
      * @return boolean indicating whether the transaction has been
      *         marked for rollback
      * @throws IllegalStateException if <code>isActive()</code> is false
      */
     public boolean getRollbackOnly();

     /**
      * Indicate whether a resource transaction is in progress.
      * @return boolean indicating whether transaction is
      *         in progress
      * @throws PersistenceException if an unexpected error 
      *         condition is encountered
      */
     public boolean isActive();
}
