package jakarta.persistence.sql;

/**
 * Specifies a mapping of the columns of a result set of a SQL query or stored procedure
 * to {@linkplain EntityMapping entities}, {@linkplain ColumnMapping scalar values}, and
 * {@linkplain ConstructorMapping Java class constructors}.
 *
 * <p>This class may be instantiated programmatically, for example:
 * {@snippet :
 * var entityMapping =
 *         ResultSetMapping.of(
 *                 EntityMapping.of(Author.class,
 *                         FieldMapping.of(Author_.ssn, "author_ssn"),
 *                         EmbeddableMapping.of(Author_.name,
 *                                 FieldMapping.of(Name_.first, "author_first_name"),
 *                                 FieldMapping.of(Name_.last, "author_last_name"))));
 *
 * var constructorMapping =
 *         ResultSetMapping.of(
 *                 ConstructorMapping.of(Summary.class,
 *                         ColumnMapping.of("book_isbn"),
 *                         ColumnMapping.of("book_title"),
 *                         ColumnMapping.of("book_author")));
 *
 * var mixedMapping =
 *         ResultSetMapping.of(
 *                 EntityMapping.of(Author.class),
 *                 EntityMapping.of(Book.class,
 *                         FieldMapping.of(Book_.isbn, "book_isbn")),
 *                 ColumnMapping.of("sales", BigDecimal.class),
 *                 ConstructorMapping.of(Summary.class,
 *                         ColumnMapping.of("book_isbn"),
 *                         ColumnMapping.of("book_title")));
 * }
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
public record ResultSetMapping<T>(Class<T> resultType, MappingElement<?>[] elements) {

    public static ResultSetMapping<Object[]> of(MappingElement<?>... mappings) {
        return new ResultSetMapping<>(Object[].class, mappings);
    }

    public static <T> ResultSetMapping<T> of(EntityMapping<T> entityMapping) {
        return new ResultSetMapping<>(entityMapping.entityClass(),
                new EntityMapping[]{entityMapping});
    }

    public static <T> ResultSetMapping<T> of(ConstructorMapping<T> constructorMapping) {
        return new ResultSetMapping<>(constructorMapping.targetClass(),
                new ConstructorMapping[]{constructorMapping});
    }

    public static <T> ResultSetMapping<T> of(ColumnMapping<T> columnMapping) {
        return new ResultSetMapping<>(columnMapping.type(),
                new ColumnMapping[]{columnMapping});
    }
}

