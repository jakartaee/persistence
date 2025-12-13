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
public record ColumnMapping<T>(String columnName, Class<T> type)
        implements MappingElement<T>, ResultSetMapping<T> {

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
        return columnName;
    }

    /**
     * Construct a new instance.
     * @param columnName The name of the mapped column of the result set
     */
    public static ColumnMapping<Object> of(String columnName) {
        return new ColumnMapping<>(columnName, Object.class);
    }

    /**
     * Construct a new instance.
     * @param columnName The name of the mapped column of the result set
     * @param type The Java type of the resulting scalar value
     * @param <T> The type of the resulting scalar value
     */
    public static <T> ColumnMapping<T> of(String columnName, Class<T> type) {
        return new ColumnMapping<>(columnName, type);
    }
}
