/*
 * Copyright (c) 2011, 2023 Oracle and/or its affiliates and others. All rights reserved.
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

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares an {@linkplain AttributeNode attribute node} of a
 * {@linkplain NamedEntityGraph named entity graph} or
 * {@linkplain NamedSubgraph named subgraph}.
 * <p>
 * A {@code NamedAttributeNode} is reified at runtime as an instance
 * of {@link AttributeNode}.
 *
 * @apiNote Alternatively, use {@link Fetch} to declare an attribute
 *          node by annotating a field of the graphed entity class.
 *
 * @see NamedEntityGraph
 * @see NamedSubgraph
 *
 * @since 2.1
 */
@Target({})
@Retention(RUNTIME)
public @interface NamedAttributeNode {

    /**
     * (Required) The name of the attribute represented by this node.
     */
    String value();

    /**
     * (Optional) The name of a subgraph rooted at this attribute node.
     * <p>
     * The name must resolve to the {@linkplain NamedSubgraph#name name}
     * of a {@link NamedSubgraph} annotation nested within the same
     * {@link NamedEntityGraph}, or to the
     * {@linkplain NamedEntityGraph#name name} of a subgraph declared by
     * a different {@link NamedEntityGraph @NamedEntityGraph} annotation.
     * <p>
     * The type of the specified subgraph must be compatible with the
     * type of the attribute represented by this node. In the case of a
     * {@code @NamedSubgraph}, the type of the subgraph is given by its
     * {@link NamedSubgraph#type type} element; in the case of
     * {@code @NamedEntityGraph}, it is the annotated entity type.
     * <p>
     * If this node represents a {@link ManyToOne} or {@link OneToOne}
     * association, and the associated entity type has entity subclasses,
     * there might be more than one {@code @NamedSubgraph} annotation
     * with the specified name. In this case, every matching subgraph is
     * considered to be rooted at this node.
     */
    String subgraph() default "";

    /**
     * (Optional) The name of a subgraph rooted at the key of the map
     * represented by this attribute node.
     * <p>
     * This element may only be specified when this node represents an
     * attribute of type {@link java.util.Map}. The named subgraph must
     * resolve as described by {@link #subgraph}, and must be compatible
     * with the map key type.
     */
    String keySubgraph() default "";
}
