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
 * Specifies the lifecycle of a container-managed persistence context
 * in a Jakarta EE container environment.
 * <ul>
 * <li>A {@linkplain #TRANSACTION transaction-scoped} persistence
 *     context has a lifecycle bound to the current JTA transaction.
 *     The container is responsible for maintaining transactional
 *     affinity when a reference to an {@link EntityManager} is
 *     injected.
 * <li>An {@linkplain #EXTENDED extended} persistence context has a
 *     lifecycle bound to an instance of a stateful session bean and
 *     may be inherited by other stateful session beans directly or
 *     indirectly instantiated by the first bean.
 * </ul>
 *
 * <p>By default, a container-managed persistence context is
 * transaction-scoped. The persistence context type for a
 * container-managed persistence context is specified by
 * {@link PersistenceContext#type}.
 *
 * @see PersistenceContext#type
 *
 * @since 1.0
 */
public enum PersistenceContextType {

    /**
     * A new persistence context begins when the container-managed entity
     * manager is invoked in the scope of an active JTA transaction, and
     * there is no current persistence context already associated with
     * the transaction. The persistence context is created and associated
     * with the transaction. This affinity of the persistence context with
     * the JTA transaction is independent of the
     * {@link SynchronizationType synchronization type} of the persistence
     * context and of whether the persistence context has been joined to
     * the transaction.
     * <p>
     * The persistence context propagates with the current JTA transaction
     * to other injected transaction-scoped entity manager references with
     * the same persistence unit and synchronization type.
     * <p>
     * The persistence context ends when the associated transaction commits
     * or rolls back, and all entities belonging to the persistence context
     * become detached.
     * <p>
     * If the entity manager is invoked outside the scope of a transaction,
     * all entities loaded from the database immediately become detached at
     * the end of the method call.
     */
    TRANSACTION,

    /**
     * The persistence context exists from the point at which the stateful
     * session bean declaring a dependency on the persistence context is
     * created. The container destroys the persistence context after the
     * {@code @Remove} method of the stateful session bean completes or
     * when the stateful session bean instance is otherwise destroyed.
     *
     * <p>If a stateful session bean instantiates a stateful session bean
     * (executing in the same EJB container instance) which also declares a
     * dependency on an extended persistence context with the same unit and
     * synchronization type, the extended persistence context of the first
     * stateful session bean is inherited by the second stateful session
     * bean. The container does not destroy an extended persistence context
     * until every stateful session bean inheriting the persistence context
     * has been removed or otherwise destroyed.
     */
    EXTENDED
}
