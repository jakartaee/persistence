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
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

/**
 * Represents an element of the from clause which may
 * function as the parent of Fetches.
 *
 * @param <Z>  the source type
 * @param <X>  the target type
 *
 * @since 2.0
 */
public interface FetchParent<Z, X> {

    /**
     * Return the fetch joins that have been made from this type.
     * Returns empty set if no fetch joins have been made from
     * this type.
     * Modifications to the set do not affect the query.
     * @return fetch joins made from this type
     */
    @Nonnull
    java.util.Set<Fetch<X, ?>> getFetches();

    /**
     * Create a fetch join to the specified single-valued attribute 
     * using an inner join.
     * @param attribute  target of the join
     * @param <Y> the type of the fetched attribute
     * @return the resulting fetch join
     */	
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull SingularAttribute<? super X, Y> attribute);

    /**
     * Create a fetch join to the specified single-valued attribute 
     * using the given join type.
     * @param attribute  target of the join
     * @param jt  join type
     * @param <Y> the type of the fetched attribute
     * @return the resulting fetch join
     */	
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull SingularAttribute<? super X, Y> attribute, @Nonnull JoinType jt);

    /**
     * Create a fetch join to the specified collection-valued 
     * attribute using an inner join. 
     * @param attribute  target of the join
     * @param <Y> the type of the fetched attribute
     * @return the resulting join
     */
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull PluralAttribute<? super X, ?, Y> attribute);
	
    /**
     * Create a fetch join to the specified collection-valued 
     * attribute using the given join type.
     * @param attribute  target of the join
     * @param jt  join type
     * @param <Y> the type of the fetched attribute
     * @return the resulting join
     */
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull PluralAttribute<? super X, ?, Y> attribute, @Nonnull JoinType jt);
	

    //String-based:
	
    /**
     * Create a fetch join to the specified attribute using an 
     * inner join.
     * @param attributeName the name of the attribute for the
     *        target of the join
     * @param <Y> the type of the fetched attribute
     * @return the resulting fetch join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #fetch(SingularAttribute)}
     *          or {@link #fetch(PluralAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */	
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull String attributeName);

    /**
     * Create a fetch join to the specified attribute using 
     * the given join type.
     * @param attributeName the name of the attribute that is the
     *        target of the join
     * @param jt the join type
     * @param <Y> the type of the fetched attribute
     * @return the resulting fetch join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #fetch(SingularAttribute, JoinType)}
     *          or {@link #fetch(PluralAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     */	
    @Nonnull
    <Y> Fetch<X, Y> fetch(@Nonnull String attributeName, @Nonnull JoinType jt);
}
