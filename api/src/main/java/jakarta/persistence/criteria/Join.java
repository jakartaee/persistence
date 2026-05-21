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

package jakarta.persistence.criteria;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.metamodel.Attribute;

import java.util.List;

/**
 * A join to an entity, embeddable, or basic type.
 *
 * @param <Z> the source type of the join
 * @param <X> the target type of the join
 *
 * @since 2.0
 */
public interface Join<Z, X> extends From<Z, X> {

    /**
     * Modify the join to restrict the result according to the
     * specified ON condition and return the join object.  
     * Replaces the previous ON condition, if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified join object
     * @since 2.1
     */
    @Nonnull
    Join<Z, X> on(@Nonnull Expression<Boolean> restriction);

    /**
     * Modify the join to restrict the result according to the
     * specified ON condition and return the join object.  
     * Replaces the previous ON condition, if any.
     * @param restrictions  zero or more restriction predicates
     * @return the modified join object
     * @since 2.1
     */
    @Nonnull
    Join<Z, X> on(@Nonnull BooleanExpression... restrictions);

    /**
     * Modify the join to restrict the result according to the
     * specified ON condition and return the join object.
     * Replaces the previous ON condition, if any.
     * @param restrictions  zero or more restriction predicates
     * @return the modified join object
     * @since 4.0
     */
    @Nonnull
    Join<Z, X> on(@Nonnull List<? extends Expression<Boolean>> restrictions);

    /**
     * Return the predicate that corresponds to the ON 
     * restriction(s) on the join, or null if no ON condition 
     * has been specified.
     * @return the ON restriction predicate
     * @since 2.1
     */
    @Nullable
    Predicate getOn();

    /**
     * Return the metamodel attribute representing the join
     * target, if any, or null if the target of the join is an
     * entity type.
     * @return metamodel attribute or null
     */
    @Nullable
    Attribute<? super Z, ?> getAttribute();

    /**
     * Return the parent of the join.
     * @return join parent
     */
    @Nonnull
    From<?, Z> getParent();

    /**
     * Return the join type.
     * @return join type
     */
    @Nonnull
    JoinType getJoinType();

    /**
     * Downcast the joined type to the given type.
     * @param type a subtype of the joined type
     * @param <T> the subtype of the element type
     * @return this join downcast to the given type
     * @since 4.0
     */
    @Override
    @Nonnull
    <T extends X> Join<Z, T> treat(@Nonnull Class<T> type);
}
