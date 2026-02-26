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

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used in conjunction with the {@link SqlResultSetMapping},
 * {@link NamedNativeQuery}, or {@link ConstructorResult}
 * annotation to map a column of the {@code SELECT} list of
 * a SQL query to a scalar (non-entity) value.
 *
 * <p>The {@link #name} element references the name of a column
 * in the {@code SELECT} list&mdash;that is, the column alias,
 * if applicable. Scalar result types can be included in the
 * query result by specifying this annotation in the metadata.
 *
 * <p>Consider the following SQL query:
 * {@snippet :
 * Query ordersWithItemNames =
 *         em.createNativeQuery(
 *             """
 *                SELECT o.id AS order_id,
 *                       o.quantity AS order_quantity,
 *                       o.item AS order_item,
 *                       i.name AS item_name
 *                FROM Order o,
 *                JOIN Item i ON o.item = i.id
 *                WHERE o.quantity > 25
 *             """,
 *             ResultMappings_.MAPPING_ORDERS_WITH_ITEM_NAMES
 *         );
 * }
 * <p>The result set mapping might be defined as follows:
 * {@snippet :
 * @SqlResultSetMapping(
 *     name = "OrdersWithItemNames",
 *     entities = @EntityResult(
 *         entityClass = Order.class,
 *         fields = {
 *             @FieldResult(name = Order_.ID,
 *                          column = "order_id"),
 *             @FieldResult(name = Order_.QUANTITY,
 *                          column = "order_quantity"),
 *             @FieldResult(name = Order_.ITEM,
 *                          column = "order_item")
 *         }
 *     ),
 *     columns = @ColumnResult(name = "item_name")
 * )
 * interface ResultMappings {}
 * }
 *
 * <p>At runtime, a {@code ColumnResult} annotation is represented by an
 * instance of {@link jakarta.persistence.sql.ColumnMapping ColumnMapping}
 * in the {@link jakarta.persistence.sql.ResultSetMapping ResultSetMapping}
 * returned by {@link EntityManagerFactory#getResultSetMappings(Class)}.
 *
 * <p>This annotation may be placed directly on a method annotated
 * {@link jakarta.persistence.query.StaticNativeQuery}.
 * {@snippet :
 * @StaticNativeQuery("SELECT count(*) AS title_count FROM books WHERE title LIKE :pattern")
 * @ColumnResult(name="title_count")
 * int countBooks(String pattern);
 * }
 *
 * @see SqlResultSetMapping
 * @see NamedNativeQuery
 * @see ConstructorResult
 *
 * @see jakarta.persistence.sql.ColumnMapping
 *
 * @since 1.0
 */
@Target(METHOD)
@Retention(RUNTIME)
@Repeatable(ColumnResult.ColumnResults.class)
public @interface ColumnResult { 

    /**
     * (Required) The name of a column in the {@code SELECT} clause of a SQL query.
     */
    String name();

    /** 
     * (Optional) The Java type to which the column type is to be mapped.
     * If the {@code type} element is not specified, the default JDBC type
     * mapping for the column is used.
     * @since 2.1
     */
    Class<?> type() default void.class;

    /**
     * @since 4.0
     */
    @Target(METHOD)
    @Retention(RUNTIME)
    @interface ColumnResults {
        ColumnResult[] value();
    }
}
