package jakarta.persistence.sql;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to parameters
 * of the constructor of a Java class.
 *
 * @param targetClass The Java class which declares the constructor
 * @param columns The names of the mapped columns of the result set
 * @param <T> The type of the Java class
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
