/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

/**
 * Specifies whether the persistence context associated with an
 * {@link EntityManager} is always automatically synchronized with
 * the current transaction.
 * <ul>
 * <li>{@link #SYNCHRONIZED} indicates that the persistence context
 *     is automatically joined to the current transaction, and that
 *     modifications are always automatically synchronized with the
 *     database according to the current {@linkplain FlushModeType
 *     flush mode}.
 * <li>{@link #UNSYNCHRONIZED} indicates that the persistence context
 *     is not automatically joined to the current transaction, and
 *     that its modifications are not synchronized with the database
 *     until it is explicitly joined to the current transaction by
 *     calling {@link EntityManager#joinTransaction()}.
 * </ul>
 *
 * <p>By default, a container-managed persistence context is of type
 * {@code SYNCHRONIZED} and is automatically joined to the current
 * transaction. A container-managed persistence context of type
 * {@code UNSYNCHRONIZED} is not enlisted in the current transaction
 * until {@code joinTransaction()} is called. The synchronization
 * type for a container-managed persistence context is specified by
 * {@link PersistenceContext#synchronization}.
 *
 * <p>By default, an application-managed persistence context
 * associated with a JTA entity manager and created within the
 * scope of an active JTA transaction is automatically joined to
 * that transaction. An application-managed JTA persistence context
 * created outside the scope of an active JTA transaction, or any
 * application-managed persistence context created with type
 * {@code UNSYNCHRONIZED}, is not enlisted with a transaction until
 * {@code joinTransaction()} is called. The synchronization type for
 * an application-managed JTA persistence context is specified via
 * {@link EntityManagerFactory#createEntityManager(SynchronizationType)}.
 *
 * <p>An application-managed persistence context associated with a
 * {@linkplain PersistenceUnitTransactionType#RESOURCE_LOCAL
 * resource-local} entity manager is always automatically joined to
 * any {@linkplain EntityTransaction resource-local transaction}
 * begun for that entity manager.
 *
 * @see PersistenceContext#synchronization
 * @see EntityManagerFactory#createEntityManager(SynchronizationType)
 * @see FlushModeType
 * @see PersistenceContextType
 *
 * @since 2.1
 */
public enum SynchronizationType {

    /**
     * The persistence context is automatically joined to the current
     * transaction, and modifications are automatically synchronized
     * with the database according to the current
     * {@linkplain FlushModeType flush mode}.
     */
    SYNCHRONIZED,

    /**
     * The persistence context must be explicitly joined to the current
     * transaction by calling {@link EntityManager#joinTransaction()}.
     * When the persistence context is not joined to a transaction,
     * modifications cannot be synchronized with the database, regardless
     * of the current flush mode. The persistence provider must not flush
     * pending modifications to the database until the persistence context
     * is joined to a transaction.
     */
    UNSYNCHRONIZED,
}
