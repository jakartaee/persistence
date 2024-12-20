package jakarta.persistence.sql;

import jakarta.persistence.ColumnResult;

import java.lang.annotation.Annotation;

public record ColumnMapping<T>(String name, Class<T> type)
        implements ColumnResult, MappingElement<T> {

    public static ColumnMapping<Object> map(String name) {
        return new ColumnMapping<>(name, Object.class);
    }

    public static <T> ColumnMapping<T> map(String name, Class<T> type) {
        return new ColumnMapping<>(name, type);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ColumnResult.class;
    }
}
