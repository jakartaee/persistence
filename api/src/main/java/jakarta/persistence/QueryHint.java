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
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Supplies a property or hint via an annotation.
 * <ul>
 * <li>As a nested annotation of
 *     {@link NamedQuery#hints @NamedQuery} or
 *     {@link NamedNativeQuery#hints @NamedNativeQuery},
 *     this annotation specifies a query hint.
 * <li>As a nested annotation of {@link Fetch#hints @Fetch},
 *     it specifies a fetching hint.
 * </ul>
 *
 * <p>If a vendor-specific property or hint is not recognized
 * by the persistence provider, it is silently ignored.
 *
 * @see NamedQuery#hints
 * @see NamedNativeQuery#hints
 * @see Fetch#hints
 *
 * @since 1.0
 */
@Target({}) 
@Retention(RUNTIME)
public @interface QueryHint { 

    /**
     * The name of the hint.
     * <p>
     * This is usually a vendor-specific name defined by the
     * persistence provider.
     */
    String name(); 

    /**
     * The value of the hint, as a string.
     */
    String value();
}
