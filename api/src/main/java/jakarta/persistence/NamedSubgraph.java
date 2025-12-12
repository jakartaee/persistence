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
//     Linda DeMichiel - 2.1

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a {@linkplain Subgraph} as a member element of a
 * {@link NamedEntityGraph}. The {@code NamedSubgraph} is only
 * referenceable within its containing {@code @NamedEntityGraph}
 * annotation and cannot be referenced independently.
 *
 * <p>A {@code NamedSubgraph} is referenced {@linkplain #name}
 * from the {@link NamedAttributeNode#subgraph subgraph} element
 * of a {@link NamedAttributeNode} annotation within the parent
 * {@code NamedEntityGraph}.
 *
 * <p>A {@code NamedSubgraph} is reified at runtime as an
 * instance of {@link Subgraph}.
 *
 * @apiNote Alternatively, use {@link NamedEntityGraphSubgraph}
 * to declare a subgraph by annotating a field of the graphed
 * entity class.
 *
 * @see NamedEntityGraph
 * @see NamedAttributeNode
 *
 * @since 2.1
 */
@Target({})
@Retention(RUNTIME)
public @interface NamedSubgraph {

    /**
     * (Required) The name of the subgraph as referenced from a
     * {@link NamedAttributeNode} element. Subgraph names are
     * scoped to the containing {@link NamedEntityGraph}, and
     * must be unique within that graph.
     */
    String name();

    /**
     * (Optional) The type represented by this subgraph. The element
     * must be specified when this subgraph is extending a definition
     * on behalf of a subclass.
     */
    Class<?> type() default void.class;

    /** 
     * (Required) The list of the attributes of the class that must
     * be included. If the named subgraph corresponds to a subclass
     * of the class referenced by the corresponding attribute node,
     * then only subclass-specific attributes are listed.
     */
    NamedAttributeNode[] attributeNodes();
}
