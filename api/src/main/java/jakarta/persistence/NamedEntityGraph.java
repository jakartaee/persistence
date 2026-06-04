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
 * <p>
 * The {@linkplain jakarta.persistence.AttributeNode nodes} and
 * {@linkplain jakarta.persistence.Subgraph subgraphs} of an entity
 * graph control the limits of the graph of associated attributes and
 * entities fetched when an operation which retrieves an instance or
 * instances of the root entity is executed.
 * <p>
 * A well-defined entity graph is a tree. The application program
 * is responsible for ensuring that every named entity graph is acyclic.
 * If a named entity graph contains a cycle, the behavior is undefined.
 * A provider may reject the graph at runtime; it may reject it when
 * the persistence unit is initialized; or it may transform the graph
 * into an acyclic graph according to some vendor-specific algorithm.
 * Portable applications must avoid relying on such behavior.
 * <p>
 * There are two ways to specify the nodes and subgraphs of a named
 * entity graph defined in annotations:
 * <ul>
 * <li>by nesting {@link NamedAttributeNode} and {@link NamedSubgraph}
 *     annotations within the {@code NamedEntityGraph} annotation,
 * <li>by annotating persistent attributes with {@link Fetch}, or
 * <li>by mixing these two approaches.
 * </ul>
 * <p>
 * The two approaches may be mixed within the same entity graph
 * definition.
 * <p>
 * When {@link Fetch @Fetch} is used, the definition of the named entity
 * graph is distributed across the entity class and its associated
 * classes, with the help of {@code @Fetch} annotations.
 * {@snippet :
 * @NamedEntityGraph(name = "EmployeeWithProjectTasksAndEmployer")
 * @Entity // root entity of graph
 * public class Employee {
 *     ...
 *     // fetched attribute node
 *     @Fetch(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToOne(fetch=LAZY) Employer employer;
 *
 *     // reference to subgraph defined in Project class
 *     @Fetch(graph = "EmployeeWithProjectTasksAndEmployer",
 *            subgraph = "EmployeeWithProjectTasksAndEmployer")
 *     @ManyToMany Set<Project> projects;
 * }
 *
 * @NamedEntityGraph(name = "EmployeeWithProjectTasksAndEmployer")
 * @Entity // root entity of subgraph
 * public class Project {
 *     ...
 *     // reference to subgraph defined in Task class
 *     @Fetch(graph = "EmployeeWithProjectTasksAndEmployer")
 *     @OneToMany List<Task> tasks;
 * }
 * }
 * <p>
 * When a {@link Fetch#subgraph subgraph} is specified by the
 * {@code @Fetch} annotation, there might be multiple entity classes with
 * {@code @NamedEntityGraph} annotations specifying identical values for
 * the {@link #name name} member. In this scenario, the root entity of
 * the named graph is identified by it being the unique entity class that
 * is not referenced by the {@code subgraph} member of any {@code @Fetch}
 * annotation belonging to the named graph. If there is no such entity
 * class, or if it is not unique, the behavior is undefined. An
 * application must ensure that each named entity graph has a unique root
 * entity.
 * <p>
 * When nested annotations are used, the definition of the named entity
 * graph is contained entirely within the {@code NamedEntityGraph}
 * annotation.
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
 * <p>
 * A named entity graph defined using annotations is reified at
 * runtime as an instance of {@link EntityGraph}, and its child
 * nodes and subgraphs are reified respectively as instances of
 * {@link jakarta.persistence.AttributeNode} and
 * {@link jakarta.persistence.Subgraph}.
 * <p>
 * The simplest and most type safe way to obtain a reference to
 * a named entity graph is via the static metamodel for the annotated
 * class.
 * {@snippet :
 * Employee employee =
 *         em.get(Employee_._EmployeeWithProjects,
 *                employeeId);
 * }
 * <p>
 * Alternatively, a reference to a named graph may be obtained by
 * calling {@link EntityManagerFactory#getNamedEntityGraphs(Class)},
 * {@link jakarta.persistence.metamodel.EntityType#getNamedEntityGraphs},
 * or {@link EntityHandler#getEntityGraph(Class, String)}.
 * {@snippet :
 * EntityGraph<Employee> graph =
 *         emf.getNamedEntityGraphs(Employee.class)
 *            .get(Employee_.GRAPH_EMPLOYEE_WITH_PROJECTS)
 * Employee employee = em.find(graph, employeeId);
 * }
 *
 * @see NamedAttributeNode
 * @see NamedSubgraph
 * @see EntityGraph
 * @see EntityHandler#find(EntityGraph, Object, FindOption...)
 * @see EntityHandler#get(EntityGraph, Object, FindOption...)
 * @see EntityHandler#createQuery(String, EntityGraph)
 * @see StatementOrTypedQuery#withEntityGraph(EntityGraph)
 * @see EntityManagerFactory#getNamedEntityGraphs(Class)
 * @see jakarta.persistence.metamodel.EntityType#getNamedEntityGraphs()
 * @see EntityHandler#getEntityGraph(Class, String) 
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
     * <p>
     * Entity graph names must be unique within a given persistence unit.
     * If two {@link NamedEntityGraph @NamedEntityGraph} annotations declare
     * the same name, then one must be a subgraph of the other, as specified
     * by {@link Fetch} annotations belonging to the named graph.
     */
    String name() default "";

    /**
     * (Optional) A list of attributes of the root entity that are included
     * as attribute nodes in this entity graph.
     * <p>
     * Each attribute is declared via a
     * {@link NamedAttributeNode @NamedAttributeNode} annotation.
     * An attribute node may specify the name of a
     * {@linkplain NamedAttributeNode#subgraph subgraph} or
     * {@linkplain NamedAttributeNode#keySubgraph key subgraph} rooted at
     * the node.
     *
     * @apiNote Alternatively, use {@link Fetch}.
     */
    NamedAttributeNode[] attributeNodes() default {};

    /**
     * (Optional) Includes every attribute of the annotated entity class
     * as an attribute node in the entity graph.
     *
     * @apiNote Even when every attribute is included in the graph, the
     * {@link #attributeNodes attributeNodes} element may still be used
     * to specify a subgraph rooted at an attribute.
     */
    boolean includeAllAttributes() default false;

    /**
     * (Optional) A list of named subgraphs of this named entity graph.
     * <p>
     * Each subgraph is declared via a
     * {@link NamedSubgraph @NamedSubgraph} annotation. A subgraph must
     * be referenced by name from the
     * {@link NamedAttributeNode#subgraph subgraph} or
     * {@link NamedAttributeNode#keySubgraph keySubgraph} element of a
     * {@link NamedAttributeNode @NamedAttributeNode} annotation of the
     * graph. A subgraph may declare its own attribute nodes via nested
     * {@code @NamedAttributeNode} annotations.
     *
     * @apiNote Alternatively, use {@link Fetch} to attach another
     *          {@link NamedEntityGraph} as a subgraph.
     *
     * @see NamedAttributeNode#subgraph
     * @see NamedAttributeNode#keySubgraph
     */
    NamedSubgraph[] subgraphs() default {};

    /**
     * (Optional) A list of subgraphs representing subclasses of the
     * annotated root entity of the graph.
     * <p>
     * Each {@link NamedSubgraph @NamedSubgraph} must specify a
     * {@linkplain NamedSubgraph#type type} which is a subclass of the
     * annotated root entity, and may declare additional attribute nodes
     * representing attributes belonging to that subclass.
     *
     * @see EntityGraph#addTreatedSubgraph(Class)
     */
    NamedSubgraph[] subclassSubgraphs() default {};
}
