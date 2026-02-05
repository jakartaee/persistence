/*
 * Copyright (c) 2011, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1

package jakarta.persistence;

import jakarta.persistence.metamodel.Attribute;

import java.util.Map;
import java.util.Set;

/**
 * Represents an attribute node of an entity graph.
 *
 * @param <T> The type of the attribute.
 *
 * @see EntityGraph
 * @see Subgraph
 * @see NamedAttributeNode
 *
 * @since 2.1
 */
public interface AttributeNode<T> {

    /**
     * Return the name of the attribute corresponding to the
     * attribute node.
     * @return name of the attribute
     */
    String getAttributeName();

    /**
     * The attribute from which this branch of the graph or subgraph is forked.
     *
     * @return The metamodel object representing the graphed attribute
     *
     * @since 4.0
     */
    Attribute<?,T> getAttribute();

    /**
     * Add a subgraph rooted at this attribute node, which
     * must be an embedded attribute or an association to
     * an entity class.
     * @return the added subgraph
     * @see EntityGraph#addSubgraph(Attribute) 
     * @since 4.0
     */
    Subgraph<T> addSubgraph();

    /**
     * Add a subgraph rooted at this attribute node, which
     * must be an association to an entity class, for an
     * entity subclass of the entity class.
     * @param type The subclass of the entity class
     * @param <S> The type of the subclass
     * @return the added subgraph
     * @see EntityGraph#addTreatedSubgraph(Attribute, Class)
     * @since 4.0
     */
    <S extends T> Subgraph<S> addTreatedSubgraph(Class<S> type);

    /**
     * Return a map of subgraphs associated with this attribute
     * node.
     * @return a {@link Map} of subgraphs associated with this
     * attribute node or an empty {@code Map} if none have been
     * defined
     */
    Map<Class<?>, Subgraph<?>> getSubgraphs();

    /**
     * Return a map of subgraphs associated with this attribute
     * node's map key.
     * @return a {@link Map} of subgraphs associated with this
     * attribute node's map key or an empty {@code Map} if none
     * have been defined
     */
    Map<Class<?>, Subgraph<?>> getKeySubgraphs();

    /**
     * Specify an {@linkplain FetchOption option} controlling
     * how this node is fetched.
     * @param option The option
     * @return the receiving instance
     * @since 4.0
     */
    AttributeNode<T> addOption(FetchOption option);

    /**
     * Return the {@linkplain FetchOption options} controlling
     * how this node is fetched.
     * <ul>
     * <li>If this is an {@linkplain Graph#addAttributeNode added}
     *     node, the returned options contain {@link FetchType#EAGER}.
     * <li>If this is a {@linkplain Graph#removeAttributeNode removed}
     *     node, the returned options contain {@link FetchType#LAZY}.
     * </ul>
     * @return The options for this node
     * @since 4.0
     */
    Set<FetchOption> getOptions();
}

