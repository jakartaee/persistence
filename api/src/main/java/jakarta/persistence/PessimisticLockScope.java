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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

/**
 * Controls how a {@linkplain LockModeType pessimistic lock}
 * applied to an entity affects associated collections and
 * relationships.
 * <p>
 * A {@code PessimisticLockScope} may be passed:
 * <ul>
 * <li>as a {@link FindOption}, {@link RefreshOption},
 *     or {@link LockOption},
 * <li>as an argument to {@link Query#setLockScope}, or
 * <li>as the value of {@link NamedQuery#lockScope}.
 * </ul>
 * <p>Alternatively, it may be specified via the configuration
 * property {@code jakarta.persistence.lock.scope}. This property
 * may be passed as an argument to methods of {@link EntityManager}
 * which accept a {@link LockModeType}, or to {@link Query#setHint},
 * or it may be specified via {@link NamedQuery#hints}.
 *
 * @since 2.0
 */
public enum PessimisticLockScope implements FindOption, RefreshOption, LockOption, QueryOption {

    /**
     * <p>The persistence provider must lock the row or rows of the
     * {@linkplain Table primary} and {@linkplain SecondaryTable secondary}
     * tables mapped by the entity. In the case of a
     * {@linkplain InheritanceType#JOINED joined} inheritance hierarchy,
     * this includes all rows in tables mapped by the entity superclasses
     * of the entity.
     *
     * <p>The pessimistic lock does not extend to rows of:
     * <ul>
     * <li>tables mapped by associated entities,
     * <li>{@linkplain JoinTable join tables} mapped by associations,
     *     or
     * <li>{@linkplain CollectionTable collection tables}.
     * </ul>
     *
     * <p>This is the default behavior of pessimistic locking.
     */
    NORMAL,

    /**
     * <p>The persistence provider must lock the row or rows of:
     * <ul>
     * <li>the {@linkplain Table primary} and
     *     {@linkplain SecondaryTable secondary} tables mapped by the
     *     entity, including all rows in tables mapped by the entity
     *     superclasses of the entity in the case of a
     *     {@linkplain InheritanceType#JOINED joined} inheritance
     *     hierarchy,
     * <li>{@linkplain JoinTable join tables} mapped by associations
     *     owned by the entity, and
     * <li>{@linkplain CollectionTable collection tables} mapped by
     *     collections belonging to the entity.
     * </ul>
     *
     * <p>The pessimistic lock does not extend to rows of:
     * <ul>
     * <li>tables mapped by associated entities,
     * <li>{@linkplain JoinTable join tables} mapped by associations
     *     not owned by the entity.
     * </ul>
     */
    EXTENDED,

    /**
     * <p>The persistence provider must lock the row or rows of:
     * <ul>
     * <li>the {@linkplain Table primary} and
     *     {@linkplain SecondaryTable secondary} tables mapped by the
     *     entity, including all rows in tables mapped by the entity
     *     superclasses of the entity in the case of a
     *     {@linkplain InheritanceType#JOINED joined} inheritance
     *     hierarchy,
     * <li>{@linkplain JoinTable join tables} mapped by associations
     *     which were fetched as part of the operation which obtained
     *     the pessimistic lock, along with the primary and secondary
     *     tables mapped by the associated entities,
     * <li>{@linkplain CollectionTable collection tables} mapped by
     *     collections belonging to the entity which were fetched as
     *     part of the operation which obtained the pessimistic lock.
     * </ul>
     *
     * <p>The pessimistic lock does not extend to data which was not
     * fetched as part of the operation which obtained the pessimistic
     * lock.
     *
     * @since 4.0
     */
    FETCHED
}
