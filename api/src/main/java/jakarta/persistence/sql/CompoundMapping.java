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

package jakarta.persistence.sql;

/**
 * Maps a JDBC {@link java.sql.ResultSet} to a tuple of values
 * packaged as an object array.
 *
 * @param elements Mappings for the elements of the tuple
 *
 * @see TupleMapping
 * @see jakarta.persistence.SqlResultSetMapping
 *
 * @since 4.0
 */
public record CompoundMapping(MappingElement<?>[] elements)
        implements ResultSetMapping<Object[]> {

    /**
     * Construct a new instance.
     * @param elements Mappings for the elements of the tuple
     */
    public static CompoundMapping of(MappingElement<?>... elements) {
        return new CompoundMapping(elements);
    }

    /**
     * Always returns {@code Object[].class}.
     */
    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }
}

