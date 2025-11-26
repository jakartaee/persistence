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
 * {@snippet :
 * @Entity
 * @EntityListeners(BookObserver.class)
 * class Book { ... }
 * }
 *
 * <p>The specified entity listener classes may have callback
 * methods annotated {@link PrePersist}, {@link PreUpdate},
 * {@link PreRemove}, {@link PostPersist}, {@link PostUpdate},
 * {@link PostRemove}, and/or {@link PostLoad}. A callback
 * method defined by an entity listener class must have the
 * signature {@code void method(E entity)} where {@code E} is
 * an entity class, a mapped superclass, or a supertype of
 * the entity class or mapped superclass to which the entity
 * listener applies. If multiple entity classes are assignable
 * to the type {@code E}, the callback method is invoked for
 * any such class to which the entity listener applies.
 * {@snippet :
 * class BookObserver {
 *
 *     @PostPersist
 *     void newBook(Book book) {
 *         ...
 *     }
 *
 * }
 * }
 *
 * <p>An entity listener class may have multiple callback
 * methods for a given type of lifecycle event, but at most
 * one callback method for a given type of event and given
 * parameter type.
 * {@snippet :
 * class Observer {
 *
 *     // called only for Books
 *     @PostPersist
 *     void newBook(Book book) {
 *         ...
 *     }
 *
 *     // called only for Authors
 *     @PostPersist
 *     void newAuthor(Author author) {
 *         ...
 *     }
 *
 *     // called for any entity type to which the listener applies
 *     @PostLoad
 *     void entityLoaded(Object entity) {
 *         ...
 *     }
 *
 * }
 * }
 *
 * <p>Entity listener classes in Jakarta EE environments support
 * dependency injection through the Contexts and Dependency
 * Injection (CDI) API when CDI is enabled. An entity listener
 * class that makes use of CDI injection may also define lifecycle
 * callback methods annotated with the {@code PostConstruct} and
 * {@code PreDestroy} annotations. These methods are called after
 * dependencies have been injected and before the entity listener
 * instance is destroyed, respectively.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface EntityListeners {

    /**
     * The callback listener classes for the annotated entity.
     */
    Class<?>[] value();
}
