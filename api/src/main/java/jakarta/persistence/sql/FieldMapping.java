package jakarta.persistence.sql;

import jakarta.persistence.FieldResult;
import jakarta.persistence.metamodel.SingularAttribute;

import java.lang.annotation.Annotation;

public record FieldMapping<T>(String name, String column)
        implements FieldResult {

    public static <T> FieldMapping<T> map(SingularAttribute<T,?> attribute, String column) {
        return new FieldMapping<>(attribute.getName(), column);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return FieldResult.class;
    }
}

