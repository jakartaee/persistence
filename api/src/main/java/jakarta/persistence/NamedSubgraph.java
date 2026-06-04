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
 * Declares a named {@linkplain Subgraph subgraph} of a
 * {@linkplain NamedEntityGraph named entity graph}. The subgraph
 * represents an associated or embedded entity or embeddable type.
 * <p>
 * The name of the subgraph is scoped to the containing named entity
 * graph. If two subgraphs belonging to a given named entity graph
 * have the same name, the types represented by the subgraphs must
 * belong to the same entity inheritance hierarchy.
 * <p>
 * A {@code NamedSubgraph} must be referenced by {@linkplain #name}
 * from the {@link NamedAttributeNode#subgraph subgraph} or
 * {@link NamedAttributeNode#keySubgraph keySubgraph} element of a
 * {@link NamedAttributeNode} annotation within the parent
 * {@code NamedEntityGraph}.
 * <p>
 * A {@code NamedSubgraph} is reified at runtime as an instance of
 * {@link Subgraph}.
 *
 * @apiNote Alternatively, use {@link NamedEntityGraph} to declare
 *          the subgraph, and use {@link Fetch} to reference the
 *          subgraph by annotating a field of the graphed entity
 *          class.
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
     * (Required) The name of the subgraph. A subgraph must be referenced by
     * name from the {@linkplain NamedAttributeNode#subgraph subgraph} or
     * {@linkplain NamedAttributeNode#keySubgraph keySubgraph} element of a
     * {@link NamedAttributeNode} annotation of the graph.
     */
    String name();

    /**
     * (Optional) The entity or embeddable type represented by this subgraph.
     * <p>
     * If not explicitly specified, the subgraph represents the declared type
     * of the attribute represented by the referencing attribute node.
     * The type must be explicitly specified when the subgraph represents an
     * entity subclass of the entity type of the attribute.
     */
    Class<?> type() default void.class;

    /**
     * (Required) The list of attributes of the entity or embeddable type
     * represented by this subgraph that are included as attribute nodes in
     * the entity graph.
     * <p>
     * Each attribute is declared via a {@link NamedAttributeNode}
     * annotation. If this subgraph represents an entity subclass of the
     * entity type of the attribute, only attributes declared by the subclass
     * may be included in the list.
     */
    NamedAttributeNode[] attributeNodes();
}
