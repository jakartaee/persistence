/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to specify the path and boundaries for a find operation or query.
 *
 * @since 2.1
 */
@Repeatable(NamedEntityGraphs.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface NamedEntityGraph {

    /**
     * (Optional) The name of the entity graph.
     * Defaults to the entity name of the root entity.
     */
    String name() default "";

    /**
     * (Optional) A list of attributes of the entity that are included in
     * this graph.
     */
    NamedAttributeNode[] attributeNodes() default {};

    /**
     * (Optional) Includes all of the attributes of the annotated
     * entity class as attribute nodes in the NamedEntityGraph without
     * the need to explicitly list them.  Included attributes can
     * still be fully specified by an attribute node referencing a
     * subgraph.
     */
    boolean includeAllAttributes() default false;

    /**
     * (Optional) A list of subgraphs that are included in the
     * entity graph. These are referenced by name from NamedAttributeNode
     * definitions.
     */
    NamedSubgraph[] subgraphs() default {};

    /**
     * (Optional) A list of subgraphs that will add additional
     * attributes for subclasses of the annotated entity class to the
     * entity graph.  Specified attributes from superclasses are
     * included in subclasses.
     */
    NamedSubgraph[] subclassSubgraphs() default {};
}

