/*
 * Copyright (c) 2023, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2

package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;

import java.util.List;

/**
 * Declares operations common to {@link EntityGraph} and {@link Subgraph},
 * most importantly, operations for adding and removing attribute nodes
 * and for creating subgraphs.
 * <p>
 * Any method creating a {@linkplain Subgraph subgraph} of the graph also
 * implicitly creates the {@linkplain AttributeNode attribute node} at
 * which the subgraph is rooted; it is redundant to separately create such
 * an attribute node. Removing an attribute node also removes any subgraph
 * rooted at that node.
 * <p>
 * When a graph is to be interpreted as a load graph, it is meaningful to
 * remove an attribute node which has not been explicitly added to the graph.
 * This operation is interpreted to suppress the inclusion of an attribute
 * mapped for eager fetching, which would otherwise be included by default.
 *
 * @see EntityGraph
 * @see Subgraph
 *
 * @param <T> The type of the root entity.
 *
 * @since 3.2
 */
public interface Graph<T> {

    /**
     * The managed type at which this graph or subgraph is rooted.
     *
     * @return The metamodel object representing the graphed type
     *
     * @since 4.0
     */
    @Nonnull
    ManagedType<T> getGraphedType();

    /**
     * Get an existing attribute node for the attribute with the given
     * name, or add a new attribute node if there is no existing node,
     * cancelling the effect of any prior
     * {@linkplain #removeAttributeNode removal}.
     *
     * <p>Added nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#EAGER}.
     *
     * @param attributeName the name of an attribute of the managed type
     * @param <Y> the type of the attribute
     * @return the attribute node
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addAttributeNode(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> addAttributeNode(@Nonnull String attributeName);

    /**
     * Get an existing attribute node for the given attribute, or add
     * a new attribute node if there is no existing node, cancelling
     * the effect of any prior {@linkplain #removeAttributeNode removal}.
     *
     * <p>Added nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#EAGER}.
     *
     * @param attribute  attribute
     * @param <Y> the type of the attribute
     * @return the attribute node
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> addAttributeNode(@Nonnull Attribute<? super T, Y> attribute);

    /**
     * Determine if there is an existing attribute node for the attribute
     * with the given name.
     *
     * @param attributeName the name of an attribute of the managed type
     * @return true if there is an existing attribute node
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #hasAttributeNode(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     *
     * @since 3.2
     */
    boolean hasAttributeNode(@Nonnull String attributeName);

    /**
     * Determine if there is an existing attribute node for the given
     * attribute.
     *
     * @param attribute  attribute
     * @return true if there is an existing attribute node
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity
     *
     * @since 3.2
     */
    boolean hasAttributeNode(@Nonnull Attribute<? super T, ?> attribute);

    /**
     * Get an existing attribute node for the attribute with the given
     * name.
     *
     * @param attributeName the name of an attribute of the managed type
     * @return the attribute node
     * @throws java.util.NoSuchElementException if there is no existing
     *         node for the attribute
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #getAttributeNode(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     *
     * @param <Y> the type of the attribute
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> getAttributeNode(@Nonnull String attributeName);

    /**
     * Get an existing attribute node for the given attribute.
     *
     * @param attribute  attribute
     * @param <Y> the type of the attribute
     * @return the attribute node
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity
     * @throws java.util.NoSuchElementException if there is no existing
     *         node for the attribute
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> getAttributeNode(@Nonnull Attribute<? super T, Y> attribute);

    /**
     * Remove an attribute node from the entity graph, cancelling the
     * effect of any prior {@linkplain #addAttributeNode addition}.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     *
     * <p>Removed nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#LAZY}.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #removeAttributeNode(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     *
     * @since 3.2
     */
    void removeAttributeNode(@Nonnull String attributeName);

    /**
     * Remove an attribute node from the entity graph, cancelling the
     * effect of any prior {@linkplain #addAttributeNode addition}.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     *
     * <p>Removed nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#LAZY}.
     *
     * @apiNote This operation does not literally remove a node from
     * the entity graph object; instead, it suppresses the inclusion
     * of an edge in the logical graph of fetched associations that
     * is inferred from this entity graph when it is interpreted as
     * a load graph or fetch graph in the context of mapping metadata.
     *
     * @param attribute  attribute
     *
     * @since 3.2
     */
    void removeAttributeNode(@Nonnull Attribute<? super T, ?> attribute);

    /**
     * Remove all attribute nodes of the given attribute type, cancelling
     * the effect of any prior {@linkplain #addAttributeNode additions}.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of attributes mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     *
     * <p>Removed nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#LAZY}.
     *
     * @param nodeType the type of attribute to remove
     *
     * @since 3.2
     */
    void removeAttributeNodes(@Nonnull Attribute.PersistentAttributeType nodeType);

    /**
     * Add one or more attribute nodes to the entity graph,
     * cancelling the effect of any prior
     * {@linkplain #removeAttributeNode removals}.
     * If there is already an existing node for one of the given
     * attribute names, and it is not marked for removal, that
     * particular argument is ignored and has no effect.
     *
     * <p>Added nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#EAGER}.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @throws IllegalArgumentException if there is no attribute
     *         for one of the given names
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addAttributeNodes(Attribute...)}.
     *          Use of the typesafe version is strongly preferred.
     */
    void addAttributeNodes(@Nonnull String... attributeName);

