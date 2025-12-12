/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

import java.util.List;
import java.util.Map;

/**
 * A reference to a named query declared via the
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
 *     @ReadQueryOptions(cacheStoreMode = CacheStoreMode.BYPASS)
 *     List<Author> findAuthorsGivenTitles(String pattern) {
 *         return entityManager.createQuery(Library_.findAuthorsGivenTitles(pattern))
 *                 .getResultList();
 *     }
 * }
 * }
 *
 * <p>In this example, it is the entity class which is annotated,
 * and the {@link NamedQuery} annotation explicitly specifies a
 * name:
 * {@snippet :
 * @NamedQuery(name = "byTitle",
 *             query = "Book b where title like ?1")
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
 * @param <R> an upper bound on the result type of the query
 *
 * @see EntityHandler#createQuery(TypedQueryReference)
 *
 * @since 3.2
 */
public interface TypedQueryReference<R> {
    /**
     * The name of the query.
     * Unique within a given persistence unit.
     */
    String getName();

    /**
     * The result type of the query.
     */
    Class<? extends R> getResultType();

    /**
     * A map keyed by hint name of all hints specified via
     * {@link NamedQuery#hints} or {@link NamedNativeQuery#hints}.
     * <p>
     * Any mutation of the returned map results in an
     * {@link UnsupportedOperationException}.
     *
     * @see Query#setHint
     */
    Map<String,Object> getHints();

    /**
     * Any {@linkplain FindOption options} controlling
     * execution of the query.
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @since 4.0
     */
    List<FindOption> getOptions();

    /**
     * The types of the supplied
     * {@linkplain #getArguments arguments} to query
     * parameters, or {@code null} if no arguments were
     * supplied. Arguments are present when this is a
     * reference to a query declared using an annotation
     * of a method.
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @since 4.0
     */
    List<Class<?>> getParameterTypes();

    /**
     * The names assigned to the supplied
     * {@linkplain #getArguments arguments} to query
     * parameters, or {@code null} if no arguments were
     * supplied. Arguments are present when this is a
     * reference to a query declared using an annotation
     * of a method. If the query has named parameters,
     * these are interpreted as the parameter names.
     * Otherwise, if the query has positional parameters,
     * they are ignored.
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @since 4.0
     */
    List<String> getParameterNames();

    /**
     * The arguments supplied to the query parameters,
     * or {@code null} if no arguments were supplied.
     * Arguments are present when this is a reference to
     * a query declared using an annotation of a method.
     * <ul>
     * <li>If the query has ordinal parameters, the
     * position of an argument in this array determines
     * its assignment to a parameter.
     * <li>If the query has named parameters, this array
     * is aligned with the {@linkplain #getParameterNames
     * array of parameter names} to obtain an assignment
     * of arguments to parameters.
     * </ul>
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @see Query#setParameter(int, Object)
     * @see Query#setParameter(String, Object)
     * @since 4.0
     */
    List<Object> getArguments();
}
