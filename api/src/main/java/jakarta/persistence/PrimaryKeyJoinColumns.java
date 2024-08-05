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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the mapping for a composite foreign key which is also a
 * primary key. This annotation groups {@link PrimaryKeyJoinColumn}
 * annotations.
 *
 * <p>Since {@code @PrimaryKeyJoinColumn} is a repeatable annotation,
 * it's not usually necessary to specify {@code @PrimaryKeyJoinColumns}
 * explicitly:
 * {@snippet :
 * @Entity
 * @Table(name = "VCUST")
 * @DiscriminatorValue("VCUST")
 * @PrimaryKeyJoinColumn(name = "CUST_ID",
 *                       referencedColumnName = "ID")
 * @PrimaryKeyJoinColumn(name = "CUST_TYPE",
 *                       referencedColumnName = "TYPE")
 * public class ValuedCustomer extends Customer { ... }
 * }
 *
 * <p>However, {@code @PrimaryKeyJoinColumns} is useful for controlling
 * generation of composite foreign key constraints:
 * {@snippet :
 * @Entity
 * @Table(name = "VCUST")
 * @DiscriminatorValue("VCUST")
 * @PrimaryKeyJoinColumns(
 *         value = {@PrimaryKeyJoinColumn(name = "CUST_ID",
 *                                        referencedColumnName = "ID"),
 *                  @PrimaryKeyJoinColumn(name = "CUST_TYPE",
 *                                        referencedColumnName = "TYPE")},
 *         foreignKey = @ForeignKey(name = "VCUST_CUST_FK"))
 * public class ValuedCustomer extends Customer { ... }
 * }
 *
 * @see ForeignKey
 *
 * @since 1.0
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface PrimaryKeyJoinColumns {

    /**
     * (Optional) The primary key columns that map the relationship.
     * <p>
     * If no {@code @PrimaryKeyJoinColumn}s are specified, the
     * columns are inferred according to the relationship mapping
     * defaults, exactly as if the {@code @PrimaryKeyJoinColumns}
     * annotation was missing. This allows the {@link #foreignKey}
     * to be specified even when the primary key join columns are
     * defaulted.
     */
    PrimaryKeyJoinColumn[] value() default {};

    /**
     * (Optional) Controls generation of the foreign key constraint
     * on these primary key join columns when table generation is in
     * effect.
     * <ul>
     * <li>If both this element and a
     *     {@link PrimaryKeyJoinColumn#foreignKey foreignKey} element
     *     of one of the {@link PrimaryKeyJoinColumn} annotations are
     *     specified, the behavior is undefined.
     * <li>If no {@link PrimaryKeyJoinColumn} annotation is specified
     *     in either location, a default foreign key strategy is
     *     selected by the persistence provider.
     * </ul>
     *
     * @since 2.1
     *
     * @see PrimaryKeyJoinColumn#foreignKey
     */
    ForeignKey foreignKey() default @ForeignKey(ConstraintMode.PROVIDER_DEFAULT);

}