    /**
     * Add one or more attribute nodes to the entity graph,
     * cancelling the effect of any prior
     * {@linkplain #removeAttributeNode removals}.
     * If there is already an existing node for one of the given
     * attributes, and it is not marked for removal, that particular
     * argument is ignored and has no effect.
     *
     * <p>Added nodes are reflected in the list of
     * {@linkplain #getAttributeNodes child nodes} as instances of
     * {@link AttributeNode} with option {@link FetchType#EAGER}.
     *
     * @param attribute  attribute
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    void addAttributeNodes(@Nonnull Attribute<? super T, ?>... attribute);

    /**
     * Add a node to the graph representing a managed type. This
     * allows for construction of multi-node entity graphs that
     * include related managed types.
     *
     * @param attribute the attribute
     * @param <X> the managed type of the attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull Attribute<? super T, X> attribute);

    /**
     * Add a node to the graph representing a managed type with
     * inheritance. This allows for multiple subclass subgraphs
     * to be defined for this node of the entity graph. Subclass
     * subgraphs automatically include the specified attributes
     * of superclass subgraphs.
     *
     * @param attribute the attribute
     * @param type an entity subclass of the attribute type
     * @param <Y> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <Y> Subgraph<Y> addTreatedSubgraph(@Nonnull Attribute<? super T, ? super Y> attribute,
                                       @Nonnull Class<Y> type);

    /**
     * Add a node to the graph representing a managed type. This
     * allows for construction of multi-node entity graphs that
     * include related managed types.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param <X> the managed type of the attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addSubgraph(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph representing a managed type with
     * inheritance. This allows for multiple subclass subgraphs
     * to be defined for this node of the entity graph. Subclass
     * subgraphs automatically include the specified attributes
     * of superclass subgraphs.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param type an entity subclass of the collection element type
     * @param <X> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addSubgraph(Attribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull String attributeName,
                                @Nonnull Class<X> type);

    /**
     * Add a node to the graph representing a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute the attribute
     * @param <E> the managed type of the collection element
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <E> Subgraph<E> addElementSubgraph(@Nonnull PluralAttribute<? super T, ?, E> attribute);

    /**
     * Add a node to the graph representing a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute the attribute
     * @param type an entity subclass of the collection element type
     * @param <E> the managed type of the collection element
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <E> Subgraph<E> addTreatedElementSubgraph(@Nonnull PluralAttribute<? super T, ?, ? super E> attribute,
                                              @Nonnull Class<E> type);

    /**
     * Add a node to the graph representing a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param <X> the managed type of the collection element
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addElementSubgraph(PluralAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addElementSubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph that representing a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param type an entity subclass of the collection element type
     * @param <X> the type of the subclass
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addElementSubgraph(PluralAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addElementSubgraph(@Nonnull String attributeName,
                                       @Nonnull Class<X> type);

    /**
     * Add a node to the graph representing a map key that is a
     * managed type. This allows for construction of multi-node
     * entity graphs that include related managed types.
     *
     * @param attribute the attribute
     * @param <K> the managed type of the map key
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <K> Subgraph<K> addMapKeySubgraph(@Nonnull MapAttribute<? super T, K, ?> attribute);

    /**
     * Add a node to the graph representing a map key that is a
     * managed type with inheritance. This allows for construction
     * of multi-node entity graphs that include related managed
     * types. Subclass subgraphs automatically include the
     * specified attributes of superclass subgraphs.
     *
     * @param attribute the attribute
     * @param type an entity subclass of the map key type
     * @param <K> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <K> Subgraph<K> addTreatedMapKeySubgraph(@Nonnull MapAttribute<? super T, ? super K, ?> attribute,
                                             @Nonnull Class<K> type);

    /**
     * Add a node to the graph representing a map key that is a
     * managed type. This allows for construction of multi-node
     * entity graphs that include related managed types.
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param <X> the managed type of the map key
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addMapKeySubgraph(MapAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addKeySubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph representing a map key that is a
     * managed type with inheritance. This allows for construction
     * of multi-node entity graphs that include related managed types.
     * Subclass subgraphs will include the specified attributes of
     * superclass subgraphs
     *
     * @param attributeName the name of an attribute of the managed
     *                      type
     * @param type an entity subclass of the map key type
     * @param <X> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if there is no attribute
     *         for the given name
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #addMapKeySubgraph(MapAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <X> Subgraph<X> addKeySubgraph(@Nonnull String attributeName,
                                   @Nonnull Class<X> type);

    /**
     * Return the attribute nodes representing the persistent
     * attributes of this managed type that were explicitly
     * {@linkplain #addAttributeNode included in (added to)} or
     * {@linkplain #removeAttributeNode excluded from (removed from)}
     * the graph. The {@linkplain AttributeNode#getOptions options}
     * of the returned nodes may be used to distinguish added nodes
     * from removed nodes.
     * <ul>
     * <li>Added nodes are represented by instances of
     *     {@link AttributeNode} with option {@link FetchType#EAGER}.
     * <li>Removed nodes are represented by instances of
     *     {@link AttributeNode} with option {@link FetchType#LAZY}.
     * </ul>
     * @return list of attribute nodes added to or removed from the
     *         graph, or an empty list if no nodes have been defined
     */
    @Nonnull
    List<AttributeNode<?>> getAttributeNodes();

}
