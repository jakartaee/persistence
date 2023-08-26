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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a composite primary key class that is mapped to 
 * multiple fields or properties of the annotated entity.
 *
 * <p>The primary key fields of the entity must be annotated
 * {@link Id}, and the specified primary key class must have
 * fields or properties with matching names and types. The
 * mapping of fields or properties of the entity to fields or
 * properties of the primary key class is implicit.
 *
 * <p>Example:
 * <pre>
 *   &#064;IdClass(com.acme.EmployeePK.class)
 *   &#064;Entity
 *   public class Employee {
 *      &#064;Id String empName;
 *      &#064;Id Date birthDay;
 *      ...
 *   }
 * </pre>
 *
 * @see EmbeddedId
 *
 * @since 1.0
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface IdClass {

    /**
     * The primary key class, which must declare fields or
     * properties with names and types that match the
     * {@link Id} fields and properties of the annotated
     * entity class.
     */
    Class<?> value();
}
