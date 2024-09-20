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
//     Gavin King - 4.0

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares that the annotated attribute is an attribute node
 * in a {@linkplain NamedEntityGraph named entity graph}. The
 * {@link #graph} member must specify the name of the graph.
 *
 * @see EntityGraph#addAttributeNode
 * 
 * @since 4.0
 */
@Repeatable(NamedEntityGraphAttributeNodes.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface NamedEntityGraphAttributeNode {
    /**
     * The name of the containing entity graph, as specified by
     * {@link NamedEntityGraph#name}. If no name is explicitly
     * specified, the name defaults to the entity name of the
     * annotated root entity.
     */
    String graph() default "";
}

