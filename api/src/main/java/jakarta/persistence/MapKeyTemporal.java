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
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation must be specified for persistent map keys of type 
 * {@link java.util.Date} and {@link java.util.Calendar}. It may only be 
 * specified for map keys of these types.
 * 
 * <p> The <code>MapKeyTemporal</code> annotation can be applied to an
 * element collection or relationship of type <code>java.util.Map</code>
 * in conjunction with the <code>ElementCollection</code>,
 * <code>OneToMany</code>, or <code>ManyToMany</code> annotation.
 *
 * <pre>
 *     Example:
 * 
 *     &#064;OneToMany
 *     &#064;MapKeyTemporal(DATE)
 *     protected java.util.Map&#060;java.util.Date, Employee&#062; employees;
 * </pre>
 *
 * @since 2.0
 *
 * @deprecated Newly-written code should use the date/time types
 *             defined in {@link java.time}.
 */
@Deprecated(since = "3.2")
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface MapKeyTemporal {

    /** (Required) The type used in mapping
     * <code>java.util.Date</code> or
     * <code>java.util.Calendar</code>. 
     */
    TemporalType value();
}

