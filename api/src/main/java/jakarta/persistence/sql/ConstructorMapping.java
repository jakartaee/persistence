package jakarta.persistence.sql;

public record ConstructorMapping<T>(Class<T> targetClass, ColumnMapping<?>[] columns)
        implements MappingElement<T>, ResultSetMapping<T> {

    public static <T> ConstructorMapping<T> of(Class<T> targetClass, ColumnMapping<?>... columns) {
        return new ConstructorMapping<>(targetClass, columns);
    }

    @Override
    public Class<T> type() {
        return targetClass;
    }
}
