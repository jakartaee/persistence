/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used in schema generation to specify creation of an index.
 * <p>
 * Note that it is not necessary to specify an index for a primary key,
 * as the primary key index will be created automatically.
 *
 * <p> 
 * The syntax of the <code>columnList</code> element is a 
 * <code>column_list</code>, as follows:
 * 
 * <pre>
 *    column::= index_column [,index_column]*
 *    index_column::= column_name [ASC | DESC]
 * </pre>
 * 
 * <p> If <code>ASC</code> or <code>DESC</code> is not specified, 
 * <code>ASC</code> (ascending order) is assumed.
 *
 * @see Table
 * @see SecondaryTable
 * @see CollectionTable
 * @see JoinTable
 * @see TableGenerator
 *
 * @since 2.1
 *
 */
@Target({})
@Retention(RUNTIME)
public @interface Index {

    /**
     * (Optional) The name of the index; defaults to a provider-generated name.
     */
    String name() default "";

    /**
     * (Required) The names of the columns to be included in the index, 
     * in order.
     */
    String columnList();

    /**
     * (Optional) Whether the index is unique.
     */
    boolean unique() default false;

}
