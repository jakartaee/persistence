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
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies the primary key field or property of an entity class.
 * Every entity must have a primary key. The primary key must be
 * declared by:
 * <ul>
 * <li>the entity class that is the root of the entity hierarchy, or
 * <li>a mapped superclass that is a (direct or indirect) superclass
 *     of all entity classes in the entity hierarchy.
 * </ul>
 * A primary key must be defined exactly once in each entity hierarchy.
 *
 * <p>The field or property to which the {@code Id} annotation is
 * applied should have one of the following types:
 * <ul>
 * <li>a Java primitive type, or wrapper of a primitive type,
 * <li>{@link String},
 * <li>{@link java.util.UUID},
 * <li>{@link java.time.LocalDate},
 * <li>{@link java.util.Date},
 * <li>{@link java.sql.Date},
 * <li>{@link java.math.BigDecimal}, or
 * <li>{@link java.math.BigInteger}.
 * </ul>
 *
 * <p>The mapped column for the primary key of the entity is assumed 
 * to be the primary key of the primary table. If no {@link Column}
 * annotation is specified, the primary key column name is assumed to
 * be the name of the primary key property or field.
 *
 * <p>For example, this field holds a generated primary key assigned
 * by the persistence provider:
 * {@snippet :
 * @Id @GeneratedValue
 * UUID uuid;
 * }
 *
 * <p>This property holds a primary key assigned by the application:
 * {@snippet :
 * @Id
 * public String getIsbn() {
 *     return isbn;
 * }
 *
 * public void setIsbn(String isbn) {
 *     this.isbn = isbn;
 * }
 * }
 * <p>An entity must declare or inherit exactly one field or property
 * annotated {@code @Id} or {@link EmbeddedId @EmbeddedId} unless the
 * entity specifies an {@link IdClass @IdClass}, in which case the
 * entity must have a field or property annotated {@code @Id} for
 * each field or property of its id class.
 *
 * <p>The {@code @Id} annotation should never be applied to a field
 * or property of an embeddable id or id class.
 *
 * @see Column
 * @see GeneratedValue
 * @see EmbeddedId
 * @see IdClass
 * @see PersistenceUnitUtil#getIdentifier(Object)
 *
 * @since 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Id {}
