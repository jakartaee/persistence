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
 * Specifies the mapping of a map key of entity type to a
 * composite foreign key. This annotation groups
 * {@link MapKeyJoinColumn} annotations mapping the columns
 * belonging to the foreign key.
 *
 * <p>When a {@code MapKeyJoinColumns} annotation contains
 * more than one {@code MapKeyJoinColumn} annotation, both
 * the {@linkplain MapKeyJoinColumn#name name} and the
 * {@linkplain MapKeyJoinColumn#referencedColumnName
 * referenced column name} must be explicitly specified by
 * every {@code MapKeyJoinColumn} annotation.
 *
 * <p>Since {@code @MapKeyJoinColumn} is a repeatable annotation,
 * it's not usually necessary to specify {@code @MapKeyJoinColumns}
 * explicitly. However, {@code @MapKeyJoinColumns} is useful for
 * controlling generation of composite foreign key constraints.
 *
 * @see MapKeyJoinColumn
 * @see ForeignKey
 * 
 * @since 2.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface MapKeyJoinColumns {
    /**
     * (Required) The map key join columns that are used to map to
     * the entity that is the map key.
     */
    MapKeyJoinColumn[] value();

    /**
     * (Optional) Used to specify or control the generation of a
     * foreign key constraint when table generation is in effect.
     * If both this element and the {@code foreignKey} element of
     * any of the {@link MapKeyJoinColumn} elements are specified,
     * the behavior is undefined. If no {@code foreignKey}
     * annotation element is specified in either location, a
     * default foreign key strategy is selected by the
     * persistence provider.
     *
     * @since 2.1
     */
    ForeignKey foreignKey() default @ForeignKey(PROVIDER_DEFAULT);
}
