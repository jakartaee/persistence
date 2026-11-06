/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */
package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Internal implementation of {@link Parameter}.
 * @param type the Java type of the parameter
 * @param position the position of the parameter, or null if the
 *                 parameter is not a positional parameter
 * @param name the name of the parameter, or null if the parameter
 *             is not a named parameter
 * @param <T> the type of the parameter
 * @see Parameter#of(Class, Integer, String)
 * @since 4.0
 */
record ParameterRecord<T>
        (@Nonnull Class<T> type, @Nullable Integer position, @Nullable String name)
        implements Parameter<T> {
    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Integer getPosition() {
        return position;
    }

    @Nonnull
    @Override
    public Class<T> getParameterType() {
        return type;
    }
}
