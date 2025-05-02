package jakarta.persistence.sql;

/**
 * Maps a column of a JDBC {@link java.sql.ResultSet} to a scalar
 * value in the result returned by the query.
 *
 * @param columnName The name of the mapped column of the result set
 * @param type The Java type of the resulting scalar value
 * @param <T> The type of the resulting scalar value
 *
 * @since 4.0
 */
public record ColumnMapping<T>(String columnName, Class<T> type)
        implements MappingElement<T>, ResultSetMapping<T> {

    public static ColumnMapping<Object> of(String columnName) {
        return new ColumnMapping<>(columnName, Object.class);
    }

    public static <T> ColumnMapping<T> of(String columnName, Class<T> type) {
        return new ColumnMapping<>(columnName, type);
    }
}
