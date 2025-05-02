package jakarta.persistence.sql;

/**
 * Supertype of objects which map a column or columns of a
 * JDBC {@link java.sql.ResultSet} to a Java type returned
 * by the query.
 *
 * @param <T> The type returned
 *
 * @since 4.0
 */
public sealed interface MappingElement<T>
        permits EntityMapping, ColumnMapping, ConstructorMapping  {
}
