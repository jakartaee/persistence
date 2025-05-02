package jakarta.persistence.sql;

/**
 * Supertype of objects which map a column or columns of
 * a JDBC {@link java.sql.ResultSet} to a member of an
 * entity or embeddable type.
 *
 * @param <T> The entity or embeddable type
 *
 * @since 4.0
 */
public sealed interface MemberMapping<T>
        permits FieldMapping, EmbeddableMapping {
}
