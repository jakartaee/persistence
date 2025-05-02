package jakarta.persistence.sql;

/**
 * Maps a JDBC {@link java.sql.ResultSet} to a tuple of values
 * packaged as an object array.
 *
 * @param elements Mappings for the elements of the tuple
 *
 * @since 4.0
 */
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

