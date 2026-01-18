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
import static jakarta.persistence.ConstraintMode.PROVIDER_DEFAULT;

/**
 * Specifies the mapping for composite foreign keys. This annotation groups
 * {@link JoinColumn} annotations for the same relationship.
 *
 * <p>Each {@link JoinColumn} annotation must explicitly specify both
 * {@link JoinColumn#name name} and {@link JoinColumn#referencedColumnName
 * referencedColumnName}.
 *
 * <p>Since {@code @JoinColumn} is a repeatable annotation, it's not usually
 * necessary to specify {@code @JoinColumns} explicitly:
 * {@snippet :
 * @ManyToOne
 * @JoinColumn(name = "ADDR_ID", referencedColumnName = "ID")
 * @JoinColumn(name = "ADDR_ZIP", referencedColumnName = "ZIP")
 * public Address getAddress() { return address; }
 * }
 *
 * <p>However, {@code @JoinColumns} is useful for controlling generation of
 * composite foreign key constraints:
 * {@snippet :
 * @ManyToOne
 * @JoinColumns(
 *         value = {@JoinColumn(name = "ADDR_ID", referencedColumnName = "ID"),
 *                  @JoinColumn(name = "ADDR_ZIP", referencedColumnName = "ZIP")},
 *         foreignKey = @ForeignKey(name = "PERSON_ADDRESS_FK"))
 * public Address getAddress() { return address; }
 * }
 *
 * @see JoinColumn
 * @see ForeignKey
 *
 * @since 1.0
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface JoinColumns {

    /**
     * (Optional) The join columns that map the relationship.
     * <p>
     * If no {@code @JoinColumn}s are specified, the join columns
     * are inferred according to the relationship mapping defaults,
     * exactly as if the {@code @JoinColumns} annotation was missing.
     * This allows the {@link #foreignKey} to be specified when the
     * join columns are defaulted.
     */
    JoinColumn[] value() default {};

    /**
     * (Optional) Controls generation of the foreign key constraint
     * on these join columns when table generation is in effect.
     * <ul>
     * <li>If both this element and a {@link JoinColumn#foreignKey
     *     foreignKey} element of one of the {@link JoinColumn}
     *     annotations are specified, the behavior is undefined.
     * <li>If no {@link ForeignKey} annotation is specified in
     *     either location, a default foreign key strategy is
     *     selected by the persistence provider.
     * </ul>
     *
     * @since 2.1
     *
     * @see JoinColumn#foreignKey
     */
    ForeignKey foreignKey() default @ForeignKey(PROVIDER_DEFAULT);
}
