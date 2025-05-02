package jakarta.persistence.sql;

import jakarta.persistence.metamodel.SingularAttribute;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to a given
 * {@linkplain jakarta.persistence.Embedded embedded object}.
 *
 * @param container The Java class which declares the field
 *                  holding the embedded object
 * @param embeddableClass The embeddable class
 * @param name The name of the field holding the embedded object
 * @param fields Mappings for fields or properties of the entity
 * @param <T> The entity type
 *
 * @since 4.0
 */
public record EmbeddableMapping<C,T>
        (Class<C> container, Class<T> embeddableClass, String name, MemberMapping<?>[] fields)
        implements MemberMapping<C> {

    @SafeVarargs
    public static <C,T> EmbeddableMapping<C,T> of(Class<C> container, Class<T> embeddableClass, String name, MemberMapping<T>... fields) {
        return new EmbeddableMapping<>(container, embeddableClass, name, fields);
    }

    @SafeVarargs
    public static <C,T> EmbeddableMapping<C,T> of(SingularAttribute<C,T> embedded, MemberMapping<T>... fields) {
        return new EmbeddableMapping<>(embedded.getDeclaringType().getJavaType(), embedded.getJavaType(), embedded.getName(), fields);
    }
}