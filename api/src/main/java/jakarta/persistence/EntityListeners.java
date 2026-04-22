/*
 * Copyright (c) 2008, 2026 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.persistence.spi.Discoverable;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the entity listener classes associated with the
 * annotated class, package, or module. This annotation may
 * be applied:
 * <ul>
 * <li>directly to an {@linkplain Entity entity class} or
 *     {@linkplain MappedSuperclass mapped superclass}, to
 *     specify its listener classes,
 * <li>to a package descriptor to specify listener classes
 *     applying to every entity contained in the package, or
 * <li>to a module descriptor to specify listener classes
 *     applying to every entity contained in any package
 *     belonging to the module.
 * </ul>
 *
 * <p>This annotation is an alternative to {@link EntityListener}.
 * Entity listeners declared using this annotation do not need to
 * be annotated {@code @EntityListener}. Callback methods of
 * listeners declared using this annotation are invoked after
 * callback methods of listeners declared using {@code EntityListener}.
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
 * methods annotated with any of the standard lifecycle callback
 * annotations:
 * <ul>
 * <li>{@link PostLoad},
 * <li>{@link PrePersist}, {@link PostPersist},
 *     {@link PreRemove}, {@link PostRemove}, and {@link PreMerge},
 * <li>{@link PreInsert}, {@link PostInsert}, {@link PreUpdate},
 *     {@link PostUpdate}, {@link PreUpsert}, {@link PostUpsert},
 *     {@link PreDelete}, and {@link PostDelete}.
 * </ul>
 * <p>A callback method declared by an entity listener class must
 * have the signature {@code void method(E entity)} where {@code E}
 * is an entity class, a mapped superclass, or a supertype of the
 * entity class or mapped superclass to which the entity listener
 * applies. If multiple entity classes are assignable to the type
 * {@code E}, the callback method is invoked for any such class to
 * which the entity listener applies.
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
 * <p>An entity listener class may have multiple callback methods
 * for a given type of lifecycle event, but at most one callback
 * method for a given type of event and given parameter type.
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
 *
 * @see EntityListener
 */
@Target({TYPE, PACKAGE, MODULE})
@Retention(RUNTIME)
@Discoverable
public @interface EntityListeners {

    /**
     * The callback listener classes for the annotated entity,
     * in the order in which their lifecycle callback methods
     * are invoked.
     */
    Class<?>[] value();
}
