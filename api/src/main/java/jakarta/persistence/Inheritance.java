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
 * Specifies the inheritance mapping strategy for the entity class
 * hierarchy which descends from the annotated entity class. The
 * inheritance strategy for the whole entity class hierarchy is
 * determined by the mapping of the root class of the hierarchy.
 *
 * <p>There are {@linkplain InheritanceType three basic strategies}
 * which may be used when mapping an entity class hierarchy to tables
 * in a relational database:
 * <ul>
 * <li>With a <b>single table per class hierarchy</b>, every field
 *     declared by any class in the hierarchy maps to a column of the
 *     same table. A subclass shares the table of its superclasses.
 * <li>With a <b>table per concrete entity class</b>, every field
 *     declared or inherited by a given subclass is mapped to the
 *     same table, but each concrete class in the hierarchy has its
 *     own separate table
 * <li>For the <b>joined subclass</b> strategy, any field which is
 *     declared by a subclass is mapped to a separate table from the
 *     fields which are declared by its superclass. Joins may be used
 *     to retrieve all fields declared and inherited by the subclass.
 * </ul>
 *
 * <p>This annotation must be applied to the entity class that is
 * the root of the entity class hierarchy. If the {@code Inheritance}
 * annotation is not specified, or if no inheritance type is specified
 * for an entity class hierarchy, the {@link InheritanceType#SINGLE_TABLE
 * SINGLE_TABLE} mapping strategy is used.
 *
 * <p>Example:
 * {@snippet :
 * @Entity
 * @Inheritance(strategy = JOINED)
 * public class Customer { ... }
 *
 * @Entity
 * public class ValuedCustomer extends Customer { ... }
 * }
 *
 * @see InheritanceType
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Inheritance {

    /**
     * The inheritance mapping strategy for the entity inheritance
     * hierarchy.
     */
    InheritanceType strategy() default InheritanceType.SINGLE_TABLE;
}
