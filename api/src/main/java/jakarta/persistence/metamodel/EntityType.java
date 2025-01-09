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
//     Gavin King      - 4.0
//     Linda DeMichiel - 2.1
//     Linda DeMichiel - 2.0

package jakarta.persistence.metamodel;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.NamedEntityGraph;

import java.util.Map;

/**
 * An instance of {@code EntityType} represents
 * an {@linkplain jakarta.persistence.Entity entity}
 * type.
 *
 * @param <X> The represented entity type.
 *
 * @since 2.0
 */
public interface EntityType<X> 
            extends IdentifiableType<X>, Bindable<X>{

    /**
     * Return the entity name.
     * @return entity name
     */
    String getName();

    /**
     * A map keyed by {@linkplain NamedEntityGraph#name graph name}, containing
     * every named {@linkplain EntityGraph entity graph} whose root entity type
     * is this type.
     * @return a map keyed by graph name
     * @see jakarta.persistence.EntityManagerFactory#getNamedEntityGraphs(Class)
     *
     * @since 4.0
     */
    Map<String,EntityGraph<X>> getNamedEntityGraphs();
}
