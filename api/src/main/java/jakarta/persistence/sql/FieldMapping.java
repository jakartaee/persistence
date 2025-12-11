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

import jakarta.persistence.metamodel.SingularAttribute;

/**
 * Maps a column of a JDBC {@link java.sql.ResultSet} to a given
 * field or column of an entity or embeddable type.
 *
 * @param container The Java class which declares the field
 * @param type The Java class of the field
 * @param name The name of the field
 * @param columnName The name of the mapped column of the result set
 * @param <C> The type of the entity or embeddable type
 * @param <T> The type of the field
 *
 * @see jakarta.persistence.FieldResult
 *
 * @since 4.0
 */
public record FieldMapping<C,T>
        (Class<C> container, Class<T> type, String name, String columnName)
        implements MemberMapping<C> {

    /**
     * Construct a new instance.
     * @param container The Java class which declares the field
     * @param type The Java class of the field
     * @param name The name of the field
     * @param columnName The name of the mapped column of the result set
     * @param <C> The type of the entity or embeddable type
     * @param <T> The type of the field
     */
    public static <C,T> FieldMapping<C,T> of(Class<C> container, Class<T> type, String name, String columnName) {
        return new FieldMapping<>(container, type, name, columnName);
    }

    /**
     * Construct a new instance.
     * @param attribute The metamodel object representing the field
     * @param columnName The name of the mapped column of the result set
     * @param <C> The type of the entity or embeddable type
     * @param <T> The type of the field
     */
    public static <C,T> FieldMapping<C,T> of(SingularAttribute<C,T> attribute, String columnName) {
        return new FieldMapping<>(attribute.getDeclaringType().getJavaType(), attribute.getJavaType(), attribute.getName(), columnName);
    }
}

