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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Explicitly specifies the {@linkplain AccessType access type} of:
 * <ul>
 * <li>an entity class, mapped superclass, or embeddable class,
 *     independent of the default access type of the entity class
 *     hierarchy to which it belongs, or
 * <li>a field or property of an entity class, mapped superclass,
 *     or embeddable class with an explicit access type.
 * </ul>
 * <p>
 * The explicit access type of an entity class, mapped superclass,
 * or embeddable class is specified by annotating the class itself.
 * <ul>
 * <li>{@link AccessType#FIELD @Access(FIELD)} specifies that mapping
 *     annotations are placed on the instance variables of the annotated
 *     class, and that the persistence provider runtime accesses the
 *     persistent state of the class via direct access to its instance
 *     variables. Every non-{@code transient} instance variable not
 *     annotated with the {@link Transient} annotation is persistent.
 *
 * <li>{@link AccessType#PROPERTY @Access(PROPERTY)} specifies that
 *     mapping annotations are placed on the property getter methods of
 *     the annotated class, and that the persistence provider runtime
 *     accesses the persistent state of the class via its property
 *     accessor methods. Every property whose getter method is not
 *     annotated with the {@link Transient} annotation is persistent.
 * </ul>
 *
 * <p>The explicit access type may be overridden at the attribute level.
 * That is, a class which explicitly specifies an access type using the
 * {@code @Access} annotation may also have fields or properties annotated
 * {@code @Access}, and so the class may have a mix of access types.
 *
 * <ul>
 * <li>When {@code @Access(FIELD)} is specified at the class level, an
 *     individual attribute within the class may be selectively declared
 *     as persistent with property access by annotating a property getter
 *     {@code @Access(PROPERTY)}. Mapping annotations for this attribute
 *     must be placed on the annotated getter.
 *
 * <li>When {@code @Access(PROPERTY)} is specified at the class level,
 *     an individual attribute within the class may be selectively declared
 *     as persistent with field access by annotating an instance variable
 *     {@code @Access(FIELD)}. Mapping annotations for this attribute must
 *     be placed on the annotated field.
 * </ul>
 *
 * @apiNote This annotation is not usually required. The default access
 * type of an entity class hierarchy is automatically determined by the
 * placement of mapping annotations on the attributes of the entity
 * classes and mapped superclasses which do not explicitly specify an
 * access type. The {@code @Access} annotation is only useful when field
 * and property access are mixed within a single entity class hierarchy.
 *
 * @since 2.0
 *
 * @see AccessType
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Access {
    /**
     * The access type.
     */
    AccessType value();
}
