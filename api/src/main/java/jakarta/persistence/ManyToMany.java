/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a many-valued association with many-to-many multiplicity,
 * mapping to an intermediate table called the <em>join table</em>.
 * If the target entity class cannot be inferred from the type arguments
 * of the declared type of the annotated field or property, then
 * {@link #targetEntity} must be explicitly specified.
 *
 * <p>The optional {@link JoinTable} annotation specifies the mapped join
 * table.
 * {@snippet :
 * @ManyToMany
 * @JoinTable(name = "BOOK_AUTHOR",
 *            joinColumns = @JoinColumn(name = "BOOK_ISBN",
 *                                      referencedColumnName = "ISBN"),
 *            inverseJoinColumns = @JoinColumn(name = "AUTHOR_SSN",
 *                                             referencedColumnName = "SSN"))
 * Set<Author> authors;
 * }
 *
 * <p>The annotated field or property might represent one side of a
 * <em>bidirectional</em> association. Every bidirectional association
 * has an <em>owning</em> side and an <em>inverse</em> (alternatively,
 * <em>non-owning</em> or <em>unowned</em>) side. Modifications to the
 * owning side of an association determine the updates made to the
 * relationship in the database. If the inverse side of an association
 * is modified without a corresponding modification to the owning side,
 * the behavior is undefined. The persistence provider is permitted to
 * ignore any modification made only to the inverse side of a
 * bidirectional association.
 *
 * <p>The inverse side of a bidirectional {@code ManyToMany} association
 * must be a field or property also annotated {@code @ManyToMany} of the
 * {@linkplain #targetEntity target entity}, and the inverse side must
 * specify the owning relationship field or property via {@link #mappedBy}.
 * The join table must be specified on the owning side.
 * {@snippet :
 * @Entity
 * public class Book {
 *     @Id
 *     String isbn;
 *
 *     // owning side
 *     @ManyToMany
 *     @JoinTable(name = "BOOK_AUTHOR")
 *     Set<Author> authors;
 *     ...
 * }
 *
 * @Entity
 * public class Author {
 *     @Id
 *     String ssn;
 *
 *     // inverse (unowned) side
 *     @ManyToMany(mappedBy = Book_.AUTHORS)
 *     Set<Customer> customers;
 *     ...
 * }
 * }
 *
 * <p>The {@code ManyToMany} annotation may be used within an
 * embeddable class contained within an entity class to specify a
 * relationship to a collection of entities. If the relationship is
 * bidirectional and the entity containing the embeddable class is
 * the owner of the relationship, the non-owning side must use the
 * {@link #mappedBy} element of the {@code ManyToMany} annotation to
 * specify the relationship field or property of the embeddable class.
 * The dot ({@code .}) notation syntax must be used in the
 * {@link #mappedBy} element to indicate the relationship attribute
 * within the embedded attribute. The value of each identifier used
 * with the dot notation is the name of the respective embedded field
 * or property.
 *
 * @see JoinTable
 *
 * @since 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ManyToMany {

    /**
     * (Optional) The entity class that is the target of the
     * association. Required if the annotated field or
     * property is declared using a raw collection type.
     *
     * <p>Defaults to the type argument of the collection
     * type when the declared type of the annotated field or
     * property is a non-raw collection type.
     */
    Class<?> targetEntity() default void.class;

    /** 
     * (Optional) The operations that must be cascaded to the
     * target of the association.
     *
     * <p>When the target collection is a {@link java.util.Map},
     * the {@code cascade} element applies to the map value.
     *
     * <p>By default, no operations are cascaded.
     */
    CascadeType[] cascade() default {};

    /**
     * (Optional) Whether the association should be lazily
     * loaded or must be eagerly fetched.
     * <ul>
     * <li>The {@link FetchType#EAGER EAGER} policy is a
     *     requirement on the persistence provider runtime
     *     that the associated entity must be eagerly fetched.
     * <li>The {@link FetchType#LAZY LAZY} policy is a hint
     *     to the persistence provider runtime.
     * </ul>
     *
     * <p>If not specified, defaults to {@code LAZY}.
     */
    FetchType fetch() default FetchType.LAZY;

    /** 
     * The field that owns the relationship. Required unless 
     * the relationship is unidirectional.
     *
     * <p>The static metamodel of the target entity may be
     * used to obtain a reference to the owning side, for
     * example, {@code mappedBy = Book_.AUTHORS}.
     */
    String mappedBy() default "";
}
