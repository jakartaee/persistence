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
    ManagedType<T> getGraphedType();

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
    <Y> AttributeNode<Y> addAttributeNode(String attributeName);

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
    <Y> AttributeNode<Y> addAttributeNode(Attribute<? super T, Y> attribute);

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
    boolean hasAttributeNode(String attributeName);

    /**
     * Determine if there is an existing attribute node for the given
     * attribute.
     *
     * @param attribute  attribute
     * @return true if there is an existing attribute node
     *
     * @since 3.2
     */
    boolean hasAttributeNode(Attribute<? super T, ?> attribute);

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
    <Y> AttributeNode<Y> getAttributeNode(String attributeName);

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
    <Y> AttributeNode<Y> getAttributeNode(Attribute<? super T, Y> attribute);

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
    void removeAttributeNode(String attributeName);

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
    void removeAttributeNode(Attribute<? super T, ?> attribute);

    /**
     * Remove all attribute nodes of the given attribute types.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of attributes mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     *
     * @since 3.2
     */
    void removeAttributeNodes(Attribute.PersistentAttributeType nodeTypes);

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
    void addAttributeNodes(String... attributeName);

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
    void addAttributeNodes(Attribute<? super T, ?>... attribute);

    /**
     * Add a node to the graph that corresponds to a managed type.
     * This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attribute the attribute
     * @param <X> the managed type of the attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    <X> Subgraph<X> addSubgraph(Attribute<? super T, X> attribute);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance. This allows for multiple subclass
     * subgraphs to be defined for this node of the entity
     * graph. Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs.
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
    <Y> Subgraph<Y> addTreatedSubgraph(Attribute<? super T, ? super Y> attribute, Class<Y> type);

    /**
     * Add a node to the graph that corresponds to a managed type.
     * This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attributeName the name of the attribute
     * @param <X> the managed type of the attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this managed type.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     */
    <X> Subgraph<X> addSubgraph(String attributeName);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance. This allows for multiple subclass
     * subgraphs to be defined for this node of the entity
     * graph. Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs
     *
     * @param attributeName the name of the attribute
     * @param type an entity subclass of the collection element type
     * @param <X> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not 
     *         an attribute of this managed type.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
      *        statically defined
     */
    <X> Subgraph<X> addSubgraph(String attributeName, Class<X> type);

    /**
     * Add a node to the graph that corresponds to a collection element
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
    <E> Subgraph<E> addElementSubgraph(PluralAttribute<? super T, ?, E> attribute);

    /**
     * Add a node to the graph that corresponds to a collection element
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
    <E> Subgraph<E> addTreatedElementSubgraph(PluralAttribute<? super T, ?, ? super E> attribute, Class<E> type);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName the name of the attribute
     * @param <X> the managed type of the collection element
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    <X> Subgraph<X> addElementSubgraph(String attributeName);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName the name of the attribute
     * @param type an entity subclass of the collection element type
     * @param <X> the type of the subclass
     * @return subgraph for the element attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    <X> Subgraph<X> addElementSubgraph(String attributeName, Class<X> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute the attribute
     * @param <K> the managed type of the map key
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    <K> Subgraph<K> addMapKeySubgraph(MapAttribute<? super T, K, ?> attribute);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types. Subclass subgraphs will automatically include
     * the specified attributes of superclass subgraphs
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
    <K> Subgraph<K> addTreatedMapKeySubgraph(MapAttribute<? super T, ? super K, ?> attribute, Class<K> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attributeName the name of the attribute
     * @param <X> the managed type of the map key
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target 
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    <X> Subgraph<X> addKeySubgraph(String attributeName);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types. Subclass subgraphs will include the specified
     * attributes of superclass subgraphs
     *
     * @param attributeName the name of the attribute
     * @param type an entity subclass of the map key type
     * @param <X> the type of the subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target
     *         type is not a managed type
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     */
    <X> Subgraph<X> addKeySubgraph(String attributeName, Class<X> type);

    /**
     * Return the attribute nodes corresponding to the attributes of
     * this managed type that are included in the graph.
     * @return list of attribute nodes included in the graph or an
     * empty list if none have been defined
     */
    List<AttributeNode<?>> getAttributeNodes();

}
