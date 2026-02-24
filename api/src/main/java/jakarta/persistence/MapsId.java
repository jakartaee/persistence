/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * Designates a {@link ManyToOne} or {@link OneToOne} relationship
 * attribute that provides the mapping for
 * <ul>
 * <li>an {@link Id} or {@link EmbeddedId} attribute of an entity
 *     with a derived identity, or
 *<li> an attribute of the {@link EmbeddedId} class of an entity
 *     with a derived identity.
 * </ul>
 *
 * <p>The {@link #value} element specifies the attribute within a
 * composite key to which the relationship attribute corresponds.
 * If the primary key of the entity is of the same Java type as
 * the primary key of the entity referenced by the relationship,
 * the {@code value} attribute is not specified.
 *
 * <p>In this example, the parent entity has simple primary key:
 * {@snippet :
 * @Entity
 * class Employee {
 *
 *     @Id
 *     long id;
 *
 *     String name;
 *
 *     ...
 * }
 * }
 *
 * <p>And then the dependent entity uses {@link EmbeddedId} to
 * declare its composite primary key:
 * {@snippet :
 * @Embeddable
 * class PaycheckId {
 *     int period;
 *     long empId;  // agrees with primary key type of Employee
 * }
 *
 * @Entity
 * class Paycheck {
 *
 *     @EmbeddedId
 *     PaycheckId id;
 *
 *     @MapsId("empId")  // maps the empId attribute of DependentId
 *     @ManyToOne
 *     Employee emp;
 *
 *     ...
 * }
 * }
 *
 * <p>Alternatively, the dependent entity may use {@link IdClass}
 * to declare its composite primary key:
 * {@snippet :
 * class PaycheckId {
 *     int period;
 *     long empId;  // agrees with empId field of Dependent
 * }
 *
 * @Entity
 * @IdClass(PaycheckId.class)
 * class Paycheck {
 *
 *     @Id
 *     int period;
 *
 *     @Id
 *     long empId;  // agrees with primary key type of Employee
 *
 *     @MapsId("empId")  // maps the empId attribute
 *     @ManyToOne
 *     Employee emp;
 *
 *     ...
 * }
 * }
 *
 * <p>If a {@link ManyToOne} or {@link OneToOne} relationship
 * declared by a dependent entity is annotated {@link MapsId},
 * an instance of the entity cannot be made persistent until
 * the relationship has been assigned a reference to an
 * instance of the parent entity, since the identity of the
 * dependent entity declaring the relationship is derived from
 * the referenced parent entity.
 *
 * @since 2.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface MapsId {

    /**
     * (Optional) The name of the attribute within the composite
     * key to which the relationship attribute corresponds. If
     * not explicitly specified, the relationship maps the primary
     * key of the entity.
     */
    String value() default "";
}
