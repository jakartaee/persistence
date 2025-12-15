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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a named {@linkplain EntityGraph entity graph} or subgraph
 * of a named entity graph. This annotation must be applied to the root
 * entity of the graph or subgraph.
 *
 * <p> The {@linkplain AttributeNode nodes} and {@linkplain Subgraph
 * subgraphs} of an entity graph control the limits of the graph of
 * associated attributes and entities fetched when an operation which
 * retrieves an instance or instances of the root entity is executed.
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
 * <li>By annotating persistent attributes of the entity class with
 *     {@link NamedEntityGraphAttributeNode} and
 *     {@link NamedEntityGraphSubgraph} annotations which reference
 *     the containing {@link NamedEntityGraph} by name.
 * <li>By nesting {@link NamedAttributeNode} and {@link NamedSubgraph}
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
 *     @NamedEntityGraphAttributeNode(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToOne(fetch=LAZY) Employer employer;
 *
 *     // reference to subgraph defined in Project class
 *     @NamedEntityGraphSubgraph(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToMany Set<Project> projects;
 * }
 *
 * @NamedEntityGraph(name = "EmployeeWithProjectTasksAndEmployer")
 * @Entity // root entity of subgraph
 * public class Project {
 *     ...
 *     // reference to subgraph defined in Task class
 *     @NamedEntityGraphSubgraph(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @OneToMany List<Task> tasks;
 * }
 * }
 *
 * <p> When {@link NamedEntityGraphSubgraph} is used, there might be
 * multiple entity classes with {@code NamedEntityGraph} annotations
 * specifying identical values for the {@code name} member. In this
 * scenario, the root entity of the named graph is identified by it
 * being the unique entity class that is not referenced by the
 * {@code subgraph} member of any {@link NamedEntityGraphSubgraph}
 * annotation belonging to the named graph. If there is no such entity
 * class, or if it is not unique, the behavior is undefined. An
 * application must ensure that each named entity graph has a unique
 * root entity.
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
 * {@link AttributeNode} and {@link Subgraph}.
 *
 * <p> A reference to a named entity graph may be obtained by
 * calling {@link EntityManagerFactory#getNamedEntityGraphs(Class)}
 * or {@link EntityManager#getEntityGraph(String)} and may be passed
 * to {@link EntityManager#find(EntityGraph, Object, FindOption...)}.
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
 *             .get("EmployeeWithProjects")>
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
 * @see NamedEntityGraphAttributeNode
 * @see NamedEntityGraphSubgraph
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
     * (Optional) The name used to identify the entity graph in calls to
     * {@link EntityManager#getEntityGraph(String)}. If no name is explicitly
     * specified, the name defaults to the entity name of the annotated root
     * entity.
     * <p>Entity graph names must be unique within a given persistence unit.
     * If two {@link NamedEntityGraph @NamedEntityGraph} annotations declare
     * the same name, then one must be a subgraph of the other, as specified
     * via the {@link NamedEntityGraphSubgraph} annotations.
     */
    String name() default "";

    /**
     * (Optional) A list of attributes of the entity that are included in
     * this graph.
     *
     * @apiNote Alternatively, use {@link NamedEntityGraphAttributeNode}
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
     * @apiNote Alternatively, use {@link NamedEntityGraphSubgraph}
     *
     * @see NamedAttributeNode#subgraph
     */
    NamedSubgraph[] subgraphs() default {};

    /**
     * (Optional) A list of subgraphs that will add additional
     * attributes for subclasses of the annotated entity class to the
     * entity graph. Specified attributes from superclasses are
     * included in subclasses.
     *
     * @see EntityGraph#addTreatedSubgraph(Class)
     */
    NamedSubgraph[] subclassSubgraphs() default {};
}

