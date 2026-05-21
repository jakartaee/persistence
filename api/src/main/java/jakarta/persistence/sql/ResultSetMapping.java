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
import jakarta.persistence.metamodel.SingularAttribute;

/**
 * Specifies a mapping of the columns of a result set of a SQL query or stored procedure
 * to {@linkplain EntityMapping entities}, {@linkplain ColumnMapping scalar values}, and
 * {@linkplain ConstructorMapping Java class constructors}.
 *
 * <p>A {@link ResultSetMapping} may be instantiated programmatically, for example:
 * {@snippet :
 * import static jakarta.persistence.sql.ResultSetMapping.*;
 *
 * ...
 *
 * var entityMapping =
 *         entity(Author.class,
 *                 field(Author_.ssn, "auth_ssn"),
 *                 embedded(Author_.name,
 *                         field(Name_.first, "auth_first_name"),
 *                         field(Name_.last, "auth_last_name")));
 *
 * var constructorMapping =
 *         constructor(Summary.class,
 *                 column("isbn", String.class),
 *                 column("title", String.class),
 *                 column("author", String.class));
 *
 * var compoundMapping =
 *         compound(
 *                 entity(Author.class),
 *                 entity(Book.class, field(Book_.isbn, "isbn")),
 *                 column("sales", BigDecimal.class),
 *                 constructor(Summary.class, column("isbn"), column("title"))
 *         );
 *}
 *
 * <p>Alternatively, an instance representing a
 * {@linkplain jakarta.persistence.SqlResultSetMapping result set mapping defined using annotations}
 * may be obtained via {@link jakarta.persistence.EntityManagerFactory#getResultSetMappings}.
 *
 * <p>A {@code ResultSetMapping} may be used to
 * {@linkplain jakarta.persistence.EntityHandler#createNativeQuery(String, ResultSetMapping) obtain}
 * and execute a {@link jakarta.persistence.TypedQuery TypedQuery}.
 *
 * @see jakarta.persistence.SqlResultSetMapping
 * @see jakarta.persistence.EntityManagerFactory#getResultSetMappings(Class)
 * @see jakarta.persistence.EntityHandler#createNativeQuery(String, ResultSetMapping)
 * @see jakarta.persistence.StoredProcedureQuery#getResultList(ResultSetMapping)
 * @see jakarta.persistence.StoredProcedureQuery#getSingleResult(ResultSetMapping)
 * @see jakarta.persistence.StoredProcedureQuery#getSingleResultOrNull(ResultSetMapping)
 *
 * @param <T> The result type of the mapping
 *
 * @since 4.0
 */
