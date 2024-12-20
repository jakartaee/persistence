package jakarta.persistence.sql;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;

import java.lang.annotation.Annotation;

public record ConstructorMapping<T>(Class<T> targetClass, ColumnResult[] columns)
        implements ConstructorResult, MappingElement<T> {

    public static <T> ConstructorMapping<T> map(Class<T> targetClass, ColumnResult... columns) {
        return new ConstructorMapping<>(targetClass, columns);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ConstructorResult.class;
    }
}
