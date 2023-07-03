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
//     Gavin King - 3.2

package jakarta.persistence;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;

import java.util.List;

/**
 * Declares operations common to {@link EntityGraph} and {@link Subgraph}.
 *
 * @since 6.3
 */
public interface AbstractGraph<T> {

    /**
     * Add an attribute nodes to the entity graph.
     *
     * @param attributeName  name of the attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 6.3
     */
    public void addAttributeNode(String attributeName);

    /**
     * Add an attribute node to the entity graph.
     *
     * @param attribute  attribute
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     *
     * @since 6.3
     */
    public void addAttributeNode(Attribute<T, ?> attribute);

    /**
     * Add one or more attribute nodes to the entity graph.
     *
     * @param attributeName  name of the attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     */
    public void addAttributeNodes(String... attributeName);

    /**
     * Add one or more attribute nodes to the entity graph.
     *
     * @param attribute  attribute
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     */
    public void addAttributeNodes(Attribute<T, ?>... attribute);

    /**
     * Add a node to the graph that corresponds to a managed
     * type. This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not a managed type
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
     * @since 6.3
     */
    public <X, Y extends X> Subgraph<Y> addTreatedSubgraph(Attribute<? super T, X> attribute, Class<Y> type);

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
     * @deprecated use {@link #addTreatedSubgraph(Attribute, Class)}
     */
    @Deprecated(since = "3.2")
    public <X> Subgraph<? extends X> addSubgraph(Attribute<T, X> attribute, Class<? extends X> type);

    /**
     * Add a node to the graph that corresponds to a managed
     * type. This allows for construction of multi-node entity graphs
     * that include related managed types.
     *
     * @param attributeName  name of the attribute
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target type
     *         is not a managed type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     */
    public <X> Subgraph<X> addSubgraph(String attributeName);

    /**
     * Add a node to the graph that corresponds to a managed
     * type with inheritance.  This allows for multiple subclass
     * subgraphs to be defined for this node of the entity graph.
     * Subclass subgraphs will automatically include the specified
     * attributes of superclass subgraphs.
     *
     * @param attributeName  name of the attribute
     * @param type  entity subclass
     * @return subgraph for the attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this managed type.
     * @throws IllegalArgumentException if the attribute's target type
     *         is not a managed type
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
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public <E> Subgraph<E> addElementSubgraph(PluralAttribute<T, ?, E> attribute);

    /**
     * Add a node to the graph that corresponds to a collection element
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public <E, F extends E> Subgraph<F> addElementSubgraph(PluralAttribute<T, ?, E> attribute, Class<F> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public <K> Subgraph<K> addKeySubgraph(MapAttribute<T, K, ?> attribute);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types.  Subclass subgraphs will include the specified
     * attributes of superclass subgraphs.
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     *
     * @since 3.2
     */
    public <K, J extends K> Subgraph<J> addTreatedKeySubgraph(MapAttribute<? super T, K, ?> attribute, Class<J> type);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type. This allows for construction of
     * multi-node entity graphs that include related managed types.
     *
     * @param attribute  attribute
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     * @deprecated the signature of this method is incorrect,
     *             use {@link #addKeySubgraph(MapAttribute)}
     */
    @Deprecated(since = "3.2")
    public <X> Subgraph<X> addKeySubgraph(Attribute<T, X> attribute);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types.  Subclass subgraphs will include the specified
     * attributes of superclass subgraphs.
     *
     * @param attribute  attribute
     * @param type  entity subclass
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     * @deprecated the signature of this method is incorrect,
     *             use {@link #addTreatedKeySubgraph(MapAttribute, Class)}
     */
    @Deprecated(since = "3.2")
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
     * @throws IllegalArgumentException if the attribute's target type
     *         is not an entity
     * @throws IllegalStateException if this EntityGraph has been
     *          statically defined
     */
    public <X> Subgraph<X> addKeySubgraph(String attributeName);

    /**
     * Add a node to the graph that corresponds to a map key
     * that is a managed type with inheritance. This allows for
     * construction of multi-node entity graphs that include related
     * managed types. Subclass subgraphs will automatically include
     * the specified attributes of superclass subgraphs
     *
     * @param attributeName  name of the attribute
     * @param type  entity subclass
     * @return subgraph for the key attribute
     * @throws IllegalArgumentException if the attribute is not an
     *         attribute of this entity.
     * @throws IllegalArgumentException if the attribute's target type
     *         is not a managed type
     * @throws IllegalStateException if this EntityGraph has been
     *         statically defined
     */
    public <X> Subgraph<X> addKeySubgraph(String attributeName, Class<X> type);

    /**
     * Add additional attributes to this entity graph that
     * correspond to attributes of subclasses of this EntityGraph's
     * entity type.  Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs.
     *
     * @param type  entity subclass
     * @return subgraph for the subclass
     * @throws IllegalArgumentException if the type is not an entity type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     */
    public <S extends T> Subgraph<S> addTreatedSubgraph(Class<S> type);

    /**
     * Add additional attributes to this entity graph that
     * correspond to attributes of subclasses of this EntityGraph's
     * entity type.  Subclass subgraphs will automatically include the
     * specified attributes of superclass subgraphs.
     *
     * @param type  entity subclass
     * @return subgraph for the subclass
     * @throws IllegalArgumentException if the type is not an entity type
     * @throws IllegalStateException if the EntityGraph has been
     *         statically defined
     * @deprecated use {@link #addTreatedSubgraph(Class)}
     */
    @Deprecated(since = "3.2")
    public <S> Subgraph<? extends S> addSubclassSubgraph(Class<? extends S> type);

    /**
     * Return the attribute nodes of this entity that are included in
     * the graph.
     * @return attribute nodes for the annotated type or empty list if
     *         none have been defined
     */
    public List<AttributeNode<?>> getAttributeNodes();
}
