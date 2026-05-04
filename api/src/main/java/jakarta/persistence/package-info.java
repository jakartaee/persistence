/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.0

/**
 * Defines the core APIs for the management for persistence and
 * object/relational mapping.
 * <ul>
 * <li>{@link jakarta.persistence.EntityManager} is the central
 *     interface used to interact with a stateful persistence
 *     context. An instance of {@code EntityManager} must never
 *     be shared across independent units of work nor between
 *     concurrently executing threads.
 * <li>{@link jakarta.persistence.EntityAgent} provides an
 *     alternative more direct approach to interaction with the
 *     database, with no intermediating persistence context. An
 *     instance of {@code EntityAgent} must never be shared
 *     between concurrently executing threads.
 * <li>{@link jakarta.persistence.TypedQuery},
 *     {@link jakarta.persistence.Statement}, and
 *     {@link jakarta.persistence.StoredProcedureQuery} allow
 *     control over the execution of queries, statememts, and
 *     stored procedures.
 * <li>{@link jakarta.persistence.EntityManagerFactory} acts
 *     as a factory for instances of {@code EntityManager} and
 *     {@code EntityAgent} and provides access to other APIs
 *     including {@link jakarta.persistence.Cache},
 *     {@link jakarta.persistence.SchemaManager},
 *     {@link jakarta.persistence.criteria.CriteriaBuilder},
 *     {@link jakarta.persistence.metamodel.Metamodel}, and
 *     {@link jakarta.persistence.PersistenceUnitUtil}. There
 *     is usally one instance of {@code EntityManager} for each
 *     configured persistence unit.
 * <li>{@link jakarta.persistence.Persistence} is an entry point
 *     for construction of an {@code EntityManagerFactory} in
 *     Java SE and defines standard configuration properties.
 * <li>As an alternative to the use of {@code persistence.xml},
 *     {@link jakarta.persistence.PersistenceConfiguration}
 *     allows a persistence unit to be configured programatically.
 * <li>{@link jakarta.persistence.SchemaManager} provides
 *     operations for direct programmatic management of the
 *     database schema.
 * <li>{@link jakarta.persistence.Cache} provides operations for
 *     direct management of the second-level cache.
 * <li>{@link jakarta.persistence.EntityGraph} provides a way to
 *     specify the boundaries of an operation on entity types.
 * </ul>
 * <p>
 * The following example demonstrates one way to quickly
 * configure and start Jakarta Persistence in Java SE:
 * {@snippet :
 * // configure the persistence unit
 * var library = new PersistenceConfiguration("Library");
 * library.nonJtaDataSource("java:global/jdbc/LibraryDatabase");
 * library.defaultToOneFetchType(FetchType.LAZY);
 * // register the entity classes
 * List.of(Book.class, Author.class, Publisher.class)
 * 	       .forEach(library::managedClass);
 * // create the entity manager factory
 * try (var factory = config.createEntityManagerFactory()) {
 * 	   // export the schema and test data
 *     factory.getSchemaManager().create(true);
 *     // start a transaction and obtain an entity agent
 *     factory.runInTransaction(EntityAgent.class, agent -> {
 *         // obtain an entity instance by providing its primary key
 *         var book = agent.get(Book.class, isbn);
 *         ...
 *     });
 * }
 * }
 * Note, however, that {@code createEntityManagerFactory} is an
 * expensive operation which should be called just once for each
 * persistence unit in the application program.
 * <p>
 * Furthermore, this package provides a suite of annotation
 * types which may be used:
 * <ul>
 * <li>to specify a model of persistent entity types, their
 *     relationships, and their attributes,
 * <li>map the entity types to database tables,
 * <li>declare
 *     {@linkplain jakarta.persistence.NamedQuery named queries},
 *     {@linkplain jakarta.persistence.NamedNativeQuery named
 *     native SQL queries}, and
 *     {@linkplain jakarta.persistence.NamedStoredProcedureQuery
 *     named stored procedures},
 * <li>declare {@linkplain jakarta.persistence.NamedEntityGraph
 *     named entity graphs} and
 *     {@linkplain jakarta.persistence.SqlResultSetMapping
 *     named SQL result set mappings}, and
 * <li>inject an {@linkplain jakarta.persistence.PersistenceContext
 *     entity manager} or
 *     {@linkplain jakarta.persistence.PersistenceUnit factory}
 *     in a Jakarta EE environment.
 * </ul>
 */
package jakarta.persistence;
