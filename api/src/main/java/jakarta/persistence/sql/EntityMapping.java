package jakarta.persistence.sql;

import jakarta.persistence.LockModeType;

public record EntityMapping<T>(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, MemberMapping<?>[] fields)
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