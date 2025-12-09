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
//     Gavin King - 4.0

package jakarta.persistence.query;

import jakarta.persistence.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a mapping of the columns of a result set of a native SQL query.
 * Whereas {@link SqlResultSetMapping} applies at the type level and must
 * explicitly specify its name, this annotation may be applied to a method
 * and the name is inferred from the method name.
 *
 * <p>Example:
 * {@snippet :
 * @StaticNativeQuery("SELECT * FROM orders WHERE order_quantity > :?1")
 * @NativeQueryMapping(
 *     entities = @EntityResult(
 *             entityClass = Order.class,
 *             fields = {
 *                 @FieldResult(name = "id", column = "order_id"),
 *                 @FieldResult(name = "quantity", column = "order_quantity"),
 *                 @FieldResult(name = "item", column = "order_item")
 *             }
 *     )
 * )
 * List<Order> largeOrders(int threshold);
 * }
 *
 * @see StaticNativeQuery
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface NativeQueryMapping {
    /**
     * Specifies the result set mapping to entities.
     */
    EntityResult[] entities() default {};

    /**
     * Specifies the result set mapping to constructors.
     */
    ConstructorResult[] classes() default {};

    /**
     * Specifies the result set mapping to scalar values.
     */
    ColumnResult[] columns() default {};
}
