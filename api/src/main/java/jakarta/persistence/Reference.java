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

import jakarta.persistence.query.TypedQueryOptions;

import java.util.List;
import java.util.Map;

/**
 * A reference to a named statement or query declared via the
 * {@link NamedQuery} or {@link NamedNativeQuery} annotations,
 * or using {@link jakarta.persistence.query.StaticQuery} or
 * {@link jakarta.persistence.query.StaticNativeQuery}. An
 * instance of {@code Reference} is usually obtained from the
 * static metamodel. This interface declares operations common
 * to {@link StatementReference} and {@link TypedQueryReference}.
 *
 * @since 4.0
 */
public sealed interface Reference
        permits StatementReference, TypedQueryReference {

    /**
     * The name of the statement or query, as specified by
     * {@link NamedQuery#name} or {@link NamedNativeQuery#name},
     * or as inferred from the name of the method annotated
     * {@link jakarta.persistence.query.StaticQuery} or
     * {@link jakarta.persistence.query.StaticNativeQuery}.
     */
    String getName();

    /**
     * A map keyed by hint name of all hints specified via
     * {@link NamedQuery#hints}, {@link NamedNativeQuery#hints},
     * {@link TypedQueryOptions#hints}, or
     * {@link jakarta.persistence.query.StatementOptions#hints}.
     * <p>
     * Any attempted mutation of the returned map results in an
     * {@link UnsupportedOperationException}.
     */
    Map<String,Object> getHints();

    /**
     * The types of the supplied
     * {@linkplain #getArguments arguments} to parameters of the
     * statement or query, or {@code null} if no arguments were
     * supplied. Arguments are present when this is a reference
     * to a statement or query declared using an annotation of a
     * method.
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @since 4.0
     */
    List<Class<?>> getParameterTypes();

    /**
     * The names assigned to the supplied
     * {@linkplain #getArguments arguments} to parameters of the
     * statement or query, or {@code null} if no arguments were
     * supplied. Arguments are present when this is a reference
     * to a statement or query declared using an annotation of a
     * method. If the query has named parameters, these are
     * interpreted as the parameter names. Otherwise, if the
     * query has positional parameters, they are ignored.
     * <p>
     * Any mutation of the returned list results in an
     * {@link UnsupportedOperationException}.
     *
     * @since 4.0
     */
    List<String> getParameterNames();

    /**
     * The arguments supplied to the parameters of the statement
     * or query, or {@code null} if no arguments were supplied.
     * Arguments are present when this is a reference to a query
     * declared using an annotation of a method.
     * <ul>
     * <li>If the query has ordinal parameters, the position of
     *     an argument in this array determines its assignment
     *     to a parameter.
     * <li>If the query has named parameters, this array is
     *     aligned elementwise with the array of
     *     {@linkplain #getParameterNames parameter names} to
     *     obtain an assignment of arguments to parameters.
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
    
    /**
     * The specified {@link Timeout}, if any, or
     * {@code null} if no timeout was specified.
     *
     * @see Query#setTimeout(Timeout) 
     * @see TypedQueryOptions#timeout
     * @see jakarta.persistence.query.StatementOptions#timeout
     * @since 4.0
     */
    Timeout getTimeout();
}
