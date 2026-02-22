/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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
//     Gavin King      - 3.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used in conjunction with the {@link SqlResultSetMapping} or
 * {@link NamedNativeQuery} annotation to map the {@code SELECT}
 * clause of a SQL query to an entity result.
 *
 * <p>The SQL {@code SELECT} statement should select every column
 * mapped by the {@linkplain #entityClass entity class}, including
 * foreign key columns of related entities. If a mapped column is
 * missing from the SQL result set, the behavior is undefined.
 *
 * <p>If the names of the columns of the result set of the SQL
 * statement exactly match the column names mapped by the entity
 * class, then it is not necessary to explicitly specify mappings
 * for the {@linkplain #fields} or {@linkplain #discriminatorColumn
 * discriminator column} of the entity. Otherwise, if a column name
 * of the SQL result set does not exactly match the column name
 * mapped by the entity class, the {@link FieldResult} annotation
 * must be used to explicitly specify the mapping.
 *
 * <p>Consider the following SQL query:
 * {@snippet :
 * Query ordersWithItems =
 *         em.createNativeQuery(
 *             """
 *                SELECT o.id, o.quantity, o.item,
 *                       i.id, i.name, i.description
 *                FROM Order o
 *                JOIN Item i ON o.item = i.id
 *                WHERE o.quantity > 25
 *             """,
 *             ResultMappings_.MAPPING_ORDERS_WITH_ITEMS
 *         );
 * }
 * <p>The result set mapping might be defined as follows:
 * {@snippet :
 * @SqlResultSetMapping(
 *     name = "OrdersWithItems",
 *     entities = {
 *         @EntityResult(entityClass = Order.class),
 *         @EntityResult(entityClass = Item.class)
 *     }
 * )
 * interface ResultMappings {}
 * }
 *
 * <p>At runtime, an {@code EntityResult} annotation is represented by an
 * instance of {@link jakarta.persistence.sql.EntityMapping EntityMapping}
 * in the {@link jakarta.persistence.sql.ResultSetMapping ResultSetMapping}
 * returned by {@link EntityManagerFactory#getResultSetMappings(Class)}.
 *
 * @see SqlResultSetMapping
 * @see NamedNativeQuery
 *
 * @see jakarta.persistence.sql.EntityMapping
 *
 * @since 1.0
 */
@Target({}) 
@Retention(RUNTIME)
public @interface EntityResult {

    /**
     * The class of the result.
     */
    Class<?> entityClass();

    /**
     * The lock mode obtained by the SQL query.
     * @since 3.2
     */
    LockModeType lockMode() default LockModeType.NONE;

    /** 
     * Maps the columns specified in the {@code SELECT} list of
     * the query to the properties or fields of the entity class.
     */
    FieldResult[] fields() default {};

    /**
     * Specifies the column name (or alias) of the column in the
     * {@code SELECT} list that is used to determine the type of
     * the entity instance. An empty string indicates that there
     * is no discriminator column.
     */
    String discriminatorColumn() default "";
}
