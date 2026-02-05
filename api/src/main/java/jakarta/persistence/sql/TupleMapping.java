/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Gavin King      - 4.0

package jakarta.persistence.sql;

import jakarta.persistence.Tuple;

import static java.util.Objects.requireNonNull;

/**
 * Map columns of a JDBC {@link java.sql.ResultSet} to entries
 * in a tuple of values packaged as an instance of {@link Tuple}.
 *
 * <p>{@snippet :
 * import static jakarta.persistence.sql.ResultSetMapping.*;
 *
 * ...
 *
 * var isbn = column("isbn", String.class);
 * var title = column("title", String.class);
 * var pages = column("pages", int.class);
 *
 * entityManager.createNativeQuery("select isbn, title, pages from Book",
 *                                 tuple(isbn, title, pages))
 *     .getResultList().forEach(tuple -> {
 *         String bookIsbn = tuple.get(isbn);
 *         String bookTitle = tuple.get(title);
 *         int bookPages = tuple.get(pages);
 *         ...
 *     });
 * }
 *
 * @param elements Mappings for the elements of the tuple
 *
 * @see CompoundMapping
 * @see jakarta.persistence.SqlResultSetMapping
 *
 * @since 4.0
 */
public record TupleMapping(MappingElement<?>[] elements)
        implements ResultSetMapping<Tuple> {

    public TupleMapping {
        requireNonNull(elements, "elements are required");
        if (elements.length == 0) {
            throw new IllegalArgumentException("at least one element is required");
        }
        for (var element : elements) {
            requireNonNull(element, "element is required");
        }
    }

    @Override
    public MappingElement<?>[] elements() {
        return elements.clone();
    }

    /**
     * Construct a new instance.
     * @param elements Mappings for the elements of the tuple
     */
    public static TupleMapping of(MappingElement<?>... elements) {
        return new TupleMapping(elements);
    }

    /**
     * Always returns {@link Tuple Tuple.class}.
     */
    @Override
    public Class<Tuple> type() {
        return Tuple.class;
    }
}