public sealed interface ResultSetMapping<T>
        permits CompoundMapping, TupleMapping, EntityMapping, ConstructorMapping, ColumnMapping {
    /**
     * The result type of the mapping.
     */
    @Nonnull
    Class<T> type();

    /**
     * Construct a mapping for a single column to a scalar value.
     *
     * @param columnName The colum name
     * @param type The Java type of the scalar value
     * @param <T> The type of the scalar value
     *
     * @see jakarta.persistence.ColumnResult
     */
    @Nonnull
    static <T> ColumnMapping<T> column(@Nonnull String columnName,
                                       @Nonnull Class<T> type) {
        return ColumnMapping.of(columnName, type);
    }

    /**
     * Construct a mapping for a single column to a scalar value.
     *
     * @param columnName The colum name
     *
     * @see jakarta.persistence.ColumnResult
     */
    @Nonnull
    static ColumnMapping<Object> column(@Nonnull String columnName) {
        return ColumnMapping.of(columnName);
    }

    /**
     * Construct a mapping to a constructor of a Java class.
     *
     * @param targetClass The Java class which declares the constructor
     * @param arguments Mappings for the constructor parameters, in order
     * @param <T> The type of the Java class
     *
     * @see jakarta.persistence.ConstructorResult
     */
    @Nonnull
    static <T> ConstructorMapping<T> constructor(@Nonnull Class<T> targetClass,
                                                 @Nonnull MappingElement<?>... arguments) {
        return ConstructorMapping.of(targetClass, arguments);
    }

    /**
     * Construct a mapping which packages a tuple of values as a Java array.
     *
     * @param elements Mappings for elements of the type
     */
    @Nonnull
    static CompoundMapping compound(@Nonnull MappingElement<?>... elements) {
        return CompoundMapping.of(elements);
    }

    /**
     * Construct a mapping which packages a tuple of values as an instance of
     * {@link jakarta.persistence.Tuple}.
     *
     * @param elements Mappings for elements of the type
     */
    @Nonnull
    static TupleMapping tuple(@Nonnull MappingElement<?>... elements) {
        return TupleMapping.of(elements);
    }

    /**
     * Construct a mapping for an entity class.
     *
     * @param entityClass The Java class of the entity
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The entity type
     *
     * @see jakarta.persistence.EntityResult
     */
    @Nonnull
    @SafeVarargs
    static <T> EntityMapping<T> entity(@Nonnull Class<T> entityClass,
                                       @Nonnull MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, fields);
    }

    /**
     * Construct a mapping for an entity class.
     *
     * @param entityClass The Java class of the entity
     * @param discriminatorColumn The name of the column holding the discriminator;
     *        an empty string indicates that there is no discriminator column
     * @param fields Mappings for fields or properties of the entity
     *               and of its entity subclasses
     * @param <T> The entity type
     *
     * @see jakarta.persistence.EntityResult
     */
    @Nonnull
    @SafeVarargs
    static <T> EntityMapping<T> entity(@Nonnull Class<T> entityClass,
                                       @Nonnull String discriminatorColumn,
                                       @Nonnull MemberMapping<? extends T>... fields) {
        return EntityMapping.of(entityClass, discriminatorColumn, fields);
    }

    /**
     * Construct a mapping for an embedded object.
     *
     * @param container The Java class which declares the field holding the embedded object
     * @param embeddableClass The Java class of the embedded object
     * @param name The name of the field holding the embedded object
     * @param fields Mappings for fields or properties of the entity
     * @param <C> The container type
     * @param <T> The embeddable type
     */
    @Nonnull
    @SafeVarargs
    static <C,T> EmbeddedMapping<C,T> embedded(@Nonnull Class<? super C> container,
                                               @Nonnull Class<T> embeddableClass,
                                               @Nonnull String name, MemberMapping<T>... fields) {
        return EmbeddedMapping.of(container, embeddableClass, name, fields);
    }

    /**
     * Construct a mapping for an embedded object.
     *
     * @param embedded The metamodel attribute representing the field or property holding the embedded object
     * @param fields Mappings for fields or properties of the entity
     * @param <C> The container type
     * @param <T> The embeddable type
     */
    @Nonnull
    @SafeVarargs
    static <C,T> EmbeddedMapping<C,T> embedded(@Nonnull SingularAttribute<? super C,T> embedded,
                                               @Nonnull MemberMapping<T>... fields) {
        return EmbeddedMapping.of(embedded, fields);
    }

    /**
     * Construct a mapping for a field or property of an entity or embeddable type.
     *
     * @param container The Java class which declares the field or property
     * @param type The type of the field or property
     * @param name The name of the field or property
     * @param columnName The name of the mapped column
     * @param <C> The type of the entity or embeddable type
     * @param <T> The type of the field or property
     *
     * @see jakarta.persistence.FieldResult
     */
    @Nonnull
    static <C,T> FieldMapping<C,T> field(@Nonnull Class<? super C> container,
                                         @Nonnull Class<T> type,
                                         @Nonnull String name,
                                         @Nonnull String columnName) {
        return FieldMapping.of(container, type, name, columnName);
    }

    /**
     * Construct a mapping for a field or property of an entity or embeddable type.
     *
     * @param attribute The metamodel attribute representing the field or property
     * @param columnName The name of the mapped column
     * @param <C> The type of the entity or embeddable type
     * @param <T> The type of the field or property
     *
     * @see jakarta.persistence.FieldResult
     */
    static <C,T> FieldMapping<C,T> field(@Nonnull SingularAttribute<? super C,T> attribute,
                                         @Nonnull String columnName) {
        return FieldMapping.of(attribute, columnName);
    }
}
