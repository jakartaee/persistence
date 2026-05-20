/*
 * Copyright (c) 2020, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

/**
 * The Jakarta Persistence API, the standard for management
 * of persistence and object/relational mapping in Java(R)
 * environments.
 * <ul>
 * <li>{@link jakarta.persistence} defines the core APIs,
 *     including {@link jakarta.persistence.EntityManager},
 *     {@link jakarta.persistence.EntityAgent},
 *     {@link jakarta.persistence.EntityManagerFactory},
 *     {@link jakarta.persistence.Persistence}, and
 *     {@link jakarta.persistence.PersistenceConfiguration}.
 *     It also provides annotations for declaring entity
 *     types, their relationships, and their persistent
 *     attributes, and for mapping these program elements
 *     to the database.
 * <li>{@link jakarta.persistence.criteria} defines the
 *     {@linkplain jakarta.persistence.criteria.CriteriaBuilder
 *     criteria query} API.
 * <li>{@link jakarta.persistence.sql} defines an API for
 *     {@linkplain jakarta.persistence.sql.ResultSetMapping
 *     mapping SQL result sets} to Java objects.
 * <li>{@link jakarta.persistence.query} defines an API for
 *     expressing
 *     {@linkplain jakarta.persistence.query.JakartaQuery
 *     statically type safe queries} by annotating a query
 *     method.
 * <li>{@link jakarta.persistence.metamodel} defines an API
 *     representing the
 *     {@linkplain jakarta.persistence.metamodel.ManagedType
 *     managed classes} of a persistence unit and allowing
 *     programatic reflection on this metamodel.
 * <li>{@link jakarta.persistence.spi} defines an SPI
 *     allowing integration of a
 *     {@linkplain jakarta.persistence.spi.PersistenceProvider
 *     persistence provider} with the Jakarta environment.
 * </ul>
 * <p>
 * This module provides standard interfaces. An implementation
 * of Jakarta Persistence is called a <em>persistence provider</em>.
 * <p>
 * Jakarta Persistence fully supports usage within Java SE or
 * Jakarta EE.
 */
module jakarta.persistence {

    requires transitive java.sql;
    requires static jakarta.annotation;

    exports jakarta.persistence;
    exports jakarta.persistence.criteria;
    exports jakarta.persistence.metamodel;
    exports jakarta.persistence.sql;
    exports jakarta.persistence.query;
    exports jakarta.persistence.spi;

    uses jakarta.persistence.spi.PersistenceProvider;
}
