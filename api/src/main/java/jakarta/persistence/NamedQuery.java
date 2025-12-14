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
//     Gavin King       - 4.0
//     Gavin King       - 3.2
//     Petros Splinakis - 2.2
//     Linda DeMichiel  - 2.1
//     Linda DeMichiel  - 2.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** 
 * Declares a named query written in the Jakarta Persistence Query
 * Language. Query names are scoped to the persistence unit.
 *
 * <p> The {@code NamedQuery} annotation can be applied to an entity
 * class or mapped superclass.
 * {@snippet :
 * @NamedQuery(name = "findAllCustomersWithName",
 *             query = "SELECT c FROM Customer c WHERE c.name LIKE :custName")
 * @Entity
 * class Customer { ... }
 * }
 *
 * <p> A named query may be executed by calling
 * {@link EntityManager#createNamedQuery(String, Class)}.
 * {@snippet :
 * List<Customer> customers =
 *         em.createNamedQuery("findAllCustomersWithName", Customer.class)
 *             .setParameter("custName", "Smith")
 *             .getResultList();
 * }
 *
 * <p> Alternatively, a reference to a named query may be obtained
 * via the static metamodel.
 * {@snippet :
 * List<Customer> customers =
 *         em.createQuery(Customer_._findAllCustomersWithName_)
 *             .setParameter("custName", "Smith")
 *             .getResultList();
 * }
 *
 * @since 1.0
 */
@Repeatable(NamedQueries.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedQuery {

    /** 
     * (Required) The name used to identify the query in calls to
     * {@link EntityManager#createNamedQuery}.
     */
    String name();

    /**
     * (Required) The query string in the Jakarta Persistence
     * query language.
     */
    String query();

    /**
     * (Optional) The class of each query result. The result class
     * may be overridden by explicitly passing a class object to
     * {@link EntityManager#createNamedQuery(String, Class)}. If
     * the result class of a named query is not specified, the
     * persistence implementation is entitled to default the
     * result class to {@code Object} or {@code Object[]}. On the
     * other hand, if the result class is explicitly specified,
     * then either:
     * <ol>
     * <li>the select list of the query contains only a single
     *     item, which must be assignable to the given result
     *     class, or
     * <li>the result class must be a non-abstract class or
     *     record type with a constructor with the same number
     *     of parameters as the query has items in its select
     *     list, and the constructor parameter types must exactly
     *     match the types of the corresponding items in the
     *     select list.
     * </ol>
     * <p>In the first case, each query result is returned
     * directly to the caller. In the second case, each query
     * result is automatically packaged in a new instance of
     * the result class by calling the matching constructor.
     * @since 3.2
     */
    Class<?> resultClass() default void.class;

    /**
     * (Optional) The lock mode type to use in query execution.
     * If a {@code lockMode} other than {@link LockModeType#NONE}
     * is specified, the query must be executed in a transaction
     * and the persistence context joined to the transaction.
     * @since 2.0
     * @see Query#setLockMode
     */
    LockModeType lockMode() default LockModeType.NONE;

    /**
     * (Optional) Query properties and hints. May include
     * vendor-specific query hints.
     * @see Query#setHint
     */
    QueryHint[] hints() default {};

    /**
     * (Optional) The name of a {@linkplain NamedEntityGraph named
     * entity graph} interpreted as a load graph and applied to the
     * entity returned by the query. The named {@link EntityGraph}
     * may be overridden by calling
     * {@link TypedQuery#setEntityGraph(EntityGraph)}.
     * @since 4.0
     */
    String entityGraph() default "";
}
