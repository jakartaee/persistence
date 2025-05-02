package jakarta.persistence.sql;

import jakarta.persistence.LockModeType;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to a given
 * {@linkplain jakarta.persistence.Entity entity} class.
 *
 * @param entityClass The entity class
 * @param lockMode The lock mode acquired by the SQL query
 * @param discriminatorColumn The name of the column holding the
 *        {@linkplain jakarta.persistence.DiscriminatorColumn
 *        discriminator}
 * @param fields Mappings for fields or properties of the entity
 * @param <T> The entity type
 *
 * @since 4.0
 */
public record EntityMapping<T>
        (Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<?>[] fields)
        implements MappingElement<T>, ResultSetMapping<T> {

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, "", fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, String discriminatorColumn, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> of(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<T>... fields) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields);
    }

    @Override
    public Class<T> type() {
        return entityClass;
    }
}