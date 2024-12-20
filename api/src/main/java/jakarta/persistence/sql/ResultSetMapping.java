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
public interface ResultSetMapping<T> {
    Class<T> type();

    static <T> ColumnMapping<T> column(String name, Class<T> type) {
        return ColumnMapping.of(name, type);
    }

    static ColumnMapping<Object> column(String name) {
        return ColumnMapping.of(name);
    }

    static <T> ConstructorMapping<T> constructor(Class<T> targetClass, ColumnMapping<?>... columns) {
        return ConstructorMapping.of(targetClass, columns);
    }

    static CompoundMapping compound(MappingElement<?>... elements) {
        return CompoundMapping.of(elements);
    }

    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, LockModeType.NONE, "", fields);
    }

    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, String discriminatorColumn, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    @SafeVarargs
    static <T> EntityMapping<T> entity(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<T>... fields) {
        return EntityMapping.of(entityClass, lockMode, discriminatorColumn, fields);
    }

    @SafeVarargs
    static <C,T> EmbeddableMapping<C,T> embedded(Class<C> container, Class<T> embeddableClass, String name, MemberMapping<T>... fields) {
        return EmbeddableMapping.of(container, embeddableClass, name, fields);
    }

    @SafeVarargs
    static <C,T> EmbeddableMapping<C,T> embedded(SingularAttribute<C,T> embedded, MemberMapping<T>... fields) {
        return EmbeddableMapping.of(embedded.getDeclaringType().getJavaType(), embedded.getJavaType(), embedded.getName(), fields);
    }

    static <C,T> FieldMapping<C,T> field(Class<C> container, Class<T> type, String name, String column) {
        return FieldMapping.of(container, type, name, column);
    }

    static <C,T> FieldMapping<C,T> field(SingularAttribute<C,T> attribute, String column) {
        return FieldMapping.of(attribute.getDeclaringType().getJavaType(), attribute.getJavaType(), attribute.getName(), column);
    }
}
