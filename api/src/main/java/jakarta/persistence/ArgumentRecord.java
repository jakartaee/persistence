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

import static java.util.Objects.requireNonNull;

/**
 * Internal implementation of {@link Parameter.Argument}.
 * @param <T> the type of the parameter
 * @param parameter the parameter
 * @param value the argument bound to the parameter
 * @since 4.0
 */
record ArgumentRecord<T>(@Nonnull Parameter<T> parameter, T value)
        implements Parameter.Argument<T> {

    ArgumentRecord {
        requireNonNull(parameter, "parameter must not be null");
    }

    @Nonnull
    @Override
    public Parameter<T> getParameter() {
        return parameter;
    }

    @Override
    public T getValue() {
        return value;
    }
}
