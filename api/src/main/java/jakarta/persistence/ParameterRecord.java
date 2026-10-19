package jakarta.persistence;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Internal implementation of {@link Parameter}.
 * @param type the Java type of the parameter
 * @param position the position of the parameter, or null if the parameter is not a positional parameter
 * @param name the name of the parameter, or null if the parameter is not a named parameter
 * @param <T> the type of the parameter
 * @see Parameter#of(Class, Integer, String)
 * @since 4.0
 */
record ParameterRecord<T>
        (@Nonnull Class<T> type, @Nullable Integer position, @Nullable String name)
        implements Parameter<T> {
    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Integer getPosition() {
        return position;
    }

    @Nonnull
    @Override
    public Class<T> getParameterType() {
        return type;
    }
}
