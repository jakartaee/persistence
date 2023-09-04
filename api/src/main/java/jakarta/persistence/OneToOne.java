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
 * Specifies a single-valued association to another entity class that
 * has one-to-one multiplicity. It is not usually necessary to specify
 * the associated target entity explicitly, since it can usually be
 * inferred from the type of the object being referenced.
 * 
 * <p>If the relationship is bidirectional, the non-owning side must
 * use the {@link #mappedBy} element of the {@code OneToOne} annotation
 * to specify the relationship field or property of the owning side.
 *
 * <p>A {@code OneToOne} association usually maps a unique foreign key
 * relationship, either a foreign key column or columns with a unique
 * constraint, or a relationship via a shared primary key. The
 * {@link JoinColumn} annotation may be used to map the foreign key
 * column or columns. Alternatively, an optional {@code OneToOne}
 * association is sometimes mapped to a join table using the
 * {@link JoinTable} annotation.
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
 *
 * <p>Example 1: One-to-one association that maps a foreign key column
 * <pre>
 *    // On Customer class:
 *
 *    &#064;OneToOne(optional=false)
 *    &#064;JoinColumn(
 *    	name="CUSTREC_ID", unique=true, nullable=false, updatable=false)
 *    public CustomerRecord getCustomerRecord() { return customerRecord; }
 *
 *    // On CustomerRecord class:
 *
 *    &#064;OneToOne(optional=false, mappedBy="customerRecord")
 *    public Customer getCustomer() { return customer; }
 * </pre>
 *
 * <p>Example 2: One-to-one association that assumes both the source and
 * target share the same primary key values.
 * <pre>
 *    // On Employee class:
 *
 *    &#064;Entity
 *    public class Employee {
 *    	&#064;Id Integer id;
 *    
 *    	&#064;OneToOne &#064;MapsId
 *    	EmployeeInfo info;
 *    	...
 *    }
 *
 *    // On EmployeeInfo class:
 *
 *    &#064;Entity
 *    public class EmployeeInfo {
 *    	&#064;Id Integer id;
 *    	...
 *    }
 * </pre>
 *
 * <p>Example 3: One-to-one association from an embeddable class to another
 * entity.
 * <pre>
 *    &#064;Entity
 *    public class Employee {
 *       &#064;Id int id;
 *       &#064;Embedded LocationDetails location;
 *       ...
 *    }
 *
 *    &#064;Embeddable
 *    public class LocationDetails {
 *       int officeNumber;
 *       &#064;OneToOne ParkingSpot parkingSpot;
 *       ...
 *    }
 *
 *    &#064;Entity
 *    public class ParkingSpot {
 *       &#064;Id int id;
 *       String garage;
 *       &#064;OneToOne(mappedBy="location.parkingSpot") Employee assignedTo;
 *        ... 
 *    } 
 *
 * </pre>
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface OneToOne {

    /** 
     * (Optional) The entity class that is the target of the
     * association.
     *
     * <p>Defaults to the type of the field or property
     * that stores the association. 
     */
    Class<?> targetEntity() default void.class;

    /**
     * (Optional) The operations that must be cascaded to the
     * target of the association.
     *
     * <p>By default no operations are cascaded.
     */
    CascadeType[] cascade() default {};

    /** 
     * (Optional) Whether the association should be lazily 
     * loaded or must be eagerly fetched.
     * <ul>
     * <li>The {@link FetchType#EAGER EAGER} strategy is a
     *     requirement on the persistence provider runtime
     *     that the associated entity must be eagerly fetched.
     * <li>The {@link FetchType#LAZY LAZY} strategy is a hint
     *     to the persistence provider runtime.
     * </ul>
     *
     * <p>If not specified, defaults to {@code EAGER}.
     */
    FetchType fetch() default FetchType.EAGER;

    /** 
     * (Optional) Whether the association is optional. If set 
     * to false then a non-null relationship must always exist.
     */
    boolean optional() default true;

    /**
     * (Optional) The field that owns the relationship. This
     * element is only specified on the inverse (non-owning)
     * side of the association.
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
