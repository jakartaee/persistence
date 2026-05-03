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
 * Defines the Jakarta Persistence Criteria Query API.
 * Criteria queries allow programmatic construction or
 * manipulation of Jakarta Persistence queries.
 * <ul>
 * <li>{@link jakarta.persistence.criteria.CriteriaBuilder}
 *     is the entry point for constructing queries,
 *     expressions, predicates, projections, and ordering
 *     criteria.
 * <li>{@link jakarta.persistence.criteria.CriteriaQuery}
 *     represents a top-level query slection query.
 * <li>{@link jakarta.persistence.criteria.CriteriaUpdate}
 *     and {@link jakarta.persistence.criteria.CriteriaDelete}
 *     represents update and delete statements, respectively.
 * <li>{@link jakarta.persistence.criteria.Expression}
 *     and its subtypes represent expressions of various
 *     types.
 * <li>{@link jakarta.persistence.criteria.Selection}
 *     represents a projected value in a query result list.
 * <li>{@link jakarta.persistence.criteria.Order} represents
 *     an ordering criterion.
 * </ul>
 * <p>
 * This example demonstrates the use of the Criteria API:
 * {@snippet :
 * var builder = factory.getCriteriaBuilder();
 * var query = builder.createQuery(Book.class);
 * // specify the root entity of the query
 * var book = query.from(Book.class);
 * // fetch an association belonging to the root entity
 * book.fetch(Book_.authors, JoinType.LEFT);
 * if (titlePattern != null) {
 *     // add a restriction to the query
 *     query.where(book.get(Book_.title).like("%Persistence%"));
 * }
 * var books = agent.createQuery(query).getResultList();
 * }
 * <p>
 * The example demonstrates the use of the
 * {@linkplain jakarta.persistence.metamodel.StaticMetamodel
 * static metamodel} type {@code Book_} corresponding to the
 * entity type {@code Book} to obtain references to persistent
 * members of {@code Book}. Use of the static metamodel is
 * usually preferred to passing string-valued attribute names
 * during query construction.
 */
package jakarta.persistence.criteria;
