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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a single-valued association to another entity class that
 * has one-to-one multiplicity. It is not usually necessary to specify
 * the associated {@linkplain #targetEntity target entity} explicitly
 * since it can usually be inferred from the declared type of the
 * annotated field or property.
 *
 * <p>A {@code OneToOne} association usually maps a unique foreign key
 * relationship, either:
 * <ul>
 * <li>a foreign key column or columns with a unique constraint, or
 * <li>a relationship via a shared primary key.
 * </ul>
 * <p>The {@link JoinColumn} annotation may be used to map the foreign
 * key column or columns.
 *
 * <p>Here, a one-to-one association maps a foreign key column:
 * {@snippet :
 * @Entity
 * public class Employee {
 *     @Id
 *     String employeeId;
 *
 *     @OneToOne(optional = false)
 *     @JoinColumn(name = "INFO_ID", unique = true)
 *     EmployeeInfo info;
 *     ...
 * }
 * }
 *
 * <p>Here, the association is defined by a shared primary key:
 * {@snippet :
 * @Entity
 * public class Employee {
 *     @Id
 *     String employeeId;
 *
 *     @OneToOne
 *     @MapsId
 *     EmployeeInfo info;
 *     ...
 * }
 *
 * @Entity
 * public class EmployeeInfo {
 *     @Id
 *     String employeeId;
 *     ...
 * }
 * }
 *
 * <p>Alternatively, an optional {@code OneToOne} association is
 * sometimes mapped to a join table using the {@link JoinTable}
 * annotation.
 * {@snippet :
 * @Entity
 * public class Person {
 *     @Id
 *     String ssn;
 *
 *     @OneToOne
 *     @JoinTable(name = "PERSON_SPOUSE",
 *                joinColumns = @JoinColumn(name = "PERSON_SSN"),
 *                inverseJoinColumns = @JoinColumn(name = "SPOUSE_SSN"))
 *     Person spouse;
 *     ...
 * }
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
 * <p>The inverse side of a bidirectional {@code OneToOne} association
 * must be a field or property also annotated {@code @OneToOne} of the
 * {@linkplain #targetEntity target entity}, and the inverse side
 * must specify the owning relationship field or property via
 * {@link #mappedBy}. The foreign key or join table must be specified
 * on the owning side.
 * {@snippet :
 * @Entity
 * public class Customer {
 *     @Id @GeneratedValue
 *     Long id;
 *
 *     // owning side
 *     @OneToOne(optional = false)
 *     @JoinColumn(name = "DETAILS_ID", unique = true)
 *     CustomerDetails customerDetails;
 *     ...
 * }
 *
 * @Entity
 * public class CustomerDetails {
 *     @Id @GeneratedValue
 *     Long id;
 *
 *     // inverse (unowned) side
 *     @OneToOne(optional = false,
 *               mappedBy = Customer_.CUSTOMER_DETAILS)
 *     Customer customer;
 *     ...
 * }
 * }
 *
 * <p>The {@code OneToOne} annotation may be used within an embeddable
 * class to specify a relationship from the embeddable class to an
 * entity class. If the relationship is bidirectional and the entity
 * containing the embeddable class is on the owning side of the
 * relationship, the non-owning side must use the {@link #mappedBy}
 * element of the {@code OneToOne} annotation to specify the relationship
 * field or property of the embeddable class. The dot ({@code .}) notation
 * syntax must be used in the {@link #mappedBy} element to indicate the
 * relationship attribute within the embedded attribute. The value of
 * each identifier used with the dot notation is the name of the
 * respective embedded field or property.
 * {@snippet :
 * @Entity
 * public class Employee {
 *     @Id
 *     int id;
 *
 *     @Embedded
 *     LocationDetails location;
 *     ...
 * }
 *
 * @Embeddable
 * public class LocationDetails {
 *     int officeNumber;
 *
 *     // owning side
 *     @OneToOne
 *     ParkingSpot parkingSpot;
 *     ...
 * }
 *
 * @Entity
 * public class ParkingSpot {
 *     @Id
 *     int id;
 *
 *     String garage;
 *
 *     // inverse (unowned) side
 *     @OneToOne(mappedBy = "location.parkingSpot")
 *     Employee assignedTo;
 *     ...
 * }
 * }
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface OneToOne {

    /**
     * (Optional) The entity class that is the target of
     * the association. Required if the target entity type
     * cannot be inferred from the declared type of the
     * annotated field or property.
     *
     * <p>Defaults to the declared type of the annotated
     * field or property.
     */
    Class<?> targetEntity() default void.class;

    /**
     * (Optional) The operations that must be cascaded to
     * the target of the association.
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
     * <p>When {@code fetch} is not explicitly specified, the
     * fetching strategy is determined by the default fetch
     * type of the persistence unit for one-to-one and
     * many-to-one associations.
     */
    FetchType fetch() default FetchType.DEFAULT;

    /** 
     * (Optional) Whether the association is optional.
     * <ul>
     * <li>By default, the association is optional, and the
     *     annotated field or property may be set to a null
     *     value.
     * <li>When {@code optional=false}, a non-null value must
     *     be assigned to the non-optional field or property
     *     before the persistence provider attempts to write
     *     the state of entity instance to the database.
     * </ul>
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

    /**
     * The field of property of the target entity that owns
     * the relationship and specifies its mapping to the
     * database. This element is only specified on the
     * inverse (non-owning) side of the association.
     *
     * <p>The static metamodel of the target entity may be
     * used to obtain a reference to the owning side, for
     * example, {@code mappedBy = Customer_.CUSTOMER_DETAILS}.
     */
    String mappedBy() default "";

    /**
     * (Optional) Whether to apply the remove operation to
     * entities that have been removed from the relationship
     * and to cascade the remove operation to those entities.
     * @since 2.0
     */
    boolean orphanRemoval() default false;
}
