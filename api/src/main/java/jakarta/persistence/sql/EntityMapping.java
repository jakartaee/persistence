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

import jakarta.persistence.LockModeType;

import static java.util.Objects.requireNonNull;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to a given
 * {@linkplain jakarta.persistence.Entity entity} class.
 *
 * @param entityClass The entity class
 * @param lockMode The lock mode acquired by the SQL query
 * @param discriminatorColumn The name of the column holding the
 *        {@linkplain jakarta.persistence.DiscriminatorColumn
 *        discriminator}; a {@code null} value indicates that
 *        there is no discriminator column.
 * @param fields Mappings for fields or properties of the entity
 *               and of its entity subclasses
 * @param <T> The entity type
 *
 * @see jakarta.persistence.EntityResult
 *
 * @since 4.0
 */
public record EntityMapping<T>
        (Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<? extends T>[] fields)
        implements MappingElement<T>, ResultSetMapping<T> {

    public EntityMapping(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<? extends T>[] fields) {
        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(lockMode, "lockMode is required");
        if (discriminatorColumn != null && discriminatorColumn.isBlank()) {
            throw new IllegalArgumentException("discriminatorColumn may not be blank");
        }
        requireNonNull(fields, "fields are required");
        for (var field : fields) {
            requireNonNull(field, "field is required");
        }
        this.entityClass = entityClass;
        this.lockMode = lockMode;
        this.discriminatorColumn = discriminatorColumn;
        this.fields = fields.clone();
    }

    @Override
    public MemberMapping<? extends T>[] fields() {
        return fields.clone();
    }

    /**
     * The entity class.
     */
    @Override
    public Class<T> getJavaType() {
        return entityClass;
    }

    /**
     * Always returns {@code null}.
     */
    @Override
    public String getAlias() {
        return null;
    }

    /**
     * Construct a new instance.
     * @param entityClass The entity class
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The entity type
     */
    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, null, fields);
    }

    /**
     * Construct a new instance.
     * @param entityClass The entity class
     * @param lockMode The lock mode acquired by the SQL query
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The entity type
     */
    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, LockModeType lockMode, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, lockMode, null, fields);
    }

    /**
     * Construct a new instance.
     * @param entityClass The entity class
     * @param discriminatorColumn The name of the column holding the
     *        {@linkplain jakarta.persistence.DiscriminatorColumn
     *        discriminator}; a {@code null} value indicates that
     *        there is no discriminator column.
     * @param fields Mappings for fields or properties of the entity
     *               and of its entity subclasses
     * @param <T> The entity type
     */
    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, String discriminatorColumn, MemberMapping<? extends T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    /**
     * Construct a new instance.
     * @param entityClass The entity class
     * @param lockMode The lock mode acquired by the SQL query
     * @param discriminatorColumn The name of the column holding the
     *        {@linkplain jakarta.persistence.DiscriminatorColumn
     *        discriminator}; a {@code null} value indicates that
     *        there is no discriminator column.
     * @param fields Mappings for fields or properties of the entity
     *               and of its entity subclasses
     * @param <T> The entity type
     */
    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<? extends T>... fields) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields);
    }

    /**
     * The entity class.
     */
    @Override
    public Class<T> type() {
        return entityClass;
    }
}