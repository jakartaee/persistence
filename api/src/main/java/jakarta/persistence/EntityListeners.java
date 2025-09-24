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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the entity listener classes associated with the
 * annotated class. This annotation may be applied to an
 * {@linkplain  Entity entity class} or to a
 * {@linkplain MappedSuperclass mapped superclass}.
 *
 * <p>Every entity listener class must have a public constructor
 * with no parameters.
 *
 * <p>The specified entity listener classes may have callback
 * methods annotated {@link PrePersist}, {@link PreUpdate},
 * {@link PreRemove}, {@link PostPersist}, {@link PostUpdate},
 * and/or {@link PostRemove}.
 *
 * <p>Entity listener classes in Jakarta EE environments support
 * dependency injection through the Contexts and Dependency
 * Injection API (CDI) when CDI is enabled. An entity listener
 * class that makes use of CDI injection may also define lifecycle
 * callback methods annotated with the {@code PostConstruct} and
 * {@code PreDestroy} annotations. These methods will be invoked
 * after injection has taken place and before the entity listener
 * instance is destroyed, respectively.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface EntityListeners {

    /** The callback listener classes */
    Class<?>[] value();
}
