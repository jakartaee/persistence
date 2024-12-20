package jakarta.persistence.sql;

public record ColumnMapping<T>(String name, Class<T> type)
        implements MappingElement<T> {

    public static ColumnMapping<Object> of(String name) {
        return new ColumnMapping<>(name, Object.class);
    }

    public static <T> ColumnMapping<T> of(String name, Class<T> type) {
        return new ColumnMapping<>(name, type);
    }
}
