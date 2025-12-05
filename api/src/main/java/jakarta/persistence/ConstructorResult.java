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

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used in conjunction with the {@link SqlResultSetMapping} or
 * {@link NamedNativeQuery} annotation to map the {@code SELECT}
 * clause of a SQL query to the constructor of an arbitrary
 * Java class.
 *
 * <p>When processing a result set, the provider instantiates
 * the {@linkplain #targetClass target class} by calling a
 * matching constructor of the class, passing as arguments the
 * values of the specified {@linkplain #columns} of the result
 * set. Columns must be explicitly listed by {@link #columns}
 * in the same order as their corresponding parameters occur in
 * the argument list of the constructor.
 *
 * <p>The target class need not be a managed type. Any instance
 * of an entity class returned as a constructor result will be
 * in either the new or detached state, depending on whether a
 * primary key was assigned to the constructed object.
 *
 * <p>Example:
 * {@snippet :
 * Query q = em.createNativeQuery(
 *     "SELECT c.id, c.name, " +
 *         "COUNT(o) as orderCount, " +
 *         "AVG(o.price) AS avgOrder " +
 *       "FROM Customer c, Orders o " +
 *       "WHERE o.cid = c.id " +
 *       "GROUP BY c.id, c.name",
 *     "CustomerDetailsResult");
 *
 * @SqlResultSetMapping(
 *     name = "CustomerDetailsResult",
 *     classes = {
 *         @ConstructorResult(
 *             targetClass = com.acme.CustomerDetails.class,
 *             columns = {
 *                 @ColumnResult(name = "id"),
 *                 @ColumnResult(name = "name"),
 *                 @ColumnResult(name = "orderCount"),
 *                 @ColumnResult(name = "avgOrder", type = Double.class)
 *             })
 *     })
 * }
 *
 * @see SqlResultSetMapping
 * @see NamedNativeQuery
 * @see ColumnResult
 *
 * @see jakarta.persistence.sql.ConstructorMapping
 *
 * @since 2.1
 */
@Target({}) 
@Retention(RUNTIME)
public @interface ConstructorResult { 

    /**
     * (Required) The class whose constructor is to be invoked.
     * This may be any Java class with a constructor matching
     * the specified {@linkplain #columns columns}.
     */
    Class<?> targetClass();

    /** 
     * (Optional) The mapping of columns in the {@code SELECT}
     * list to arguments of a constructor of the specified Java
     * {@linkplain #targetClass target class}, in order.
     *
     * <p>Constructor parameters mapping directly to column
     * results must occur after parameters mapping to entities.
     */
    ColumnResult[] columns() default {};

    /**
     * (Optional) The mapping of columns in the {@code SELECT}
     * list to {@linkplain EntityResult entity results} which
     * are then assigned to parameters of a constructor of the
     * specified Java {@linkplain #targetClass target class},
     * in order.
     *
     * <p>Constructor parameters mapping to entity results must
     * occur after parameters mapping to columns.
     *
     * @since 4.0
     */
    EntityResult[] entities() default {};
}
