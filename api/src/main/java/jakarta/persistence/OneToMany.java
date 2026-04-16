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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a many-valued association to another entity class that has
 * one-to-many multiplicity. If the target entity class cannot be inferred
 * from the type arguments of the declared type of the annotated field or
 * property, then {@link #targetEntity} must be explicitly specified.
 *
 * <p>A {@code OneToMany} association usually maps a foreign key column
 * or columns in the table of the associated entity. This mapping may
 * be specified using the {@link JoinColumn} annotation.
 * {@snippet :
 * @OneToMany
 * @JoinColumn(name = "ORD_ID)
 * Set<Order> orders;
 * }
 * <p>Alternatively, a unidirectional {@code OneToMany} association is
 * sometimes mapped to a join table using the {@link JoinTable} annotation.
 * {@snippet :
 * @OneToMany
 * @JoinTable(name = "CUSTOMER_ORDERS",
 *            joinColumns = @JoinColumn("CUST_ID"),
 *            inverseJoinColumns = @JoinColumn("ORD_ID"))
 * Set<Order> orders;
 * }
 *
 * <p>The annotated field or property usually represents one side of a
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
 * <p>In a bidirectional association, a {@code OneToMany} must be the
 * inverse side. The owning side of a bidirectional one-to-many
 * association must be a field or property annotated {@link ManyToOne
 * &#64;ManyToOne} of the {@linkplain #targetEntity target entity}, and
 * the non-owning {@code OneToMany} side must specify the relationship
 * field or property annotated {@code @ManyToOne} via {@link #mappedBy}.
 * The foreign key or join table must be specified on the owning side.
 *
 * <p>The {@code OneToMany} annotation may be used within an embeddable
 * class contained within an entity class to specify a relationship to a
 * collection of entities. If the relationship is bidirectional, the
 * {@link #mappedBy} element must be used to specify the relationship
 * field or property of the entity that is the owner of the relationship.
 * {@snippet :
 * @Entity
 * public class Customer {
 *     ...
 *     // inverse (unowned) side
 *     @OneToMany(cascade = ALL,
 *                orphanRemoval = true
 *                mappedBy = Order_.CUSTOMER)
 *     Set<Order> orders;
 * }
 *
 * @Entity
 * public class Order {
 *     ...
 *     // owning side
 *     @ManyToOne(optional = false)
 *     @JoinColumn(name = "CUST_ID")
 *     Customer customer;
 * }
 * }
 * <p>When the collection is a {@link java.util.Map}, the {@link #cascade}
 * element and the {@link #orphanRemoval} element apply to the map value.
 *
 * @see ManyToOne
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface OneToMany {

    /**
     * (Optional) The entity class that is the target of the
     * association. Optional only if the collection property is
     * defined using Java generics. Must be specified otherwise.
     *
     * <p> Defaults to the parameterized type of the collection when
     * defined using generics.
     */
    Class<?> targetEntity() default void.class;

    /** 
     * (Optional) The operations that must be cascaded to the target
     * of the association.
     * <p> Defaults to no operations being cascaded.
     * <p> When the target collection is a {@link java.util.Map},
     * the {@code cascade} element applies to the map value.
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
     */
    String mappedBy() default "";

    /**
     * (Optional) Whether to apply the remove operation to entities
     * that have been removed from the relationship and to cascade
     * the remove operation to those entities.
     * @since 2.0
     */
    boolean orphanRemoval() default false;
}
