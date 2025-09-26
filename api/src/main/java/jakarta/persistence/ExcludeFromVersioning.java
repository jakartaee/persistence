/*
 * Copyright (c) 2008, 2024 Oracle and/or its affiliates. All rights reserved.
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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies that mutation of the annotated field or property does
 * not result in incrementation of the {@linkplain Version version
 * field or property} of the entity.
 * <p>
 * This annotation indicates that the application tolerates lost
 * updates to the annotated field or property when concurrent
 * optimistic transactions modify the same entity instance, and
 * that no {@link OptimisticLockException} should occur in this
 * scenario.
 *
 * @see Version
 *
 * @since 4.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ExcludeFromVersioning {}
