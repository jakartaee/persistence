package jakarta.persistence.sql;

import jakarta.persistence.metamodel.SingularAttribute;

public record FieldMapping<C,T>(Class<C> container, Class<T> type, String name, String column) implements MemberMapping<C> {

    public static <C,T> FieldMapping<C,T> of(Class<C> container, Class<T> type, String name, String column) {
        return new FieldMapping<>(container, type, name, column);
    }

    public static <C,T> FieldMapping<C,T> of(SingularAttribute<C,T> attribute, String column) {
        return new FieldMapping<>(attribute.getDeclaringType().getJavaType(), attribute.getJavaType(), attribute.getName(), column);
    }
}

