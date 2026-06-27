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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares a callback method for the corresponding lifecycle event.
 * This annotation may be applied to methods of a callback listener
 * class declared via the {@link EntityListener} annotation. Every
 * annotated method must have a single parameter of type
 * {@link EntityManagerFactory}, {@link EntityManager}, or
 * {@link EntityAgent}.
 * <p>
 * The {@code PreClose} callback for an entity manager factory, entity
 * manager, or entity agent occurs after the {@link AutoCloseable#close
 * close()} method of the factory, manager, or agent has been called,
 * but before the factory, manager, or agent is actually destroyed.
 * <p>
 * A {@code PreClose} callback is permitted to call any method of
 * its argument. If the callback throws an exception, the behavior
 * is undefined. Typically, an exception thrown by a {@code PreClose}
 * callback does not abort destruction of the factory, manager, or
 * agent.
 *
 * @since 4.0
 *
 * @see EntityListener
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface PreClose {
}
