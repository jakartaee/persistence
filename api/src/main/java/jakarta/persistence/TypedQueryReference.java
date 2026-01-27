/*
 * Copyright (c) 2023, 2025 Contributors to the Eclipse Foundation
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
//     Gavin King      - 3.2

package jakarta.persistence;

/**
 * A reference to a typed named query declared via the
 * {@link NamedQuery} or {@link NamedNativeQuery} annotations,
 * or using {@link jakarta.persistence.query.StaticQuery} or
 * {@link jakarta.persistence.query.StaticNativeQuery}. An
 * instance of {@code TypedQueryReference} is usually obtained
 * from the static metamodel of the annotated type.
 *
 * <p>In this example, a method is annotated, and the name of
 * the query is determined by the name of the annotated method.
 * The annotated query is executed with the given argument to
 * the query parameter named {@code pattern} and with the
 * {@link CacheStoreMode#BYPASS} option:
 * {@snippet :
 * class Library {
 *     @Inject EntityManager entityManager;
 *
 *     @StaticQuery("select a from Book b join b.authors a where b.title like :pattern")
 *     @QueryOptions(cacheStoreMode = CacheStoreMode.BYPASS)
 *     List<Author> findAuthorsGivenTitles(String pattern) {
 *         return entityManager.createQuery(Library_.findAuthorsGivenTitles(pattern))
 *                 .getResultList();
 *     }
 * }
 * }
 *
 * <p>In this example, it is the entity class that is annotated,
 * and the {@link NamedQuery} annotation explicitly specifies a
 * name:
 * {@snippet :
 * @NamedQuery(name = "byTitle",
 *             query = "from Book where title like ?1")
 * @Entity class Book { .. }
 * }
 * <p>In this case the {@code TypedQueryReference} obtained from
 * its static metamodel does not include arguments to the query
 * parameters, and so they must be supplied via {@code setParameter}:
 * {@snippet :
 * var books =
 *         entityManager.createQuery(Book_._byTitle_)
 *                 .setCacheStoreMode(CacheStoreMode.BYPASS)
 *                 .setParameter(1, pattern)
 *                 .getResultList();
 * }
 *
 * <p>A {@code TypedQueryReference} may include arguments to
 * parameters of the query.
 * <ul>
 * <li>A reference representing a query declared using an
 *     annotation of a type or field has no arguments, and so
 *     {@link #getArguments()}, {@link #getParameterNames()},
 *     and {@link #getParameterTypes()} all return {@code null}.
 * <li>A reference representing a query declared using an
 *     annotation of a method holds information about the
 *     arguments passed to the method, making it available via
 *     {@link #getArguments()}, {@link #getParameterNames()},
 *     and {@link #getParameterTypes()}.
 * </ul>
 *
 * <p>In the Jakarta Persistence query language, only SELECT
 * queries are typed queries, since only a SELECT query can
 * return a result. A DELETE or UPDATE statement is not a
 * typed query and is always represented by an untyped
 * instance of {@link StatementReference}. On the other hand, a
 * native SQL query is considered a typed query if it returns
 * a result set.
 *
 * @param <R> an upper bound on the result type of the query
 *
 * @see EntityHandler#createQuery(TypedQueryReference)
 *
 * @since 3.2
 */
public non-sealed interface TypedQueryReference<R> extends Reference {

    /**
     * The result type of the query, as specified by
     * {@link NamedQuery#resultClass} or
     * {@link NamedNativeQuery#resultClass}, or as inferred
     * from the declared return type of the method annotated
     * {@link jakarta.persistence.query.StaticQuery} or
     * {@link jakarta.persistence.query.StaticNativeQuery}.
     */
    Class<? extends R> getResultType();

    /**
     * The specified {@link CacheRetrieveMode}, if any,
     * or {@code null} when the default mode of the
     * {@link EntityHandler} should be used.
     *
     * @see TypedQuery#setCacheRetrieveMode
     * @see jakarta.persistence.query.QueryOptions#cacheRetrieveMode
     * @since 4.0
     */
    CacheRetrieveMode getCacheRetrieveMode();

    /**
     * The specified {@link CacheStoreMode}, if any,
     * or {@code null} when the default mode of the
     * {@link EntityHandler} should be used.
     *
     * @see TypedQuery#setCacheStoreMode
     * @see jakarta.persistence.query.QueryOptions#cacheStoreMode
     * @since 4.0
     */
    CacheStoreMode getCacheStoreMode();

    /**
     * The specified {@link LockModeType}, if any,
     * oo {@link LockModeType#NONE} if no lock mode
     * was specified.
     *
     * @see TypedQuery#setLockMode
     * @see NamedQuery#lockMode
     * @see jakarta.persistence.query.QueryOptions#lockMode
     * @since 4.0
     */
    LockModeType getLockMode();

    /**
     * The specified {@link PessimisticLockScope},
     * if any, or {@link PessimisticLockScope#NORMAL}
     * if no lock scope was specified.
     *
     * @see TypedQuery#setLockScope
     * @see NamedQuery#lockScope
     * @see jakarta.persistence.query.QueryOptions#lockScope
     * @since 4.0
     */
    PessimisticLockScope getPessimisticLockScope();

    /**
     * The specified entity graph name, if any,
     * or {@code null} if no entity graph was
     * specified.
     *
     * @see TypedQuery#setEntityGraph
     * @see NamedQuery#entityGraph
     * @see jakarta.persistence.query.QueryOptions#entityGraph
     * @since 4.0
     */
    String getEntityGraphName();
}
