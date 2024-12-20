package jakarta.persistence.sql;

import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.LockModeType;

import java.lang.annotation.Annotation;

public record EntityMapping<T>(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, FieldResult[] fields)
        implements EntityResult, MappingElement<T> {

    @SafeVarargs
    public static <T> EntityMapping<T> map(Class<T> entityClass, FieldMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, "", fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> map(Class<T> entityClass, String discriminatorColumn, FieldMapping<T>... fields) {
        return new EntityMapping<>(entityClass, LockModeType.NONE, discriminatorColumn, fields);
    }

    @SafeVarargs
    public static <T> EntityMapping<T> map(Class<T> entityClass, LockModeType lockMode, String discriminatorColumn, FieldMapping<T>... fields) {
        return new EntityMapping<>(entityClass, lockMode, discriminatorColumn, fields);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return EntityResult.class;
    }
}