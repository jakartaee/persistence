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
 * Enumerates the kinds of optimistic or pessimistic lock which may
 * be obtained on an entity instance.
 *
 * <p>A specific lock mode may be requested by passing an explicit
 * {@code LockModeType} as an argument to:
 * <ul>
 * <li>one of the methods of {@link EntityManager} which obtains
 *     locks ({@link EntityManager#lock lock()},
 *     {@link EntityManager#find find()}, or
 *     {@link EntityManager#refresh refresh()}), or
 * <li>to {@link Query#setLockMode(LockModeType)} or
 *     {@link TypedQuery#setLockMode(LockModeType)}.
 * </ul>
 * <p>Lock modes other than {@link #NONE} prevent dirty reads and
 * non-repeatable reads of locked entity data within the current
 * transaction.
 * 
 * <p>An optimistic lock is requested by specifying the lock mode
 * type {@link #OPTIMISTIC} or {@link #OPTIMISTIC_FORCE_INCREMENT}.
 * The lock mode types {@link #READ} and {@link #WRITE} are synonyms
 * for {@code OPTIMISTIC} and {@code OPTIMISTIC_FORCE_INCREMENT}
 * respectively. The latter are preferred for new applications.
 *
 * <p>The persistence implementation is not required to accept
 * requests for optimistic locks on non-versioned entities. When
 * a provider does not support such a lock request, it must throw
 * {@link PersistenceException}.
 *
 * <p>An immediate pessimistic database-level lock is requested by
 * specifying one of the lock mode types {@link #PESSIMISTIC_READ},
 * {@link #PESSIMISTIC_WRITE}, or {@link #PESSIMISTIC_FORCE_INCREMENT}.
 * <ul>
 * <li>A lock with type {@code PESSIMISTIC_WRITE} can be obtained on
 *     an entity instance to force serialization among transactions
 *     attempting to update the entity data.
 * <li>A lock with type {@code PESSIMISTIC_READ} can be used to query
 *     data using repeatable-read semantics without the need to reread
 *     the data at the end of the transaction to obtain a lock, and
 *     without blocking other transactions reading the data.
 * <li>A lock with type {@code PESSIMISTIC_WRITE} can be used when
 *     querying data and there is a high likelihood of deadlock or
 *     update failure among concurrent updating transactions.
 * </ul>
 * 
 * <p>The persistence implementation must accept requests for locks
 * of type {@code PESSIMISTIC_READ} and {@code PESSIMISTIC_WRITE} on
 * both versioned and non-versioned entities.
 * <ul>
 * <li>When the lock cannot be obtained, and the database locking
 *     failure results in transaction-level rollback, the provider
 *     must throw {@link PessimisticLockException} and ensure that
 *     the JTA transaction or {@code EntityTransaction} has been
 *     marked for rollback.
 * <li>When the lock cannot be obtained, and the database locking
 *     failure results in only statement-level rollback, the provider
 *     must throw the {@link LockTimeoutException} (and must not mark
 *     the transaction for rollback).</li>
 * </ul>
 * <p>The persistence implementation is not required to accept
 * requests for locks of type {@code PESSIMISTIC_FORCE_INCREMENT} on
 * non-versioned entities. When a provider does not support such a
 * lock request, it must throw {@link PersistenceException}.
 *
 * @since 1.0
 *
 */
public enum LockModeType implements FindOption, RefreshOption {
    /**
     * Synonymous with {@link #OPTIMISTIC}.
     * <p>
     * {@code OPTIMISTIC} is preferred for new applications.
     *
     */
    READ,

    /**
     * Synonymous with {@link #OPTIMISTIC_FORCE_INCREMENT}.
     * <p>
     * {@code OPTIMISTIC_FORCE_INCREMENT} is preferred for
     * new applications.
     *
     */
    WRITE,

    /**
     * An optimistic lock.
     *
     * <p>If transaction T1 calls for a lock of type
     * {@code OPTIMISTIC} on a versioned object, the entity manager
     * must ensure that neither of the following phenomena can occur:
     * <ul>
     * <li>P1 <b>Dirty read</b>: Transaction T1 modifies a row.
     *     Another transaction T2 then reads that row and obtains the
     *     modified value, before T1 has committed or rolled back.
     *     Transaction T2 eventually commits successfully; it does not
     *     matter whether T1 commits or rolls back and whether it does
     *     so before or after T2 commits.
     * </li>
     * <li>P2 <b>Non-repeatable read</b>: Transaction T1 reads a row.
     *     Another transaction T2 then modifies or deletes that row,
     *     before T1 has committed. Both transactions eventually commit
     *     successfully.
     * </li>
     * </ul>
     *
     * <p>The persistence implementation is not required to accept
     * requests for {@code OPTIMISTIC} locks on non-versioned entities.
     *
     * @since 2.0
     */
    OPTIMISTIC,

    /**
     * Optimistic lock, with version update, which prevents the
     * following phenomena:
     *
     * <p>If transaction T1 calls for a lock of type
     * {@code OPTIMISTIC_FORCE_INCREMENT} on a versioned object,
     * the entity manager must ensure that neither of the following
     * phenomena can occur:
     * <ul>
     * <li>P1 <b>Dirty read</b>: Transaction T1 modifies a row.
     *     Another transaction T2 then reads that row and obtains the
     *     modified value, before T1 has committed or rolled back.
     *     Transaction T2 eventually commits successfully; it does not
     *     matter whether T1 commits or rolls back and whether it does
     *     so before or after T2 commits.
     * </li>
     * <li>P2 <b>Non-repeatable read</b>: Transaction T1 reads a row.
     *     Another transaction T2 then modifies or deletes that row,
     *     before T1 has committed. Both transactions eventually commit
     *     successfully.
     * </li>
     * </ul>
     *
     * <p>In addition, obtaining a lock of type
     * {@code OPTIMISTIC_FORCE_INCREMENT} on a versioned entity will
     * also force an update (increment) to the {@linkplain Version
     * version column}.
     *
     * <p>The persistence implementation is not required to accept
     * requests for {@code OPTIMISTIC_FORCE_INCREMENT} locks on
     * non-versioned entities.
     *
     * @since 2.0
     */
    OPTIMISTIC_FORCE_INCREMENT,

    /**
     * Pessimistic read lock.
     *
     * <p>If transaction T1 calls for a lock of type
     * {@code PESSIMISTIC_READ} on an object, the entity manager must
     * ensure that neither of the following phenomena can occur:
     * <ul>
     * <li>P1 <b>Dirty read</b>: Transaction T1 modifies a row.
     *     Another transaction T2 then reads that row and obtains the
     *     modified value, before T1 has committed or rolled back.
     * </li>
     * <li>P2 <b>Non-repeatable read</b>: Transaction T1 reads a row.
     *     Another transaction T2 then modifies or deletes that row,
     *     before T1 has committed or rolled back.
     * </ul>
     *
     * @since 2.0
     */
    PESSIMISTIC_READ,

    /**
     * Pessimistic write lock.
     *
     * <p>If transaction T1 calls for a lock of type
     * {@code PESSIMISTIC_WRITE} on an object, the entity manager must
     * ensure that neither of the following phenomena can occur:
     * <ul>
     * <li>P1 <b>Dirty read</b>: Transaction T1 modifies a row.
     *     Another transaction T2 then reads that row and obtains the
     *     modified value, before T1 has committed or rolled back.
     * </li>
     * <li>P2 <b>Non-repeatable read</b>: Transaction T1 reads a row.
     *     Another transaction T2 then modifies or deletes that row,
     *     before T1 has committed or rolled back.
     * </ul>
     *
     * @since 2.0
     */
    PESSIMISTIC_WRITE,

    /**
     * Pessimistic write lock, with version update.
     *
     * <p>If transaction T1 calls for a lock of type
     * {@code PESSIMISTIC_FORCE_INCREMENT} on an object, the entity
     * manager must ensure that neither of the following phenomena
     * can occur:
     * <ul>
     * <li>P1 <b>Dirty read</b>: Transaction T1 modifies a row.
     *     Another transaction T2 then reads that row and obtains the
     *     modified value, before T1 has committed or rolled back.
     * </li>
     * <li>P2 <b>Non-repeatable read</b>: Transaction T1 reads a row.
     *     Another transaction T2 then modifies or deletes that row,
     *     before T1 has committed or rolled back.
     * </ul>
     *
     * <p>In addition, obtaining a lock of type
     * {@code PESSIMISTIC_FORCE_INCREMENT} on a versioned entity will
     * also force an update (increment) to the {@linkplain Version
     * version column}.
     *
     * <p>The persistence implementation is not required to accept
     * requests for {@code PESSIMISTIC_FORCE_INCREMENT} locks on
     * non-versioned entities.
     *
     * @since 2.0
     */
    PESSIMISTIC_FORCE_INCREMENT,

    /**
     * No lock.
     *
     * @since 2.0
     */
    NONE
}
