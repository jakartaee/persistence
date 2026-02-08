/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package jakarta.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares an entity listener class and specifies the entity
 * classes to which it applies. The entity listener receives
 * entity lifecycle callbacks for every entity class belonging
 * to the persistence unit and assignable to one of its declared
 * entity lifecycle callback methods. It is not necessary to
 * separately declare the entity classes to which the listener
 * applies.
 * <p>
 * This annotation is an alternative to {@link EntityListeners}.
 * Callback methods of listeners declared using this annotation
 * are invoked before callback methods of listeners declared
 * using {@link EntityListeners}. However, there is no defined
 * ordering among listeners declared using this annotation. If
 * the order of invocation of callback methods is important,
 * the entity listeners should be declared using the
 * {@link EntityListeners} annotation instead.
 * <p>
 * Every entity listener class must have a public constructor
 * with no parameters.
 * {@snippet :
 * @EntityListener(Book.class)
 * class BookObserver { ... }
 * }
 * <p>
 * The annotated class may have callback methods annotated with
 * any of the standard lifecycle callback annotations:
 * <ul>
 * <li>{@link PostLoad},
 * <li>{@link PrePersist}, {@link PostPersist},
 *     {@link PreRemove}, {@link PostRemove}, and {@link PreMerge},
 * <li>{@link PreInsert}, {@link PostInsert}, {@link PreUpdate},
 *     {@link PostUpdate}, {@link PreUpsert}, {@link PostUpsert},
 *     {@link PreDelete}, and {@link PostDelete}.
 * </ul>
 * <p>
 * A callback method declared by an entity listener class must
 * have the signature {@code void method(E entity)} where {@code E}
 * is an entity class, a mapped superclass, or a supertype of the
 * entity class or mapped superclass to which the entity listener
 * applies. If multiple entity classes are assignable to the type
 * {@code E}, the callback method is invoked for any such class to
 * which the entity listener applies.
 * {@snippet :
 * @EntityListener
 * class BookObserver {
 *
 *     @PostPersist
 *     void newBook(Book book) {
 *         ...
 *     }
 *
 * }
 * }
 * <p>
 * An entity listener class may have multiple callback methods for
 * a given type of lifecycle event, but at most one callback method
 * for a given type of event and given parameter type.
 * {@snippet :
 * @EntityListener
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
 * <p>
 * Entity listener classes in Jakarta EE environments support
 * dependency injection through the Contexts and Dependency
 * Injection (CDI) API when CDI is enabled. An entity listener
 * class that makes use of CDI injection may also define lifecycle
 * callback methods annotated with the {@code PostConstruct} and
 * {@code PreDestroy} annotations. These methods are called after
 * dependencies have been injected and before the entity listener
 * instance is destroyed, respectively.
 *
 *
 * @since 4.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Discoverable
public @interface EntityListener {
}
