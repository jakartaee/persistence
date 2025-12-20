/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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

import java.util.List;
import java.util.Map;

/**
 * A reference to an executable named query declared via the
 * {@link NamedQuery} or {@link NamedNativeQuery} annotations,
 * or using {@link jakarta.persistence.query.StaticQuery} or
 * {@link jakarta.persistence.query.StaticNativeQuery}. An
 * instance of {@code StatementReference} is usually obtained
 * from the static metamodel of the annotated type.
 *
 * <p>In this example, a method is annotated, and the name of
 * the query is determined by the name of the annotated method.
 * The annotated query is executed with the given argument to
 * the query parameter named {@code pattern} and with the
 * {@link CacheStoreMode#BYPASS} option:
 * {@snippet :
 * class Filer {
 *     @Inject EntityManager entityManager;
 *
 *     @StaticQuery("delete from Record where temporary = true")
 *     int purgeTemporaryRecords() {
 *         return entityManager.createStatement(Filer_.purgeTemporaryRecords())
 *                 .execute();
 *     }
 * }
 * }
 *
 * <p>In this example, it is the entity class which is annotated,
 * and the {@link NamedQuery} annotation explicitly specifies a
 * name:
 * {@snippet :
 * @NamedQuery(name = "updateSales",
 *             query = "update Book set sales = ?1 where isbn = ?2")
 * @Entity class Book { .. }
 * }
 * <p>In this case the {@code TypedStatementReference} obtained from
 * its static metamodel does not include arguments to the query
 * parameters, and so they must be supplied via {@code setParameter}:
 * {@snippet :
 * int updated =
 *         entityManager.createStatement(Book_._updateSales_)
 *                 .setParameter(1, sales)
 *                 .setParameter(2, isbn)
 *                 .execute();
 * }
 *
 * <p>A {@code StatementReference} may include arguments to parameters
 * of the query.
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
 * <p>In the Jakarta Persistence query language, a DELETE or
 * UPDATE statement is not a typed query, and is always
 * represented by an instance of {@link StatementReference}.
 * A native SQL query is represented by an instance of
 * {@link StatementReference} if it returns a row count. A
 * Jakarta Persistence SELECT query, or a native SQL query
 * that returns a result set, is usually represented as an
 * instance of {@link TypedQueryReference}.
 *
 * @see EntityHandler#createStatement(StatementReference)
 *
 * @since 4.0
 */
public interface StatementReference {
    /**
     * The name of the query.
     */
    String getName();

    /**
     * A map keyed by hint name of all hints specified via
     * {@link NamedQuery#hints} or {@link NamedNativeQuery#hints}.
     */
    Map<String,Object> getHints();

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