/*
 * Copyright (c) 2008, 2025 Oracle and/or its affiliates. All rights reserved.
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
//     Gavin King      - 4.0

package jakarta.persistence.sql;

import jakarta.persistence.metamodel.SingularAttribute;

/**
 * Maps columns of a JDBC {@link java.sql.ResultSet} to a given
 * {@linkplain jakarta.persistence.Embedded embedded object}.
 *
 * @param container The Java class which declares the field
 *                  holding the embedded object
 * @param embeddableClass The embeddable class
 * @param name The name of the field holding the embedded object
 * @param fields Mappings for fields or properties of the entity
 * @param <T> The embeddable type
 * @param <C> The container type
 *
 * @since 4.0
 */
public record EmbeddedMapping<C,T>
        (Class<C> container, Class<T> embeddableClass, String name, MemberMapping<?>[] fields)
        implements MemberMapping<C> {

    /**
     * Construct a new instance.
     * @param container The Java class which declares the field
     *                  holding the embedded object
     * @param embeddableClass The embeddable class
     * @param name The name of the field holding the embedded object
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The embeddable type
     * @param <C> The container type
     */
    @SafeVarargs
    public static <C,T> EmbeddedMapping<C,T> of(Class<C> container, Class<T> embeddableClass, String name, MemberMapping<T>... fields) {
        return new EmbeddedMapping<>(container, embeddableClass, name, fields);
    }

    /**
     * Construct a new instance.
     * @param embedded The metamodel object representing the embedded object
     * @param fields Mappings for fields or properties of the entity
     * @param <T> The embeddable type
     * @param <C> The container type
     */
    @SafeVarargs
    public static <C,T> EmbeddedMapping<C,T> of(SingularAttribute<C,T> embedded, MemberMapping<T>... fields) {
        return new EmbeddedMapping<>(embedded.getDeclaringType().getJavaType(), embedded.getJavaType(), embedded.getName(), fields);
    }
}