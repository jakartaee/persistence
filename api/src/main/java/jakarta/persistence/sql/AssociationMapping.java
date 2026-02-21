/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import static java.util.Objects.requireNonNull;

/**
 * Maps a fetched association to columns in a JDBC {@link java.sql.ResultSet}.
 * {@snippet :
 * var bookWithPublisherAndAuthors =
 *         entity(Book.class,
 *                 field(Book_.isbn, "BOOK_ISBN"),
 *                 field(Book_.title, "BOOK_TITLE"),
 *                 toOne(Book_.publisher,
 *                         entity(Publisher.class,
 *                                 field(Publisher_.name, "PUB_NAME")),
 *                         "BOOK_PUB_ID"),
 *                 toMany(Book_.authors,
 *                         entity(Author.class,
 *                                 field(Author_.ssn, "AUTH_SSN"),
 *                                 field(Author_.name, "AUTH_NAME")),
 *                         "BOOK_AUTH_SSN"));
 * }
 *
 * @param container The Java class which declares the association
 * @param name The name of the association field
 * @param target A mapping for the target entity
 * @param joinColumnNames The foreign key columns of the result set
 * @param <C> The type of the entity or embeddable type which declares
 *            the association
 * @param <E> The type of the target entity
 *
 * @since 4.0
 */
public record AssociationMapping<C,E>
        (Class<C> container, String name, EntityMapping<E> target, String[] joinColumnNames)
        implements MemberMapping<C> {

    public AssociationMapping(Class<C> container, String name, EntityMapping<E> target, String[] joinColumnNames) {
        requireNonNull(container, "container is required");
        requireNonNull(target, "target is required");
        requireNonNull(name, "name is required");
        requireNonNull(joinColumnNames, "join column names are required");
        if (joinColumnNames.length == 0) {
            throw new IllegalArgumentException("at least one join column name is required");
        }
        for (var joinColumnName : joinColumnNames) {
            requireNonNull(joinColumnName, "join column name is required");
        }
        this.container = container;
        this.name = name;
        this.target = target;
        this.joinColumnNames = joinColumnNames.clone();
    }

    /**
     * Construct a new instance for the given to-one association.
     * @param attribute The metamodel object representing the to-one association
     * @param target The mapping for the target entity
     * @param joinColumnNames The foreign key columns of the result set
     * @param <C> The type of the entity or embeddable type which declares
     *            the association
     * @param <E> The type of the target entity
     */
    public static <C,E> AssociationMapping<C,E> of(SingularAttribute<C,E> attribute, EntityMapping<E> target, String... joinColumnNames) {
        return new AssociationMapping<>(attribute.getDeclaringType().getJavaType(), attribute.getName(), target, joinColumnNames);
    }

    /**
     * Construct a new instance for the given to-many association.
     * @param attribute The metamodel object representing the to-many association
     * @param target The mapping for the target entity
     * @param joinColumnNames The foreign key columns of the result set
     * @param <C> The type of the entity or embeddable type which declares
     *            the association
     * @param <E> The type of the target entity
     */
    public static <C,E> AssociationMapping<C,E> of(PluralAttribute<C, ?, E> attribute, EntityMapping<E> target, String... joinColumnNames) {
        return new AssociationMapping<>(attribute.getDeclaringType().getJavaType(), attribute.getName(), target, joinColumnNames);
    }

    /**
     * Construct a new instance for the named association.
     * @param container The Java class which declares the association
     * @param name The name of the association field
     * @param target The mapping for the target entity
     * @param joinColumnNames The foreign key columns of the result set
     * @param <C> The type of the entity or embeddable type which declares
     *            the association
     * @param <E> The type of the target entity
     */
    public static <C,E> AssociationMapping<C,E> of(Class<C> container, String name, EntityMapping<E> target, String... joinColumnNames) {
        return new AssociationMapping<>(container, name, target, joinColumnNames);
    }
}
