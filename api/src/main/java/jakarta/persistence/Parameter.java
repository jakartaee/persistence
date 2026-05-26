/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents a parameter of a {@linkplain Query query}.
 * <p>
 * A portable application should not attempt to reuse a {@code Parameter}
 * object obtained from one instance of {@code Query} by passing it to a
 * method of a different instance of {@code Query}.
 *
 * @param <T> the type of the parameter
 *
 * @see Query
 * @see TypedQuery
 *
 * @since 2.0
 */
public interface Parameter<T> {

    /**
     * Return the parameter name, or null if the parameter is
     * not a named parameter or if no name has been assigned.
     * @return parameter name
     */
    @Nullable
    String getName();

    /**
     * Return the parameter position, or null if the parameter
     * is not a positional parameter. 
     * @return position of parameter
     */
    @Nullable
    Integer getPosition();

    /**
     * Return the Java type of the parameter, if known to the
     * persistence provider. Values bound to the parameter must
     * be assignable to this type.
     * <ul>
     * <li>If this object is a parameter of a criteria query,
     *     the persistence provider always knows its type.
     * <li>Otherwise, if this object represents a parameter of
     *     a query written in the Jakarta Persistence query
     *     language or in native SQL, the persistence provider
     *     is permitted to throw an {@link IllegalStateException}
     *     if it is unable to infer the type of the parameter.
     * </ul>
     * @return the Java type of the parameter
     * @throws IllegalStateException if the type of the parameter
     *         is not known and cannot be inferred
     */
    @Nonnull
    Class<T> getParameterType();
}

