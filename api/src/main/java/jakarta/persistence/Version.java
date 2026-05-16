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
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares the version field or property of an entity class, which is
 * used to detect optimistic locking conflicts, ensuring the integrity
 * of optimistic transactions. The version field or property holds a
 * version number or timestamp identifying the revision of the entity
 * data held by an entity class instance.
 *
 * <p>An {@linkplain OptimisticLockException optimistic locking conflict}
 * occurs when verification of the version or timestamp fails during an
 * attempt to update the entity, that is, if the version or timestamp
 * held in the database changes between reading the state of an entity
 * instance and attempting to update or delete the state of the instance.
 *
 * <p>The version attribute must be of one of the following basic
 * types: {@code int}, {@link Integer}, {@code short}, {@link Short},
 * {@code long}, {@link Long}, {@code java.sql.Timestamp},
 * {@link java.time.Instant}, {@link java.time.LocalDateTime}.
 *
 * <p>This field declares a version number:
 *
 * {@snippet :
 * @Version
 * @Column(name = "REVISION")
 * protected int version;
 * }
 *
 * <p>This field declares a revision timestamp:
 *
 * {@snippet :
 * @Version
 * @Column(name = "LAST_UPDATED")
 * private Instant lastUpdated;
 * }
 *
 * <p>An entity class should have at most one {@code Version} field
 * or property. The version field or property should be declared by
 * the root entity class in an entity class hierarchy, or by one of
 * its mapped superclasses.
 *
 * <p>The {@code Version} field or property should be mapped to the
 * primary table of the entity.
 *
 * <p>The entity version must be updated by the persistence provider each
 * time the state of an entity instance is written to the database.
 * Furthermore, if the current persistence context contains a revision
 * of the entity instance when the instance is written to the database,
 * the persistence provider must verify that the revision held in the
 * persistence context is identical to the revision held in the database
 * by comparing the versions held in memory and in the database.
 * If the versions do not match, the persistence provider must throw an
 * {@link OptimisticLockException}.
 *
 * <p>The persistence provider is permitted to use the value of the
 * identifier and version fields or properties of an entity instance
 * to determine whether the instance is new or detached.
 *
 * @see LockModeType
 * @see PersistenceUnitUtil#getVersion(Object)
 * @see ExcludedFromVersioning
 *
 * @since 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Version {}
