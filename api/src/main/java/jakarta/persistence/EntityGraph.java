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

package jakarta.persistence;

import jakarta.persistence.metamodel.Attribute;
import java.util.List;

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
public interface EntityGraph<T> {

    /**
     * Return the name of a named EntityGraph (an entity graph
     * defined by means of the <code>NamedEntityGraph</code>
     * annotation, XML descriptor element, or added by means of the
     * <code>addNamedEntityGraph</code> method.  Returns null if the
     * EntityGraph is not a named EntityGraph.
     */
    public String getName();

    /**
     * Add one or more attribute nodes to the entity graph.
     *
     * @param attributeName  name of the attribute
     * @throws IllegalArgumentException if the attribute is not an 
     *         attribute of this entity.
     * @throws IllegalStateException if the EntityGraph has been 
     *         statically defined
     */
    public void addAttributeNodes(String ... attributeName);

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
    public <X> Subgraph<X> addSubgraph(Attribute<T, X> attribute);

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
     */
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
     */
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
     */
    public <X> Subgraph<? extends X> addKeySubgraph(Attribute<T, X> attribute, Class<? extends X> type);

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
    public <T> Subgraph<? extends T> addSubclassSubgraph(Class<? extends T> type);


    /**
     * Return the attribute nodes of this entity that are included in
     * the entity graph.
     * @return attribute nodes for the annotated entity type or empty
     *         list if none have been defined
     */
    public List<AttributeNode<?>> getAttributeNodes();

}
