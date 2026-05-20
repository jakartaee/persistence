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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
public record EntityMapping<T>(@Nonnull Class<T> entityClass,
                               @Nonnull LockModeType lockMode,
                               @Nullable String discriminatorColumn,
                               @Nonnull MemberMapping<? extends T>[] fields,
                               @Nullable String alias)
        implements MappingElement<T>, ResultSetMapping<T> {

    public EntityMapping(@Nonnull Class<T> entityClass,
                         @Nonnull LockModeType lockMode,
                         @Nullable String discriminatorColumn,
                         @Nonnull MemberMapping<? extends T>[] fields,
                         @Nullable String alias) {
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
        this.alias = alias;
    }

    @Override
    @Nonnull
    public MemberMapping<? extends T>[] fields() {
        return fields.clone();
    }

    /**
     * The entity class.
     */
    @Override
    @Nonnull
    public Class<T> getJavaType() {
        return entityClass;
    }

    /**
     * Return the alias specified via {@link #withAlias},
     * which may be used to retrieve an entity instance using
     * {@link jakarta.persistence.Tuple#get(String, Class)}
     * @return the explicitly specified alias or {@code null}
     */
    @Override
    @Nullable
    public String getAlias() {
        return alias;
    }

    /**
     * Specify an alias for this entity in the result set.
     * @param alias The alias
     */
    @Override
    @Nonnull
    public EntityMapping<T> withAlias(@Nonnull String alias) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields, alias);
    }

    /**
     * Specify the lock mode obtained on this entity.
     * @param lockMode The lock mode
     */
    @Nonnull
    public EntityMapping<T> withLockMode(@Nonnull LockModeType lockMode) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields, alias);
    }

    /**
     * Construct a new instance.
     * @param entityClass The entity class
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The entity type
     */
    @Nonnull
    @SafeVarargs
    public static <T> EntityMapping<T> of(@Nonnull Class<T> entityClass,
                                          @Nonnull MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, null, fields, null);
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
    @Nonnull
    @SafeVarargs
    public static <T> EntityMapping<T> of(@Nonnull Class<T> entityClass,
                                          @Nonnull String discriminatorColumn,
                                          @Nonnull MemberMapping<? extends T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, discriminatorColumn, fields, null);
    }

    /**
     * The entity class.
     */
    @Override
    @Nonnull
    public Class<T> type() {
        return entityClass;
    }
}