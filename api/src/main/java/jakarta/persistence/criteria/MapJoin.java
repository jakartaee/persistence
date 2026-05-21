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
import java.util.Map;
import jakarta.persistence.metamodel.MapAttribute;

/**
 * The {@code MapJoin} interface is the type of the result of
 * joining to a collection over an association or element 
 * collection that has been specified as a {@link java.util.Map}.
 *
 * @param <Z> the source type of the join
 * @param <K> the type of the target Map key
 * @param <V> the type of the target Map value
 *
 * @since 2.0
 */
public interface MapJoin<Z, K, V> 
		extends PluralJoin<Z, Map<K, V>, V> {

    /**
     * Modify the join to restrict the result according to the
     * specified ON condition and return the join object.  
     * Replaces the previous ON condition, if any.
     * @param restriction  a simple or compound boolean expression
     * @return the modified join object
     * @since 2.1
     */
    @Nonnull
    MapJoin<Z, K, V> on(@Nonnull Expression<Boolean> restriction);

    /**
     * Modify the join to restrict the result according to the
     * specified ON condition and return the join object.  
     * Replaces the previous ON condition, if any.
     * @param restrictions  zero or more restriction predicates
     * @return the modified join object
     * @since 2.1
     */
    @Nonnull
    MapJoin<Z, K, V> on(@Nonnull BooleanExpression... restrictions);

    /**
     * Return the metamodel representation for the map attribute.
     * @return metamodel type representing the {@code Map} that is
     *         the target of the join
     */
    @Nonnull
    MapAttribute<? super Z, K, V> getModel();
    
    /**
     * Create a path expression that corresponds to the map key.
     * @return path corresponding to map key
     */
    @Nonnull
    Path<K> key();
    
    /**
     * Create a path expression that corresponds to the map value.
     * This method is for stylistic use only: it just returns this.
     * @return path corresponding to the map value
     */
    @Nonnull
    Path<V> value(); 
    
    /**
     * Create an expression that corresponds to the map entry.
     * @return expression corresponding to the map entry
     */
    @Nonnull
    Expression<Map.Entry<K, V>> entry();

    /**
     * Downcast the value type to the given type.
     * @param type a subtype of the value type
     * @param <T> the subtype of the value type
     * @return this join downcast to the given value type
     * @since 4.0
     */
    @Nonnull
    <T extends V> MapJoin<Z, K, T> treat(@Nonnull Class<T> type);
}
