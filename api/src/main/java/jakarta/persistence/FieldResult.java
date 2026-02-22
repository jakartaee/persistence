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

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used in conjunction with the {@link EntityResult} annotation to map
 * a column specified in the {@code SELECT} list of a SQL query to a
 * property or field of an entity class.
 *
 * <p>The {@link #name} member specifies the name of the mapped field
 * or property of the entity class. If the property or field is
 * declared by a {@linkplain Embedded child embeddable object}, then
 * {@link #name} specifies a qualified path.
 *
 * <p>Consider the following SQL query:
 * {@snippet :
 * Query orders =
 *         em.createNativeQuery(
 *             """
 *                SELECT o.id AS order_id,
 *                       o.quantity AS order_quantity,
 *                       o.item AS order_item
 *                FROM Order o
 *                WHERE o.quantity > 25
 *             """,
 *             ResultMappings_.MAPPING_ORDERS
 *         );
 * }
 * <p>The result set mapping might be defined as follows:
 * {@snippet :
 * @SqlResultSetMapping(
 *     name = "Orders",
 *     entities = @EntityResult(
 *          entityClass = Order.class,
 *          fields = {
 *              @FieldResult(name = Order_.ID,
 *                           column = "order_id"),
 *              @FieldResult(name = Order_.QUANTITY,
 *                           column = "order_quantity"),
 *              @FieldResult(name = Order_.ITEM,
 *                           column = "order_item")
 *         }
 *     )
 * )
 * interface ResultMappings {}
 * }
 *
 * <p>At runtime, a {@code FieldResult} annotation is represented by an
 * instance of {@link jakarta.persistence.sql.FieldMapping FieldMapping}
 * in the {@link jakarta.persistence.sql.ResultSetMapping ResultSetMapping}
 * returned by {@link EntityManagerFactory#getResultSetMappings(Class)}.
 *
 * @see EntityResult
 * @see SqlResultSetMapping
 *
 * @see jakarta.persistence.sql.FieldMapping
 *
 * @since 1.0
 */
@Target({}) 
@Retention(RUNTIME)
public @interface FieldResult { 

    /**
     * Name of the persistent field or property of an entity class.
     */
    String name();

    /** 
     * Name of the column in the {@code SELECT} clause&mdash;that is,
     * the column alias, if applicable.
     */
    String column();
}
