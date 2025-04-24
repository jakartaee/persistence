package jakarta.persistence;

/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

/**
 * Represents any query written in HQL or in SQL. May be used to
 * obtain an instance of {@link TypedQuery} or {@link ExecutableQuery}.
 * <p>
 * To execute a {@code SELECT} query, call {@link #forType(Class)}
 * to obtain a {@link TypedQuery}.
 * {@snippet :
 * entityManager.createQuery("select title from Book order by isbn")
 *         .forType(String.class)
 *         .setMaxResults(100)
 *         .getResultList();
 * }
 * To execute a {@code DELETE} or{@code UPDATE} query, call
 * {@link #forExecution()} to obtain an {@link ExecutableQuery}.
 * {@snippet :
 * entityManager.createQuery("delete from Book where extract(year from publicationDate)>?1")
 *         .forExecution()
 *         .setParameter(1, minDate)
 *         .executeUpdate();
 * }
 *
 * @apiNote For backward compatibility, this type extends {@link Query}.
 * Clients should avoid calling the deprecated methods of {@code Query}
 * on an instance of {@code QueryCreator}. Instead, {@link #forType}
 * or {@link #forExecution} should be called to obtain a {@link TypedQuery}
 * or {@link ExecutableQuery}.
 *
 * @since 4.0
 */
public interface QueryCreator extends Query {
    /**
     * Obtain a {@link TypedQuery} with the given query result type,
     * which must be a supertype of the result type of this query.
     * This query must be a SELECT query.
     * @param resultType The Java class of the query result type
     * @param <R> The query result type
     *
     * @since 4.0
     */
    <R> TypedQuery<R> forType(Class<R> resultType);

    /**
     * Obtain an {@link ExecutableQuery}.
     * This query must be an UPDATE or DELETE query.
     *
     * @since 4.0
     */
    ExecutableQuery forExecution();
}
