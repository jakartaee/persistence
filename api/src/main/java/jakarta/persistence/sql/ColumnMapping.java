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

import static java.util.Objects.requireNonNull;

/**
 * Maps a column of a JDBC {@link java.sql.ResultSet} to a scalar
 * value in the result returned by the query.
 *
 * @param columnName The name of the mapped column of the result set
 * @param type The Java type of the resulting scalar value
 * @param <T> The type of the resulting scalar value
 *
 * @see jakarta.persistence.ColumnResult
 *
 * @since 4.0
 */
public record ColumnMapping<T>(String columnName, Class<T> type, String alias)
        implements MappingElement<T>, ResultSetMapping<T> {

    public ColumnMapping {
        requireNonNull(columnName, "columnName is required");
        requireNonNull(type, "type is required");
    }

    /**
     * The Java type of the scalar value.
     */
    @Override
    public Class<T> getJavaType() {
        return type;
    }

    /**
     * The colum name.
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * Specify an alias for this column in the result set.
     * @param alias The alias
     */
    @Override
    public ColumnMapping<T> withAlias(String alias) {
        return new ColumnMapping<>(columnName, type, alias);
    }

    /**
     * Construct a new instance.
     * @param columnName The name of the mapped column of the result set
     */
    public static ColumnMapping<Object> of(String columnName) {
        return new ColumnMapping<>(columnName, Object.class, columnName);
    }

    /**
     * Construct a new instance.
     * @param columnName The name of the mapped column of the result set
     * @param type The Java type of the resulting scalar value
     * @param <T> The type of the resulting scalar value
     */
    public static <T> ColumnMapping<T> of(String columnName, Class<T> type) {
        return new ColumnMapping<>(columnName, type, columnName);
    }
}
