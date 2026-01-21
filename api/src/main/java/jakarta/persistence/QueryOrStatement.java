/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import jakarta.persistence.sql.ResultSetMapping;

/**
 * Declares operations allowing a {@link Statement} or {@link TypedQuery}
 * to be obtained when the type of a query or statement has not yet been
 * specified. This allows a slightly more fluent programming style where:
 * <ul>
 * <li>a Jakarta Persistence or native SQL query is provided to
 *     {@code createQuery()}, and
 * <li>the type of the query clarified in a subsequent method call.
 * </ul>
 *
 * <p>If an instance of this interface represents an {@code UPDATE}
 * or {@code DELETE} statement, then a {@code Statement} representing
 * the same statement may be obtained by calling {@link #asStatement()}.
 * {@snippet :
 * int updated =
 *         em.createQuery("delete from Temporary where timestamp > ?1")
 *                 .asStatement()
 *                 .setParameter(1, cutoffDateTime)
 *                 .execute();
 * }
 *
 * <p>If an instance of this interface represents a {@code SELECT}
 * query, then a {@code TypedQuery} representing the same query may
 * be obtained by calling {@link #ofType(Class)}, passing the result
 * type of the query.
 * {@snippet :
 * List<Book> books =
 *         em.createQuery("from Book where extract(year from publicationDate) > :year")
 *                 .ofType(Book.class)
 *                 .setParameter("year", Year.of(2000))
 *                 .setMaxResults(10)
 *                 .setCacheRetrieveMode(CacheRetrieveMode.BYPASS)
 *                 .getResultList();
 * }
 *
 * <p>These operations may be viewed as a sort of type cast to a
 * given subtype of this interface.
 *
 */
public interface QueryOrStatement extends Query {

    /**
     * Obtain a {@link Statement} representing this statement,
     * which must be some kind of executable statement, that is,
     * a Jakarta Persistence {@code UPDATE} or {@code DELETE}
     * statement, or any native SQL statement that returns a row
     * count. The executable statement may be executed by calling
     * {@link Statement#execute}.
     *
     * @throws IllegalStateException if this query is a
     *         Jakarta Persistence {@code SELECT} query
     * @since 4.0
     */
    Statement asStatement();

    /**
     * Obtain a {@link TypedQuery} with the given query result type,
     * which must be a supertype of the result type of this query.
     * This query must be a Jakarta Persistence {@code SELECT} query
     * or a native SQL query which returns a result set.
     *
     * @param resultType The Java class of the query result type
     * @param <R> The query result type
     * @throws IllegalArgumentException if the given result type is
     *         not a supertype of the result type of this query
     * @throws IllegalStateException if this query is a
     *         Jakarta Persistence {@code UPDATE} or {@code DELETE}
     *         statement
     * @since 4.0
     */
    <R> TypedQuery<R> ofType(Class<R> resultType);

    /**
     * Obtain a {@link TypedQuery} with the given entity graph,
     * which must be rooted at a supertype of the result type of
     * this query. This query must be a Jakarta Persistence
     * {@code SELECT} query which returns a single entity type.
     *
     * @param graph The entity graph, interpreted as a load graph
     * @param <R> The query result type
     * @throws IllegalArgumentException if the given graph type is
     *         not rooted at a supertype of the result type of this
     *         query
     * @throws IllegalStateException if this query is a
     *         Jakarta Persistence {@code UPDATE} or {@code DELETE}
     *         statement
     * @since 4.0
     */
    <R> TypedQuery<R> withEntityGraph(EntityGraph<R> graph);

    /**
     * Obtain a {@link TypedQuery} with the given result set mapping.
     * This query must be a native SQL query returning a result set
     * compatible with the given mapping.
     *
     * @param mapping The result set mapping
     * @param <R> The query result type
     * @throws IllegalArgumentException if the given graph type is
     *         not rooted at a supertype of the result type of this
     *         query
     * @throws IllegalStateException if this query is a
     *         Jakarta Persistence query or statement
     * @since 4.0
     */
    <R> TypedQuery<R> withResultSetMapping(ResultSetMapping<R> mapping);

}
