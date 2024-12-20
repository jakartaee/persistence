package jakarta.persistence.sql;

public record ConstructorMapping<T>(Class<T> targetClass, ColumnMapping<?>[] columns)
        implements MappingElement<T> {

    public static <T> ConstructorMapping<T> of(Class<T> targetClass, ColumnMapping<?>... columns) {
        return new ConstructorMapping<>(targetClass, columns);
    }
}
