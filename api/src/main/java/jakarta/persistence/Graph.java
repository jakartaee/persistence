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
     * Add an attribute nodes to the entity graph.
     *
     * @param attributeName  name of the attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public void addAttributeNode(String attributeName);

    /**
     * Add an attribute node to the entity graph.
     *
     * @param attribute  attribute
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public void addAttributeNode(Attribute<? super T, ?> attribute);

    /**
     * Remove an attribute node from the entity graph.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     * @param attributeName  name of the attribute
     * @since 3.2
     */
    public void removeAttributeNode(String attributeName);

    /**
     * Remove an attribute node from the entity graph.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of an attribute mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     * @param attribute  attribute
     * @since 3.2
     */
    public void removeAttributeNode(Attribute<? super T, ?> attribute);

    /**
     * Remove all attribute nodes of the given attribute types.
     * When this graph is interpreted as a load graph, this operation
     * suppresses inclusion of attributes mapped for eager fetching.
     * The effect of this call may be overridden by subsequent
     * invocations of {@link #addAttributeNode} or {@link #addSubgraph}.
     * @since 3.2
     */
    public void removeAttributeNodes(Attribute.PersistentAttributeType nodeTypes);

    /**
     * Add one or more attribute nodes to the entity graph.
     *
     * @param attributeName  name of the attribute     
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this managed type.
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    public void addAttributeNodes(String ... attributeName);

    /**
     * Add one or more attribute nodes to the entity graph.
     * @param attribute  attribute
     *
     * @throws IllegalStateException if this EntityGraph has been 
     *         statically defined
     */
    public void addAttributeNodes(Attribute<? super T, ?>... attribute);

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
    public <X> Subgraph<X> addSubgraph(Attribute<? super T, X> attribute);

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
    public <Y> Subgraph<Y> addTreatedSubgraph(Attribute<? super T, ? super Y> attribute, Class<Y> type);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance.  This allows for multiple subclass
     * subgraphs to be defined for this node of the entity
     * graph. Subclass subgraphs will automatically include the specified
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
    public <X> Subgraph<? extends X> addSubgraph(Attribute<? super T, X> attribute, Class<? extends X> type);

    /**
     * Add a node to the graph that corresponds to a managed
     * type. This allows for construction of multi-node entity graphs
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
    public <X> Subgraph<X> addSubgraph(String attributeName);

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
     *         statically defined
     */
    public <X> Subgraph<X> addSubgraph(String attributeName, Class<X> type);

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
    public <E> Subgraph<E> addElementSubgraph(PluralAttribute<? super T, ?, E> attribute);

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
    public <E> Subgraph<E> addTreatedElementSubgraph(PluralAttribute<? super T, ?, ? super E> attribute, Class<E> type);

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
     *          statically defined
     */
    public <X> Subgraph<X> addElementSubgraph(String attributeName);

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
     *          statically defined
     */
    public <X> Subgraph<X> addElementSubgraph(String attributeName, Class<X> type);

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
    public <K> Subgraph<K> addMapKeySubgraph(MapAttribute<? super T, K, ?> attribute);

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
    public <K> Subgraph<K> addTreatedMapKeySubgraph(MapAttribute<? super T, ? super K, ?> attribute, Class<K> type);

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
    public <X> Subgraph<X> addKeySubgraph(Attribute<? super T, X> attribute);

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
    public <X> Subgraph<? extends X> addKeySubgraph(Attribute<? super T, X> attribute, Class<? extends X> type);

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
     *          statically defined
     */
    public <X> Subgraph<X> addKeySubgraph(String attributeName);

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
    public <X> Subgraph<X> addKeySubgraph(String attributeName, Class<X> type);

    /**
     * Return the attribute nodes corresponding to the attributes of
     * this managed type that are included in the graph.
     * @return list of attribute nodes included in the graph or an
     * empty list if none have been defined
     */
    public List<AttributeNode<?>> getAttributeNodes();

}
