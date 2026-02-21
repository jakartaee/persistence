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
//     Gavin King      - 4.0

package jakarta.persistence.sql;

/**
 * Supertype of objects which map a column or columns of
 * a JDBC {@link java.sql.ResultSet} to a member of an
 * entity or embeddable type. A {@link EntityMapping} or
 * {@link EmbeddedMapping} packages a list of
 * {@code MemberMapping}s.
 *
 * @param <T> The entity or embeddable type
 *
 * @since 4.0
 */
public sealed interface MemberMapping<T>
        permits FieldMapping, EmbeddedMapping, FetchMapping {
}
