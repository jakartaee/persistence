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
//     Gavin King - 3.2

package jakarta.persistence;

/**
 * This type represents the root of an entity graph that will be used
 * as a template to define the attribute nodes and boundaries of a
 * graph of entities and entity relationships. The root must be an
 * entity type.
 * <p>
 * The methods to add subgraphs implicitly create the
 * corresponding attribute nodes as well; such attribute nodes
 * should not be redundantly specified.
 *
 * @param <T> The type of the root entity.
 *
 * @see AttributeNode
 * @see Subgraph
 * @see NamedEntityGraph
 *
 * @since 2.1
 */
public interface EntityGraph<T> extends AbstractGraph<T> {

    /**
     * Return the name of a named EntityGraph (an entity graph
     * defined by means of the <code>NamedEntityGraph</code>
     * annotation, XML descriptor element, or added by means of the
     * <code>addNamedEntityGraph</code> method.  Returns null if the
     * EntityGraph is not a named EntityGraph.
     */
    public String getName();

}
