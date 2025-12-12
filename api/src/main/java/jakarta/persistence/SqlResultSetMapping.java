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
 * Java class constructors}. Every SQL result set mapping has a {@link #name},
 * which may be defaulted, especially when the annotation is applied at the
 * method level. SQL result set mapping names must be unique within a
 * persistence unit.
 *
 * <p>In this example, a named mapping is declared by annotating an entity
 * class, and a reference to the mapping is obtained from the static
 * metamodel class of the entity:
 * {@snippet :
 * @SqlResultSetMapping(
 *         name = "orderResults",
 *         entities = @EntityResult(
 *             entityClass = Order.class,
 *             fields = {
 *                 @FieldResult(name = "id", column = "order_id"),
 *                 @FieldResult(name = "total", column = "order_total"),
 *                 @FieldResult(name = "item", column = "order_item")
 *             }
 *         ),
 *         columns = @ColumnResult(name = "item_name")
 * )
 * @Entity
 * class Order { ... }
 * }
 * {@snippet :
 * Query query = entityManager.createNativeQuery(
 *         """
 *           SELECT o.id AS order_id,
 *                  o.total AS order_total,
 *                  o.item_id AS order_item,
 *                  i.desc_name AS item_name
 *           FROM orders o, order_items i
 *           WHERE order_total > 25 AND order_item = i.id
 *         """,
 *         Order_.MAPPING_ORDER_RESULTS
 * );
 * }
 *
 * <p>In this example, a mapping is specified by annotating a method
 * which declares a {@link jakarta.persistence.query.StaticNativeQuery},
 * and the mapping does not need to explicitly specify its name:
 * {@snippet :
 * @StaticNativeQuery("SELECT * FROM orders WHERE order_total > ?")
 * @SqlResultSetMapping(
 *     entities = @EntityResult(
 *             entityClass = Order.class,
 *             fields = {
 *                 @FieldResult(name = "id", column = "order_id"),
 *                 @FieldResult(name = "total", column = "order_total"),
 *                 @FieldResult(name = "item_id", column = "order_item")
 *             }
 *     )
 * )
 * List<Order> largeOrders(int threshold) {
 *     return entityManager.createQuery(Shop_.largeOrders(threshold))
 *             .getResultList();
 * }
 * }
 *
 * <p>A {@code SqlResultSetMapping} may be reified at runtime as
 * an instance of {@link jakarta.persistence.sql.ResultSetMapping}.
 * A reified representation of a {@code SqlResultSetMapping} known
 * to the persistence unit may be obtained by calling
 * {@link EntityManagerFactory#getResultSetMappings(Class)}.
 * {@snippet :
 * ResultSetMapping<Order> mapping =
 *         entityManager.getResultSetMappings(Order.class)
 *              .get(Order_.MAPPING_ORDER_RESULTS);
 * List<Order> orders =
 *         entityManager.createNativeQuery(
 *                 """
 *                   SELECT o.id AS order_id,
 *                          o.total AS order_total,
 *                          o.item_id AS order_item,
 *                          i.desc_name AS item_name
 *                   FROM orders o, order_items i
 *                   WHERE order_total > 25 AND order_item = i.id
 *                 """,
 *                 mapping
 *         ).getResultList();
 *}
 *
 * @see Query
 * @see StoredProcedureQuery
 * @see NamedNativeQuery
 * @see NamedStoredProcedureQuery
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
public @interface SqlResultSetMapping { 

    /** 
     * The name given to the result set mapping, and used to
     * refer to it in the methods of the {@link Query} and
     * {@link StoredProcedureQuery} APIs. If not specified,
     * the name is assigned a default value depending on
     * where the annotation occurs.
     * <ul>
     * <li>If the annotation occurs on a type, the name
     * defaults to the unqualified name of the type.
     * <li>Otherwise, if the annotation occurs on a member
     * of a type, the name defaults to the concatenation of
     * the unqualified name of the type, with the string
     * {@code "."}, and the name of the annotated member.
     * </ul>
     */
    String name() default "";

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
