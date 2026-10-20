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

import static java.util.Objects.requireNonNull;

/**
 * Internal implementation of {@link TupleElement}.
 * Note that the return type of {@link #getJavaType()}
 * is intentionally declared invariant in {@code X},
 * to ensure type safety of the {@link #bind} method.
 * @param type the Java type of the tuple element
 * @param alias the alias of the tuple element, or
 *              null if the tuple element has no alias
 * @param <X> the type of the tuple element
 * @since 4.0
 */
record TupleElementRecord<X>
        (@Nonnull Class<X> type, @Nullable String alias)
        implements TupleElement<X> {

    @Override
    @Nonnull
    public Class<X> getJavaType() {
        return type;
    }

    @Override
    @Nullable
    public String getAlias() {
        return alias;
    }

    @Nonnull
    Tuple.Entry<X> bind(X argument) {
        return new Entry<>(this, argument);
    }

    record Entry<T>(@Nonnull TupleElement<T> element, T value)
            implements Tuple.Entry<T> {
        public Entry {
            requireNonNull(element, "element must not be null");
        }

        @Nonnull
        @Override
        public TupleElement<T> getElement() {
            return element;
        }

        @Override
        public T getValue() {
            return value;
        }
    }
}
