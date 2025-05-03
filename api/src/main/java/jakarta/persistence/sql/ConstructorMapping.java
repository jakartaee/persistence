/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
 * Maps columns of a JDBC {@link java.sql.ResultSet} to parameters
 * of the constructor of a Java class.
 *
 * @param targetClass The Java class which declares the constructor
 * @param columns The names of the mapped columns of the result set
 * @param <T> The type of the Java class
 *
 * @see jakarta.persistence.ConstructorResult
 *
 * @since 4.0
 */

public record ConstructorMapping<T>(Class<T> targetClass, ColumnMapping<?>[] columns)
        implements MappingElement<T>, ResultSetMapping<T> {

    public static <T> ConstructorMapping<T> of(Class<T> targetClass, ColumnMapping<?>... columns) {
        return new ConstructorMapping<>(targetClass, columns);
    }

    @Override
    public Class<T> type() {
        return targetClass;
    }
}
