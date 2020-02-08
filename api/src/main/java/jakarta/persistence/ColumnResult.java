/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * Used in conjunction with the {@link SqlResultSetMapping} annotation or
 * {@link ConstructorResult} annotation to map a column of the SELECT
 * list of a SQL query.
 *
 * <p> The <code>name</code> element references the name of a column in the SELECT list
 *  &#8212; i.e., column alias, if applicable. Scalar result types can be 
 * included in the query result by specifying this annotation in 
 * the metadata.
 *
 * <pre>
 *
 * Example:
 *   Query q = em.createNativeQuery(
 *       "SELECT o.id AS order_id, " +
 *           "o.quantity AS order_quantity, " +
 *           "o.item AS order_item, " + 
 *           "i.name AS item_name, " +
 *         "FROM Order o, Item i " +
 *         "WHERE (order_quantity &gt; 25) AND (order_item = i.id)",
 *       "OrderResults");
 *
 *   &#064;SqlResultSetMapping(name="OrderResults",
 *       entities={
 *           &#064;EntityResult(entityClass=com.acme.Order.class, fields={
 *               &#064;FieldResult(name="id", column="order_id"),
 *               &#064;FieldResult(name="quantity", column="order_quantity"),
 *               &#064;FieldResult(name="item", column="order_item")})},
 *       columns={
 *           &#064;ColumnResult(name="item_name")}
 *       )
 * </pre>
 *
 * @see SqlResultSetMapping
 *
 * @since 1.0
 */
@Target({}) 
@Retention(RUNTIME)

public @interface ColumnResult { 

    /** (Required) The name of a column in the SELECT clause of a SQL query */
    String name();

    /** 
     *  (Optional) The Java type to which the column type is to be mapped.
     *  If the <code>type</code> element is not specified, the default JDBC type 
     *  mapping for the column will be used.
     *  @since 2.1
     */
    Class type() default void.class;
}
