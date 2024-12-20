package jakarta.persistence.sql;

import jakarta.persistence.metamodel.SingularAttribute;

public record EmbeddableMapping<C,T>(Class<C> container, Class<T> embeddableClass, String name, MemberMapping<?>[] fields)
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