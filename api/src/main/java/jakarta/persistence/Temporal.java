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
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation must be specified for persistent fields 
 * or properties of type <code>java.util.Date</code> and 
 * <code>java.util.Calendar</code>. It may only be specified for fields 
 * or properties of these types.
 * 
 * <p> The <code>Temporal</code> annotation may be used in 
 * conjunction with the {@link Basic} annotation, the {@link Id}
 * annotation, or the {@link ElementCollection} annotation when
 * the element collection value is of such a temporal type.
 *
 * <pre>
 *     Example:
 * 
 *     &#064;Temporal(DATE)
 *     protected java.util.Date endDate;
 * </pre>
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface Temporal {

    /** The type used in mapping <code>java.util.Date</code> or <code>java.util.Calendar</code>. */
    TemporalType value();
}
