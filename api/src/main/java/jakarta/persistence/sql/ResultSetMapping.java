package jakarta.persistence.sql;

import jakarta.persistence.LockModeType;
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
 *                 column("isbn"),
 *                 column("title"),
 *                 column("author"));
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
 * {@linkplain jakarta.persistence.EntityManager#createNativeQuery(String, ResultSetMapping) obtain}
 * and execute a {@link jakarta.persistence.TypedQuery TypedQuery}.
 *
 * @since 4.0
 */
public sealed interface ResultSetMapping<T>
        permits EntityMapping, CompoundMapping, ColumnMapping, ConstructorMapping {
    /**
     * The result type of the mapping.
     */
    Class<T> type();

    /**
     * Construct a mapping for a single column to a scalar value.
     * @param columnName The colum name
     * @param type The Java type of the scalar value
     */
    static <T> ColumnMapping<T> column(String columnName, Class<T> type) {
        return ColumnMapping.of(columnName, type);
    }

    /**
     * Construct a mapping for a single column to a scalar value.
     * @param columnName The colum name
     */
    static ColumnMapping<Object> column(String columnName) {
        return ColumnMapping.of(columnName);
    }

    /**
     * Construct a mapping to a constructor of a Java class.
     *
     * @param targetClass The Java class which declares the constructor
     * @param columns Mappings for the columns
     */
    static <T> ConstructorMapping<T> constructor(Class<T> targetClass, ColumnMapping<?>... columns) {
        return ConstructorMapping.of(targetClass, columns);
    }

    /**
     * Construct a mapping which packages a tuple of values as a Java array.
     *
     * @param elements Mappings for elements of the type
     */
    static CompoundMapping compound(MappingElement<?>... elements) {
        return CompoundMapping.of(elements);
    }

    /**
     * Construct a mapping for an entity class.
     *
     * @param entityClass The Java class of the entity
     * @param fields Mappings for fields or properties of the entity
     */
    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, LockModeType.NONE, "", fields);
    }

    /**
     * Construct a mapping for an entity class.
     *
     * @param entityClass The Java class of the entity
     * @param discriminatorColumn The name of the column holding the discriminator
     * @param fields Mappings for fields or properties of the entity
     */
    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, String discriminatorColumn, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    /**
     * Construct a mapping for an entity class.
     *
     * @param entityClass The Java class of the entity
     * @param lockMode The lock mode acquired by SQL query
     * @param discriminatorColumn The name of the column holding the discriminator
     * @param fields Mappings for fields or properties of the entity
     */
    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, lockMode, discriminatorColumn, fields);
    }

    /**
     * Construct a mapping for an embedded object.
     *
     * @param container The Java class which declares the field holding the embedded object
     * @param embeddableClass The Java class of the embedded object
     * @param name The name of the field holding the embedded object
     * @param fields Mappings for fields or properties of the entity
     */
    @SafeVarargs
    static <C,T> EmbeddableMapping<C,T> embedded(Class<C> container, Class<T> embeddableClass, String name, MemberMapping<T>... fields) {
        return EmbeddableMapping.of(container, embeddableClass, name, fields);
    }

    /**
     * Construct a mapping for an embedded object.
     *
     * @param embedded The metamodel attribute representing the field or property holding the embedded object
     * @param fields Mappings for fields or properties of the entity
     */
    @SafeVarargs
    static <C,T> EmbeddableMapping<C,T> embedded(SingularAttribute<C,T> embedded, MemberMapping<T>... fields) {
        return EmbeddableMapping.of(embedded.getDeclaringType().getJavaType(), embedded.getJavaType(), embedded.getName(), fields);
    }

    /**
     * Construct a mapping for a field or property of an entity or embeddable type.
     *
     * @param container The Java class which declares the field or property
     * @param type The type of the field or property
     * @param name The name of the field or property
     * @param columnName The name of the mapped column
     */
    static <C,T> FieldMapping<C,T> field(Class<C> container, Class<T> type, String name, String columnName) {
        return FieldMapping.of(container, type, name, columnName);
    }

    /**
     * Construct a mapping for a field or property of an entity or embeddable type.
     *
     * @param attribute The metamodel attribute representing the field or property
     * @param columnName The name of the mapped column
     */
    static <C,T> FieldMapping<C,T> field(SingularAttribute<C,T> attribute, String columnName) {
        return FieldMapping.of(attribute.getDeclaringType().getJavaType(), attribute.getJavaType(), attribute.getName(), columnName);
    }
}
