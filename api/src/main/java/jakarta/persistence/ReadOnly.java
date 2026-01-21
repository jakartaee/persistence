/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that the annotated entity class is read-only,
 * meaning that mutations to its fields and properties are
 * not normally made persistent.
 * <p>
 * If an instance of a read-only entity is modified while
 * the instance is in the managed state and associated with
 * a persistence context, the behavior is undefined. Such
 * modifications might be lost; they might be made persistent;
 * or the provider might throw an exception. Portable
 * applications should not depend on such provider-specific
 * behavior.
 * <p>
 * Many write operations are permitted for read-only entities.
 * A new instance of a read-only entity may be made persistent
 * by calling {@link EntityManager#persist}. A managed instance
 * may be removed by calling {@link EntityManager#remove}. And
 * a new or detached instance may be passed to any appropriate
 * operation of {@link EntityAgent}, including {@code insert()},
 * {@code update()}, {@code upsert()}, and {@code delete()}.
 * <p>
 * The effect of this annotation may be selectively disabled
 * for a given managed instance of a read-only entity class by
 * passing the instance to {@link EntityManager#enableFlush}.
 * The {@link ReadOnly} annotation has no effect at all on an
 * {@code EntityAgent}.
 * <p>
 * The provider is not required to detect modifications to
 * read-only entities and is encouraged to avoid tracking
 * the state of read-only entities whenever it might result
 * in a significant cost to performance.
 *
 * @see EntityManager#enableFlush(Object)
 *
 * @since 4.0
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ReadOnly {
}
