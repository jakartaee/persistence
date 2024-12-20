package jakarta.persistence.sql;

import java.lang.reflect.Array;
import java.util.Arrays;

public interface MappingElement<T> {

    @SuppressWarnings("unchecked")
    private static <R> R[] newArrayInstance(Class<R> type, int length) {
        return (R[]) Array.newInstance(type, length);
    }
    static <R> R[] extract(Class<R> type, MappingElement[] mappingElements) {
        return Arrays.stream(mappingElements)
                .filter(type::isInstance)
                .toArray(length -> newArrayInstance(type, length));
    }
}
