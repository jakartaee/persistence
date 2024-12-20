package jakarta.persistence.sql;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.EntityResult;
import jakarta.persistence.SqlResultSetMapping;

import java.lang.annotation.Annotation;

import static jakarta.persistence.sql.MappingElement.extract;

public record ResultSetMapping<T>(String name, EntityResult[] entities, ConstructorResult[] classes, ColumnResult[] columns)
        implements SqlResultSetMapping {

    public static ResultSetMapping<Object[]> create(MappingElement<?>... mappings) {
        return new ResultSetMapping<>("",
                extract(EntityResult.class, mappings),
                extract(ConstructorResult.class, mappings),
                extract(ColumnResult.class, mappings));
    }

    public static <T> ResultSetMapping<T> create(EntityMapping<T> entityMapping) {
        return new ResultSetMapping<>("",
                new EntityResult[]{entityMapping},
                new ConstructorResult[0],
                new ColumnResult[0]);
    }

    public static <T> ResultSetMapping<T> create(ConstructorMapping<T> constructorMapping) {
        return new ResultSetMapping<>("",
                new EntityResult[0],
                new ConstructorResult[]{constructorMapping},
                new ColumnResult[0]);
    }

    public static <T> ResultSetMapping<T> create(ColumnMapping<T> columnResult) {
        return new ResultSetMapping<>("",
                new EntityResult[0],
                new ConstructorResult[0],
                new ColumnResult[]{columnResult});
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return SqlResultSetMapping.class;
    }
}

