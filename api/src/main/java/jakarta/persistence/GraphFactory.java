/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 4.0

package jakarta.persistence;

import java.util.List;
import java.util.Map;

/**
 * An object which acts as a source of {@linkplain EntityGraph entity graphs}.
 * <p>
 * Note that entity graphs may also be obtained via the
 * {@linkplain jakarta.persistence.metamodel.EntityType static metamodel}.
 *
 * @see EntityManagerFactory
 * @see EntityManager
 *
 * @since 4.0
 */
public interface GraphFactory {

    /**
     * Create a new mutable {@link EntityGraph}, allowing programmatic
     * definition of the graph.
     * @param rootType the root entity type of the new graph
     * @return a trivial entity graph with only a root node
     * @see jakarta.persistence.metamodel.EntityType#createEntityGraph()
     * @since 2.1
     */
    <T> EntityGraph<T> createEntityGraph(Class<T> rootType);

    /**
     * Obtain a mutable copy of a named {@link EntityGraph}, or return
     * {@code null} if there is no entity graph with the given name.
     * @param graphName the name of an existing entity graph
     * @return a copy of the entity graph with the given name,
     *         or {@code null} if there is no such graph
     * @since 2.1
     */
    EntityGraph<?> createEntityGraph(String graphName);

    /**
     * Obtain a mutable copy of a named {@link EntityGraph}.
     * @param rootType the root entity type of the graph
     * @param graphName the name of an existing entity graph
     * @return a copy of the entity graph with the given name
     * @throws IllegalArgumentException if there is no entity
     *         graph with the given name, or if the entity
     *         graph with the given name does not have exactly
     *         the given root entity type
     * @since 4.0
     */
    <T> EntityGraph<T> createEntityGraph(Class<T> rootType, String graphName);

    /**
     * Obtain a named {@link EntityGraph}. The returned instance of
     * {@code EntityGraph} should be considered immutable.
     * @param graphName the name of an existing entity graph
     * @return the entity graph with the given name
     * @throws IllegalArgumentException if there is no entity
     *         graph with the given name
     * @since 2.1
     */
    EntityGraph<?> getEntityGraph(String graphName);

    /**
     * Obtain a named {@link EntityGraph}. The returned instance of
     * {@code EntityGraph} should be considered immutable.
     * @param rootType the root entity type of the graph
     * @param graphName the name of an existing entity graph
     * @return the entity graph with the given name
     * @throws IllegalArgumentException if there is no entity
     *         graph with the given name, or if the entity
     *         graph with the given name does not have exactly
     *         the given root entity type
     * @since 4.0
     */
    <T> EntityGraph<T> getEntityGraph(Class<T> rootType, String graphName);

    /**
     * Return a list of all named {@link EntityGraph}s defined for the
     * given root entity type or for any one of its entity supertypes.
     * @param entityClass the root entity type of the graph
     * @return list of all entity graphs defined for the root entity type
     * @throws IllegalArgumentException if the class is not an entity
     * @since 2.1
     */
    <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass);

    /**
     * A map keyed by {@linkplain NamedEntityGraph#name graph name},
     * containing every named {@linkplain EntityGraph entity graph}
     * whose entity type is assignable to the given Java type.
     * @param entityType any Java type, including {@code Object.class}
     *                   meaning all entity graphs
     * @return a map keyed by graph name
     * @param <E> the specified upper bound on the entity graph types
     * @see jakarta.persistence.metamodel.EntityType#getNamedEntityGraphs()
     * @since 3.2
     */
    <E> Map<String, EntityGraph<? extends E>> getNamedEntityGraphs(Class<E> entityType);

}
