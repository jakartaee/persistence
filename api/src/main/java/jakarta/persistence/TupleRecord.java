package jakarta.persistence;

import jakarta.annotation.Nonnull;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * Create a tuple with the elements and values specified by the given entries.
 * @param entries the elements and values belonging to the tuple
 * @since 4.0
 */
record TupleRecord(Tuple.Entry<?>[] entries) implements Tuple {
    TupleRecord {
        requireNonNull(entries, "entries are required");
        for (var entry: entries) {
            requireNonNull(entry, "entry may not be null");
        }
    }

    @Override
    public <X> X get(@Nonnull TupleElement<X> tupleElement) {
        for (var entry : entries) {
            if (entry.element().equals(tupleElement)) {
                return tupleElement.getJavaType().cast(entry.value());
            }
        }
        throw new IllegalArgumentException("no such tuple element");
    }

    @Override
    public <X> X get(@Nonnull String alias, @Nonnull Class<X> type) {
        for (var entry : entries) {
            if (entry.element().hasAlias(alias)) {
                return type.cast(entry.value());
            }
        }
        throw new IllegalArgumentException("no tuple element with the given alias");
    }

    @Override
    public Object get(@Nonnull String alias) {
        for (var entry : entries) {
            if (entry.element().hasAlias(alias)) {
                return entry.value();
            }
        }
        throw new IllegalArgumentException("no tuple element with the given alias");
    }

    @Override
    public <X> X get(int position, @Nonnull Class<X> type) {
        if (position >= 0 && position < entries.length) {
            return type.cast(entries[position]);
        }
        else {
            throw new IllegalArgumentException("no tuple element with the given position");
        }
    }

    @Override
    public Object get(int position) {
        if (position >= 0 && position < entries.length) {
            return entries[position];
        }
        else {
            throw new IllegalArgumentException("no tuple element with the given position");
        }
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        final var values = new Object[entries.length];
        for (int i = 0; i < entries.length; i++) {
            values[i] = entries[i].value();
        }
        return values;
    }

    @Nonnull
    @Override
    public List<TupleElement<?>> getElements() {
        final var elements = new TupleElement<?>[entries.length];
        for (int i = 0; i < entries.length; i++) {
            elements[i] = entries[i].element();
        }
        return asList(elements);
    }

    @Nonnull
    @Override
    public List<Entry<?>> getEntries() {
        return asList(entries);
    }
}
