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
//     Gavin King       - 4.0

package jakarta.persistence.query;

import jakarta.persistence.QueryHint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Configures options that affect the execution of a
 * database write operation. This annotation may be applied
 * to:
 * <ul>
 * <li>a method with a {@link StaticQuery} or Jakarta Data
 *     {@code jakarta.data.repository.Query} annotation
 *     whose {@code value} member suplies an {@code UPDATE}
 *     or {@code DELETE} statement,
 * <li>a method with a {@link StaticNativeQuery} annotation
 *     whose {@code value} member specifies a SQL operation
 *     that returns a row count, or
 * <li>a Jakarta Data repository method annotated
 *     {@code jakarta.data.repository.Delete} that uses the
 *     parameter-based automatic query pattern.
 * </ul>
 *
 * <p>This annotation must be respected by an implementation
 * of Jakarta Data backed by Jakarta Persistence.
 *
 * @see QueryOptions
 *
 * @since 4.0
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface StatementOptions {
    /**
     * A query timeout in milliseconds.
     * By default, there is no timeout.
     * @see jakarta.persistence.Statement#setTimeout(Integer)
     */
    int timeout() default -1;

    /**
     * Query properties and hints.
     * May include vendor-specific query hints.
     * @see jakarta.persistence.Statement#setHint(String, Object)
     */
    QueryHint[] hints() default {};
}