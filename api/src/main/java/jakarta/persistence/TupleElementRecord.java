package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Internal implementation of {@link TupleElement}.
 * @param type the Java type of the tuple element
 * @param alias the alias of the tuple element, or
 *              null if the tuple element has no alias
 * @param <X> the type of the tuple element
 * @since 4.0
 */
record TupleElementRecord<X>
        (@Nonnull Class<X> type, @Nullable String alias)
        implements TupleElement<X> {

    @Override
    @Nonnull
    public Class<X> getJavaType() {
        return type;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Nonnull
    Tuple.Entry<X> bind(X argument) {
        return new Tuple.Entry<>(this, argument);
    }
}
