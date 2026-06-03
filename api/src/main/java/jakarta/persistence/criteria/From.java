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
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SetAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a bound type, usually an entity that appears in
 * the from clause, but may also be an embeddable belonging to
 * an entity in the from clause. 
 * <p> Serves as a factory for {@link Join}s of associations,
 * embeddables, and collections belonging to the type, and for
 * {@link Path}s of attributes belonging to the type.
 *
 * @param <Z>  the source type
 * @param <X>  the target type
 *
 * @since 2.0
 */
public interface From<Z, X> extends Path<X>, FetchParent<Z, X> {

    /**
     * Return the joins originating from this bound type.
     * Modifying the contents of the returned set does not
     * affect the query.
     * @return joins originating from this type, or an
     *         empty set if no joins originate from this
     *         type
     */
    @Nonnull
    Set<Join<X, ?>> getJoins();

    /**
     * Return the joins originating from this bound type in
     * the order in which they were defined. If this object
     * represents a clause of a query written in the Jakarta
     * Persistence query language, the joins are returned
     * in the order they occur in the query. Modifying the
     * contents of the returned list does not affect the
     * query.
     * @return joins originating from this type, or an
     *         empty list if no joins originate from this
     *         type
     * @since 4.0
     */
    @Nonnull
    List<Join<X, ?>> getJoinList();

    /**
     * Whether the {@link From} object has been obtained as a result
     * of correlation (use of a {@link Subquery#correlate} method).
     * @return boolean indicating whether the object has been
     *         obtained through correlation
     */
    boolean isCorrelated();

    /**
     * Returns the parent {@link From} object from which the correlated
     * {@link From} object has been obtained through correlation (use
     * of {@link Subquery#correlate} method).
     * @return  the parent of the correlated {@code From} object
     * @throws IllegalStateException if the {@code From} object has
     *         not been obtained through correlation
     */
    @Nonnull
    From<Z, X> getCorrelationParent();

    /**
     * Create and add an inner join to the given entity.
     * @param entityClass  the target entity class
     * @param <Y> the target entity type
     * @return the resulting join
     * @since 3.2
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull Class<Y> entityClass);

    /**
     * Create and add a join to the given entity.
     * @param entityClass  the target entity class
     * @param joinType  join type
     * @param <Y> the target entity type
     * @return the resulting join
     * @since 3.2
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull Class<Y> entityClass,
                        @Nonnull JoinType joinType);

    /**
     * Create and add an inner join to the given entity.
     * @param entity  metamodel entity representing the join target
     * @param <Y> the target entity type
     * @return the resulting join
     * @since 3.2
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull EntityType<Y> entity);

    /**
     * Create and add a join to the given entity.
     * @param entity  metamodel entity representing the join target
     * @param joinType  join type
     * @param <Y> the target entity type
     * @return the resulting join
     * @since 3.2
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull EntityType<Y> entity,
                        @Nonnull JoinType joinType);

    /**
     * Create an inner join to the specified single-valued 
     * attribute.
     * @param attribute  target of the join
     * @param <Y> the type of the joined attribute
     * @return the resulting join
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull SingularAttribute<? super X, Y> attribute);

    /**
     * Create a join to the specified single-valued attribute 
     * using the given join type.
     * @param attribute  target of the join
     * @param jt  join type 
     * @param <Y> the type of the joined attribute
     * @return the resulting join
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull SingularAttribute<? super X, Y> attribute,
                        @Nonnull JoinType jt);

    /**
     * Create an inner join to the specified {@link Collection}-valued
     * attribute.
     * @param collection  target of the join
     * @param <Y> the element type of the joined collection
     * @return the resulting join
     */
    @Nonnull
    <Y> CollectionJoin<X, Y> join(@Nonnull CollectionAttribute<? super X, Y> collection);

    /**
     * Create an inner join to the specified {@link Set}-valued
     * attribute.
     * @param set  target of the join
     * @param <Y> the element type of the joined set
     * @return the resulting join
     */
    @Nonnull
    <Y> SetJoin<X, Y> join(@Nonnull SetAttribute<? super X, Y> set);

    /**
     * Create an inner join to the specified
     * {@link List}-valued attribute.
     * @param list  target of the join
     * @param <Y> the element type of the joined list
     * @return the resulting join
     */
    @Nonnull
    <Y> ListJoin<X, Y> join(@Nonnull ListAttribute<? super X, Y> list);

    /**
     * Create an inner join to the specified {@link Map}-valued
     * attribute.
     * @param map  target of the join
     * @param <K> the key type of the joined map
     * @param <V> the value type of the joined map
     * @return the resulting join
     */
    @Nonnull
    <K, V> MapJoin<X, K, V> join(@Nonnull MapAttribute<? super X, K, V> map);

