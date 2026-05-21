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

package jakarta.persistence.criteria;

import jakarta.annotation.Nonnull;
import jakarta.persistence.metamodel.EntityType;

/**
 * A root type in the from clause.
 * Query roots always reference entities.
 *
 * @param <X> the entity type referenced by the root
 *
 * @since 2.0
 */
public interface Root<X> extends From<X, X> {

    /**
     * Return the metamodel entity corresponding to the root.
     * @return metamodel entity corresponding to the root
     */
    @Override
    @Nonnull
    EntityType<X> getModel();

    /**
     * Downcast the root type to the given type.
     * @param type a subtype of the root type
     * @param <T> the subtype of the root type
     * @return this root downcast to the given type
     * @since 4.0
     */
    @Override
    @Nonnull
    <T extends X> Root<T> treat(@Nonnull Class<T> type);
}
