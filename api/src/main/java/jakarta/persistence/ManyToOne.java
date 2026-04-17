/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a single-valued association to another entity class that
 * has many-to-one multiplicity. It is not usually necessary to specify
 * the associated {@linkplain #targetEntity target entity} explicitly
 * since it can usually be inferred from the declared type of the
 * annotated field or property.
 *
 * <p>A {@code ManyToOne} association usually maps a foreign key column
 * or columns. This mapping may be specified using the {@link JoinColumn}
 * annotation.
 * {@snippet :
 * @ManyToOne(optional = false)
 * @JoinColumn(name = "CUST_ID")
 * Customer customer;
 * }
 *
 * <p>Alternatively, an optional {@code ManyToOne} association is
 * sometimes mapped to a join table using the {@link JoinTable} annotation.
 * {@snippet :
 * @ManyToOne
 * @JoinTable(name = "ORDER_CUSTOMER",
 *            joinColumns = @JoinColumn(name="ORD_ID"),
 *            inverseJoinColumns = @JoinColumn(name="CUST_ID"))
 * Customer customer;
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
 * <p>In a bidirectional association, a {@code OneToMany} must be the
 * owning side. The inverse side of a bidirectional {@code ManyToOne}
 * association must be a field or property annotated {@link OneToMany
 * &#64;OneToMany} of the {@linkplain #targetEntity target entity}, and
 * the non-owning {@link OneToMany} side must specify the relationship
 * field or property annotated {@code @ManyToOne} via
 * {@link OneToMany#mappedBy mappedBy}. The foreign key or join table
 * must be specified on the owning side.
 * {@snippet :
 * @Entity
 * public class Order {
 *     @Id
 *     @GeneratedValue
 *     int id;
 *
 *     // owning side
 *     @ManyToOne(optional = false,
 *                fetch = LAZY)
 *     @JoinColumn(name = "CUST_ID")
 *     Customer customer;
 *     ...
 * }
 *
 * @Entity
 * public class Customer {
 *     @Id
 *     @GeneratedValue
 *     long id;
 *
 *     // inverse (unowned) side
 *     @OneToMany(mappedBy = Order_.CUSTOMER)
 *     @OrderBy
 *     List<Order> orders;
 *     ...
 * }
 * }
 * <p>The {@code ManyToOne} annotation may be used within an embeddable
 * class to specify a relationship from the embeddable class to an entity
 * class. If the relationship is bidirectional, the non-owning
 * {@link OneToMany} entity side must use the {@code mappedBy} element of
 * the {@code OneToMany} annotation to specify the relationship field or
 * property of the embeddable field or property on the owning side of the
 * relationship. The dot ({@code .}) notation syntax must be used in the
 * {@code mappedBy} element to indicate the relationship attribute within
 * the embedded attribute. The value of each identifier used with the dot
 * notation is the name of the respective embedded field or property.
 * {@snippet :
 * @Entity
 * public class Employee {
 *     @Id
 *     int id;
 *
 *     @Embedded
 *     Job job;
 *     ...
 * }
 *
 * @Embeddable
 * public class Job {
 *     String description;
 *
 *     // owning side
 *     @ManyToOne
 *     ProgramManager manager;
 * }
 *
 * @Entity
 * public class ProgramManager {
 *     @Id
 *     int id;
 *
 *     // inverse (unowned) side
 *     @OneToMany(mappedBy = "job.manager")
 *     Collection<Employee> reports;
 * }
 * }
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface ManyToOne {

    /** 
     * (Optional) The entity class that is the target of 
     * the association. 
     *
     * <p>Defaults to the type of the field or property
     * that stores the association. 
     */
    Class<?> targetEntity() default void.class;

    /**
     * (Optional) The operations that must be cascaded to 
     * the target of the association.
     *
     * <p>By default no operations are cascaded.
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
     * <p>When {@code fetch} is not explicitly specified, the
     * fetching policy is determined by the default fetch
     * type of the persistence unit for one-to-one and
     * many-to-one associations.
     */
    FetchType fetch() default FetchType.DEFAULT;

    /** 
     * (Optional) Whether the association is optional. When
     * {@code optional=false}, the relationship must always exist.
     * The annotated field or property must not be set to a null
     * value when persistence provider reads the state of the entity.
     *
     * <p>If the annotated field or property is also annotated
     * {@code @jakarta.annotation.Nonnull}, then {@code optional=false}
     * is implied, and the value of this annotation member is ignored.
     *
     * <p>May be used in schema generation to infer that the
     * mapped foreign key column is {@link JoinColumn#nullable
     * not null}.
     */
    boolean optional() default true;
}
