/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * The simplest type of mapping to a database column. The {@code Basic}
 * annotation may be applied to a persistent property or instance variable
 * whose type is any of the following:
 * <ul>
 * <li>Java primitive types, and wrappers of the primitive types,
 * <li>{@link String}
 * <li>{@link java.math.BigInteger} and {@link java.math.BigDecimal},
 * <li>{@link java.time.LocalDate}, {@link java.time.LocalTime},
 * {@link java.time.LocalDateTime}, {@link java.time.OffsetTime},
 * {@link java.time.OffsetDateTime}, {@link java.time.Instant},
 * and {@link java.time.Year}
 * <li>{@link java.util.Date}, {@link java.util.Calendar},
 * <li>{@code java.sql.Date}, {@code java.sql.Time}, {@code java.sql.Timestamp},
 * <li>{@code byte[]}, {@code Byte[]}, {@code char[]}, {@code Character[]},
 * <li>enum types, and
 * <li>any other type that implements {@link java.io.Serializable}.
 * </ul>
 *
 * <p> The use of the {@code Basic} annotation is optional for persistent
 * fields and properties of these types. If the {@code Basic} annotation is
 * not specified for such a field or property, the default values of the
 * {@code Basic} annotation apply.
 *
 * <p>Example 1:
 * <pre>
 *    &#064;Basic
 *    protected String name;
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *    &#064;Basic(fetch=LAZY)
 *    protected String getName() { return name; }
 * </pre>
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface Basic {

    /**
     * (Optional) Whether the value of the field or property
     * should be lazily loaded or must be eagerly fetched.
     * <ul>
     * <li>The {@link FetchType#EAGER EAGER} strategy is a
     *    requirement on the persistence provider runtime
     *    that the associated entity must be eagerly fetched.
     * <li>The {@link FetchType#LAZY LAZY} strategy is a hint
     *    to the persistence provider runtime.
     * </ul>
     *
     * <p>If not specified, defaults to {@code EAGER}.
     */
    FetchType fetch() default FetchType.EAGER;

    /**
     * (Optional) Defines whether the value of the field or
     * property may be null. This is a hint and is disregarded
     * for primitive types; it may be used in schema generation.
     *
     * <p>If not specified, defaults to {@code true}.
     */
    boolean optional() default true;
}
