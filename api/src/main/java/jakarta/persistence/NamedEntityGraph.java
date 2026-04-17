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
//     Petros Splinakis - 2.2
//     Linda DeMichiel - 2.1

package jakarta.persistence;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a named {@linkplain EntityGraph entity graph} or subgraph
 * of a named entity graph. This annotation must be applied to the root
 * entity of the graph or subgraph.
 *
 * <p> The {@linkplain jakarta.persistence.AttributeNode nodes} and
 * {@linkplain jakarta.persistence.Subgraph subgraphs} of an entity
 * graph control the limits of the graph of associated attributes and
 * entities fetched when an operation which retrieves an instance or
 * instances of the root entity is executed.
 *
 * <p> A well-defined entity graph is a tree. The application program
 * is responsible for ensuring that every named entity graph is acyclic.
 * If a named entity graph contains a cycle, the behavior is undefined.
 * A provider may reject the graph at runtime; it may reject it when
 * the persistence unit is initialized; or it may transform the graph
 * into an acyclic graph according to some vendor-specific algorithm.
 * Portable applications must avoid relying on such behavior.
 *
 * <p> There are two ways to specify the nodes and subgraphs of a named
 * entity graph defined in annotations:
 * <ul>
 * <li>by annotating persistent attributes of the entity class with
 *     {@link AttributeNode} and {@link Subgraph} annotations which
 *     reference the containing {@link NamedEntityGraph} by name, or
 * <li>by nesting {@link NamedAttributeNode} and {@link NamedSubgraph}
 *     annotations within the {@link NamedEntityGraph} annotation.
 * </ul>
 * <p> The two approaches may be mixed within the same entity graph
 * definition.
 *
 * <p> In the first approach, the definition of the named entity graph
 * is distributed across the entity class and its associated classes.
 * {@snippet :
 * @NamedEntityGraph(name = "EmployeeWithProjectTasksAndEmployer")
 * @Entity // root entity of graph
 * public class Employee {
 *     ...
 *     // fetched attribute node
 *     @NamedEntityGraph.AttributeNode(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToOne(fetch=LAZY) Employer employer;
 *
 *     // reference to subgraph defined in Project class
 *     @NamedEntityGraph.Subgraph(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToMany Set<Project> projects;
 * }
 *
 * @NamedEntityGraph(name = "EmployeeWithProjectTasksAndEmployer")
 * @Entity // root entity of subgraph
 * public class Project {
 *     ...
 *     // reference to subgraph defined in Task class
 *     @NamedEntityGraph.Subgraph(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @OneToMany List<Task> tasks;
 * }
 * }
 *
 * <p> When {@link Subgraph} is used, there might be multiple entity
 * classes with {@code NamedEntityGraph} annotations specifying
 * identical values for the {@code name} member. In this scenario,
 * the root entity of the named graph is identified by it being the
 * unique entity class that is not referenced by the {@code subgraph}
 * member of any {@link Subgraph} annotation belonging to the named
 * graph. If there is no such entity class, or if it is not unique,
 * the behavior is undefined. An application must ensure that each
 * named entity graph has a unique root entity.
 *
 * <p> In the second approach, the definition of the named entity graph
 * is contained entirely within the {@link NamedEntityGraph} annotation.
 * {@snippet :
 * @NamedEntityGraph(
 *         name = "EmployeeWithProjectTasksAndEmployer",
 *         // fetched attributes of Employee
 *         attributeNodes = {
 *             @NamedAttributeNode("projects"),
 *             @NamedAttributeNode(
 *                     value = "employer", // name of association
 *                     subgraph = "Projects" // reference to subgraph
 *             )
 *         },
 *         // list of subgraphs of the entity graph
 *         subgraphs = {
 *             // subgraph specifying fetched attributes of Project
 *             @NamedSubgraph(
 *                     name = "Projects",
 *                     // fetched attributes of Project
 *                     attributeNodes = @NamedAttributeNode(
 *                             value = "tasks", // name of association
 *                             subgraph = "Tasks" // reference to subgraph
 *                     )
 *             ),
 *             // subgraph specifying fetched attributes of Task
 *             @NamedSubgraph(
 *                     name = "Tasks",
 *                     attributeNodes = ...
 *             )
 *         }
 * )
 * @Entity // root entity of graph
 * public class Employee { ... }
 * }
 *
 * <p> A named entity graph defined using annotations is reified at
 * runtime as an instance of {@link EntityGraph}, and its child
 * nodes and subgraphs are reified respectively as instances of
 * {@link jakarta.persistence.AttributeNode} and
 * {@link jakarta.persistence.Subgraph}.
 *
 * <p> A reference to a named entity graph may be obtained by
 * calling {@link EntityManagerFactory#getNamedEntityGraphs(Class)}
 * or {@link EntityHandler#getEntityGraph(String)} and may be passed
 * to {@link EntityHandler#find(EntityGraph, Object, FindOption...)}
 * or {@link EntityHandler#get(EntityGraph, Object, FindOption...)}.
 * {@snippet :
 * Object employee =
 *         em.find(em.getEntityGraph("EmployeeWithProjects"),
 *                 employeeId);
 * }
 *
 * <p> Alternatively, a reference to a named entity graph may be obtained
 * from the {@link EntityManagerFactory}.
 * {@snippet :
 * EntityGraph<Employee> graph =
 *         emf.getNamedEntityGraphs(Employee.class)
 *             .get("EmployeeWithProjects")
 * Employee employee = em.find(graph, employeeId);
 * }
 *
 * <p> Alternatively, a reference to a named entity graph may be obtained
 * from the static metamodel.
 * {@snippet :
 * Employee employee =
 *         em.find(Employee_._EmployeeWithProjects,
 *                 employeeId);
 * }
 *
 * @see NamedAttributeNode
 * @see NamedSubgraph
 * @see EntityGraph
 *
 * @since 2.1
 */
@Repeatable(NamedEntityGraphs.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NamedEntityGraph {

    /**
     * (Optional) The name used to identify this entity graph in calls to
     * {@link EntityHandler#getEntityGraph(String)}. If no name is explicitly
     * specified, the name defaults to the entity name of the annotated root
     * entity.
     * <p>Entity graph names must be unique within a given persistence unit.
     * If two {@link NamedEntityGraph @NamedEntityGraph} annotations declare
     * the same name, then one must be a subgraph of the other, as specified
     * via the {@link Subgraph} annotations.
     */
    String name() default "";

    /**
     * (Optional) A list of attributes of the entity that are included in
     * this graph.
     *
     * @apiNote Alternatively, use {@link AttributeNode}
     */
    NamedAttributeNode[] attributeNodes() default {};

    /**
     * (Optional) Includes every attribute of the annotated entity
     * class as an attribute node in the {@link NamedEntityGraph}
     * without the need to explicitly list them. Included attributes
     * can still be fully specified by an attribute node referencing
     * a subgraph.
     */
    boolean includeAllAttributes() default false;

    /**
     * (Optional) A list of subgraphs that are included in the
     * entity graph. These are referenced by name from
     * {@link NamedAttributeNode} annotations.
     *
     * @apiNote Alternatively, use {@link Subgraph}
     *
     * @see NamedAttributeNode#subgraph
     */
    NamedSubgraph[] subgraphs() default {};

    /**
     * (Optional) A list of subgraphs that will add additional
     * attributes for subclasses of the annotated entity class to
     * the entity graph. Specified attributes from superclasses
     * are included in subclasses.
     *
     * @see EntityGraph#addTreatedSubgraph(Class)
     */
    NamedSubgraph[] subclassSubgraphs() default {};

    /**
     * Declares that the annotated attribute is an attribute
     * node in a {@linkplain NamedEntityGraph named entity graph}.
     * The {@link #graph} member must specify the name of the
     * graph.
     *
     * <p>Multiple {@code @AttributeNode} annotations may be
     * applied to a single attribute, but each must specify the
     * name of a distinct parent graph.
     *
     * <p>An {@code AttributeNode} is reified at runtime as an
     * instance of {@link jakarta.persistence.AttributeNode}.
     *
     * @apiNote Alternatively, use {@link NamedAttributeNode}
     * when nesting the declaration of the attribute node
     * directly within a {@link NamedEntityGraph} annotation.
     *
     * @see NamedEntityGraph
     * @see Subgraph
     * @see EntityGraph#addAttributeNode
     *
     * @since 4.0
     */
    @Repeatable(AttributeNodes.class)
    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @interface AttributeNode {
        /**
         * The name of the containing entity graph, as specified by
         * {@link NamedEntityGraph#name}. If no name is explicitly
         * specified, the name defaults to the entity name of the
         * annotated entity class.
         */
        String graph() default "";
    }

    /**
     * Declares that the annotated association is the root of a
     * subgraph of a {@linkplain NamedEntityGraph named entity
     * graph}.
     * <ul>
     * <li>The {@link #graph} member specifies the name of
     *     the containing graph.
     * <li>The {@link #subgraph} member specifies the name of
     *     a {@linkplain NamedEntityGraph named entity graph}
     *     whose root is the associated entity.
     * </ul>
     * <p>Multiple {@code @Subgraph} annotations may be applied
     * to a single attribute, but each must specify the name of
     * a distinct parent graph.
     *
     * <p>A {@code Subgraph} is reified at runtime as an
     * instance of {@link jakarta.persistence.Subgraph}.
     *
     * @apiNote Alternatively, use {@link NamedSubgraph} when
     * nesting the definition of a subgraph directly within a
     * {@link NamedEntityGraph} annotation.
     *
     * @see NamedEntityGraph
     * @see AttributeNode
     * @see EntityGraph#addSubgraph
     * @see EntityGraph#addElementSubgraph
     *
     * @since 4.0
     */
    @Repeatable(Subgraphs.class)
    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @interface Subgraph {
        /**
         * The name of the containing entity graph, as specified by
         * {@link NamedEntityGraph#name}. If no name is explicitly
         * specified, the name defaults to the entity name of the
         * annotated entity class.
         */
        String graph() default "";

        /**
         * The name of an entity graph whose root is the associated
         * entity, as specified by {@link NamedEntityGraph#name}.
         * If no subgraph name is explicitly specified, the subgraph
         * name defaults to the {@linkplain #graph name of the
         * containing graph}.
         *
         * <p>The target subgraph must be explicitly declared via a
         * {@link NamedEntityGraph} annotation of the target entity
         * of the annotated association.
         */
        String subgraph() default "";
    }

    /**
     * Groups {@link AttributeNode} annotations.
     *
     * @see AttributeNode
     * @since 4.0
     */
    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @interface AttributeNodes {
        AttributeNode[] value();
    }

    /**
     * Groups {@link Subgraph} annotations.
     *
     * @see Subgraph
     * @since 4.0
     */
    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    @interface Subgraphs {
        Subgraph[] value();
    }
}