    /**
     * Create a join to the specified {@link Collection}-valued
     * attribute using the given join type.
     * @param collection  target of the join
     * @param jt  join type 
     * @param <Y> the element type of the joined collection
     * @return the resulting join
     */
    @Nonnull
    <Y> CollectionJoin<X, Y> join(@Nonnull CollectionAttribute<? super X, Y> collection,
                                  @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link Set}-valued attribute
     * using the given join type.
     * @param set  target of the join
     * @param jt  join type 
     * @param <Y> the element type of the joined set
     * @return the resulting join
     */
    @Nonnull
    <Y> SetJoin<X, Y> join(@Nonnull SetAttribute<? super X, Y> set,
                           @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link List}-valued attribute
     * using the given join type.
     * @param list  target of the join
     * @param jt  join type 
     * @param <Y> the element type of the joined list
     * @return the resulting join
     */
    @Nonnull
    <Y> ListJoin<X, Y> join(@Nonnull ListAttribute<? super X, Y> list,
                            @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link Map}-valued attribute
     * using the given join type.
     * @param map  target of the join
     * @param jt  join type 
     * @param <K> the key type of the joined map
     * @param <V> the value type of the joined map
     * @return the resulting join
     */
    @Nonnull
    <K, V> MapJoin<X, K, V> join(@Nonnull MapAttribute<? super X, K, V> map,
                                 @Nonnull JoinType jt);


    //String-based:

    /**
     * Create an inner join to the specified attribute.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param <Y> the type of the joined attribute
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(SingularAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull String attributeName);

    /**
     * Create an inner join to the specified {@link Collection}-valued
     * attribute.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param <Y> the element type of the joined collection
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(CollectionAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> CollectionJoin<X, Y> joinCollection(@Nonnull String attributeName);

    /**
     * Create an inner join to the specified {@link Set}-valued
     * attribute.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param <Y> the element type of the joined set
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(SetAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> SetJoin<X, Y> joinSet(@Nonnull String attributeName);

    /**
     * Create an inner join to the specified {@link List}-valued
     * attribute.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param <Y> the element type of the joined list
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(ListAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> ListJoin<X, Y> joinList(@Nonnull String attributeName);
    
    /**
     * Create an inner join to the specified {@link Map}-valued
     * attribute.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param <K> the key type of the joined map
     * @param <V> the value type of the joined map
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(MapAttribute)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <K, V> MapJoin<X, K, V> joinMap(@Nonnull String attributeName);

    /**
     * Create a join to the specified attribute using the given
     * join type.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param jt the join type
     * @param <Y> the type of the joined attribute
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(SingularAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> Join<X, Y> join(@Nonnull String attributeName,
                        @Nonnull JoinType jt);
    
    /**
     * Create a join to the specified {@link Collection}-valued
     * attribute using the given join type.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param jt the join type
     * @param <Y> the element type of the joined collection
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(CollectionAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> CollectionJoin<X, Y> joinCollection(@Nonnull String attributeName,
                                            @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link Set}-valued attribute
     * using the given join type.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param jt the join type
     * @param <Y> the element type of the joined set
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(SetAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> SetJoin<X, Y> joinSet(@Nonnull String attributeName,
                              @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link List}-valued attribute
     * using the given join type.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param jt the join type
     * @param <Y> the element type of the joined list
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(ListAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     */
    @Nonnull
    <Y> ListJoin<X, Y> joinList(@Nonnull String attributeName,
                                @Nonnull JoinType jt);

    /**
     * Create a join to the specified {@link Map}-valued attribute
     * using the given join type.
     * @param attributeName the name of the attribute that is the
     *                      target of the join
     * @param jt the join type
     * @return the resulting join
     * @throws IllegalArgumentException if there is no attribute
     *         with the given name
     *
     * @apiNote This method accepts a string-valued attribute name,
     *          and lacks type safety compared to passing a static
     *          metamodel element to {@link #join(MapAttribute, JoinType)}.
     *          Use of the typesafe version is strongly preferred.
     *
     * @param <K> the key type of the joined map
     * @param <V> the value type of the joined map
     */
    @Nonnull
    <K, V> MapJoin<X, K, V> joinMap(@Nonnull String attributeName,
                                    @Nonnull JoinType jt);

    /**
     * Downcast the bound type to the given type.
     * @param type a subtype of the bound type
     * @param <T> the subtype of the bound type
     * @return this root or join downcast to the given type
     * @since 4.0
     */
    @Override
    @Nonnull
    <T extends X> From<?, T> treat(@Nonnull Class<T> type);
}
