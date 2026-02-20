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
//     Gavin King       - 3.2
//     Petros Splinakis - 2.2
//     Linda DeMichiel  - 2.1
//     Linda DeMichiel  - 2.0

package jakarta.persistence;

import jakarta.persistence.spi.Discoverable;
import jakarta.persistence.sql.ResultSetMapping;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a named native SQL query and, optionally, the mapping
 * of the result of the native SQL query. Query names are scoped
 * to the persistence unit. A named query may be executed by
 * calling {@link EntityManager#createNamedQuery(String, Class)}.
 * The SQL query should return a result set.
 *
 * <p> In simple cases, a {@link #resultClass} specifies how the
 * native SQL query result set should be interpreted, for example:
 * {@snippet :
 * @NamedNativeQuery(
 *         name = "findWidgets",
 *         query = "SELECT o.id, o.quantity, o.item " +
 *                 "FROM Order o, Item i " +
 *                 "WHERE (o.item = i.id) AND (i.name = 'widget')",
 *         resultClass = com.acme.Order.class
 * )
 * }
 *
 * <p>
 * In more complicated cases, a {@linkplain SqlResultSetMapping
 * result set mapping} is needed, which may be specified using
 * either a separate annotation:
 * {@snippet :
 * @NamedNativeQuery(
 *         name = "OrderItems",
 *         query = "SELECT o.id, o.quantity, o.item, i.id, i.name, i.description " +
 *                 "FROM Order o, Item i " +
 *                 "WHERE (o.quantity > 25) AND (o.item = i.id)",
 *         resultSetMapping = "OrderItemResults"
 * )
 * @SqlResultSetMapping(
 *         name = "OrderItemResults",
 *         entities = {
 *                 @EntityResult(entityClass=com.acme.Order.class),
 *                 @EntityResult(entityClass=com.acme.Item.class)
 *         }
 * )
 * }
 * or using the elements of this annotation:
 * {@snippet :
 * @NamedNativeQuery(
 *         name = "OrderItems",
 *         query = "SELECT o.id, o.quantity, o.item, i.id, i.name, i.description " +
 *                 "FROM Order o, Item i " +
 *                 "WHERE (o.quantity > 25) AND (o.item = i.id)",
 *         resultSetMapping = "OrderItemResults",
 *         entities = {
 *                 @EntityResult(entityClass=com.acme.Order.class),
 *                 @EntityResult(entityClass=com.acme.Item.class)
 *         }
 * )
 * }
 *
 * <p> This annotation may be applied to any class or interface
 * belonging to the persistence unit.
 *
 * @apiNote A named native SQL statement which returns a row count
 *          is usually declared using {@link NamedNativeStatement}.
 *          For backward compatibility, this annotation may be used
 *          instead, but this usage is no longer encouraged.
 *
 * @see SqlResultSetMapping
 * @see EntityHandler#createNamedQuery(String)
 * @see EntityHandler#createNamedQuery(String,Class)
 * @see NamedNativeStatement
 *
 * @since 1.0
 */
@Repeatable(NamedNativeQueries.class)
@Target({TYPE, PACKAGE, MODULE})
@Retention(RUNTIME)
@Discoverable
public @interface NamedNativeQuery { 

    /**
     * The name used to identify the query in calls to
     * {@link EntityManager#createNamedQuery}.
     */
    String name();

    /**
     * The native SQL query string.
     */
    String query();

    /**
     * The class of each query result.
     *
     * <p>When the result class is explicitly specified and the
     * {@linkplain #resultSetMapping result set mapping} is not, either:
     * <ul>
     * <li>the result class is an entity class and is interpreted as a
     *     managed {@linkplain EntityResult entity result} with implicit
     *     field mappings determined by the names of the columns in the
     *     result set and the object/relational mapping of the entity,
     * <li>the result class is the class of a {@linkplain Basic basic}
     *     type and the result set must have a single column which is
     *     interpreted as a {@linkplain ColumnResult scalar result},
     * <li>the result class is a non-abstract class or record type
     *     with a constructor with the same number of parameters as the
     *     result set has columns, and is interpreted as a
     *     {@linkplain ConstructorResult constructor result} including
     *     all the columns of the result set, or
     * <li>the result class is {@code Object[].class} and each query
     *     result is packaged in an array of type {@code Object[]},
     *     with the array elements corresponding by position with the
     *     columns of the select list and column values obtained
     *     according to the default type mappings defined by the JDBC
     *     specification.
     * </ul>
     *
     * <p>Otherwise, if a {@linkplain #resultSetMapping result set mapping}
     * is specified, an explicitly specified result class must agree with
     * the type inferred from the result set mapping.
     *
     * <p>If the result class is not explicitly specified, then:
     * <ul>
     * <li>it is inferred from the {@linkplain #resultSetMapping result
     *     set mapping}, if any, or
     * <li>it defaults to {@code Object} if the result set has a single
     *     column or {@code Object[]} if the result set has multiple
     *     columns.
     * </ul>
     * <p>When neither result class nor result set mapping is specified,
     * column values are obtained according to the default type mappings
     * defined by the JDBC specification.
     *
     * <p>The result class may be overridden by explicitly passing a class
     * object to {@link EntityManager#createNamedQuery(String, Class)}.
     */
    Class<?> resultClass() default void.class;

    /**
     * The {@linkplain SqlResultSetMapping#name name} of a
     * {@linkplain SqlResultSetMapping result set mapping} declared in
     * metadata. The named result set mapping is used to interpret the
     * result set of the native SQL query.
     *
     * <p>Alternatively, the elements {@link #entities}, {@link #classes},
     * and {@link #columns} may be used to specify a result set mapping.
     * These elements may not be used in conjunction with
     * {@code resultSetMapping}.
     *
     * @see SqlResultSetMapping
     * @see ResultSetMapping
     */
    String resultSetMapping() default "";

    /**
     * Specifies the result set mapping to entities.
     * May not be used in combination with {@link #resultSetMapping}.
     * @since 3.2
     */
    EntityResult[] entities() default {};

    /**
     * Specifies the result set mapping to constructors.
     * May not be used in combination with {@link #resultSetMapping}.
     * @since 3.2
     */
    ConstructorResult[] classes() default {};

    /**
     * Specifies the result set mapping to scalar values.
     * May not be used in combination with {@link #resultSetMapping}.
     * @since 3.2
     */
    ColumnResult[] columns() default {};

    /**
     * Query properties and hints.
     * (May include vendor-specific query hints.)
     * @see Query#setHint
     */
    QueryHint[] hints() default {};

    /**
     * (Optional) The {@linkplain QueryFlushMode query flush mode}
     * to use when executing the query.
     * @see Query#setQueryFlushMode(QueryFlushMode)
     * @since 4.0
     */
    QueryFlushMode flush() default QueryFlushMode.DEFAULT;
}
