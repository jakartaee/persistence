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

/**
 * An entity graph is a template that captures the boundaries of an
 * operation or query. Every entity graph has a <em>root entity</em>.
 * An instance of {@code EntityGraph} is the root node of a graph and
 * represents this entity. The child nodes of the root node represent
 * persistent attributes, embedded objects, or associations to other
 * entities. A node representing an embedded object or an association
 * to an entity might be the root of a subgraph with its own child
 * nodes.
 * <p>
 * An entity graph is usually used to control the data fetched from
 * the database. The {@linkplain AttributeNode attribute nodes} and
 * {@linkplain Subgraph subgraphs} of an entity graph determine the
 * limits of the graph of associated attributes and entities fetched
 * when an operation which retrieves an instance or instances of the
 * root entity of the graph is executed.
 * <p>
 * When used to specify fetching, an entity graph has two possible
 * interpretations:
 * <ul>
 * <li>As a <em>load graph</em>, where every node explicitly added
 *     to or explicitly removed from the graph overrides the
 *     {@linkplain FetchType fetching strategy} of the attribute
 *     which was specified via annotations or XML descriptor, but
 *     the graph does not affect the fetching strategy of any
 *     attribute which was neither added to nor removed from the
 *     graph.
 * <li>As a <em>fetch graph</em>, where the graph completely
 *     overrides every fetching strategy specified via annotations
 *     or XML descriptor, and every attribute not explicitly added
 *     to the graph is treated as {@link FetchType#LAZY}.
 * </ul>
 * <p>
 * An entity graph passed as the first argument to
 * {@link EntityManager#find(EntityGraph, Object, FindOption...)},
 * {@link EntityManager#get(EntityGraph, Object, FindOption...)},
 * or {@link TypedQuery#setEntityGraph(EntityGraph)} is
 * interpreted as a load graph.
 * <p>
 * The persistence provider is always permitted to fetch additional
 * entity state beyond that specified by a fetch graph or load graph.
 * It is required, however, that the persistence provider fetch all
 * state specified by the fetch or load graph.
 *
 * @apiNote Support for lazy fetching of {@linkplain Basic basic}
 * fields and properties varies between persistence providers and
 * even between container environments. Therefore, the behavior of
 * a fetch graph, or of a load graph which explicitly removes a
 * {@linkplain Basic basic} attribute, is likely to lack portability.
 *
 * @param <T> The type of the root entity.
 *
 * @see AttributeNode
 * @see Subgraph
 * @see NamedEntityGraph
 *
 * @see EntityHandler#createEntityGraph(Class)
 * @see EntityHandler#createEntityGraph(String)
 * @see EntityHandler#getEntityGraph(String)
 * @see EntityManagerFactory#addNamedEntityGraph(String, EntityGraph)
 * @see EntityHandler#find(EntityGraph, Object, FindOption...)
 * @see EntityHandler#get(EntityGraph, Object, FindOption...)
 *
 * @since 2.1
 */
public interface EntityGraph<T> extends Graph<T> {

    /**
     * Return the name of a named {@code EntityGraph} (an entity
     * graph defined by means of the {@link NamedEntityGraph}
     * annotation, XML descriptor element, or added by means of the
     * {@link EntityManagerFactory#addNamedEntityGraph} method).
     * Returns null if the {@code EntityGraph} is not a named
     * {@code EntityGraph}.
     */
    String getName();

    /**
     * Add additional attributes to this entity graph that
     * correspond to attributes of subclasses of the entity type of
     * this {@code EntityGraph}. Subclass subgraphs automatically
     * include the specified attributes of superclass subgraphs.
     *
     * @param type  entity subclass
     * @return subgraph for the subclass
     * @throws IllegalArgumentException if the type is not an entity type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     */
    <S extends T> Subgraph<S> addTreatedSubgraph(Class<S> type);

}
