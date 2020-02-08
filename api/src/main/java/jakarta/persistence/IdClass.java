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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies a composite primary key class that is mapped to 
 * multiple fields or properties of the entity.  
 *
 * <p> The names of the fields or properties in the primary key 
 * class and the primary key fields or properties of the entity 
 * must correspond and their types must be the same.
 *
 * <pre>
 *
 *   Example:
 *
 *   &#064;IdClass(com.acme.EmployeePK.class)
 *   &#064;Entity
 *   public class Employee {
 *      &#064;Id String empName;
 *      &#064;Id Date birthDay;
 *      ...
 *   }
 * </pre>
 *
 * @since 1.0
 */
@Target({TYPE})
@Retention(RUNTIME)

public @interface IdClass {

    /** Primary key class */
    Class value();
}
