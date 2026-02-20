/*
 * Copyright (c) 2025, 2026 Contributors to the Eclipse Foundation
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
//     Christian Beikov - 4.0


package jakarta.persistence.metamodel;

/**
 * Instances of the type {@linkplain NumericAttribute} represents persistent
 * single-valued properties or fields of number type.
 *
 * @param <X> The type containing the represented attribute
 * @param <N> The type of the represented attribute
 *
 * @since 4.0
 */
public interface NumericAttribute<X, N extends Number & Comparable<N>>
		extends ComparableAttribute<X, N> {
}
