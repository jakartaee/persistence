/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 3.2

package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;

import java.util.List;

/**
 * Declares operations common to {@link EntityGraph} and {@link Subgraph}.
 *
 * @see EntityGraph
 * @see Subgraph
 *
 * @since 3.2
 */
public interface Graph<T> {

    /**
     * Get an existing attribute node for the attribute with the given
     * name, or add a new attribute node if there is no existing node.
     *
     * @param attributeName  name of the attribute
     * @return the attribute node
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> addAttributeNode(@Nonnull String attributeName);

    /**
     * Get an existing attribute node for the given attribute, or add
     * a new attribute node if there is no existing node.
     *
     * @param attribute  attribute
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
     * @param attributeName  name of the attribute
     * @return true if there is an existing attribute node
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
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
     *
     * @since 3.2
     */
    boolean hasAttributeNode(@Nonnull Attribute<? super T, ?> attribute);

    /**
     * Get an existing attribute node for the attribute with the given
     * name.
     *
     * @param attributeName  name of the attribute
     * @return the attribute node
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws java.util.NoSuchElementException if there is no existing
     *         node for the attribute
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> getAttributeNode(@Nonnull String attributeName);

    /**
     * Get an existing attribute node for the given attribute.
     *
     * @param attribute  attribute
     * @return the attribute node
     * @throws java.util.NoSuchElementException if there is no existing
     *         node for the attribute
     *
     * @since 3.2
     */
    @Nonnull
    <Y> AttributeNode<Y> getAttributeNode(@Nonnull Attribute<? super T, Y> attribute);

    /**
     * Remove an attribute node from the entity graph.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     * If there is no existing node for the given attribute name, this
     * operation has no effect.
     *
     * @param attributeName  name of the attribute
     *
     * @since 3.2
     */
    void removeAttributeNode(@Nonnull String attributeName);

    /**
     * Remove an attribute node from the entity graph.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     * If there is no existing node for the given attribute, this
     * operation has no effect.
     *
     * @param attribute  attribute
     *
     * @since 3.2
     */
    void removeAttributeNode(@Nonnull Attribute<? super T, ?> attribute);

    /**
     * Remove all attribute nodes of the given attribute types.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of attributes mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     *
     * @since 3.2
     */
    void removeAttributeNodes(@Nonnull Attribute.PersistentAttributeType nodeTypes);

    /**
     * Add one or more attribute nodes to the entity graph.
     * If there is already an existing node for one of the given
     * attribute names, that particular argument is ignored and
     * has no effect.
     *
     * @param attributeName  name of the attribute     
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this managed type.
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    void addAttributeNodes(@Nonnull String... attributeName);

    /**
     * Add one or more attribute nodes to the entity graph.
     * If there is already an existing node for one of the given
     * attributes, that particular argument is ignored and has no
     * effect.
     *
     * @param attribute  attribute
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    void addAttributeNodes(@Nonnull Attribute<? super T, ?>... attribute);

    /**
     * Add a node to the graph that corresponds to a managed
     * type. This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull Attribute<? super T, X> attribute);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance.  This allows for multiple subclass
     * subgraphs to be defined for this node of the entity
     * graph. Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs.
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <Y> Subgraph<Y> addTreatedSubgraph(
            @Nonnull Attribute<? super T, ? super Y> attribute,
            @Nonnull Class<Y> type);

    /**
     * Add a node to the graph that corresponds to a managed type
     * with inheritance. This allows for multiple subclass
     * subgraphs to be defined for this node of the entity graph.
     * Subclass subgraphs will automatically include the specified
     * attributes of superclass subgraphs
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     * @deprecated use {@link #addTreatedSubgraph(Attribute, Class)}
     */
    @Deprecated(since = "3.2", forRemoval = true)
    @Nonnull
    <X> Subgraph<? extends X> addSubgraph(
            @Nonnull Attribute<? super T, X> attribute,
            @Nonnull Class<? extends X> type);

    /**
     * Add a node to the graph that corresponds to a managed type.
     * This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attributeName  name of the attribute 
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this managed type.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance.  This allows for multiple subclass
     * subgraphs to be defined for this node of the entity
     * graph. Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs
     *
     * @param attributeName  name of the attribute 
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not 
     *         an attribute of this managed type.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
      *        statically defined
     */
    @Nonnull
    <X> Subgraph<X> addSubgraph(@Nonnull String attributeName,
                                @Nonnull Class<X> type);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <E> Subgraph<E> addElementSubgraph(
            @Nonnull PluralAttribute<? super T, ?, E> attribute);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    @Nonnull
    <E> Subgraph<E> addTreatedElementSubgraph(
            @Nonnull PluralAttribute<? super T, ?, ? super E> attribute,
            @Nonnull Class<E> type);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName  name of the attribute
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addElementSubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName  name of the attribute
     * @param type  entity subclass
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addElementSubgraph(@Nonnull String attributeName,
                                       @Nonnull Class<X> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <K> Subgraph<K> addMapKeySubgraph(
            @Nonnull MapAttribute<? super T, K, ?> attribute);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types.  Subclass subgraphs will automatically include
     * the specified attributes of superclass subgraphs
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <K> Subgraph<K> addTreatedMapKeySubgraph(
            @Nonnull MapAttribute<? super T, ? super K, ?> attribute,
            @Nonnull Class<K> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     * @deprecated use {@link #addMapKeySubgraph(MapAttribute)}
     */
    @Deprecated(since = "3.2", forRemoval = true)
    @Nonnull
    <X> Subgraph<X> addKeySubgraph(@Nonnull Attribute<? super T, X> attribute);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types.  Subclass subgraphs will automatically include
     * the specified attributes of superclass subgraphs
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     * @deprecated use {@link #addTreatedMapKeySubgraph(MapAttribute, Class)}
     */
    @Deprecated(since = "3.2", forRemoval = true)
    @Nonnull
    <X> Subgraph<? extends X> addKeySubgraph(
            @Nonnull Attribute<? super T, X> attribute,
            @Nonnull Class<? extends X> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName  name of the attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addKeySubgraph(@Nonnull String attributeName);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types.  Subclass subgraphs will include the specified
     * attributes of superclass subgraphs
     *
     * @param attributeName  name of the attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     */
    @Nonnull
    <X> Subgraph<X> addKeySubgraph(@Nonnull String attributeName,
                                   @Nonnull Class<X> type);

    /**
     * Return the attribute nodes corresponding to the attributes of
     * this managed type that are included in the graph.
     * @return list of attribute nodes included in the graph or an
     * empty list if none have been defined
     */
    @Nonnull
    List<AttributeNode<?>> getAttributeNodes();

}
