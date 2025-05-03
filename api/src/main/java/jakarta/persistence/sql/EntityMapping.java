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

import jakarta.persistence.LockModeType;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to a given
 * {@linkplain jakarta.persistence.Entity entity} class.
 *
 * @param entityClass The entity class
 * @param lockMode The lock mode acquired by the SQL query
 * @param discriminatorColumn The name of the column holding the
 *        {@linkplain jakarta.persistence.DiscriminatorColumn
 *        discriminator}
 * @param fields Mappings for fields or properties of the entity
 * @param <T> The entity type
 *
 * @see jakarta.persistence.EntityResult
 *
 * @since 4.0
 */
public record EntityMapping<T>
        (Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<?>[] fields)
        implements MappingElement<T>, ResultSetMapping<T> {

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, "", fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, String discriminatorColumn, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields);
    }

    @Override
    public Class<T> type() {
        return entityClass;
    }
}