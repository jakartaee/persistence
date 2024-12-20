package jakarta.persistence.sql;

public record CompoundMapping(MappingElement<?>[] elements)
        implements ResultSetMapping<Object[]> {
    public static CompoundMapping of(MappingElement<?>... elements) {
        return new CompoundMapping(elements);
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }
}

