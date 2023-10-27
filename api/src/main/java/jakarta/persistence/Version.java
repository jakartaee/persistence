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
 * Specifies the version field or property of an entity class that
 * is used to detect optimistic lock failures. The version is used
 * to ensure integrity when performing the merge operation and for
 * optimistic concurrency control.
 *
 * <p>There should be no more than one {@code Version} property or
 * field per class; entities with more than one {@code Version}
 * property or field are not portable.
 * 
 * <p> The {@code Version} property should be mapped to the primary
 * table for the entity class; entities that map the {@code Version}
 * property to a table other than the primary table are not portable.
 * 
 * <p>The version property should have one of the following types:
 * {@code int}, {@link Integer}, {@code short}, {@link Short},
 * {@code long}, {@link Long}, {@code java.sql.Timestamp},
 * {@link java.time.Instant}, {@link java.time.LocalDateTime}.
 *
 * <p>Example:
 * <pre>
 *    &#064;Version
 *    &#064;Column(name="OPTLOCK")
 *    protected int getVersionNum() { return versionNum; }
 * </pre>
 *
 * @since 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Version {}
