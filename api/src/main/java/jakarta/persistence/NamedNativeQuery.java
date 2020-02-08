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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a named native SQL query.
 * Query names are scoped to the persistence unit.
 * The <code>NamedNativeQuery</code> annotation can be applied to an 
 * entity or mapped superclass.
 *
 * @since 1.0
 */
@Repeatable(NamedNativeQueries.class)
@Target({TYPE}) 
@Retention(RUNTIME)
public @interface NamedNativeQuery { 

    /**
     * The name used to refer to the query with the {@link EntityManager} 
     * methods that create query objects.
     */
    String name();

    /** The SQL query string. */
    String query();

    /** Query properties and hints.  (May include vendor-specific query hints.) */
    QueryHint[] hints() default {};

    /** The class of the result. */
    Class resultClass() default void.class; 

    /** The name of a {@link SqlResultSetMapping}, as defined in metadata. */
    String resultSetMapping() default "";
}
