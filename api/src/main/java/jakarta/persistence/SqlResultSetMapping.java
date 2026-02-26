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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import jakarta.persistence.spi.Discoverable;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** 
 * Specifies an explicit mapping of the columns of a result set of a native
 * SQL query or stored procedure to {@linkplain EntityResult entity classes},
 * {@linkplain ColumnResult scalar values}, and {@linkplain ConstructorResult
 * Java class constructors}. Every SQL result set mapping has a {@link #name}.
 * SQL result set mapping names must be unique within a persistence unit.
 *
 * <p>In this example, a named mapping is declared by annotating an entity
 * class:
 * {@snippet :
 * @SqlResultSetMapping(
 *         name = "orderResults",
 *         entities = @EntityResult(
 *             entityClass = Order.class,
 *             fields = {
 *                 @FieldResult(name = Order_.ID, column = "order_id"),
 *                 @FieldResult(name = Order_.TOTAL, column = "order_total"),
 *                 @FieldResult(name = Order_.ITEM, column = "order_item")
 *             }
 *         ),
 *         columns = @ColumnResult(name = "item_name")
 * )
 * @Entity
 * class Order { ... }
 * }
 * <p>A reference to the mapping is obtained from the
 * {@linkplain jakarta.persistence.metamodel.StaticMetamodel static metamodel}
 * class of the entity:
 * {@snippet :
 * List<Order> orders =
 *         entityManager.createNativeQuery(
 *                 """
 *                   SELECT o.id AS order_id,
 *                          o.total AS order_total,
 *                          o.item_id AS order_item,
 *                          i.desc_name AS item_name
 *                   FROM orders o
 *                   JOIN order_items i
 *                     ON o.id = i.order_id
 *                   WHERE o.total > 25
 *                 """,
 *                 Order_._orderResults
 *         ).getResultList();
 * }
 *
 * <p>A {@code SqlResultSetMapping} may be reified at runtime as
 * an instance of {@link jakarta.persistence.sql.ResultSetMapping}.
 * A reified representation of a {@code SqlResultSetMapping} known
 * to the persistence unit may be obtained by calling
 * {@link EntityManagerFactory#getResultSetMappings(Class)} or
 * from the static metamodel class of the annotated class.
 * {@snippet :
 * ResultSetMapping<Order> mapping =
 *         entityManager.getResultSetMappings(Order.class)
 *              .get(Order_.MAPPING_ORDER_RESULTS);
 *  *}
 *
 * @see NamedNativeQuery#resultSetMapping
 * @see NamedStoredProcedureQuery#resultSetMappings
 * @see EntityHandler#createNativeQuery(String, String)
 *
 * @see jakarta.persistence.sql.ResultSetMapping
 * @see jakarta.persistence.sql.CompoundMapping
 * @see EntityManagerFactory#getResultSetMappings(Class)
 *
 * @since 1.0
 */
@Repeatable(SqlResultSetMappings.class)
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Discoverable
public @interface SqlResultSetMapping {

    /** 
     * The name given to the result set mapping. This name
     * identifies the mapping in:
     * <ul>
     * <li>methods of {@link EntityHandler} and
     *     {@link EntityManagerFactory}, and
     * <li>in {@link NamedNativeQuery#resultSetMapping} and
     *     {@link NamedStoredProcedureQuery#resultSetMappings}.
     * </ul>
     */
    String name();

    /**
     * Specifies the result set mapping to entities.
     */
    EntityResult[] entities() default {};

    /** 
     * Specifies the result set mapping to constructors. 
     * @since 2.1
     */
    ConstructorResult[] classes() default {};

    /**
     * Specifies the result set mapping to scalar values.
     */
    ColumnResult[] columns() default {};
}
